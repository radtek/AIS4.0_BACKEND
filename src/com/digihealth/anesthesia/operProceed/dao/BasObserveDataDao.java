/*
 * BasObserveDataDao.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-04-07 Created
 */
package com.digihealth.anesthesia.operProceed.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.digihealth.anesthesia.basedata.formbean.BaseInfoQuery;
import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.operProceed.formbean.RealTimeDataFormBean;
import com.digihealth.anesthesia.operProceed.po.BasMonitorDisplay;
import com.digihealth.anesthesia.operProceed.po.BasObserveData;
import com.digihealth.anesthesia.operProceed.po.Observe;

@MyBatisDao
public interface BasObserveDataDao {

    BasObserveData selectByPrimaryKey(@Param("keyid") String keyid, @Param("timesend") Date timesend);

	//获取手术时间轴信息
	public List<BasObserveData> searchObserveTimeById(@Param("regOptId") String regOptId);
	
	//获取单个手术设备采集项信息
	public List<BasObserveData> searchObserveIdById(@Param("regOptId") String regOptId);
	
	
	/**
	 * 查询术中实时监测项
	 */
	public List<RealTimeDataFormBean> searchObserveDataByPosition(@Param("baseQuery") BaseInfoQuery baseQuery);
	
	/**
	 * 查询对应数据的点的数据
	 */
	public List<BasObserveData> findObserveDataListByTime(@Param("regOptId")String regOptId,@Param("timesend") String timesend, @Param("observeIds")List<String> observeIds,@Param("docType") Integer docType);
	
	
	/* 根据床号，查询所有的需要采集的参数 */
	public List<Observe> searchObserveListByBedId(@Param("bedId") int bedId);
	
	public int searchMonitorDisplayByTime(@Param("regOptId")String regOptId,@Param("timesend")String timesend);
	
	
	public List<BasObserveData> searchObsListByRegOptId( @Param("regOptIds") List<String> regOptIds);
	
	public int deleteObsByIds( @Param("observeIds") List<String> observeIds);

    public List<BasMonitorDisplay> searchObserveWaveList(@Param("regOptId")String regOptId,@Param("observeIds")List<String> observeIds);

    public List<BasMonitorDisplay> searchObserveSecondsList(@Param("regOptId")String regOptId,@Param("observeNames")List<String> observeNames);

	public int deleteObsWave();
}