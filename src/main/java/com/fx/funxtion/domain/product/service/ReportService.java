package com.fx.funxtion.domain.product.service;

import com.fx.funxtion.domain.product.entity.Report;
import com.fx.funxtion.domain.product.repository.ReportRepository;
import com.fx.funxtion.global.RsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;

    public RsData<Long> report(Long userId, Long productId, String reportTypeCode) {
        Optional<Report> findReport = reportRepository.findByUserIdAndProductId(userId, productId);
        if(findReport.isPresent()) {
            return RsData.of("500", "이미 신고 접수된 상품입니다.");
        }

        Report r = Report.builder()
                .reportType(reportTypeCode)
                .userId(userId)
                .productId(productId)
                .build();

        reportRepository.save(r);

        return RsData.of("200", "신고 접수 되었습니다!", r.getId());
    }
}
