package com.irfan.casestudy.domain;

public class JobExecutionStatus {

    private int taskId;
    private JobStatus status;
    private int startTime;
    private int totalWaitTime;
    private int executionTime;

    public void endJob(int time) {
        this.endTime = time;
        this.status = JobStatus.COMPLETED;
        executionTime = endTime - startTime -1;

    }

    public void startJob(int time) {
        this.startTime = time;
        this.status = JobStatus.STARTED;

    }


    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    private int endTime;
    private int resourceId;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getTotalWaitTime() {
        return totalWaitTime;
    }

    public void setTotalWaitTime(int totalWaitTime) {
        this.totalWaitTime = totalWaitTime;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void calculateEndTime() {
        this.endTime = this.startTime + this.executionTime;
    }

    @Override
    public String toString() {
        return status +
                ":" + startTime +
                ":" + endTime +
                ":" + resourceId ;
    }
}
