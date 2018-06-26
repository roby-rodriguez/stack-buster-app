package com.robyrodriguez.stackbuster.database;

import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.annotation.StackBusterData.StructureType;
import com.robyrodriguez.stackbuster.constants.DatabasePaths;
import com.robyrodriguez.stackbuster.service.listener.DefaultQuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.QuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.UserQuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.WorkingQuestionsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Firebase database wrapper managing connections
 */
@Component
public class DatabaseManager {

    @Value("${firebase.database.type}")
    private StructureType structureType;

    @Autowired
    private DatabasePaths paths;

    @Autowired
    private FirebaseDatabase database;

    public void registerListener(QuestionsListener listener) {
        if (StructureType.FLAT.equals(structureType)) {
            this.database.getReference(paths.defaultResolver().root("questions")).addChildEventListener(listener);
        }
    }

    public void registerListener(DefaultQuestionsListener listener) {
        if (!StructureType.FLAT.equals(structureType)) {
            this.database.getReference(paths.defaultResolver().root("questions")).addChildEventListener(listener);
        }
    }

    public void registerListener(UserQuestionsListener listener) {
        if (!StructureType.FLAT.equals(structureType)) {
            this.database.getReference(paths.userResolver().root("questions")).addChildEventListener(listener);
        }
    }

    public void registerListeners(WorkingQuestionsListener defaultWorkingQuestionsListener, WorkingQuestionsListener userWorkingQuestionsListener) {
        this.database.getReference(paths.defaultResolver().root("workingQuestions")).addChildEventListener(defaultWorkingQuestionsListener);
        if (!StructureType.FLAT.equals(structureType)) {
            this.database.getReference(paths.userResolver().root("workingQuestions")).addChildEventListener(userWorkingQuestionsListener);
        }
    }
}
