package com.anji.tmsexpand.service.reports;

import java.util.List;
import java.util.Map;

import com.anji.tmsexpand.ParamEntity;

public interface IWellenReportService {

	/**
	 * 获取总站点数
	 * @param projectName
	 * @param dateValue
	 * @return
	 */
	int getTotalSiteNums(String projectName, String dateValue);

	/**
	 * 获取总到达数
	 * @param projectName
	 * @param dateValue
	 * @return
	 */
	int getTotalArriveNums(String projectName, String dateValue);

	/**
	 * 获取总迟到数
	 * @param projectName
	 * @param dateValue
	 * @return
	 */
	int getTotalLateNums(String projectName, String dateValue);

	/**
	 * 获取破损件数
	 * @param projectName
	 * @param dateValue
	 * @return
	 */
	int getTotalDestroyNums(String projectName, String dateValue);

	/**
	 * 获取总订单数
	 * @param projectName
	 * @param dateValue
	 * @return
	 */
	int getTotalOrderNums(String projectName, String dateValue);

	/**
	 * 路线订单数
	 * @param model
	 * @return
	 */
	List<ParamEntity> ListOrderRoute(ParamEntity model);

	/**
	 * 路线到达数
	 * @param model
	 * @return
	 */
	List<ParamEntity> ListArriveRoute(ParamEntity model);

	/**
	 * 线路迟到数
	 * @param model
	 * @return
	 */
	List<ParamEntity> ListLateRoute(ParamEntity model);

	/**
	 * 获取所有路线
	 * @param model 
	 * @return
	 */
	List<ParamEntity> ListRoute(ParamEntity model);

	/**
	 * 路线站点数
	 * @param model
	 * @return
	 */
	List<ParamEntity> ListSiteRoute(ParamEntity model);

	/**
	 * 线路破损数
	 * @param model
	 * @return
	 */
	List<ParamEntity> ListDestroyRoute(ParamEntity model);

	/**
	 * 日线路详细信息
	 * @param model
	 * @return
	 */
	List<Map<String, String>> listRouteDayInfo(ParamEntity model);

	/**
	 * 周线路详细信息
	 * @param model
	 * @return
	 */
	List<Map<String, String>> listRouteWeekInfo(ParamEntity model);
}
