package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.channel.ChannelList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ChannelListRepository extends JpaRepository<ChannelList, Long> {

    ChannelList findByNameAndParentIdAndUseYn(String name, Long parentId, String useYn);
    List<ChannelList> findByParentIdAndUseYn(Long parentId, String useYn);
    List<ChannelList> findByIdIn(Set<Long> channelIds);

    @Modifying
    @Query("update ChannelList cl set cl.channelOrder = cl.channelOrder +1 where cl.channelOrder >= :order")
    void increaseChannelOrderGreaterThanEqual(@Param("order") Integer order);
}
