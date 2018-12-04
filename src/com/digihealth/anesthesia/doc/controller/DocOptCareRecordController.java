package com.digihealth.anesthesia.doc.controller;

import java.text.ParseException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.common.beanvalidator.ValidatorBean;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.formbean.OptCareRecordFormBean;
import com.digihealth.anesthesia.doc.po.DocOptCareRecord;
import com.digihealth.anesthesia.evt.formbean.SearchRegOptByIdFormBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


/**
 * 手术护理记录
 * <功能详细描述>
 * 
 * @author  zhouyi
 */
@Controller
@RequestMapping(value = "/document")
@Api(value="DocOptCareRecordController",description="手术护理记录处理类")
public class DocOptCareRecordController extends BaseController {
	
	/** 
	 * 查询手术护理
	 * <功能详细描述>
	 * @param map
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	@RequestMapping(value = "/searchOptCareRecordByRegOptId")
	@ResponseBody
	@ApiOperation(value="查询手术护理",httpMethod="POST",notes="查询手术护理")
	public String searchOptCareRecByRegOptId(@ApiParam(name="map", value ="手术护理参数") @RequestBody Map<String, Object> map) {
		logger.info("----------start searchOptCareRecordByRegOptId---------------");
		ResponseValue resp = docOptCareRecordService.searchOptCareRecordByRegOptId(map);
		logger.info("----------end searchOptCareRecordByRegOptId---------------");
		return resp.getJsonStr();
	}

    /** 
     * 保存手术护理单，用户还可以进行修改
     * <功能详细描述>
     * @param optCareRecord
     * @return
     * @throws ParseException 
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/updateOptCareRecord")
    @ResponseBody
	@ApiOperation(value="保存手术护理单，用户还可以进行修改",httpMethod="POST",notes="保存手术护理单，用户还可以进行修改")
    public String updateOptCareRecord(@ApiParam(name="optCareRecordFormBean", value ="手术护理参数") @RequestBody OptCareRecordFormBean optCareRecordFormBean) throws ParseException {
        logger.info("----------start updateOptCareRecord---------------");
        ResponseValue resp = new ResponseValue();
        ValidatorBean validatorBean = beanValidator(optCareRecordFormBean);
        if (!(validatorBean.isValidator())) {
        	resp.setResultCode("10000001");
        	resp.setResultMessage(validatorBean.getMessage());
            return resp.getJsonStr();
        }
        if (null != optCareRecordFormBean.getInOperRoomTime() && !"".equals(optCareRecordFormBean.getInOperRoomTime())) {
            String inOperRoomTime = optCareRecordFormBean.getInOperRoomTime().replace("Z", " UTC");
            optCareRecordFormBean.setInOperRoomTime(inOperRoomTime);
        }
        
        if (null != optCareRecordFormBean.getOutOperRoomTime() && !"".equals(optCareRecordFormBean.getOutOperRoomTime())) {
            String outOperRoomTime = optCareRecordFormBean.getOutOperRoomTime().replace("Z", " UTC");
            optCareRecordFormBean.setOutOperRoomTime(outOperRoomTime);
        }
        resp = docOptCareRecordService.updateOptCareRecord(optCareRecordFormBean);
        logger.info("----------end updateOptCareRecord---------------");
        return resp.getJsonStr();
    }
    
    @RequestMapping(value = "/searchOptCareRecordByRegOptIdNHYY")
    @ResponseBody
    @ApiOperation(value="查询手术护理",httpMethod="POST",notes="查询手术护理")
    public String searchOptCareRecordByRegOptIdNHYY(@ApiParam(name="map", value ="手术护理参数") @RequestBody Map<String, Object> map) {
        logger.info("----------start searchOptCareRecordByRegOptId---------------");
        ResponseValue resp = new ResponseValue();
        String regOptId = map.get("regOptId") != null ? map.get("regOptId").toString() : "";
        String inOperRoomTime = map.get("inOperRoomTime") != null ? map.get("inOperRoomTime").toString() : "";
        DocOptCareRecord optCareRecord = docOptCareRecordService.selectByRegOptIdNHYY(regOptId, inOperRoomTime);
        resp.put("optCareRecord", optCareRecord);
        SearchRegOptByIdFormBean searchRegOptByIdFormBean = basRegOptService.searchApplicationById(regOptId);
        resp.put("searchRegOptByIdFormBean", searchRegOptByIdFormBean);
        logger.info("----------end searchOptCareRecordByRegOptId---------------");
        return resp.getJsonStr();
    }
    
    
    @RequestMapping(value = "/updateOptCareRecordNHYY")
    @ResponseBody
    @ApiOperation(value="保存手术护理单",httpMethod="POST",notes="保存手术护理单")
    public String updateOptCareRecordNHYY(@ApiParam(name="optCareRecord", value ="手术护理参数") @RequestBody DocOptCareRecord optCareRecord)
    {
        logger.info("----------start searchOptCareRecordByRegOptId---------------");
        ResponseValue resp = new ResponseValue();
        docOptCareRecordService.updateOptCareRecordNHYY(optCareRecord);
        logger.info("----------end searchOptCareRecordByRegOptId---------------");
        return resp.getJsonStr();
    }
    
    /** 
     * 保存手术护理单，用户还可以进行修改
     * 耒阳人民医院
     */
    @RequestMapping(value = "/updateOptCareRecordLYYY")
    @ResponseBody
	@ApiOperation(value="保存手术护理单，用户还可以进行修改",httpMethod="POST",notes="保存手术护理单，用户还可以进行修改")
    public String updateOptCareRecordLYYY(@ApiParam(name="optCareRecordFormBean", value ="手术护理参数") @RequestBody OptCareRecordFormBean optCareRecordFormBean) throws ParseException {
        logger.info("----------start updateOptCareRecordLYYY---------------");
        ResponseValue resp = new ResponseValue();
        ValidatorBean validatorBean = beanValidator(optCareRecordFormBean);
        if (!(validatorBean.isValidator())) {
        	resp.setResultCode("10000001");
        	resp.setResultMessage(validatorBean.getMessage());
            return resp.getJsonStr();
        }
        if (null != optCareRecordFormBean.getInOperRoomTime() && !"".equals(optCareRecordFormBean.getInOperRoomTime())) {
            String inOperRoomTime = optCareRecordFormBean.getInOperRoomTime().replace("Z", " UTC");
            optCareRecordFormBean.setInOperRoomTime(inOperRoomTime);
        }
        
        if (null != optCareRecordFormBean.getOutOperRoomTime() && !"".equals(optCareRecordFormBean.getOutOperRoomTime())) {
            String outOperRoomTime = optCareRecordFormBean.getOutOperRoomTime().replace("Z", " UTC");
            optCareRecordFormBean.setOutOperRoomTime(outOperRoomTime);
        }
        resp = docOptCareRecordService.updateOptCareRecordLYYY(optCareRecordFormBean);
        logger.info("----------end updateOptCareRecordLYYY---------------");
        return resp.getJsonStr();
    }
    
    /** 
	 * 查询手术护理
	 * 耒阳人民医院
	 */
    @RequestMapping(value = "/searchOptCareRecordByRegOptIdLYYY")
	@ResponseBody
	@ApiOperation(value="查询手术护理",httpMethod="POST",notes="查询手术护理")
	public String searchOptCareRecordByRegOptIdLYYY(@ApiParam(name="map", value ="手术护理参数") @RequestBody Map<String, Object> map) {
		logger.info("----------start searchOptCareRecordByRegOptIdLYYY---------------");
		ResponseValue resp = docOptCareRecordService.searchOptCareRecordByRegOptIdLYYY(map);
		logger.info("----------end searchOptCareRecordByRegOptIdLYYY---------------");
		return resp.getJsonStr();
	}
}
