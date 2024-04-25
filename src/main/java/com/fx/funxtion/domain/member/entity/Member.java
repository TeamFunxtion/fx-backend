package com.fx.funxtion.domain.member.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    private String email;
    @JsonIgnore
    private String password;
    private String refreshToken;
}
