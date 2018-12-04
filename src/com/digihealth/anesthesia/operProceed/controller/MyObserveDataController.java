package com.digihealth.anesthesia.operProceed.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.json.JSONObject;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.config.OperationState;
import com.digihealth.anesthesia.basedata.formbean.BaseInfoQuery;
import com.digihealth.anesthesia.basedata.formbean.SysCodeFormbean;
import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.basedata.po.BasDeviceConfig;
import com.digihealth.anesthesia.basedata.po.BasMonitorConfigFreq;
import com.digihealth.anesthesia.basedata.po.BasOperroom;
import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.basedata.po.Device;
import com.digihealth.anesthesia.common.config.Global;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.utils.DateUtils;
import com.digihealth.anesthesia.common.utils.Exceptions;
import com.digihealth.anesthesia.common.utils.JsonType;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.common.utils.SysUtil;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.po.DocAccede;
import com.digihealth.anesthesia.doc.po.DocAnaesRecord;
import com.digihealth.anesthesia.doc.po.DocPreOperVisit;
import com.digihealth.anesthesia.doc.po.DocPreVisit;
import com.digihealth.anesthesia.evt.formbean.SearchFormBean;
import com.digihealth.anesthesia.evt.formbean.SearchOperaPatrolRecordFormBean;
import com.digihealth.anesthesia.evt.formbean.SearchOptOperIoevent;
import com.digihealth.anesthesia.evt.formbean.SearchRegOptByRoomIdAndOperDateAndStateFormBean;
import com.digihealth.anesthesia.evt.po.EvtCtlBreath;
import com.digihealth.anesthesia.operProceed.core.MyConstants;
import com.digihealth.anesthesia.operProceed.formbean.BasAnaesMonitorConfigFormBean;
import com.digihealth.anesthesia.operProceed.formbean.CentralBigScreenDataFormbean;
import com.digihealth.anesthesia.operProceed.formbean.CmdMsg;
import com.digihealth.anesthesia.operProceed.formbean.CtlBreathDateFormBean;
import com.digihealth.anesthesia.operProceed.formbean.DeviceConfigFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonitorData;
import com.digihealth.anesthesia.operProceed.formbean.MonitorDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonitorDataPage;
import com.digihealth.anesthesia.operProceed.formbean.MonitorDisplayChangeFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonitorFreqFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonitorPupilFormBean;
import com.digihealth.anesthesia.operProceed.formbean.RealTimeDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.RegOptFormBean;
import com.digihealth.anesthesia.operProceed.formbean.RescueeventFormBean;
import com.digihealth.anesthesia.operProceed.formbean.SeriesData;
import com.digihealth.anesthesia.operProceed.formbean.SeriesDataObj;
import com.digihealth.anesthesia.operProceed.formbean.SuspendFormBean;
import com.digihealth.anesthesia.operProceed.formbean.UpdAnaesMonitorConfigFormbean;
import com.digihealth.anesthesia.operProceed.formbean.XAxisData1;
import com.digihealth.anesthesia.operProceed.formbean.XAxisDataBean;
import com.digihealth.anesthesia.operProceed.formbean.YAxisData;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfig;
import com.digihealth.anesthesia.operProceed.po.BasMonitorDisplay;
import com.digihealth.anesthesia.operProceed.po.BasMonitorFreqChange;
import com.digihealth.anesthesia.operProceed.po.BasMonitorPupil;
import com.digihealth.anesthesia.sysMng.po.BasDictItem;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@org.springframework.stereotype.Controller
@RequestMapping("/operCtl")
@Api(value = "MyObserveDataController", description = "术中总控处理类")
public class MyObserveDataController extends BaseController {

	/**
	 * 新增点,无需发送采集数据模块，新增显示表，b_observe_data表需要先根据time+docId+observeId查询是否有值，如果有记录
	 * ，则修改，无记录，则新增；
	 * 
	 * @return
	 */
	@RequestMapping("/addobsdat")
	@ResponseBody
	@ApiOperation(value = "添加点", httpMethod = "POST", notes = "添加点")
	public String addobsdat(@ApiParam(name = "params", value = "参数") @RequestBody BasMonitorDisplay monitorDisp) {
		logger.info("----------------start addobsdat------------------------");
		ResponseValue res = new ResponseValue();
		basMonitorDisplayService.addobsdat(monitorDisp);
		res.setResultCode("1");
		res.setResultMessage("操作成功");
		logger.info("------------------end addobsdat------------------------");
		return res.getJsonStr();
	}

	/**
     * 修改点，无需发送采集数据模块，修改显示表，历史表，并处理b_observe_data表
     * （b_observe_data表需要先根据time+docId+observeId查询是否有值，如果有记录，则修改，无记录，则新增）
     * 
     * @return
     */
    @RequestMapping("/updobsdat")
    @ResponseBody
    @ApiOperation(value = "修改点", httpMethod = "POST", notes = "修改点")
    public String updobsdat(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDisplayChangeFormBean changeBean) {
        logger.info("----------------start updobsdat------------------------");
        ResponseValue res = new ResponseValue();
        basMonitorDisplayService.changeMonitDisplay(changeBean);
        res.setResultCode("1");
        res.setResultMessage("操作成功");
        logger.info("------------------end updobsdat------------------------");
        return res.getJsonStr();
    }

	/**
	 * 批量新增or修改点
	 * 
	 * @param monitors
	 * @return
	 */
	@RequestMapping("/batchHandleObsDat")
	@ResponseBody
	@ApiOperation(value = "批量修改点", httpMethod = "POST", notes = "批量修改点")
	public String batchHandleObsDat(@ApiParam(name = "params", value = "参数") @RequestBody List<MonitorDisplayChangeFormBean> monitors) {
		logger.info("----------------start batchHandleObsDat------------------------");
		ResponseValue res = new ResponseValue();
		if (null != monitors && monitors.size() > 0) {
			basMonitorDisplayService.batchHandleObsDat(monitors);
		} else {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
			logger.info("------------------end batchHandleObsDat------------------------");
			return res.getJsonStr();
		}
		res.setResultCode("1");
		res.setResultMessage("操作成功");
		logger.info("------------------end batchHandleObsDat------------------------");
		return res.getJsonStr();
	}

	/**
	 * 修改颜色、图标、max、min，无需发送采集数据模块, ---
	 * 页面只传递已勾选的AnaesMonitorConfig列表，存入b_anaes_monitor_config
	 * 
	 * @return
	 *//*
	@RequestMapping("/updmonitorDisp")
	@ResponseBody
	@ApiOperation(value = "修改颜色、图标、max、min", httpMethod = "POST", notes = "修改颜色、图标、max、min")
	public String updmonitorDisp(@ApiParam(name = "params", value = "参数") @RequestBody List<BasAnaesMonitorConfig> anaesMonitorConfigList) {
		logger.info("----------------start updmonitorDisp------------------------");
		ResponseValue res = new ResponseValue();
		basAnaesMonitorConfigService.saveAnaesMonitorConfig(anaesMonitorConfigList);
		res.setResultCode("1");
		res.setResultMessage("修改成功！");
		logger.info("------------------end updmonitorDisp------------------------");
		return res.getJsonStr();
	}*/

	/**
	 * 查询麻醉记录单
	 * 
	 * @param regOptId
	 * @return
	 */
	@RequestMapping("/getAnaesRecord")
	@ResponseBody
	@ApiOperation(value = "查询麻醉记录单", httpMethod = "POST", notes = "查询麻醉记录单")
	public String getAnaesRecord(@ApiParam(name = "regOptId", value = "手术信息id") @RequestBody String regOptId) {
		logger.info("----------------start getAnaesRecord------------------------");
		ResponseValue res = new ResponseValue();
		DocAnaesRecord anaesRecord = docAnaesRecordService.getAnaesRecord(regOptId);
		res.put("anaesRecord", anaesRecord);
		res.setResultCode("1");
		res.setResultMessage("获取麻醉记录单成功！");
		logger.info("------------------end getAnaesRecord------------------------");
		return res.getJsonStr();
	}

	/**
	 * 获取实时数据和获取设备状态，无需发送采集数据模块； 如果手脱了，数据还是原来的数据，比对5s，如果超过5s，则不传递数据给前端；
	 * 
	 * @return
	 */
	@RequestMapping("/getrtData")
	@ResponseBody
	@ApiOperation(value = "获取实时数据和获取设备状态", httpMethod = "POST", notes = "获取实时数据和获取设备状态")
	public String getrtData(@ApiParam(name = "params", value = "参数") @RequestBody BaseInfoQuery baseQuery) {
		logger.info("----------------start getrtData------------------------");
		ResponseValue res = new ResponseValue();
		if (null != baseQuery) {
			// List<RealTimeDataFormBean> monitorList =
			// basMonitorDisplayService.searchObserveDataByPosition(baseQuery);
		    if (StringUtils.isBlank(baseQuery.getBeid()))
		    {
		        baseQuery.setBeid(getBeid());
		    }
		    
			Map<String, RealTimeDataFormBean> resultMap = basMonitorDisplayService.searchObserveMapByPosition(baseQuery);
			res.put("monitorList", resultMap);
			// 获取设备列表
			List<Device> devices = basDeviceService.searchDeviceListByRoomId(basDeviceService.getCurRoomId(null), baseQuery.getBeid());
			res.put("devices", devices);

			res.setResultCode("1");
			res.setResultMessage("操作成功");
		}
		logger.info("------------------end getrtData------------------------");
		return res.getJsonStr();
	}

