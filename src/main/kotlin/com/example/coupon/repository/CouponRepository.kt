package com.example.coupon.repository

import com.example.coupon.domain.Coupon
import com.example.coupon.domain.enums.CouponStatus
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface CouponRepository : JpaRepository<Coupon, Long> {
    fun findByCode(code: String): Coupon?

    // 활성화된 쿠폰 목록 조회
    fun findByStatusAndStartDateBeforeAndEndDateAfter(
        status: CouponStatus,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<Coupon>

    @Query("""
        SELECT c FROM Coupon c
        WHERE c.status = :status
        AND c.startDate <= :now
        AND c.endDate >= :now
        AND (c.maxIssuance IS NULL OR c.issuedCount < c.maxIssuance)
    """)
    fun findAvailableCoupons(
        @Param("status") status: CouponStatus,
        @Param("now") now: LocalDateTime
    ): List<Coupon>
}