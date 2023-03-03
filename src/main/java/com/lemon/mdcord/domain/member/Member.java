package com.lemon.mdcord.domain.member;

import com.lemon.mdcord.common.exception.PasswordNotMachedException;
import com.lemon.mdcord.domain.BaseEntity;
import com.lemon.mdcord.domain.channel.ChannelMember;
import com.lemon.mdcord.dto.member.MemberPasswordEncoder;
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
@Table(name = "member")
@Entity
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private String id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @NotBlank
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    // TODO - 별도의 테이블이 없으면 String이 낫지않을려나? 물어보기
    @Column(name = "icon_file_id")
    private Integer iconFileId;

    @Column(name = "use_yn", nullable = false, length = 1)
    @ColumnDefault("'Y'")
    private String useYn;

    // TODO - ERD 반영 필요
    @Column(name = "type", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_by")
    private String updateBy;

    @OneToMany(mappedBy = "member")
    private List<ChannelMember> channelMembers = new ArrayList<>();

    @Builder
    public Member(String id, String name, String password, Integer iconFileId, MemberPasswordEncoder passwordEncoder, String createBy) {
        Assert.state(StringUtils.isNotBlank(id), "id may not be blank");
        Assert.state(StringUtils.isNotBlank(password), "password may not be blank");
        Assert.state(passwordEncoder != null, "passwordEncoder may not be null");

        this.id = id;
        this.name = name;
        this.iconFileId = iconFileId;
        this.password = this.encodePassword(password, passwordEncoder);
        this.memberRole = MemberRole.USER;
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
