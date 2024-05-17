package com.fx.funxtion.domain.qna.service;

import com.fx.funxtion.domain.member.repository.MemberRepository;

import com.fx.funxtion.domain.qna.dto.QnaCreateRequest;
import com.fx.funxtion.domain.qna.dto.QnaCreateResponse;
import com.fx.funxtion.domain.qna.dto.QnaDto;
import com.fx.funxtion.domain.qna.entity.Qna;
import com.fx.funxtion.domain.qna.repository.QnaRepository;
import com.fx.funxtion.global.RsData.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;

    public RsData<QnaCreateResponse> createQna(QnaCreateRequest qnaCreateRequest) {

        Qna qna = Qna.builder()
                .userId(qnaCreateRequest.getUserId())
                .categoryId(qnaCreateRequest.getCategoryId())
                .qnaTitle(qnaCreateRequest.getQnaTitle())
                .qnaContent(qnaCreateRequest.getQnaContent())
                .build();

        qnaRepository.save(qna);

        Optional<Qna> optionalQna = qnaRepository.findById(qna.getId());
        
        return optionalQna.map(q -> RsData.of("200","1:1문의 등록 성공",new QnaCreateResponse(q)))
                .orElseGet(() -> RsData.of("500","1:1 문의 등록 실패"));
    }


    @Transactional
    public Page<QnaDto> getSelectPage(Long userId,Pageable pageable, int pageNo, int pageSize){
        pageable = PageRequest.of(pageNo,pageSize, Sort.by(Sort.Direction.DESC,"id"));
        Page<QnaDto> list = qnaRepository.findByUserId(userId,pageable).map(QnaDto::new);

        return list;
    }
}
