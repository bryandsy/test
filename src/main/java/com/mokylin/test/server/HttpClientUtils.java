package com.mokylin.test.server;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	private final transient ReentrantLock lock = new ReentrantLock();
	private final RequestConfig config;

	public HttpClientUtils() {
		config = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();//set request and request timeout
	}
	
	
	public String get(final QueryBuilder<HttpGet> context) {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			final HttpGet httpget = context.toQueryString(); 
			if(checkValidArgument(httpget,"http post object is null")) {
        		return null;
        	}
			httpget.setConfig(config);
			logger.info("executing request " + httpget.getURI());  
			String returnStr = null;
			try(CloseableHttpClient httpclient = HttpClients.createDefault();
					CloseableHttpResponse response = httpclient.execute(httpget);) {
			    HttpEntity entity = response.getEntity();  
			    logger.info("--------------------------------------"); 
			    logger.info(response.getStatusLine().toString());  
			    if (entity != null) {  
			    	logger.info("Response content length: " + entity.getContentLength());  
			        returnStr = EntityUtils.toString(entity);
			        logger.info("Response content: " + returnStr);  
			        
			    }  
			    logger.info("------------------------------------"); 
			     
			}
			return returnStr;
		} catch (Exception e) {
			logger.error("http client get false , cause : {}",e);
			return null;
		} finally {
			lock.unlock();
		}
	}
	
	public String post(final QueryBuilder<HttpPost> context) {
		final ReentrantLock lock = this.lock;
		lock.lock();
        try {
        	final HttpPost httppost = context.toQueryString();
        	httppost.setConfig(config);
        	if(checkValidArgument(httppost,"http post object is null")) {
        		return null;
        	}
        	String returnStr = null;
			try(CloseableHttpClient httpclient = HttpClients.createDefault();
					CloseableHttpResponse response = httpclient.execute(httppost);) {
	            HttpEntity entity = response.getEntity();  
	            logger.info("--------------------------------------"); 
	            System.out.println(response.getStatusLine());  
	            if (entity != null) { 
	            	returnStr = EntityUtils.toString(entity);
	            	logger.info("Response content length: " + entity.getContentLength());  
	            	logger.info("Response content: " + returnStr);  
	            }  
	            logger.info("------------------------------------");  
			}
			return returnStr;
        } catch(Exception ex) {
        	logger.error("HttpClientUtils.post exception, Cause: {}",ex);
        	return null;
        } finally {
    		lock.unlock();
        }
	}
	
	private boolean checkValidArgument(Object key,String message) {
		if(null == key || "".equals(key)) {
			logger.error(message);
			return true;
		}
		return false;
	}
	
	
}
