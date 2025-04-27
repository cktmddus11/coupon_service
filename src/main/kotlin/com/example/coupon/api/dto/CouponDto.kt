package com.example.coupon.api.dto

import com.example.coupon.domain.Coupon
import com.example.coupon.domain.enums.CouponStatus
import com.example.coupon.domain.enums.CouponType
import java.math.BigDecimal
import java.time.LocalDateTime

// 코틀린의 데이터 클래스는 equals, hashCode, toString을 자동 생성
data class CouponResponse(
    val id: Long,
    val code: String,
    val name: String,
    val description: String?,
    val type: CouponType,
    val discountAmount: BigDecimal?,
    val discountRate: Double?,
    val minimumPurchaseAmount: BigDecimal?,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val maxIssuance: Int?,
    val issuedCount: Int,
    val status: CouponStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    // 추가 필드: 발급 가능 여부
    val isAvailable: Boolean
){
    // 컴패니언 객체: 정적 메서드를 정의하는 코틀린 방식
    companion object {
        // 확장 함수를 사용해 Coupon 엔티티를 CouponResponse로 변환
        fun from(coupon: Coupon): CouponResponse {
            return CouponResponse(
                id = coupon.id,
                code = coupon.code,
                name = coupon.name,
                description = coupon.description,
                type = coupon.type,
                discountAmount = coupon.discountAmount,
                discountRate = coupon.discountRate,
                minimumPurchaseAmount = coupon.minimumPurchaseAmount,
                startDate = coupon.startDate,
                endDate = coupon.endDate,
                maxIssuance = coupon.maxIssuance,
                issuedCount = coupon.issuedCount,
                status = coupon.status,
                createdAt = coupon.createdAt,
                updatedAt = coupon.updatedAt,
                isAvailable = coupon.isAvailableForIssue()
            )
        }
    }
}
// 쿠폰 생성 요청 DTO
data class CouponCreateRequest(
    val code: String,
    val name: String,
    val description: String? = null,
    val type: CouponType,
    val discountAmount: BigDecimal? = null,
    val discountRate: Double? = null,
    val minimumPurchaseAmount: BigDecimal? = null,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val maxIssuance: Int? = null
) {
    // 요청 데이터의 유효성 검증
    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (name.isBlank()) {
            errors.add("쿠폰 이름은 필수입니다.")
        }

        if (startDate.isAfter(endDate)) {
            errors.add("시작일은 종료일보다 이전이어야 합니다.")
        }

        when (type) {
            CouponType.FIXED_AMOUNT -> {
                if (discountAmount == null || discountAmount <= BigDecimal.ZERO) {
                    errors.add("고정 금액 쿠폰은 할인 금액이 필요합니다.")
                }
            }
            CouponType.PERCENTAGE -> {
                if (discountRate == null || discountRate <= 0 || discountRate > 100) {
                    errors.add("비율 할인 쿠폰은 0~100 사이의 할인율이 필요합니다.")
                }
            }
            else -> {}
        }

        return errors
    }

    // DTO를 엔티티로 변환
    fun toEntity(): Coupon {
        return Coupon(
            code = code,
            name = name,
            description = description,
            type = type,
            discountAmount = discountAmount,
            discountRate = discountRate,
            minimumPurchaseAmount = minimumPurchaseAmount,
            startDate = startDate,
            endDate = endDate,
            maxIssuance = maxIssuance,
            status = CouponStatus.CREATED
        )
    }
}
// 쿠폰 수정 요청 DTO
data class CouponUpdateRequest(
    val name: String? = null,
    val description: String? = null,
    val minimumPurchaseAmount: BigDecimal? = null,
    val status: CouponStatus? = null
)
/*
* 데이터 클래스: data class는 equals(), hashCode(), toString(), copy() 메서드를 자동으로 생성합니다.
컴패니언 객체: Java의 static 멤버에 해당하는 기능을 제공합니다.
널 허용 타입: String?과 같이 null이 가능한 타입을 명시적으로 표현합니다.
when 표현식: Java의 switch문과 유사하지만 더 강력하며, 값을 반환할 수 있습니다.
mutableListOf: 변경 가능한 리스트를 생성하는 코틀린 표준 라이브러리 함수입니다.
*
* */