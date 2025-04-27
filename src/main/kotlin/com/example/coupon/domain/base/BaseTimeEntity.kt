package com.example.coupon.domain.base

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass // 이 클래스의 필드가 자식 엔티티의 컬럼으로 포함되도록 함
@EntityListeners(AuditingEntityListener::class) // JPA Auditing 기능을 사용하기 위한 리스너
abstract class BaseTimeEntity {

    @CreatedDate // 엔티티 생성 시 자동으로 시간 설정
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set // 코틀린에서 필드의 접근 제어자를 설정하는 방식

    @LastModifiedDate // 엔티티 수정 시 자동으로 시간 업데이트
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set
}