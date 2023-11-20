package com.example.controllerexample.util;

import org.assertj.core.util.Lists;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String username() default "username";

    String name() default "sh";

    String[] authorities() default {"ROLE_USER"};
}
