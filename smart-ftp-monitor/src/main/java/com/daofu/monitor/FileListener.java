package com.daofu.monitor;


import com.daofu.util.ExecutorPoolUtil;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Date;

/**
 * @ClassName FileListener
 * @Description TODO
 * @Author shenzj
 * @Date 2019/2/11 15:23
 **/
public class FileListener implements FileAlterationListener {
    private static Logger logger = Logger.getLogger(FileListener.class);


    public void onStart(FileAlterationObserver observer) {
        //System.out.println("onStart"+new Date().getTime());
    }

    public void onDirectoryCreate(File directory) {
       // logger.info("onDirectoryCreate:" + directory.getName());
    }

    public void onDirectoryChange(File directory) {
    }

    public void onDirectoryDelete(File directory) {

    }

    public void onFileCreate(File file) {
        FileUploadThread thread = new FileUploadThread( file);
        ExecutorPoolUtil.getThreadPool().execute(thread);
    }

    public void onFileChange(File file) {
        // System.out.println("onFileCreate : " + file.getName());

    }

    public void onFileDelete(File file) {

        //System.out.println("onFileDelete :" + file.getName());
    }

    public void onStop(FileAlterationObserver observer) {
        //System.out.println("onStop"+new Date().getTime());
    }

}
