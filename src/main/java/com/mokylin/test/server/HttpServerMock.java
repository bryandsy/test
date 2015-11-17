package com.mokylin.test.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class HttpServerMock {

	public final static int DEFAULT_PORT = 9191;
    public final static String DEFAULT_CONTENT_TYPE = "application/json";
    public final static int DEFAULT_STATUS_CODE=HttpServletResponse.SC_OK;

    private Server _httpServer;
    private int _port;

    public HttpServerMock() {
        _port = DEFAULT_PORT;
    }

    public HttpServerMock(int port) {
        _port = port;
    }

    /**
     * 启动Jetty服务器�?�默认的响应status code�?"200"，content type�?"application/json"�?
     * @param content 响应内容
     */
    public void start(String content) throws Exception {
        start(content, DEFAULT_CONTENT_TYPE, DEFAULT_STATUS_CODE);
    }
    /**
     * 启动Jetty服务器�?�默认的响应status code�?"200"，content type�?"application/json"�?
     * @param content 响应内容
     */
    public void startDFLT(String content) throws Exception {
    	startNo(content, DEFAULT_CONTENT_TYPE, DEFAULT_STATUS_CODE);
    }
    /**
     * 启动Jetty服务器�?�默认的响应status code�?"200"，content type�?"application/json"�?
     * @param content 响应内容
     */
    public void startDFLT(String content,String contentType) throws Exception {
    	startNo(content, contentType, DEFAULT_STATUS_CODE);
    }
    /**
     * 启动Jetty服务器�?�默认的响应status code�?"200"，content type�?"application/json"�?
     * @param content 响应内容
     */
    public void startDFLT(String content,String contentType,int statuCode) throws Exception {
    	startNo(content, contentType, statuCode);
    }

    /**
     * 启动Jetty服务器�?�默认的响应status code�?"200"�?
     * @param content 响应内容
     * @param contentType 响应内容的MIME类型
     */
    public void start(String content, String contentType) throws Exception {
        start(content, contentType, DEFAULT_STATUS_CODE);
    }

    /**
     * 启动Jetty服务器�??
     * @param content 响应内容
     * @param contentType 响应内容的MIME类型
     * @param statuCode 响应状�?�码
     */
    public void startNo(String content, String contentType, 
            int statuCode) throws Exception {
        _httpServer = new Server();
        _httpServer.setStopAtShutdown(true);
        ServerConnector connector = new ServerConnector(_httpServer); 
        connector.setPort(_port);
        connector.setIdleTimeout(30000);  
        connector.setReuseAddress(true);
        _httpServer.setConnectors(new Connector[] { connector });
       
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{createHandler(content, contentType, statuCode)});
        _httpServer.setHandler(handlers);
        _httpServer.start();
    }
    /**
     * 启动Jetty服务器�??
     * @param content 响应内容
     * @param contentType 响应内容的MIME类型
     * @param statuCode 响应状�?�码
     */
    public void start(String content, String contentType, 
    		int statuCode) throws Exception {
    	_httpServer = new Server();
    	_httpServer.setStopAtShutdown(true);
    	ServerConnector connector = new ServerConnector(_httpServer); 
        connector.setPort(_port);
        connector.setIdleTimeout(30000);  
    	connector.setReuseAddress(true);
    	_httpServer.setConnectors(new Connector[] { connector });
    	ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);  
    	context.setContextPath("/");  
    	context.addServlet(new ServletHolder(new TestPostServlet()), "/game_recharge");
    	context.addServlet(new ServletHolder(new MockAlipayAfterNotify()), "/gateway_verify");
    	HandlerList handlers = new HandlerList();
    	handlers.setHandlers(new Handler[]{context,createHandler(content, contentType, statuCode)});
    	_httpServer.setHandler(handlers);
    	_httpServer.start();
    }

    /**
     * 停止Jetty服务器�??
     */
    public void stop() throws Exception {
        if (null != _httpServer) {
            _httpServer.stop();
            _httpServer = null;
        }
    }

    private Handler createHandler(final String content, final String contentType, 
            final int statusCode) {
        return new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
                response.setContentType(contentType);
                response.setStatus(statusCode);
                baseRequest.setHandled(true);
                response.getWriter().print(content);
            }
        };
    }

}
