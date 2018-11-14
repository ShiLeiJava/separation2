package com.leo.separation2.config.dbConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by Leo_lei on 2018/11/8
 */
@Configuration
public class DataSourceConfiguration {

    private static Logger log = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Value("${mysql.datasource.type}")
    private Class<? extends DataSource> dataSourceType;


    //写库
    @Primary
    @Qualifier("writeDataSource")
    @Bean("writeDataSource")
    @ConfigurationProperties(prefix = "mysql.datasource.write")
    public DataSource writeDataSource(){
        log.info("-------------------- writeDataSource init ---------------------");
        return DataSourceBuilder.create().type(dataSourceType).build();
    }


    //读库
    @Qualifier("readDataSource")
    @Bean(name = "readDataSource")
    @ConfigurationProperties(prefix = "mysql.datasource.read")
    public DataSource readDataSourceOne() {
        log.info("-------------------- read DataSourceOne init ---------------------");
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

}
