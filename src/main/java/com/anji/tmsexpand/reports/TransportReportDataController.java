package com.anji.tmsexpand.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anji.tmsexpand.entity.DateEntity;
import com.anji.tmsexpand.service.reports.ITransportReportService;
import com.anji.tmsexpand.utils.USUtils;

/**
 * 运输服务水平报表数据交互控制层
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/transportReport")
public class TransportReportDataController {

	@Resource(name="transportReportService")
	private ITransportReportService service;

	/**
	 * 加载小时覆盖率（累计）图表
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/loadHourCoverAddChart")
	public Object loadHourCoverAddChart(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map paramMap = USUtils.getParameterMap(request);
		String projectName = (String) paramMap.get("projectName");
		String type = (String) paramMap.get("type");
		String timeBegin1 = (String) paramMap.get("timeBegin1");
		String timeEnd1 = (String) paramMap.get("timeEnd1");
		String timeBegin2 = (String) paramMap.get("timeBegin2");
		String timeEnd2 = (String) paramMap.get("timeEnd2");
		String timeBegin3 = (String) paramMap.get("timeBegin3");
		String timeEnd3 = (String) paramMap.get("timeEnd3");

		DateEntity model = new DateEntity();
		model.setProjectName(projectName);
		
		List name = new ArrayList();
		List data1 = new ArrayList();
		List data2 = new ArrayList();
		List data3 = new ArrayList();
		
		if(!StringUtils.isEmpty(timeBegin1) && !StringUtils.isEmpty(timeEnd1)) {
			model.setTimeBegin(timeBegin1);
			model.setTimeEnd(timeEnd1);
			List<Map<String, Object>> list = service.listData(model);
			for(int i=0;i<list.size();i++) {
				name.add(list.get(i).get("endHour"));
				if("hourLocAll".equals(type)) {
					data1.add(list.get(i).get("hourLocAll"));
				}else if("hourLoc".equals(type)) {
					data1.add(list.get(i).get("hourLoc"));
				}else if("averageKm".equals(type)) {
					data1.add(list.get(i).get("averageKm"));
				}else if("cou".equals(type)) {
					data1.add(list.get(i).get("cou"));
				}
			}
		}
		
		if(!StringUtils.isEmpty(timeBegin2) && !StringUtils.isEmpty(timeEnd2)) {
			model.setTimeBegin(timeBegin2);
			model.setTimeEnd(timeEnd2);
			List<Map<String, Object>> list = service.listData(model);
			for(int i=0;i<list.size();i++) {
				if(name.size() < list.size()) {
					name.add(list.get(i).get("endHour"));
				}
				if("hourLocAll".equals(type)) {
					data2.add(list.get(i).get("hourLocAll"));
				}else if("hourLoc".equals(type)) {
					data2.add(list.get(i).get("hourLoc"));
				}else if("averageKm".equals(type)) {
					data2.add(list.get(i).get("averageKm"));
				}else if("cou".equals(type)) {
					data2.add(list.get(i).get("cou"));
				}
			}
		}
		
		if(!StringUtils.isEmpty(timeBegin3) && !StringUtils.isEmpty(timeEnd3)) {
			model.setTimeBegin(timeBegin3);
			model.setTimeEnd(timeEnd3);
			List<Map<String, Object>> list = service.listData(model);
			for(int i=0;i<list.size();i++) {
				if(name.size() < list.size()) {
					name.add(list.get(i).get("endHour"));
				}
				if("hourLocAll".equals(type)) {
					data3.add(list.get(i).get("hourLocAll"));
				}else if("hourLoc".equals(type)) {
					data3.add(list.get(i).get("hourLoc"));
				}else if("averageKm".equals(type)) {
					data3.add(list.get(i).get("averageKm"));
				}else if("cou".equals(type)) {
					data3.add(list.get(i).get("cou"));
				}
			}
		}


		map.put("name", name);
		map.put("data1", data1);
		map.put("data2", data2);
		map.put("data3", data3);
		return map;
	}
	
	/**
	 * 加载表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadGrid")
	public JSONObject loadGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "timeBegin1", required = false) String timeBegin1,
			@RequestParam(value = "timeEnd1", required = false) String timeEnd1,
			@RequestParam(value = "timeBegin2", required = false) String timeBegin2,
			@RequestParam(value = "timeEnd2", required = false) String timeEnd2,
			@RequestParam(value = "timeBegin3", required = false) String timeBegin3,
			@RequestParam(value = "timeEnd3", required = false) String timeEnd3,
			@RequestParam(value = "projectName", required = false) String projectName) {
		JSONObject js = new JSONObject();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		DateEntity model = new DateEntity();
		model.setProjectName(projectName);
		
		if(!StringUtils.isEmpty(timeBegin1) && !StringUtils.isEmpty(timeEnd1)) {
			model.setTimeBegin(timeBegin1);
			model.setTimeEnd(timeEnd1);
			model.setDateSymbol("日期段1");
			List<Map<String, Object>> list = service.listDataForTable(model);
			result.addAll(list);
		}
		
		if(!StringUtils.isEmpty(timeBegin2) && !StringUtils.isEmpty(timeEnd2)) {
			model.setTimeBegin(timeBegin2);
			model.setTimeEnd(timeEnd2);
			model.setDateSymbol("日期段2");
			List<Map<String, Object>> list = service.listDataForTable(model);
			result.addAll(list);
		}
		
		if(!StringUtils.isEmpty(timeBegin3) && !StringUtils.isEmpty(timeEnd3)) {
			model.setTimeBegin(timeBegin3);
			model.setTimeEnd(timeEnd3);
			model.setDateSymbol("日期段3");
			List<Map<String, Object>> list = service.listDataForTable(model);
			result.addAll(list);
		}

		
		js.put("rows", result);
		js.put("total", result.size());
		return js;
	}
}
