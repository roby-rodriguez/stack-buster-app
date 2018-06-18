package com.robyrodriguez.stackbuster.annotation;

import com.robyrodriguez.stackbuster.annotation.StackBusterListener.ListenerType;
import com.robyrodriguez.stackbuster.service.listener.QuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.UserQuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.WorkingQuestionsListener;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.CompositeUserWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.CompositeWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.FlatWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.InheritedUserWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.InheritedWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.utils.ReflectionUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.constraints.NotNull;

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

    interface ListenerFactory {
        Object create() throws Exception;
    }

    enum ListenerLookup implements ListenerFactory {
        FQL(StructureType.FLAT, ListenerType.QUESTION, () -> ReflectionUtil.getQuestionsListener(
                QuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.QuestionDO.class,
                FlatWorkingQuestionFactory.class
        )),
        FUQL(StructureType.FLAT, ListenerType.USER_QUESTION, () -> ReflectionUtil.getQuestionsListener(
                UserQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.QuestionDO.class,
                FlatWorkingQuestionFactory.class
        )),
        FWQL(StructureType.FLAT, ListenerType.WORKING_QUESTION, () -> ReflectionUtil.getWorkingQuestionsListener(
                WorkingQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.WorkingQuestionDO.class
        )),
        FUWQL(StructureType.FLAT, ListenerType.WORKING_USER_QUESTION, () -> ReflectionUtil.getWorkingQuestionsListener(
                WorkingQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.WorkingQuestionDO.class
        )),

        CQL(StructureType.COMPOSITION, ListenerType.QUESTION, () -> ReflectionUtil.getQuestionsListener(
                QuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.QuestionDO.class,
                CompositeWorkingQuestionFactory.class
        )),
        CUQL(StructureType.COMPOSITION, ListenerType.USER_QUESTION, () -> ReflectionUtil.getQuestionsListener(
                UserQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserQuestionDO.class,
                CompositeUserWorkingQuestionFactory.class
        )),
        CWQL(StructureType.COMPOSITION, ListenerType.WORKING_QUESTION, () -> ReflectionUtil.getWorkingQuestionsListener(
                WorkingQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.WorkingQuestionDO.class
        )),
        CUWQL(StructureType.COMPOSITION, ListenerType.WORKING_USER_QUESTION, () -> ReflectionUtil.getWorkingQuestionsListener(
                WorkingQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserWorkingQuestionDO.class
        )),

        IQL(StructureType.INHERITANCE, ListenerType.QUESTION, () -> ReflectionUtil.getQuestionsListener(
                QuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.QuestionDO.class,
                InheritedWorkingQuestionFactory.class
        )),
        IUQL(StructureType.INHERITANCE, ListenerType.USER_QUESTION, () -> ReflectionUtil.getQuestionsListener(
                UserQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.UserQuestionDO.class,
                InheritedUserWorkingQuestionFactory.class
        )),
        IWQL(StructureType.INHERITANCE, ListenerType.WORKING_QUESTION, () -> ReflectionUtil.getWorkingQuestionsListener(
                WorkingQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.WorkingQuestionDO.class
        )),
        IUWQL(StructureType.INHERITANCE, ListenerType.WORKING_USER_QUESTION, () -> ReflectionUtil.getWorkingQuestionsListener(
                WorkingQuestionsListener.class,
                com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.UserWorkingQuestionDO.class
        ));

        private StructureType structureType;
        private ListenerType listenerType;
        private ListenerFactory listenerFactory;

        ListenerLookup(StructureType structureType, ListenerType listenerType, ListenerFactory listenerFactory) {
            this.structureType = structureType;
            this.listenerType = listenerType;
            this.listenerFactory = listenerFactory;
        }

        @Override
        public Object create() throws Exception {
            return listenerFactory.create();
        }

        public static ListenerLookup fromType(@NotNull StructureType structureType, @NotNull ListenerType listenerType) {
            for (ListenerLookup listenerLookup : ListenerLookup.values()) {
                if (listenerLookup.structureType == structureType && listenerLookup.listenerType == listenerType)
                    return listenerLookup;
            }
            return null;
        }
    }

    enum StructureType {
        FLAT,
        COMPOSITION,
        INHERITANCE;
    }

    StructureType structureType() default StructureType.COMPOSITION;
}
