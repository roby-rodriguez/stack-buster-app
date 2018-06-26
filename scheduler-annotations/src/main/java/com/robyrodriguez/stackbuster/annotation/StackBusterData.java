package com.robyrodriguez.stackbuster.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows switching between data structure types to store working question data:
 *
 * - flat based (uses same DO for all working questions)
 * - inheritance based (
 * - composition based (a somewhat more flexible alternative)
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface StackBusterData {
    enum StructureType {
        FLAT,
        COMPOSITION,
        INHERITANCE;
    }
    StructureType structureType() default StructureType.COMPOSITION;
}
