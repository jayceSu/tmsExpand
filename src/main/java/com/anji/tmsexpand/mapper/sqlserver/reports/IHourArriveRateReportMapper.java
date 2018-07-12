package com.anji.tmsexpand.mapper.sqlserver.reports;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.anji.tmsexpand.ParamEntity;

@Mapper
public interface IHourArriveRateReportMapper {

	/**
	 * 查询基础数据
	 * @param model
	 * @return
	 */
	int queryBaseInfo(ParamEntity model);
	
	/**
	 * 查找图表数据
	 * @param model
	 * @return
	 */
	List<Map<String, Object>> queryChartData(ParamEntity model);
}
