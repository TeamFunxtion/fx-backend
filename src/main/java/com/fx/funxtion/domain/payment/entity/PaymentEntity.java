package com.fx.funxtion.domain.payment.entity;

import com.fx.funxtion.domain.payment.dto.PaymentDto;
import com.fx.funxtion.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="payments", indexes={@Index(name= "impuid_index", columnList = "impuid", unique = true)}) // impuid 인덱스 생성
public class PaymentEntity extends BaseEntity {

    @Column
    private String impUid;

    @Column
    private String email;

    @Column
    private String status;

    @Column
    private Long amount;

    public PaymentEntity(PaymentDto dto) {
        BeanUtils.copyProperties(dto, this);
    }
}