	/**
	 * 新增、修改、停用设备、采样频率，判断是否需要发送采集数据模块 修改设备配置
	 * 
	 * @return
	 */
	@RequestMapping("/updDeviceConfig")
	@ResponseBody
	@ApiOperation(value = "修改设备配置", httpMethod = "POST", notes = "修改设备配置")
	public String updDeviceConfig(@ApiParam(name = "params", value = "参数") @RequestBody DeviceConfigFormBean deviceConfigFormBean) {
		logger.info("----------------start updDeviceConfig------------------------");
		ResponseValue res = new ResponseValue();
		if (deviceConfigFormBean != null) {
		    BasDeviceConfig deviceConfig = deviceConfigFormBean.getDeviceConfig();
		    String roomId = deviceConfig.getRoomId();
			List<BasDeviceConfig> enableDeviceConfigList = basDeviceConfigService.getEnableDeviceConfigList(roomId);
			if (null != enableDeviceConfigList && enableDeviceConfigList.size() > 0) {
				if (null != deviceConfig && (null != deviceConfig.getEnable() && 1 == deviceConfig.getEnable())) {
					for (BasDeviceConfig enableDevConf : enableDeviceConfigList) {
						if (!enableDevConf.getDeviceId().equals(deviceConfig.getDeviceId()) && enableDevConf.getDeviceType().equals(deviceConfig.getDeviceType())) {
							res.setResultCode("70000000");
							res.setResultMessage("已有相同类型的设备启用！");
							return res.getJsonStr();
						}
					}
				}
			}

			basMonitorDisplayService.updDeviceConfig(deviceConfigFormBean);
			// 根据房间和术中状态查询手术
			/*BasRegOpt regOpt = basRegOptService.queryRegOptByState("04");
			if (null != regOpt) {
				// 发送修改设备配置的命令给数据处理模块
				CmdMsg msg = new CmdMsg();
				msg.setMsgType(MyConstants.OPERATION_STATUS_CHECK);
				msg.setRegOptId(regOpt.getRegOptId());
				res = MessageProcess.process(msg);
			}*/
		} else {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
		}

		logger.info("------------------end updDeviceConfig------------------------");
		return res.getJsonStr();
	}

	/**
	 * 修改采集频率，获取当前频率
	 * 
	 * @param freqList
	 * @return
	 */
	@RequestMapping("/updFreq")
	@ResponseBody
	@ApiOperation(value = "修改采集频率", httpMethod = "POST", notes = "修改采集频率")
	public String updFreq(@ApiParam(name = "params", value = "参数") @RequestBody MonitorFreqFormBean formBean) {
		logger.info("----------------start updFreq------------------------");
		ResponseValue res = new ResponseValue("1");
		String regOptId = formBean.getRegOptId();
		List<BasMonitorConfigFreq> freqList = formBean.getFreqList();
		Date time = formBean.getTime();
		String currentModel = basRegOptService.getCurrentModel(regOptId);
		BasMonitorConfigFreq monitorFreq = basMonitorConfigFreqService.searchMonitorFreqByType(currentModel,regOptId);
		//String f = "";
		//Integer freq = 0;
		String model = "";
		if (monitorFreq != null) {
			//f = monitorFreq.getValue();
			//freq = Integer.valueOf(f);
			model = monitorFreq.getType();
		}
		// monitorConfigFreqService.updateFreq(freqList); //修改采集频率
		basMonitorDisplayService.updateFreq(freqList, model, time, regOptId);

		/*if (null != freqList && freqList.size() > 0) {
			for (BasMonitorConfigFreq monitorConfigFreq : freqList) {
				String type = monitorConfigFreq.getType();
				if (type.equals(model)) {// 传递过来的数据有当前模式下的频率 当前模式=传递过来的模式
					String value = monitorConfigFreq.getValue();
					Integer curFreq = Integer.valueOf(value);
					if (curFreq != freq) { // 频率不相等，则发送修改频率的命令到数据处理模块，只有普通模式的时候，频率才会不相等
						CmdMsg msg = new CmdMsg();
						msg.setMsgType(MyConstants.OPERATION_UPDATE_FREQ);
						msg.setRegOptId(regOptId);
						res = MessageProcess.process(msg);
						break;
					}
				}
			}
		}*/
		logger.info("------------------end updFreq------------------------");

		return res.getJsonStr();
	}

	/**
	 * 在手术初始化后，查询设备状态
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping("/getDeviceStatus")
	@ResponseBody
	@ApiOperation(value = "手术初始化查询设备状态", httpMethod = "POST", notes = "手术初始化查询设备状态")
	public String getDeviceStatus() {
		logger.info("----------------start getDeviceStatus------------------------");
		ResponseValue res = new ResponseValue();
		List<Device> devices = basDeviceService.searchDeviceListByRoomId(basDeviceService.getCurRoomId(null), null);
		res.put("devices", devices);
		res.setResultCode("1");
		res.setResultMessage("获取设备列表成功！");
		logger.info("------------------end getDeviceStatus------------------------");
		return res.getJsonStr();
	}

	/**
	 * 切换模式（普通模式/抢救模式），发送采集数据模块
	 * 
	 * 1、传递切换频率的时间,修改最近点的interval_time 2、存入抢救事件
	 * 
	 * @return
	 */
	@RequestMapping("/changeModel")
	@ResponseBody
	@ApiOperation(value = "切换模式", httpMethod = "POST", notes = "切换模式")
	public String changeModel(@ApiParam(name = "params", value = "参数") @RequestBody RescueeventFormBean rescueeventFormBean) {
		logger.info("----------------start changeModel------------------------");
		ResponseValue res = new ResponseValue();
		//EvtRescueevent rescueevent = rescueeventFormBean.getRescueevent();
		basMonitorDisplayService.changeModel(rescueeventFormBean);
		/*CmdMsg msg = new CmdMsg();
		msg.setMsgType(rescueevent.getModel());
		String regOptId = rescueeventFormBean.getRegOptId();
		msg.setRegOptId(regOptId);
		// 发送模式切换的命令
		res = MessageProcess.process(msg);*/
		logger.info("------------------end changeModel------------------------");
		return res.getJsonStr();
	}

	/**
	 * 查看非急诊的手术，是否相关文书已经完成
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping("/searchDocIsFinished")
	@ResponseBody
	@ApiOperation(value = "查看非急诊的手术，是否相关文书已经完成", httpMethod = "POST", notes = "查看非急诊的手术，是否相关文书已经完成")
	public String searchDocIsFinished(@ApiParam(name = "params", value = "参数") @RequestBody JSONObject json) {
		logger.info("-----------------start  searchDocIsFinished------------------------");
		ResponseValue res = new ResponseValue();
		String regOptId = json.get("regOptId").toString();
		if (StringUtils.isBlank(regOptId)) {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
			logger.info("-----------------end  searchDocIsFinished------------------------");
			return res.getJsonStr();
		}
		if (null != regOptId) {
			BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
			DocPreVisit preVisit = docPreVisitService.searchPreVisitByRegOptId(regOptId).getPreVisit();
			DocPreOperVisit preOperVisit = docPreOperVisitService.searchPreOperVisit(regOptId);
			DocAccede accede = docAccedeService.searchAccedeByRegOptId(regOptId);

			if (null == regOpt) {
				res.setResultCode("10000000");
				res.setResultMessage("未查询到regOptId=" + regOptId + "的记录！");
			} else {
				Integer emergency = regOpt.getEmergency();
				if (1 == emergency) {
					res.setResultCode("10000000");
					res.setResultMessage("手术regOptId=" + regOptId + "是急诊手术,无需检查！");
				} else {
					if ((null != preVisit || null != preOperVisit) && null != accede) {
					    String pv_processState = "";
					    if (null != preVisit)
					    {
					        pv_processState = preVisit.getProcessState();
					    }
					    if (null != preOperVisit)
					    {
					        pv_processState = preOperVisit.getProcessState();
					    }
						String accede_processState = accede.getProcessState();
						if (0 == regOpt.getIsLocalAnaes()) {
							if ("END".equals(pv_processState) && "END".equals(accede_processState)) {
								res.setResultCode("1");
								res.setResultMessage("术前访视单和麻醉同意书都已经完成！");
							} else if (!"END".equals(pv_processState)) {
								res.setResultCode("10000000");
								res.setResultMessage("术前访视单未完成！");
							} else if (!"END".equals(accede_processState)) {
								res.setResultCode("10000000");
								res.setResultMessage("麻醉同意书未完成！");
							}
						}

					} else {
						res.setResultCode("10000000");
						res.setResultMessage("系统错误，请联系管理员！");
					}
				}
			}
		} else {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
		}

		logger.info("-----------------end  searchDocIsFinished------------------------");
		return res.getJsonStr();
	}

	/**
	 * 手术初始化
	 * 
	 * @param msg
	 * @return
	 *//*
	@RequestMapping("/initOper")
	@ResponseBody
	@ApiOperation(value = "手术初始化", httpMethod = "POST", notes = "手术初始化")
	public String initOper(@ApiParam(name = "params", value = "参数") @RequestBody CmdMsg msg) {
		logger.info("----------------start initOper------------------------");
		ResponseValue res = new ResponseValue();
		if (null == msg) {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
			logger.info("----------------end initOper------------------------");
			return res.getJsonStr();
		} else {
			if (StringUtils.isEmpty(msg.getMsgType())) {
				res.setResultCode("70000000");
				res.setResultMessage(Global.getRetMsg(res.getResultCode()));
				logger.info("----------------end initOper------------------------");
				return res.getJsonStr();
			} else {
				res = MessageProcess.process(msg);
			}
		}

		logger.info("------------------end initOper------------------------");
		return res.getJsonStr();
	}*/

