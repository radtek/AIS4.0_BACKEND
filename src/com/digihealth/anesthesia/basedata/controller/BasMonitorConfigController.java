package com.digihealth.anesthesia.basedata.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfig;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * Title: BasDeviceMonitorConfigController.java Description:监测点设置Controller
 * 
 * @author liukui
 * @created 2015-9-15 下午1:56:40
 */
@Controller
@RequestMapping(value = "/basedata")
@Api(value = "BasDeviceMonitorConfigController", description = "床旁设备配置处理类")
public class BasMonitorConfigController extends BaseController
{
    /**
     * 运营中心新增局点时，配置局点对应的额设备信息
     * 
     * @param BasDeviceConfigOperateFormBean
     * @return
     */
    @RequestMapping(value = "/saveBasMonitorConfig")
    @ResponseBody
    @ApiOperation(value = "配置采集指标信息", httpMethod = "POST", notes = "配置采集指标信息")
    public String saveBasMonitorConfig(@ApiParam(name = "BasMonitorConfig", value = "配置采集指标信息") @RequestBody BasMonitorConfig basMonitorConfig)
    {
        logger.info("begin saveBasMonitorConfig");
        ResponseValue resp = new ResponseValue();
        basMonitorConfigService.batUpdateMonitorConfig(basMonitorConfig);
        logger.info("end saveBasMonitorConfig");
        return resp.getJsonStr();
    }
    
    /**
     * 运营中心新增采集指标
     * 
     * @param BasDeviceConfigOperateFormBean
     * @return
     */
    @RequestMapping(value = "/insertBasMonitorConfig")
    @ResponseBody
    @ApiOperation(value = "新增采集指标", httpMethod = "POST", notes = "新增采集指标")
    public String insertBasMonitorConfig(@ApiParam(name = "BasMonitorConfig", value = "新增采集指标") @RequestBody BasMonitorConfig basMonitorConfig)
    {
        logger.info("begin insertBasMonitorConfig");
        ResponseValue resp = new ResponseValue();
        basMonitorConfigService.insertBasMonitorConfig(basMonitorConfig);
        logger.info("end insertBasMonitorConfig");
        return resp.getJsonStr();
    }
    
    /**
     * 运营中心查询所有基础采集指标
     * 
     * @param 
     * @return
     */
    @RequestMapping(value = "/queryBasMonitorConfigList")
    @ResponseBody
    @ApiOperation(value = "查询所有基础采集指标", httpMethod = "POST", notes = "查询所有基础采集指标")
    public String queryBasMonitorConfigList(@ApiParam(name = "SystemSearchFormBean", value = "新增采集指标") @RequestBody SystemSearchFormBean systemSearchFormBean)
    {
        logger.info("begin queryBasMonitorConfigList");
        ResponseValue resp = new ResponseValue();
        resp.put("resultList", basMonitorConfigService.queryBasMonitorConfigList(systemSearchFormBean));
        resp.put("total", basMonitorConfigService.queryBasMonitorConfigListTotal(systemSearchFormBean));
        logger.info("end queryBasMonitorConfigList");
        return resp.getJsonStr();
    }
    
}
