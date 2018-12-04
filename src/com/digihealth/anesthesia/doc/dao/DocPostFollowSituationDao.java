/*
 * DocPostFollowYxrmDao.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2018-05-29 Created
 */
package com.digihealth.anesthesia.doc.dao;

import org.apache.ibatis.annotations.Param;

import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.doc.po.DocPostFollowSituation;

@MyBatisDao
public interface DocPostFollowSituationDao {
    int deleteByPrimaryKey(String postFollowStId);

    int insert(DocPostFollowSituation record);

    int insertSelective(DocPostFollowSituation record);

    DocPostFollowSituation selectByPrimaryKey(String postFollowStId);

    int updateByPrimaryKeySelective(DocPostFollowSituation record);

    int updateByPrimaryKey(DocPostFollowSituation record);

    DocPostFollowSituation selectPostFollowYxrmByFollowId(@Param("postFollowId") String postFollowId);
	
	int deleteFollowYxrmByFollowId(@Param("postFollowId") String postFollowId); 
}