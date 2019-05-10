package com.project.EhrRoute.Security;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;


@Documented // are to be documented by javadoc
@AuthenticationPrincipal // Resolves Authentication.getPrincipal() to inject the authenticated user
@Retention(RetentionPolicy.RUNTIME) // Runtime retention
@Target({ElementType.PARAMETER, ElementType.TYPE}) // The contexts in which an annotation type is applicable
public @interface CurrentUser {
}
