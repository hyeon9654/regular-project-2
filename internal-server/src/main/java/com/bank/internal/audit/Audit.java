package com.bank.internal.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 붙이지 않아도 모든 컨트롤러는 자동으로 기록된다.
// 붙이면 행위 이름이 "클래스.메서드" 대신 여기 적은 이름으로 남는다.
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {
    String action();
}
