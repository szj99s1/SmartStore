package com.daofu.monitor;

import com.daofu.util.Configuration;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;

import java.io.File;

/**
 *功能描述 文件夹监控
 * @author shenzj
 * @date 2019/2/12
 */
public class  FileMonitor {
    private static Logger logger = Logger.getLogger(FileMonitor.class);
    FileAlterationMonitor monitor = null;

    public FileMonitor(long interval) throws Exception {
        monitor = new FileAlterationMonitor(interval);
    }

    public void monitor(String path, FileAlterationListener listener) {
        FileAlterationObserver observer = new FileAlterationObserver(new File(path));
        monitor.addObserver(observer);
        observer.addListener(listener);
    }

    public void stop() throws Exception {
        monitor.stop();
    }

    public void start() throws Exception {
        monitor.start();
    }

    public static void main(String[] args) throws Exception {
        FileMonitor m = new FileMonitor(Configuration.getInt("monitor.interval.time"));
        m.monitor(Configuration.getString("ftp.root.path"), new FileListener());
        m.start();
        logger.info("ftp文件夹监听服务启动成功");

        SocketServer socketServer = new SocketServer();
        socketServer.startServer();
    }
}