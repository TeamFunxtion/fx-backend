package com.fx.funxtion.domain.follow.entity;

import com.fx.funxtion.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString
@Table(name="user_follows" , uniqueConstraints = {
        @UniqueConstraint(
                columnNames={"from_id", "to_id"}
        )
})
@EntityListeners(AuditingEntityListener.class)
public class UserFollows {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="from_id")
    private Member fromMember;

    @OneToOne
    @JoinColumn(name="to_id")
    private Member toMember;
}
