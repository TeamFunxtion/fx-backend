package com.fx.funxtion.domain.help.qna.service;

import com.fx.funxtion.domain.help.qna.dto.*;
import com.fx.funxtion.domain.help.qna.entity.Qna;
import com.fx.funxtion.domain.member.entity.Member;
import com.fx.funxtion.domain.member.repository.MemberRepository;

import com.fx.funxtion.domain.help.qna.repository.QnaRepository;
import com.fx.funxtion.global.RsData.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaService {
    private final QnaRepository qnaRepository;
    private final MemberRepository memberRepository;

    public RsData<QnaCreateResponse> createQna(QnaCreateRequest qnaCreateRequest) throws Exception {
        Optional<Member> optionalMember = memberRepository.findById(qnaCreateRequest.getUserId());
        if (optionalMember.isEmpty()) {
            return RsData.of("500", "유효하지 않은 사용자입니다");
        }
        Member member = optionalMember.get();

        Qna qna = Qna.builder()
                .member(member)
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
    public Page<QnaDto> getSelectPage(Long userId, Pageable pageable, int pageNo, int pageSize) throws Exception {
        Optional<Member> member = memberRepository.findById(userId);
        pageable = PageRequest.of(pageNo,pageSize, Sort.by(Sort.Direction.DESC,"id"));
        Page<QnaDto> list = qnaRepository.findByMember(member.get(),pageable).map(QnaDto::new);
        return list;
    }

    @Transactional
    public Page<QnaDto> getSelectManagerPage( Pageable pageable, int pageNo, int pageSize,String ch) throws Exception {
        pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "id"));

        if (ch.equals("all")) {
            Page<QnaDto> list = qnaRepository.findAllBy(pageable).map(QnaDto::new);
            return list;
        } else {
            Page<QnaDto> list = qnaRepository.findByQnaAnswerNull(pageable).map(QnaDto::new);
            return list;
        }
    }

    public RsData<QnaUpdateResponse> updateQna(QnaUpdateRequest qnaUpdateRequest) throws Exception {
        Optional<Qna> optionalQna = qnaRepository.findById(qnaUpdateRequest.getId());
        if(optionalQna.isEmpty()){
            return RsData.of("500","1:1 문의가 존재하지않습니다");
        }
        Qna q = optionalQna.get();

        if(qnaUpdateRequest.getQnaAnswer() != null && !qnaUpdateRequest.getQnaAnswer().isEmpty()){
            q.setQnaAnswer(qnaUpdateRequest.getQnaAnswer());
        } else {
            return RsData.of("500","내용이 없습니다");
        }
        qnaRepository.save(q);
        return RsData.of("200","1:1문의 답변이 등록되었습니다.",new QnaUpdateResponse(q));
    }
}
