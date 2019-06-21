package com.daofu.monitor;

import com.daofu.util.Configuration;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName SocketServer
 * @Description SOCKET服务用户修改设备对应的设备号文件夹，停用和启动
 * @Author shenzj
 * @Date 2019/2/18 10:52
 **/
public class SocketServer {
    private static Logger logger = Logger.getLogger(ServiceServerTask.class);
    public void startServer(){
        try{
            // 创建一个serversocket，绑定到本机的8899端口上
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(Configuration.getString("socket.server.url"), Configuration.getInt("socket.server.port")));
            logger.info(Configuration.getString("socket.server.url")+"socket服务启动，监听端口:"+Configuration.getInt("socket.server.port"));
            while (true) {
                Socket socket = server.accept();
                new Thread(new ServiceServerTask(socket)).start();
            }
        }catch(Exception e){
            logger.error(e);
        }
    }


    public static void main(String[] args){
        SocketServer server  = new SocketServer();
        server.startServer();
    }
}
