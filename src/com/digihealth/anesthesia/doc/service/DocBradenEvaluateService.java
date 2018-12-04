package com.digihealth.anesthesia.doc.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.doc.formbean.BradenEvaluateFormbean;
import com.digihealth.anesthesia.doc.po.DocBradenDetail;
import com.digihealth.anesthesia.doc.po.DocBradenEvaluate;

@Service
public class DocBradenEvaluateService extends BaseService
{
    public BradenEvaluateFormbean selectByRegOptId(String regOptId)
    {
        BradenEvaluateFormbean formbean = new BradenEvaluateFormbean();
        DocBradenEvaluate bradenEvaluate = docBradenEvaluateDao.selectByRegOptId(regOptId);
        if (null != bradenEvaluate)
        {
            formbean.setBradenEvaluate(bradenEvaluate);
            formbean.setBradenDetailList(docBradenDetailDao.selectByEvaluateId(bradenEvaluate.getId()));
        }
        return formbean;
    }
    
    @Transactional
    public void saveBradenEvaluate(BradenEvaluateFormbean formbean)
    {
        DocBradenEvaluate bradenEvaluate = formbean.getBradenEvaluate();
        docBradenEvaluateDao.updateByPrimaryKey(bradenEvaluate);
        List<DocBradenDetail> bradenDetailList = formbean.getBradenDetailList();
        if (null != bradenDetailList && bradenDetailList.size() > 0)
        {
            for (DocBradenDetail bradenDetail : bradenDetailList)
            {
                if (StringUtils.isNotBlank(bradenDetail.getId()))
                {
                    docBradenDetailDao.updateByPrimaryKey(bradenDetail);
                }
                else
                {
                    bradenDetail.setId(GenerateSequenceUtil.generateSequenceNo());
                    bradenDetail.setEvaluateId(bradenEvaluate.getId());
                    docBradenDetailDao.insert(bradenDetail);
                }
            }
        }
    }
}
