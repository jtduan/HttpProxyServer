package com.hekewangzi.httpProxyServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hekewangzi.httpProxyServer.threadPools.ThreadPoolManager;
import com.hekewangzi.httpProxyServer.threads.HttpProxyThread;

/**
 * Http代理服务器入口类
 * 
 * @author qq
 * 
 */
public class HttpProxyClientMain {
	private final static Logger log = LoggerFactory.getLogger(HttpProxyClientMain.class);

	/**
	 * 服务端Socket
	 */
	private static ServerSocket serverSocket;

	/**
	 * 程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new HttpProxyClientMain().init();

		try {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				// 每当客户端连接后启动一条线程为该客户端服务
				ThreadPoolManager.execute(new HttpProxyThread(clientSocket,true));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 系统初始化
	 */
	private void init() {
		log.info("系统初始化开始...");
		this.initServerSocket();
		log.info("系统初始化完成.");
	}

	/**
	 * 初始化ServerSocket
	 * 
	 * @throws IOException
	 */
	private void initServerSocket() {
		log.info("ServerSocket初始化开始...");

		try {
			serverSocket = new ServerSocket(8888);

			log.info("绑定IP：{}", serverSocket.getInetAddress().getHostAddress());
			log.info("绑定端口：{}", serverSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		log.info("ServerSocket初始化结束.");
	}

}
