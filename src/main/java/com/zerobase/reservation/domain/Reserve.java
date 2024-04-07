package com.zerobase.reservation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Reserve extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;
    private String customerName;
    // 예약할 날짜
    private String reserveDate;
    // 예약할 시간
    private String reserveTime;
}
