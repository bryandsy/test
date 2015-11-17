package com.mokylin.test.server;

public class AnalogPaymentServer {

	private static HttpServerMock verifyMock = new HttpServerMock(19999);
	private static HttpServerMock verifyIdMock = new HttpServerMock(19088);
	
	public static void main(String[] args) {
		try {
			verifyMock.start("alipay_verify");
			verifyIdMock.startDFLT("true");
			System.out.println("Pay Mock Startup port: 19999");
			System.out.println("Pay Verify Mock Startup port: 19088");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
