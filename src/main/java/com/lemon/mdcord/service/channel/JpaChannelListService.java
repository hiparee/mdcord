package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.common.exception.ChannelAlreadyDisabledException;
import com.lemon.mdcord.common.exception.ChannelCantDeleteException;
import com.lemon.mdcord.common.exception.ChannelListDuplicatedException;
import com.lemon.mdcord.common.exception.ChannelNotFoundException;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.domain.channel.ChannelMember;
import com.lemon.mdcord.domain.member.MemberRole;
import com.lemon.mdcord.dto.channel.list.*;
import com.lemon.mdcord.repository.ChannelListRepository;
import com.lemon.mdcord.repository.ChannelMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class JpaChannelListService implements ChannelListService {

    private final ChannelListRepository channelListRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final String USE_Y = "Y";
    private final String USE_N = "N";

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

    private static Authentication getAuthentication() {
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

}
