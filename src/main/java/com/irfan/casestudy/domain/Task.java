package com.irfan.casestudy.domain;

public class Task {

    private String taskId;

    private int duration;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Task(String taskId, int duration) {
        this.taskId = taskId;
        this.duration = duration;
    }

    public String toString() {

        return taskId +"-"+Integer.valueOf(duration);
    }
}
