package com.fx.funxtion.domain.favorites.entity;

import com.fx.funxtion.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
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
@ToString
@Table(name="user_favorites" , uniqueConstraints = {
        @UniqueConstraint(
                columnNames={"user_id", "product_id"}
        )
})
@EntityListeners(AuditingEntityListener.class)
public class UserFavorites {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

}
