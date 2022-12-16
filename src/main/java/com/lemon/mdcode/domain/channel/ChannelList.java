package com.lemon.mdcode.domain.channel;

import com.lemon.mdcode.domain.BaseEntity;
import com.lemon.mdcode.domain.chat.ChannelChat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Table(name = "channel_list")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "use_yn")
    private String useYn;

    @OneToMany(mappedBy = "channelList")
    private List<ChannelChat> channelChats;

    @OneToMany(mappedBy = "channelList")
    private List<ChannelMember> channelMembers;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

}
