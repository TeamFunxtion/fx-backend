    package com.fx.funxtion.domain.qna.entity;


    import com.fx.funxtion.domain.member.entity.Member;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import lombok.experimental.SuperBuilder;
    import org.springframework.data.annotation.CreatedDate;
    import org.springframework.data.jpa.domain.support.AuditingEntityListener;

    import java.time.LocalDateTime;

    @Entity
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    @EntityListeners(AuditingEntityListener.class)
    @Table(name="qna_log")
    public class Qna  {
        @Id
        @Column(updatable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String categoryId;

        @OneToOne
        @JoinColumn(name = "user_id")
        private Member member;

        private String qnaTitle;

        private String qnaContent;

        private String qnaAnswer;
        @CreatedDate
        @Column(updatable = false)
        private LocalDateTime createDate;

    }
