package com.example.coupon.exception

// 기본 예외 클래스
open class CouponException(
    override val message: String,
    val errorCode: String = "COUPON_ERROR"
) : RuntimeException(message)

// 구체적인 예외 클래스들
class CouponNotFoundException(id: Long) :
    CouponException("쿠폰을 찾을 수 없습니다: $id", "COUPON_NOT_FOUND")

class DuplicateCouponCodeException(code: String) :
    CouponException("이미 사용 중인 쿠폰 코드입니다: $code", "DUPLICATE_COUPON_CODE")

class CouponValidationException(val errors: List<String>) :
    CouponException("쿠폰 유효성 검사 실패: ${errors.joinToString(", ")}", "COUPON_VALIDATION_ERROR")

class CouponIssuanceException(override val message: String) :
    CouponException(message, "COUPON_ISSUANCE_ERROR")

/*
*
* 상속: 클래스 이름 뒤에 콜론(:)을 사용하여 상속을 표현합니다.
기본 생성자 매개변수 override: 부모 클래스의 속성을 재정의할 때 override 키워드를 사용합니다.
문자열 템플릿: "쿠폰을 찾을 수 없습니다: $id"와 같이 문자열 내에 변수를 직접 사용할 수 있습니다.
*
*  */

