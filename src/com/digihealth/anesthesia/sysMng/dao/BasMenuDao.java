/*
 * BasMenuDao.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-03-23 Created
 */
package com.digihealth.anesthesia.sysMng.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.digihealth.anesthesia.basedata.formbean.FindAllMenuFormBean;
import com.digihealth.anesthesia.common.persistence.CrudDao;
import com.digihealth.anesthesia.common.persistence.PKEntity;
import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.sysMng.formbean.BasMenuFormBean;
import com.digihealth.anesthesia.sysMng.po.BasMenu;

@MyBatisDao
public interface BasMenuDao extends CrudDao<BasMenu>{
    int deleteByPrimaryKey(@Param("id") String id, @Param("beid") String beid);

    int deleteByPrimaryKeyAndBeid(PKEntity<String> primaryKey);
	
    int insertSelective(BasMenu record);

    BasMenu selectByPrimaryKey(@Param("id") String id, @Param("beid") String beid);

    int updateByPrimaryKeySelective(BasMenu record);

    int updateByPrimaryKey(BasMenu record);
    
    public List<BasMenu> findMenuIsLeft();

    public List<Map<String, Object>> findMenuThr(BasMenu params);
    
	public List<BasMenu> findByParentIdsLike(BasMenu menu);

	public List<BasMenu> findByUserId(BasMenu menu);

	public int updateParentIds(BasMenu menu);

	public int updateSort(BasMenu menu);

	public List<String> findByRoleId(String id);
	
	public List<BasMenu> findMenu(BasMenu params);
	
	public List<BasMenu> findMenuByIds(@Param("ids")List<String> ids,@Param("roleId")String roleId,@Param("beid")String beid);

	public List<BasMenu> findAllByRoleId(@Param("id")String id);
	
	public List<FindAllMenuFormBean> findAllMenu();
	
	public List<BasMenu> findMenuByRoleId(@Param("roleId")String roleId, @Param("beid")String beid, @Param("module")String module);
	
	public List<BasMenuFormBean> selectMenuTree(@Param("beid") String beid,@Param("module")String module);
	
	public List<BasMenu> findSubMenuById(@Param("parentId") String parentId, @Param("beid") String beid);
	
	public List<BasMenu> findSubMenuByBeid(@Param("beid") String beid);
	
	//查询文书的一级目录有哪些菜单
	public List<BasMenu> findDocMenuPartIdByBeid(@Param("beid") String beid);
	
	//查询指定菜单的第一个子节点
	public BasMenu findDocMenuSunId(@Param("menuId") String menuId);
	
}