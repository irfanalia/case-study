package com.irfan.casestudy.service;

import com.irfan.casestudy.domain.Resource;
import com.irfan.casestudy.domain.TaskConfig;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TaskScheduler {

    static final int NUMBER_OF_RESOURCES = 3;
    Map<String, List<String>> jobDependencyMap = new HashMap<>();

    public void executeTasks(List<TaskConfig> taskConfigList) {

        List<TaskConfig> completeDependentList = createJobDependentList(taskConfigList);
        createExecutionSequence(completeDependentList);

    }




    private List<TaskConfig> createJobDependentList(List<TaskConfig> taskConfigList) {

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
        Collections.sort(completeDependentTaskConfigList);
        System.out.println("completeDependentTaskConfigList:"+completeDependentTaskConfigList);
        return completeDependentTaskConfigList;


    }
    private void createExecutionSequence(List<TaskConfig> completeDependentList) {
        Resource resource[] = new Resource[NUMBER_OF_RESOURCES];
        int counter = (NUMBER_OF_RESOURCES <= completeDependentList.size())?
                NUMBER_OF_RESOURCES: completeDependentList.size();
        allocateFirstJob(completeDependentList, resource, counter);
        int maxTasksInAResource = completeDependentList.size() /NUMBER_OF_RESOURCES +1;
        for(int i=counter; i<completeDependentList.size(); i++) {

            TaskConfig taskConfig = completeDependentList.get(i);
            allocateTaskToResource(taskConfig, resource, maxTasksInAResource);
        }

        System.out.println("");

    }



    private void allocateFirstJob(List<TaskConfig> completeDependentList, Resource[] resource, int counter) {

        for(int i=0; i<counter; i++) {
            resource[i] =  new Resource();
            resource[i].setResourceId(i);
            resource[i].addTask(completeDependentList.get(i).getTask().getTaskId());

        }
    }

    private void allocateTaskToResource(TaskConfig taskConfig, Resource[] resource, int maxTasksInAResource) {
        //ThreadLocalRandom.current().nextInt(1, 10);
        int matchJObs = 0;

        Resource allocateResource = resource[0];
        for(Resource eachResource: resource) {
            if(eachResource.getAllocatedJobCount() >= maxTasksInAResource) {
                continue;
            }
            int currentMatch = 0;
            for(String task: taskConfig.getDependentTasks()) {

                if(eachResource.getTaskListToExecute().contains(task)) {
                    currentMatch ++;
                }

            }
            if(currentMatch > matchJObs) {
                allocateResource = eachResource;
                matchJObs = currentMatch;

            }
        }
        allocateResource.addTask(taskConfig.getTask().getTaskId());
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
