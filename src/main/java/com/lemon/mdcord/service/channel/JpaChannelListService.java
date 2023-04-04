package com.lemon.mdcord.service.channel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.lemon.mdcord.common.exception.ChannelAlreadyDisabledException;
import com.lemon.mdcord.common.exception.ChannelCantDeleteException;
import com.lemon.mdcord.common.exception.ChannelListDuplicatedException;
import com.lemon.mdcord.common.exception.ChannelNotFoundException;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.domain.channel.ChannelMember;
import com.lemon.mdcord.domain.member.MemberRole;
import com.lemon.mdcord.dto.channel.list.*;
import com.lemon.mdcord.dto.socket.ChannelCreateRequest;
import com.lemon.mdcord.dto.socket.MessageType;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.ChannelMemberRepository;
import com.lemon.mdcord.service.MessageTypeInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JpaChannelListService implements ChannelListService, MessageTypeInterface {

    private final ChannelListRepository channelListRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ObjectMapper objectMapper;
    private static final String USE_Y = "Y";
    private static final String USE_N = "N";

    @Override
    public boolean support(String messageType) {
        return MessageType.CREATE_CHANNEL.name().equals(messageType);
    }

    /**
     * 생성된 채널 dept에 따른 채널 생성 소켓 처리
     *
     * dept 0 : 다른 사람들이 알 필요 없음. 신규 생성 루트 채널(서버 채널)이므로 존재하는 사용자가 없기 때문
     * dept 1 : 생성된 채널의 dept 0에 속한 사람들이 알아야 함
     * dept 2 : 생성된 채널의 dept 0에 속한 사람들이 알아야 함 + 생성된 채널을 소켓 목록에 추가하고, 이용 중인 사용자를 담아야 함
     */
    @Override
    public void handle(String messageType, String payload, Map<Long, List<WebSocketSession>> channelMap) throws IOException {
        ChannelCreateRequest request = new Gson().fromJson(payload, ChannelCreateRequest.class);

        // 1일 때는 소켓에 대한 것 x -> 처리 x
        // 2일 때는 소켓에 대한 설정 o
        Integer createdChannelDept = request.getChannelDept();

        if(createdChannelDept.equals(2)) {
            // 루트 채널(서버 채널) 아이디 받음
            Long serverId = request.getServerId();

            // 루트 채널 아이디로 dept 1 채널 목록 찾음
            Set<Long> parentChannelIds = this.findByParentId(Set.of(serverId)).stream()
                    .map(o -> o.getId())
                    .collect(Collectors.toSet());

            // dept 1 채널 목록의 id 값들로 dept 2 채널 목록 아이디를 찾음. 그런데 생성된 채널 id는 빼고.
            Long anyTargetChannel = this.findByParentId(parentChannelIds).stream()
                    .filter(o -> o.getId() != request.getChannelId())
                    .map(o -> o.getId())
                    .findAny()
                    .get();

            if(anyTargetChannel != null) {
                channelMap.put(request.getChannelId(), channelMap.get(anyTargetChannel));
            }
        }

        ChannelList result = this.getTargetChannel(request.getChannelId());

        // 메시지 소켓 통신
        List<WebSocketSession> webSocketSessions = channelMap.get(request.getChannelId());
        TextMessage modifiedMessage = modifiedMessage(payload, result);
        for(WebSocketSession webSocketSession : webSocketSessions) {
            webSocketSession.sendMessage(modifiedMessage);
        }
    }

    @Override
    public ChannelListCreateResponse createChannel(final ChannelListCreateRequest dto) {
        Optional<ChannelList> checkDuplicated = channelListRepository.findByNameAndParentId(dto.getName(), dto.getParentId());
        if(checkDuplicated.isPresent()) throw new ChannelListDuplicatedException(dto.getName());

        int channelOrder = dto.getChannelOrder() == null ? 1 : dto.getChannelOrder();
        channelListRepository.increaseChannelOrderGreaterThanEqual(channelOrder);

        ChannelList channelList = ChannelList.builder()
                .name(dto.getName())
                .parentId(dto.getParentId())
                .dept(dto.getDept())
                .channelOrder(channelOrder)
                .createBy(getAuthentication().getName())
                .build();

        return new ChannelListCreateResponse(channelListRepository.save(channelList));
    }

    @Override
    public MultipleChannelListResponse fetchChannels() {
        String memberRole = getAuthentication().getAuthorities().toString();

        List<ChannelList> channelLists = new ArrayList<>();
        channelLists = createChannelListByMemberRole(memberRole, channelLists);

        if(channelLists.size() == 0) {
            return MultipleChannelListResponse.builder().channelCount(0).build();
        }

        List<ChannelListResponse> channelListResponses = channelLists.stream()
                .map(channelList -> toChannelListResponse(channelList))
                .collect(Collectors.toList());

        return MultipleChannelListResponse.builder()
                .channelLists(channelListResponses)
                .channelCount(channelListResponses.size())
                .build();
    }

    @Override
    public void deleteChannel(Long id) {
        ChannelList channel = getTargetChannel(id);
        if(channel.getUseYn().equals(USE_N)) throw new ChannelAlreadyDisabledException();

        List<ChannelList> childChannels = channelListRepository.findByParentIdAndUseYn(channel.getId(), USE_Y);
        if(!childChannels.isEmpty()) throw new ChannelCantDeleteException();

        channel.disable(getAuthentication().getName());
    }

    @Override
    public ChannelListUpdateResponse updateChannelInfo(ChannelListUpdateRequest dto) {
        ChannelList channel = getTargetChannel(dto.getId());
        channel.updateChannelInfo(
                dto.getChannelName(), dto.getChannelOrder(),
                dto.getUseYn(), getAuthentication().getName()
        );

        return new ChannelListUpdateResponse(channel);
    }

    @Override
    public void updateChannelOrder(List<ChannelListOrderUpdateRequest> list) {
        Map<Long, ChannelListOrderUpdateRequest> dtoListToMap = list.stream()
                .collect(Collectors.toMap(
                        o1 -> o1.getId(),
                        o2 -> o2
        ));

        Set<Long> channelIds = dtoListToMap.keySet();
        List<ChannelList> targetChannelList = channelListRepository.findByIdIn(channelIds);

        for(ChannelList cl : targetChannelList) {
            cl.updateChannelOrder(
                    dtoListToMap.get(cl.getId()).getParentId()
                    , dtoListToMap.get(cl.getId()).getChannelOrder()
                    , getAuthentication().getName()
            );
        }
    }

    @Override
    public List<ChannelList> findByParentId(Set<Long> channelIds) {
        return channelListRepository.findByParentIdIn(channelIds);
    }

    private List<ChannelList> createChannelListByMemberRole(String memberRole, List<ChannelList> channelLists) {
        if(memberRole.contains(MemberRole.ADMIN.toString())) {
            channelLists = channelListRepository.findAll();
        }
        else if(memberRole.contains(MemberRole.USER.toString())) {
            getUserRoleChannelList(channelLists);
        }
        return channelLists;
    }

    private void getUserRoleChannelList(List<ChannelList> channelLists) {
        String memberId = getAuthentication().getName();
        List<ChannelMember> channelMembers = channelMemberRepository.findByMemberId(memberId);
        Set<Long> joinedDept0ChannelId = channelMembers.stream()
                .map(o -> o.getChannelList().getId())
                .collect(Collectors.toSet());

        List<ChannelList> joinedDept0Channel = channelListRepository.findByIdInAndUseYn(joinedDept0ChannelId, USE_Y);

        List<ChannelList> joinedDept1Channel = channelListRepository.findByParentIdInAndUseYn(joinedDept0ChannelId, USE_Y);
        Set<Long> joinedDept1ChannelId = joinedDept1Channel.stream()
                .map(o -> o.getId())
                .collect(Collectors.toSet());

        List<ChannelList> joinedDept2Channel = channelListRepository.findByParentIdInAndUseYn(joinedDept1ChannelId, USE_Y);

        channelLists.addAll(joinedDept0Channel);
        channelLists.addAll(joinedDept1Channel);
        channelLists.addAll(joinedDept2Channel);
    }

    private ChannelList getTargetChannel(Long id) {
        return channelListRepository.findById(id).orElseThrow(() -> new ChannelNotFoundException(id));
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    private ChannelListResponse toChannelListResponse(ChannelList channelList) {
        return ChannelListResponse.builder()
                    .id(channelList.getId())
                    .name(channelList.getName())
                    .dept(channelList.getDept())
                    .parentId(channelList.getParentId())
                    .channelOrder(channelList.getChannelOrder())
                    .useYn(channelList.getUseYn())
                    .build();
    }

    /**
     * message 정보에 채널 ID, 채널명, 채널 순서
     *
     * @param payload
     * @param channelList
     * @return
     * @throws JsonProcessingException
     */
    private TextMessage modifiedMessage(String payload, ChannelList channelList) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);

        ((ObjectNode)jsonNode).put("channelId", channelList.getId());
        ((ObjectNode)jsonNode).put("channelName", channelList.getName());
        ((ObjectNode)jsonNode).put("channelOrder", channelList.getChannelOrder());

        return new TextMessage(objectMapper.writeValueAsString(jsonNode));
    }

}
