/**     
 * @discription 在此输入一句话描述此文件的作用
 * @author chengwang       
 * @created 2015-10-10 下午5:34:19    
 * tags     
 * see_to_target     
 */

package com.digihealth.anesthesia.doc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.DesignedOptCodes;
import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.basedata.utils.BasRegOptUtils;
import com.digihealth.anesthesia.common.beanvalidator.ValidatorBean;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.utils.PingYinUtil;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.formbean.OptNurseInstrubillItemFormbean;
import com.digihealth.anesthesia.doc.po.DocAnaesRecord;
import com.digihealth.anesthesia.doc.po.DocInstrubillItem;
import com.digihealth.anesthesia.doc.po.DocOptCareRecord;
import com.digihealth.anesthesia.doc.po.DocOptNurse;
import com.digihealth.anesthesia.evt.formbean.SearchFormBean;
import com.digihealth.anesthesia.evt.formbean.SearchRegOptByIdFormBean;
import com.digihealth.anesthesia.evt.po.EvtOptRealOper;
import com.digihealth.anesthesia.evt.service.EvtParticipantService;
import com.digihealth.anesthesia.sysMng.formbean.UserInfoFormBean;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Title: PreVisitController.java Description: 描述
 * 
 * @author chengwang
 * @created 2015-10-10 下午5:34:19
 */
@Controller
@RequestMapping(value = "/document")
@Api(value="DocOptNurseController",description="手术清点单处理类")
public class DocOptNurseController extends BaseController {

	/**
	 * 
	 * @discription 修改手术护理
	 * @author chengwang
	 * @created 2015-10-20 上午9:33:40
	 * @param docId
	 * @return
	 */
	@RequestMapping(value = "/updateOptNurse")
	@ResponseBody
	@ApiOperation(value="修改手术清点单",httpMethod="POST",notes="修改手术清点单")
	public String updateOptNurse(@ApiParam(name="optNurseItem", value ="修改参数") @RequestBody OptNurseInstrubillItemFormbean optNurseItem) {
		logger.info("-----------------begin updateOptNurse-----------------");
		ResponseValue resp = new ResponseValue();
		ValidatorBean validatorBean = beanValidator(optNurseItem.getOptNurse());
		if(!(validatorBean.isValidator())){
			resp.setResultCode("10000001");
			resp.setResultMessage(validatorBean.getMessage());
			return resp.getJsonStr();
		}
		resp = docOptNurseService.updateOptNurse(optNurseItem);
		logger.info("-----------------end updateOptNurse-----------------");
		return resp.getJsonStr();
	}

	/**
	 * 
	 * @discription 插入器械
	 * @author chengwang
	 * @created 2015-10-22 下午1:56:23
	 * @return
	 */
	@RequestMapping(value = "/insertInstrubillItem")
	@ResponseBody
	@ApiOperation(value="插入器械",httpMethod="POST",notes="插入器械")
	public String insertInstrubillItem(@ApiParam(name="map", value ="保存参数") @RequestBody Map<String, Object> map) {
		logger.info("-----------------begin insertInstrubillItem-----------------");
		ResponseValue resp = new ResponseValue();
		String regOptId = map.get("regOptId") == null ? "" : map.get("regOptId").toString();
		String optNurseId = map.get("optNurseId") == null ? "" : map.get("optNurseId").toString();
		String instrumentCode = map.get("instrumentId") == null ? "" : map.get("instrumentId").toString();
		String instrsuitCode = map.get("instrsuitId") == null ? "" : map.get("instrsuitId").toString();
		String type = map.get("type") == null ? "" : map.get("type").toString();
		String instrumentName = map.get("instrumentName") == null ? "" : map.get("instrumentName").toString();
		List<DocInstrubillItem> list = docOptNurseService.insertInstubillItem(regOptId,
				optNurseId, instrumentCode, instrsuitCode, type, instrumentName);
		resp.put("result", list);
		logger.info("-----------------end insertInstrubillItem-----------------");
		return resp.getJsonStr();
	}

