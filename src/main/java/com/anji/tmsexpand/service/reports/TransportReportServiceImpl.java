package com.anji.tmsexpand.service.reports;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anji.tmsexpand.entity.DateEntity;
import com.anji.tmsexpand.mapper.sqlserver.reports.ITransportReportMapper;

@Service("transportReportService")
public class TransportReportServiceImpl implements ITransportReportService {
	
	@Autowired
	private ITransportReportMapper mapper;

	@Override
	public List<Map<String, Object>> listData(DateEntity model) {
		return mapper.listData(model);
	}

	@Override
	public List<Map<String, Object>> listDataForTable(DateEntity model) {
		return mapper.listDataForTable(model);
	}

}
