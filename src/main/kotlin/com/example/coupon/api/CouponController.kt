package com.example.coupon.api

import com.example.coupon.api.dto.CouponCreateRequest
import com.example.coupon.api.dto.CouponResponse
import com.example.coupon.api.dto.CouponUpdateRequest
import com.example.coupon.service.CouponService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/coupons")
@Tag(name = "쿠폰 관리", description = "쿠폰 CRUD API")
class CouponController(
    private val couponService: CouponService
) {

    @PostMapping
    @Operation(summary = "쿠폰 생성", description = "새로운 쿠폰을 생성합니다.")
    fun createCoupon(
        @Valid @RequestBody request: CouponCreateRequest
    ): ResponseEntity<CouponResponse> {
        val couponResponse = couponService.createCoupon(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(couponResponse)
    }

    @GetMapping("/{id}")
    @Operation(summary = "쿠폰 조회", description = "특정 쿠폰의 상세 정보를 조회합니다.")
    fun getCoupon(
        @PathVariable id: Long
    ): ResponseEntity<CouponResponse> {
        val couponResponse = couponService.getCoupon(id)
        return ResponseEntity.ok(couponResponse)
    }

    @GetMapping
    @Operation(summary = "쿠폰 목록 조회", description = "쿠폰 목록을 페이징하여 조회합니다.")
    fun getCoupons(
        @PageableDefault(size = 10, sort = ["id"]) pageable: Pageable
    ): ResponseEntity<Page<CouponResponse>> {
        val coupons = couponService.getCoupons(pageable)
        return ResponseEntity.ok(coupons)
    }

    @PutMapping("/{id}")
    @Operation(summary = "쿠폰 수정", description = "특정 쿠폰 정보를 수정합니다.")
    fun updateCoupon(
        @PathVariable id: Long,
        @Valid @RequestBody request: CouponUpdateRequest
    ): ResponseEntity<CouponResponse> {
        val updatedCoupon = couponService.updateCoupon(id, request)
        return ResponseEntity.ok(updatedCoupon)
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "쿠폰 활성화", description = "특정 쿠폰을 활성화합니다.")
    fun activateCoupon(
        @PathVariable id: Long
    ): ResponseEntity<CouponResponse> {
        val activatedCoupon = couponService.activateCoupon(id)
        return ResponseEntity.ok(activatedCoupon)
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "쿠폰 비활성화", description = "특정 쿠폰을 비활성화합니다.")
    fun deactivateCoupon(
        @PathVariable id: Long
    ): ResponseEntity<CouponResponse> {
        val deactivatedCoupon = couponService.deactivateCoupon(id)
        return ResponseEntity.ok(deactivatedCoupon)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "쿠폰 삭제", description = "특정 쿠폰을 삭제합니다.")
    fun deleteCoupon(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        couponService.deleteCoupon(id)
        return ResponseEntity.noContent().build()
    }
}
/*
* 함수 파라미터: 코틀린에서는 함수 파라미터에 애노테이션을 직접 붙일 수 있습니다.
함수 반환 타입: 함수 선언 뒤에 : 타입으로 반환 타입을 명시합니다.
ResponseEntity 사용: 코틀린에서도 Spring의 ResponseEntity를 사용하는 방식은 동일합니다.
*
* */