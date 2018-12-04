package com.digihealth.anesthesia.basedata.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.po.BasAnaesKndgbase;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.DateUtils;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
@Service
public class BasAnaesKndgbaseService extends BaseService
{
    public List<BasAnaesKndgbase> queryAnaesKndgbaseList(BasAnaesKndgbase basAnaesKndgbase){
        if (StringUtils.isBlank(basAnaesKndgbase.getBeid()))
        {
            basAnaesKndgbase.setBeid(getBeid());
        }
        return basAnaesKndgbaseDao.queryAnaesKndgbaseList(basAnaesKndgbase);
    }
    
    @Transactional
    public String updateAnaesKndgbase(BasAnaesKndgbase basAnaesKndgbase){
        if (StringUtils.isBlank(basAnaesKndgbase.getBeid()))
        {
            basAnaesKndgbase.setBeid(getBeid());
        }
        
        if(StringUtils.isNotBlank(basAnaesKndgbase.getId())){
            basAnaesKndgbaseDao.updateByPrimaryKeySelective(basAnaesKndgbase);
            return "";
        }else{
            String id = GenerateSequenceUtil.generateSequenceNo();
            basAnaesKndgbase.setId(id);
            basAnaesKndgbase.setCreateTime(DateUtils.getCurrDate());
            basAnaesKndgbase.setOpen(0);
            basAnaesKndgbaseDao.insert(basAnaesKndgbase);
            return id;
        }
    }
    
    @Transactional
    public int deleteAnaesKndgbase(BasAnaesKndgbase basAnaesKndgbase){
        if (StringUtils.isBlank(basAnaesKndgbase.getBeid()))
        {
            basAnaesKndgbase.setBeid(getBeid());
        }
        int total = basAnaesKndgbaseDao.selectByPid(basAnaesKndgbase.getId(),basAnaesKndgbase.getBeid());
        if(total >0){
            return 0;
        }else{
            return basAnaesKndgbaseDao.deleteByPrimaryKey(basAnaesKndgbase.getId());
        }
    }
}
