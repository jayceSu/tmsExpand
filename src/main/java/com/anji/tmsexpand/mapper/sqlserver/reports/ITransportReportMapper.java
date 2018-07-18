package com.anji.tmsexpand.mapper.sqlserver.reports;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.anji.tmsexpand.entity.DateEntity;

@Mapper
public interface ITransportReportMapper {

	/**
	 * 查询数据
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> listData(DateEntity model);
	
	/**
	 * 查询表格数据
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> listDataForTable(DateEntity model);
}
