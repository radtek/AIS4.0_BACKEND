/*
 * DocPackagesItemDao.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-03-30 Created
 */
package com.digihealth.anesthesia.doc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.common.persistence.CrudDao;
import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.doc.po.DocPackagesItem;
import com.digihealth.anesthesia.interfacedata.formbean.hnhtyy.CostHNHTYYRow;
import com.digihealth.anesthesia.interfacedata.formbean.syzxyy.CostRow;
import com.digihealth.anesthesia.interfacedata.po.ChargItem;

@MyBatisDao
public interface DocPackagesItemDao extends CrudDao<DocPackagesItem>{
    int deleteByPrimaryKey(String pkItId);

    @Override
	int insert(DocPackagesItem record);

    int insertSelective(DocPackagesItem record);

    DocPackagesItem selectByPrimaryKey(String pkItId);

    int updateByPrimaryKeySelective(DocPackagesItem record);

    int updateByPrimaryKey(DocPackagesItem record);

	public List<DocPackagesItem> queryPackagesItemList(@Param("filter")String filter,@Param("systemSearchFormBean")SystemSearchFormBean systemSearchFormBean, @Param("beid")String beid);
	
	public int updateIsCharge(@Param("packagesItem")DocPackagesItem packagesItem);
	
	public DocPackagesItem searchPackagesItemById(@Param("pkItId")String pkItId);
	
	public List<CostRow> queryUnCostListByRegOptId(@Param("regOptId")String regOptId,@Param("costType")String costType);
	
	public List<ChargItem> queryUnChargItemByRegOptId(@Param("regOptId")String regOptId);
	
	public int updateChargeState(@Param("pkItId")String pkItId);
	
	public int updateChargeStateByChargeItemId(@Param("chargeItemId")String chargeItemId, @Param("regOptId")String regOptId);
	
	public List<CostHNHTYYRow> queryHNHTYYUnCostListByRegOptId(@Param("regOptId")String regOptId,@Param("costType")String costType);
}