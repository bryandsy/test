package com.mokylin.test.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.CharArrayBuffer;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Servlet implementation class TestPostServlet
 */
public class TestPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestPostServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Enumeration<String> headerEnum = request.getHeaderNames();
    	while(headerEnum.hasMoreElements()) {
    		String hKey = headerEnum.nextElement();
    		String hValue = request.getHeader(hKey);
    		System.out.println(hKey+": "+hValue);
    	}
		System.out.println(request.getParameter("uid"));
		System.out.println(request.getParameter("role_id"));
		System.out.println(request.getParameter("role_name"));
		System.out.println(request.getParameter("gkey"));
		System.out.println(request.getParameter("skey"));
		System.out.println(request.getParameter("order_id"));
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setContentType("application/json");
		Map<String, Object>  test = Maps.newHashMap();
		test.put("status", "1");
		test.put("errmsg", "充�?�成�?");
		Map<String, String>  data = Maps.newHashMap();
		data.put("order_id", "订单�?");
		data.put("uid", "用户id");
		data.put("role_id", "游戏角色ID");
		data.put("role_name", "角色名称");
		data.put("platfrom", "平台ID");
		data.put("gkey", "游戏�?");
		data.put("skey", "区服");
		data.put("coins", "游戏币数�?");
		data.put("moneys", "充�?�数�? ");
		data.put("time", "时间�?");
		test.put("data", data);
		
		JSON.toJSONString(test);
		
		PrintWriter out = response.getWriter();
		out.println(JSON.toJSONString(test));
	}
	
	private List<Header> getAllHeaders(HttpServletRequest request) {
		List<Header> headers = Lists.newArrayList();
		CharArrayBuffer cab = null;
    	Enumeration<String> headerEnum = request.getHeaderNames();
    	while(headerEnum.hasMoreElements()) {
    		String hKey = headerEnum.nextElement();
    		String hValue = request.getHeader(hKey);
    		cab = new CharArrayBuffer(32);
    		cab.append(hKey+": "+hValue);
    		headers.add(new BufferedHeader(cab));
    	}
    	return headers;
	}

}
