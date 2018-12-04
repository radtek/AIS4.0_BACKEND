package com.digihealth.anesthesia.basedata.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.BasDeviceConfigFormBean;
import com.digihealth.anesthesia.basedata.formbean.BasDeviceConfigOperateFormBean;
import com.digihealth.anesthesia.basedata.po.BasDeviceConfig;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.web.BaseController;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * 
 * Title: AnaesMonitorConfigController.java Description:监测点设置Controller
 * 
 * @author liukui
 * @created 2015-9-15 下午1:56:40
 */
@Controller
@RequestMapping(value = "/basedata")
@Api(value = "BasDeviceConfigController", description = "床旁设备配置处理类")
public class BasDeviceConfigController extends BaseController
{
    @RequestMapping(value = "/getDeviceConfigList")
    @ResponseBody
    @ApiOperation(value = "查询床旁设备配置", httpMethod = "POST", notes = "查询床旁设备配置")
    public String getDeviceConfigList(@ApiParam(name = "map", value = "查询参数") @RequestBody Map map)
    {
        logger.info("begin getDeviceConfigList");
        ResponseValue resp = new ResponseValue();
        //如果是复苏室，则将bedId传过来
        String bedId = null;
        if (null != map)
        {
            bedId = null != map.get("bedId") ? (String)map.get("bedId") : null;
        }
        List<BasDeviceConfig> resultList = basDeviceConfigService.getDeviceConfigList(bedId);
        resp.put("resultList", resultList);
        logger.info("end getDeviceConfigList");
        return resp.getJsonStr();
    }
    
    /**
     * 设备监测项数据配置成功
     * 
     * @param BasDeviceConfigOperateFormBean
     * @return
     */
    @RequestMapping(value = "/saveDeviceConfig")
    @ResponseBody
    @ApiOperation(value = "保存设备监测项数据配置", httpMethod = "POST", notes = "保存设备监测项数据配置")
    public String saveDeviceConfig(@ApiParam(name = "deviceConfigOperateFormBean", value = "床旁设备配置对象") @RequestBody BasDeviceConfigOperateFormBean deviceConfigOperateFormBean)
    {
        logger.info("begin saveDeviceConfig");
        ResponseValue resp = new ResponseValue();
        basDeviceConfigService.saveDeviceConfig(deviceConfigOperateFormBean);
        logger.info("end saveDeviceConfig");
        return resp.getJsonStr();
    }
    
    /**
     * 运营中心新增局点时，配置局点对应的额设备信息
     * 
     * @param BasDeviceConfigOperateFormBean
     * @return
     */
    @RequestMapping(value = "/saveBeidDeviceConfig")
    @ResponseBody
    @ApiOperation(value = "配置局点对应的设备信息", httpMethod = "POST", notes = "配置局点对应的设备信息")
    public String saveBeidDeviceConfig(@ApiParam(name = "deviceConfigOperateFormBean", value = "配置局点对应的设备信息") @RequestBody BasDeviceConfigFormBean basDeviceConfig)
    {
        logger.info("begin saveBeidDeviceConfig");
        ResponseValue resp = new ResponseValue();
        basDeviceConfigService.saveBeidDeviceConfig(basDeviceConfig);
        logger.info("end saveBeidDeviceConfig");
        return resp.getJsonStr();
    }
    
    
}
