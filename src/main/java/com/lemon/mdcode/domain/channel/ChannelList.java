package com.lemon.mdcode.domain.channel;

import com.lemon.mdcode.domain.BaseEntity;
import com.lemon.mdcode.domain.chat.ChannelChat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @NotBlank
    @Column(name = "use_yn", nullable = false)
    @ColumnDefault("'Y'")
    private String useYn;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ChannelList parent;

    @OneToMany(mappedBy = "parent")
    private List<ChannelList> child = new ArrayList<>();

    @OneToMany(mappedBy = "channelList")
    private List<ChannelChat> channelChats;

    @OneToMany(mappedBy = "channelList")
    private List<ChannelMember> channelMembers;
}
