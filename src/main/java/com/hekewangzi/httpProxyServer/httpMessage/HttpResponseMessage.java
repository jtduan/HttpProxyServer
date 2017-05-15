package com.hekewangzi.httpProxyServer.httpMessage;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hekewangzi.httpProxyServer.constants.HttpResponseStatus;
import com.hekewangzi.httpProxyServer.httpMessage.exception.BuildHttpMessageError;
import com.hekewangzi.httpProxyServer.httpMessage.startLine.ResponseStartLine;
import com.hekewangzi.httpProxyServer.httpMessage.startLine.StartLine;

/**
 * Http响应报文
 * 
 * @author qq
 * 
 */
/**
 * @author qq
 * 
 */
public class HttpResponseMessage extends HttpMessage {
	private final static Logger log = LoggerFactory.getLogger(HttpResponseMessage.class);

	/*
	 * constructor
	 */
	private HttpResponseMessage() {
		super();
	}

	public HttpResponseMessage(InputStream inputStream) throws BuildHttpMessageError {
		super(inputStream);
	}

	public HttpResponseMessage(HttpResponseStatus httpResponseStatus) {
		this.buildByHttpRespnseState(httpResponseStatus);
	}

	/**
	 * 根据HttpResponseStatus构建
	 * 
	 * @param httpResponseStatus
	 * @return
	 */
	private HttpResponseMessage buildByHttpRespnseState(HttpResponseStatus httpResponseStatus) {
		ResponseStartLine responseStartLine = new ResponseStartLine(null);

		responseStartLine.setProtocol("HTTP");
		responseStartLine.setMasterVersion(1);
		responseStartLine.setMinorVersion(1);
		responseStartLine.setStatus(httpResponseStatus.getStatus());
		responseStartLine.setDescription(httpResponseStatus.getDescription());

		super.setStartLine(responseStartLine);

		return this;
	}

	@Override
	public StartLine buildStartLien(String startLine) {
		return new ResponseStartLine(startLine);
	}

	/**
	 * 是否包含实体
	 * 
	 * true：GET、POST、PUT、TRACE、DELETE、CONNECT
	 * 
	 * @return
	 */
	@Override
	public boolean isSupportBody() {
		// 报空指针异常
		// RequestMethod requestMethod = ((RequestStartLine)
		// super.getStartLine()).getMethod();
		// log.info(requestMethod.toString());
		//
		// switch (requestMethod) {
		// case GET:
		// case POST:
		// case PUT:
		// case TRACE:
		// case DELETE:
		// case CONNECT:
		// return true;
		// default:
		// return false;
		// }
		return true;
	}

}
