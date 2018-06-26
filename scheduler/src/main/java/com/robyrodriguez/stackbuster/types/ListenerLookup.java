package com.robyrodriguez.stackbuster.types;

import com.robyrodriguez.stackbuster.annotation.StackBusterData.StructureType;
import com.robyrodriguez.stackbuster.annotation.StackBusterListener.ListenerType;
import com.robyrodriguez.stackbuster.service.listener.DefaultQuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.QuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.UserQuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.WorkingQuestionsListener;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.DefaultQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.CompositeUserWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.CompositeWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.FlatWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.InheritedUserWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.InheritedWorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.QuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.WorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.DefaultWorkingQuestionDO;
import com.robyrodriguez.stackbuster.utils.ReflectionUtil;

import javax.validation.constraints.NotNull;

interface ListenerFactory {
    Object create() throws Exception;
}

public enum ListenerLookup implements ListenerFactory {
    FQL(StructureType.FLAT, ListenerType.QUESTION, () -> ReflectionUtil.getQuestionsListener(
            QuestionsListener.class
    )),
    FDQL(StructureType.FLAT, ListenerType.QUESTION_DEFAULT, () -> ReflectionUtil.getQuestionsListener(
            DefaultQuestionsListener.class,
            QuestionDO.class,
            FlatWorkingQuestionFactory.class
    )),
    FUQL(StructureType.FLAT, ListenerType.QUESTION_USER, () -> ReflectionUtil.getQuestionsListener(
            UserQuestionsListener.class,
            QuestionDO.class,
            FlatWorkingQuestionFactory.class
    )),
    FDWQL(StructureType.FLAT, ListenerType.WORKING_QUESTION_DEFAULT, () -> ReflectionUtil.getWorkingQuestionsListener(
            WorkingQuestionsListener.class,
            WorkingQuestionDO.class
    )),
    FUWQL(StructureType.FLAT, ListenerType.WORKING_QUESTION_USER, () -> null),

    CQL(StructureType.COMPOSITION, ListenerType.QUESTION, () -> null),
    CDQL(StructureType.COMPOSITION, ListenerType.QUESTION_DEFAULT, () -> ReflectionUtil.getQuestionsListener(
            DefaultQuestionsListener.class,
            DefaultQuestionDO.class,
            CompositeWorkingQuestionFactory.class
    )),
    CUQL(StructureType.COMPOSITION, ListenerType.QUESTION_USER, () -> ReflectionUtil.getQuestionsListener(
            UserQuestionsListener.class,
            com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserQuestionDO.class,
            CompositeUserWorkingQuestionFactory.class
    )),
    CDWQL(StructureType.COMPOSITION, ListenerType.WORKING_QUESTION_DEFAULT, () -> ReflectionUtil.getWorkingQuestionsListener(
            WorkingQuestionsListener.class,
            com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.DefaultWorkingQuestionDO.class
    )),
    CUWQL(StructureType.COMPOSITION, ListenerType.WORKING_QUESTION_USER, () -> ReflectionUtil.getWorkingQuestionsListener(
            WorkingQuestionsListener.class,
            com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserWorkingQuestionDO.class
    )),

    IQL(StructureType.INHERITANCE, ListenerType.QUESTION, () -> null),
    IDQL(StructureType.INHERITANCE, ListenerType.QUESTION_DEFAULT, () -> ReflectionUtil.getQuestionsListener(
            DefaultQuestionsListener.class,
            com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.DefaultQuestionDO.class,
            InheritedWorkingQuestionFactory.class
    )),
    IUQL(StructureType.INHERITANCE, ListenerType.QUESTION_USER, () -> ReflectionUtil.getQuestionsListener(
            UserQuestionsListener.class,
            com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.UserQuestionDO.class,
            InheritedUserWorkingQuestionFactory.class
    )),
    IDWQL(StructureType.INHERITANCE, ListenerType.WORKING_QUESTION_DEFAULT, () -> ReflectionUtil.getWorkingQuestionsListener(
            WorkingQuestionsListener.class,
            DefaultWorkingQuestionDO.class
    )),
    IUWQL(StructureType.INHERITANCE, ListenerType.WORKING_QUESTION_USER, () -> ReflectionUtil.getWorkingQuestionsListener(
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
