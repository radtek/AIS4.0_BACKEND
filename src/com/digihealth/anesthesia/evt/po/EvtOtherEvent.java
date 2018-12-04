/*
 * EvtOtherEvent.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-04-06 Created
 */
package com.digihealth.anesthesia.evt.po;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "其他事件对象")
public class EvtOtherEvent {
	/**
	 * 其他事件主键
	 */
	@ApiModelProperty(value = "其他事件主键")
	private String otherEventId;

	/**
	 * 文书id
	 */
	@ApiModelProperty(value = "文书ID")
	private String docId;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private Date endTime;

	/**
	 * 事件标题
	 */
	@ApiModelProperty(value = "事件标题")
	private String title;
	
	/**
     * 其他事件定义ID
     */
    @ApiModelProperty(value = "其他事件定义ID")
    private String eventDefId;
    
    @ApiModelProperty(value = "文书类型")
    private Integer docType;

    public Integer getDocType()
    {
        return docType;
    }

    public void setDocType(Integer docType)
    {
        this.docType = docType;
    }

    public String getEventDefId()
    {
        return eventDefId;
    }

    public void setEventDefId(String eventDefId)
    {
        this.eventDefId = eventDefId;
    }

	public String getOtherEventId() {
		return otherEventId;
	}

	public void setOtherEventId(String otherEventId) {
		this.otherEventId = otherEventId == null ? null : otherEventId.trim();
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId == null ? null : docId.trim();
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title == null ? null : title.trim();
	}
}