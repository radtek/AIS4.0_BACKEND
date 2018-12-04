/**     
 * @discription 在此输入一句话描述此文件的作用
 * @author liukui       
 * @created 2015-10-10 下午5:34:19    
 * tags     
 * see_to_target     
 */

package com.digihealth.anesthesia.doc.controller;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.common.beanvalidator.ValidatorBean;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.formbean.AnaesRecordFormBean;
import com.digihealth.anesthesia.doc.po.DocAnaesRecord;
import com.digihealth.anesthesia.doc.po.DocPreVisit;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Title: AnaesRecordController.java Description: 描述
 * 
 * @author liukui
 * @created 2015-10-10 下午5:34:19
 */
@Controller
@RequestMapping(value = "/document")
@Api(value = "DocAnaesRecordController", description = "麻醉记录单处理类")
public class DocAnaesRecordController extends BaseController {

	/**
	 * 
	 * @discription 根据手术ID获取麻醉记录单
	 * @author liukui
	 * @created 2015-10-10 下午5:13:48
	 * @param regOptId
	 * @return
	 */
	@RequestMapping(value = "/searchAnaesRecordByRegOptId")
	@ResponseBody
	@ApiOperation(value = "根据手术ID获取麻醉记录单", httpMethod = "POST", notes = "根据手术ID获取麻醉记录单")
	public String searchAnaesRecordByRegOptId(@ApiParam(name = "record", value = "查询参数") @RequestBody DocAnaesRecord record) {
		logger.info("-------------------begin searchAnaesRecordByRegOptId-------------------");
		ResponseValue resp = new ResponseValue();
		DocAnaesRecord anaesRecord = docAnaesRecordService.searchAnaesRecordByRegOptId(record.getRegOptId());
		resp.put("anaesRecord", anaesRecord);
		resp.setResultCode("1");
		resp.setResultMessage("获取麻醉记录单信息成功!");
		logger.info("-------------------end searchAnaesRecordByRegOptId-------------------");
		return resp.getJsonStr();
	}

	/**
	 * 
	 * @discription 根据手术ID获取麻醉记录单
	 * @author liukui
	 * @created 2015-10-10 下午5:13:48
	 * @param regOptId
	 * @return
	 */
	@RequestMapping(value = "/searchAnaesRecordById")
	@ResponseBody
	@ApiOperation(value = "根据手术ID获取麻醉记录单", httpMethod = "POST", notes = "根据手术ID获取麻醉记录单")
	public String searchAnaesRecordById(@ApiParam(name = "record", value = "查询参数") @RequestBody DocAnaesRecord record) {
		logger.info("------------------------begin searchAnaesRecordById------------------------");
		ResponseValue resp = new ResponseValue();
		DocAnaesRecord anaesRecord = docAnaesRecordService.searchAnaesRecordById(record.getAnaRecordId());
		resp.put("anaesRecord", anaesRecord);
		resp.setResultCode("1");
		resp.setResultMessage("获取麻醉记录单信息成功!");
		logger.info("------------------------end searchAnaesRecordById------------------------");
		return resp.getJsonStr();
	}

	@RequestMapping("/updatefievt")
	@ResponseBody
	@ApiOperation(value = "修改麻醉记录单信息", httpMethod = "POST", notes = "修改麻醉记录单信息")
	public String updatefievt(@ApiParam(name = "record", value = "修改参数") @RequestBody DocAnaesRecord record) {
		logger.info("----------------start updatefievt------------------------");
		ResponseValue resp = new ResponseValue();
		docAnaesRecordService.updatefievt(record);
		resp.setResultCode("1");
		resp.setResultMessage("操作成功");
		logger.info("------------------end updatefievt------------------------");
		return resp.getJsonStr();
	}
	
	@RequestMapping("/updateFlow")
    @ResponseBody
    @ApiOperation(value = "修改镇痛药流速", httpMethod = "POST", notes = "修改镇痛药流速")
    public String updateFlow(@ApiParam(name = "record", value = "修改参数") @RequestBody DocAnaesRecord record) {
        logger.info("----------------start updateFlow------------------------");
        ResponseValue resp = new ResponseValue();
        docAnaesRecordService.updateFlow(record);
        resp.setResultCode("1");
        resp.setResultMessage("操作成功");
        logger.info("------------------end updateFlow------------------------");
        return resp.getJsonStr();
    }

