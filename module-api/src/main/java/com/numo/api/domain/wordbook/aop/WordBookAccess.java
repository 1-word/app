package com.numo.api.domain.wordbook.aop;

import com.numo.domain.wordbook.WordBookRole;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WordBookAccess {
    WordBookRole value() default WordBookRole.admin;
}