	/**
	 * 
	 * @discription 删除器械
	 * @author chengwang
	 * @created 2015-10-23 上午9:39:38
	 */
	@RequestMapping(value = "/deleteInstrubillItem")
	@ResponseBody
	@ApiOperation(value="删除器械",httpMethod="POST",notes="删除器械")
	public String deleteInstrubillItem(@ApiParam(name="map", value ="删除参数") @RequestBody Map<String, Object> map) {
		logger.info("-----------------begin deleteInstrubillItem-----------------");
		ResponseValue resp = new ResponseValue();
		String emptyFlag = map.get("emptyFlag")!=null?map.get("emptyFlag").toString():null;
		String instruItemId = map.get("instruItemId")!=null?map.get("instruItemId").toString():"";
		String regOptId = map.get("regOptId")!=null?map.get("regOptId").toString():null;
		String type = map.get("type")!=null?map.get("type").toString():null;
		int result = 0;
		if ("1".equals(emptyFlag))
		{
		    result = docInstrubillItemService.deleteByRegOptId(regOptId, type);
		}
		else
		{
		    result =docInstrubillItemService.deleteInstrubillItem(instruItemId);
		}
		if(result == 1){
			resp.setResultCode("1");
			resp.setResultMessage("删除手术所用器械成功!");
		}
		if(result == 0){
			resp.setResultCode("1");
			resp.setResultMessage("删除手术所用器械失败!");
		}
		logger.info("-----------------end deleteInstrubillItem-----------------");
		return resp.getJsonStr();
	}
	
