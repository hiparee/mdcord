package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.common.exception.ChannelAlreadyDisabledException;
import com.lemon.mdcord.common.exception.ChannelCantDeleteException;
import com.lemon.mdcord.common.exception.ChannelListDuplicatedException;
import com.lemon.mdcord.common.exception.ChannelNotFoundException;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.dto.channel.list.*;
import com.lemon.mdcord.repository.ChannelListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaChannelListService implements ChannelListService {

    private final ChannelListRepository channelListRepository;
    private final String USE_Y = "Y";
    private final String USE_N = "N";

    @Override
    @Transactional
    public ChannelList createChannel(final ChannelListCreateRequest dto) {
        ChannelList checkDuplicated = channelListRepository.findByNameAndParentIdAndUseYn(dto.getName(), dto.getParentId(), USE_Y);
        if(checkDuplicated != null) throw new ChannelListDuplicatedException(dto.getName());

        int channelOrder = dto.getChannelOrder() == null ? 1 : dto.getChannelOrder();
        channelListRepository.increaseChannelOrderGreaterThanEqual(channelOrder);

        ChannelList channelList = ChannelList.builder()
                .name(dto.getName())
                .parentId(dto.getParentId())
                .dept(dto.getDept())
                .channelOrder(channelOrder)
                .createBy(getAuthentication().getName())
                .build();

        return channelListRepository.save(channelList);
    }

    @Override
    public MultipleChannelListResponse fetchChannels() {
        List<ChannelList> channelLists = channelListRepository.findAll();

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
    @Transactional
    public void deleteChannel(Long id) {
        ChannelList channel = getTargetChannel(id);
        if(channel.getUseYn().equals(USE_N)) throw new ChannelAlreadyDisabledException();

        List<ChannelList> childChannels = channelListRepository.findByParentIdAndUseYn(channel.getId(), USE_Y);
        if(!childChannels.isEmpty()) throw new ChannelCantDeleteException();

        channel.disable(getAuthentication().getName());
    }

    @Override
    @Transactional
    public ChannelList updateChannelInfo(ChannelListUpdateRequest dto) {
        ChannelList channel = getTargetChannel(dto.getId());
        channel.updateChannelInfo(
                dto.getChannelName(), dto.getChannelOrder(),
                dto.getUseYn(), getAuthentication().getName()
        );

        return channel;
    }

    @Override
    @Transactional
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
