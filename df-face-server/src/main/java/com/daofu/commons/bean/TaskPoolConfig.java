package com.daofu.commons.bean;

import com.daofu.commons.propertie.CompUtils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @author lichuang
 * @description
 * @date 2019-02-28 09:29
 */
@EnableAsync
@Configuration
public class TaskPoolConfig {

    @Bean(name = "tank1Executor")
    public Executor taskExecutor1() {
        ExecutorService executor = new ThreadPoolExecutor(
                CompUtils.getThreadPoolTask1Num()
                , CompUtils.getThreadPoolTask1Num()
                ,0L, TimeUnit.SECONDS
                , new LinkedBlockingQueue<>()
                , new ThreadFactoryBuilder().setNameFormat("pool1-%d").build()
                , new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}