package com.example.coupon.service.impl

import com.example.coupon.api.dto.CouponCreateRequest
import com.example.coupon.api.dto.CouponResponse
import com.example.coupon.api.dto.CouponUpdateRequest
import com.example.coupon.domain.enums.CouponStatus
import com.example.coupon.domain.enums.UserCouponStatus
import com.example.coupon.exception.CouponNotFoundException
import com.example.coupon.exception.CouponValidationException
import com.example.coupon.exception.DuplicateCouponCodeException
import com.example.coupon.repository.CouponRepository
import com.example.coupon.repository.UserCouponRepository
import com.example.coupon.service.CouponService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponServiceImpl(
    private val couponRepository: CouponRepository,
    private val userCouponRepository: UserCouponRepository
) : CouponService {

    @Transactional
    override fun createCoupon(request: CouponCreateRequest): CouponResponse {
        // 유효성 검증
        val validationErrors = request.validate()
        if (validationErrors.isNotEmpty()) {
            throw CouponValidationException(validationErrors)
        }

        // 중복 코드 검사
        couponRepository.findByCode(request.code)?.let {
            throw DuplicateCouponCodeException(request.code)
        }

        val coupon = request.toEntity()
        val savedCoupon = couponRepository.save(coupon)

        return CouponResponse.from(savedCoupon)
    }

    @Transactional(readOnly = true)
    override fun getCoupon(id: Long): CouponResponse {
        val coupon = couponRepository.findById(id).orElseThrow {
            CouponNotFoundException(id)
        }

        return CouponResponse.from(coupon)
    }

    @Transactional(readOnly = true)
    override fun getCoupons(pageable: Pageable): Page<CouponResponse> {
        return couponRepository.findAll(pageable).map { coupon ->
            CouponResponse.from(coupon)
        }
    }

    @Transactional
    override fun updateCoupon(id: Long, request: CouponUpdateRequest): CouponResponse {
        val coupon = couponRepository.findById(id).orElseThrow {
            CouponNotFoundException(id)
        }

        // 코틀린의 안전 호출 연산자 '?.'와 엘비스 연산자 '?:'를 활용
        // 요청값에 값이 없으면 DB에서 조회한 값으로 대체
        request.name?.let { coupon.name = it }
        request.description?.let { coupon.description = it }
        request.minimumPurchaseAmount?.let { coupon.minimumPurchaseAmount = it }
        request.status?.let { coupon.status = it }

        val updatedCoupon = couponRepository.save(coupon)
        return CouponResponse.from(updatedCoupon)
    }

    @Transactional
    override fun activateCoupon(id: Long): CouponResponse {
        val coupon = couponRepository.findById(id).orElseThrow {
            CouponNotFoundException(id)
        }

        coupon.status = CouponStatus.ACTIVE
        val updatedCoupon = couponRepository.save(coupon)

        return CouponResponse.from(updatedCoupon)
    }

    @Transactional
    override fun deactivateCoupon(id: Long): CouponResponse {
        val coupon = couponRepository.findById(id).orElseThrow {
            CouponNotFoundException(id)
        }

        coupon.status = CouponStatus.DISABLED
        val updatedCoupon = couponRepository.save(coupon)

        return CouponResponse.from(updatedCoupon)
    }

    @Transactional
    override fun deleteCoupon(id: Long) {
        val coupon = couponRepository.findById(id).orElseThrow {
            CouponNotFoundException(id)
        }

        // 이미 발급된 쿠폰이 있는지 확인 (사용되지 않은 상태의 쿠폰 수 조회)
        val issuedCount = userCouponRepository.countByCouponIdAndStatusIn(
            couponId = id,
            statuses = listOf(UserCouponStatus.ISSUED, UserCouponStatus.USED)
        )

        if (issuedCount > 0) {
            // 발급된 쿠폰이 있으면 삭제 대신 비활성화
            coupon.status = CouponStatus.DISABLED
            couponRepository.save(coupon)
        } else {
            // 발급된 쿠폰이 없으면 삭제
            couponRepository.delete(coupon)
        }
    }
}
/*
*
* 생성자 주입: 코틀린은 기본 생성자에서 바로 의존성 주입을 할 수 있습니다.
안전 호출 연산자 (?.): 객체가 null이 아닐 때만 메서드를 호출하거나 속성에 접근합니다.
스코프 함수 (let): 특정 객체를 컨텍스트 내에서 사용할 수 있는 함수입니다.
엘비스 연산자 (?:): 왼쪽 표현식이 null이면 오른쪽 값을 반환합니다.
강제 언래핑 (!!): null이 아님을 단언하며, null이면 NPE가 발생합니다.
*
* */