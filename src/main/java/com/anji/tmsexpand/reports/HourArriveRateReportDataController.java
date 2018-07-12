package com.anji.tmsexpand.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.anji.tmsexpand.ParamEntity;
import com.anji.tmsexpand.service.reports.IHourArriveRateReportService;
import com.anji.tmsexpand.utils.DateUtil;
import com.anji.tmsexpand.utils.USUtils;

/**
 * 小时到达率报表数据交互控制层
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/hourReport")
public class HourArriveRateReportDataController {
	@Resource(name="hourArriveRateReportService")
	private IHourArriveRateReportService service;
	

	private DecimalFormat df = new DecimalFormat("#.##");
	private static final String[] HOURS = new String[] { "24", "48", "72", "96", "120", "144", "168"
			, "192", "216", "240", "264", "288", "312"};
	
	/**
	 * 加载基础信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/loadBaseInfo")
	public Object loadBaseInfo(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Map paramMap = USUtils.getParameterMap(request);
		String dateSymbol = (String) paramMap.get("dateSymbol");
		String dateValue = (String) paramMap.get("dateValue");
		String projectName = (String) paramMap.get("projectName");
		ParamEntity model = new ParamEntity();
		model.setProjectName(projectName);

		int totalSiteNum = 0;
		int totalArriveNum = 0;
		
		if ("day".equals(dateSymbol)) {
			model.setDateValue(dateValue);
			model.setDateSymbol("site");
			totalSiteNum = service.queryBaseInfo(model);
			model.setDateSymbol("arrive");
			totalArriveNum = service.queryBaseInfo(model);
			map.put("totalSiteNum", totalSiteNum);
			map.put("totalArriveNum", totalArriveNum);
		} else if("week".equals(dateSymbol)) {
			int year = Integer.parseInt(dateValue.split("-")[0]);
			int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
			String[] dates = DateUtil.getWeekDays(year, week);
			for(int i=0;i<dates.length;i++) {
				model.setDateValue(dates[i]);
				model.setDateSymbol("site");
				totalSiteNum += service.queryBaseInfo(model);
				model.setDateSymbol("arrive");
				totalArriveNum += service.queryBaseInfo(model);
			}
			map.put("totalSiteNum", totalSiteNum);
			map.put("totalArriveNum", totalArriveNum);
		}

		return map;
	}
	
	/**
	 * 加载图表信息
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/loadChart")
	public Object loadChart(HttpServletRequest request) throws Exception {
		ParamEntity model = new ParamEntity();
		Map<String, Object> map = new HashMap<String, Object>();
		Map paramMap = USUtils.getParameterMap(request);
		String dateSymbol = (String) paramMap.get("dateSymbol");
		String dateValue = (String) paramMap.get("dateValue");
		String projectName = (String) paramMap.get("projectName");
		model.setDateValue(dateValue);
		model.setProjectName(projectName);
		List<Object> result = new ArrayList<Object>();
		
		if("day".equals(dateSymbol)) {
			List<Map<String, Object>> dataList = service.queryChartData(model);
			Object[] data = getData(dataList);
			result.add(data);
		}else {
			int year = Integer.parseInt(dateValue.split("-")[0]);
			int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
			String[] dates = DateUtil.getWeekDays(year, week);
			for(int i=0;i<dates.length;i++) {
				model.setDateValue(dates[i]);
				List<Map<String, Object>> dataList = service.queryChartData(model);
				Object[] data = getData(dataList);
				result.add(data);
			}
		}
		
		map.put("result", result);
		
		return map;
	}
	
	/**
	 * 加载日表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadDayGrid")
	public JSONObject loadDayGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "dateValue", required = false) String dateValue,
			@RequestParam(value = "projectName", required = false) String projectName) {
		JSONObject js = new JSONObject();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		ParamEntity model = new ParamEntity();
		model.setDateValue(dateValue);
		model.setProjectName(projectName);
		model.setDateSymbol("site");
		// 总站点数
		int totalSiteNum = service.queryBaseInfo(model);
		// 各小时累计站点数
		List<Map<String, Object>> dataList = service.queryChartData(model);
		Object[] data = getData(dataList);
		for(int i=0;i<data.length;i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", dateValue);
			map.put("hour", HOURS[i]);
			if(i==0) {
				map.put("arriveSite", data[i]);
			}else {
				map.put("arriveSite", (Integer)data[i] - (Integer)data[i-1]);
			}
			map.put("addArriveSite", data[i]);
			map.put("totalSite", totalSiteNum);
			if(totalSiteNum == 0) {
				map.put("hourRate", "0%");
			}else {
				map.put("hourRate", df.format((double)(Integer)map.get("arriveSite")/(double)totalSiteNum*100) + "%");
			}
			result.add(map);
		}
		
		js.put("rows", result);
		js.put("total", result.size());
		return js;
	}
	
	/**
	 * 加载周表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadWeekGrid")
	public JSONObject loadWeekGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "dateValue", required = false) String dateValue,
			@RequestParam(value = "projectName", required = false) String projectName) {
		JSONObject js = new JSONObject();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ParamEntity model = new ParamEntity();
		// 总站点数
		int totalSiteNum = 0;
		Integer[] arriveSites = new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0,0};
		
		int year = Integer.parseInt(dateValue.split("-")[0]);
		int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
		String[] dates = DateUtil.getWeekDays(year, week);
		for(int i=0;i<dates.length;i++) {
			model.setDateValue(dates[i]);
			model.setProjectName(projectName);
			model.setDateSymbol("site");
			// 总站点数
			totalSiteNum += service.queryBaseInfo(model);
			// 各小时累计站点数
			List<Map<String, Object>> dataList = service.queryChartData(model);
			Object[] data = getData(dataList);
			arriveSites[0] += (Integer)data[0];
			arriveSites[1] += (Integer)data[1] - (Integer)data[0];
			arriveSites[2] += (Integer)data[2] - (Integer)data[1];
			arriveSites[3] += (Integer)data[3] - (Integer)data[2];
			arriveSites[4] += (Integer)data[4] - (Integer)data[3];
			arriveSites[5] += (Integer)data[5] - (Integer)data[4];
			arriveSites[6] += (Integer)data[6] - (Integer)data[5];
			arriveSites[7] += (Integer)data[7] - (Integer)data[6];
			arriveSites[8] += (Integer)data[8] - (Integer)data[7];
			arriveSites[9] += (Integer)data[9] - (Integer)data[8];
			arriveSites[10] += (Integer)data[10] - (Integer)data[9];
			arriveSites[11] += (Integer)data[11] - (Integer)data[10];
			arriveSites[12] += (Integer)data[12] - (Integer)data[11];
			
		}
		
		for(int i=0;i<arriveSites.length;i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", year + "年" + week + "周");
			map.put("hour", HOURS[i]);
			map.put("arriveSite", arriveSites[i]);
			map.put("totalSite", totalSiteNum);
			if(totalSiteNum == 0) {
				map.put("hourRate", "0%");
			}else {
				map.put("hourRate", df.format((double)arriveSites[i]/(double)totalSiteNum*100) + "%");
			}
			result.add(map);
		}
		
		js.put("rows", result);
		js.put("total", result.size());
		return js;
	}
	
	/**
	 * 加载周小时表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadWeekHourGrid")
	public JSONObject loadWeekHourGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "dateValue", required = false) String dateValue,
			@RequestParam(value = "projectName", required = false) String projectName,
			@RequestParam(value = "hour", required = false) String hour) {
		JSONObject js = new JSONObject();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		ParamEntity model = new ParamEntity();
		model.setProjectName(projectName);
		int year = Integer.parseInt(dateValue.split("-")[0]);
		int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
		String[] dates = DateUtil.getWeekDays(year, week);
		for(int j=0;j<dates.length;j++) {
			model.setDateValue(dates[j]);
			// 总站点数
			int totalSiteNum = service.queryBaseInfo(model);
			// 各小时累计站点数
			List<Map<String, Object>> dataList = service.queryChartData(model);
			Object[] data = getData(dataList);
			for(int i=0;i<data.length;i++) {
				if("sss".equals(hour)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("date", dates[j]);
					map.put("hour", HOURS[i]);
					if(i==0) {
						map.put("arriveSite", data[i]);
					}else {
						map.put("arriveSite", (Integer)data[i] - (Integer)data[i-1]);
					}
					map.put("addArriveSite", data[i]);
					map.put("totalSite", totalSiteNum);
					if(totalSiteNum == 0) {
						map.put("hourRate", "0%");
					}else {
						map.put("hourRate", df.format((double)(Integer)map.get("arriveSite")/(double)totalSiteNum*100) + "%");
					}
					result.add(map);
				}else {
					if(!HOURS[i].equals(hour)) {
						continue;
					}
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("date", dates[j]);
					map.put("hour", HOURS[i]);
					if(i==0) {
						map.put("arriveSite", data[i]);
					}else {
						map.put("arriveSite", (Integer)data[i] - (Integer)data[i-1]);
					}
					map.put("addArriveSite", data[i]);
					map.put("totalSite", totalSiteNum);
					if(totalSiteNum == 0) {
						map.put("hourRate", "0%");
					}else {
						map.put("hourRate", df.format((double)(Integer)map.get("arriveSite")/(double)totalSiteNum*100) + "%");
					}
					result.add(map);
				}
			}
		}
		
		js.put("rows", result);
		js.put("total", result.size());
		return js;
	}
	
	public Object[] getData(List<Map<String, Object>> dataList) {
		Object[] data = new Object[]{0,0,0,0,0,0,0,0,0,0,0,0,0};
		for(int i=0;i<dataList.size();i++) {
			if(24 == (Integer)dataList.get(i).get("hour")) {
				data[0] = dataList.get(i).get("num");
			}else if(48 == (Integer)dataList.get(i).get("hour")) {
				data[1] = dataList.get(i).get("num");
			}else if(72 == (Integer)dataList.get(i).get("hour")) {
				data[2] = dataList.get(i).get("num");
			}else if(96 == (Integer)dataList.get(i).get("hour")) {
				data[3] = dataList.get(i).get("num");
			}else if(120 == (Integer)dataList.get(i).get("hour")) {
				data[4] = dataList.get(i).get("num");
			}else if(144 == (Integer)dataList.get(i).get("hour")) {
				data[5] = dataList.get(i).get("num");
			}else if(168 == (Integer)dataList.get(i).get("hour")) {
				data[6] = dataList.get(i).get("num");
			}else if(192 == (Integer)dataList.get(i).get("hour")) {
				data[7] = dataList.get(i).get("num");
			}else if(216 == (Integer)dataList.get(i).get("hour")) {
				data[8] = dataList.get(i).get("num");
			}else if(240 == (Integer)dataList.get(i).get("hour")) {
				data[9] = dataList.get(i).get("num");
			}else if(264 == (Integer)dataList.get(i).get("hour")) {
				data[10] = dataList.get(i).get("num");
			}else if(288 == (Integer)dataList.get(i).get("hour")) {
				data[11] = dataList.get(i).get("num");
			}else if(312 == (Integer)dataList.get(i).get("hour")) {
				data[12] = dataList.get(i).get("num");
			}
		}
		return data;
	}
	
}
