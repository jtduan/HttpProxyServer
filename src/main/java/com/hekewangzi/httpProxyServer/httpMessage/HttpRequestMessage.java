package com.hekewangzi.httpProxyServer.httpMessage;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hekewangzi.httpProxyServer.constants.RequestMethod;
import com.hekewangzi.httpProxyServer.httpMessage.exception.BuildHttpMessageError;
import com.hekewangzi.httpProxyServer.httpMessage.startLine.RequestStartLine;
import com.hekewangzi.httpProxyServer.httpMessage.startLine.StartLine;

/**
 * * Http请求报文
 * 
 * @author qq
 * 
 */
public class HttpRequestMessage extends HttpMessage {
	private final static Logger log = LoggerFactory.getLogger(HttpRequestMessage.class);

	/*
	 * constructor
	 */
	public HttpRequestMessage(InputStream inputStream) throws BuildHttpMessageError {
		super(inputStream);
	}

	@Override
	public StartLine buildStartLien(String startLine) {
		return new RequestStartLine(startLine);
	}

	/**
	 * 是否包含实体
	 * 
	 * true：POST、PUT
	 * 
	 * CONNECT有吗？
	 * 
	 * @return
	 */
	@Override
	public boolean isSupportBody() {
		RequestMethod requestMethod = ((RequestStartLine) super.getStartLine()).getMethod();

		switch (requestMethod) {
		case POST:
		case PUT:
			return true;
		default:
			return false;
		}
	}

}
