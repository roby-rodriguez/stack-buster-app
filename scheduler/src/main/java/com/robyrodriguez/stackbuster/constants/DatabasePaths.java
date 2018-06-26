package com.robyrodriguez.stackbuster.constants;

import com.robyrodriguez.stackbuster.annotation.StackBusterData.StructureType;
import com.robyrodriguez.stackbuster.service.listener.DefaultQuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.UserQuestionsListener;
import com.robyrodriguez.stackbuster.service.worker.strategy.DefaultQuestionStrategy;
import com.robyrodriguez.stackbuster.service.worker.strategy.UserQuestionStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

/**
 * Container for Firebase database paths resolution
 */
@Component
public final class DatabasePaths {

    private static final String DEFAULT = "default";
    private static final String USER = "user";

    @Value("${firebase.database.type}")
    private StructureType structureType;

    private PathResolver userPathResolver;
    private PathResolver defaultPathResolver;

    @PostConstruct
    public void init() {
        if (StructureType.FLAT.equals(structureType)) {
            defaultPathResolver = userPathResolver = new PathResolver(null);
        } else {
            defaultPathResolver = new PathResolver(DEFAULT);
            userPathResolver = new PathResolver(USER);
        }
    }

    public String users(String id) {
        return "/users/" + id;
    }

    public String usersActiveQuestions(String id) {
        return "/users/" + id + "/activeQuestions";
    }

    public String history(String id) {
        return "/history/" + id;
    }

    public PathResolver resolve(DefaultQuestionsListener listener) {
        return defaultPathResolver;
    }

    public PathResolver resolve(UserQuestionsListener listener) {
        return userPathResolver;
    }

    public PathResolver resolve(DefaultQuestionStrategy strategy) {
        return defaultPathResolver;
    }

    public PathResolver resolve(UserQuestionStrategy strategy) {
        return userPathResolver;
    }

    public PathResolver defaultResolver() {
        return defaultPathResolver;
    }

    public PathResolver userResolver() {
        return userPathResolver;
    }

    public class PathResolver {

        private String root;

        public PathResolver(String root) {
            this.root = root;
        }

        public String root(@NotNull String base) {
            if (root != null) {
                return "/" + base + "/" + root;
            }
            return "/" + base;
        }

        public String questions(String id) {
            return root("questions") + "/" + id;
        }

        public String questionsCompleted(String id) {
            return questions(id) + "/completed";
        }

        public String questionsProgress(String id) {
            return questions(id) + "/progress";
        }

        public String workingQuestions(String id) {
            return root("workingQuestions") + "/" + id;
        }
    }
}
