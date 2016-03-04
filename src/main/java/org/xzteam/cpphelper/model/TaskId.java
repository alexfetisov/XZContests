package org.xzteam.cpphelper.model;

public class TaskId {
    private final String platform;
    private final String contest;
    private final String task;

    public TaskId(String platform, String contest, String task) {
        this.platform = platform;
        this.contest = contest;
        this.task = task;
    }

    public String getPlatform() {
        return platform;
    }

    public String getContest() {
        return contest;
    }

    public String getTask() {
        return task;
    }

    public String getDir() {
        return contest == null ?
               String.format("%s/%s/", platform, task) :
               String.format("%s/%s/%s/", platform, contest, task);
    }

    public String getPrefix() {
        return contest == null ?
               String.format("%s%s", platform, task) :
               String.format("%s_%s%s", platform, contest, task);
    }
}
