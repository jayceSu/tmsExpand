package com.anji.tmsexpand.service.reports;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.tmsexpand.ParamEntity;
import com.anji.tmsexpand.mapper.sqlserver.reports.IWellenReportMapper;


@Service("wellenReportService")
public class WellenReportServiceImpl implements IWellenReportService {

	@Autowired
	private IWellenReportMapper mapper;

	@Override
	public int getTotalSiteNums(String projectName, String dateValue) {
		return mapper.getTotalSiteNums(projectName, dateValue);
	}

	@Override
	public int getTotalArriveNums(String projectName, String dateValue) {
		return mapper.getTotalArriveNums(projectName, dateValue);
	}

	@Override
	public int getTotalLateNums(String projectName, String dateValue) {
		return mapper.getTotalLateNums(projectName, dateValue);
	}

	@Override
	public int getTotalDestroyNums(String projectName, String dateValue) {
		return mapper.getTotalDestroyNums(projectName, dateValue);
	}

	@Override
	public int getTotalOrderNums(String projectName, String dateValue) {
		return mapper.getTotalOrderNums(projectName, dateValue);
	}

	@Override
	public List<ParamEntity> ListOrderRoute(ParamEntity model) {
		return mapper.ListOrderRoute(model);
	}

	@Override
	public List<ParamEntity> ListArriveRoute(ParamEntity model) {
		return mapper.ListArriveRoute(model);
	}

	@Override
	public List<ParamEntity> ListLateRoute(ParamEntity model) {
		return mapper.ListLateRoute(model);
	}

	@Override
	public List<ParamEntity> ListRoute(ParamEntity model) {
		return mapper.ListRoute(model);
	}

	@Override
	public List<ParamEntity> ListSiteRoute(ParamEntity model) {
		return mapper.ListSiteRoute(model);
	}

	@Override
	public List<ParamEntity> ListDestroyRoute(ParamEntity model) {
		return mapper.ListDestroyRoute(model);
	}

	@Override
	public List<Map<String, String>> listRouteDayInfo(ParamEntity model) {
		return mapper.listRouteDayInfo(model);
	}

	@Override
	public List<Map<String, String>> listRouteWeekInfo(ParamEntity model) {
		return mapper.listRouteWeekInfo(model);
	}

	@Override
	public int getShouldArrvieNums(String projectName, String dateValue) {
		return mapper.getShouldArrvieNums(projectName, dateValue);
	}

	@Override
	public Object getOnTimePer(String projectName, String dateValue) {
		return mapper.getOnTimePer(projectName, dateValue);
	}

	@Override
	public List<ParamEntity> ListShouldArriveRoute(ParamEntity model) {
		return mapper.ListShouldArriveRoute(model);
	}
	
	
}
