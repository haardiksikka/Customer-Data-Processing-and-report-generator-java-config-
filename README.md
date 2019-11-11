# Customer-Data-Processing-and-report-generator-java-config-
Spring batch application for processing customer data and generating day wise report using java config

Projects Requirements

-Database - Maria Db 10.3
-jdk 1.8.0
-IDE - eclipse/Spring tool suite(sts)

Follow the below mentioned steps to execute the project:

1).Update maven project to install all required dependencies.
2).Create database customer_processing in your machine.
3).Create table customer with fields first_name, middle_name, last_name, city, address, phone_number and created_on with phone_number as primary key.
4).Create table fee_info with fields phone_number, fee_amount, fee_date (refer to schema.sql file).
5).Update data source in application.properties.
6).Change location for generating report file according to your preference in batch configuration.
