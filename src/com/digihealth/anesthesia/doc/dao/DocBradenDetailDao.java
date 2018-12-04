package com.digihealth.anesthesia.doc.dao;

import java.util.List;

import com.digihealth.anesthesia.common.persistence.annotation.MyBatisDao;
import com.digihealth.anesthesia.doc.po.DocBradenDetail;

@MyBatisDao
public interface DocBradenDetailDao {
    int deleteByPrimaryKey(String id);

    int insert(DocBradenDetail record);

    int insertSelective(DocBradenDetail record);

    DocBradenDetail selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DocBradenDetail record);

    int updateByPrimaryKey(DocBradenDetail record);
    
    List<DocBradenDetail> selectByEvaluateId(String evaluateId);
}