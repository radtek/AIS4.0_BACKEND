/**     
 * @discription 在此输入一句话描述此文件的作用
 * @author liukui       
 * @created 2015-10-10 下午5:34:19    
 * tags     
 * see_to_target     
 */

package com.digihealth.anesthesia.doc.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.DispatchFormbean;
import com.digihealth.anesthesia.basedata.formbean.SysCodeFormbean;
import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.common.beanvalidator.ValidatorBean;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.formbean.AnaesSummaryItemFormbean;
import com.digihealth.anesthesia.doc.po.DocAnaesRecord;
import com.digihealth.anesthesia.doc.po.DocAnaesSummary;
import com.digihealth.anesthesia.doc.po.DocAnaesSummaryAllergicReaction;
import com.digihealth.anesthesia.doc.po.DocAnaesSummaryVenipuncture;
import com.digihealth.anesthesia.doc.po.DocGeneralAnaes;
import com.digihealth.anesthesia.doc.po.DocNerveBlock;
import com.digihealth.anesthesia.doc.po.DocSpinalCanalPuncture;
import com.digihealth.anesthesia.evt.formbean.SearchFormBean;
import com.digihealth.anesthesia.evt.po.EvtOptLatterDiag;
import com.digihealth.anesthesia.evt.po.EvtOptRealOper;
import com.digihealth.anesthesia.evt.po.EvtRealAnaesMethod;
import com.digihealth.anesthesia.evt.po.EvtShiftChange;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Title: AnaesSummaryController.java Description: 描述
 * 
 * @author liukui
 * @created 2015-10-10 下午5:34:19
 */
@Controller
@RequestMapping(value = "/document")
@Api(value="DocAnaesSummaryController",description="麻醉总结处理类")
public class DocAnaesSummaryController extends BaseController {
	
	/**
	 * 查询麻醉总结单
	 * @param searchBean
	 * @return
	 */
	@RequestMapping(value = "/searchAnaesSummaryList")
	@ResponseBody
	@ApiOperation(value="查询麻醉总结单",httpMethod="POST",notes="查询麻醉总结单")
	public String searchAnaesSummaryList(@ApiParam(name="searchBean", value ="查询参数") @RequestBody SearchFormBean searchBean) {
		logger.info("-------------begin searchAnaesSummaryList-------------");
		ResponseValue resp = new ResponseValue();
		List<DocAnaesSummary> anaesSummaryList = docAnaesSummaryService.searchAnaesSummaryList(searchBean);
		resp.put("resultList", anaesSummaryList);
		logger.info("-------------end searchAnaesSummaryList-------------");
		return resp.getJsonStr();
	}
	
