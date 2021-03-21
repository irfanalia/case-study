package com.irfan.casestudy.service;

import com.irfan.casestudy.domain.Task;
import com.irfan.casestudy.domain.TaskConfig;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskSchedulerTest extends TestCase {
    TaskScheduler taskScheduler = new TaskScheduler();
    public void testExecuteTasks() {

        List<TaskConfig> taskConfigList = new ArrayList<>();
        taskConfigList.add(createTaskConfig("T1(2):"));
        taskConfigList.add(createTaskConfig("T2(3):"));
        taskConfigList.add(createTaskConfig("T3(5):"));
        taskConfigList.add(createTaskConfig("T8(3):T4"));
        taskConfigList.add(createTaskConfig("T4(4):T1"));
        taskConfigList.add(createTaskConfig("T7(2):T5,T6"));
        taskConfigList.add(createTaskConfig("T6(6):T5,T3"));
        taskConfigList.add(createTaskConfig("T5(1):T1,T2"));
        taskScheduler.executeTasks(taskConfigList);

    }

    public void testExecuteTasksWithRandomFailure() {

        List<TaskConfig> taskConfigList = new ArrayList<>();
        taskConfigList.add(createTaskConfig("T1(2):"));
        taskConfigList.add(createTaskConfig("T2(3):"));
        taskConfigList.add(createTaskConfig("T3(5):"));
        taskConfigList.add(createTaskConfig("T8(3):T4"));
        taskConfigList.add(createTaskConfig("T4(4):T1"));
        taskConfigList.add(createTaskConfig("T7(2):T5,T6"));
        taskConfigList.add(createTaskConfig("T6(6):T5,T3"));
        taskConfigList.add(createTaskConfig("T5(1):T1,T2"));
        taskScheduler.executeTasks(taskConfigList, true);

    }

    private TaskConfig createTaskConfig(String config) {

        TaskConfig taskConfig = new TaskConfig();
        String[] configArray = config.split(":");
        String taskId = configArray[0].substring(0, configArray[0].indexOf("("));
        taskConfig.setTask(new Task(taskId, Integer.valueOf(configArray[0].split("[\\(\\)]")[1])));
        if(configArray.length>1) {
            taskConfig.setDependentTasks(Arrays.asList(configArray[1].split(",")));
        }
        return taskConfig;

    }
}