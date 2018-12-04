package com.digihealth.anesthesia.interfacedata.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.common.config.Global;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.utils.Exceptions;
import com.digihealth.anesthesia.common.utils.ModifyBillingCheckUtil;
import com.digihealth.anesthesia.common.utils.SpringContextHolder;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.interfacedata.formbean.hnhtyy.CostHNHTYYRow;
import com.digihealth.anesthesia.interfacedata.formbean.hnhtyy.HisOptcostHNHTYYFormBean;
import com.digihealth.anesthesia.interfacedata.service.HisInterfaceServiceHNHTYY;

@Controller
@RequestMapping(value = "/interfacedata")
public class HisInterfaceController extends BaseController
{
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());
    
   
    /**
     * 湖南航天中心医院计费接口
     * @param map
     * @return
     */
    @RequestMapping("/sendOperCostDataToHisHNHTYY")
	@ResponseBody
	public String sendOperCostDataToHisHNHTYY(@RequestBody Map map)
	{
		logger.info("sendOperCostDataToHisHNHTYY======费用开始同步!!!");
		ResponseValue resp = new ResponseValue();
		String regOptId = map.get("regOptId").toString();
		String costType = map.get("costType").toString(); //收费类型 护士收费 麻醉医生收费
		
		HisOptcostHNHTYYFormBean formbean = new HisOptcostHNHTYYFormBean();
		List<CostHNHTYYRow>  costList = new ArrayList<CostHNHTYYRow>();
		
		logger.info("sendOperCostDataToHis==="+regOptId+"===术中收费项目开始同步!!!");
		
		List<CostHNHTYYRow> operCost = docPackagesItemService.queryHNHTYYUnCostListByRegOptId(regOptId,costType);
		List<CostHNHTYYRow> operMedCost = docEventBillingService.queryHNHTYYUnCostListByRegOptId(regOptId,costType);
		if(null!=operCost && operCost.size()>0){
			costList.addAll(operCost);
		}
		if(null!=operMedCost && operMedCost.size()>0){
			costList.addAll(operMedCost);
		}
		formbean.setRows(costList);
		if(null == costList || costList.size()<1){
			resp.setResultCode("40000001");
			resp.setResultMessage("没有可以同步到his的收费数据!");
			return resp.getJsonStr();
		}
		
		BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
		if(null != regOpt && "03".equals(regOpt.getState())){
			resp.setResultCode("40000001");
			resp.setResultMessage("手术状态为术前时，不允许同步计费数据到his系统!");
			return resp.getJsonStr();
		}
		
		Boolean modFlag = ModifyBillingCheckUtil.checkModifyBill(regOptId,costType,resp,false);
		if(!modFlag){
			return resp.getJsonStr();
		}
		
		//是否调用his接口
		String isConnectionFlag = Global.getConfig("isConnectionHis").trim();
		if((null != operCost && operCost.size()>0) || (null != operMedCost && operMedCost.size()>0)){
			logger.info("sendOperCostDataToHis===患者姓名为："+regOpt.getName()+"===costType:"+costType+"的费用开始同步!!!");
			try {
				if(StringUtils.isEmpty(isConnectionFlag) || "true".equals(isConnectionFlag)){
					//修改收费标志为结算中
					basRegOptService.updatePayState(regOptId,costType,new Integer(9));
					HisInterfaceServiceHNHTYY hisInterfaceService = SpringContextHolder.getBean(HisInterfaceServiceHNHTYY.class);
					hisInterfaceService.sendOptCostInteface(formbean, regOptId ,costType,resp,docPackagesItemService.getUserName());
					
					
					if(resp.getResultCode().equals("1") && resp.getResultObj().get("failList")!=null && resp.getResultObj().get("failList").toString().length()>0){
						resp.setResultCode("50000000001");
						resp.setResultMessage("收费列表中存在计费错误的数据："+resp.getResultObj().get("failList").toString());
					}
					
				}
			} catch (Exception e) {
				resp.setResultCode("1000000000");
				resp.setResultMessage("单项收费项目费用同步异常！err:"+Exceptions.getStackTraceAsString(e));
				logger.error("sendOperCostDataToHis===单项收费项目费用同步异常:"+Exceptions.getStackTraceAsString(e));
				basRegOptService.updatePayState(regOptId,costType,new Integer(0));
				return resp.getJsonStr();
			}
		}
		logger.info("sendOperCostDataToHisHNHTYY======费用同步完成!!!");
		return resp.getJsonStr();
	}
    
    /**
     * 调用his接口获取检验数据
     * @param map
     * @return
     */
    @RequestMapping("/synLisDataList")
 	@ResponseBody
 	public String synLisDataList(@RequestBody Map map)
 	{
 		logger.info("synLisDataList======检验数据同步开始!!!");
 		ResponseValue resp = new ResponseValue();
 		String regOptId = map.get("regOptId").toString();
 		
 		HisInterfaceServiceHNHTYY hisInterfaceService = SpringContextHolder.getBean(HisInterfaceServiceHNHTYY.class);
 		
 		if(StringUtils.isNotBlank(regOptId)){
 			hisInterfaceService.synLisDataList(regOptId);
 		}
 		
 		logger.info("synLisDataList======检验数据同步完成!!!");
 		return resp.getJsonStr();
 	}
    
    
    
    /**
     * 调用his接口获取检查数据
     * @param map
     * @return
     */
    @RequestMapping("/synChecksDataList")
 	@ResponseBody
 	public String synChecksDataList(@RequestBody Map map)
 	{
 		logger.info("synChecksDataList======检查数据同步开始!!!");
 		ResponseValue resp = new ResponseValue();
 		String regOptId = map.get("regOptId").toString();
 		
 		HisInterfaceServiceHNHTYY hisInterfaceService = SpringContextHolder.getBean(HisInterfaceServiceHNHTYY.class);
 		
 		if(StringUtils.isNotBlank(regOptId)){
 			hisInterfaceService.synChecksDataList(regOptId);
 		}
 		
 		logger.info("synChecksDataList======检查数据同步完成!!!");
 		return resp.getJsonStr();
 	}
    
}
