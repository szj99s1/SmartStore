package com.daofu.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorPoolUtil {
	private static ExecutorService pool = Executors.newFixedThreadPool(Configuration.getInt("thread.pool.num"));
	public static ExecutorService getThreadPool(){
		return pool;
	}
}
