package com.digihealth.anesthesia.doc.dao;

import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.doc.po.DocBradenEvaluate;

@MyBatisDao
public interface DocBradenEvaluateDao {
    int deleteByPrimaryKey(String id);

    int insert(DocBradenEvaluate record);

    int insertSelective(DocBradenEvaluate record);

    DocBradenEvaluate selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DocBradenEvaluate record);

    int updateByPrimaryKey(DocBradenEvaluate record);
    
    DocBradenEvaluate selectByRegOptId(String regOptId);
}