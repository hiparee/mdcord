package com.lemon.mdcode.domain.member;

import com.lemon.mdcode.common.exception.PasswordNotMachedException;
import com.lemon.mdcode.domain.BaseEntity;
import com.lemon.mdcode.domain.channel.ChannelMember;
import com.lemon.mdcode.domain.chat.ChannelChat;
import com.lemon.mdcode.dto.member.MemberPasswordEncoder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "member")
@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "icon_file_id")
    private Integer iconFileId;

    @OneToMany(mappedBy = "member")
    private List<ChannelChat> channelChats = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<ChannelMember> channelMembers = new ArrayList<>();

    @Column(name = "use_yn")
    @ColumnDefault("'Y'")
    private String useYn;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @Builder
    public Member(String id, String name, String password, MemberPasswordEncoder passwordEncoder, String createBy) {
        Assert.state(StringUtils.isNotBlank(id), "id may not be blank");
        Assert.state(StringUtils.isNotBlank(password), "password may not be blank");
        Assert.state(passwordEncoder != null, "passwordEncoder may not be null");

        this.id = id;
        this.name = name;
        this.password = this.encodePassword(password, passwordEncoder);
        super.setCreateBy(createBy);
    }

    public String encodePassword(String password, MemberPasswordEncoder passwordEncoder) {
        return passwordEncoder.encode(password);
    }

    public void checkPassword(String password, MemberPasswordEncoder passwordEncoder) {
        if(!passwordEncoder.matches(password, this.password)) {
            throw new PasswordNotMachedException(this.getId());
        }
    }

    public void updateMemberInfo(String name, String password, MemberPasswordEncoder passwordEncoder, Integer iconFileId, String useYn, String updateBy) {
        this.name = name;
        if(StringUtils.isNotBlank(password)) {
            this.password = this.encodePassword(password, passwordEncoder);
        }
        this.iconFileId = iconFileId;
        this.useYn = useYn;
        this.updateBy = updateBy;
        this.updateDate = LocalDateTime.now();
    }

}
