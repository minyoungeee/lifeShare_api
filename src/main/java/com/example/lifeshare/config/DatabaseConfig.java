package com.example.lifeshare.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * 데이터베이스 접근을 위한 설정을 수행한다.
 *
 * @author chauki
 * @version 1.0
 ** 1. SqlSessionFactory 및 SqlSessionTemplate 설정
 ** 2. mybatis 이용을 위한 설정 xml 리소스 추가(mybatis-config.xml)
 ** 3. mybatis가 인식한 쿼리위치 리소스 추가
 **/
@Configuration
@MapperScan(basePackages = "com.example.lifeshare.api") //mapper 위치 검색(반드시 해야함)
@EnableTransactionManagement
public class DatabaseConfig {

    /**
     * SqlSessionFactory 설정
     * (mybatis config 파일,  sql 파일)
     *
     * @param dataSource DataSource 객체
     * @return
     * @author chauki
     * @version 1.0
     **/
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        sessionFactory.setMapperLocations(resolver.getResources("classpath:sql/*.xml"));
        sessionFactory.setVfs(SpringBootVFS.class);
        return sessionFactory.getObject();
    }

    /**
     * SqlSessionTemplate 설정
     *
     * @param sqlSessionFactory SqlSessionFactory 객체
     * @return SqlSessionTemplate
     * @author chauki
     * @version 1.0
     **/
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