	/**
	 * 查询手术室是否有正在进行的手术
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/queryRoomOper")
	@ResponseBody
	@ApiOperation(value = "查询手术室是否有正在进行的手术", httpMethod = "POST", notes = "查询手术室是否有正在进行的手术")
	public String queryRoomOper(@ApiParam(name = "params", value = "参数") @RequestBody SearchFormBean searchBean) {
		logger.info("----------------begin queryRoomOper------------------------");
		Map result = new HashMap();
		BaseInfoQuery baseQuery = new BaseInfoQuery();
		//baseQuery.setOperRoomId(Global.getConfig("roomId").toString());
		baseQuery.setState(OperationState.SURGERY);
		// 判断当前手术室是否存在未完成的手术，如果存在则返回错误消息
		List<SearchOperaPatrolRecordFormBean> operaPatrolList = basRegOptService.getOperaPatrolRecordListByRoomId(baseQuery);
		if (operaPatrolList.size() > 0) {
			SearchOperaPatrolRecordFormBean po = operaPatrolList.get(0);
			if (!po.getRegOptId().equals(searchBean.getRegOptId())) {
				result.put("resultCode", "40000004");
				String str = po.getOperRoomName() + "存在未完成的全麻手术，患者为" + po.getName()+"，  麻醉医生为"+po.getAnaesDocName();
				if ("1".equals(po.getIsLocalAnaes()))
				{
				    str = po.getOperRoomName() + "存在未完成的局麻手术，患者为" + po.getName()+"，  巡回护士为"+po.getCircunurseName();
				}
				result.put("resultMessage", str);
				result.put("id", po.getRegOptId());
				logger.info("------------------end queryRoomOper------------------------");
				return JsonType.jsonType(result);
			} else {
				result.put("resultCode", "1");
				result.put("resultMessage", "当前手术正在术中!");
				logger.info("------------------end queryRoomOper------------------------");
				return JsonType.jsonType(result);
			}
		} else {
			result.put("resultCode", "1");
			result.put("resultMessage", "当前手术室暂无正在进行的手术!");
			logger.info("------------------end queryRoomOper------------------------");
			return JsonType.jsonType(result);
		}
	}

	/**
	 * 强制结束手术，发送采集数据模块，并发送采集服务
	 * 
	 * @return
	 */
	@RequestMapping("/forceEndOper")
	@ResponseBody
	@ApiOperation(value = "强制结束手术", httpMethod = "POST", notes = "强制结束手术")
	public String forceEndOper(@ApiParam(name = "params", value = "参数") @RequestBody CmdMsg msg) {
		logger.info("----------------start forceEndOper------------------------");
		ResponseValue res = new ResponseValue();
		BaseInfoQuery baseQuery = new BaseInfoQuery();
		//baseQuery.setOperRoomId(Global.getConfig("roomId").toString());
		baseQuery.setState(OperationState.SURGERY);
		// 判断当前手术室是否存在未完成的手术，如果存在则返回错误消息
		List<SearchOperaPatrolRecordFormBean> operaPatrolList = basRegOptService.getOperaPatrolRecordListByRoomId(baseQuery);
		List<String> regOptIds = new ArrayList<String>();
		if (operaPatrolList.size() > 0) {
			for (int i = 0; i < operaPatrolList.size(); i++) {
				String regOptId = operaPatrolList.get(i).getRegOptId();
				regOptIds.add(regOptId);
			}
			if (null != regOptIds && regOptIds.size() > 0) {
				basRegOptService.forceEndOperation(regOptIds);
			}
			
			logger.info("------------------forceEndOperation----end--------------------");
			
			/*// 发送结束手术的命令
			if (StringUtils.isNotBlank(msg.getMsgType())) {
				if (StringUtils.isNotBlank(msg.getRegOptId())) {
					res = MessageProcess.process(msg);
				}
			}*/
		}
		logger.info("------------------end forceEndOper------------------------");
		return res.getJsonStr();
	}

	/**
	 * 终止手术 觉得手术不宜做，直接终止手术
	 * 
	 * @return
	 */
	@RequestMapping("/suspendOperation")
	@ResponseBody
	@ApiOperation(value = "终止手术", httpMethod = "POST", notes = "终止手术")
	public String suspendOperation(@ApiParam(name = "params", value = "参数") @RequestBody SuspendFormBean bean) {
		logger.info("----------------start suspendOperation------------------------");
		ResponseValue res = new ResponseValue();
		basRegOptService.suspendOperation(bean);
		/*// 发送结束手术的命令
		if (StringUtils.isNotBlank(bean.getMsgType())) {
			CmdMsg msg = new CmdMsg();
			msg.setMsgType(bean.getMsgType());
			msg.setRegOptId(bean.getRegOptId());
			res = MessageProcess.process(msg);
		}*/

		logger.info("----------------end suspendOperation------------------------");
		return res.getJsonStr();
	}

	
	/**
	 * 单独停采集服务
	 * @return
	 *//*
	@RequestMapping("/stopCollectService")
	@ResponseBody
	public String stopCollectService(@RequestBody SuspendFormBean bean){
		logger.info("----------------start stopCollectService------------------------");
		ResponseValue res = new ResponseValue();
		//发送停止采集服务的命令
		if(StringUtils.isNotBlank(bean.getRegOptId())){
			
			CmdMsg msg = new CmdMsg();
			msg.setMsgType(MyConstants.COMMAND_OPERATION_END);
			msg.setRegOptId(bean.getRegOptId());
			res = MessageProcess.process(msg);
		}
		logger.info("----------------end stopCollectService------------------------");
		return res.getJsonStr();
	}*/

	/**
	 * 
	 * 查询数字部分的监测项
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/queryAllMonitorConfig")
	@ResponseBody
	@ApiOperation(value = "查询监测项", httpMethod = "POST", notes = "查询监测项")
	public String getMonitorConfig(@ApiParam(name = "params", value = "参数") @RequestBody Map map) {
		logger.info("----------------start queryAllMonitorConfig------------------------");
		ResponseValue res = new ResponseValue();
		
		String regOptId = (String) map.get("regOptId");
		Integer docType = (Integer) map.get("docType");
		
		List<BasMonitorConfig> monitorConfigList = basMonitorConfigService.queryAllMonitorConfig(regOptId, docType);

		BaseInfoQuery baseQuery = new BaseInfoQuery();
		baseQuery.setRegOptId(regOptId);
		baseQuery.setPosition("1");
		baseQuery.setDocType(docType);
		List<BasAnaesMonitorConfigFormBean> anaesMonitorConfigList = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
		if (null != monitorConfigList && monitorConfigList.size() > 0) {
			for (BasMonitorConfig monitorConfig : monitorConfigList) {
				// 将监测项设置为未勾选
				monitorConfig.setChecked("0");
				if (null != anaesMonitorConfigList && anaesMonitorConfigList.size() > 0) {
					for (BasAnaesMonitorConfigFormBean anaesMonitorConfig : anaesMonitorConfigList) {
						// 如果监测项在b_anaes_monitor_config表中，则设置为勾选
						if (anaesMonitorConfig.getEventId().equals(String.valueOf(monitorConfig.getEventId()))) {
							monitorConfig.setChecked("1");
							monitorConfig.setMax(anaesMonitorConfig.getMax());
                            monitorConfig.setMin(anaesMonitorConfig.getMin());
							break;
						}
					}
				}
			}
		}
		res.setResultCode("1");
		res.setResultMessage("查询成功！");
		res.put("monitorConfigList", monitorConfigList);
		logger.info("----------------end queryAllMonitorConfig------------------------");
		return res.getJsonStr();
	}

	/**
	 * 修改数字部分监测项的显示字段
	 * 
	 * @param monitorConfigList
	 * @return
	 */
	@RequestMapping("/updMonitorConfig")
	@ResponseBody
	@ApiOperation(value = "修改数字部分监测项的显示字段", httpMethod = "POST", notes = "修改数字部分监测项的显示字段")
	public String updMonitorConfig(@ApiParam(name = "params", value = "参数") @RequestBody UpdAnaesMonitorConfigFormbean formbean) {
		logger.info("----------------start updMonitorConfig------------------------");
		ResponseValue res = new ResponseValue();
		if (null != formbean) {
			basAnaesMonitorConfigService.updAnaesMonitorConfig(formbean);
			res.setResultCode("1");
			res.setResultMessage("修改成功！");
			BaseInfoQuery baseQuery = new BaseInfoQuery();
			baseQuery.setRegOptId(formbean.getRegOptId());
			baseQuery.setPosition("1");
			baseQuery.setDocType(formbean.getDocType());
			res.put("monitorConfigList", basAnaesMonitorConfigService.finaAnaesListWithoutO2(baseQuery));
		}

		logger.info("----------------end updMonitorConfig------------------------");
		return res.getJsonStr();
	}

	/**
	 * 数字部分：获取新点
	 * 
	 * @param formbean
	 * @return
	 */
	@RequestMapping("/getmonDataNew")
	@ResponseBody
	@ApiOperation(value = "数字部分：获取新点", httpMethod = "POST", notes = "数字部分：获取新点")
	public String getmonDataNew(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDataFormBean formBean) {
		logger.info("------------------start  getmonDataNew---------------------------");
		ResponseValue res = new ResponseValue();
		String regOptId = formBean.getRegOptId();
		if (StringUtils.isBlank(regOptId)) {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
			logger.info("----------------end getmonDataNew------------------------");
			return res.getJsonStr();
		}

		BaseInfoQuery baseQuery = new BaseInfoQuery();
		baseQuery.setRegOptId(regOptId);
		baseQuery.setPosition("1");
		List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);

		List<String> observeIds = new ArrayList<String>();

