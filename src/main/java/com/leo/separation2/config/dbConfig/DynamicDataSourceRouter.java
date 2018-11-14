package com.leo.separation2.config.dbConfig;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSourceRouter extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {

        System.out.println("最终拿到的是："+DataSourceContextHolder.getReadOrWrite());

        String typeKey = DataSourceContextHolder.getReadOrWrite();
//
        if(typeKey == null){
            return DataSourceType.write.getType();
        }

        if (typeKey.equals(DataSourceType.write.getType())){
            System.err.println("使用数据库write.............");
            return DataSourceType.write.getType();
        }

        //读库， 简单负载均衡
//                int number = count.getAndAdd(1);
//                int lookupKey = number % readSize;
//                System.err.println("使用数据库read-"+(lookupKey+1));
        return DataSourceType.read.getType()/*+(lookupKey+1)*/;

//        return DataSourceContextHolder.getReadOrWrite();
    }
}
