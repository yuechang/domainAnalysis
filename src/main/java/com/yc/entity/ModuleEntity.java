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

/**
 * @ClassName: ModuleEntity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Yue Chang
 * @date 2018年4月21日 上午9:36:16
 * @since 1.0
 */
public class ModuleEntity {

	public static final String[] ATTRIBUTES_TITLE = { "域名", "是否可用", "后缀", "价格", "平均价格", "类型" };
	public static final String DOMAIN_INFO = "域名信息";

	private String name;
	private int avail;
	private String tld;

	private double price;
	private double preRegisterPrice;
	private String type;



	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getPreRegisterPrice() {
		return preRegisterPrice;
	}

	public void setPreRegisterPrice(double preRegisterPrice) {
		this.preRegisterPrice = preRegisterPrice;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAvail() {
		return avail;
	}

	public void setAvail(int avail) {
		this.avail = avail;
	}

	public String getTld() {
		return tld;
	}

	public void setTld(String tld) {
		this.tld = tld;
	}

	@Override
	public String toString() {
		return "ModuleEntity [price=" + price + ", preRegisterPrice=" + preRegisterPrice + ", type=" + type + ", name="
				+ name + ", avail=" + avail + ", tld=" + tld + "]";
	}
}
