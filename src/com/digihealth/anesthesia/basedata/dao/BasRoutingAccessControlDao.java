/*
 * BasRoutingAccessControlDao.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-03-31 Created
 */
package com.digihealth.anesthesia.basedata.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.digihealth.anesthesia.basedata.po.BasRoutingAccessControl;
import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface BasRoutingAccessControlDao {
    int deleteByPrimaryKey(String id);

    int insert(BasRoutingAccessControl record);

    int insertSelective(BasRoutingAccessControl record);

    BasRoutingAccessControl selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BasRoutingAccessControl record);

    int updateByPrimaryKey(BasRoutingAccessControl record);

	List<BasRoutingAccessControl> selectAllRac();
	
	List<BasRoutingAccessControl> getBasRoutingAccessByBeid(@Param("beid") String beid);
	
	List<BasRoutingAccessControl> getBasRoutingAccessByAliasName(@Param("aliasName") String aliasName);
	
	List<BasRoutingAccessControl> getBasRoutingAccessByUrl(@Param("uri") String uri,@Param("beid") String beid);
}