package com.example.coupon.repository

import com.example.coupon.domain.UserCoupon
import com.example.coupon.domain.enums.UserCouponStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserCouponRepository : JpaRepository<UserCoupon, Long> {

    // 사용자가 보유한 쿠폰 목록 조회
    fun findByUserIdAndStatus(userId: Long, status: UserCouponStatus): List<UserCoupon>

    // 사용자가 특정 쿠폰을 보유하고 있는지 확인
    fun existsByUserIdAndCouponId(userId: Long, couponId: Long): Boolean

    // 특정 사용자가 보유한 사용 가능한 쿠폰 개수 조회
    @Query("""
        SELECT COUNT(uc) FROM UserCoupon uc
        JOIN uc.coupon c
        WHERE uc.userId = :userId
        AND uc.status = com.example.coupon.domain.enums.UserCouponStatus.ISSUED
        AND c.endDate >= CURRENT_TIMESTAMP
    """)
    fun countAvailableCouponsByUserId(userId: Long): Long

    fun countByCouponIdAndStatusIn(couponId: Long,
                                   statuses: List<UserCouponStatus>): Long
}
/*
* 함수 선언: 코틀린에서는 fun 키워드로 함수를 선언합니다.
Nullable 타입 반환: Coupon?는 반환값이 null일 수 있음을 명시합니다.
파라미터에 기본값 지정: 필요한 경우 함수 파라미터에 기본값을 지정할 수 있습니다.
문자열 템플릿: 코틀린에서는 """로 감싸진 문자열에서는 이스케이프 문자 없이 줄바꿈과 특수문자를 그대로 사용할 수 있습니다.
*
* */