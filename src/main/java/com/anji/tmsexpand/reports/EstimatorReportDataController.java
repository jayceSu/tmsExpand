package com.anji.tmsexpand.reports;

import java.math.BigDecimal;
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
import com.anji.tmsexpand.service.reports.IEstimatorReportService;
import com.anji.tmsexpand.utils.DateUtil;
import com.anji.tmsexpand.utils.USUtils;

/**
 * 估方报表数据交互控制层
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/estReport")
public class EstimatorReportDataController {
	
	@Resource(name = "estimatorReportService")
	private IEstimatorReportService service;

	private DecimalFormat df = new DecimalFormat("#.##");
	private static final String[] WEEKS = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };
	private static final String[] MONTHS = new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月" };
	private static final String[] MONTH_DATE = new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
	
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
		model.setDateSymbol(dateSymbol);

		if ("day".equals(dateSymbol) || "year".equals(dateSymbol)) {
			model.setDateValue(dateValue);
			Map<String, BigDecimal> resultMap = service.queryDayBaseInfo(model);
			if(resultMap == null) {
				map.put("ygfs", 0);
				map.put("sjfs", 0);
				map.put("ygzql", "0");
			}else {
				map.put("ygfs", df.format(resultMap.get("gf").doubleValue()));
				map.put("sjfs", df.format(resultMap.get("sf")));
				map.put("ygzql", resultMap.get("sf").doubleValue() == 0 ? 0 :
					df.format(Math.abs(resultMap.get("sf").doubleValue() - resultMap.get("gf").doubleValue())/resultMap.get("sf").doubleValue()*100));
			}
		} else if("week".equals(dateSymbol)) {
			int year = Integer.parseInt(dateValue.split("-")[0]);
			int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
			String[] dates = DateUtil.getWeekDays(year, week);
			double gf = 0.0;
			double sf = 0.0;
			for(int i=0;i<dates.length;i++) {
				model.setDateValue(dates[i]);
				Map<String, BigDecimal> resultMap = service.queryDayBaseInfo(model);
				if(resultMap == null) {
					continue;
				}
				gf += resultMap.get("gf").doubleValue();
				sf += resultMap.get("sf").doubleValue();
			}
			map.put("ygfs", df.format(gf));
			map.put("sjfs", df.format(sf));
			if(sf == 0) {
				map.put("ygzql", 0.0);
			}else {
				map.put("ygzql", df.format(Math.abs(sf - gf)/sf*100));
			}
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
		model.setProjectName(projectName);
		model.setDateSymbol(dateSymbol);
		
		List<String> name = new ArrayList<String>();
		List<Integer> gf = new ArrayList<Integer>();
		List<Integer> sf = new ArrayList<Integer>();
		
		if("day".equals(dateSymbol)) {
			model.setDateValue(dateValue);
			List<Map<String, BigDecimal>> rs = service.listRouteData(model);
			for(int i=0;i<rs.size();i++) {
				name.add(rs.get(i).get("route") + "");
				gf.add(rs.get(i).get("gf").intValue());
				sf.add(rs.get(i).get("sf").intValue());
			}
		}else if("week".equals(dateSymbol)) {
			int year = Integer.parseInt(dateValue.split("-")[0]);
			int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
			String[] dates = DateUtil.getWeekDays(year, week);
			for(int i=0;i<dates.length;i++) {
				model.setDateValue(dates[i]);
				Map<String, BigDecimal> rs = service.queryDayBaseInfo(model);
				name.add(WEEKS[i]);
				if(rs == null) {
					gf.add(0);
					sf.add(0);
				}else {
					gf.add(rs.get("gf").intValue());
					sf.add(rs.get("sf").intValue());
				}
			}
		}else {
			for(int i=0;i<MONTHS.length;i++) {
				model.setDateSymbol("month");
				model.setDateValue(dateValue + "-" + MONTH_DATE[i]);
				Map<String, BigDecimal> rs = service.queryDayBaseInfo(model);
				name.add(MONTHS[i]);
				if(rs == null) {
					gf.add(0);
					sf.add(0);
				}else {
					gf.add(rs.get("gf").intValue());
					sf.add(rs.get("sf").intValue());
				}
			}
		}
		
		map.put("name", name);
		map.put("gf", gf);
		map.put("sf", sf);
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
		
		List<Map<String, BigDecimal>> rs = service.listRouteData(model);
		for(int i=0;i<rs.size();i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("date", dateValue);
			map.put("route", rs.get(i).get("route"));
			map.put("gf", df.format(rs.get(i).get("gf").doubleValue()));
			map.put("sf", df.format(rs.get(i).get("sf").doubleValue()));
			if(rs.get(i).get("sf").doubleValue() == 0) {
				map.put("correctRate", 0 + "%");
			}else {
				map.put("correctRate", df.format(Math.abs(rs.get(i).get("sf").doubleValue() - rs.get(i).get("gf").doubleValue())/rs.get(i).get("sf").doubleValue()*100) + "%");
			}
			map.put("thingsValue", df.format(rs.get(i).get("hz").doubleValue()));
			map.put("volumeRatio", rs.get(i).get("sf").doubleValue() == 0 ? (0 + "%" ) : 
				df.format(rs.get(i).get("hz").doubleValue()/rs.get(i).get("sf").doubleValue()*100) + "%");
			result.add(map);
		}
		
		js.put("rows", result);
		js.put("total", result.size());
		return js;
	}
	
	/**
	 * 加载日表格路线数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadDayRouteInfoGrid")
	public JSONObject loadDayRouteInfoGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "dateValue", required = false) String dateValue,
			@RequestParam(value = "projectName", required = false) String projectName,
			@RequestParam(value = "route", required = false) String route) {
		JSONObject js = new JSONObject();

		ParamEntity model = new ParamEntity();
		model.setDateValue(dateValue);
		model.setProjectName(projectName);
		model.setRoute(route);
		List<Map<String, String>> list = service.listRouteDayInfo(model);
		js.put("rows", list);
		js.put("total", list.size());
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
			@RequestParam(value = "projectName", required = false) String projectName,
			@RequestParam(value = "route", required = false) String route) {
		JSONObject js = new JSONObject();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ParamEntity model = new ParamEntity();
		model.setProjectName(projectName);
		model.setDateSymbol("week");
		
		int year = Integer.parseInt(dateValue.split("-")[0]);
		int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
		String[] dates = DateUtil.getWeekDays(year, week);
		
		for(int i=0;i<dates.length;i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			model.setDateValue(dates[i]);
			Map<String, BigDecimal> resultMap = service.queryDayBaseInfo(model);
			if(resultMap == null) {
				map.put("weekNum", year + "年" + week + "周");
				map.put("day", WEEKS[i]);
				map.put("date", dates[i]);
				map.put("gf", 0);
				map.put("sf", 0);
				map.put("thingsValue", 0);
				map.put("correctRate", 0 + "%");
				map.put("volumeRatio", 0 + "%");
			}else {
				map.put("weekNum", year + "年" + week + "周");
				map.put("day", WEEKS[i]);
				map.put("date", dates[i]);
				map.put("gf", df.format(resultMap.get("gf").doubleValue()));
				map.put("sf", df.format(resultMap.get("sf").doubleValue()));
				map.put("thingsValue", df.format(resultMap.get("thingsValue").doubleValue()));
				if(resultMap.get("sf").doubleValue() == 0) {
					map.put("correctRate", 0.0 + "%");
					map.put("volumeRatio", 0.0 + "%");
				}else {
					map.put("correctRate", df.format(Math.abs(resultMap.get("sf").doubleValue() - resultMap.get("gf").doubleValue())/resultMap.get("sf").doubleValue()*100) + "%");
					map.put("volumeRatio", df.format(resultMap.get("thingsValue").doubleValue()/resultMap.get("sf").doubleValue()*100) + "%");
				}
			}
			
			list.add(map);
		}
		
		js.put("rows", list);
		js.put("total", list.size());
		return js;
	}
	
	/**
	 * 加载周路线详细信息表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadWeekRouteInfoGrid")
	public JSONObject loadWeekRouteInfoGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "dateValue", required = false) String dateValue,
			@RequestParam(value = "projectName", required = false) String projectName,
			@RequestParam(value = "date", required = false) String date) {
		JSONObject js = new JSONObject();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ParamEntity model = new ParamEntity();
		model.setProjectName(projectName);
		if("sss".equals(date)) {
			int year = Integer.parseInt(dateValue.split("-")[0]);
			int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
			String[] dates = DateUtil.getWeekDays(year, week);
			for(int i=0;i<dates.length;i++) {
				model.setDateValue(dates[i]);
				model.setRoute("sss");
				list.addAll(service.listRouteDayInfo(model));
			}
		}else {
			model.setDateValue(date);
			model.setRoute("sss");
			list = service.listRouteDayInfo(model);
		}
		
		js.put("rows", list);
		js.put("total", list.size());
		return js;
	}
	
	/**
	 * 加载年表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadYearGrid")
	public JSONObject loadYearGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "dateValue", required = false) String dateValue,
			@RequestParam(value = "projectName", required = false) String projectName) {
		JSONObject js = new JSONObject();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ParamEntity model = new ParamEntity();
		model.setProjectName(projectName);
		model.setDateSymbol("month");
		
		for(int i=0;i<MONTH_DATE.length;i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			model.setDateValue(dateValue + "-" + MONTH_DATE[i]);
			Map<String, BigDecimal> resultMap = service.queryDayBaseInfo(model);
			if(resultMap == null) {
				map.put("month", dateValue + "年" + MONTHS[i]);
				map.put("date", dateValue + "-" + MONTH_DATE[i]);
				map.put("gf", 0);
				map.put("sf", 0);
				map.put("thingsValue", 0);
				map.put("correctRate", 0.0 + "%");
				map.put("volumeRatio", 0.0 + "%");
			}else {
				map.put("month", dateValue + "年" + MONTHS[i]);
				map.put("date", dateValue + "-" + MONTH_DATE[i]);
				map.put("gf", df.format(resultMap.get("gf").doubleValue()));
				map.put("sf", df.format(resultMap.get("sf").doubleValue()));
				map.put("thingsValue", df.format(resultMap.get("thingsValue").doubleValue()));
				if(resultMap.get("sf").doubleValue() == 0) {
					map.put("correctRate", 0.0 + "%");
					map.put("volumeRatio", 0.0 + "%");
				}else {
					map.put("correctRate", df.format(Math.abs(resultMap.get("sf").doubleValue() - resultMap.get("gf").doubleValue())/resultMap.get("sf").doubleValue()*100) + "%");
					map.put("volumeRatio", df.format(resultMap.get("thingsValue").doubleValue()/resultMap.get("sf").doubleValue()*100) + "%");
				}
			}
			
			list.add(map);
		}
		
		js.put("rows", list);
		js.put("total", list.size());
		return js;
	}
	
	/**
	 * 加载年路线详细信息表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadYearRouteInfoGrid")
	public JSONObject loadYearRouteInfoGrid(@RequestParam(value = "limit", required = false) Integer limit,
			@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "dateValue", required = false) String dateValue,
			@RequestParam(value = "projectName", required = false) String projectName,
			@RequestParam(value = "date", required = false) String date) {
		JSONObject js = new JSONObject();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ParamEntity model = new ParamEntity();
		model.setProjectName(projectName);
		model.setDateSymbol(date);
		if("sss".equals(date)) {
			model.setDateValue(dateValue);
		}else {
			model.setDateValue(date);
		}
		
		list = service.listRouteMonthInfo(model);
		
		js.put("rows", list);
		js.put("total", list.size());
		return js;
	}
}
