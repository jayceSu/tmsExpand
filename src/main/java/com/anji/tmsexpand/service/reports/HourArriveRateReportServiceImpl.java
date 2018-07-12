package com.anji.tmsexpand.service.reports;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.tmsexpand.ParamEntity;
import com.anji.tmsexpand.mapper.sqlserver.reports.IHourArriveRateReportMapper;

@Service("hourArriveRateReportService")
public class HourArriveRateReportServiceImpl implements IHourArriveRateReportService {

	@Autowired
	private IHourArriveRateReportMapper mapper;

	@Override
	public int queryBaseInfo(ParamEntity model) {
		return mapper.queryBaseInfo(model);
	}

	@Override
	public List<Map<String, Object>> queryChartData(ParamEntity model) {
		return mapper.queryChartData(model);
	}
	
}
