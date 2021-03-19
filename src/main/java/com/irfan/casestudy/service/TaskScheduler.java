package com.irfan.casestudy.service;

import com.irfan.casestudy.domain.TaskConfig;

import java.util.*;

public class TaskScheduler {

    public void executeTasks(List<TaskConfig> taskConfigList) {

        createJobDependentList(taskConfigList);
        sortJobsOnNumberOfMinDependency();

    }


    private void createJobDependentList(List<TaskConfig> taskConfigList) {

        Map<String, List<String>> jobDependencyMap = new HashMap<>();

        List<TaskConfig> completeDependentTaskConfigList = new ArrayList<>();
        for(TaskConfig taskConfig: taskConfigList) {
            if(taskConfig.getTask() != null) {
                jobDependencyMap.put(taskConfig.getTask().getTaskId(), taskConfig.getDependentTasks());

            }
        }
        for(TaskConfig taskConfig: taskConfigList) {
            TaskConfig taskConfigWithDependency = new TaskConfig();
            List<String> allDependents = new ArrayList<>();
                if (jobDependencyMap.get(taskConfig.getTask().getTaskId()) != null) {
                    allDependents.addAll(getDependents(jobDependencyMap, taskConfig.getTask().getTaskId()));

                }
            taskConfigWithDependency.setTask(taskConfig.getTask());
            taskConfigWithDependency.setDependentTasks(allDependents);
            completeDependentTaskConfigList.add(taskConfigWithDependency);


        }
        System.out.println("completeDependentTaskConfigList:"+completeDependentTaskConfigList);

    }

    private Set<String> getDependents(Map<String, List<String>> jobDependencyMap, String taskId) {

        Set<String> allDependents = new HashSet<>();
        List<String> dependents = new ArrayList<>();
        if(jobDependencyMap.get(taskId) != null) {
            dependents = jobDependencyMap.get(taskId);
            allDependents.addAll(dependents);

        }
        for (String t: dependents) {
            allDependents.addAll(getDependents(jobDependencyMap, t));
        }
        return allDependents;
    }

    private void sortJobsOnNumberOfMinDependency() {
    }
}
