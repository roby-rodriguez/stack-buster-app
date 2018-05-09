package com.robyrodriguez.stackbuster.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String utility
 */
public final class StringUtil {

    private StringUtil() {}

    /**
     * Extracts ivc URL path from html source
     *
     * @param html source to scrap
     * @return ivc URL path
     */
    public static String extractIVC(String html) {
        String result = null;
        Pattern pattern = Pattern.compile("StackExchange\\.ready\\(function\\(\\)\\{\\$\\.get\\('(.+?)'\\);}\\);");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    /**
     * Checks if URL is a shared question link by some user
     * i.e. format `https://stackoverflow.com/q/{qid}/{uid}`
     *
     * @param url shared question link
     * @return true if valid format
     */
    public static boolean isSharedQuestionLink(String url) {
        // TODO implement
        return false;
    }
}
