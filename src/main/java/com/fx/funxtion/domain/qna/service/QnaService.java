package com.fx.funxtion.domain.qna.service;

import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;
import com.fx.funxtion.domain.product.dto.ProductCreateRequest;
import com.fx.funxtion.domain.qna.dto.QnaCreateRequest;
import com.fx.funxtion.domain.qna.dto.QnaCreateResponse;
import com.fx.funxtion.domain.qna.repository.QnaRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class QnaService {
    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    public RsData<QnaCreateResponse> createQna(QnaCreateRequest qnaCreateRequest) {
        //Member member = memberRepository.findById(qnaCreateRequest.getMember());

    }

}
