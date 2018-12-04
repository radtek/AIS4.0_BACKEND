package com.digihealth.anesthesia.doc.dao;

import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.doc.po.DocVisitEvaluate;

@MyBatisDao
public interface DocVisitEvaluateDao {
    int deleteByPrimaryKey(String id);

    int insert(DocVisitEvaluate record);

    int insertSelective(DocVisitEvaluate record);

    DocVisitEvaluate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DocVisitEvaluate record);

    int updateByPrimaryKey(DocVisitEvaluate record);
    
    DocVisitEvaluate selectByRegOptId(String regOptId);
}