package com.hekewangzi.httpProxyServer.threads;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hekewangzi.httpProxyServer.constants.Properties;
import com.hekewangzi.httpProxyServer.constants.RequestMethod;
import com.hekewangzi.httpProxyServer.httpMessage.HttpRequestMessage;
import com.hekewangzi.httpProxyServer.httpMessage.exception.BuildHttpMessageError;
import com.hekewangzi.httpProxyServer.httpMessage.exception.ConnectServerError;
import com.hekewangzi.httpProxyServer.httpMessage.startLine.RequestStartLine;
import com.hekewangzi.httpProxyServer.proxy.ClientProxy;
import com.hekewangzi.httpProxyServer.proxy.Proxy;
import com.hekewangzi.httpProxyServer.proxy.ServerProxy;
import com.hekewangzi.httpProxyServer.utils.SocketUtil;

/**
 * Http代理线程
 * 
 * @author qq
 * 
 */
public class HttpProxyThread extends Thread {
	private final static Logger log = LoggerFactory.getLogger(HttpProxyThread.class);

	/*
	 * 客户端相关
	 */
	/**
	 * 客户端Socket
	 */
	private Socket clientSocket;

	/**
	 * 客户端输入流
	 */
	private InputStream clientInputStream;

	/*
	 * 服务端相关
	 */
	/**
	 * 服务端Socket
	 */
	private Socket serverSocket;

	/**
	 * 启动模式
	 * 1: 客户端模式，用于二级代理
	 * 0: 服务器模式, 直接连接待访问的URL
	 */
	private boolean clientMode;

	/*
	 * constructor
	 */
	private HttpProxyThread() {
	}

	public HttpProxyThread(Socket clientSocket,boolean clientMode) {
		super();
		this.clientSocket = clientSocket;
		this.clientMode = clientMode;
		try {
			this.clientInputStream = clientSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		/*
		 * 解析客户端请求
		 */
		HttpRequestMessage requestMessage = null;
		try {
			requestMessage = new HttpRequestMessage(clientInputStream);
		} catch (BuildHttpMessageError e1) {
			e1.printStackTrace();

			SocketUtil.closeSocket(clientSocket, serverSocket);
			return;
		}

		// 请求方法
		RequestMethod httpRequestMethod = ((RequestStartLine) requestMessage.getStartLine()).getMethod();

		System.out.println("[原始请求:]");
		System.out.print(requestMessage);
		System.out.println("----------");

		/*
		 * 连接服务器
		 */
		String hostHeader = requestMessage.getHeader("host"); // 如果没有Host首部，则从Url中解析
		String host = hostHeader;
		int port = 80; // 默认端口
		if (RequestMethod.CONNECT == httpRequestMethod) { // https
			port = 443;
		}
		if (hostHeader.contains(":")) {
			String[] hostArr = hostHeader.split(":");
			host = hostArr[0];
			port = Integer.parseInt(hostArr[1]);
		}

		/*
		 * 
		 */
		try {
			if(this.clientMode){
				this.serverSocket = SocketUtil.connectServer(Properties.ServerIP, Properties.ServerPort, Properties.ServerConnectTimeout);
			}else {
				this.serverSocket = SocketUtil.connectServer(host, port, Properties.ServerConnectTimeout);
			}
		} catch (ConnectServerError e) {
			e.printStackTrace();

			SocketUtil.closeSocket(clientSocket, serverSocket);
			return;
		}

		if (RequestMethod.CONNECT != httpRequestMethod) { // 代理http
			try {
				this.clientSocket.setSoTimeout(Properties.ClientSoceketReadTimeout); // 设置读取浏览器Socket超时时间
				this.serverSocket.setSoTimeout(Properties.ServerSocketReadTimeout); // 设置读取代理服务器Socket超时时间
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}

		/*
		 * 转发流量
		 */
		Proxy proxy;
		if(clientMode) {
			proxy = new ClientProxy(clientSocket, serverSocket, requestMessage);
		}else{
			proxy = new ServerProxy(clientSocket, serverSocket, requestMessage);
		}
		proxy.proxy();
	}

}
