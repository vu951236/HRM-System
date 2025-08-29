package com.example.hrm.systemlog;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggableAction {
    String action();
    String description() default "";
}
