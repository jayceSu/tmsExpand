package com.anji.tmsexpand.service.reports;

import java.util.List;
import java.util.Map;

import com.anji.tmsexpand.ParamEntity;

public interface IHourArriveRateReportService {

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
