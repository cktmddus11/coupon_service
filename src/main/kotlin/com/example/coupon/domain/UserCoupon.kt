package com.example.coupon.domain

import com.example.coupon.domain.base.BaseTimeEntity
import com.example.coupon.domain.enums.UserCouponStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_coupons",
//    uniqueConstraints = [
//        UniqueConstraint(
//            name = "uk_user_coupon",
//            columnNames = ["user_id", "coupon_id"]
//        )
//    ]
)
class UserCoupon (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 설정
    @JoinColumn(name = "coupon_id", nullable = false)
    val coupon: Coupon,

    @Column(name = "issued_at", nullable = false)
    val issuedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "used_at")
    var usedAt: LocalDateTime? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: UserCouponStatus = UserCouponStatus.ISSUED
): BaseTimeEntity() {
    // 쿠폰 사용 처리 메서드
    fun use(): Boolean {
        if (status != UserCouponStatus.ISSUED) {
            return false
        }

        val now = LocalDateTime.now()
        if (now.isAfter(coupon.endDate)) {
            status = UserCouponStatus.EXPIRED
            return false
        }

        status = UserCouponStatus.USED
        usedAt = now
        return true
    }

    // 쿠폰 사용 취소 메서드
    fun cancel(): Boolean {
        if (status != UserCouponStatus.USED) {
            return false
        }

        status = UserCouponStatus.ISSUED
        usedAt = null
        return true
    }
}
/*
* 지연 로딩(Lazy Loading): fetch = FetchType.LAZY를 사용하여 연관된 엔티티를 필요할 때만 로딩합니다.
연관 관계(ManyToOne): JPA의 연관 관계를 정의하는 것은 Java와 유사하지만, 코틀린의 타입 시스템이 더 엄격합니다.
메서드 내 지역 변수: val now = LocalDateTime.now()와 같이 지역 변수를 선언하며, 타입 추론으로 타입을 명시하지 않아도 됩니다.
*
* */