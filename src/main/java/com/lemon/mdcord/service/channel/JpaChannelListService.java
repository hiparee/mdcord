package com.lemon.mdcord.service.channel;

import com.lemon.mdcord.common.exception.ChannelListDuplicatedException;
import com.lemon.mdcord.domain.channel.ChannelList;
import com.lemon.mdcord.dto.channel.ChannelListCreateRequest;
import com.lemon.mdcord.dto.channel.ChannelListResponse;
import com.lemon.mdcord.dto.channel.MultipleChannelListResponse;
import com.lemon.mdcord.repository.ChannelListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaChannelListService implements ChannelListService {

    private final ChannelListRepository channelListRepository;

    @Override
    @Transactional
    public ChannelList createChannel(final ChannelListCreateRequest dto) {
        Optional<ChannelList> checkDuplicated = channelListRepository.findByNameAndParentIdAndUseYn(dto.getName(), dto.getParentId(), "Y");

        if(checkDuplicated.isPresent()) {
            throw new ChannelListDuplicatedException(dto.getName());
        }

        String currentMemberId = getAuthentication().getName();

        ChannelList channelList = ChannelList.builder()
                .name(dto.getName())
                .parentId(dto.getParentId())
                .dept(dto.getDept())
                .channelOrder(dto.getChannelOrder() == null ? 0 : dto.getChannelOrder())
                .createBy(currentMemberId)
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
