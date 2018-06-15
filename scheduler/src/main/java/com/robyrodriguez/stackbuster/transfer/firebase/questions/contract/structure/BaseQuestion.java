package com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure;

import com.robyrodriguez.stackbuster.types.BadgeType;

public interface BaseQuestion {
    String getId();
    void setId(String pId);
    String getUser_id();
    void setUser_id(String user_id);
    BadgeType getBadgeType();
}
