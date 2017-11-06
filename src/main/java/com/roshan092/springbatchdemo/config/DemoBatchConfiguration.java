package com.roshan092.springbatchdemo.config;

import com.roshan092.springbatchdemo.domain.DemoBatchInput;
import com.roshan092.springbatchdemo.domain.DemoBatchOutput;
import com.roshan092.springbatchdemo.service.DemoItemProcessor;
import com.roshan092.springbatchdemo.service.DemoProcessorListener;
import com.roshan092.springbatchdemo.service.DemoReaderListener;
import com.roshan092.springbatchdemo.service.DemoWriterListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Date;

@Configuration
@EnableBatchProcessing
public class DemoBatchConfiguration {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private DemoItemProcessor demoItemProcessor;
    @Autowired
    private DemoReaderListener demoReaderListener;
    @Autowired
    private DemoProcessorListener demoProcessorListener;
    @Autowired
    private DemoWriterListener demoWriterListener;

    @Bean
    public DataSourceTransactionManager transactionManager(final DataSource batchDataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(batchDataSource);
        return dataSourceTransactionManager;
    }

    @Bean
    public JobRepository jobRepository(final DataSource batchDataSource) throws Exception {
        JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setDataSource(batchDataSource);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager(batchDataSource));
        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleAsyncTaskExecutor jobTaskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(1);
        return simpleAsyncTaskExecutor;
    }

    @Bean
    public JobLauncher jobLauncher(@Qualifier("batchDataSource") final DataSource batchDataSource) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setTaskExecutor(jobTaskExecutor());
        jobLauncher.setJobRepository(jobRepository(batchDataSource));
        return jobLauncher;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<DemoBatchInput> reader() {
        System.out.println("FlatFileItemReader created ------------------->");
        FlatFileItemReader<DemoBatchInput> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("/home/roshan092/input/sample-data.csv"));
        reader.setLineMapper(new DefaultLineMapper<DemoBatchInput>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"firstName", "lastName"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<DemoBatchInput>() {{
                setTargetType(DemoBatchInput.class);
            }});
        }});
        return reader;
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<DemoBatchOutput> writer() {
        System.out.println("FlatFileItemWriter created ------------------->");
        long timeStamp = new Date().getTime();
        FlatFileItemWriter<DemoBatchOutput> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("/home/roshan092/output/test-output" + timeStamp + ".csv"));
        writer.setHeaderCallback(headerWriter -> headerWriter.write("Name(Upper Case)"));
        DelimitedLineAggregator<DemoBatchOutput> delLineAgg
                = new DelimitedLineAggregator<>();
        delLineAgg.setDelimiter(",");
        BeanWrapperFieldExtractor<DemoBatchOutput> fieldExtractor
                = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"name"});
        delLineAgg.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(delLineAgg);
        return writer;
    }

    @Bean
    public SimpleAsyncTaskExecutor stepTaskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(2);
        return simpleAsyncTaskExecutor;
    }

    @Bean
    public Step step() {
        System.out.println("step created ------------------->");
        return stepBuilderFactory.get("step")
                .<DemoBatchInput, DemoBatchOutput>chunk(1)
                .reader(reader())
                .processor(demoItemProcessor)
                .writer(writer())
                .listener(demoReaderListener)
                .listener(demoProcessorListener)
                .listener(demoWriterListener)
                .taskExecutor(stepTaskExecutor())
                .build();
    }

    @Bean(name = "demoJob")
    public Job job() {
        System.out.println("demoJob created ------------------->");
        return jobBuilderFactory.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .flow(step())
                .end()
                .build();
    }

}
