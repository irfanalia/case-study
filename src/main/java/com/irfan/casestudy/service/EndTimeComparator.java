package com.irfan.casestudy.service;

import com.irfan.casestudy.domain.JobExecutionStatus;

import java.util.Comparator;

public class EndTimeComparator implements Comparator<JobExecutionStatus> {

    @Override
    public int compare(JobExecutionStatus o1, JobExecutionStatus o2) {
        return Integer.valueOf(o2.getEndTime()).compareTo(Integer.valueOf(o1.getEndTime()));
    }
}
