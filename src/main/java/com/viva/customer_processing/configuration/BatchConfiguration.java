package com.viva.customer_processing.configuration;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.viva.customer_processing.entity.Customer;
import com.viva.customer_processing.entity.FeeInfo;
import com.viva.customer_processing.listener.JobListner;
import com.viva.customer_processing.processor.CustomerItemProcessor;
import com.viva.customer_processing.processor.DeleteCustomerProcessor;
import com.viva.customer_processing.processor.DeleteFeeInfoProcessor;
import com.viva.customer_processing.processor.FeeInfoProcessor;


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
    @Qualifier("importUserJob")
    @Primary
    public Job importUserJob(@Qualifier("reportGeneratorStep")  Step reportGeneratorStep,JobListner listener,TaskExecutor taskExecutor) {
        return jobBuilderFactory.get("importUserJob")
        	.incrementer(new RunIdIncrementer())
        	.listener(listener)
        	.flow(customerStep(taskExecutor))
        	.next(feeInfoStep(taskExecutor))
        	.next(reportGeneratorStep)
            .end()
            .build();
    }

    @Bean
    public Step customerStep(TaskExecutor taskExecutor) {
        return stepBuilderFactory.get("customerStep")
            .<Customer, Customer> chunk(50)            
            .reader(customerReader())
            .processor(customerProcessor())
            .writer(customerWriter())
            .taskExecutor(taskExecutor)
            .throttleLimit(10)
            .build();
    }
    
    @Bean
    public Step feeInfoStep(TaskExecutor taskExecutor) {
        return stepBuilderFactory.get("feeInfoStep")
            .<Customer, FeeInfo> chunk(50)            
            .reader(customerReader())
            .processor(feeInfoProcessor())
            .writer(feeInfoWriter())
            .taskExecutor(taskExecutor)
            .throttleLimit(10)
            .build();
    }
    
    @Bean
    public Step reportGeneratorStep(ItemWriter<FeeInfo> databaseCsvItemWriter){
    	 return stepBuilderFactory.get("reportGeneratorStep")
                 .<FeeInfo, FeeInfo>chunk(50)
                 .reader(databaseCsvItemReader())
                 .writer(databaseCsvItemWriter)
                 .build();
    }
    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    
//    @Bean
//	public Job deactivateSim() {
//		return this.jobBuilderFactory.get("deactivateSim")
//				.incrementer(new RunIdIncrementer())
//				.flow(deactivateCustomer())
//	        	.next(deleteFeeInfo())
//	        	.end()
//	        	.build();
//	}
    
//    @Bean
//    @Qualifier(value="deactivateCustomer")
//    public Step deactivateCustomer() {
//    	return stepBuilderFactory.get("deactivateCustomer")
//                .<Customer, Customer> chunk(50)            
//                .reader(deactivateCustomerReader())
//                .processor(deleteCustomerProcessor())
//                .writer(deleteCustomer())
//                .build();
//    }
//    @Bean
//    public Step deleteFeeInfo() {
//    	return stepBuilderFactory.get("deleteFeeInfo")
//                .<Customer, FeeInfo> chunk(50)            
//                .reader(deactivateCustomerReader())
//                .processor(deleteFeeInfoProcessor())
//                .writer(deleteFeeRecord())
//                .build();
//    }
    
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
    public FlatFileItemReader<Customer> deactivateCustomerReader() {
    	
        return new FlatFileItemReaderBuilder<Customer>()
            .name("customerItemReader")          
            .resource(new ClassPathResource("deletecustomer.csv"))
            .delimited()
            .names(new String[]{"firstName","middleName", "lastName","address","city","phoneNumber"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
                setTargetType(Customer.class);
            }})
            .build();
    }
    
    @Bean
    public FlatFileItemReader<FeeInfo> deactivateFeeInfoReader() {
        return new FlatFileItemReaderBuilder<FeeInfo>()
            .name("feeItemReader")
            .resource(new ClassPathResource("deletecustomer.csv"))
            .delimited()
            .names(new String[]{"phoneNumber","feeAmount","feeDate"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<FeeInfo>() {{
                setTargetType(FeeInfo.class);
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
    
    @Bean
    public DeleteCustomerProcessor deleteCustomerProcessor() {
        return new DeleteCustomerProcessor();
    }
    @Bean
    public DeleteFeeInfoProcessor deleteFeeInfoProcessor() {
        return new DeleteFeeInfoProcessor();
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
    public JdbcBatchItemWriter<Customer> deleteCustomer() {  	
      JdbcBatchItemWriter<Customer> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
      jdbcBatchItemWriter.setAssertUpdates(true);      
      jdbcBatchItemWriter.setDataSource(dataSource);     
      jdbcBatchItemWriter.setSql("DELETE FROM customer where phone_number like :phoneNumber");     
      jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
      return jdbcBatchItemWriter;
}
    @Bean
    public JdbcBatchItemWriter<FeeInfo> deleteFeeRecord() {  	
      JdbcBatchItemWriter<FeeInfo> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
      jdbcBatchItemWriter.setAssertUpdates(true);      
      jdbcBatchItemWriter.setDataSource(dataSource);     
      jdbcBatchItemWriter.setSql("DELETE FROM fee_info where phone_number like :phoneNumber");     
      jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<FeeInfo>());
      return jdbcBatchItemWriter;
}
    
  

    @Bean
    public JdbcBatchItemWriter<FeeInfo> feeInfoWriter() {
    
    		return new JdbcBatchItemWriterBuilder<FeeInfo>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO fee_info (phone_number,fee_amount,fee_date) VALUES (:phoneNumber,:feeAmount,now())")
            .dataSource(dataSource)
            .build();
    }
    
 //"C:\\Users\\comviva\\Desktop\\"+"report"+fileDate;   
    @Bean
    ItemWriter<FeeInfo> databaseCsvItemWriter(Environment environment) {
        FlatFileItemWriter<FeeInfo> csvFileWriter = new FlatFileItemWriter<>();
        String fileDate=new SimpleDateFormat("ddMMyyyyHHmmss'.csv'").format(new Date());
        String exportFilePath = "reports\\"+"report"+fileDate;
       // File file = new File("report/report"+.csv");
        csvFileWriter.setResource(new FileSystemResource(exportFilePath));
        LineAggregator<FeeInfo> lineAggregator = createCustomerLineAggregator();
        csvFileWriter.setLineAggregator(lineAggregator);

        return csvFileWriter;
    }
    private LineAggregator<FeeInfo> createCustomerLineAggregator() {
        DelimitedLineAggregator<FeeInfo> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");

        FieldExtractor<FeeInfo> fieldExtractor = createCustomerFieldExtractor();
        lineAggregator.setFieldExtractor(fieldExtractor);

        return lineAggregator;
    }
    
    private FieldExtractor<FeeInfo> createCustomerFieldExtractor() {
        BeanWrapperFieldExtractor<FeeInfo> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[] {"feeAmount","feeDate"});
        return extractor;
    }
    
}
