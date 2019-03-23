package com.hekewangzi.httpProxyServer;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hekewangzi.httpProxyServer.threads.HttpProxyThread;
import com.hekewangzi.httpProxyServer.utils.SocketUtil;

/**
 * Http代理服务器入口类
 *
 * @author qq
 *
 */
public class PhoneSide {
    private final static Logger log = LoggerFactory.getLogger(PhoneSide.class);

    /**
     * 服务端Socket
     */
    private static Socket socket;

    /**
     * 程序入口
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        Thread t = new HttpProxyThread("127.0.0.1", 9999);
        t.start();
        t.join();
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
            Socket socket = SocketUtil.connectServer("127.0.0.1", 8888, 1000);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        log.info("ServerSocket初始化结束.");
    }

}
