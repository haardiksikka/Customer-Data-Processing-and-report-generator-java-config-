package com.viva.CustomerProcessing.configuration;



import java.text.SimpleDateFormat;
import java.util.Date;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


import com.viva.CustomerProcessing.entity.Customer;
import com.viva.CustomerProcessing.entity.FeeInfo;
import com.viva.CustomerProcessing.listener.JobListner;
import com.viva.CustomerProcessing.processor.CustomerItemProcessor;
import com.viva.CustomerProcessing.processor.FeeInfoProcessor;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    public DataSource dataSource;
     
    
    @Bean
    public Job importUserJob(@Qualifier("step3")  Step step3,JobListner listener) {
        return jobBuilderFactory.get("importUserJob")
        	.incrementer(new RunIdIncrementer())
        	.listener(listener)
        	.flow(step1())
        	.next(step2())
        	.next(step3)
            .end()
            .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
            .<Customer, Customer> chunk(50)            
            .reader(customerReader())
            .processor(customerProcessor())
            .writer(customerWriter())
            .build();
    }
    
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
            .<Customer, FeeInfo> chunk(50)            
            .reader(customerReader())
            .processor(feeInfoProcessor())
            .writer(feeInfoWriter())
            .build();
    }
    
    @Bean
    public Step step3(ItemWriter<FeeInfo> databaseCsvItemWriter){
    	 return stepBuilderFactory.get("step3")
                 .<FeeInfo, FeeInfo>chunk(50)
                 .reader(databaseCsvItemReader())
                 .writer(databaseCsvItemWriter)
                 .build();
    }
    
    @Bean
    public FlatFileItemReader<Customer> customerReader() {
    	
        return new FlatFileItemReaderBuilder<Customer>()
            .name("customerItemReader")          
            .resource(new ClassPathResource("customerdata.csv"))
            .delimited()
            .names(new String[]{"firstName","middleName", "lastName","address","city","phoneNumber"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
                setTargetType(Customer.class);
            }})
            .build();
    }
    
    @Bean
    ItemReader<FeeInfo> databaseCsvItemReader() {
    	JdbcCursorItemReader<FeeInfo> databaseReader = new JdbcCursorItemReader<>();

    	databaseReader.setDataSource(dataSource);
        databaseReader.setSql("select sum(fee_amount) 'fee_amount',fee_date from fee_info group by fee_date");
        databaseReader.setRowMapper(new BeanPropertyRowMapper<>(FeeInfo.class));

   
        return databaseReader;
    }
    
    @Bean
    public FlatFileItemReader<FeeInfo> feeInfoReader() {
        return new FlatFileItemReaderBuilder<FeeInfo>()
            .name("feeItemReader")
            .resource(new ClassPathResource("customerdata.csv"))
            .delimited()
            .names(new String[]{"phoneNumber","feeAmount","feeDate"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<FeeInfo>() {{
                setTargetType(FeeInfo.class);
            }})
            .build();
    }

    @Bean
    public CustomerItemProcessor customerProcessor() {
        return new CustomerItemProcessor();
    }
    
    @Bean FeeInfoProcessor feeInfoProcessor() {
    	return new FeeInfoProcessor();
    }
    
    @Bean
    public JdbcBatchItemWriter<Customer> customerWriter() {  	
      JdbcBatchItemWriter<Customer> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
      jdbcBatchItemWriter.setAssertUpdates(true);      
      jdbcBatchItemWriter.setDataSource(dataSource);     
      jdbcBatchItemWriter.setSql("INSERT INTO customer (first_name, middle_name, last_name, address, city, phone_number,created_on) VALUES (:firstName,:middleName,:lastName,:address,:city,:phoneNumber,now())");     
      jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
      return jdbcBatchItemWriter;
}    
    @Bean
    public JdbcBatchItemWriter<FeeInfo> feeInfoWriter() {
    	try {
    		return new JdbcBatchItemWriterBuilder<FeeInfo>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO fee_info (phone_number,fee_amount,fee_date) VALUES (:phoneNumber,:feeAmount,now())")
            .dataSource(dataSource)
            .build();
    	}
    	catch(Exception e) {
    		return null;
    	}
    }
    
    
    @Bean
    ItemWriter<FeeInfo> databaseCsvItemWriter(Environment environment) {
        FlatFileItemWriter<FeeInfo> csvFileWriter = new FlatFileItemWriter<>();
        String fileDate=new SimpleDateFormat("ddMMyyyyHHmmss'.csv'").format(new Date());
        String exportFilePath = "C:\\Users\\comviva\\Desktop"+fileDate;
        csvFileWriter.setResource(new FileSystemResource(exportFilePath));
        LineAggregator<FeeInfo> lineAggregator = createStudentLineAggregator();
        csvFileWriter.setLineAggregator(lineAggregator);

        return csvFileWriter;
    }
    private LineAggregator<FeeInfo> createStudentLineAggregator() {
        DelimitedLineAggregator<FeeInfo> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        FieldExtractor<FeeInfo> fieldExtractor = createStudentFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;
    }
    
    private FieldExtractor<FeeInfo> createStudentFieldExtractor() {
        BeanWrapperFieldExtractor<FeeInfo> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"feeAmount","feeDate"});
        return extractor;
    }
    
}
