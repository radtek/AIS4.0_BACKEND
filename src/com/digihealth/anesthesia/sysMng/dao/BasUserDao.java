/*
 * BasUserDao.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-03-23 Created
 */
package com.digihealth.anesthesia.sysMng.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.digihealth.anesthesia.basedata.formbean.BaseInfoQuery;
import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.common.persistence.CrudDao;
import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.sysMng.formbean.BasUserFormBean;
import com.digihealth.anesthesia.sysMng.formbean.UserInfoFormBean;
import com.digihealth.anesthesia.sysMng.po.BasUser;

@MyBatisDao
public interface BasUserDao extends CrudDao<BasUser>{
    int deleteByPrimaryKey(@Param("userName") String userName, @Param("beid") String beid);

    void deleteByBeId(@Param("beid") String beid);

    int insertSelective(BasUser record);

    BasUser selectByPrimaryKey(@Param("userName") String userName, @Param("beid") String beid);

	List<BasUser> selectEntityList(BasUser basUser);
	
    int updateByPrimaryKeySelective(BasUser record);

    int updateByPrimaryKey(BasUser record);

    int updateToken(BasUser record);

    public BasUser get(BasUser entity, @Param("beid") String beid);
    
	public BasUser getByLoginName(@Param("loginName") String loginName, @Param("beid") String beid);

	public int getAllUserTotal(@Param("sql") String sql, @Param("beid") String beid);

	public List<UserInfoFormBean> getSelectUser(@Param("sql") String sql, @Param("beid") String beid);
	
	public List<BasUser> getUserList(@Param("baseQuery") BaseInfoQuery baseQuery);
	
    public List<BasUserFormBean> queryUserList(@Param("filter")String filter,@Param("systemSearchFormBean")SystemSearchFormBean systemSearchFormBean);
    
    public int queryUserListTotal(@Param("filter")String filter,@Param("systemSearchFormBean")SystemSearchFormBean systemSearchFormBean);
    
    public BasUser searchUserById(@Param("username")String username,@Param("beid")String beid);

	public List<BasUser> getAllUser(@Param("sql") String sql, @Param("beid") String beid, @Param("pageNo") int pageNo, @Param("pageSize") int pageSize);

	public BasUser searchHnUserById(@Param("id")String id, @Param("beid")String beid);

	public List<BasUser> getUserOrderbyId(@Param("beid")String beid);

	public BasUser anaesDirectorUserLogin(@Param("loginName") String loginName,@Param("administrativeLeve")String administrativeLeve, @Param("beid")String beid);

	public BasUser userLogin(@Param("loginName") String loginName,@Param("userType")String userType, @Param("beid")String beid);

	public BasUser getRoleByLoginName(@Param("loginName") String loginName, @Param("beid")String beid);
	
	public String selectHisIdByUserName(@Param("loginName") String loginName, @Param("beid")String beid);
	
	public String selectUserNameByHisId(@Param("hisId") String hisId, @Param("beid")String beid);
	
	/**
     * 查询在岗麻醉医师总数
     * @return
     */
    public int selectAnaesDocNum(@Param("beid")String beid);
    
    public String selectNameByUserName(@Param("userName") String username, @Param("beid")String beid);
}