	/**
     * 获取麻醉总结明细信息
     * @param searchBean
     * @return
     */
    @RequestMapping(value = "/searchAnaesSummaryDetail")
    @ResponseBody
    @ApiOperation(value="获取麻醉总结明细信息",httpMethod="POST",notes="获取麻醉总结明细信息")
    public String searchAnaesSummaryDetail(@ApiParam(name="searchBean", value ="查询参数") @RequestBody SearchFormBean searchBean){
        logger.info("-------------begin searchAnaesSummaryDetail-------------");
        ResponseValue result = new ResponseValue();

        //手术信息表
        BasRegOpt opt = basRegOptService.searchRegOptById(searchBean.getRegOptId());
    
        result.put("regOpt", opt);  
        DocAnaesRecord anaesRecord = docAnaesRecordService.getAnaesRecord(searchBean.getRegOptId());
        if(null != anaesRecord)
        {
            searchBean.setDocId(anaesRecord.getAnaRecordId());
        }
        //术后诊断
        List<EvtOptLatterDiag> optLatterDiagList = evtOptLatterDiagService.searchOptLatterDiagList(searchBean);
        
        String optLatterDiagStr = "";
        for (EvtOptLatterDiag optLatterDiag : optLatterDiagList) {
            optLatterDiagStr += optLatterDiag.getName()+",";
        }
        if(StringUtils.isNotBlank(optLatterDiagStr)){
            optLatterDiagStr =  optLatterDiagStr.substring(0, optLatterDiagStr.length()-1);
        }
        
        //实施手术
        List<EvtOptRealOper> optRealOperList = evtOptRealOperService.searchOptRealOperList(searchBean);
        String optRealOperStr = "";
        for (EvtOptRealOper optRealOper : optRealOperList) {
            optRealOperStr += optRealOper.getName()+",";
        }
        if(StringUtils.isNotBlank(optRealOperStr)){
            optRealOperStr =  optRealOperStr.substring(0, optRealOperStr.length()-1);
        }
        
        // 实际麻醉方法
        List<EvtRealAnaesMethod> realAnaesList = evtRealAnaesMethodService.searchRealAnaesMethodList(searchBean);
        String realAnaesStr = "";
        for (EvtRealAnaesMethod realAnaesMethod : realAnaesList) {
            realAnaesStr += realAnaesMethod.getName()+",";
        }
        if(StringUtils.isNotBlank(realAnaesStr)){
            realAnaesStr =  realAnaesStr.substring(0, realAnaesStr.length()-1);
        }
        
        //麻醉总结数据
        List<DocAnaesSummary> anaSumList = docAnaesSummaryService.searchAnaesSummaryList(searchBean);
        DocAnaesSummary anaesSummary = anaSumList.size()>0?anaSumList.get(0):new DocAnaesSummary();
        searchBean.setAnaSumId(anaesSummary.getAnaSumId());
        //椎管内穿刺
        List<DocSpinalCanalPuncture> spinaCanalList = docSpinalCanalPunctureService.searchSpinalCanalPunctureList(searchBean);
        //全身麻醉参数
        List<DocGeneralAnaes> genAnasList = docGeneralAnaesService.searchGeneralAnaesList(searchBean);
        //神经阻滞
        List<DocNerveBlock> nerveBlockList = docNerveBlockService.searchNerveBlockList(searchBean);
        
        List<DocAnaesSummaryAllergicReaction> reacList =  docAnaesSummaryAllergicReactionService.searchAllergicReactionList(searchBean);
        
        List<DocAnaesSummaryVenipuncture> veniList =  docAnaesSummaryVenipunctureService.searchVenipunctureList(searchBean);
        //麻醉医生列表
        List<String> anaesDocList = new ArrayList<String>();
        if (null == anaesSummary.getAnesthetistId())
        {
            DispatchFormbean dispatchPeople = basDispatchService.getDispatchOperByRegOptId(opt.getRegOptId());
            if (dispatchPeople != null)
            {
                anaesSummary.setAnesthetistId(dispatchPeople.getAnesthetistId() != null ? dispatchPeople.getAnesthetistId()
                    : "");
            }
        }
        
        if ("NO_END".equals(anaesSummary.getProcessState()))
        {
            String docId = anaesRecord.getAnaRecordId();
            SearchFormBean sb = new SearchFormBean();
            sb.setDocId(docId);
            List<EvtShiftChange> shiftChangeList = evtShiftChangeService.searchShiftChangeList(sb);
            if (null != shiftChangeList && shiftChangeList.size() > 0)
            {
                anaesSummary.setAnesthetistId(shiftChangeList.get(shiftChangeList.size() - 1).getShiftChangePeopleId());
            }
        }
        
        anaesSummary.setAnaesDocList(anaesDocList);
        List<SysCodeFormbean> puncturePointList =  basSysCodeService.searchSysCodeByGroupId("puncture_point", searchBean.getBeid());
        result.put("puncturePointList", puncturePointList);//穿刺点
        
        result.put("anaesRecord", anaesRecord);
        result.put("anaesSummary", anaesSummary);
        result.put("optLatterDiagStr", optLatterDiagStr);
        result.put("realAnaesStr", realAnaesStr);
        result.put("optRealOperStr", optRealOperStr);
        result.put("spinalCanalPuncture", spinaCanalList.get(0));
        result.put("nerveBlock", nerveBlockList != null && nerveBlockList.size() > 0 ? nerveBlockList.get(0): new DocNerveBlock());
        result.put("generalAnaes", genAnasList != null && genAnasList.size() > 0 ? genAnasList.get(0) : new DocGeneralAnaes()); 
        result.put("allergicReact", reacList != null && reacList.size() > 0 ? reacList.get(0) : new DocAnaesSummaryAllergicReaction()); 
        result.put("venipuncture", veniList != null && veniList.size() > 0 ? veniList.get(0) : new DocAnaesSummaryVenipuncture());
        
        logger.info("-------------end searchAnaesSummaryDetail-------------");
        return result.getJsonStr();
    }
	
	/**
     * 修改麻醉总结单
     * @param anaesSummary
     * @return
     */
    @RequestMapping(value = "/saveAnaesSummaryDetail")
    @ResponseBody
    public String saveAnaesSummaryDetail(@RequestBody AnaesSummaryItemFormbean anaesSummaryItemFormbean)
    {
        logger.info("-------------begin saveAnaesSummaryDetail-------------");
        ResponseValue resp = new ResponseValue();
        ValidatorBean validatorBean = beanValidator(anaesSummaryItemFormbean.getAnaesSummary());
        if (!(validatorBean.isValidator()))
        {
            resp.put("resultCode", "10000001");
            resp.put("resultMessage", validatorBean.getMessage());
            return resp.getJsonStr();
        }
        ValidatorBean validatorBean2 = beanValidator(anaesSummaryItemFormbean.getNerveBlock());
        if (!(validatorBean2.isValidator()))
        {
            resp.put("resultCode", "10000001");
            resp.put("resultMessage", validatorBean2.getMessage());
            return resp.getJsonStr();
        }
        ValidatorBean validatorBean3 = beanValidator(anaesSummaryItemFormbean.getSpinalCanalPuncture());
        if (!(validatorBean3.isValidator()))
        {
            resp.put("resultCode", "10000001");
            resp.put("resultMessage", validatorBean3.getMessage());
            return resp.getJsonStr();
        }
        
        ValidatorBean validatorBean4 = beanValidator(anaesSummaryItemFormbean.getGeneralAnaes());
        if (!(validatorBean4.isValidator()))
        {
            resp.put("resultCode", "10000001");
            resp.put("resultMessage", validatorBean4.getMessage());
            return resp.getJsonStr();
        }
        
        docAnaesSummaryService.saveAnaesSummaryDetail(anaesSummaryItemFormbean);
        logger.info("-------------end saveAnaesSummaryDetail-------------");
        return resp.getJsonStr();
    }
    
}
