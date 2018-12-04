package com.digihealth.anesthesia.doc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.web.BaseController;
import com.wordnik.swagger.annotations.Api;

@Controller
@RequestMapping(value = "/document")
@Api(value = "DocPatCheckRecordController", description = "检查结果处理类")
public class DocPatCheckRecordController extends BaseController {

	/*
	 * 患者对应检测列表
	 */
	@RequestMapping(value = "/getPatCheckRecordList")
	@ResponseBody
	public String getPatCheckRecordList(@RequestBody SystemSearchFormBean searchFormBean){
		logger.info("getPatCheckRecordList===========start");
		ResponseValue resp = new ResponseValue();
		resp.put("checkRecordList", docPatCheckRecordService.getPatCheckRecordList(searchFormBean));
		resp.put("total", docPatCheckRecordService.getTotalPatCheckRecordList(searchFormBean));
		logger.info("getPatCheckRecordList===========end");
		return resp.getJsonStr();
	}
	/**
	 * 获取检查对应明细数据
	 * @param searchFormBean
	 * @return
	 */
	@RequestMapping(value = "/getPatCheckItemList")
	@ResponseBody
	public String getPatCheckItemList(@RequestBody SystemSearchFormBean searchFormBean){
		logger.info("getPatCheckItemList===========start");
		ResponseValue resp = new ResponseValue();
		resp.put("checkItemList", docPatCheckItemService.getPatCheckItemList(searchFormBean));
		logger.info("getPatCheckItemList===========start");
		return resp.getJsonStr();
	}
}
