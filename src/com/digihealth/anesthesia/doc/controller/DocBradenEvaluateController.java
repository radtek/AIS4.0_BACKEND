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
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.formbean.BradenEvaluateFormbean;
import com.digihealth.anesthesia.doc.po.DocBradenDetail;
import com.digihealth.anesthesia.doc.po.DocBradenEvaluate;
import com.digihealth.anesthesia.evt.formbean.SearchRegOptByIdFormBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/document")
@Api(value="DocBradenEvaluateController",description="手术室压疮风险评估表处理类")
public class DocBradenEvaluateController extends BaseController
{
    @RequestMapping(value = "/searchBradenEvaluateByRegOptId")
    @ResponseBody
    @ApiOperation(value="根据手术ID获取手术室压疮风险评估表",httpMethod="POST",notes="根据手术ID获取手术室压疮风险评估表")
    public String searchBradenEvaluateByRegOptId(@ApiParam(name = "map", value = "查询参数") @RequestBody Map map)
    {
        logger.info("====================begin searchBradenEvaluateByRegOptId====================");
        ResponseValue resp = new ResponseValue();
        String regOptId = map.get("regOptId") != null ? map.get("regOptId").toString() : "";
        BradenEvaluateFormbean formbean = docBradenEvaluateService.selectByRegOptId(regOptId);
        DocBradenEvaluate bradenEvaluate = formbean.getBradenEvaluate();
        if (null == bradenEvaluate)
        {
            resp.setResultCode("30000002");
            resp.setResultMessage("手术室压疮风险评估记录不存在");
            return resp.getJsonStr();
        }
        DispatchFormbean dispatchFormbean = basDispatchService.getDispatchOperByRegOptId(regOptId);
        String circunurse = null;
        if (null != dispatchFormbean)
        {
            circunurse = dispatchFormbean.getCircunurseId1();
        }
        List<DocBradenDetail> bradenDetailList = formbean.getBradenDetailList();
        if (null == bradenDetailList || bradenDetailList.size() == 0)
        {
            bradenDetailList = new ArrayList<DocBradenDetail>();
            DocBradenDetail bradenDetail = new DocBradenDetail();
            bradenDetail.setEvaluateId(bradenEvaluate.getId());
            bradenDetail.setType("术前");
            bradenDetail.setEvaluator(circunurse);
            bradenDetailList.add(bradenDetail);
            
            bradenDetail = new DocBradenDetail();
            bradenDetail.setEvaluateId(bradenEvaluate.getId());
            bradenDetail.setType("术中");
            bradenDetail.setEvaluator(circunurse);
            bradenDetailList.add(bradenDetail);
            
            bradenDetail = new DocBradenDetail();
            bradenDetail.setEvaluateId(bradenEvaluate.getId());
            bradenDetail.setEvaluator(circunurse);
            bradenDetailList.add(bradenDetail);
            formbean.setBradenDetailList(bradenDetailList); 
        }
        
        SearchRegOptByIdFormBean searchRegOptByIdFormBean = basRegOptService.searchApplicationById(regOptId);
        resp.put("regOptItem",searchRegOptByIdFormBean);
        resp.put("bradenEvaluate", bradenEvaluate);
        resp.put("bradenDetailList", formbean.getBradenDetailList());
        
        logger.info("====================end searchBradenEvaluateByRegOptId====================");
        return resp.getJsonStr();
    }
    
    @RequestMapping(value = "/saveBradenEvaluate")
    @ResponseBody
    @ApiOperation(value="更新手术室压疮风险评估表",httpMethod="POST",notes="更新手术室压疮风险评估表")
    public String saveBradenEvaluate(@ApiParam(name = "formbean", value = "手术室压疮风险评估表对象") @RequestBody BradenEvaluateFormbean formbean)
    {
        logger.info("====================begin saveBradenEvaluate====================");
        ResponseValue resp = new ResponseValue();
        if (null == formbean || null == formbean.getBradenEvaluate())
        {
            resp.setResultCode("30000002");
            resp.setResultMessage("手术室压疮风险评估表不存在");
            return resp.getJsonStr();
        }
        docBradenEvaluateService.saveBradenEvaluate(formbean);
        logger.info("====================end saveBradenEvaluate====================");
        return resp.getJsonStr();
    }
}
