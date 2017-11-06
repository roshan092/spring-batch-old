package com.roshan092.springbatchdemo.controller;

import com.roshan092.springbatchdemo.domain.BatchResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.util.Set;

@RestController
public class BatchController {
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job demoJob;
    @Autowired
    private Clock clock;
    @Autowired
    private JobExplorer jobExplorer;


    @PostMapping("/batch/run")
    public BatchResponse startBatch() throws JobExecutionException {
        JobExecution execution;
        Set<JobExecution> runningJobExecutions = jobExplorer.findRunningJobExecutions("demoJob");
        if (runningJobExecutions.size() != 0) {
            execution = runningJobExecutions.stream().findFirst().get();
        } else {
            JobParametersBuilder builder = new JobParametersBuilder()
                    .addLong("time", clock.instant().getEpochSecond());
            execution = jobLauncher.run(this.demoJob, builder.toJobParameters());
        }
        StepExecution stepExecution = execution.getStepExecutions().stream().findFirst().orElse(null);
        return BatchResponse.builder().
                jobExecutionId(execution.getId())
                .readCount(stepExecution == null ? 0 : stepExecution.getReadCount())
                .writeCount(stepExecution == null ? 0 : stepExecution.getWriteCount())
                .build();
    }
}
