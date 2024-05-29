package com.fx.funxtion.domain.help.faq.dto;

import com.fx.funxtion.domain.help.faq.entity.Faq;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FaqUpdateResponse {
    private Long id;
    private String faqTitle;
    private String faqContent;
    private Long faqOrder;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public FaqUpdateResponse(Faq faq) {
        BeanUtils.copyProperties(faq, this);}
}
