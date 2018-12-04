package com.digihealth.anesthesia.doc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.DispatchFormbean;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.po.DocVisitEvaluate;
import com.digihealth.anesthesia.evt.formbean.SearchRegOptByIdFormBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/document")
@Api(value="DocVisitEvaluateController",description="手术室病人方式评估表处理类")
public class DocVisitEvaluateController extends BaseController
{
    @RequestMapping(value = "/searchVisitEvaluateByRegOptId")
    @ResponseBody
    @ApiOperation(value="根据手术ID获取手术室病人方式评估表",httpMethod="POST",notes="根据手术ID获取手术室病人方式评估表")
    public String searchVisitEvaluateByRegOptId(@ApiParam(name = "map", value = "查询参数") @RequestBody Map map)
    {
        logger.info("====================begin searchVisitEvaluateByRegOptId====================");
        ResponseValue resp = new ResponseValue();
        String regOptId = map.get("regOptId") != null ? map.get("regOptId").toString() : "";
        DocVisitEvaluate visitEvaluate = docVisitEvaluateService.selectByRegOptId(regOptId);
        if (null == visitEvaluate)
        {
            resp.setResultCode("30000002");
            resp.setResultMessage("手术室病人方式评估表不存在");
            return resp.getJsonStr();
        }
        
        List<String> designedInstrnurses = new ArrayList<String>();
        List<String> designedCircunurses = new ArrayList<String>();
        DispatchFormbean formbean = basDispatchService.getDispatchOperByRegOptId(regOptId);
        if (null != formbean)
        {
            if (StringUtils.isNotBlank(formbean.getInstrnurseName1()))
            {
                designedInstrnurses.add(formbean.getInstrnurseName1());
            }
            if (StringUtils.isNotBlank(formbean.getInstrnurseName2()))
            {
                designedInstrnurses.add(formbean.getInstrnurseName2());
            }
            if (StringUtils.isNotBlank(formbean.getCircunurseName1()))
            {
                designedCircunurses.add(formbean.getCircunurseName1());
            }
            if (StringUtils.isNotBlank(formbean.getCircunurseName2()))
            {
                designedCircunurses.add(formbean.getCircunurseName2());
            }
            
            if (null == visitEvaluate.getVisitNurse())
            {
                visitEvaluate.setVisitNurse(formbean.getCircunurseId1());
            }
        }
        SearchRegOptByIdFormBean searchRegOptByIdFormBean = basRegOptService.searchApplicationById(regOptId);
        resp.put("regOptItem",searchRegOptByIdFormBean);
        resp.put("docVisitEvaluate", visitEvaluate);
        resp.put("designedInstrnurse", StringUtils.getStringByList(designedInstrnurses));
        resp.put("designedCircunurses", StringUtils.getStringByList(designedCircunurses));
        
        logger.info("====================end searchVisitEvaluateByRegOptId====================");
        return resp.getJsonStr();
    }
    
    @RequestMapping(value = "/saveVisitEvaluate")
    @ResponseBody
    @ApiOperation(value="更新手术室病人方式评估表",httpMethod="POST",notes="更新手术室病人方式评估表")
    public String saveVisitEvaluate(@ApiParam(name = "docVisitEvaluate", value = "手术室病人方式评估表对象") @RequestBody DocVisitEvaluate docVisitEvaluate)
    {
        logger.info("====================begin saveVisitEvaluate====================");
        ResponseValue resp = new ResponseValue();
        if (null == docVisitEvaluate)
        {
            resp.setResultCode("30000002");
            resp.setResultMessage("手术室病人方式评估表不存在");
            return resp.getJsonStr();
        }
        docVisitEvaluateService.saveVisitEvaluate(docVisitEvaluate);
        logger.info("====================end saveVisitEvaluate====================");
        return resp.getJsonStr();
    }
}
