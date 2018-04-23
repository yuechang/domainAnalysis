/*
 * Copyright (c) 2016, 2025, HHLY and/or its affiliates. All rights reserved.
 * HHLY PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package com.yc.entity;

import java.util.List;

/**
 * @ClassName: ResultEntity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Yue Chang
 * @date 2018年4月21日 上午9:39:42
 * @since 1.0
 */
public class ResultEntity {

	private List<ModuleEntity> module;
	private int errorCode;
	private String success;

	public List<ModuleEntity> getModule() {
		return module;
	}

	public void setModule(List<ModuleEntity> module) {
		this.module = module;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "ResultEntity [module=" + module + ", errorCode=" + errorCode + ", success=" + success + "]";
	}
}