		if (null != anaesLists && anaesLists.size() > 0) {
			for (BasAnaesMonitorConfigFormBean mc : anaesLists) {
				if (!observeIds.contains(mc.getEventId() + "")) {
					observeIds.add(mc.getEventId() + "");
				}
			}
		}
		List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getMonDataNew(formBean, observeIds);
		Map<Date, List<BasMonitorDisplay>> map = new TreeMap<Date, List<BasMonitorDisplay>>();
		List<BasMonitorDisplay> mds = null;
		if (null != monitorList && monitorList.size() > 0) {
			for (BasMonitorDisplay md : monitorList) {
				Date time = md.getTime();
				if (!map.containsKey(time)) {
					mds = new ArrayList<BasMonitorDisplay>();
					mds.add(md);
					map.put(time, mds);
				} else {
					mds = map.get(time);
					mds.add(md);
					map.put(time, mds);
				}
			}
		} else {
			// 无采集数据
			logger.info("----------无采集数据----------------");
		}

		List<MonDataFormBean> monDataList = new ArrayList<MonDataFormBean>();
		MonDataFormBean monData = null;
		MonitorData monitorData = null;
		if (!map.isEmpty() && map.size() > 0) {
			// 循环seriesMap中的数据
			for (Date key : map.keySet()) {
				monData = new MonDataFormBean();
				List<MonitorData> monitorDataList = new ArrayList<MonitorData>();
				List<BasMonitorDisplay> list = map.get(key);
				monData.setTime(key);
				if (null != list && list.size() > 0) {
					for (BasMonitorDisplay md : list) {
						Integer freq = md.getFreq();
						if (null != freq) {
							monData.setFreq(freq);
						}
						monitorData = new MonitorData();
						BeanUtils.copyProperties(md, monitorData);
						monitorDataList.add(monitorData);
					}
				}
				monData.setMonitorDataList(monitorDataList);
				monDataList.add(monData);
			}
		}

