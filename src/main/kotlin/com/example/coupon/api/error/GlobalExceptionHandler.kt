package com.example.coupon.api.error

import com.example.coupon.exception.CouponException
import com.example.coupon.exception.CouponNotFoundException
import com.example.coupon.exception.CouponValidationException
import com.example.coupon.exception.DuplicateCouponCodeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

// 공통 에러 응답 형식
data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val code: String,
    val message: String
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CouponNotFoundException::class)
    fun handleCouponNotFoundException(ex: CouponNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.name,
            code = ex.errorCode,
            message = ex.message
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(DuplicateCouponCodeException::class)
    fun handleDuplicateCouponCodeException(ex: DuplicateCouponCodeException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = HttpStatus.CONFLICT.name,
            code = ex.errorCode,
            message = ex.message
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    @ExceptionHandler(CouponValidationException::class)
    fun handleCouponValidationException(ex: CouponValidationException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            code = ex.errorCode,
            message = ex.message
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(CouponException::class)
    fun handleCouponException(ex: CouponException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.name,
            code = ex.errorCode,
            message = ex.message
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        // 필드 에러 메시지를 모아서 반환
        val errorMessage = ex.bindingResult.fieldErrors
            .joinToString(", ") { fieldError: FieldError ->
                "${fieldError.field}: ${fieldError.defaultMessage}"
            }

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.name,
            code = "VALIDATION_ERROR",
            message = errorMessage
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.name,
            code = "INTERNAL_SERVER_ERROR",
            message = ex.message ?: "서버 내부 오류가 발생했습니다."
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
/*
 클래스 참조: CouponNotFoundException::class와 같이 클래스에 대한 참조를 표현합니다.
엘비스 연산자: ex.message ?: "서버 내부 오류가 발생했습니다."에서 메시지가 null이면 기본 메시지를 사용합니다.
고차 함수: joinToString에 람다 함수를 전달하여 각 필드 에러를 문자열로 변환합니다.
 *
 */