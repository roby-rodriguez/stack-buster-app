package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.ChildEventListener;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseQuestion;

/**
 * Wrapper interface
 */
public interface FirebaseListener<Q extends BaseQuestion> extends ChildEventListener {
    void process(Q question);
}
