package com.fx.funxtion.domain.faq.entity;

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
@Table(name="site_faq")
public class Faq extends BaseEntity {
    private String faqTitle;
    private String faqContent;
    private String order;
}
