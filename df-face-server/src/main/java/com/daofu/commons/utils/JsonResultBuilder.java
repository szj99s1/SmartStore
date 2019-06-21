package com.daofu.commons.utils;

import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lichuang
 */
public class JsonResultBuilder {

	private JsonResultBuilder(){}

	private Map<String, Object> results = new HashMap<>();

	private static JsonResultBuilder create(){
		return new JsonResultBuilder();
	}

	public static JsonResultBuilder success() {
		JsonResultBuilder builder = create();
		builder.set("errCode", CommonEnum.SUCCESS.getCode());
		builder.set("msg", CommonEnum.SUCCESS.getMsg());
		return builder;
	}

//	public static JsonResultBuilder success(MsgInterface msgInterface) {
//		JsonResultBuilder builder = create();
//		builder.set("errCode", msgInterface.getCode());
//		builder.set("msg", msgInterface.getMsg());
//		return builder;
//	}

	public static JsonResultBuilder fail(MsgInterface msgInterface) {
		JsonResultBuilder builder = create();
		builder.set("errCode", msgInterface.getCode());
		builder.set("msg", msgInterface.getMsg());
		return builder;
	}

	public static JsonResultBuilder fail() {
		JsonResultBuilder builder = create();
		builder.set("errCode", CommonEnum.FAILURE.getCode());
		builder.set("msg", CommonEnum.FAILURE.getMsg());
		return builder;
	}

	public static JsonResultBuilder fail(BindingResult result) {
		JsonResultBuilder builder = create();
		builder.set("errCode", "-2");
		builder.set("msg", result.getAllErrors().get(0).getDefaultMessage());
		return builder;
	}

	public JsonResultBuilder set(String name, Object value) {
		results.put(name, value);
		return this;
	}

	public Map<String, Object> toMap() {
		return results;
	}
}
