package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.channel.ChannelList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelListRepository extends JpaRepository<ChannelList, Long> {

    Optional<ChannelList> findByNameAndParentId(String name, Long parentId);

}
