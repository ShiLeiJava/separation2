package com.leo.separation2.config.dbConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo_lei on 2018/11/13
 */
@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        value = "com.leo.separation2.dao")
@AutoConfigureAfter(DataSourceConfiguration.class)
public class JpaEntityManager {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("writeDataSource")
    private DataSource writeDataSource;
    @Autowired
    @Qualifier("readDataSource")
    private DataSource readDataSource;

    @Value("${mysql.datasource.readSize}")
    private String readDataSourceSize;



    @Bean(name = "routingDataSource")
    public AbstractRoutingDataSource routingDataSource() {
        DynamicDataSourceRouter proxy = new DynamicDataSourceRouter();
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DataSourceType.write.getType(), writeDataSource);
        targetDataSources.put(DataSourceType.read.getType(), readDataSource);

        proxy.setDefaultTargetDataSource(writeDataSource);
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    //@Primary
    @Bean(name = "entityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        // 不明白为什么这里获取不到 application.yml 里的配置
        Map<String, String> properties = jpaProperties.getProperties();
        //要设置这个属性，实现 CamelCase -> UnderScore 的转换
        properties.put("hibernate.physical_naming_strategy",
                "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");


        return builder
                .dataSource(routingDataSource())//关键：注入routingDataSource
                .properties(properties)
                .packages("com.leo.separation2.entity")
                .persistenceUnit("myPersistenceUnit")
                .build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return this.entityManagerFactoryBean(builder).getObject();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder));
    }



//    @Bean(name="routingDataSource")
//    public AbstractRoutingDataSource roundRobinDataSouceProxy() {
//
//        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
//        targetDataSources.put(DataSourceType.write.getType(), writeDataSource);
//        targetDataSources.put(DataSourceType.read.getType(), readDataSource);
//        final int readSize = Integer.parseInt(readDataSourceSize);
//
//        //路由类，寻找对应的数据源
//        AbstractRoutingDataSource proxy = new AbstractRoutingDataSource(){
//            private AtomicInteger count = new AtomicInteger(0);
//            /**
//             * 这是AbstractRoutingDataSource类中的一个抽象方法，
//             * 而它的返回值是你所要用的数据源dataSource的key值，有了这个key值，
//             * targetDataSources就从中取出对应的DataSource，如果找不到，就用配置默认的数据源。
//             */
//            @Override
//            protected Object determineCurrentLookupKey() {
//                String typeKey = DataSourceContextHolder.getReadOrWrite();
//
//                if(typeKey == null){
//                    //	System.err.println("使用数据库write.............");
//                    //    return DataSourceType.write.getType();
//                    throw new NullPointerException("数据库路由时，决定使用哪个数据库源类型不能为空...");
//                }
//
//                if (typeKey.equals(DataSourceType.write.getType())){
//                    System.err.println("使用数据库write.............");
//                    return DataSourceType.write.getType();
//                }
//
//                //读库， 简单负载均衡
////                int number = count.getAndAdd(1);
////                int lookupKey = number % readSize;
////                System.err.println("使用数据库read-"+(lookupKey+1));
//                return DataSourceType.read.getType()/*+(lookupKey+1)*/;
//            }
//        };
//
//        proxy.setDefaultTargetDataSource(writeDataSource);//默认库
//        proxy.setTargetDataSources(targetDataSources);
//        return proxy;
//    }


}
