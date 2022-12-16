package com.lemon.mdcode.repository;

import com.lemon.mdcode.domain.channel.ChannelList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelListRepository extends JpaRepository<ChannelList, Long> {

    Optional<ChannelList> findByNameAndParentId(String name, Long parentId);

}
