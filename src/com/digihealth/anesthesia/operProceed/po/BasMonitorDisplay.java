/*
 * BasMonitorDisplay.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-04-07 Created
 */
package com.digihealth.anesthesia.operProceed.po;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "采集数据显示表对象")
public class BasMonitorDisplay {
	@ApiModelProperty(value = "主键ID")
	private String id;

	/**
	 * 时间
	 */
	@ApiModelProperty(value = "时间")
	private Date time;

	/**
	 * 患者id
	 */
	@ApiModelProperty(value = "患者id")
	private String regOptId;

	/**
	 * 观察点ID
	 */
	@ApiModelProperty(value = "观察点ID")
	private String observeId;

	/**
	 * 观测点的值
	 */
	@ApiModelProperty(value = "观测点的值")
	private Float value;

	/**
	 * 是否正常值，0正常，-1过低，1过高
	 */
	@ApiModelProperty(value = "是否正常值，0正常，-1过低，1过高")
	private Integer state;

	/**
	 * 观测点的值
	 */
	@ApiModelProperty(value = "观测点的值")
	private String observeName;

	/**
	 * 图标
	 */
	@ApiModelProperty(value = "图标")
	private String icon;

	/**
	 * svg图标
	 */
	@ApiModelProperty(value = "svg图标")
	private String iconSvg;

	/**
	 * 颜色
	 */
	@ApiModelProperty(value = "颜色")
	private String color;

	/**
	 * 频率
	 */
	@ApiModelProperty(value = "频率")
	private Integer freq;

	/**
	 * 位置
	 */
	@ApiModelProperty(value = "位置")
	private Integer position;

	/**
	 * 间隔时间
	 */
	@ApiModelProperty(value = "间隔时间")
	private Integer intervalTime;

	/**
	 * 设备id
	 */
	@ApiModelProperty(value = "设备id")
	private String deviceId;

	/**
	 * 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
	 */
	@ApiModelProperty(value = "数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加")
	private Integer amendFlag;

    /**
     * 出室数据增加标识;0-默认,1-是
     */
	@ApiModelProperty(value = "出室数据增加标识;0-默认,1-是")
    private Integer outerFlag;
	
	private Integer docType;
    
	public Integer getDocType()
    {
        return docType;
    }

    public void setDocType(Integer docType)
    {
        this.docType = docType;
    }

    public Integer getOuterFlag()
    {
        return outerFlag;
    }

    public void setOuterFlag(Integer outerFlag)
    {
        this.outerFlag = outerFlag;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getRegOptId() {
		return regOptId;
	}

	public void setRegOptId(String regOptId) {
		this.regOptId = regOptId == null ? null : regOptId.trim();
	}

	public String getObserveId() {
		return observeId;
	}

	public void setObserveId(String observeId) {
		this.observeId = observeId == null ? null : observeId.trim();
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getObserveName() {
		return observeName;
	}

	public void setObserveName(String observeName) {
		this.observeName = observeName == null ? null : observeName.trim();
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon == null ? null : icon.trim();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color == null ? null : color.trim();
	}

	public Integer getFreq() {
		return freq;
	}

	public void setFreq(Integer freq) {
		this.freq = freq;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Integer getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId == null ? null : deviceId.trim();
	}

	public Integer getAmendFlag() {
		return amendFlag;
	}

	public void setAmendFlag(Integer amendFlag) {
		this.amendFlag = amendFlag;
	}

	public String getIconSvg() {
		return iconSvg;
	}

	public void setIconSvg(String iconSvg) {
		this.iconSvg = iconSvg;
	}

}