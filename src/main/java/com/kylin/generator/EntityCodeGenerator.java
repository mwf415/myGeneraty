package com.kylin.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
		
		final String codePath ="E:\\mywork\\code";

		/**
		 * ims 新系统生成代码
		 */
//		final String dbUrl = "jdbc:mysql://39.107.253.198:3306/ims?autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
//		final String userName ="root";
//		final String password ="321";




		
//		final String packageName ="cn.onlov.cycle.core.dao";
		final String packageName ="com.neep.common.dao";
		final String entityTemplate ="/templates/kylin/entity.java.vm";
		final String mapperTemplate ="/templates/kylin/mapper.java.vm";
		final String serviceTemplate ="/templates/kylin/service.java.vm";
		final String serviceImplTemplate ="/templates/kylin/serviceImpl.java.vm";
		
		String[] tableNames = new String[] {
				"bid_iqr_quotation",
				"bid_supplier_info",
//				"d_iqr_attachment",
				"bid_attachment",
				"text_document_info",
				"text_sentence_comp_result",
				"text_doc_comp_result",
				"text_doc_comp_info_result"
		};
		
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
