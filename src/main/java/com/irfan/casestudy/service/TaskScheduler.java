package com.irfan.casestudy.service;

import com.irfan.casestudy.domain.JobExecutionStatus;
import com.irfan.casestudy.domain.JobStatus;
import com.irfan.casestudy.domain.Resource;
import com.irfan.casestudy.domain.TaskConfig;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TaskScheduler {

    static final int NUMBER_OF_RESOURCES = 3;
    Map<String, List<String>> jobDependencyMap = new HashMap<>();
    Map<String, JobExecutionStatus> statusMap = new HashMap<>();
    Map<String, TaskConfig> taskConfigMap = new HashMap<>();

    public void executeTasks(List<TaskConfig> taskConfigList) {
        executeTasks(taskConfigList,false);
    }

    public void executeTasks(List<TaskConfig> taskConfigList, boolean failJob) {

        List<TaskConfig> completeDependentList = createJobDependentList(taskConfigList);
        Resource[] resources = createExecutionSequence(completeDependentList);
        populateInitialStatus(resources);

        executeTasksOnResources(resources, completeDependentList, failJob);
        System.out.println("Sequence");
        System.out.println("---------------------------------------------------");
        for(Resource resource: resources) {
            System.out.println(resource);
        }
        System.out.println("---------------------------------------------------");
        findTotalExecutionTime(resources) ;
        System.out.println("---------------------------------------------------");


    }

    private void populateInitialStatus(Resource[] resources) {
        for(Resource resource: resources) {
            for(String task: resource.getTaskListToExecute()) {
                JobExecutionStatus jobExecutionStatus = new JobExecutionStatus();
                jobExecutionStatus.setTaskId(task);
                jobExecutionStatus.setStatus(JobStatus.NOT_STARTED);
                jobExecutionStatus.setResourceId(resource.getResourceId());
                statusMap.put(task, jobExecutionStatus);
            }
        }
    }

    private void executeTasksOnResources(Resource[] resources, List<TaskConfig> completeDependentList, boolean failJob) {

        String taskIdToFail = null;
        if(failJob) {
            int jobIndex = ThreadLocalRandom.current().nextInt(1, completeDependentList.size());
            taskIdToFail = completeDependentList.get(jobIndex).getTask().getTaskId();
        }

        boolean allJobsCompleted = false;
        int time = 0;
        int [] jobPointer = new int[resources.length];
        while(!allJobsCompleted) {
            for(int i=0; i< resources.length;i++) {
                if(jobPointer[i] == resources[i].getTaskListToExecute().size()) {
                    continue;
                }
                String task = resources[i].getTaskListToExecute().get(jobPointer[i]);
                JobExecutionStatus status = statusMap.get(task);
               /* if(failJob && taskIdToFail.equals(task)) {
                    status.failJob(time);
                    resources[i].updateJobStatus(status);
                    continue;
                }*/
                if(JobStatus.NOT_STARTED.equals(status.getStatus())) {
                    if(!checkIfDependentsCompleted(taskConfigMap.get(task).getDependentTasks())) {
                        continue;
                    }
                    status.startJob(time);
                } else if(status.getStatus().equals(JobStatus.STARTED)) {
                    if((status.getStartTime() + taskConfigMap.get(task).getTask().getDuration() == time )) {
                        status.endJob(time);
                        resources[i].updateJobStatus(status);
                        jobPointer[i] ++;
                        if(jobPointer[i] == resources[i].getTaskListToExecute().size()) {
                            continue;
                        }
                        String nextTask = resources[i].getTaskListToExecute().get(jobPointer[i]);
                        if(!checkIfDependentsCompleted(taskConfigMap.get(nextTask).getDependentTasks())) {
                            continue;
                        }
                        statusMap.get(nextTask).startJob(time+1);
                    }
                }
            }

            allJobsCompleted = checkJobStatus();
            time++;

        }

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
            taskConfigMap.put(taskConfig.getTask().getTaskId(), taskConfig);


        }
        Collections.sort(completeDependentTaskConfigList);
        return completeDependentTaskConfigList;


    }
    private Resource[] createExecutionSequence(List<TaskConfig> completeDependentList) {

        Resource resource[] = new Resource[NUMBER_OF_RESOURCES];
        int counter = (NUMBER_OF_RESOURCES <= completeDependentList.size())?
                NUMBER_OF_RESOURCES: completeDependentList.size();
        allocateFirstJob(completeDependentList, resource, counter);
        int maxTasksInAResource = completeDependentList.size() /NUMBER_OF_RESOURCES  ;
        for(int i=counter; i<completeDependentList.size(); i++) {

            TaskConfig taskConfig = completeDependentList.get(i);
            allocateTaskToResource(taskConfig, resource, maxTasksInAResource);
        }

        return resource;

    }

    private void allocateFirstJob(List<TaskConfig> completeDependentList, Resource[] resource, int counter) {

        for(int i=0; i<counter; i++) {
            resource[i] =  new Resource();
            resource[i].setResourceId(i);
            resource[i].addTask(completeDependentList.get(i).getTask().getTaskId());

        }
    }

    private void allocateTaskToResource(TaskConfig taskConfig, Resource[] resource, int maxTasksInAResource) {

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

    private boolean checkJobStatus() {
        boolean completed = false;
        for(Map.Entry<String, JobExecutionStatus> entry: statusMap.entrySet()) {
            if(entry.getValue().getStatus().equals(JobStatus.COMPLETED) || entry.getValue().getStatus().equals(JobStatus.FAILED)) {
                completed = true;
            } else {
                return false;
            }
        }

        return completed;
    }

    private boolean checkIfDependentsCompleted(List<String> dependentTasks) {
        boolean completed = false;
        if(null== dependentTasks) {
            return true;
        }

        for (String task : dependentTasks) {
            if (statusMap.get(task).getStatus().equals(JobStatus.COMPLETED) || statusMap.get(task).getStatus().equals(JobStatus.FAILED)) {
                completed = true;
            } else {
                return false;
            }

        }

        return completed;
    }

    public void findTotalExecutionTime(Resource[] resources) {

        List<JobExecutionStatus> statusList = new ArrayList<>();
        statusList.addAll(statusMap.values());
        Collections.sort(statusList, new EndTimeComparator());
        System.out.println("Total Execution Time:" + statusList.get(0).getEndTime());
        for(Resource resource: resources) {
            Collections.sort(resource.getExecutionStatusList(), new EndTimeComparator());
            System.out.println("Total Execution Time for resource"+ resource.getResourceId() +" :" + resource.getExecutionStatusList().get(0).getEndTime());
        }
    }
}
