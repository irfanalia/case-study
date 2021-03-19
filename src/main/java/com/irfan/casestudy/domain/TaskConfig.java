package com.irfan.casestudy.domain;

import java.util.List;

public class TaskConfig {

    private Task task;
    private List<String> dependentTasks;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<String> getDependentTasks() {
        return dependentTasks;
    }

    public void setDependentTasks(List<String> dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public String toString() {

        return task.getTaskId() + ":" + getDependentTasks();
    }
}
