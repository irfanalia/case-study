package com.irfan.casestudy.domain;

import java.util.List;

public class TaskConfig implements Comparable{

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

        return task + ":" + getDependentTasks();
    }


    @Override
    public int compareTo(Object o) {
        TaskConfig taskConfigToCompare = (TaskConfig) o;
        if(taskConfigToCompare.getDependentTasks() != null && this.getDependentTasks() != null) {
             return Integer.valueOf(this.getDependentTasks().size())
                     .compareTo(Integer.valueOf(taskConfigToCompare.getDependentTasks().size()));
        }
        if(this.getDependentTasks() == null) {
            return -1;
        }
        if(taskConfigToCompare.getDependentTasks() == null) {
            return 1;
        }
        return 0;
    }
}
