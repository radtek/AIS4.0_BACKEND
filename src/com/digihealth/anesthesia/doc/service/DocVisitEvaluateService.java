package com.digihealth.anesthesia.doc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.doc.po.DocVisitEvaluate;

@Service   
public class DocVisitEvaluateService extends BaseService
{
    public DocVisitEvaluate selectByRegOptId(String regOptId)
    {
        return docVisitEvaluateDao.selectByRegOptId(regOptId);
    }
    
    @Transactional
    public void saveVisitEvaluate(DocVisitEvaluate visitEvaluate)
    {
        docVisitEvaluateDao.updateByPrimaryKey(visitEvaluate);
    }
}
