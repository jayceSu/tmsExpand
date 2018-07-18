package com.anji.tmsexpand.reports;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anji.tmsexpand.utils.DateUtil;

/**
 * 报表跳转控制层
 * 
 * @author Administrator
 *
 */
@Controller
public class ReportSkipPage {
	protected static final Logger logger = LoggerFactory.getLogger(ReportSkipPage.class);

	private static Date date;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 跳转至波次报表
	 * 
	 * @return
	 */
	@RequestMapping("/wellenReport")
	public String wellenReport(@PathParam(value = "projectName") String projectName, Model m) {
		date = new Date();
		int num = DateUtil.getWeekNum(date);
		String weekNum = num < 10 ? "0" + num : num + "";
		m.addAttribute("projectName", projectName);
		m.addAttribute("nowDay", sdf.format(date));
		m.addAttribute("nowWeek", sdf.format(date).substring(0, 5) + "W" + weekNum);
		
//		logger.info(projectName);
//		logger.info(sdf.format(date));
//		logger.info(sdf.format(date).substring(0, 5) + "W" + weekNum);
		
		return "report/wellenReport";
	}
	
	/**
	 * 跳转至日路线详细信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/dayRouteInfo")
	public String dayRouteInfo(@PathParam(value = "route") String route,
			@PathParam(value = "projectName") String projectName,
			@PathParam(value = "dateValue") String dateValue,
			Model m) {
		m.addAttribute("route", route);
		m.addAttribute("projectName", projectName);
		m.addAttribute("dateValue", dateValue);
		
		return "report/dayRouteInfo";
	}
	
	/**
	 * 跳转至周路线详细信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/weekRouteInfo")
	public String weekRouteInfo(@PathParam(value = "date") String date,
			@PathParam(value = "projectName") String projectName,
			@PathParam(value = "dateValue") String dateValue,
			Model m) {
		m.addAttribute("date", date);
		m.addAttribute("projectName", projectName);
		m.addAttribute("dateValue", dateValue);
		
		return "report/weekRouteInfo";
	}
	
	/**
	 * 跳转至估方报表
	 * 
	 * @return
	 */
	@RequestMapping("/estimatorReport")
	public String estimatorReport(@PathParam(value = "projectName") String projectName, Model m) {
		date = new Date();
		int num = DateUtil.getWeekNum(date);
		String weekNum = num < 10 ? "0" + num : num + "";
		m.addAttribute("projectName", projectName);
		m.addAttribute("nowDay", sdf.format(date));
		m.addAttribute("nowWeek", sdf.format(date).substring(0, 5) + "W" + weekNum);
		m.addAttribute("nowYear", sdf.format(date).substring(0, 4));
		
		return "report/estimatorReport";
	}
	
	/**
	 * 跳转至估方日路线详细信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/dayGFRouteInfo")
	public String dayGFRouteInfo(@PathParam(value = "route") String route,
			@PathParam(value = "projectName") String projectName,
			@PathParam(value = "dateValue") String dateValue,
			Model m) {
		m.addAttribute("route", route);
		m.addAttribute("projectName", projectName);
		m.addAttribute("dateValue", dateValue);
		
		return "report/dayGFRouteInfo";
	}
	
	/**
	 * 跳转至估方周路线详细信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/weekGFRouteInfo")
	public String weekGFRouteInfo(@PathParam(value = "date") String date,
			@PathParam(value = "projectName") String projectName,
			@PathParam(value = "dateValue") String dateValue,
			Model m) {
		m.addAttribute("date", date);
		m.addAttribute("projectName", projectName);
		m.addAttribute("dateValue", dateValue);
		
		return "report/weekGFRouteInfo";
	}
	
	/**
	 * 跳转至估方年路线详细信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/yearGFRouteInfo")
	public String yearGFRouteInfo(@PathParam(value = "date") String date,
			@PathParam(value = "projectName") String projectName,
			@PathParam(value = "dateValue") String dateValue,
			Model m) {
		m.addAttribute("date", date);
		m.addAttribute("projectName", projectName);
		m.addAttribute("dateValue", dateValue);
		
		return "report/yearGFRouteInfo";
	}
	
	/**
	 * 跳转至小时到达率报表
	 * 
	 * @return
	 */
	@RequestMapping("/hourArriveRateReport")
	public String hourArriveRateReport(@PathParam(value = "projectName") String projectName, Model m) {
		date = new Date();
		int num = DateUtil.getWeekNum(date);
		String weekNum = num < 10 ? "0" + num : num + "";
		m.addAttribute("projectName", projectName);
		m.addAttribute("nowDay", sdf.format(date));
		m.addAttribute("nowWeek", sdf.format(date).substring(0, 5) + "W" + weekNum);
		
		return "report/hourArriveRateReport";
	}

	/**
	 * 跳转至周小时到达率详细信息页面
	 * 
	 * @return
	 */
	@RequestMapping("/hourWeekInfo")
	public String hourWeekInfo(@PathParam(value = "hour") String hour,
			@PathParam(value = "projectName") String projectName,
			@PathParam(value = "dateValue") String dateValue,
			Model m) {
		m.addAttribute("hour", hour);
		m.addAttribute("projectName", projectName);
		m.addAttribute("dateValue", dateValue);
		
		return "report/weekHourInfo";
	}
	
	/**
	 * 跳转至运输服务水平报表
	 * 
	 * @return
	 */
	@RequestMapping("/transportReport")
	public String transportReport(@PathParam(value = "projectName") String projectName, Model m) {
		date = new Date();
		m.addAttribute("projectName", projectName);
		m.addAttribute("nowDay", sdf.format(date));
		// 默认前第7天
		m.addAttribute("sevenDaysAgo", DateUtil.getPastDate(7));
		// 默认前第30天
		m.addAttribute("thirtyDaysAgo", DateUtil.getPastDate(30));
		// 默认前第90天
		m.addAttribute("ninetyDaysAgo", DateUtil.getPastDate(90));
		return "report/transportReport";
	}
	
	
}
