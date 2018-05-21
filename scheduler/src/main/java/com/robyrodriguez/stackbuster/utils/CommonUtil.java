package com.robyrodriguez.stackbuster.utils;

/**
 * General purpose utility
 */
public final class CommonUtil {

    private static final String EMPTY = "0%";
    public static final String COMPLETED = "100%";

    private CommonUtil() {
    }

    /**
     * 133/300 -> "44.33%"
     *
     * @param count so far
     * @param total total
     * @return za parsentigi
     */
    public static String getCompletionPercentage(int count, int total) {
        if (count == 0)
            return EMPTY;
        if (count < total)
            return Math.round((count / (float) total) * 10000) / 100.0 + "%";
        return CommonUtil.COMPLETED;
    }
}
