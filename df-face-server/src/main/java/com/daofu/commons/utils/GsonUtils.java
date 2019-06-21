package com.daofu.commons.utils;

import com.daofu.commons.utils.adapter.DateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lc
 * Created by Li-chuang on 2017/9/13.
 */
public final class GsonUtils {
	private GsonUtils(){}

	public static Gson defaultGson;

	private static final Type STRING_OBJECT_MAP = new TypeToken<Map<String,Object>>() {}.getType();
	private static final Type STRING_STRING_MAP = new TypeToken<Map<String,String>>() {}.getType();
	private static final Type STRING_OBJECT_MAP_LIST = new TypeToken<List<Map<String,Object>>>() {}.getType();
	private static final Type STRING_STRING_MAP_LIST = new TypeToken<List<Map<String,String>>>() {}.getType();

	static {
		defaultGson = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
				.setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().disableHtmlEscaping().create();
	}

	public static Gson getGson() {
		return defaultGson;
	}

	public static String toJson(Object json){
		return defaultGson.toJson(json);
	}

	public static <T>T fromJson(String json,Class<T> tc){
		return defaultGson.fromJson(json,tc);
	}

	public static <T>T fromJson(String json,Type type){
		return defaultGson.fromJson(json,type);
	}

	public static Map<String,Object> fromJsonToStrObjMap(String json){
		return defaultGson.fromJson(json,STRING_OBJECT_MAP);
	}

	public static Map<String,String> fromJsonToStrStrMap(String json){
		return defaultGson.fromJson(json,STRING_STRING_MAP);
	}

	public static List<Map<String,Object>> fromJsonToStrObjMapList(String json){
		return defaultGson.fromJson(json,STRING_OBJECT_MAP_LIST);
	}

	public static List<Map<String,String>> fromJsonToStrStrMapList(String json){
		return defaultGson.fromJson(json,STRING_STRING_MAP_LIST);
	}
}