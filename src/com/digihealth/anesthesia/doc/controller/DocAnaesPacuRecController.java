package com.digihealth.anesthesia.doc.controller;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.basedata.po.BasDeviceConfig;
import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.formbean.AnaesPacuChangeBedFormBean;
import com.digihealth.anesthesia.doc.po.DocAnaesPacuRec;
import com.digihealth.anesthesia.evt.formbean.Filter;
import com.digihealth.anesthesia.evt.po.EvtAnaesEvent;
import com.digihealth.anesthesia.evt.service.EvtAnaesEventService;
import com.digihealth.anesthesia.operProceed.formbean.CmdMsg;
import com.digihealth.anesthesia.sysMng.po.BasDictItem;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/document")
@Api(value="DocAnaesPacuRecController",description="复苏室记录单处理类")
/**
 * 复苏室记录单
 * @author dell
 *
 */
public class DocAnaesPacuRecController extends BaseController {
	
	@RequestMapping(value = "/getAnaesPacuRecCard")
	@ResponseBody
	@ApiOperation(value="查询复苏室记录单列表",httpMethod="POST",notes="查询复苏室记录单列表")
	public String getAnaesPacuRecCard(){
		logger.info("-------------------start getAnaesPacuRecCard---------------------");
		ResponseValue resp = new ResponseValue();
		resp.put("anaesPacuRecCard", docAnaesPacuRecService.getAnaesPacuRecCard());
		resp.setResultCode("1");
		resp.setResultMessage("查询复苏室记录单成功");
		logger.info("-------------------end getAnaesPacuRecCard---------------------");
		return resp.getJsonStr();
	}
	
	@RequestMapping(value = "/searchAnaesPacuRecList")
	@ResponseBody
	@ApiOperation(value="查询复苏室记录单列表",httpMethod="POST",notes="查询复苏室记录单列表")
	public String searchAnaesPacuRecList(@ApiParam(name="searchFormBean", value ="查询参数") @RequestBody SystemSearchFormBean searchFormBean){
		logger.info("-------------------start searchAnaesPacuRecList---------------------");
		ResponseValue resp = new ResponseValue();
		resp.put("anaesPacuRecList", docAnaesPacuRecService.searchAnaesPacuRecList(searchFormBean));
		resp.put("total", docAnaesPacuRecService.searchTotalAnaesPacuRecList(searchFormBean));
		resp.setResultCode("1");
		resp.setResultMessage("查询复苏室记录单列表成功");
		logger.info("-------------------end searchAnaesPacuRecList---------------------");
		return resp.getJsonStr();
	}

	/**
	 * 获取设备列表
	 * @return
	 */
	@RequestMapping("/getPacuDeviceByType")
	@ResponseBody
	@ApiOperation(value="获取设备列表",httpMethod="POST",notes="获取设备列表")
	public String getPacuDeviceByType(@ApiParam(name="json", value ="查询参数") @RequestBody JSONObject json) {
		logger.info("----------------start getPacuDeviceByType------------------------");
		ResponseValue resp = new ResponseValue();
		String bedId = json.getString("bedId");
        String beid = (String) json.get("beid");
        if (StringUtils.isBlank(beid)) {
            beid = getBeid();
        }
		List<BasDeviceConfig> devicesList = docAnaesPacuRecService.getPacuDeviceByType(bedId, beid);
		resp.put("resultList", devicesList);
		resp.setResultCode("1");
		resp.setResultMessage("操作成功");
		logger.info("------------------end getPacuDeviceByType------------------------");
		return resp.getJsonStr();
	}
	
