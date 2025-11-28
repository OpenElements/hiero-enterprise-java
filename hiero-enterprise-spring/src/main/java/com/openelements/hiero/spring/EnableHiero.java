package com.openelements.hiero.spring;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.openelements.hiero.spring.implementation.HieroAutoConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Enables Hiero support.
 *
 * <p>Using this annotation will import the necessary configuration to enable Hiero support.
 */
@Target(TYPE)
@Retention(RUNTIME)
@Import(HieroAutoConfiguration.class)
@Documented
public @interface EnableHiero {}