	/**
	 * 
	 * @discription 修改手术护理
	 * @author liukui
	 * @created 2015-10-20 上午9:33:40
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/updateAnaesRecord")
	@ResponseBody
	@ApiOperation(value = "修改手术护理", httpMethod = "POST", notes = "修改手术护理")
	public String updateAnaesRecord(@ApiParam(name = "anaesrecord", value = "修改参数") @RequestBody AnaesRecordFormBean anaesRecordFormBean) {
		logger.info("----------------------begin updateAnaesRecord----------------------");
		ResponseValue resp = new ResponseValue();
		ValidatorBean validatorBean = beanValidator(anaesRecordFormBean);
		if (!(validatorBean.isValidator())) {
			resp.setResultCode("10000001");
			resp.setResultMessage(validatorBean.getMessage());
			return resp.getJsonStr();
		}
		
		DocAnaesRecord anaesRecord = new DocAnaesRecord();
		anaesRecordFormBean.setOptBody(StringUtils.getStringByList(anaesRecordFormBean.getOptBodys()));
		BeanUtils.copyProperties(anaesRecordFormBean, anaesRecord);
		if (StringUtils.isBlank(anaesRecord.getAnaRecordId()))
		{
		    anaesRecord.setAnaRecordId(anaesRecordFormBean.getDocId());
		}
        docAnaesRecordService.changeAnaesRecordState(anaesRecord);
		resp.setResultCode("1");
		resp.setResultMessage("麻醉记录单修改成功!");
		logger.info("----------------------end updateAnaesRecord----------------------");
		return resp.getJsonStr();
	}


	private void setMapValue(DocPreVisit preVisit) {
		JSONObject jasonObject1 = JSONObject.fromObject(preVisit.getBriefHis());
		preVisit.setBriefHisMap((Map) jasonObject1);

		JSONObject jasonObject2 = JSONObject.fromObject(preVisit.getLungbreathCond());
		preVisit.setLungbreathCondMap((Map) jasonObject2);

		JSONObject jasonObject3 = JSONObject.fromObject(preVisit.getBrainNerve());
		preVisit.setBrainNerveMap((Map) jasonObject3);

		JSONObject jasonObject4 = JSONObject.fromObject(preVisit.getSpineLimb());
		preVisit.setSpineLimbMap((Map) jasonObject4);

		JSONObject jasonObject5 = JSONObject.fromObject(preVisit.getBlood());
		preVisit.setBloodMap((Map) jasonObject5);

		JSONObject jasonObject7 = JSONObject.fromObject(preVisit.getDigestion());
		preVisit.setDigestionMap((Map) jasonObject7);

		JSONObject jasonObject8 = JSONObject.fromObject(preVisit.getEndocrine());
		preVisit.setEndocrineMap((Map) jasonObject8);

		JSONObject jasonObject9 = JSONObject.fromObject(preVisit.getInfectious());
		preVisit.setInfectiousMap((Map) jasonObject9);

		JSONObject jasonObject10 = JSONObject.fromObject(preVisit.getAirwayManage());
		preVisit.setAirwayManageMap((Map) jasonObject10);

		JSONObject jasonObject11 = JSONObject.fromObject(preVisit.getSpecialHandle());
		preVisit.setSpecialHandleMap((Map) jasonObject11);

		JSONObject jasonObject12 = JSONObject.fromObject(preVisit.getAnalgesicCond());
		preVisit.setAnalgesicMap((Map) jasonObject12);

		JSONObject jasonObject13 = JSONObject.fromObject(preVisit.getMonitor());
		preVisit.setMonitorMap((Map) jasonObject13);

		JSONObject jasonObject14 = JSONObject.fromObject(preVisit.getHeartBoolCond());
		preVisit.setHeartBoolCondMap((Map) jasonObject14);

		JSONObject jasonObject15 = JSONObject.fromObject(preVisit.getToothAbnormalCond());
		preVisit.setToothAbnormalMap((Map) jasonObject15);

		JSONObject jasonObject16 = JSONObject.fromObject(preVisit.getAssayAbnormalCond());
		preVisit.setAssayAbnormalMap((Map) jasonObject16);
	}
}
