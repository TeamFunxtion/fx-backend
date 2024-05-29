package com.fx.funxtion.domain.help.faq.dto;

import com.fx.funxtion.domain.help.faq.entity.Faq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
public class FaqDto {
    private long id;
    private String faqTitle;
    private String faqContent;
    private Long faqOrder;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public FaqDto(Faq faq){
        this.id = faq.getId();
        this.faqTitle = faq.getFaqTitle();
        this.faqContent = faq.getFaqContent();
        this.faqOrder = faq.getFaqOrder();
        this.createDate = faq.getCreateDate();
        this.updateDate = faq.getUpdateDate();
    }
}
