package com.fx.funxtion.domain.faq.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqCreateRequest {
    private String faqTitle;
    private String faqContent;

}
