package com.fx.funxtion.domain.product.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
@Table(name="user_favorites" , uniqueConstraints = {
        @UniqueConstraint(
                columnNames={"user_id", "product_id"}
        )
})
public class Favorite {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

}
