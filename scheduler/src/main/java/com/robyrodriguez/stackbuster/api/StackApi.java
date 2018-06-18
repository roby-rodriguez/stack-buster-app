package com.robyrodriguez.stackbuster.api;

/**
 * StackOverflow API URLs
 */
public final class StackApi {
    public static String QUESTION(String id) {
        return String.format("https://api.stackexchange"
                + ".com/2.2/questions/%s?order=desc&sort=activity&site=stackoverflow", id);
    }
    public static String USER(String id) {
        return String.format("https://api.stackexchange"
                + ".com/2.2/users/%s?order=asc&sort=reputation&site=stackoverflow", id);
    }
    public static String QUESTION_LINK(String qid, String uid) {
        if (uid == null)
            return String.format("https://stackoverflow.com/q/%s", qid);
        return String.format("https://stackoverflow.com/q/%s/%s", qid, uid);
    }
    public static String IVC_LINK(String ivcPath) {
        return String.format("https://stackoverflow.com%s?_=%d", ivcPath, System.currentTimeMillis());
    }
}
