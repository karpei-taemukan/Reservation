package com.zerobase.reservation.domain;

import com.zerobase.reservation.type.StoreReserveType;
import jakarta.persistence.*;
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
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Long sellerId;

    private String location;

    private String description;

    // 예약할 수 있는 지 확인
    /*
     * 예약 가능 : RESERVE_POSSIBLE
     * 예약 불가 : RESERVE_IMPOSSIBLE
     * */
    @Enumerated(EnumType.STRING)
    private StoreReserveType enableReserve;

    // 고객이 예약한 시각
    // 예약 있으면 LocalDateTime --> RESERVE_IMPOSSIBLE
    // 예약 없으면 null --> RESERVE_POSSIBLE


}