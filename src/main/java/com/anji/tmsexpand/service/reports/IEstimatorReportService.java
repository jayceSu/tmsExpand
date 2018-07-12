package com.anji.tmsexpand.service.reports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.anji.tmsexpand.ParamEntity;

public interface IEstimatorReportService {

	/**
	 * 查询日基础信息
	 * @param model
	 * @return
	 */
	Map<String, BigDecimal> queryDayBaseInfo(ParamEntity model);

	/**
	 * 查询线路方数
	 * @param model
	 * @return
	 */
	List<Map<String, BigDecimal>> listRouteData(ParamEntity model);

	/**
	 * 查询线路信息
	 * @param model
	 * @return
	 */
	List<Map<String, String>> listRouteDayInfo(ParamEntity model);

	/**
	 * 加载月表格路线数据
	 * @param model
	 * @return
	 */
	List<Map<String, String>> listRouteMonthInfo(ParamEntity model);

}
