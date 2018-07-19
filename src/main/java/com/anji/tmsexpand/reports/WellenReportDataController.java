package com.anji.tmsexpand.reports;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.anji.tmsexpand.service.reports.IWellenReportService;
import com.anji.tmsexpand.utils.DateUtil;
import com.anji.tmsexpand.utils.USUtils;

/**
 * 波次报表数据交互控制层
 * 
 * @author Administrator
 *
 */
@RestController
public class WellenReportDataController {

	@Resource(name = "wellenReportService")
	private IWellenReportService service;

	private DecimalFormat df = new DecimalFormat("#.##");
	private static final String[] WEEKS = new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" };

	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("#.##");
		int a = 30000;
		int b = 1000;
		double c = (double) b / (double) a;
		System.out.println(Double.parseDouble(df.format(c)));
	}

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

		if ("day".equals(dateSymbol)) {
			// 总站点数
			int totalSiteNums = service.getTotalSiteNums(projectName, dateValue);
			// 总订单数
			int totalOrderNums = service.getTotalOrderNums(projectName, dateValue);
			// 总到达数
			int totalArriveNums = service.getTotalArriveNums(projectName, dateValue);
			// 应到站点数
			int shouldArriveNums = service.getShouldArrvieNums(projectName, dateValue);
			// 订单总迟到
			int totalLateNums = service.getTotalLateNums(projectName, dateValue);
			// 准点率
			double onTimePer = shouldArriveNums == 0 ? 100
					: Double.parseDouble(
							df.format((1-((double) totalLateNums / (double) shouldArriveNums))*100));
			// 破损件数
			int totalDestroyNums = service.getTotalDestroyNums(projectName, dateValue);
			// 破损率
			double destroyPer = totalOrderNums == 0 ? 0.00
					: Double.parseDouble(df.format((double) totalDestroyNums / (double) totalOrderNums * 100));

			map.put("totalSiteNums", totalSiteNums);
			map.put("totalArriveNums", totalArriveNums);
			map.put("totalLateNums", totalLateNums);
			map.put("shouldArriveNums", shouldArriveNums);
			map.put("onTimePer", onTimePer);
			map.put("destroyPer", destroyPer);
		} else {
			int year = Integer.parseInt(dateValue.split("-")[0]);
			int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
			String[] dates = DateUtil.getWeekDays(year, week);
			// 总站点数
			int totalSiteNums = 0;
			// 总订单数
			int totalOrderNums = 0;
			// 总到达数
			int totalArriveNums = 0;
			// 应到站点数
			int shouldArriveNums = 0;
			// 订单总迟到数
			int totalLateNums = 0;
			// 准点率
			double onTimePer = 0.0;
			// 破损件数
			int totalDestroyNums = 0;
			// 破损率
			double destroyPer = 0.0;

			for (int i = 0; i < dates.length; i++) {
				totalSiteNums += service.getTotalSiteNums(projectName, dates[i]);
				totalOrderNums += service.getTotalOrderNums(projectName, dates[i]);
				totalArriveNums += service.getTotalArriveNums(projectName, dates[i]);
				shouldArriveNums += service.getShouldArrvieNums(projectName, dates[i]);
				totalLateNums += service.getTotalLateNums(projectName, dates[i]);
				totalDestroyNums += service.getTotalDestroyNums(projectName, dates[i]);
			}

			onTimePer = shouldArriveNums == 0 ? 100
					: Double.parseDouble(
							df.format((1-((double) totalLateNums / (double) shouldArriveNums))*100));
			destroyPer = totalOrderNums == 0 ? 0.00
					: Double.parseDouble(df.format((double) totalDestroyNums / (double) totalOrderNums * 100));

			map.put("totalSiteNums", totalSiteNums);
			map.put("totalArriveNums", totalArriveNums);
			map.put("totalLateNums", totalLateNums);
			map.put("shouldArriveNums", shouldArriveNums);
			map.put("onTimePer", onTimePer);
			map.put("destroyPer", destroyPer);
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
		
		if ("day".equals(dateSymbol)) {
			List<ParamEntity> orderList = service.ListOrderRoute(model);
			List<ParamEntity> arriveList = service.ListArriveRoute(model);
			List<ParamEntity> lateList = service.ListLateRoute(model);
			List<String> name = new ArrayList<String>();
			List<String> order = new ArrayList<String>();
			List<String> arrive = new ArrayList<String>();
			List<String> late = new ArrayList<String>();
			for (int i = 0; i < orderList.size(); i++) {
				name.add(orderList.get(i).getRoute());
				order.add(orderList.get(i).getNum());

				boolean flag1 = false;
				for (int j = 0; j < arriveList.size(); j++) {
					if (arriveList.get(j).getRoute() == null) {
						if (orderList.get(i).getRoute() == null) {
							arrive.add(arriveList.get(j).getNum());
							flag1 = true;
							break;
						}
					} else {
						if (arriveList.get(j).getRoute().equals(orderList.get(i).getRoute())) {
							arrive.add(arriveList.get(j).getNum());
							flag1 = true;
							break;
						}
					}
				}
				if (!flag1) {
					arrive.add("0");
				}

				boolean flag2 = false;
				for (int j = 0; j < lateList.size(); j++) {
					if (lateList.get(j).getRoute() == null) {
						if (orderList.get(i).getRoute() == null) {
							late.add(lateList.get(j).getNum());
							flag2 = true;
							break;
						}
					} else {
						if (lateList.get(j).getRoute().equals(orderList.get(i).getRoute())) {
							late.add(lateList.get(j).getNum());
							flag2 = true;
							break;
						}
					}
				}
				if (!flag2) {
					late.add("0");
				}
			}

			map.put("name", name);
			map.put("order", order);
			map.put("arrive", arrive);
			map.put("late", late);
		} else {
			int year = Integer.parseInt(dateValue.split("-")[0]);
			int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
			String[] dates = DateUtil.getWeekDays(year, week);
			// 总订单数
			int totalOrderNums = 0;
			// 总到达数
			int totalArriveNums = 0;
			// 订单总迟到数
			int totalLateNums = 0;

			List<String> name = Arrays.asList(WEEKS);
			List<String> order = new ArrayList<String>();
			List<String> arrive = new ArrayList<String>();
			List<String> late = new ArrayList<String>();

			for (int i = 0; i < dates.length; i++) {
				totalOrderNums = service.getTotalOrderNums(projectName, dates[i]);
				totalArriveNums = service.getTotalArriveNums(projectName, dates[i]);
				totalLateNums = service.getTotalLateNums(projectName, dates[i]);
				order.add(totalOrderNums + "");
				arrive.add(totalArriveNums + "");
				late.add(totalLateNums + "");
			}

			map.put("name", name);
			map.put("order", order);
			map.put("arrive", arrive);
			map.put("late", late);
		}

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

		ParamEntity model = new ParamEntity();
		model.setDateValue(dateValue);
		model.setProjectName(projectName);
		// 查出所有的线路
		List<ParamEntity> routeList = service.ListRoute(model);

		List<ParamEntity> siteList = service.ListSiteRoute(model);
		List<ParamEntity> orderList = service.ListOrderRoute(model);
		List<ParamEntity> arriveList = service.ListArriveRoute(model);
		List<ParamEntity> shouldArriveList = service.ListShouldArriveRoute(model);
		List<ParamEntity> lateList = service.ListLateRoute(model);
		List<ParamEntity> destroyList = service.ListDestroyRoute(model);

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();

		for (int i = 0; i < routeList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("date", dateValue);
			if(routeList.get(i) != null) {
				map.put("route", routeList.get(i).getRoute());
			}else {
				routeList.set(i, new ParamEntity());
				map.put("route", "");
			}
			
			boolean flag1 = false;
			// 各路线总站点
			for (int j = 0; j < siteList.size(); j++) {
				if (siteList.get(j).getRoute() == null) {
					if (routeList.get(i).getRoute() == null) {
						map.put("siteNums", siteList.get(j).getNum());
						flag1 = true;
						break;
					}
				} else {
					if (siteList.get(j).getRoute().equals(routeList.get(i).getRoute())) {
						map.put("siteNums", siteList.get(j).getNum());
						flag1 = true;
						break;
					}
				}
			}
			if (!flag1) {
				map.put("siteNums", "0");
			}

			flag1 = false;
			// 各路线总到达
			for (int j = 0; j < arriveList.size(); j++) {
				if (arriveList.get(j).getRoute() == null) {
					if (routeList.get(i).getRoute() == null) {
						map.put("arriveNums", arriveList.get(j).getNum());
						flag1 = true;
						break;
					}
				} else {
					if (arriveList.get(j).getRoute().equals(routeList.get(i).getRoute())) {
						map.put("arriveNums", arriveList.get(j).getNum());
						flag1 = true;
						break;
					}
				}
			}
			if (!flag1) {
				map.put("arriveNums", "0");
			}

			flag1 = false;
			// 各路线总迟到
			for (int j = 0; j < lateList.size(); j++) {
				if (lateList.get(j).getRoute() == null) {
					if (routeList.get(i).getRoute() == null) {
						map.put("lateNums", lateList.get(j).getNum());
						flag1 = true;
						break;
					}
				} else {
					if (lateList.get(j).getRoute().equals(routeList.get(i).getRoute())) {
						map.put("lateNums", lateList.get(j).getNum());
						flag1 = true;
						break;
					}
				}
			}
			if (!flag1) {
				map.put("lateNums", "0");
			}

			flag1 = false;
			// 各路线总订单
			for (int j = 0; j < orderList.size(); j++) {
				if (orderList.get(j).getRoute() == null) {
					if (routeList.get(i).getRoute() == null) {
						map.put("orderNums", orderList.get(j).getNum());
						flag1 = true;
						break;
					}
				} else {
					if (orderList.get(j).getRoute().equals(routeList.get(i).getRoute())) {
						map.put("orderNums", orderList.get(j).getNum());
						flag1 = true;
						break;
					}
				}
			}
			if (!flag1) {
				map.put("orderNums", "0");
			}

			flag1 = false;
			// 各路线总破损
			for (int j = 0; j < destroyList.size(); j++) {
				if (destroyList.get(j).getRoute() == null) {
					if (routeList.get(i).getRoute() == null) {
						map.put("destroyNums", destroyList.get(j).getNum());
						flag1 = true;
						break;
					}
				} else {
					if (destroyList.get(j).getRoute().equals(routeList.get(i).getRoute())) {
						map.put("destroyNums", destroyList.get(j).getNum());
						flag1 = true;
						break;
					}
				}
			}
			if (!flag1) {
				map.put("destroyNums", "0");
			}
			
			flag1 = false;
			// 各路线应到达
			for (int j = 0; j < shouldArriveList.size(); j++) {
				if (shouldArriveList.get(j).getRoute() == null) {
					if (routeList.get(i).getRoute() == null) {
						map.put("shouldNums", shouldArriveList.get(j).getNum());
						flag1 = true;
						break;
					}
				} else {
					if (shouldArriveList.get(j).getRoute().equals(routeList.get(i).getRoute())) {
						map.put("shouldNums", shouldArriveList.get(j).getNum());
						flag1 = true;
						break;
					}
				}
			}
			if (!flag1) {
				map.put("shouldNums", "0");
			}

			// 准点率
			double onTimePer = Integer.parseInt(map.get("shouldNums")) == 0 ? 100
					: Double.parseDouble(
							df.format(
									(1-(double) Integer.parseInt(map.get("lateNums")) / (double) Integer.parseInt(map.get("shouldNums")))*100
									));
			map.put("onTimePer", onTimePer + "%");
			
			// 破损率
			double destroyPer = Integer.parseInt(map.get("orderNums")) == 0 ? 0.00
					: Double.parseDouble(df.format((double) Integer.parseInt(map.get("destroyNums")) / (double) Integer.parseInt(map.get("orderNums")) * 100));
			map.put("destroyPer", destroyPer + "%");
			
			result.add(map);
		}

//		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
//		if(limit != null) {
//			if(result.size() > limit * offset || result.size() == limit * offset) {
//				for(int i=limit * offset - limit; i<limit * offset; i++) {
//					resultList.add(result.get(i));
//				}
//			}else {
//				for(int i=limit * offset - limit; i<result.size(); i++) {
//					resultList.add(result.get(i));
//				}
//			}
//		}else {
//			resultList = result;
//		}
		
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
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		
		int year = Integer.parseInt(dateValue.split("-")[0]);
		int week = Integer.parseInt(dateValue.split("-")[1].substring(1));
		String[] dates = DateUtil.getWeekDays(year, week);
		for(int i=0;i<dates.length;i++) {
			Map<String, String> map = new HashMap<String, String>();
			// 总站点数
			int totalSiteNums = service.getTotalSiteNums(projectName, dates[i]);
			// 总订单数
			int totalOrderNums = service.getTotalOrderNums(projectName, dates[i]);
			// 总到达数
			int totalArriveNums = service.getTotalArriveNums(projectName, dates[i]);
			// 应到站点数
			int shouldArriveNums = service.getShouldArrvieNums(projectName, dates[i]);
			// 订单总迟到
			int totalLateNums = service.getTotalLateNums(projectName, dates[i]);
			// 准点率
			double onTimePer = shouldArriveNums == 0 ? 100
					: Double.parseDouble(
							df.format((1-((double) totalLateNums / (double) shouldArriveNums))*100));
			// 破损件数
			int totalDestroyNums = service.getTotalDestroyNums(projectName, dates[i]);
			// 破损率
			double destroyPer = totalOrderNums == 0 ? 0.00
					: Double.parseDouble(df.format((double) totalDestroyNums / (double) totalOrderNums * 100));
			
			map.put("weekNum", year + "年" + week + "周");
			map.put("day", WEEKS[i]);
			map.put("siteNums", totalSiteNums + "");
			map.put("arriveNums", totalArriveNums + "");
			map.put("shouldNums", shouldArriveNums + "");
			map.put("lateNums", totalLateNums + "");
			map.put("onTimePer", onTimePer + "%");
			map.put("destroyPer", destroyPer + "%");
			map.put("date", dates[i]);
			
			result.add(map);
		}

		js.put("rows", result);
		js.put("total", result.size());
		return js;
	}
	
	/**
	 * 加载日路线详细信息表格数据
	 * 
	 * @param limit
	 * @param offset
	 * @param departmentname
	 * @param statu
	 * @return
	 */
	@RequestMapping("/loadDayRouteInfoGrid")
	public JSONObject loadRouteInfoGrid(@RequestParam(value = "limit", required = false) Integer limit,
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
				list.addAll(service.listRouteWeekInfo(model));
			}
		}else {
			model.setDateValue(date);
			list = service.listRouteWeekInfo(model);
		}
		
		js.put("rows", list);
		js.put("total", list.size());
		return js;
	}
}
