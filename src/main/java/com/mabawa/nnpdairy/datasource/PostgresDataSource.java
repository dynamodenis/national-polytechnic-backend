package com.mabawa.nnpdairy.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class PostgresDataSource {
//    @Bean
//    public HikariDataSource hikariDataSource() {
//        String dbUrl = "jdbc:postgresql://localhost:5432/nnp_dairy";
//        //String dbUrl = "jdbc:postgresql://database:5432/bawa_sales_auto";
//        String username = "nnpuser";
//        String password = "nnp-dairy-user";
//
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(dbUrl);
//        config.setUsername(username);
//        config.setPassword(password);
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//        HikariDataSource ds = new HikariDataSource(config);
//        return ds;
//    }
//}
