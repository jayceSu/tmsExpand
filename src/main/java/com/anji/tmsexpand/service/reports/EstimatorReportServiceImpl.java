package com.anji.tmsexpand.service.reports;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.tmsexpand.ParamEntity;
import com.anji.tmsexpand.mapper.sqlserver.reports.IEstimatorReportMapper;

@Service("estimatorReportService")
public class EstimatorReportServiceImpl implements IEstimatorReportService {

	@Autowired
	private IEstimatorReportMapper mapper;

	@Override
	public Map<String, BigDecimal> queryDayBaseInfo(ParamEntity model) {
		return mapper.queryDayBaseInfo(model);
	}

	@Override
	public List<Map<String, BigDecimal>> listRouteData(ParamEntity model) {
		return mapper.listRouteData(model);
	}

	@Override
	public List<Map<String, String>> listRouteDayInfo(ParamEntity model) {
		return mapper.listRouteDayInfo(model);
	}

	@Override
	public List<Map<String, String>> listRouteMonthInfo(ParamEntity model) {
		return mapper.listRouteMonthInfo(model);
	}
	
	
}
