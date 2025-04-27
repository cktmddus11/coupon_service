package com.example.coupon.service

import com.example.coupon.api.dto.CouponCreateRequest
import com.example.coupon.api.dto.CouponResponse
import com.example.coupon.api.dto.CouponUpdateRequest
import com.example.coupon.domain.Coupon
import com.example.coupon.domain.enums.CouponStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

// 서비스 인터페이스
interface CouponService {
    fun createCoupon(request: CouponCreateRequest): CouponResponse
    fun getCoupon(id: Long): CouponResponse
    fun getCoupons(pageable: Pageable): Page<CouponResponse>
    fun updateCoupon(id: Long, request: CouponUpdateRequest): CouponResponse
    fun activateCoupon(id: Long): CouponResponse
    fun deactivateCoupon(id: Long): CouponResponse
    fun deleteCoupon(id: Long)
}