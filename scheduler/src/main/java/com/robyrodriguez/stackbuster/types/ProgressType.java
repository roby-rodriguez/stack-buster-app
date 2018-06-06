package com.robyrodriguez.stackbuster.types;

public enum ProgressType {
    /** scheduler task completed for this question */
    COMPLETED,
    /** scheduler task still in progress for this question */
    IN_PROGRESS,
    /** question question by user */
    DELETED,
    /** scheduler task aborted for this question */
    ABORTED;

    public static boolean isPending(ProgressType progress) {
        return progress == null || progress.equals(IN_PROGRESS);
    }
}
