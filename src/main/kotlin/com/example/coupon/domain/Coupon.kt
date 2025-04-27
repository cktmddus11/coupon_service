package com.example.coupon.domain

import com.example.coupon.domain.base.BaseTimeEntity
import com.example.coupon.domain.enums.CouponStatus
import com.example.coupon.domain.enums.CouponType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "coupons")
class Coupon(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val code: String,

    @Column(nullable = false, length = 100)
    var name: String,

    @Column(length = 500)
    var description: String? = null, // nullable 타입

    @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
    @Column(nullable = false)
    val type: CouponType,

    @Column(name = "discount_amount")
    var discountAmount: BigDecimal? = null, // FIXED_AMOUNT 쿠폰에 사용

    @Column(name = "discount_rate")
    var discountRate: Double? = null, // PERCENTAGE 쿠폰에 사용

    @Column(name = "minimum_purchase_amount")
    var minimumPurchaseAmount: BigDecimal? = null, // 최소 구매 금액

    @Column(name = "start_date", nullable = false)
    val startDate: LocalDateTime,

    @Column(name = "end_date", nullable = false)
    val endDate: LocalDateTime,

    @Column(name = "max_issuance")
    var maxIssuance: Int? = null, // 최대 발급 수량 (null이면 무제한)

    @Column(name = "issued_count", nullable = false)
    var issuedCount: Int = 0, // 현재 발급된 수량

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: CouponStatus = CouponStatus.CREATED
) : BaseTimeEntity() {

    // 쿠폰 발급 가능 여부 확인 메서드
    fun isAvailableForIssue(): Boolean {
        val now = LocalDateTime.now()
        return status == CouponStatus.ACTIVE &&
                now.isAfter(startDate) &&
                now.isBefore(endDate) &&
                (maxIssuance == null || issuedCount < maxIssuance!!)
    }

    // 쿠폰 발급 시 발급 카운트 증가 메서드
    fun increaseIssuedCount() {
        issuedCount += 1

        // 최대 발급 수량에 도달하면 상태 변경
        if (maxIssuance != null && issuedCount >= maxIssuance!!) {
            status = CouponStatus.EXPIRED
        }
    }
}
/*
* 코틀린 특성 설명:

주 생성자(Primary Constructor): 코틀린 클래스는 클래스 헤더에 주 생성자를 정의합니다. 매개변수에 val/var을 사용하면 자동으로 필드로 선언됩니다.
기본값(Default Values): 매개변수에 기본값을 지정할 수 있어 선택적 매개변수 구현이 용이합니다.
Nullable 타입: String?과 같이 타입 뒤에 ?를 붙여 널 허용 타입을 선언합니다.
프로퍼티(Properties): 자동으로 getter/setter가 생성되며, val은 불변(읽기 전용), var은 가변(읽기/쓰기)입니다.
*
* */