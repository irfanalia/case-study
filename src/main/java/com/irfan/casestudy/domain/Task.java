package com.irfan.casestudy.domain;

public class Task {

    private String taskId;

    private float duration;

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Task(String taskId, float duration) {
        this.taskId = taskId;
        this.duration = duration;
    }

    public String toString() {

        return taskId;
    }
}
