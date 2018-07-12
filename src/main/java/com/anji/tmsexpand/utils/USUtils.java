package com.anji.tmsexpand.utils;

import java.io.BufferedReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 
* @ClassName: ReflectTools
* @Description: TODO(服务工具类)
* @author kangz
* @date 2017-8-25 下午7:22:59
*
 */
public class USUtils {

	/**
	 * invoke 反射调用方法
	 * 
	 * @param owner
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object invokeMethod(Object owner, String methodName,
			Object[] args) throws Exception {
		Class ownerClass = owner.getClass();
		Class[] argsClass = new Class[args.length];
		for (int i = 0, j = args.length; i < j; i++) {
			if (args[i] instanceof Map) {
				argsClass[i] = Map.class;
				continue;
			}
			argsClass[i] = args[i].getClass();
		}
		Method method = ownerClass.getMethod(methodName, argsClass);
		return method.invoke(owner, args);
	}
	
	/**
	 * 
	* @Title: instanceObject
	* @Description: TODO(获取spring bean 和 dubbox rpc服务实例)
	* @param @param serverType
	* @param @param classPath
	* @param @param extend1
	* @param @return
	* @param @throws Exception    设定文件
	* @return Object    返回类型
	* @throws
	 */
	public static Object instanceObject(String serverType, String classPath,
			String extend1) throws Exception {
		Object object = null;
		if ("RPC".equalsIgnoreCase(serverType)) {
//			SpringRpcFactoryProvider p = LocateServiceFactory
//					.getRpcFactoryProvider();
//			SpringRpcConsumerBean rpcBean = p.createConsumerBean();
//			if (rpcBean != null) {
//				rpcBean.setGroup(FrameworkPropertyConfigurer.processGroup(null));
//				rpcBean.setInterfaceClass(Class.forName(classPath));
//				rpcBean.afterPropertiesSet();
//				object = rpcBean.getObject();
//			}
		} else if ("SBEAN".equalsIgnoreCase(serverType)) {
			object = ContextLoader.getCurrentWebApplicationContext().getBean(
					extend1);
		}
		return object;
	}

	@SuppressWarnings("rawtypes")
	public static Object list2Json(List list) {
		Object str = JSON.toJSON(list);
		return str;
	}

	public static String getStackMsg(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackArray = e.getStackTrace();
		for (int i = 0; i < stackArray.length; i++) {
			StackTraceElement element = stackArray[i];
			sb.append(element.toString() + "\n");
		}
		return sb.toString();
	}

	public static Object getObj(String outCode, String outMsg, String type) {
		Object returnObj = "";
		List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
		Map<String, Object> outMap = new HashMap<String, Object>();
		outMap.put("rtnCode", outCode);
		outMap.put("rtnMsg", outMsg);
		outList.add(outMap);

		if ("json".equalsIgnoreCase(type)) {
			returnObj = list2Json(outList);
		} else {
			returnObj = outMsg;
		}
		return returnObj;
	}

	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getParameterMap(HttpServletRequest request)
			throws Exception {
		String name = "";
		Entry entry;
		Map parameterMap = getParameterMapUrl(request);
		Map parameterMapBody = getParameterMapBody(request);
		Iterator entries = parameterMapBody.entrySet().iterator();
		while(entries.hasNext()){
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			parameterMap.put(name, valueObj);
		}
		return parameterMap;
	}
	
	/**
	 * 获取请求体中
	 * 
	 * @param reques
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Map getParameterMapBody(HttpServletRequest request)
			throws Exception {
		JSONObject parameterJson = getInputParamJsonBody(request);
		return  parameterJson == null ? new HashMap():(Map)parameterJson;
	}
	
	/**
	 * 获取请求体中参数
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getInputParamJsonBody(HttpServletRequest request)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		char[] buff = new char[1024];
		int len;
		while (reader != null && (len = reader.read(buff)) != -1) {
			sb.append(buff, 0, len);
		}
		if (reader != null)
			reader.close();
		return JSONObject.parseObject(sb.toString());
	}
	
	
	/**
	 * 获取请求路径中参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getParameterMapUrl(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

}
