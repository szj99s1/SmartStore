package com.daofu.faceserver.util;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SystemTimeUtil {
	private static Logger logger = Logger.getLogger(SystemTimeUtil.class);
	public static String  getTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
       return df.format(new Date());// new Date()为获取当前系统时间
	}
	public static String  dateFomart(String date){
		if(date==null){
			return   "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date d = null;
		try {
			d = df.parse( date);
			return   df.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
			return   "";
	}

	public static long  getTimeStamp(String dateStr){
		long time=0;
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = formatter.parse(dateStr);
			time = date.getTime();
		}catch(Exception e){

		}
		return time;
	}

	/*获取当前日期的前后 num 天*/
	public static String getDate(int num){
			 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date date=new Date();
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DAY_OF_MONTH, num);
				date = calendar.getTime();
				return sdf.format(date);
		}
		public static void main(String[] args){
		        //String cur = getTime();

			System.out.println(dateToString(new Date()));
		}

		//将date 类型的时间格式化成 String 字符串
		public static String  dateToString(Date date){
			if(date==null){
				return   "";
			}
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");//设置日期格式
			String format = df.format(date);
			return   format;
	}

}
