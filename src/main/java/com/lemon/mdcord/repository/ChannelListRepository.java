package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.channel.ChannelList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChannelListRepository extends JpaRepository<ChannelList, Long> {

    Optional<ChannelList> findByNameAndParentIdAndUseYn(String name, Long parentId, String useYn);
    List<ChannelList> findByParentIdAndUseYn(Long parentId, String useYn);
    List<ChannelList> findByIdIn(Set<Long> channelIds);
}