	/**
	 * 获取床位上的已选择的设备列表
	 * @param bedId
	 * @return
	 */
	@RequestMapping("/getPacuDeviceConfigList")
	@ResponseBody
	@ApiOperation(value="获取床位上的已选择的设备列表",httpMethod="POST",notes="获取床位上的已选择的设备列表")
	public String getPacuDeviceConfigList(@ApiParam(name="json", value ="查询参数") @RequestBody JSONObject json) {
		logger.info("----------------start getPacuDeviceConfigList------------------------");
		ResponseValue resp = new ResponseValue();
		String bedId = json.getString("bedId");
		String beid = (String) json.get("beid");
		if (StringUtils.isBlank(beid)) {
			beid = getBeid();
		}
		List<BasDeviceConfig> pacuDeviceList = docAnaesPacuRecService.getPacuDeviceConfigList(bedId, beid);
		resp.put("resultList", pacuDeviceList);
		resp.setResultCode("1");
		resp.setResultMessage("操作成功");
		logger.info("------------------end getPacuDeviceConfigList------------------------");
		return resp.getJsonStr();
	}
	
	@RequestMapping("/initAnaesPacu")
	@ResponseBody
	@ApiOperation(value="initAnaesPacu",httpMethod="POST",notes="initAnaesPacu")
	public String initAnaesPacu(@ApiParam(name="msg", value ="查询参数") @RequestBody CmdMsg msg){
		ResponseValue resp = new ResponseValue();
		
		return resp.getJsonStr();
	}
	
	@RequestMapping(value = "/saveAnaesPacuRec")
	@ResponseBody
	@ApiOperation(value="保存复苏室记录单",httpMethod="POST",notes="保存复苏室记录单")
	public String saveAnaesPacuRec(@ApiParam(name="record", value ="查询参数") @RequestBody DocAnaesPacuRec record){
		logger.info("-------------------start saveAnaesPacuRec---------------------");
		ResponseValue resp = new ResponseValue();
		docAnaesPacuRecService.saveAnaesPacuRec(record,resp); 
		logger.info("-------------------end saveAnaesPacuRec---------------------");
		return resp.getJsonStr();
	}
	
	
	@RequestMapping(value = "/changeBedByPacuRec")
	@ResponseBody
	@ApiOperation(value="复苏室更换床位",httpMethod="POST",notes="复苏室更换床位")
	public String changeBedByPacuRec(@ApiParam(name="record", value ="查询参数") @RequestBody AnaesPacuChangeBedFormBean record){
		logger.info("-------------------start saveAnaesPacuRec---------------------");
		ResponseValue resp = new ResponseValue();
		docAnaesPacuRecService.changeBedByPacuRec(record,resp);
		resp.setResultCode("1");
		resp.setResultMessage("保存复苏室记录单成功");
		logger.info("-------------------end saveAnaesPacuRec---------------------");
		return resp.getJsonStr();
	}
	
	
	
	@RequestMapping(value = "/updatePacuRecEnterRoomTime")
    @ResponseBody
    @ApiOperation(value="保存复苏室记录单",httpMethod="POST",notes="保存复苏室记录单")
    public String updatePacuRecEnterRoomTime(@ApiParam(name="record", value ="查询参数") @RequestBody DocAnaesPacuRec record){
        logger.info("-------------------start updatePacuRecEnterRoomTime---------------------");
        ResponseValue resp = new ResponseValue();
        docAnaesPacuRecService.updatePacuRecEnterRoomTime(record.getEnterTime(), record.getId());
        logger.info("-------------------end updatePacuRecEnterRoomTime---------------------");
        return resp.getJsonStr();
    }
	
