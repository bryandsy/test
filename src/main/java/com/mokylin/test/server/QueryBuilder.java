package com.mokylin.test.server;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;


public class QueryBuilder<T> {

	private static final int COMP_START = 1;
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
	private final CopyOnWriteArrayList<NameValuePair> formparams;
	private static final String Q_EQUALS = "=";
	private static final String Q_AND = "&";
	private static final String UTF_8 = "UTF-8";
	private final String url;
	private final boolean postFlag;
	private final boolean orderByFlag;
	private final HttpPost httppost;
	private static final transient StringBuilder buff = new StringBuilder();
	private static final Comparator<NameValuePair> NameValueCompartor = new Comparator<NameValuePair>() {
		@Override
		public int compare(NameValuePair o1, NameValuePair o2) {
			return ComparisonChain.start().compare(o1.getName(), o2.getName()).result();
		}
	};

	/**
	 * 
	 * @param url
	 * @param postFlag isn't post
	 * @param orderByFlag isn't sign order by
	 */
	public QueryBuilder(String url,final boolean postFlag,boolean orderByFlag) {
		this.formparams = Lists.newCopyOnWriteArrayList();
		this.url = url;
		this.postFlag = postFlag;
		this.orderByFlag = orderByFlag;
		this.httppost = new HttpPost(this.url);
	}
	
	public boolean getPostFlag() {
		return this.postFlag;
	}
	
	public boolean getOrderByFlag() {
		return this.orderByFlag;
	}

	public void add(String key, String value) {
		try {
			putParameter(key, value);
		} catch (UnsupportedEncodingException e) {
			logger.error("request add parameter error : {}",e.getCause());
		}
	}
	
	public String makeUrl(final String securityCode) {
		List<NameValuePair> copyValue = Lists.newArrayList(formparams);
		int size = copyValue.size();
		if(this.orderByFlag) {
			if(size > COMP_START) {
				Collections.sort(copyValue, NameValueCompartor);
			}
		}
		buff.setLength(0);
		for (int i = 0, len = copyValue.size(); i < len; i++) {
			NameValuePair valuePair = copyValue.get(i);
			String key = valuePair.getName();
			String value = valuePair.getValue();
			if(value.equals("") || value == null || 
					key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")){
				continue;
			}
			if (i > 0) {
				buff.append(Q_AND);
			}
			buff.append(key).append(Q_EQUALS).append(value);
		}
		
		buff.append(securityCode);
		logger.info("sign before : {}",buff);
		String signStr = null;
		try {
			signStr = DigestUtils.md5Hex(buff.toString().getBytes(UTF_8));
			this.add("sign", signStr);
			logger.info("sign after md5 : {}",signStr);
		} catch (UnsupportedEncodingException e) {
			logger.error("query sign entrypt error : {}",e.getCause());
		}
		return signStr;
	}

	private void putParameter(String key, String value) throws UnsupportedEncodingException {
		if(value.equals("") || value == null){
			return;
		}
		formparams.add(new BasicNameValuePair(key, value));
	}
	public void remove(String key, String value) throws UnsupportedEncodingException {
		if(value.equals("") || value == null){
			return;
		}
		BasicNameValuePair bnvp = new BasicNameValuePair(key, value);
		if(formparams.contains(bnvp)) {
			formparams.remove(bnvp);
		}
	}

	@SuppressWarnings("unchecked")
	public T toQueryString() {
		try {
			if (postFlag) {
				UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, UTF_8);
				httppost.setEntity(uefEntity);
				return (T) httppost;
			} else {
				buff.setLength(0);
				buff.append(url).append("?");
				for (int i = 0, len = formparams.size(); i < len; i++) {
					NameValuePair valuePair = formparams.get(i);
					String key = valuePair.getName();
					String value = valuePair.getValue();
					if (i > 0) {
						buff.append(Q_AND);
					}
					buff.append(key).append(Q_EQUALS).append(value);
				}
				return (T) new HttpGet(buff.toString());
			}
		} catch (Exception e) {
			logger.error("assemble request parameter error : {}",e.getCause());
			return null;
		}
	}


}
