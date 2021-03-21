package com.irfan.casestudy.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Resource {
   private int resourceId;
   private List<String> taskListToExecute = new ArrayList<>();



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
}
