package com.lemon.mdcord.domain.channel;

import com.lemon.mdcord.domain.BaseEntity;
import com.lemon.mdcord.domain.chat.ChannelChat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "channel_list")
@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long id;

    @NotBlank
    @Column(name = "channel_name", nullable = false, length = 50)
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "use_yn", nullable = false, length = 1)
    @ColumnDefault("'Y'")
    private String useYn;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "dept", length = 1)
    private Integer dept;

    @Column(name = "channel_order", nullable = false, length = 1)
    private Integer channelOrder;

    @OneToMany(mappedBy = "parentId")
    private List<ChannelList> child = new ArrayList<>();

    @OneToMany(mappedBy = "channelList")
    private List<ChannelChat> channelChats;

    @OneToMany(mappedBy = "channelList")
    private List<ChannelMember> channelMembers;

    @Builder
    public ChannelList(String name, Long parentId, Integer dept, Integer channelOrder, String createBy) {
        Assert.state(StringUtils.isNotBlank(name), "name may not be blank");
        Assert.state(dept != null, "dept may not be blank");
        Assert.state(channelOrder != null, "channelOrder may not be blank");
        Assert.state(StringUtils.isNotBlank(createBy), "createBy may not be blank");

        this.name = name;
        this.parentId = parentId;
        this.dept = dept;
        this.channelOrder = channelOrder;
        super.setCreateBy(createBy);
    }
}