	@RequestMapping(value = "/selectByPacuRecId")
    @ResponseBody
    @ApiOperation(value="根据id查询复苏记录单",httpMethod="POST",notes="根据id查询复苏记录单")
    public String selectByPacuRecId(@ApiParam(name="searchFormBean", value ="查询参数") @RequestBody SystemSearchFormBean searchFormBean) {
        logger.info("begin selectByPacuRecId");
        ResponseValue resp = new ResponseValue();
        
        //复苏记录单数据
        DocAnaesPacuRec anaesPacuRec = new DocAnaesPacuRec(); 
        
        String regOptId = ""; 
        Date date = new Date();
        if (searchFormBean != null) {
            List<Filter> fs = searchFormBean.getFilters();
            for (Filter filter : fs) {
                String field = filter.getField();
                String value = filter.getValue();
                if ("regOptId".equals(field)) {
                    regOptId = value;
                }
                if ("inTime".equals(field) && StringUtils.isNotBlank(value))
                {
                    date = new Date(Long.valueOf(value));
                }
            }
        }
        if(org.apache.commons.lang3.StringUtils.isNotBlank(regOptId)){
          anaesPacuRec = docAnaesPacuRecService.getAnaesPacuRecByRegOptId(regOptId);
        }
        
        if(anaesPacuRec == null) {
            resp.setResultCode("1000000000");
            resp.setResultMessage("复苏室记录单不存在");
            return resp.getJsonStr();
        }
        anaesPacuRec.setPortablePipeList(StringUtils.getListByString(anaesPacuRec.getPortablePipe()));
        anaesPacuRec.setPortableResList(StringUtils.getListByString(anaesPacuRec.getPortableRes()));
        
        /*BasRegOpt regOpt = basRegOptService.searchRegOptById(anaesPacuRec.getRegOptId());
        String state = regOpt.getState();
        
        String pacurecid = anaesPacuRec.getId();
        
        String pacuNumber = "";
        //状态为05表示患者进入了复苏室，此时需要采集数据
        if ("05".equals(state) && StringUtils.isNotBlank(anaesPacuRec.getBedId())) {
            if (null == anaesPacuRec.getEnterTime()) {
                
                docAnaesPacuRecService.updatePacuRecEnterRoomTime(date, pacurecid); // 首次进入pacu存入时间
                anaesPacuRec.setEnterTime(date);
                
                //添加入室事件
                EvtAnaesEvent anaesEventPacu = new EvtAnaesEvent();
                anaesEventPacu.setCode(EvtAnaesEventService.IN_ROOM);
                anaesEventPacu.setOccurTime(date);
                anaesEventPacu.setDocId(anaesPacuRec.getId());
                anaesEventPacu.setDocType(2);
                evtAnaesEventService.insertPacuAnaesevent(anaesEventPacu);
                
                //第一次进入，取数据字典表中的编号，并且将数据字典表中的编号值加1
                List<BasDictItem> dictList = basDictItemService.getListByGroupId("pacu_number", null, getBeid());
                if (null != dictList && dictList.size() > 0)
                {
                    BasDictItem dict = dictList.get(0);
                    pacuNumber = dict.getCodeValue();
                    anaesPacuRec.setPacuNumber(pacuNumber);
                    //防止编号以0000格式开头，加1后在前面补零
                    int size = pacuNumber.length();
                    long l = Long.parseLong(pacuNumber);
                    l = l + 1;
                    String s = StringUtils.leftPad(String.valueOf(l), size, "0");
                    dict.setCodeValue(s);
                    basDictItemService.upBasDictItem(dict);
                }
            }
        }*/
                
        resp.put("anaesPacuRec", anaesPacuRec);
        //人员基础信息
        resp.put("regOpt", basRegOptService.getPostopOptInfo(anaesPacuRec.getRegOptId()) );
        logger.info("end selectByPacuRecId");
        return resp.getJsonStr();
    }

    @RequestMapping(value = "/hasAnaesPacuRec")
    @ResponseBody
    @ApiOperation(value="根据手术id查询复苏记录单是否存在",httpMethod="POST",notes="根据手术id查询复苏记录单是否存在")
    public String hasAnaesPacuRec(@ApiParam(name="docAnaesPacuRec", value ="查询参数") @RequestBody DocAnaesPacuRec docAnaesPacuRec) {
        logger.info("begin hasAnaesPacuRec");
        ResponseValue resp = new ResponseValue();
        boolean flag = docAnaesPacuRecService.hasAnaesPacuByRegOptId(docAnaesPacuRec.getRegOptId());
        resp.put("flag", flag);
        logger.info("end hasAnaesPacuRec");
        return resp.getJsonStr();
    }
}
