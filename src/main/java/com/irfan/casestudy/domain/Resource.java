package com.irfan.casestudy.domain;

import java.util.ArrayList;
import java.util.List;

public class Resource {
   private int resourceId;
   private List<String> taskListToExecute = new ArrayList<>();
   private List<JobExecutionStatus> executionStatusList = new ArrayList<>();



    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public List<String> getTaskListToExecute() {
        return taskListToExecute;
    }

    public void setTaskListToExecute(List<String> taskListToExecute) {
        this.taskListToExecute = taskListToExecute;
    }

    public Resource() {
    }

    public Resource(int resourceId, List<String> taskListToExecute) {
        this.resourceId = resourceId;
        this.taskListToExecute = taskListToExecute;
    }

    public int getAllocatedJobCount() {

        return taskListToExecute.size();
    }

    public void addTask(String taskId) {
        taskListToExecute.add(taskId);
    }

    public void updateJobStatus(JobExecutionStatus status) {
        executionStatusList.add(status);
    }

    public List<JobExecutionStatus> getExecutionStatusList() {
        return executionStatusList;
    }

    public void setExecutionStatusList(List<JobExecutionStatus> executionStatusList) {
        this.executionStatusList = executionStatusList;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer(resourceId);
        sb.append("Resource ").append(resourceId).append(" ==> ");
        for(JobExecutionStatus status: executionStatusList) {

            sb.append(status.getTaskId());
            sb.append(":");
            sb.append(status.getStartTime());
            sb.append(":");
            sb.append(status.getEndTime());
            sb.append("->");
        }

        return sb.toString();

    }
}