		// 获取总数
		int total = basMonitorDisplayService.getobsDataTotal(formBean, observeIds);
		res.setResultCode("1");
		res.setResultMessage("查询监测项数据成功！");
		res.put("total", total);
		res.put("monDataList", monDataList);
		logger.info("------------------end  getmonDataNew---------------------------");
		return res.getJsonStr();
	}

	/**
	 * 后台不分页，但根据每页分组给前端数据
	 * 
	 * @param formBean
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getPrintObserveData")
	@ResponseBody
	@ApiOperation(value = "分组获取打印数据", httpMethod = "POST", notes = "分组获取打印数据")
	public Map getPrintObserveData(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDataFormBean formBean) {
		logger.info("----------------start getPrintObserveData------------------------");
		Map map = new HashMap();
		List<MonitorDataPage> mdPageList = new ArrayList<MonitorDataPage>();

		// 获取需要显示的数据
		String regOptId = formBean.getRegOptId();
		if (StringUtils.isBlank(regOptId)) {
			map.put("resultCode", "70000000");
			map.put("resultMessage", Global.getRetMsg(map.get("resultCode").toString()));
			logger.info("----------------end getPrintObserveData------------------------");
			return map;
		}

		int size = 31;
		int pageSize = formBean.getSize();
		if (pageSize == 0) {
			pageSize = size;
		}

		// 查询入室时间
		DocAnaesRecord anaesRecord = docAnaesRecordService.searchAnaesRecordByRegOptId(regOptId);
		if (null != anaesRecord) {
			String inTime = anaesRecord.getInOperRoomTime();
			Date inTimeDate = null;
			if (StringUtils.isNotBlank(inTime)) {
				inTimeDate = SysUtil.getDate(inTime);
				formBean.setInTime(inTimeDate);
			} else {
				map.put("resultCode", "70000000");
				map.put("resultMessage", "入室时间不能为空！");
				logger.info("----------------end getPrintObserveData------------------------");
				return map;
			}

		} else {
			map.put("resultCode", "70000000");
			map.put("resultMessage", "麻醉记录单没有查询到记录！");
			logger.info("----------------end getPrintObserveData------------------------");
			return map;
		}

		Integer position = 0;
		String beid = formBean.getBeid();

		BaseInfoQuery baseQuery = new BaseInfoQuery();
		baseQuery.setRegOptId(regOptId);
		baseQuery.setPosition(position + "");
		baseQuery.setEnable("1");
		List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
		List<String> observeIds = new ArrayList<String>();

		if (null != anaesLists && anaesLists.size() > 0) {
			for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
				String observeId = bean.getEventId();
				// 获取显示项需要的observeIds
				observeIds.add(observeId);
			}
		}

		// 数字部分的obsIds
		baseQuery.setPosition("1");
		List<BasAnaesMonitorConfigFormBean> anaesNumberLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
		// List<BasMonitorConfig> monitorConfigList =
		// basMonitorConfigService.selectMustShowList();

		// 获取当前频率
		String currentModel = basRegOptService.getCurrentModel(formBean.getRegOptId());
		BasMonitorConfigFreq monitorFreq = basMonitorConfigFreqService.searchMonitorFreqByType(currentModel,regOptId);
		String f = "";
		Integer curfreq = 0;
		if (monitorFreq != null) {
			f = monitorFreq.getValue();
			curfreq = Integer.valueOf(f);
		}
		
        String docId = "";
        if( null != anaesRecord ){
            docId = anaesRecord.getAnaRecordId();
        }
        SearchFormBean searchBean = new SearchFormBean();
        searchBean.setDocId(docId);
        List<EvtCtlBreath> ctlBreathList = evtCtlBreathService.searchCtlBreathList(searchBean);
        CtlBreathDateFormBean cbFormBean = null;
        List<CtlBreathDateFormBean> cbList = new ArrayList<CtlBreathDateFormBean>();
        
        if(null != ctlBreathList && ctlBreathList.size()>0){
            for (int i = 0; i < ctlBreathList.size(); i++) {
                EvtCtlBreath ctlBreath = ctlBreathList.get(i);
                //String curState = ctlBreath.getCurrentState();
                Integer type = ctlBreath.getType();
                if(i == 0){
                    cbFormBean = new CtlBreathDateFormBean();
                    cbFormBean.setStartTime(ctlBreath.getStartTime());
                    cbFormBean.setType(type);
                    List<SysCodeFormbean> scList = basSysCodeService.searchSysCodeByGroupIdAndCodeName("breath_event", type + "", beid);
                    if(null != scList && scList.size()>0){
                        String codeValue = scList.get(0).getCodeValue();
                        cbFormBean.setCodeValue(codeValue);
                        cbFormBean.setCodeSvg(basIconSvgService.getPathByIcon(codeValue, beid));
                    }
                    cbList.add(cbFormBean);
                }else if(i > 0){
                    cbFormBean = new CtlBreathDateFormBean();
                    cbFormBean.setStartTime(ctlBreath.getStartTime());
                    cbFormBean.setEndTime(cbList.get(cbList.size()-1).getStartTime());
                    cbFormBean.setType(type);
                    List<SysCodeFormbean> scList = basSysCodeService.searchSysCodeByGroupIdAndCodeName("breath_event", type + "", beid);
                    if(null != scList && scList.size()>0){
                        String codeValue = scList.get(0).getCodeValue();
                        cbFormBean.setCodeValue(codeValue);
                        cbFormBean.setCodeSvg(basIconSvgService.getPathByIcon(codeValue, beid));
                    }
                    cbList.add(cbFormBean);
                }
                    
            }
        }

		// 获取总数
		int total = basMonitorDisplayService.getobsDataTotal(formBean, observeIds);

		if (total == 0) {
			map.put("resultCode", "1");
			map.put("resultMessage", "当前患者采集点数据暂无！");
			map.put("mdPageList", mdPageList);
			return map;
		} else {
			int no = total / pageSize; // 页码
			int mod = total % pageSize;
			if (no == 0) {
				if (0 != mod) {
					no = no + 1;
				}
				// 只有1页的数据
				formBean.setNo(no);
				formBean.setSize(pageSize);

				List<XAxisData1> xAxis = new ArrayList<XAxisData1>();
				XAxisData1 xaisData = null;
				List<XAxisDataBean> data = new ArrayList<XAxisDataBean>();

				// 建造yAxis数据
				List<YAxisData> yAxis = new ArrayList<YAxisData>();
				YAxisData yd = null;
		        yd = new YAxisData();
		        yd.setType("value");
		        yd.setName("bpm");
		        yd.setOrder(1);
		        yAxis.add(yd);
		        yd = new YAxisData();
		        yd.setType("value");
		        yd.setName("℃");
		        yd.setOrder(2);
		        yAxis.add(yd);
		        yd = new YAxisData();
		        yd.setType("value");
		        yd.setName("mmHg");
		        yd.setOrder(3);
		        yAxis.add(yd);
		        Collections.sort(yAxis);

				List<SeriesData> series = new ArrayList<SeriesData>();
				SeriesData seriesdata = null;
				List<SeriesDataObj> da = null;
				SeriesDataObj obj = null;
				Map<String, SeriesData> seriesMap = new HashMap<String, SeriesData>();

				if (null != anaesLists && anaesLists.size() > 0) {
					for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
						String observeId = bean.getEventId();
						// 获取显示项需要的observeIds
						observeIds.add(observeId);
						String observeName = bean.getEventName();
						String color = bean.getColor();// 对应图标
						String icon = bean.getIconId();// 对应颜色
						String units = bean.getUnits(); // 默认单位
						String svg = basIconSvgService.getPathByIcon(bean.getIconId(), beid);
						Float max = bean.getMax(); // max
						Float min = bean.getMin(); // min
						// ABP_HR，CVP，℃
						if (observeId.equals(MyConstants.TEMP_EVENT_ID)) { // 如果是温度，则y轴为1
							seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 1, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
						} else if (observeId.equals(MyConstants.CVP_SYS_EVENT_ID) || observeId.equals(MyConstants.CVP_DIA_EVENT_ID)) { // cvp,则y轴为2
							seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 2, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
						} else {// abp_hp等,y轴为0
							seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 0, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
						}
					}
				}

				// 数字部分的obsIds
				List<String> obsIds = new ArrayList<String>();
				if (null != anaesNumberLists && anaesNumberLists.size() > 0) {
					for (BasAnaesMonitorConfigFormBean mc : anaesNumberLists) {
						if (!obsIds.contains(mc.getEventId() + "")) {
							obsIds.add(mc.getEventId() + "");
						}
					}
				}

				List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsData(formBean, observeIds);

				Date t = new Date(1L);
				if (null != monitorList && monitorList.size() > 0) {
					for (int i = 0; i < monitorList.size(); i++) {
						BasMonitorDisplay md = monitorList.get(i);
						String key = md.getObserveId();
						Date time = md.getTime();

						if (t.getTime() != time.getTime()) {
							t = time;
							XAxisDataBean bean = new XAxisDataBean();
							bean.setValue(t);
							Integer freq = md.getIntervalTime();
							bean.setFreq(freq);
							data.add(bean);
						}

						// series
						if (!seriesMap.containsKey(key)) {
							logger.info("------------------没有当前key" + key + "------------------------");
						} else {
							seriesdata = seriesMap.get(key);
							da = seriesdata.getData();
							seriesdata.setType("line");
							seriesdata.setName(md.getObserveName());
							// 设置指定对应的y轴
							// 设置指定对应的y轴
							if (MyConstants.TEMP_EVENT_ID.equals(md.getObserveId())) {
								seriesdata.setyAxisIndex(1);
							} else if (MyConstants.CVP_SYS_EVENT_ID.equals(md.getObserveId()) || MyConstants.CVP_DIA_EVENT_ID.equals(md.getObserveId())) {
								seriesdata.setyAxisIndex(2);
							} else {
								seriesdata.setyAxisIndex(0);
							}
							// seriesdata.setSymbolSize(8);
							// 增加呼吸事件图标的判断
							if (MyConstants.RR_EVENT_ID.equals(md.getObserveId())) {
								if (null != cbList && cbList.size() > 0) {
									int flag = -1;
									for (CtlBreathDateFormBean cb : cbList) {
										Date st = cb.getStartTime();
										Date et = cb.getEndTime();
										// logger.info("getobsData----st="+st+",et="+et+",time="+time);
										if (null != et) {
											if (time.getTime() >= st.getTime() && time.getTime() < et.getTime()) {
												obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue());
												flag = 0;
												break;
											}
										} else {
											if (time.getTime() >= st.getTime()) {
												obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue());
												flag = 0;
												break;
											}
										}
									}
									if (flag == -1) {
										obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
									}
								} else {
									obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
								}
							} else {
								obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
							}

							da.add(obj);
							seriesdata.setData(da);
							seriesMap.put(key, seriesdata);
						}

					}

				} else {
					// 无采集数据
					logger.info("----------无采集数据----------------");
				}

				// 循环seriesMap中的数据
				for (String key : seriesMap.keySet()) {
					SeriesData sd = seriesMap.get(key);
					series.add(sd);
				}

				// 添加times到x轴
				xaisData = new XAxisData1();
				xaisData.setData(data);
				xAxis.add(xaisData);

				// 获取数字部分
				List<BasMonitorDisplay> monitorDisplayList = basMonitorDisplayService.getobsData(formBean, obsIds);
				Map<Date, List<BasMonitorDisplay>> tableMap = new TreeMap<Date, List<BasMonitorDisplay>>();
				List<BasMonitorDisplay> mds = null;
				if (null != monitorDisplayList && monitorDisplayList.size() > 0) {
					for (BasMonitorDisplay md : monitorDisplayList) {
						Date time = md.getTime();
						if (!tableMap.containsKey(time)) {
							mds = new ArrayList<BasMonitorDisplay>();
							mds.add(md);
							tableMap.put(time, mds);
						} else {
							mds = tableMap.get(time);
							mds.add(md);
							tableMap.put(time, mds);
						}
					}
				} else {
					// 无采集数据
					logger.info("----------无采集数据----------------");
				}

				List<MonDataFormBean> monDataList = new ArrayList<MonDataFormBean>();
				MonDataFormBean monData = null;
				MonitorData monitorData = null;

				if (!tableMap.isEmpty() && tableMap.size() > 0) {
					// int i = 0;
					int index = 0;
					// 循环seriesMap中的数据
					for (Date key : tableMap.keySet()) {
						// if(i%3==0){
						monData = new MonDataFormBean();
						List<MonitorData> monitorDataList = new ArrayList<MonitorData>();
						List<BasMonitorDisplay> list = tableMap.get(key);
						monData.setTime(key);
						monData.setIndex(index++);
						if (null != list && list.size() > 0) {
							for (BasMonitorDisplay md : list) {
								Integer fre = md.getFreq();
								if (null != fre) {
									monData.setFreq(fre);
								}
								monitorData = new MonitorData();
								BeanUtils.copyProperties(md, monitorData);
								monitorDataList.add(monitorData);
							}
						}
						monData.setMonitorDataList(monitorDataList);
						monDataList.add(monData);
						// }
						// i++;
					}
				}
				
				// 计算偏移量
		        Date inTime = formBean.getInTime();
		        Integer docType = formBean.getDocType();
		        long offset = 0;// 偏移量
		        BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
		        if (null == regOpt) {
		            logger.info("getobsData----regOpt手术为null！--------------");
		            map.put("resultCode", "10000000");
		            map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
		            return map;
		        } else {
		            Date operTime = regOpt.getOperTime();
		            if (null == operTime) {
		                logger.info("getobsData----operTime手术时间为null！--------------");
		                map.put("resultCode", "10000000");
		                map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
		                return map;
		            } else {

		                BasMonitorFreqChange monitorFreqChange = basMonitorFreqChangeService.selectFirstChangeTime(regOptId, DateUtils.formatDateTime(inTime));
		                if (null == monitorFreqChange) {
		                    map.put("changeFreqTime", null);
		                } else {
		                    map.put("changeFreqTime", monitorFreqChange.getTime());
		                }

		                long time = inTime.getTime() - operTime.getTime();
		                if (time == 0) {
		                    map.put("offset", offset);
		                } else if (time < 0) { // 修改后的入室时间小于第一次的手术命令开始时间
		                    BasMonitorDisplay firstMd = basMonitorDisplayService.findMonitorDisplaybyTime(regOptId, SysUtil.getTimeFormat(operTime), docType);
		                    long operFreq = 0;
		                    if (null != firstMd) {
		                        operFreq = firstMd.getFreq();
		                        offset = (Math.abs(time) % (operFreq * 1000)) / 1000;// 取绝对值取mod
		                        map.put("offset", offset);
		                    } else {
		                        logger.info("getobsData----operTime查询出来的MonitorDisplay为null！--------------");
		                        map.put("resultCode", "10000000");
		                        map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
		                        return map;
		                    }
		                } else { // time > 0
		                    BasMonitorDisplay curMd = basMonitorDisplayService.findMonitorDisplayByInTimeLimit1(regOptId, inTime, formBean.getDocType());
		                    if (null != curMd) {
		                        Date curTime = curMd.getTime();
		                        Integer operFreq = curMd.getFreq();
		                        Integer intervalTime = curMd.getIntervalTime();
		                        if (operFreq.intValue() != intervalTime.intValue()) {
		                            long myTime = ((curTime.getTime() + intervalTime * 1000) - inTime.getTime()) / 1000;
		                            if (myTime > 0) { // 1、如果最近点+intervalTime大于intime，则
		                                                // offset
		                                                // =(curTime+interval_time)-inTime
		                                map.put("offset", myTime);
		                            } else if (myTime == 0) {
		                                map.put("offset", 0);
		                            } else { // curTime+intervalTime 必须大于 inTime ，不然数据有误
										logger.info("getobsData----获取到的curTime+intervalTime小于修改的inTime，数据有误--------------");
										map.put("resultCode", "10000000");
										map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
										return map;
		                            }
		                        } else {
		                            long cTime = (inTime.getTime() - curTime.getTime()) / 1000; // cTime
		                                                                                        // 等于
		                                                                                        // 根据
		                            if (cTime == 0) {
		                                map.put("offset", 0);
		                            } else {
		                                offset = operFreq - cTime;// 当前频率-（入室时间-当前最近点时间）
		                                logger.info("offset===" + offset + ",cTime==" + cTime + ",inTime=" + inTime + ",curTime==" + curTime);
		                                map.put("offset", offset);
		                            }
		                        }
		                    } else {
		                        logger.info("getobsData----inTime查询出来的最近的右边的MonitorDisplay为null！--------------");
		                        map.put("resultCode", "10000000");
		                        map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
		                        return map;
		                    }
		                }
		            }
		        }

				MonitorDataPage page = new MonitorDataPage();
				page.setxAxis(xAxis);
				page.setyAxis(yAxis);
				page.setSeries(series);
				page.setMonDataList(monDataList);
				page.setFreq(curfreq); // 发送当前频率
				mdPageList.add(page);
				map.put("resultCode", "1");
				map.put("resultMessage", "操作成功！");
				map.put("mdPageList", mdPageList);
				logger.info("------------------end getPrintObserveData------------------------");
				return map;

			} else if (no > 0) { // 多页分组
				if (0 != mod) {
					no = no + 1;
				}
				for (int j = 0; j < no; j++) {
					int pageNo = j + 1;
					formBean.setNo(pageNo);
					formBean.setSize(pageSize);

					List<XAxisData1> xAxis = new ArrayList<XAxisData1>();
					XAxisData1 xaisData = null;
					List<XAxisDataBean> data = new ArrayList<XAxisDataBean>();

					// 建造yAxis数据
					List<YAxisData> yAxis = new ArrayList<YAxisData>();
					YAxisData yd = null;
			        yd = new YAxisData();
			        yd.setType("value");
			        yd.setName("bpm");
			        yd.setOrder(1);
			        yAxis.add(yd);
			        yd = new YAxisData();
			        yd.setType("value");
			        yd.setName("℃");
			        yd.setOrder(2);
			        yAxis.add(yd);
			        yd = new YAxisData();
			        yd.setType("value");
			        yd.setName("mmHg");
			        yd.setOrder(3);
			        yAxis.add(yd);
			        Collections.sort(yAxis);

					List<SeriesData> series = new ArrayList<SeriesData>();
					SeriesData seriesdata = null;
					List<SeriesDataObj> da = null;
					SeriesDataObj obj = null;
					Map<String, SeriesData> seriesMap = new HashMap<String, SeriesData>();

					if (null != anaesLists && anaesLists.size() > 0) {
						for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
							String observeId = bean.getEventId();
							// 获取显示项需要的observeIds
							observeIds.add(observeId);
							String observeName = bean.getEventName();
							String color = bean.getColor();// 对应图标
							String icon = bean.getIconId();// 对应颜色
							String units = bean.getUnits(); // 默认单位
							String svg = basIconSvgService.getPathByIcon(bean.getIconId(), beid);
							Float max = bean.getMax(); // max
							Float min = bean.getMin(); // min
							// ABP_HR，CVP，℃
							if (observeId.equals(MyConstants.TEMP_EVENT_ID)) { // 如果是温度，则y轴为2
								seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 1, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
							} else if (observeId.equals(MyConstants.CVP_SYS_EVENT_ID) || observeId.equals(MyConstants.CVP_DIA_EVENT_ID)) { // cvp,则y轴为1
								seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 2, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
							} else {// abp_hp等,y轴为0
								seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 0, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
							}

						}
					}

					// 数字部分的obsIds
					List<String> obsIds = new ArrayList<String>();
					if (null != anaesNumberLists && anaesNumberLists.size() > 0) {
						for (BasAnaesMonitorConfigFormBean mc : anaesNumberLists) {
							if (!obsIds.contains(mc.getEventId() + "")) {
								obsIds.add(mc.getEventId() + "");
							}
						}
					}

					// 获取当页的数据集合
					List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsData(formBean, observeIds);

					Date t = new Date(1L);
					if (null != monitorList && monitorList.size() > 0) {
						for (int i = 0; i < monitorList.size(); i++) {
							BasMonitorDisplay md = monitorList.get(i);
							String key = md.getObserveId();
							Date time = md.getTime();

							if (t.getTime() != time.getTime()) {
								t = time;
								XAxisDataBean bean = new XAxisDataBean();
								bean.setValue(t);
								Integer freq = md.getIntervalTime();
								bean.setFreq(freq);
								data.add(bean);
							}

							// series
							if (!seriesMap.containsKey(key)) {
								logger.info("------------------没有当前key" + key + "------------------------");
							} else {
								seriesdata = seriesMap.get(key);
								da = seriesdata.getData();
								seriesdata.setType("line");
								seriesdata.setName(md.getObserveName());
								// 设置指定对应的y轴
								if (MyConstants.TEMP_EVENT_ID.equals(md.getObserveId())) {
									seriesdata.setyAxisIndex(1);
								} else if (MyConstants.CVP_SYS_EVENT_ID.equals(md.getObserveId()) || MyConstants.CVP_DIA_EVENT_ID.equals(md.getObserveId())) {
									seriesdata.setyAxisIndex(2);
								} else {
									seriesdata.setyAxisIndex(0);
								}
								// seriesdata.setSymbolSize(8);

								// 增加呼吸事件图标的判断
								if (MyConstants.RR_EVENT_ID.equals(md.getObserveId())) {
									if (null != cbList && cbList.size() > 0) {
										int flag = -1;
										for (CtlBreathDateFormBean cb : cbList) {
											Date st = cb.getStartTime();
											Date et = cb.getEndTime();
											// logger.info("getobsData----st="+st+",et="+et+",time="+time);
											if (null != et) {
												if (time.getTime() >= st.getTime() && time.getTime() < et.getTime()) {
													obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue());
													flag = 0;
													break;
												}
											} else {
												if (time.getTime() >= st.getTime()) {
													obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue());
													flag = 0;
													break;
												}
											}
										}
										if (flag == -1) {
											obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
										}
									} else {
										obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
									}
								} else {
									obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
								}

								da.add(obj);
								seriesdata.setData(da);
								seriesMap.put(key, seriesdata);
							}

						}

					} else {
						// 无采集数据
						logger.info("----------无采集数据----------------");
					}

					// 循环seriesMap中的数据
					for (String key : seriesMap.keySet()) {
						SeriesData sd = seriesMap.get(key);
						series.add(sd);
					}

					// 添加times到x轴
					xaisData = new XAxisData1();
					xaisData.setData(data);
					xAxis.add(xaisData);

					// 获取数字部分
					List<BasMonitorDisplay> monitorDisplayList = basMonitorDisplayService.getobsData(formBean, obsIds);
					Map<Date, List<BasMonitorDisplay>> tableMap = new TreeMap<Date, List<BasMonitorDisplay>>();
					List<BasMonitorDisplay> mds = null;
					if (null != monitorDisplayList && monitorDisplayList.size() > 0) {
						for (BasMonitorDisplay md : monitorDisplayList) {
							Date time = md.getTime();
							if (!tableMap.containsKey(time)) {
								mds = new ArrayList<BasMonitorDisplay>();
								mds.add(md);
								tableMap.put(time, mds);
							} else {
								mds = tableMap.get(time);
								mds.add(md);
								tableMap.put(time, mds);
							}
						}
					} else {
						// 无采集数据
						logger.info("----------无采集数据----------------");
					}

					List<MonDataFormBean> monDataList = new ArrayList<MonDataFormBean>();
					MonDataFormBean monData = null;
					MonitorData monitorData = null;

					if (!tableMap.isEmpty() && tableMap.size() > 0) {
						// int i = 0;
						int index = 0;
						// 循环seriesMap中的数据
						for (Date key : tableMap.keySet()) {
							// if(i%3==0){
							monData = new MonDataFormBean();
							List<MonitorData> monitorDataList = new ArrayList<MonitorData>();
							List<BasMonitorDisplay> list = tableMap.get(key);
							monData.setTime(key);
							monData.setIndex(index++);
							if (null != list && list.size() > 0) {
								for (BasMonitorDisplay md : list) {
									Integer fre = md.getFreq();
									if (null != fre) {
										monData.setFreq(fre);
									}
									monitorData = new MonitorData();
									BeanUtils.copyProperties(md, monitorData);
									monitorDataList.add(monitorData);
								}
							}
							monData.setMonitorDataList(monitorDataList);
							monDataList.add(monData);
							// }
							// i++;
						}
					}
					
					// 计算偏移量
			        Date inTime = formBean.getInTime();
			        Integer docType = formBean.getDocType();
			        long offset = 0;// 偏移量
			        BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
			        if (null == regOpt) {
			            logger.info("getobsData----regOpt手术为null！--------------");
			            map.put("resultCode", "10000000");
			            map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
			            return map;
			        } else {
			            Date operTime = regOpt.getOperTime();
			            if (null == operTime) {
			                logger.info("getobsData----operTime手术时间为null！--------------");
			                map.put("resultCode", "10000000");
			                map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
			                return map;
			            } else {

			                BasMonitorFreqChange monitorFreqChange = basMonitorFreqChangeService.selectFirstChangeTime(regOptId, DateUtils.formatDateTime(inTime));
			                if (null == monitorFreqChange) {
			                    map.put("changeFreqTime", null);
			                } else {
			                    map.put("changeFreqTime", monitorFreqChange.getTime());
			                }

			                long time = inTime.getTime() - operTime.getTime();
			                if (time == 0) {
			                    map.put("offset", offset);
			                } else if (time < 0) { // 修改后的入室时间小于第一次的手术命令开始时间
			                    BasMonitorDisplay firstMd = basMonitorDisplayService.findMonitorDisplaybyTime(regOptId, SysUtil.getTimeFormat(operTime), docType);
			                    long operFreq = 0;
			                    if (null != firstMd) {
			                        operFreq = firstMd.getFreq();
			                        offset = (Math.abs(time) % (operFreq * 1000)) / 1000;// 取绝对值取mod
			                        map.put("offset", offset);
			                    } else {
			                        logger.info("getobsData----operTime查询出来的MonitorDisplay为null！--------------");
			                        map.put("resultCode", "10000000");
			                        map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
			                        return map;
			                    }
			                } else { // time > 0
			                    BasMonitorDisplay curMd = basMonitorDisplayService.findMonitorDisplayByInTimeLimit1(regOptId, inTime, formBean.getDocType());
			                    if (null != curMd) {
			                        Date curTime = curMd.getTime();
			                        Integer operFreq = curMd.getFreq();
			                        Integer intervalTime = curMd.getIntervalTime();
			                        if (operFreq.intValue() != intervalTime.intValue()) {
			                            long myTime = ((curTime.getTime() + intervalTime * 1000) - inTime.getTime()) / 1000;
			                            if (myTime > 0) { // 1、如果最近点+intervalTime大于intime，则
			                                                // offset
			                                                // =(curTime+interval_time)-inTime
			                                map.put("offset", myTime);
			                            } else if (myTime == 0) {
			                                map.put("offset", 0);
			                            } else { // curTime+intervalTime 必须大于 inTime ，不然数据有误
											logger.info("getobsData----获取到的curTime+intervalTime小于修改的inTime，数据有误--------------");
											map.put("resultCode", "10000000");
											map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
											return map;
										}
			                        } else {
			                            long cTime = (inTime.getTime() - curTime.getTime()) / 1000; // cTime
			                                                                                        // 等于
			                                                                                        // 根据
			                            if (cTime == 0) {
			                                map.put("offset", 0);
			                            } else {
			                                offset = operFreq - cTime;// 当前频率-（入室时间-当前最近点时间）
			                                logger.info("offset===" + offset + ",cTime==" + cTime + ",inTime=" + inTime + ",curTime==" + curTime);
			                                map.put("offset", offset);
			                            }
			                        }
			                    } else {
			                        logger.info("getobsData----inTime查询出来的最近的右边的MonitorDisplay为null！--------------");
			                        map.put("resultCode", "10000000");
			                        map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
			                        return map;
			                    }
			                }
			            }
			        }

					MonitorDataPage page = new MonitorDataPage();
					page.setxAxis(xAxis);
					page.setyAxis(yAxis);
					page.setSeries(series);
					page.setMonDataList(monDataList);
					page.setFreq(curfreq);
					mdPageList.add(page);

				}
				map.put("resultCode", "1");
				map.put("resultMessage", "操作成功！");
				map.put("mdPageList", mdPageList);
				logger.info("------------------end getPrintObserveData------------------------");
				return map;
			}
		}

		logger.info("------------------end getPrintObserveData------------------------");
		return map;
	}

	/**
	 * -- 当前医生或护士下面的手术（包含手术中未完成的手术和当前登录用户下的术前的手术），以及下面部分所有人当天的手术；
	 * 
	 * 当天所有的手术
	 */
	@RequestMapping("/getDayOper")
	@ResponseBody
	@ApiOperation(value = "当天所有的手术", httpMethod = "POST", notes = "当天所有的手术")
	public String getDayOper(@ApiParam(name="searchFormBean", value ="查询参数") @RequestBody SystemSearchFormBean searchFormBean) {
		logger.info("------------------start getDayOper------------------------");
		ResponseValue res = new ResponseValue();
		List<SearchRegOptByRoomIdAndOperDateAndStateFormBean> resultList = basRegOptService.searchDayRegOpt(DateUtils.getDate(),searchFormBean);
		int total = basRegOptService.searchDayRegOptTotal(DateUtils.getDate(), searchFormBean);
		res.put("total", total);
		res.put("resultList", resultList);
		BasOperroom operroom = basOperroomService.queryRoomListById(null);
		res.put("operRoomName", operroom!=null?operroom.getName():"");
		res.put("roomId", operroom!=null?operroom.getOperRoomId():"");
		logger.info("------------------end getDayOper------------------------");
		return res.getJsonStr();
	}

	/**
	 * -- 当前麻醉医生或巡回护士下面的手术（包含当前手术室下术中的手术和当前登录用户下的术前的手术） 麻醉医生只显示全麻的术前手术
	 * 巡回护士显示全麻和局麻的术前手术；
	 * 
	 * 当前用户下的手术（麻醉医生和巡回护士）
	 */
	@RequestMapping("/getCurUserOper")
	@ResponseBody
	@ApiOperation(value = "当前用户下的手术", httpMethod = "POST", notes = "当前用户下的手术")
	public String getCurUserOper(@ApiParam(name = "params", value = "参数") @RequestBody JSONObject json) {
		logger.info("------------------start getCurUserOper------------------------");
		ResponseValue res = new ResponseValue();
		String userId = json.get("userId").toString();
		List<SearchRegOptByRoomIdAndOperDateAndStateFormBean> resultList = basRegOptService.searchCurUserRegOpt(DateUtils.getDate(), userId);
		res.put("resultList", resultList);
		BasOperroom operroom = basOperroomService.queryRoomListById(null);
		res.put("operRoomName", operroom!=null?operroom.getName():"");
		res.put("roomId", operroom!=null?operroom.getOperRoomId():"");
//		String operRoomName = resultList.size() > 0 ? resultList.get(0).getOperRoomName() : basOperroomService.queryRoomListById(null).getName();
//		res.put("operRoomName", operRoomName);
//		res.put("roomId", getRoomId());
		logger.info("------------------end getCurUserOper------------------------");
		return res.getJsonStr();
	}
	
	/**
	 * 查询历史记录
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/queryAllRegOpt")
	@ResponseBody
	@ApiOperation(value = "查询所有手术", httpMethod = "POST", notes = "查询所有手术")
	public String queryAllRegOpt(@ApiParam(name = "params", value = "参数") @RequestBody RegOptFormBean bean) {
		logger.info("------------------start queryAllRegOpt------------------------");
		ResponseValue res = new ResponseValue();
		BasOperroom operroom = basOperroomService.queryRoomListById(null);
		//String roomId = Global.getConfig("roomId").toString();
		bean.setRoomId(operroom.getOperRoomId());
		List<SearchRegOptByRoomIdAndOperDateAndStateFormBean> resultList = basRegOptService.queryAllRegOpt(bean);
		res.put("resultList", resultList);
		res.put("operRoomName", operroom!=null?operroom.getName():"");
		res.put("roomId", operroom!=null?operroom.getOperRoomId():"");
//		String operRoomName = resultList.size() > 0 ? resultList.get(0).getOperRoomName() : basOperroomService.queryRoomListById(null).getName();
//		res.put("operRoomName", operRoomName);
//		res.put("roomId", getRoomId());
		logger.info("------------------end queryAllRegOpt------------------------");
		return res.getJsonStr();
	}

	@RequestMapping("/getPupilData")
	@ResponseBody
	@ApiOperation(value = "getPupilData", httpMethod = "POST", notes = "getPupilData")
	public String getPupilData(@ApiParam(name = "formBean", value = "查询参数") @RequestBody MonitorDataFormBean formBean) {
		logger.info("----------------start getPupilData------------------------");
		ResponseValue res = new ResponseValue();
		String regOptId = formBean.getRegOptId();
		if (StringUtils.isBlank(regOptId)) {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
			logger.info("----------------end getPupilData------------------------");
			return res.getJsonStr();
		}
		// Integer position = 1;
		// List<Observe> observes =
		// observeService.searchMonitorsDispList(regOptId, position);
		List<BasMonitorConfig> monitorConfigList = basMonitorConfigService.selectMustShowList(regOptId);
		List<String> observeIds = new ArrayList<String>();

		if (null != monitorConfigList && monitorConfigList.size() > 0) {
			for (BasMonitorConfig mc : monitorConfigList) {
				if (!observeIds.contains(mc.getEventId() + "")) {
					observeIds.add(mc.getEventId() + "");
				}
			}
		}

		List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsData(formBean, observeIds);
		TreeMap<Date, Integer> treeMap = new TreeMap<Date, Integer>();
		if (null != monitorList && monitorList.size() > 0) {
			for (int i = 0; i < monitorList.size(); i++) {
				BasMonitorDisplay md = monitorList.get(i);
				Date time = md.getTime();
				if (!treeMap.containsKey(time)) {
					treeMap.put(time, i);
				} else {
					treeMap.put(time, i);
				}
			}
		} else {
			// 无采集数据
			logger.info("treeMap----------无采集数据----------------");
		}

		List<MonitorPupilFormBean> mpList = new ArrayList<MonitorPupilFormBean>();

		if (!treeMap.isEmpty() && treeMap.size() > 0) {
			Date startTime = treeMap.firstKey();
			Date endTime = treeMap.lastKey();
			if (null == startTime) {
				startTime = new Date();
			}
			if (null == endTime) {
				endTime = new Date();
			}
			List<BasMonitorPupil> pupilList = basMonitorPupilService.getPupilList(regOptId, startTime, endTime);

			MonitorPupilFormBean tempBean = null;
			boolean bool = false;
			int index = 0;
			if (null != pupilList && pupilList.size() > 0) {
				for (Date key : treeMap.keySet()) {
					for (int i = 0; i < pupilList.size(); i++) {
						BasMonitorPupil mp = pupilList.get(i);
						if (mp.getTime().getTime() == key.getTime()) {
							MonitorPupilFormBean monitorPupil = new MonitorPupilFormBean();
							BeanUtils.copyProperties(mp, monitorPupil);
							monitorPupil.setIndex(index++);
							// mpList.add(monitorPupil);
							tempBean = monitorPupil;
							bool = true;
							break;
						}
					}
					if (bool) {
						mpList.add(tempBean);
						bool = false;
					} else {
						MonitorPupilFormBean monitorPupil = new MonitorPupilFormBean();
						monitorPupil.setRegOptId(regOptId);
						monitorPupil.setTime(key);
						monitorPupil.setIndex(index++);
						mpList.add(monitorPupil);
					}
				}

			} else {
				for (Date key : treeMap.keySet()) {
					MonitorPupilFormBean mp = new MonitorPupilFormBean();
					mp.setRegOptId(regOptId);
					mp.setTime(key);
					mp.setIndex(index++);
					mpList.add(mp);
				}
			}

		}

		// 获取总数
		int total = basMonitorDisplayService.getobsDataTotal(formBean, observeIds);

		res.setResultCode("1");
		res.setResultMessage("查询监测项数据成功！");
		res.put("total", total);
		res.put("pupilDataList", mpList);
		logger.info("----------------end getPupilData------------------------");
		return res.getJsonStr();
	}
	
	/**
     * 包含新增和修改
     * @param mp
     * @return
     */
    @RequestMapping("/saveMonitorPupil")
    @ResponseBody
    public String saveMonitorPupil(@RequestBody BasMonitorPupil mp){
        logger.info("----------------start saveMonitorPupil------------------------");
        ResponseValue res = new ResponseValue();
        try {
            String pupilId = basMonitorDisplayService.saveMonitorPupil(mp);
            res.put("pupilId", pupilId);
            res.setResultCode("1");
            res.setResultMessage("操作成功");
        } catch (Exception e) {
            logger.error("saveMonitorPupil---"+Exceptions.getStackTraceAsString(e));
            res.setResultCode("10000000");
            res.setResultMessage("系统错误，请与系统管理员联系!");
        }
        logger.info("------------------end saveMonitorPupil------------------------");
        return res.getJsonStr();
    }
    
    /**
     * 查询术中出入量总量详情
     * @param mp
     * @return
     */
    @RequestMapping("/searchIOAmoutDetail")
    @ResponseBody
    public String searchIOAmoutDetail(@RequestBody SearchFormBean searchBean)
    {
        logger.info("----------------start searchIOAmoutDetail------------------------");
        ResponseValue res = new ResponseValue();
        String docId = searchBean.getDocId();
        Integer docType = searchBean.getDocType();
        //失血量
        Integer bleeding = evtEgressService.getEgressCountValueByIoDefName("出血量", docId, docType);
        res.put("bleeding", bleeding);
        //尿量
        Integer urine = evtEgressService.getEgressCountValueByIoDefName("尿量", docId, docType);
        res.put("urine", urine);
        //输液
        Integer infusion = evtInEventService.getIoeventCountValueByIoDef(docId, null, "1", docType);
        res.put("infusion", infusion);
        //输血
        Integer blood = evtInEventService.getIoeventCountValueByIoDef(docId, null, "2", docType);
        
        searchBean.setSubType("2");
        List<SearchOptOperIoevent> bloodList = evtInEventService.queryIoeventTotal(searchBean);
        if (null != bloodList && bloodList.size() > 0)
        {
            String bloodDetail = "";
            for (SearchOptOperIoevent ioevent : bloodList)
            {
                if ("冷沉淀".equals(ioevent.getName()) && StringUtils.isNotBlank(ioevent.getTotalAmout()))
                {
                    blood = blood - (int)Float.parseFloat(ioevent.getTotalAmout()); 
                    bloodDetail = bloodDetail + "+" + ioevent.getName() + ioevent.getTotalAmout() + ioevent.getDosageUnit();
                }
                else if ("红细胞悬液".equals(ioevent.getName()) && StringUtils.isNotBlank(ioevent.getTotalAmout()))
                {
                    blood = blood - (int)Float.parseFloat(ioevent.getTotalAmout());
                    bloodDetail = bloodDetail + "+" + ioevent.getName() + ioevent.getTotalAmout() + ioevent.getDosageUnit();
                }
                else if ("血小板".equals(ioevent.getName()) && StringUtils.isNotBlank(ioevent.getTotalAmout()))
                {
                    blood = blood - (int)Float.parseFloat(ioevent.getTotalAmout());
                    bloodDetail = bloodDetail + "+" + ioevent.getName() + ioevent.getTotalAmout() + ioevent.getDosageUnit();
                }
                    
            }
            res.put("bloodDetail", bloodDetail);
        }
        res.put("blood", blood);
        logger.info("------------------end searchIOAmoutDetail------------------------");
        return res.getJsonStr();
    }
    
    /**
     * 判断局麻是否需要麻醉医生参与
     * @param mp
     * @return
     */
    @RequestMapping("/isNeedAnaesDoctor")
    @ResponseBody
    public String isNeedAnaesDoctor(@RequestBody Map<String,Object> map)
    {
        logger.info("----------------start isNeedAnaesDoctor------------------------");
        ResponseValue res = new ResponseValue();
        String regOptId = null == map ? "" : (String)map.get("regOptId");
        BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
        if (null == regOpt)
        {
            res.setResultCode("20000002");
            res.setResultMessage("手术信息不存在!");
            return res.getJsonStr();
        }
        Integer isNeed = null == map ? null : (Integer)map.get("isNeed");
        //如果需要麻醉医生，则把当前局麻手术改为全麻手术
        if (null != isNeed && 1 == isNeed)
        {
            basRegOptService.updateIsLocalAnaes(0, regOptId);
        }
        logger.info("------------------end isNeedAnaesDoctor------------------------");
        return res.getJsonStr();
    }
    
    
    
    
    
    
    /**
     * 中央监测
     * @param mp
     * @return
     */
    @RequestMapping("/queryCentralBigScreenData")
    @ResponseBody
    public String queryCentralBigScreenData()
    {
        logger.info("----------------start queryCentralBigScreenData------------------------");
        ResponseValue res = new ResponseValue();
        
        BasDictItem flipTimeItem = basDictItemService.getListByGroupIdandCodeName("screen_flip_time", "翻页时间(秒)", getBeid());
        res.put("flipTime", flipTimeItem!=null?flipTimeItem.getCodeValue():"");
        BasDictItem showBedItem = basDictItemService.getListByGroupIdandCodeName("show_free_bed", "显示空床(1是0否)", getBeid());
        res.put("showFree", showBedItem!=null?showBedItem.getCodeValue():"");
        
        List<CentralBigScreenDataFormbean> dataList =  basRegOptService.queryCentralBigScreenData(showBedItem!=null?showBedItem.getCodeValue():"");
        BaseInfoQuery baseInfo = new BaseInfoQuery();
        
        if(null!= dataList && dataList.size()>0){
        	for (CentralBigScreenDataFormbean centralBigScreenDataFormbean : dataList) {
        		if(StringUtils.isNotBlank(centralBigScreenDataFormbean.getRegOptId())){
	        		baseInfo.setRegOptId(centralBigScreenDataFormbean.getRegOptId());
	        		baseInfo.setDocType(1);
	        		Map<String, RealTimeDataFormBean> monitorList = basMonitorDisplayService.searchObserveMapByPosition(baseInfo);
	        		
	        		if(monitorList.get("RESP") != null){
	        			RealTimeDataFormBean respData = monitorList.get("RESP");
	        			if(respData.getValue() !=null){
		        			float dataVal = respData.getValue();
		        			centralBigScreenDataFormbean.setRespValue((int)dataVal);
		        			centralBigScreenDataFormbean.setRespState(respData.getState());
	        			}
		        	}
	        		if(monitorList.get("HR") != null){
	        			RealTimeDataFormBean hrData = monitorList.get("HR");
	        			if(hrData.getValue() !=null){
	        				float dataVal = hrData.getValue();
		        			centralBigScreenDataFormbean.setHrValue((int)dataVal);
		        			centralBigScreenDataFormbean.setHrState(hrData.getState());
		        		}
	        		}
	        		if(monitorList.get("PR") != null){
	        			RealTimeDataFormBean prData = monitorList.get("PR");
	        			if(prData.getValue() !=null){
		        			float dataVal = prData.getValue();
		        			centralBigScreenDataFormbean.setPrValue((int)dataVal);
		        			centralBigScreenDataFormbean.setPrState(prData.getState());
	        			}
	        		}
	        		if(monitorList.get("ABP_DIAS") != null){
	        			RealTimeDataFormBean abpDiasData = monitorList.get("ABP_DIAS");
	        			if(abpDiasData.getValue() !=null){
		        			float dataVal = abpDiasData.getValue();
		        			centralBigScreenDataFormbean.setAbpDiasValue((int)dataVal);
		        			centralBigScreenDataFormbean.setAbpDiasState(abpDiasData.getState());
	        			}
	        		}
	        		if(monitorList.get("ABP_SYS") != null){
	        			RealTimeDataFormBean abpSysData = monitorList.get("ABP_SYS");
	        			if(abpSysData.getValue() !=null){
		        			float dataVal = abpSysData.getValue();
		        			centralBigScreenDataFormbean.setAbpSysValue((int)dataVal);
		        			centralBigScreenDataFormbean.setAbpSysState(abpSysData.getState());
	        			}
		        	}
	        		if(monitorList.get("NIBP_DIAS") != null){
	        			RealTimeDataFormBean nibpDiasData = monitorList.get("NIBP_DIAS");
	        			if(nibpDiasData.getValue() !=null){
	        				float dataVal = nibpDiasData.getValue();
	        				centralBigScreenDataFormbean.setNibpDiasValue((int)dataVal);
		        			centralBigScreenDataFormbean.setNibpDiasState(nibpDiasData.getState());
	        			}
	        		}
	        		if(monitorList.get("NIBP_SYS") != null){
	        			RealTimeDataFormBean nibpSysData = monitorList.get("NIBP_SYS");
	        			if(nibpSysData.getValue() !=null){
		        			float dataVal = nibpSysData.getValue();
		        			centralBigScreenDataFormbean.setNibpSysValue((int)dataVal);
		        			centralBigScreenDataFormbean.setNibpSysState(nibpSysData.getState());
		        		}
	        		}
        		}
			}
        }
        
        
        
        res.put("resultList", dataList);
        logger.info("------------------end queryCentralBigScreenData------------------------");
        return res.getJsonStr();
    }
    
    /**
     * 再次手术
     * @param searchBean
     * @return
     */
    @RequestMapping("/againStartOper")
    @ResponseBody
    public String againStartOper(@RequestBody SearchFormBean searchBean){
        logger.info("----------------start againStartOper------------------------");
        ResponseValue resp = new ResponseValue();
        basMonitorDisplayService.againStartOper(searchBean, resp);   
        logger.info("------------------end againStartOper------------------------");
        return resp.getJsonStr();
    }
    
}
