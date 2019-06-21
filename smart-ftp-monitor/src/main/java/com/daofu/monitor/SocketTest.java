package com.daofu.monitor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @ClassName SocketTest
 * @Description TODO
 * @Author shenzj
 * @Date 2019/2/18 13:13
 **/
public class SocketTest {
    public static void main(String[] args) throws  Exception{
        Socket socket = new Socket("192.168.6.241", 8899);
        OutputStream out =socket.getOutputStream();
        out.write("0home                                       \r\n".getBytes());
        InputStream in = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String param = br.readLine();
        System.out.println(param);
        in.close();
        out.close();
        socket.close();
    }
}
