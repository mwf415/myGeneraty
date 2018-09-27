package com.kylin.generator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class EntityCodeGenerator {

	private DataSourceConfig createDsConfig(String dbUrl,String userName,String password) {
        
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
            .setUrl(dbUrl)
            .setUsername(userName)
            .setPassword(password)
            .setDriverName("com.mysql.jdbc.Driver");
        return dataSourceConfig;
	}
	
	private StrategyConfig createStrategyConfig(String ... tableNames) {
        StrategyConfig strategyConfig = new StrategyConfig();
        String[] fieldPrefix = null;
        strategyConfig
        .setVersionFieldName("version")
        .setTablePrefix("d")
        .setEntityLombokModel(true)
        .setCapitalMode(true)
        .setDbColumnUnderline(true)
        .setNaming(NamingStrategy.underline_to_camel)
        .entityTableFieldAnnotationEnable(true)
        .fieldPrefix(fieldPrefix)
        .setInclude(tableNames);
        return strategyConfig;
	}
	
	private GlobalConfig createGlobalConfig(String path) {
		GlobalConfig config = new GlobalConfig();
        config.setActiveRecord(false)
        .setIdType(IdType.ID_WORKER)
        .setAuthor("kaifa").setDateType(DateType.ONLY_DATE)
        .setOutputDir(path)
        .setFileOverride(true);
		return config;
	}
	
	private AutoGenerator ceateAutoGenerator(String packageName,String outDir,DataSourceConfig ds,StrategyConfig strategy, GlobalConfig globalConfig) {
		AutoGenerator  auto = new AutoGenerator();
		auto.setDataSource(ds);
		auto.setGlobalConfig(globalConfig);
		auto.setStrategy(strategy);
		
		Map<String, String> pathInfo = new HashMap<String, String>();
		
		pathInfo.put(ConstVal.ENTITY_PATH, joinPath(outDir,packageName+".entities"));
		pathInfo.put(ConstVal.MAPPER_PATH, joinPath(outDir,packageName+".mapper"));
		pathInfo.put(ConstVal.SERVICE_PATH, joinPath(outDir,packageName+".interfaces"));
		pathInfo.put(ConstVal.SERVICE_IMPL_PATH, joinPath(outDir,packageName+".impl"));
		
		auto.setPackageInfo(  new PackageConfig().setParent(packageName)
				.setEntity("entities")
				.setMapper("mapper")
				.setService("interfaces")
				.setServiceImpl("impl")
				.setPathInfo(pathInfo));
		
		return auto;
	}
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }
    
	public void execute() {
		
		final String codePath ="/Users/sunyihan/Documents/ideaWorkspace/opt";
		
		final String dbUrl = "jdbc:mysql://rm-2ze1e8na28wb60205o.mysql.rds.aliyuncs.com:3306/dev_gdpaycenter?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
		final String userName ="dev_gdpaycenter";
		final String password ="KoeVigzX1TiL3D";
//
//		final String dbUrl = "jdbc:mysql://rm-2ze437718vui912zso.mysql.rds.aliyuncs.com:3306/test_paydb?autoReconnect=true&rewriteBatchedStatements=true&socketTimeout=30000&connectTimeout=3000&characterEncoding=utf8&allowMultiQueries=true";
//		final String userName = "test_paydb";
//		final String password ="BJtydic_123";


//		final String dbUrl = "jdbc:mysql://localhost:3306/bit_test?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
//		final String userName ="root";
//		final String password ="123456a";
		
		final String packageName ="com.cgd.paycenter.dao";
		final String entityTemplate ="/templates/kylin/entity.java.vm";
		final String mapperTemplate ="/templates/kylin/mapper.java.vm";
		final String serviceTemplate ="/templates/kylin/service.java.vm";
		final String serviceImplTemplate ="/templates/kylin/serviceImpl.java.vm";
		
		String[] tableNames = new String[] {"d_timer_log"};
		
		DataSourceConfig ds  = this.createDsConfig(dbUrl, userName, password);
		GlobalConfig globalConfig = this.createGlobalConfig(codePath);
		StrategyConfig strategy = this.createStrategyConfig(tableNames);
		AutoGenerator generator = this.ceateAutoGenerator(packageName,codePath,ds, strategy, globalConfig);
		
		TemplateConfig template = new TemplateConfig();
		template.setEntity(entityTemplate);
		template.setMapper(mapperTemplate);
		template.setService(serviceTemplate);
		template.setServiceImpl(serviceImplTemplate);
		
		generator.setTemplate(template);
		generator.execute();
		
//		generator = this.ceateAutoGenerator(packageName,codePath,ds, strategy, globalConfig);
//		template = new TemplateConfig();
//		template.setEntity(entityTemplate);
//		generator.setTemplate(template);
//		generator.execute();
		
		
		
	}
	
	public static void main(String[] args) {
		EntityCodeGenerator generator = new EntityCodeGenerator();
		generator.execute();
	}
}
