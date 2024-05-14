package com.fx.funxtion.domain.qna.entity;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

@Table(name="qna_log")
public class Qna extends BaseEntity {
    @ManyToOne
    @JoinColumn(name= "store_id")
    private Member member;
    private String qnaTitle;
    private String qnaContent;
}
