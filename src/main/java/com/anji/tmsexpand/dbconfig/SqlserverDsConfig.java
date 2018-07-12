package com.anji.tmsexpand.dbconfig;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = "com.anji.tmsexpand.mapper.sqlserver.reports", sqlSessionTemplateRef  = "sqlserverSqlSessionTemplate")
public class SqlserverDsConfig {

	@Bean(name = "sqlserverDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.sqlserverds")
	@Primary
	public DataSource testDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "sqlserverSqlSessionFactory")
	@Primary
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("sqlserverDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/sqlserver/*.xml"));
		return bean.getObject();
	}

	@Bean(name = "sqlserverTransactionManager")
	@Primary
	public DataSourceTransactionManager testTransactionManager(@Qualifier("sqlserverDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "sqlserverSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate testSqlSessionTemplate(
			@Qualifier("sqlserverSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}
