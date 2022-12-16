package com.lemon.mdcode.service.channel;

import com.lemon.mdcode.common.exception.ChannelListDuplicatedException;
import com.lemon.mdcode.domain.channel.ChannelList;
import com.lemon.mdcode.dto.channel.ChannelListCreateRequest;
import com.lemon.mdcode.dto.channel.ChannelListResponse;
import com.lemon.mdcode.dto.channel.MultipleChannelListResponse;
import com.lemon.mdcode.repository.ChannelListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultChannelListService implements ChannelListService {

    private final ChannelListRepository channelListRepository;

    @Override
    @Transactional
    public ChannelList createChannelList(final ChannelListCreateRequest dto) {
        Optional<ChannelList> checkDuplicated = channelListRepository.findByNameAndParentId(dto.getName(), dto.getParentId());

        if(checkDuplicated.isPresent()) {
            throw new ChannelListDuplicatedException(dto.getName());
        }

        ChannelList channelList = ChannelList.builder()
                .name(dto.getName())
                .parentId(dto.getParentId())
                .createBy(dto.getCreateBy())
                .build();

        return channelListRepository.save(channelList);
    }

    @Override
    public MultipleChannelListResponse fetchChannelList() {
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

    private ChannelListResponse toChannelListResponse(ChannelList channelList) {
        return ChannelListResponse.builder()
                    .name(channelList.getName())
                    .parentId(channelList.getParentId())
                    .useYn(channelList.getUseYn())
                    .build();
    }

}
