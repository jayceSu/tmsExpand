package com.anji.tmsexpand.service.reports;

import java.util.List;
import java.util.Map;

import com.anji.tmsexpand.entity.DateEntity;

public interface ITransportReportService {

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
