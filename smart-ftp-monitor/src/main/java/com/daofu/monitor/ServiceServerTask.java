package com.daofu.monitor;

import com.daofu.util.Configuration;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

/**
 * @ClassName ServiceServerTask
 * @Description TODO
 * @Author shenzj
 * @Date 2019/2/18 11:08
 **/
public class ServiceServerTask  implements Runnable{
    private static Logger logger = Logger.getLogger(ServiceServerTask.class);
    Socket socket ;
    InputStream in=null;
    OutputStream out = null;
    public ServiceServerTask(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try{
            in = socket.getInputStream();
            out = socket.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String param = br.readLine();
            //0启用 3停用
            String type = param.substring(0,1);
            String device_code = param.substring(1,param.length()).trim();
            File rootFile = new File(Configuration.getString("ftp.root.path"));
            //子目录，机构名文件夹
            File[]  files = rootFile.listFiles();
            boolean find_device=false;
            for(int i=0;i<files.length;i++){
                File orgFile = files[i];
                if(orgFile.isDirectory()){
                    //机构名称文件夹下，设备号文件夹
                    File[] deviceFiles = orgFile.listFiles();
                    for(int j=0;j<deviceFiles.length;j++){
                        File deviceFile = deviceFiles[j];
                        if(deviceFile.isDirectory()){
                            String name =  deviceFile.getName();
                            if(name.indexOf(device_code)>=0){
                                find_device=true;
                                String path =deviceFile.getParent();
                                if("0".equals(type)){
                                    Runtime.getRuntime().exec("chmod 777 "+path+"/"+device_code);
                                }else{
                                    Runtime.getRuntime().exec("chmod 000 "+path+"/"+device_code);
                                }
                                break;
                            }
                        }
                    }
                    if(find_device){
                        break;
                    }
                }
            }
            out.write("ok\r\n".getBytes());
        }catch (Exception e){
            logger.error("ServiceServerTask",e);
        }finally {
            try{
                if(in!=null){
                    in.close();
                }
                if(out!=null){
                    out.close();
                }
                if(socket!=null){
                    socket.close();
                }
            }catch (Exception e){
                logger.error("ServiceServerTask",e);
            }


        }



    }
}