    /**
     * 
     * @discription 根据手术ID获取手术护理(南华局点)
     * @author chengwang
     * @created 2015-10-10 下午5:13:48
     * @param regOptId
     * @return
     */
    @RequestMapping(value = "/searchOptNurseByRegOptId")
    @ResponseBody
    @ApiOperation(value="根据手术ID获取手术清点单",httpMethod="POST",notes="根据手术ID获取手术清点单")
    public String searchOptNurseByRegOptId(@ApiParam(name="map", value ="查询参数")@RequestBody Map map) {
        logger.info("------------------begin searchOptNurseByRegOptId----------------------------------");
        ResponseValue resp = new ResponseValue();
        String regOptId = map.get("regOptId") != null ? map.get("regOptId").toString() : "";
        
        DocOptNurse optNurse = docOptNurseService.searchOptNurseByRegOptId(regOptId);
        if (optNurse == null)
        {
            resp.put("resultCode", "40000002");
            resp.put("resultMessage", "护理记录单不存在");
            return resp.getJsonStr();
        }
        List<DocInstrubillItem> instrubillItem1 =
            docInstrubillItemService.searchInstrubillItemByRegOptId(regOptId, "1");// 器械
        
        List<DocInstrubillItem> instrubillItem2 =
            docInstrubillItemService.searchInstrubillItemByRegOptId(regOptId, "2");// 敷料
        
        
        SearchRegOptByIdFormBean searchRegOptByIdFormBean = basRegOptService.searchApplicationById(regOptId);
        
        DocAnaesRecord anaesRecord = docAnaesRecordService.searchAnaesRecordByRegOptId(regOptId);
        SearchFormBean searchBean = new SearchFormBean();
        searchBean.setDocId(anaesRecord.getAnaRecordId());
        
        // 首次进入清点单或者点击数据同步时，不从清点表中获取数据
        List<String> instrnuseList = new ArrayList<String>();
        if ("1".equals(map.get("type")))
        {
            // 全麻时从麻醉记录单中获取到器械护士
            searchBean.setRole(EvtParticipantService.ROLE_NURSE);
            searchBean.setType(EvtParticipantService.OPER_TYPE_INSTRUMENT);
            List<UserInfoFormBean> instruNurseList = evtParticipantService.getSelectParticipantList(searchBean);
            if (instruNurseList != null && instruNurseList.size() > 0)
            {
                for (int i = 0; i < instruNurseList.size(); i++)
                {
                    instrnuseList.add(instruNurseList.get(i).getId());
                }
            }
        }
        else if (StringUtils.isNotBlank(optNurse.getInstrnuseId()))
        {
            String[] instruNurseAry = optNurse.getInstrnuseId().split(",");
            if (null != instruNurseAry && instruNurseAry.length > 0)
            {
                for (int i = 0; i < instruNurseAry.length; i++)
                {
                    instrnuseList.add(instruNurseAry[i]);
                }
            }
        }
        optNurse.setInstrnuseList(instrnuseList);
        optNurse.setInstrnuseId(StringUtils.getStringByList(instrnuseList));
        
        // 首次进入清点单或者点击数据同步时，不从清点表中获取数据
        List<String> circunurseList = new ArrayList<String>();
        if ("1".equals(map.get("type")))
        {
            // 全麻时从麻醉记录单中获取到巡回护士
            searchBean.setRole(EvtParticipantService.ROLE_NURSE);
            searchBean.setType(EvtParticipantService.OPER_TYPE_TOUR);
            List<UserInfoFormBean> circuNurseList = evtParticipantService.getSelectParticipantList(searchBean);
            if (circuNurseList != null && circuNurseList.size() > 0)
            {
                for (int i = 0; i < circuNurseList.size(); i++)
                {
                    circunurseList.add(circuNurseList.get(i).getId());
                }
            }
        }
        else if (StringUtils.isNotBlank(optNurse.getCircunurseId()))
        {
            String[] circuNurseAry = optNurse.getCircunurseId().split(",");
            if (null != circuNurseAry && circuNurseAry.length > 0)
            {
                for (int i = 0; i < circuNurseAry.length; i++)
                {
                    circunurseList.add(circuNurseAry[i]);
                }
            }
        }
        optNurse.setCircunurseList(circunurseList);
        optNurse.setCircunurseId(StringUtils.getStringByList(circunurseList));
        
        // 手术医生
        optNurse.setOperDoctorList(StringUtils.getListByString(optNurse.getOperDoctor()));
        
        optNurse.setShiftCircunurseList(StringUtils.getListByString(optNurse.getShiftCircunurseId()));
        optNurse.setShiftInstrnuseList(StringUtils.getListByString(optNurse.getShiftInstrnuseId())); 
        
        //术中巡回护士
        optNurse.setMidCircunurseList(StringUtils.getListByString(optNurse.getMidCircunurseId()));
        
        // 手术名称
        BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
        List<DesignedOptCodes> operDefList = new ArrayList<DesignedOptCodes>();
        String operatorId = "";
        String operatorName = "";
        
        if (0 == regOpt.getIsLocalAnaes())
        { // 全麻
            List<EvtOptRealOper> optRealOperList = evtOptRealOperService.searchOptRealOperList(searchBean);
            for (EvtOptRealOper optRealOper : optRealOperList)
            {
                DesignedOptCodes designedOptCode = new DesignedOptCodes();
                designedOptCode.setOperDefId(optRealOper.getOperDefId());
                designedOptCode.setName(optRealOper.getName());
                designedOptCode.setPinYin(PingYinUtil.getFirstSpell(optRealOper.getName()));
                operDefList.add(designedOptCode);
                
                if (StringUtils.isBlank(operatorId))
                {
                    operatorId = optRealOper.getOperDefId();
                }
                else
                {
                    operatorId = operatorId + "," + optRealOper.getOperDefId();
                }
                
                if (StringUtils.isBlank(operatorName))
                {
                    operatorName = optRealOper.getName();
                }
                else
                {
                    operatorName = operatorName + "," + optRealOper.getName();
                }
            }
        }
        else
        {
            DocOptCareRecord optCareRecord = docOptCareRecordService.selectByRegOptId(regOptId);
            if (null != optCareRecord)
            {
                operDefList = BasRegOptUtils.getOperDefList(optCareRecord.getOperationCode());
                operatorId = optCareRecord.getOperationCode();
                operatorName = optCareRecord.getOperationName();
            }
        }
        optNurse.setOperationNameList(operDefList);
        optNurse.setOperatorId(operatorId);
        optNurse.setOperatorName(operatorName);
        
        // 数据同步
        if ("1".equals("type"))
        {
            OptNurseInstrubillItemFormbean optNurseItem = new OptNurseInstrubillItemFormbean();
            optNurseItem.setOptNurse(optNurse);
            docOptNurseService.updateOptNurse(optNurseItem);
        }
        resp.put("result", "true");
        resp.put("resultCode", "1");
        resp.put("resultMessage", "查询成功!");
        resp.put("optNurseItem", optNurse);
        resp.put("instrubillItem1", instrubillItem1);
        resp.put("instrubillItem2", instrubillItem2);
        resp.put("searchRegOptByIdFormBean", searchRegOptByIdFormBean);
        
        logger.info("---------------end searchOptNurseByRegOptId----------------------");
        return resp.getJsonStr();
    }
    
    
    /**
     * 
     * @discription 根据手术ID获取手术护理(南华局点)
     * @author chengwang
     * @created 2015-10-10 下午5:13:48
     * @param regOptId
     * @return
     */
    @RequestMapping(value = "/searchOptNurseByRegOptIdNHYY")
    @ResponseBody
    @ApiOperation(value="根据手术ID获取手术清点单",httpMethod="POST",notes="根据手术ID获取手术清点单")
    public String searchOptNurseByRegOptIdNHYY(@ApiParam(name="map", value ="查询参数")@RequestBody Map map) {
        logger.info("------------------begin searchOptNurseByRegOptId----------------------------------");
        ResponseValue resp = new ResponseValue();
        String regOptId = map.get("regOptId") != null ? map.get("regOptId").toString() : "";
        
        DocOptNurse optNurse = docOptNurseService.searchOptNurseByRegOptId(regOptId);
        if (optNurse == null)
        {
            resp.put("resultCode", "40000002");
            resp.put("resultMessage", "手术物品清点记录单不存在");
            return resp.getJsonStr();
        }
        List<DocInstrubillItem> instrubillItem =
            docInstrubillItemService.searchInstrubillItemByRegOptId(regOptId, null);// 器械
        SearchRegOptByIdFormBean searchRegOptByIdFormBean = basRegOptService.searchApplicationById(regOptId);
        
        DocAnaesRecord anaesRecord = docAnaesRecordService.searchAnaesRecordByRegOptId(regOptId);
        String docId = anaesRecord.getAnaRecordId();
        SearchFormBean searchBean = new SearchFormBean();
        searchBean.setDocId(docId);
        
        // 手术名称
        BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
        List<DesignedOptCodes> operDefList = new ArrayList<DesignedOptCodes>();
        String operatorId = "";
        String operatorName = "";
        
        // 全麻
        if (0 == regOpt.getIsLocalAnaes())
        { 
            List<EvtOptRealOper> optRealOperList = evtOptRealOperService.searchOptRealOperList(searchBean);
            for (EvtOptRealOper optRealOper : optRealOperList)
            {
                DesignedOptCodes designedOptCode = new DesignedOptCodes();
                designedOptCode.setOperDefId(optRealOper.getOperDefId());
                designedOptCode.setName(optRealOper.getName());
                designedOptCode.setPinYin(PingYinUtil.getFirstSpell(optRealOper.getName()));
                operDefList.add(designedOptCode);
                
                if (StringUtils.isBlank(operatorId))
                {
                    operatorId = optRealOper.getOperDefId();
                }
                else
                {
                    operatorId = operatorId + "," + optRealOper.getOperDefId();
                }
                
                if (StringUtils.isBlank(operatorName))
                {
                    operatorName = optRealOper.getName();
                }
                else
                {
                    operatorName = operatorName + "," + optRealOper.getName();
                }
            }
        }
        else
        {
            DocOptCareRecord optCareRecord = docOptCareRecordService.selectByRegOptId(regOptId);
            if (null != optCareRecord)
            {
                operDefList = BasRegOptUtils.getOperDefList(optCareRecord.getOperationCode());
                operatorId = optCareRecord.getOperationCode();
                operatorName = optCareRecord.getOperationName();
            }
        }
        optNurse.setOperationNameList(operDefList);
        optNurse.setOperatorId(operatorId);
        optNurse.setOperatorName(operatorName);
        
        //从麻醉记录单中获取到血型
        String bloodType = evtInEventService.getBloodByDocId(docId);
        if (null == optNurse.getBloodType() && StringUtils.isNotBlank(bloodType))
        {
            optNurse.setBloodType(bloodType);
        }
        
        //输液量
        Integer infusion = evtInEventService.getIoeventCountValueByIoDef(docId,null,"1",1);
        if (null == optNurse.getInfusion())
        {
            optNurse.setInfusion(infusion);
        }
        
        //输血总量
        Integer bloodTotal = evtInEventService.getIoeventCountValueByIoDef(docId,null,"2",1);
        //血浆
        Integer plasma = evtInEventService.getIoeventCountValueByName(docId, "血浆", 1);
        if (null == optNurse.getPlasma())
        {
            optNurse.setPlasma(plasma);
        }
        //红细胞
        Integer rbc = evtInEventService.getIoeventCountValueByName(docId, "红细胞", 1);
        if (null == optNurse.getRbc())
        {
            optNurse.setRbc(rbc);
        }
        //输血其他
        Integer bloodOther = null == bloodTotal ? 0 : bloodTotal - (null == plasma ? 0 : plasma) - (null == rbc ? 0 : rbc);
        if (null == optNurse.getBloodOther())
        {
            optNurse.setBloodOther(bloodOther);
        }
        Integer bleeding = evtEgressService.getEgressCountValueByIoDefName("出血量", docId, 1);
        if (null == optNurse.getBleeding())
        {
            optNurse.setBleeding(bleeding); //出血量
        }
        Integer urine = evtEgressService.getEgressCountValueByIoDefName("尿量", docId, 1);
        if (null == optNurse.getUrine())
        {
            optNurse.setUrine(urine);    //尿量
        }
        resp.put("result", "true");
        resp.put("resultCode", "1");
        resp.put("resultMessage", "查询成功!");
        resp.put("optNurseItem", optNurse);
        resp.put("instrubillItem", instrubillItem);
        resp.put("searchRegOptByIdFormBean", searchRegOptByIdFormBean);
        
        logger.info("---------------end searchOptNurseByRegOptId----------------------");
        return resp.getJsonStr();
    }
}
