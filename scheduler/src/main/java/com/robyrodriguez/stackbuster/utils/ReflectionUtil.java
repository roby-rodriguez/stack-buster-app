package com.robyrodriguez.stackbuster.utils;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.WorkingQuestionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import javax.validation.constraints.NotNull;

public class ReflectionUtil {

    private ReflectionUtil() {
    }

    public static void updateField(Field field, Object bean, Object newValue) throws Exception {
        field.setAccessible(true);
        field.set(bean, newValue);
        field.setAccessible(false);
    }

    public static Object getQuestionsListener(@NotNull Class<?> qListenerClass, @NotNull Class<?> doClass,
            @NotNull Class<?> wqFactoryClass) throws Exception {
        Constructor<?> workingQuestionFactoryConstructor = wqFactoryClass.getConstructor();
        Object workingQuestionFactory = workingQuestionFactoryConstructor.newInstance();
        Constructor<?> listenerConstructor = qListenerClass.getConstructor(Class.class, WorkingQuestionFactory.class);
        return listenerConstructor.newInstance(doClass, workingQuestionFactory);
    }

    public static Object getWorkingQuestionsListener(@NotNull Class<?> wqListenerClass, @NotNull Class<?> doClass)
            throws Exception {
        Constructor<?> listenerConstructor = wqListenerClass.getConstructor(Class.class);
        return listenerConstructor.newInstance(doClass);
    }
}
