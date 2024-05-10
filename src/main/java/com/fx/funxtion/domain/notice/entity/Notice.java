package com.fx.funxtion.domain.notice.entity;


import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name="site_notice")
public class Notice extends BaseEntity {


    private String noticeTitle;
    private String noticeContent;

}
