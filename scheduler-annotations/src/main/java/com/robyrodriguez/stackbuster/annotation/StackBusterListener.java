package com.robyrodriguez.stackbuster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks field as listener @Component for autowiring
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StackBusterListener {
    enum ListenerType {
        QUESTION,
        QUESTION_DEFAULT,
        QUESTION_USER,
        WORKING_QUESTION_DEFAULT,
        WORKING_QUESTION_USER,
    }
    ListenerType type() default ListenerType.QUESTION;
}
