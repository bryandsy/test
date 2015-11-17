package com.mokylin.test.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpPost;

import com.google.common.collect.Maps;


public class MockAlipayAfterNotify extends HttpServlet {
	private static final String RETURN_URL_KEY = "return_url";
	private static final String NOTIFY_URL_KEY = "notify_url";
	private static final long serialVersionUID = 1L;
	private static final HttpClientUtils client = new HttpClientUtils();
	
	private static final String ALIPAY_KEY = "b2dwihheo4ccmsvwihhkys056n6ijhip";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MockAlipayAfterNotify() {
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getParameterMap());
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		response.setContentType("application/json");
		String notifyUrlKey = request.getParameter(NOTIFY_URL_KEY);
		String returnUrlKey = request.getParameter(RETURN_URL_KEY);
		
		
		QueryBuilder<HttpPost> nofityPost = fillPostPayData(notifyUrlKey,request);
 		String nofityInfo = client.post(nofityPost);
 		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		QueryBuilder<HttpPost> returnPost = fillPostPayData(returnUrlKey,request);
 		client.post(nofityPost);
 		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		RequestDispatcher rrd = request.getRequestDispatcher(returnUrl);
//		rrd.forward(request, response);
		PrintWriter out = response.getWriter();
		out.println(nofityInfo);
	}

	private QueryBuilder<HttpPost> fillPostPayData(String urlKey,HttpServletRequest request)
			throws UnsupportedEncodingException {
		QueryBuilder<HttpPost> nofityPost = new QueryBuilder<HttpPost>(urlKey, true,true);
		fillParameter(request, nofityPost);
 		String signNew = nofityPost.makeUrl(ALIPAY_KEY);
 		String signOld = request.getParameter("sign");
 		if(signOld.equals(signNew)) {
 			nofityPost.add("quantity", "1");
 			nofityPost.add("notify_id", "3b523fe53bb7d514d69f061906f6511gxc");
 			nofityPost.add("notify_type", "trade_status_sync");
 			nofityPost.add("notify_time", "2015-10-16 11:40:13");
 			nofityPost.add("use_coupon", "N");
 			nofityPost.add("gmt_create", "2015-10-16 11:26:10");
 			nofityPost.add("gmt_payment", "2015-10-16 11:26:14");
 			nofityPost.add("is_total_fee_adjust", "N");
 			nofityPost.add("trade_status", "TRADE_SUCCESS");
 			nofityPost.add("buyer_email", "test@mokylin.com");
 			nofityPost.remove("sign", signNew);
 			nofityPost.remove("sign", signOld);
 			String newSign = nofityPost.makeUrl(ALIPAY_KEY);
 		}
		return nofityPost;
	}

	private void fillParameter(HttpServletRequest request, QueryBuilder<HttpPost> nofityPost) {
		Map<String, String[]> keyLists = Maps.newHashMap(request.getParameterMap());
		Set<Entry<String, String[]>> Iter = keyLists.entrySet();
 		for (Entry<String, String[]> entry : Iter) {
 			String[] tempStrs = entry.getValue();
 			nofityPost.add(entry.getKey(),tempStrs[0]);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
}
