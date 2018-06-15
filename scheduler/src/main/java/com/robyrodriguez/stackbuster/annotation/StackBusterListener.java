package com.robyrodriguez.stackbuster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StackBusterListener {
    enum ListenerType {
        QUESTION,
        USER_QUESTION,
        WORKING_QUESTION,
        WORKING_USER_QUESTION,
    }
    ListenerType type();
}
