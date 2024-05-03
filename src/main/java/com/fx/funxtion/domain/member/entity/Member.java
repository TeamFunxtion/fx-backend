package com.fx.funxtion.domain.member.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Table(name="members")
public class Member extends BaseEntity {
    private String email;
    @JsonIgnore
    private String password;
    private Long roleId;
    private String profileImageUrl;
    private String name;
    private String nickname;
    private String phoneNumber;
    private String intro;
    private int point;
    private String socialId;
    private String socialProvider;
    private String deleteYn;
    private String refreshToken;
}
