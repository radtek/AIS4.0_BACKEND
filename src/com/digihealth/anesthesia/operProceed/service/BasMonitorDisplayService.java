package com.digihealth.anesthesia.operProceed.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.config.OperationState;
import com.digihealth.anesthesia.basedata.formbean.BaseInfoQuery;
import com.digihealth.anesthesia.basedata.formbean.SysCodeFormbean;
import com.digihealth.anesthesia.basedata.po.BasCollectConfig;
import com.digihealth.anesthesia.basedata.po.BasDeviceConfig;
import com.digihealth.anesthesia.basedata.po.BasDispatch;
import com.digihealth.anesthesia.basedata.po.BasMonitorConfigFreq;
import com.digihealth.anesthesia.basedata.po.BasOperroom;
import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.basedata.po.BasRegionBed;
import com.digihealth.anesthesia.basedata.po.Controller;
import com.digihealth.anesthesia.basedata.po.Device;
import com.digihealth.anesthesia.common.config.Global;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.exception.CustomException;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.CompareObject;
import com.digihealth.anesthesia.common.utils.DateUtils;
import com.digihealth.anesthesia.common.utils.Exceptions;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.common.utils.JsonType;
import com.digihealth.anesthesia.common.utils.SysUtil;
import com.digihealth.anesthesia.doc.po.DocAnaesPacuRec;
import com.digihealth.anesthesia.doc.po.DocAnaesRecord;
import com.digihealth.anesthesia.evt.formbean.ChangeValueFormbean;
import com.digihealth.anesthesia.evt.formbean.SearchConditionFormBean;
import com.digihealth.anesthesia.evt.formbean.SearchFormBean;
import com.digihealth.anesthesia.evt.formbean.SearchOperaPatrolRecordFormBean;
import com.digihealth.anesthesia.evt.po.EvtAnaesEvent;
import com.digihealth.anesthesia.evt.po.EvtAnaesModifyRecord;
import com.digihealth.anesthesia.evt.po.EvtOperBodyEvent;
import com.digihealth.anesthesia.evt.po.EvtRescueevent;
import com.digihealth.anesthesia.evt.service.EvtAnaesEventService;
import com.digihealth.anesthesia.operProceed.core.MyConstants;
import com.digihealth.anesthesia.operProceed.formbean.BasAnaesMonitorConfigFormBean;
import com.digihealth.anesthesia.operProceed.formbean.DeviceConfigFormBean;
import com.digihealth.anesthesia.operProceed.formbean.EndOperationFormBean;
import com.digihealth.anesthesia.operProceed.formbean.EnterRoomFormBean;
import com.digihealth.anesthesia.operProceed.formbean.FirstSpotFormBean;
import com.digihealth.anesthesia.operProceed.formbean.IntervalDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonitorDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonitorDisplayChangeFormBean;
import com.digihealth.anesthesia.operProceed.formbean.RealTimeDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.RescueeventFormBean;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfig;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfigDefault;
import com.digihealth.anesthesia.operProceed.po.BasMonitorDisplay;
import com.digihealth.anesthesia.operProceed.po.BasMonitorDisplayChangeHis;
import com.digihealth.anesthesia.operProceed.po.BasMonitorFreqChange;
import com.digihealth.anesthesia.operProceed.po.BasMonitorPupil;
import com.digihealth.anesthesia.operProceed.po.BasObserveData;
import com.digihealth.anesthesia.operProceed.po.Observe;
import com.digihealth.anesthesia.sysMng.po.BasDictItem;
import com.digihealth.anesthesia.websocket.WebSocketHandler;

@Service
public class BasMonitorDisplayService extends BaseService{
	
	private static Logger logger = Logger.getLogger(BasMonitorDisplayService.class);
	
	@Transactional
	public void addobsdat(BasMonitorDisplay monitorDisp) {
		String observeId = monitorDisp.getObserveId();
		Float value = monitorDisp.getValue();
		String regOptId = monitorDisp.getRegOptId();
		Date observeDate = monitorDisp.getTime();
		//String time = "";
		//if(null!=observeDate){
		//	time = SysUtil.getTimeFormat(observeDate);
		//}
		BasMonitorDisplay md = basMonitorDisplayDao.getUniqMonitorDisplay(regOptId, observeDate, observeId);
		if(null != md){ //修改b_monitor_display
			String id = md.getId();
			BaseInfoQuery baseQuery = new BaseInfoQuery();
			baseQuery.setRegOptId(regOptId);
			baseQuery.setEventId(observeId);
			baseQuery.setBeid(getBeid());
			
			//从数据库中查询最大最小值
			BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean = basAnaesMonitorConfigDao.getAnaesMonitorConfigEventId(baseQuery);
			if(null != anaesMonitorConfigFormBean){
				int state = -2;
				Float max = anaesMonitorConfigFormBean.getMax();
				Float min = anaesMonitorConfigFormBean.getMin();
				if(null != value){
					if(value.floatValue() > max.floatValue()){
						state = 1; 
					}else if(value.floatValue() < min.floatValue()){
						state = -1;
					}else{
						state = 0;
					}
					monitorDisp.setState(state);
				}
			}
			//插入修改历史表
			BasMonitorDisplayChangeHis observeHis = new BasMonitorDisplayChangeHis();
			observeHis.setObserveDataChange(md, value,"");
			observeHis.setId(GenerateSequenceUtil.generateSequenceNo());
			basMonitorDisplayChangeHisDao.insert(observeHis);
			
			BeanUtils.copyProperties(monitorDisp, md);
			md.setId(id);//防止页面传递了id
			md.setAmendFlag(3);
			basMonitorDisplayDao.updateByPrimaryKeySelective(md); //修改monitorDisplay数据
			
		}else{//新增b_monitor_display
			String newId = GenerateSequenceUtil.generateSequenceNo();
			monitorDisp.setId(newId);
			
			BaseInfoQuery baseQuery = new BaseInfoQuery();
			baseQuery.setRegOptId(regOptId);
			baseQuery.setEventId(observeId);
			baseQuery.setBeid(getBeid());
			
			BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean = basAnaesMonitorConfigDao.getAnaesMonitorConfigEventId(baseQuery);
			if(null != anaesMonitorConfigFormBean){
				int state = -2;
				Float max = anaesMonitorConfigFormBean.getMax();
				Float min = anaesMonitorConfigFormBean.getMin();
				if(null != value){
					if(value.floatValue() > max.floatValue()){
						state = 1; 
					}else if(value.floatValue() < min.floatValue()){
						state = -1;
					}else{
						state = 0;
					}
					monitorDisp.setState(state);
				}
				monitorDisp.setObserveName(anaesMonitorConfigFormBean.getEventName());
				monitorDisp.setIcon(anaesMonitorConfigFormBean.getIconId());
				monitorDisp.setColor(anaesMonitorConfigFormBean.getColor());
				monitorDisp.setPosition(anaesMonitorConfigFormBean.getPosition());
				monitorDisp.setAmendFlag(3); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
			}
			basMonitorDisplayDao.insertSelective(monitorDisp);
		}
		
	}
	
	@Transactional
    public void changeMonitDisplay(MonitorDisplayChangeFormBean changeBean) {
        // 第一步先修改 monitorDisplay
        String id = changeBean.getId();
        //Date time = changeBean.getTime();
        String regOptId = changeBean.getDocId();
        String observeId = changeBean.getObserveId();
        Float value = changeBean.getValue();
        //String searchtime = "";
        //if (null != time) {
        //  searchtime = SysUtil.getTimeFormat(time);
        //}
        BasMonitorDisplay monitorDisplay = basMonitorDisplayDao.selectById(id);
        //值相等则不进行修改操作
        if (Objects.equals(monitorDisplay.getValue(), value))
        {
            return;
        }
        BaseInfoQuery baseQuery = new BaseInfoQuery();
        baseQuery.setRegOptId(regOptId);
        baseQuery.setEventId(observeId);
        baseQuery.setBeid(getBeid());
        
        //从数据库中查询最大最小值
        BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean = basAnaesMonitorConfigDao.getAnaesMonitorConfigEventId(baseQuery);
        if(null != anaesMonitorConfigFormBean){
            int state = -2;
            Float max = anaesMonitorConfigFormBean.getMax();
            Float min = anaesMonitorConfigFormBean.getMin();
            if(null != value){
                if(value.floatValue() > max.floatValue()){
                    state = 1; 
                }else if(value.floatValue() < min.floatValue()){
                    state = -1;
                }else{
                    state = 0;
                }
                changeBean.setState(state);
            }
        }
        
        
        /**
         * 2017-10-30沈阳本溪
         * 将修改痕迹保存到表中
         */
        String newId ="";
        Float oldValue = 0f;
        String remark = "修改";
        if (StringUtils.isBlank(id)){
            newId = GenerateSequenceUtil.generateSequenceNo(GenerateSequenceUtil.getRoomId(regOptId));
            remark = "新增";
        }else{
            newId = id;
            if(null != monitorDisplay)
                oldValue = monitorDisplay.getValue();
        }
        BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
        //如果当前状态不为术中时，则需要记录变更信息
        if(null!=regOpt && !"04".equals(regOpt.getState())){
            
            if(!Objects.equals(oldValue, value)){
            
                EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
                evtModRd.setBeid(getBeid());
                evtModRd.setIp(getIP());
                evtModRd.setOperId(getUserName());
                evtModRd.setEventId(newId);
                evtModRd.setRegOptId(regOptId);
                evtModRd.setModifyDate(new Date());
                evtModRd.setModTable("bas_monitor_display(术中监测表)");
                evtModRd.setOperModule("术中监测");
                evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
                evtModRd.setModProperty(changeBean.getObserveId()+":"+changeBean.getObserveName());
                evtModRd.setOldValue(oldValue==null?"":oldValue+"");
                evtModRd.setNewValue(value==null?"":value+"");
                evtModRd.setRemark(remark);
                evtAnaesModifyRecordDao.insert(evtModRd);
            }
        }
        
        //如果id为空则插入新点
        if (StringUtils.isBlank(id))
        {
            monitorDisplay = new BasMonitorDisplay();
            monitorDisplay.setId(newId);
            BeanUtils.copyProperties(changeBean, monitorDisplay);
            monitorDisplay.setAmendFlag(3); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
            basMonitorDisplayDao.insert(monitorDisplay);
        }
        else
        {
            if (null != monitorDisplay)
            {
                // 插入修改历史表
                BasMonitorDisplayChangeHis observeHis = new BasMonitorDisplayChangeHis();
                observeHis.setObserveDataChange(monitorDisplay, value, changeBean.getMemo());
                observeHis.setId(GenerateSequenceUtil.generateSequenceNo());
                observeHis.setUserId(changeBean.getUserName());
                basMonitorDisplayChangeHisDao.insert(observeHis);
                
                BeanUtils.copyProperties(changeBean, monitorDisplay);
                monitorDisplay.setAmendFlag(2); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                basMonitorDisplayDao.updateByPrimaryKeySelective(monitorDisplay); // 修改monitorDisplay数据
                
            }
        }
        
        if (MyConstants.O2_EVENT_ID.equals(monitorDisplay.getObserveId()))
        {
            //如果氧流量的值进行了修改，则修改时间点以后所有的氧流量值都进行修改
            List<String> observeIds = new ArrayList<String>();
            observeIds.add(MyConstants.O2_EVENT_ID);
            //获取修改时间点以后所有氧流量点
            List<BasMonitorDisplay> bmdList = basMonitorDisplayDao.getobsDataNew2(regOptId, DateUtils.formatDateTime(changeBean.getTime()), observeIds,null);
            if (null != bmdList && bmdList.size() > 0 )
            {
                for (BasMonitorDisplay bmd : bmdList)
                {
                    // 插入修改历史表
                    BasMonitorDisplayChangeHis observeHis = new BasMonitorDisplayChangeHis();
                    observeHis.setObserveDataChange(bmd, value, changeBean.getMemo());
                    observeHis.setId(GenerateSequenceUtil.generateSequenceNo());
                    observeHis.setUserId(changeBean.getUserName());
                    basMonitorDisplayChangeHisDao.insert(observeHis);
                    
                    //BeanUtils.copyProperties(changeBean, bmd);
                    bmd.setValue(changeBean.getValue());
                    bmd.setAmendFlag(2); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                    basMonitorDisplayDao.updateByPrimaryKeySelective(bmd); // 修改monitorDisplay数据
                }
            }
        }
    }
	
	/**
	 * 查询术中实时监测项 双阳改变
	 */
	public Map<String,RealTimeDataFormBean> searchObserveMapByPosition(BaseInfoQuery baseQuery) {
		// 从数据库查询最新的实时监测数据 
		List<RealTimeDataFormBean> observeList = basObserveDataDao.searchObserveDataByPosition(baseQuery);
		Map<String,RealTimeDataFormBean> resultMap = new HashMap<String,RealTimeDataFormBean>();
		Date curTime = new Date();
		if (null != observeList && observeList.size() > 0) {
			for (RealTimeDataFormBean rtData : observeList) {
				Date time = rtData.getTime();
				String obserName = rtData.getObserveName();
				String beid = getBeid();
                BasMonitorConfigDefault mcd = basMonitorConfigDefaultDao.selectByEventName(obserName, beid, baseQuery.getDocType());
				if (null != mcd)
				{
				    String commonEventId = mcd.getEventId();
				    rtData.setObserveId(commonEventId); 
                    BaseInfoQuery bq = new BaseInfoQuery();
                    bq.setRegOptId(baseQuery.getRegOptId());
                    bq.setEventId(commonEventId);
                    bq.setBeid(beid);
                    bq.setOperRoomId(getCurRoomId(null));
                    bq.setDocType(baseQuery.getDocType());
                    BasAnaesMonitorConfigFormBean amc = basAnaesMonitorConfigDao.getAnaesMonitorConfigByEventId(baseQuery);
                    RealTimeDataFormBean formBean = resultMap.get(obserName);
                    //三种情况下，将当前rtData作为重复监测项的描点数据
                    //1、当前重复监测项没有对应的监测数据
                    if (null == formBean)
                    {
                        resultMap.put(obserName, rtData);
                    }
                    //2、用户选择的设备对应有相应的监测数据
                    else if (null != amc && rtData.getObserveId().equals(amc.getRealEventId()) && null != rtData.getValue())
                    {
                        resultMap.put(obserName, rtData);
                    }
                    //3、当前放入map中的rtData没有数据，但是当前循环的rtData有数据
                    else if (null == formBean.getValue()  && null != rtData.getValue())
                    {
                        resultMap.put(obserName, rtData);
                    }
				}
				else
				{
				    resultMap.put(obserName, rtData);
				}
				long t = Math.abs(curTime.getTime()-time.getTime());
				//超过300秒的数据不传给前端
				if(t- MyConstants.REAL_TIME_DATA_TIMEOUT * 1000 > 0){
					//observeList = null;
					resultMap.clear();
					break;
				}
			}
		}
		return resultMap;
	}
	
	
	/**
	 * 获取最近点的对象
	 * @param regOptId
	 * @return
	 */
	public BasMonitorDisplay findLastestMonitorDisplay(String regOptId, Integer docType){
		return basMonitorDisplayDao.findLastestMonitorDisplay(regOptId, docType);
	}
	
	
	@Transactional
	public void batchInsertMonitorDisplays(List<BasMonitorDisplay> mds) {
		for (BasMonitorDisplay md : mds) {
			basMonitorDisplayDao.insertSelective(md);
		}
	}
	
	@Transactional
	public void batchUpdateMonitorDisplayIntervalTime(List<BasMonitorDisplay> mds) {
		for (BasMonitorDisplay md : mds) {
			basMonitorDisplayDao.updateIntevalTime(md);
		}
	}
	
	public int calcPageNo(int total,Integer no ,int pageSize){
		int pageNo = 0;
		if(no != null){
			if (0 == no) { //no为0时，指求最新页
				// 求页码
				if(total <= pageSize){
					pageNo = 1;
				}else{
					if (((total-pageSize) % (pageSize-1)) == 0) {
						pageNo = (total-pageSize) / (pageSize-1) + 1;
					} else {
						pageNo = ((total-pageSize) / (pageSize-1) + 1) + 1 ;
					}
				}
				
				// 求传递给sql语句的pageNo值
				if(1==pageNo){ //防止 total等于0时的情况
					pageNo = 0;
				}else{
					pageNo = (pageNo - 1) * (pageSize - 1)  ;
				}
			}else{
				if(1==no){
					pageNo = 0;
				}else{
					pageNo = (no - 1) * (pageSize - 1) ;
				}
				
			}
		}
		return pageNo;
	}
	
	public Integer getobsDataTotal(MonitorDataFormBean bean,List<String> observeIds){
		String regOptId = bean.getRegOptId();
		Date time = bean.getInTime();
		String inTime = SysUtil.getTimeFormat(time);
        String outerTime = null;
        if(Objects.equals(2, bean.getDocType())){
            DocAnaesPacuRec pacuRec = docAnaesPacuRecDao.selectPacuByRegOptId(regOptId);
            if(null != pacuRec && null != pacuRec.getExitTime()){
                outerTime = DateUtils.formatDateTime(pacuRec.getExitTime());
            }
        }else{
            DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
            if(null != anaesRecord){
                outerTime = anaesRecord.getOutOperRoomTime();
            }
        }
		return basMonitorDisplayDao.searchMonitorDisplayListTotal(regOptId, observeIds, inTime, outerTime, bean.getDocType());
	}
	
	
	public List<BasMonitorDisplay> getMonDataNew(MonitorDataFormBean formBean ,List<String> observeIds){
		Date inTime = formBean.getInTime();
		Date sDate = formBean.getStartTime();
		//Date eDate = formBean.getEndTime();
		String regOptId = formBean.getRegOptId();
		//BaseInfo baseInfo = baseInfoDao.selectByPrimaryKey(patId);
		//Integer bedId = baseInfo.getBedId();
		logger.info("sDate: "+sDate+"---inTime: "+inTime);
		String startTime = SysUtil.getTimeFormat(sDate);
		//String endTime = SysUtil.getTimeFormat(eDate);
		List<BasMonitorDisplay> list = basMonitorDisplayDao.getMonDataNew(regOptId, startTime,observeIds);
		return list;
	}
	
	/**
	 * 术中修改设备配置
	 * @param deviceConfigFormBean
	 */
	@Transactional
	public void updDeviceConfig(DeviceConfigFormBean deviceConfigFormBean) {
		if(deviceConfigFormBean !=null && deviceConfigFormBean.getDeviceConfig()!=null){
		    
		    BasDeviceConfig deviceConfig = deviceConfigFormBean.getDeviceConfig(); 
		    
		    String roomId = StringUtils.isNotBlank(deviceConfig.getRoomId()) ?  deviceConfig.getRoomId() : getCurRoomId(null);
		    String beid = getBeid();
			
			//先删除床旁设备配置表的数据，再做新增处理
			basDeviceConfigDao.deleteDeviceConfig(deviceConfig.getBeid(),deviceConfig.getDeviceId(),roomId);
			basDeviceConfigDao.insert(deviceConfig);
			//将当前手术室正在使用的设备的协议写入到采集配置表中
			List<Device> devices = basDeviceConfigDao.searchDeviceListByRoomId(roomId, beid);
			BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(roomId, beid);
			if (null == devices || devices.size() < 1)
			{
			    collectConfig.setDevicesUsed(null);
			}
			else
			{
			    String devicesUsed = "";
			    for (Device device : devices)
			    {
			        if ("".equals(devicesUsed))
			        {
			            devicesUsed = device.getProtocol();
			        }
			        else
			        {
			            devicesUsed = devicesUsed + "," + device.getProtocol();
			        }
			    }
			    collectConfig.setDevicesUsed(devicesUsed);
			}
			basCollectConfigDao.updateByPrimaryKey(collectConfig);
		}
	}
	
	@Transactional
	public void updateEnterRoomTimegt(EnterRoomFormBean formBean,ResponseValue res) {
		//ResultObj result = new ResultObj();
		
		String docId = formBean.getDocId();
		String regOptId = formBean.getRegOptId();
		Date inTime = formBean.getInTime();
		Date operTime = formBean.getOperTime();
		String time = SysUtil.getTimeFormat(operTime);
		String in_time = SysUtil.getTimeFormat(inTime);
		Integer code = formBean.getCode();
		String anaEventId = formBean.getAnaEventId();
		Integer docType = formBean.getDocType();
		
		// 1、删除原有无用数据
		basMonitorDisplayDao.deleteByOperTime(time,regOptId, formBean.getDocType());
		// 2、记录事件，并修改入室时间
		DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(docId);
		
		EvtAnaesEvent anaesevent = new EvtAnaesEvent();
		
		//anaesevent.setState(anaesRecord.getState());
		anaesevent.setDocId(docId);
		anaesevent.setCode(code);
		anaesevent.setOccurTime(inTime);
		
		List<EvtAnaesEvent> anaeseventList =  evtAnaesEventDao.selectByCodeAndDocId(anaesevent.getDocId(), anaesevent.getCode());
		
        if(StringUtils.isEmpty(anaEventId)){
			if(anaeseventList!=null && anaeseventList.size()>0){
				res.setResultCode("-1");
				res.setResultMessage("入室事件已存在，不能重复添加！");
				//res.setMsg("该事件已存在，不能重复添加！anaEventId="+anaEventId);
				throw new RuntimeException();
			}else{
				List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
					//Date d = DateUtils.getParseTime(sAnaesEventList.get(0).getOccurtime());
					if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime().getTime())){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-2);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						res.setResultCode("-2");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						throw new RuntimeException();
						//return result;
					}
				}
				List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
					
					if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-3);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						//return result;
						res.setResultCode("-3");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						throw new RuntimeException();
					}
				}
				anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
				anaesevent.setOccurTime(inTime);
				evtAnaesEventDao.insert(anaesevent);
			}
		}else{
			
			List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
				
				if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-2);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-2");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					throw new RuntimeException();
					
				}
			}
			List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
				
				
				if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-3);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-3");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					throw new RuntimeException();
				}
			}
			anaesevent.setAnaEventId(anaEventId);
			anaesevent.setOccurTime(inTime);
			evtAnaesEventDao.updateByPrimaryKeySelective(anaesevent);
		}
		
		anaesRecord.setInOperRoomTime(in_time);
		docAnaesRecordDao.updateByPrimaryKey(anaesRecord);
		
		
		BasRegOpt regOpt = basRegOptDao.searchRegOptById(anaesRecord.getRegOptId());
		//如果当前状态不为术中时，则需要记录变更信息
    	if(null!=regOpt && !"04".equals(regOpt.getState())){
    		if(!Objects.equals(inTime, operTime)){
	    		EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
	    		evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
	    		evtModRd.setBeid(getBeid());
	    		evtModRd.setIp(getIP());
	    		evtModRd.setOperId(getUserName());
	    		evtModRd.setEventId(anaesevent.getAnaEventId());
	    		evtModRd.setRegOptId(regOpt.getRegOptId());
	    		evtModRd.setModifyDate(new Date());
	    		evtModRd.setModTable("evt_anaesevent(麻醉事件表)");
	    		evtModRd.setOperModule("麻醉事件");
	    		evtModRd.setModProperty("入室时间");
	    		evtModRd.setOldValue(DateUtils.formatDateTime(operTime));
	    		evtModRd.setNewValue(DateUtils.formatDateTime(inTime));
	    		evtModRd.setRemark("修改麻醉事件");
	    		evtAnaesModifyRecordDao.insert(evtModRd);
    		}
    	}
		
		//result.setResult(0);
		//result.setMsg("修改入室时间成功！");
		res.setResultCode("1");
		res.setResultMessage("修改入室时间成功！");
		//return result;
	}
    
    public ResponseValue saveAnaes(EvtAnaesEvent anaesevent,ResponseValue res){
        List<EvtAnaesEvent> anaeseventList =  evtAnaesEventDao.selectByCodeAndDocId(anaesevent.getDocId(), anaesevent.getCode());
        Integer docType = anaesevent.getDocType();
        if(StringUtils.isEmpty(anaesevent.getAnaEventId())){
            if(anaeseventList!=null && anaeseventList.size()>0){
                res.setResultCode("-1");
                res.setResultMessage("该事件已存在，不能重复添加！");
                return res;
            }else{
                List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
                if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
                    //Date d = DateUtils.getParseTime(sAnaesEventList.get(0).getOccurtime());
                    if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime().getTime())){
                        List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
                        //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
                        List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
                        //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
                        res.setResultCode("-1");
                        res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
                        return res;
                    }
                }
                List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
                if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
                    
                    if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
                        List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
                        //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
                        List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
                        //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
                        res.setResultCode("-1");
                        res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
                        return res;
                    }
                }
                anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
                //anaesevent.setOccurtime(DateUtils.getParseTime(anaesevent.getOccurtime()).getTime()+"");
                
                
				/**
				 * 2017-10-30沈阳本溪
				 * 将修改痕迹保存到表中
				 */
				// 获取麻醉记录单
				DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(anaesevent.getDocId());

				BasRegOpt regOpt = basRegOptDao.searchRegOptById(anaesRecord.getRegOptId());
				//如果当前状态不为术中时，则需要记录变更信息
		    	if(null!=regOpt && !"04".equals(regOpt.getState())){
		    		
		    		List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode() + "");
		    		//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode() + "", getBeid());
		    		EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
		    		evtModRd.setBeid(getBeid());
		    		evtModRd.setIp(getIP());
		    		evtModRd.setOperId(getUserName());
		    		evtModRd.setEventId(anaesevent.getAnaEventId());
		    		evtModRd.setRegOptId(regOpt.getRegOptId());
		    		evtModRd.setModifyDate(new Date());
		    		evtModRd.setModTable("evt_anaesevent(麻醉事件表)");
		    		evtModRd.setOperModule("麻醉事件");
		    		evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
		    		evtModRd.setModProperty(s1.get(0).getCodeName());
		    		evtModRd.setNewValue(DateUtils.formatDateTime(anaesevent.getOccurTime()));
		    		evtModRd.setRemark("新增");
		    		evtAnaesModifyRecordDao.insert(evtModRd);
		    	}
                
                
                evtAnaesEventDao.insert(anaesevent);
            }
        }else{
            List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
            if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
                
                if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime()).getTime()){
                    List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
                    //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
                    List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
                    //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
                    res.setResultCode("-1");
                    res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
                    return res;
                }
            }
            List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
            if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
                
                
                if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
                    List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
                    //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
                    List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
                    //basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
                    res.setResultCode("-1");
                    res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
                    return res;
                }
            }
            
            /**
			 * 2017-10-30沈阳本溪
			 * 将修改痕迹保存到表中
			 */
			// 获取麻醉记录单
			DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(anaesevent.getDocId());

			BasRegOpt regOpt = basRegOptDao.searchRegOptById(anaesRecord.getRegOptId());
			//如果当前状态不为术中时，则需要记录变更信息
	    	if(null!=regOpt && !"04".equals(regOpt.getState())){
	    		if(!Objects.equals(anaeseventList.get(0).getOccurTime(), anaesevent.getOccurTime())){
	    			List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode() + "");
	    			//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode() + "", getBeid());
		    		EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
		    		evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
		    		evtModRd.setBeid(getBeid());
		    		evtModRd.setIp(getIP());
		    		evtModRd.setOperId(getUserName());
		    		evtModRd.setEventId(anaesevent.getAnaEventId());
		    		evtModRd.setRegOptId(regOpt.getRegOptId());
		    		evtModRd.setModifyDate(new Date());
		    		evtModRd.setModTable("evt_anaesevent(麻醉事件表)");
		    		evtModRd.setOperModule("麻醉事件");
		    		evtModRd.setModProperty(s1.get(0).getCodeName());
		    		evtModRd.setOldValue(DateUtils.formatDateTime(anaeseventList.get(0).getOccurTime()));
		    		evtModRd.setNewValue(DateUtils.formatDateTime(anaesevent.getOccurTime()));
		    		evtModRd.setRemark("修改");
		    		evtAnaesModifyRecordDao.insert(evtModRd);
	    		}
	    	}
            
            //anaesevent.setOccurtime(DateUtils.getParseTime(anaesevent.getOccurtime()).getTime()+"");
            evtAnaesEventDao.updateByPrimaryKey(anaesevent);
        }
        return res;
    }
    
    public void saveAnaesPacuRec(DocAnaesPacuRec record) {
        if (StringUtils.isBlank(record.getId())) {
            String bedId = record.getBedId();
            String regOptId = record.getRegOptId();
            if (bedId != null) {
                BasRegionBed regionBed = basRegionBedDao.selectByPrimaryKey(bedId);
                if (regionBed.getStatus() == 1) {
                    logger.error("saveAnaesPacuRec---该床位已被占用请选择其他床位!");
                    return;
                }
                regionBed.setRegOptId(regOptId);
                regionBed.setStatus(1);
                basRegionBedDao.updateByPatId(regionBed);
            }
            DocAnaesPacuRec p = docAnaesPacuRecDao.selectPacuByRegOptId(regOptId);
            if (p == null) {
                record.setId(GenerateSequenceUtil.generateSequenceNo());
                record.setProcessState("NO_END");
                record.setAnabioticState(0);
                docAnaesPacuRecDao.insertSelective(record);
            }
        } else {
            // 当leaveTo不为空则代表患者出复苏室，则需要将床位状态改成有效
            if (StringUtils.isNotBlank(record.getLeaveTo() + "")) {
                BasRegionBed regionBed = basRegionBedDao.selectByPrimaryKey(record.getBedId());
                if(null != regionBed){
	                regionBed.setStatus(0);
	                regionBed.setRegOptId("");
	                basRegionBedDao.updateByPrimaryKey(regionBed);
                }
                record.setProcessState("END");
                record.setAnabioticState(2);
                basRegOptDao.updateState(record.getRegOptId(), "06");
            } else {
                // basRegOptDao.updateState(record.getRegOptId(), "05");
                record.setAnabioticState(1);
            }
            docAnaesPacuRecDao.updateByPrimaryKeySelective(record);
        }
    }
    
    public void changeAnaesRecordState(DocAnaesRecord anaesRecord) {

        logger.info("anaesEventService-----begin changeAnaesRecordState");

        anaesRecord.setProcessState("END");
        //Controller controller = controllerDao.getControllerById(anaesRecord.getRegOptId());
        // 如果状态没发生改变，直接修改数据

        // 如果手术体位发生改变，则需要记录到变更表中
        if (StringUtils.isNotBlank(anaesRecord.getOptBody()) && StringUtils.isNotBlank(anaesRecord.getOptBody())) {
            if (!anaesRecord.getOptBody().equals(anaesRecord.getOptBody())) {
                EvtOperBodyEvent operBodyEvent = new EvtOperBodyEvent();
                operBodyEvent.setDocId(anaesRecord.getAnaRecordId());
                operBodyEvent.setOptBody(anaesRecord.getOptBody());
                //operBodyEvent.setState(controller.getState());
                operBodyEvent.setStartTime(new Date());
                operBodyEvent.setOptBodyEventId(GenerateSequenceUtil.generateSequenceNo());
                operBodyEvent.setOptBodyOld(anaesRecord.getOptBody());
                evtOperBodyEventDao.insert(operBodyEvent);
            }
        }
        docAnaesRecordDao.updateByPrimaryKeySelective(anaesRecord);

        logger.info("anaesEventService-----end changeAnaesRecordState");
    }
    
    /**
     * 
     * @param formBean
     */
    @Transactional
    public void updateEndTime(EndOperationFormBean formBean,ResponseValue res) {
        EvtAnaesEvent anaesevent = formBean.getAnaesevent();
        String docId = anaesevent.getDocId();
        String regOptId = formBean.getRegOptId();
        Integer code = anaesevent.getCode();
        String leaveTo = anaesevent.getLeaveTo();
        //String anaEventId = anaesevent.getAnaEventId();
        anaesevent.setDocType(formBean.getDocType());
        
        // 麻醉事件处理
        ResponseValue resp = this.saveAnaes(anaesevent, res);
        if(!resp.getResultCode().equals("1")){
            return;
        }

        //麻醉记录单处理
        DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(docId);
        BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
        String bedStr = "";
        if(StringUtils.isNotBlank(regOpt.getBed())){
            bedStr = regOpt.getBed()+ "床的";
        }
        //将麻醉记录单表中的去向、出室时间进行修改，并将状态修改成为术后状态
        if(EvtAnaesEventService.OUT_ROOM.equals(code)){
            logger.info("---进入---手术结束------ENDOPERATION----出室去向="+leaveTo);
            Controller controller = controllerDao.getControllerById(regOptId);
            //当麻醉事件提交数据为出室时，需要将控制表的状态改成POSTOPERATIVE 术后
            if(null != controller){
                //南华版本，增加去pacu的处理
                if(("2").equals(leaveTo) &&(!controller.getState().equals(OperationState.POSTOPERATIVE))){
                    controller.setPreviousState(OperationState.SURGERY);
                    controller.setState(OperationState.RESUSCITATION);
                    //存入pacu
                    DocAnaesPacuRec p = docAnaesPacuRecDao.selectPacuByRegOptId(controller.getRegOptId());
                    if(p == null){
                        p = new DocAnaesPacuRec();
                        p.setRegOptId(controller.getRegOptId());
                    }
                    /**
                     * 2018-06-06
                     * 邵阳中心医院需要考虑从复苏室再次手术后，进入复苏室。所以增加复苏记录单状态判断
                     */
                    if(null != p && "END".equals(p.getProcessState()) && Objects.equals(2, p.getAnabioticState())){
                    	
                    	p.setAnabioticState(0);
                    	p.setProcessState("NO_END");
                    	p.setExitTime(null);
                    	p.setBedId("");
                    	p.setLeaveTo(null);
                    	docAnaesPacuRecDao.updateByPrimaryKey(p);
                    	
                    	List<EvtAnaesEvent> evtList = evtAnaesEventDao.selectByCodeAndDocId(anaesRecord.getAnaRecordId(), EvtAnaesEventService.OUT_ROOM);// 出复苏室事件
                    	//删除原出复苏室事件
                    	if(null != evtList && evtList.size()>0){
                    		for (EvtAnaesEvent evtAnaesEvent : evtList) {
								if(Objects.equals(2, evtAnaesEvent.getDocType())){
									evtAnaesEventDao.deleteByPrimaryKey(evtAnaesEvent.getAnaEventId());
								}
							}
                    	}
                    	
                    	//添加一条再次入复苏室开始事件
//                    	EvtAnaesEvent againEvent = new EvtAnaesEvent();
//                    	againEvent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
//                    	againEvent.setCode(41);//再次入复苏室事件
//                    	againEvent.setDocId(anaesRecord.getAnaRecordId());
//                    	againEvent.setDocType(2);
//                    	againEvent.setOccurTime(new Date());
//                    	evtAnaesEventDao.insertSelective(againEvent);
                    }else{
                    	saveAnaesPacuRec(p);	
                    }
                    
                }else if (!controller.getState().equals(OperationState.RESUSCITATION)){
                    controller.setPreviousState(OperationState.SURGERY);
                    controller.setState(OperationState.POSTOPERATIVE);
                }
                
                controllerDao.update(controller);
                
                //当状态从术中转术后时，将事件表的数据备份
                anaesRecord.setOutOperRoomTime(DateUtils.formatLongTime(anaesevent.getOccurTime().getTime()));
                
                if(StringUtils.isBlank(anaesRecord.getLeaveTo())){
                    anaesRecord.setLeaveTo(leaveTo);
                }
                anaesRecord.setProcessState("END");
                //anaesRecord.setPostOperState(Integer.parseInt(controller.getState()));
                docAnaesRecordDao.updateByPrimaryKey(anaesRecord); 
            }
            
            //将消息推送到手术室大屏   结束手术
            List<SysCodeFormbean> ls = basDictItemDao.searchSysCodeByGroupIdAndCodeValue("leave_to", anaesevent.getLeaveTo(), getBeid());
            String leaveToName = ls.size()>0?ls.get(0).getCodeName():"";
            WebSocketHandler.sentMessageToAllUser(regOpt.getDeptName()+regOpt.getRegionName()+bedStr+regOpt.getName()+"手术已结束,去往"+leaveToName);
        }else if(EvtAnaesEventService.CANCEL_OPER.equals(code)){
            logger.info("---进入---手术取消------CANCEL_OPER----");
            Controller controller = controllerDao.getControllerById(regOptId);
            if(null != controller){
                if(controller.getState().equals(OperationState.SURGERY)){
                    controller.setState(OperationState.CANCEL);
                    controller.setPreviousState(OperationState.SURGERY);
                    controllerDao.update(controller);
                }
            }
            
            if(StringUtils.isNoneBlank(formBean.getReasons())){
                BasRegOpt bro = new BasRegOpt();
                bro.setReasons(formBean.getReasons());
                bro.setRegOptId(regOpt.getRegOptId());
                basRegOptDao.updateByPrimaryKeySelective(bro);
            }
            
            
            if(StringUtils.isNotEmpty(leaveTo)){
                anaesRecord.setLeaveTo(leaveTo);
            }else{
                anaesRecord.setLeaveTo("1");//如果前端传递为空，则默认回病房
            }
            anaesRecord.setOutOperRoomTime(DateUtils.formatLongTime(anaesevent.getOccurTime().getTime()));
            anaesRecord.setProcessState("END");
            //anaesRecord.setPostOperState(Integer.parseInt(OperationState.CANCEL));//置为取消状态
            docAnaesRecordDao.updateByPrimaryKey(anaesRecord);
            
            List<SysCodeFormbean> ls = basDictItemDao.searchSysCodeByGroupIdAndCodeValue("leave_to", anaesRecord.getLeaveTo(), getBeid());
            String leaveToName = ls.size()>0?ls.get(0).getCodeName():"";
            WebSocketHandler.sentMessageToAllUser(regOpt.getDeptName()+regOpt.getRegionName()+bedStr+regOpt.getName()+"手术已取消,去往"+leaveToName);
        }
        
        Date endTime = anaesevent.getOccurTime(); //获取到页面传过来的结束时间
        Integer docType = formBean.getDocType();
        Date dbEndTime = basMonitorDisplayDao.findEndTime(regOptId, docType); //获取数据库的
        BasMonitorDisplay md = basMonitorDisplayDao.findMonitorDisplayByInTimeLimit1(regOptId, dbEndTime, docType); //获取最后一个正常数据点
        if(null != dbEndTime){
            long pageEndTimeLong = endTime.getTime();
            long dbEndTimeLong = dbEndTime.getTime();
            if(pageEndTimeLong > dbEndTimeLong){ // 页面传过来的数据  大于 数据库最后一个点的时间
                if(null != md ){
                    Integer freq = md.getFreq();
                    List<BasMonitorDisplay> mds = null;
                    BasMonitorDisplay mDisplay = null;
                    List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(getBeid(),getRoomIdByDocType(regOptId, docType));
                    observes = removeSameObserve(observes, docType);
                    
                    long time = dbEndTimeLong + freq*1000;
                    for(;time <= pageEndTimeLong;){
                        // 新增数据点
                        mds = new ArrayList<BasMonitorDisplay>();
                        Timestamp tt = SysUtil.getCurrentTimeStamp(new Date(time));
                        for (Observe observe : observes) {
                            mDisplay = new BasMonitorDisplay();
                            BeanUtils.copyProperties(observe, mDisplay);
                            mDisplay.setFreq(freq);
                            mDisplay.setValue(null); // 设值为0.0f
                            mDisplay.setId(GenerateSequenceUtil.generateSequenceNo());
                            mDisplay.setObserveName(observe.getName());
                            // 设置新增时间
                            mDisplay.setTime(tt);
                            mDisplay.setRegOptId(regOptId);// 设置regOptId
                            mDisplay.setIntervalTime(freq); //设置间隔时间
                            mDisplay.setAmendFlag(3);//3 ： 人为添加  
                            mDisplay.setOuterFlag(1);//结束手术，数据添加
                            mDisplay.setDocType(docType);
                            mds.add(mDisplay);
                        }
                        
                        if (null != mds && mds.size() > 0) {
                            int count = basMonitorDisplayDao.searchMonitorDisplayByTime(regOptId, SysUtil.getTimeFormat(new Date(time)), docType);//查询数据库是否已存在
                            if(count == 0){ //当没有数据的时候，才添加
                                for (BasMonitorDisplay monitorDisplay : mds) {
                                    basMonitorDisplayDao.insertSelective(monitorDisplay);
                                }
                            }
                        }
                        time += freq*1000;
                    }
                }
                
            }else{ // 页面传过来的时间  小于等于  数据库最后一个点的时间
                //删除掉无用数据
                basMonitorDisplayDao.deleteByEndTime(SysUtil.getTimeFormat(dbEndTime), regOptId, docType);
            }
            
            //出室时，将采集配置表中手术Id清除掉
            BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(getCurRoomId(regOptId), getBeid());
            collectConfig.setPatientId(null);
            basCollectConfigDao.updateByPrimaryKey(collectConfig);
        }
    }
    
    
	public BasMonitorDisplay findMonitorDisplayByInTimeLimit1(String regOptId,Date time, Integer docType){
		return basMonitorDisplayDao.findMonitorDisplayByInTimeLimit1(regOptId, time, docType);
	}
	
	public BasMonitorDisplay findMonitorDisplaybyTime(String regOptId, String time, Integer docType){
		return basMonitorDisplayDao.findMonitorDisplaybyTime(regOptId, time, docType);
	}
	
	@Transactional
	public void updEnterRoomTimelt(EnterRoomFormBean formBean,ResponseValue res) {
		//ResultObj result = new ResultObj();
		String docId = formBean.getDocId();
		String regOptId = formBean.getRegOptId();
		Date inTime = formBean.getInTime();
		Date operTime = formBean.getOperTime();
		String time = SysUtil.getTimeFormat(operTime);
		String in_time = SysUtil.getTimeFormat(inTime);
		Integer code = formBean.getCode();
		String anaEventId = formBean.getAnaEventId();
		Integer docType = formBean.getDocType();
		
		// 取beid
		String beid = formBean.getBeid();
		String roomId = getRoomIdByDocType(regOptId, docType);
		if(StringUtils.isBlank(beid)){
			beid = getBeid();
			formBean.setBeid(beid);
		}
		// 1、删除原有无用数据
        basMonitorDisplayDao.deleteByOperTime(time,regOptId, docType);
		
		//2、根据当前手术时间往前推freq的值，存入b_monitor_display表中
		long inTimeLong = inTime.getTime();
		long operTimeLong = operTime.getTime();
		BasMonitorDisplay mDisplay = basMonitorDisplayDao.findMonitorDisplaybyTime(regOptId, time, docType);
		
		if(null != mDisplay){
			Integer freq = mDisplay.getFreq();
			logger.info("inTimeLong=="+inTimeLong+",operTimeLong=="+operTimeLong+",freq=="+freq);
			operTimeLong -= freq*1000; //防止operTime的数据再次插入
			List<BasMonitorDisplay> mds = null;
			BasMonitorDisplay md = null;
			List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid,roomId);
			observes = removeSameObserve(observes, docType);
			for(;operTimeLong>=inTimeLong;){
				
				mds = new ArrayList<BasMonitorDisplay>();
				Timestamp tt = SysUtil.getCurrentTimeStamp(new Date(operTimeLong));
				for (Observe observe : observes) {
					md = new BasMonitorDisplay();
					BeanUtils.copyProperties(observe, md);
					md.setFreq(freq);
					md.setValue(null); // 设值为0.0f
					md.setId(GenerateSequenceUtil.generateSequenceNo());
					md.setObserveName(observe.getName());
					// 设置新增时间
					md.setTime(tt);
					md.setRegOptId(regOptId);// 设置regOptId
					md.setIntervalTime(freq); //设置间隔时间
					md.setDocType(docType);
					mds.add(md);
				}
				
				if (null != mds && mds.size() > 0) {
					for (BasMonitorDisplay monitorDisplay : mds) {
						basMonitorDisplayDao.insertSelective(monitorDisplay);
					}
				}
				
				operTimeLong -= freq*1000;
			}
		}
		
		//3、记录事件，并修改入室时间
		DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(docId);
		
		EvtAnaesEvent anaesevent = new EvtAnaesEvent();
		
		//anaesevent.setState(anaesRecord.getState());
		anaesevent.setDocId(docId);
		anaesevent.setCode(code);
		anaesevent.setOccurTime(inTime);
		
		List<EvtAnaesEvent> anaeseventList =  evtAnaesEventDao.selectByCodeAndDocId(anaesevent.getDocId(), anaesevent.getCode());
		
		if(StringUtils.isEmpty(anaEventId)){
			if(anaeseventList!=null && anaeseventList.size()>0){
				//result.setResult(-1);
				//result.setMsg("该事件已存在，不能重复添加！anaEventId="+anaEventId);
				//return "该事件已存在，不能重复添加！anaEventId="+anaEventId;
				//return result;
				res.setResultCode("-1");
				res.setResultMessage("入室事件已存在，不能重复添加！");
				return;
			}else{
				List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
					//Date d = DateUtils.getParseTime(sAnaesEventList.get(0).getOccurtime());
					if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime().getTime())){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-2);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						//return result;
						res.setResultCode("-2");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						return;
					}
				}
				List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
					
					if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-3);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						//return result;
						res.setResultCode("-3");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						return ;
					}
				}
				anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
				anaesevent.setOccurTime(inTime);
				evtAnaesEventDao.insert(anaesevent);
			}
		}else{
			
			List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
				
				if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-2);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-2");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					return ;
				}
			}
			List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
				
				
				if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-3);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-3");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					return;
				}
			}
			anaesevent.setAnaEventId(anaEventId);
			anaesevent.setOccurTime(inTime);
			evtAnaesEventDao.updateByPrimaryKeySelective(anaesevent);
		}
		
		anaesRecord.setInOperRoomTime(in_time);
		docAnaesRecordDao.updateByPrimaryKey(anaesRecord);
		
		
		BasRegOpt regOpt = basRegOptDao.searchRegOptById(anaesRecord.getRegOptId());
		//如果当前状态不为术中时，则需要记录变更信息
    	if(null!=regOpt && !"04".equals(regOpt.getState())){
    		if(!Objects.equals(inTime, operTime)){
	    		EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
	    		evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
	    		evtModRd.setBeid(getBeid());
	    		evtModRd.setIp(getIP());
	    		evtModRd.setOperId(getUserName());
	    		evtModRd.setEventId(anaesevent.getAnaEventId());
	    		evtModRd.setRegOptId(regOpt.getRegOptId());
	    		evtModRd.setModifyDate(new Date());
	    		evtModRd.setModTable("evt_anaesevent(麻醉事件表)");
	    		evtModRd.setOperModule("麻醉事件");
	    		evtModRd.setModProperty("入室时间");
	    		evtModRd.setOldValue(DateUtils.formatDateTime(operTime));
	    		evtModRd.setNewValue(DateUtils.formatDateTime(inTime));
	    		evtModRd.setRemark("修改");
	    		evtAnaesModifyRecordDao.insert(evtModRd);
    		}
    	}
		
		
		//result.setResult(0);
		//result.setMsg("修改入室时间成功！");
		//return result;
		res.setResultCode("1");
		res.setResultMessage("修改入室时间成功！");
	}
	
	@Transactional
	public void batchHandleObsDat(List<MonitorDisplayChangeFormBean> monitors) {
		if(null != monitors && monitors.size()>0){
			for (MonitorDisplayChangeFormBean changeBean : monitors) {
				
				String id = changeBean.getId();
				Date time = changeBean.getTime();
				String regOptId = changeBean.getDocId();
				String observeId = changeBean.getObserveId();
				Float value = changeBean.getValue();
				String oldValue = "";
				String newId = "";
				
				//String searchtime = "";
				//if (null != time) {
				//	searchtime = SysUtil.getTimeFormat(time);
				//}
				//判断id是否有值
				if(StringUtils.isNotBlank(id)){//不是null,或者不是空字符串，则为修改
					BasMonitorDisplay monitorDisplay = basMonitorDisplayDao.selectById(id);
					BaseInfoQuery baseQuery = new BaseInfoQuery();
					baseQuery.setRegOptId(regOptId);
					baseQuery.setEventId(observeId);
					baseQuery.setBeid(getBeid());
					
					//从数据库中查询最大最小值
					BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean = basAnaesMonitorConfigDao.getAnaesMonitorConfigEventId(baseQuery);
					if(null != anaesMonitorConfigFormBean){
						int state = -2;
						Float max = anaesMonitorConfigFormBean.getMax();
						Float min = anaesMonitorConfigFormBean.getMin();
						if(null != value){
							if(value.floatValue() > max.floatValue()){
								state = 1; 
							}else if(value.floatValue() < min.floatValue()){
								state = -1;
							}else{
								state = 0;
							}
							changeBean.setState(state);
						}
					}
					if (null != monitorDisplay) {
						
						oldValue = monitorDisplay.getValue()+"";
						
					    //插入修改历史表
					    BasMonitorDisplayChangeHis observeHis = new BasMonitorDisplayChangeHis();
					    observeHis.setObserveDataChange(monitorDisplay, value,changeBean.getMemo());
					    observeHis.setId(GenerateSequenceUtil.generateSequenceNo());
					    basMonitorDisplayChangeHisDao.insert(observeHis);
					    
						BeanUtils.copyProperties(changeBean, monitorDisplay);
						monitorDisplay.setAmendFlag(2); //数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
						basMonitorDisplayDao.updateByPrimaryKeySelective(monitorDisplay); //修改monitorDisplay数据
					}

					// 第二步查看b_observe_data是否有值，如果有就修改
					/*ObserveData observeData = observeDataDao.getUniqObserveData(regOptId, searchtime, observeId);
					if (null != observeData) {
						observeData.setValue(value);
						if(null != changeBean.getState()){
							observeData.setState(changeBean.getState());
						}
						observeDataDao.updateValue(observeData);
					}*/
					
				}else{//可能为新增，也可能为修改，查询b_monitor_display是否有对应记录，如果有，则修改，如果无，则修改；
					BasMonitorDisplay md = basMonitorDisplayDao.getUniqMonitorDisplay(regOptId, time, observeId);
					if(null != md){ //修改b_monitor_display
						
						BaseInfoQuery baseQuery = new BaseInfoQuery();
						baseQuery.setRegOptId(regOptId);
						baseQuery.setEventId(observeId);
						baseQuery.setBeid(getBeid());
						
						oldValue = md.getValue()+"";
						
						//从数据库中查询最大最小值
						BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean = basAnaesMonitorConfigDao.getAnaesMonitorConfigEventId(baseQuery);
						if(null != anaesMonitorConfigFormBean){
							int state = -2;
							Float max = anaesMonitorConfigFormBean.getMax();
							Float min = anaesMonitorConfigFormBean.getMin();
							if(null != value){
								if(value.floatValue() > max.floatValue()){
									state = 1; 
								}else if(value.floatValue() < min.floatValue()){
									state = -1;
								}else{
									state = 0;
								}
								changeBean.setState(state);
							}
						}
						//插入修改历史表
						BasMonitorDisplayChangeHis observeHis = new BasMonitorDisplayChangeHis();
						observeHis.setObserveDataChange(md, value,changeBean.getMemo());
						observeHis.setId(GenerateSequenceUtil.generateSequenceNo());
						basMonitorDisplayChangeHisDao.insert(observeHis);
						
						BeanUtils.copyProperties(changeBean, md);
						md.setAmendFlag(2); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
						basMonitorDisplayDao.updateByPrimaryKeySelective(md); //修改monitorDisplay数据
						
						
						/*ObserveData observeData = observeDataDao.getUniqObserveData(regOptId, searchtime, observeId);
						if (null != observeData) {
							observeData.setValue(value);
							if(null != changeBean.getState()){
								observeData.setState(changeBean.getState());
							}
							observeDataDao.updateValue(observeData);
						}*/
					}else{//新增b_monitor_display
						newId = GenerateSequenceUtil.generateSequenceNo();
						changeBean.setId(newId);
						
						BaseInfoQuery baseQuery = new BaseInfoQuery();
						baseQuery.setRegOptId(regOptId);
						baseQuery.setEventId(observeId);
						baseQuery.setBeid(getBeid());
						
						BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean = basAnaesMonitorConfigDao.getAnaesMonitorConfigEventId(baseQuery);
						if(null != anaesMonitorConfigFormBean){
							int state = -2;
							Float max = anaesMonitorConfigFormBean.getMax();
							Float min = anaesMonitorConfigFormBean.getMin();
							if(null != value){
								if(value.floatValue() > max.floatValue()){
									state = 1; 
								}else if(value.floatValue() < min.floatValue()){
									state = -1;
								}else{
									state = 0;
								}
								changeBean.setState(state);
							}
							changeBean.setObserveName(anaesMonitorConfigFormBean.getEventName());
							changeBean.setIcon(anaesMonitorConfigFormBean.getIconId());
							changeBean.setColor(anaesMonitorConfigFormBean.getColor());
							changeBean.setPosition(anaesMonitorConfigFormBean.getPosition());
						}
						
						//根据observeId+docId+time来查询b_observe_data表中是否有记录
						/*ObserveData observeData = observeDataDao.getUniqObserveData(regOptId,searchtime,observeId);
						if(null != observeData){ // 修改
							observeData.setValue(changeBean.getValue());
							observeData.setState(changeBean.getState());
							observeDataDao.updateValue(observeData);
						}else{//新增
							observeData = new ObserveData();
							BeanUtils.copyProperties(changeBean, observeData);
							observeData.setTime(new Timestamp(time.getTime()));//设置时间
							observeDataDao.insert(observeData);
						}*/
						BasMonitorDisplay monitorDisplay = new BasMonitorDisplay();
						BeanUtils.copyProperties(changeBean, monitorDisplay);
						monitorDisplay.setAmendFlag(3); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
						basMonitorDisplayDao.insertSelective(monitorDisplay);
					}
				}
				
				/**
				 * 2017-10-30沈阳本溪
				 * 将修改痕迹保存到表中
				 */
				String remark = "修改";
				if (StringUtils.isNotBlank(id)){
					newId = id;
				}else{
					remark = "新增";
				}
				BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
		        //如果当前状态不为术中时，则需要记录变更信息
		    	if(null!=regOpt && !"04".equals(regOpt.getState())){
		    		if(!Objects.equals(oldValue, value)){
			    		EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
			    		evtModRd.setBeid(getBeid());
			    		evtModRd.setIp(getIP());
			    		evtModRd.setOperId(getUserName());
			    		evtModRd.setEventId(newId);
			    		evtModRd.setRegOptId(regOptId);
			    		evtModRd.setModifyDate(new Date());
			    		evtModRd.setModTable("bas_monitor_display(术中监测表)");
			    		evtModRd.setOperModule("术中监测");
			    		evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
			    		evtModRd.setModProperty(changeBean.getObserveId()+":"+changeBean.getObserveName());
			    		evtModRd.setOldValue(oldValue==null?"":oldValue+"");
			    		evtModRd.setNewValue(value==null?"":value+"");
			    		evtModRd.setRemark(remark);
			    		evtAnaesModifyRecordDao.insert(evtModRd);
		    		}
		    	}
			}
		}
	}
	
	@Transactional
    public void getIntervalObsData(IntervalDataFormBean bean) {
        List<Date> times = bean.getTimes();
        String regOptId = bean.getRegOptId();
        Integer freq = bean.getFreq();
        Integer docType = bean.getDocType(); 
        // 取beid
        String beid = bean.getBeid();
        String roomId = getRoomIdByDocType(regOptId, docType);
        if(StringUtils.isBlank(beid)){
            beid = getBeid();
            bean.setBeid(beid);
        }
        logger.info("------getIntervalObsData-------regOptId="+regOptId+",freq="+freq);
        if(null != times && times.size()>0){
            List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid, roomId);
            observes = removeSameObserve(observes, docType);
            List<BasMonitorDisplay> mds = null;
            BasMonitorDisplay md = null;
            for (int i = 0; i < times.size(); i++) {
                //Timestamp tt = SysUtil.getTimestamp(times.get(i));
                Date t = times.get(i);
                String timeFormat = SysUtil.getTimeFormat(t);
                Date tt = SysUtil.getDate(timeFormat);
                mds = new ArrayList<BasMonitorDisplay>();
                for (Observe observe : observes) {
                    md = new BasMonitorDisplay();
                    BeanUtils.copyProperties(observe, md);
                    md.setFreq(freq);
                    
                    md.setValue(null); // 设值为0.0f
                    if (MyConstants.O2_EVENT_ID.equals(md.getObserveId()))
                    {
                        //找到前一条O2监测项的值
                        BasMonitorDisplay o2MonitorDisplay = basMonitorDisplayDao.findLastestedDataByObserveId(regOptId,MyConstants.O2_EVENT_ID, docType);
                        if(null!=o2MonitorDisplay){
                        	md.setValue(o2MonitorDisplay.getValue());
                        }
                    }
                    
                    md.setId(GenerateSequenceUtil.generateSequenceNo());
                    md.setObserveName(observe.getName());
                    // 设置新增时间
                    md.setTime(tt);
                    md.setRegOptId(regOptId);// 设置regOptId
                    md.setIntervalTime(freq); // 设置间隔时间
                    md.setAmendFlag(1); //程序修正 
                    md.setDocType(docType);
                    mds.add(md);
                }

                if (null != mds && mds.size() > 0) {
                    int count = basMonitorDisplayDao.searchMonitorDisplayByTime(regOptId, timeFormat, docType);
                    logger.info("getIntervalObsData-----count=="+count+",timeFormat="+timeFormat);
                    if(count==0){
                        for (BasMonitorDisplay mDisplay : mds) {
                            basMonitorDisplayDao.insertSelective(mDisplay);
                        }
                    }
                }
            }
        }
        
    }
	
	@Transactional
	public void firstSpot(FirstSpotFormBean formBean) {
		Date inTime = formBean.getInTime();
		String operTime = SysUtil.getTimeFormat(inTime);
		String regOptId = formBean.getRegOptId();
		Integer docType = formBean.getDocType();
		
		basRegOptDao.updateOperTime(operTime,regOptId);
		docAnaesRecordDao.updateAnaesInRoomTime(operTime, regOptId);
		// 取beid
		String beid = formBean.getBeid();
		String roomId  = getCurRoomId(regOptId);
		if(StringUtils.isBlank(beid)){
			beid = getBeid();
			formBean.setBeid(beid);
		}
		//将消息推送到手术室大屏
		BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
		String bedStr = StringUtils.isNotBlank(regOpt.getBed())==true?regOpt.getBed()+"床的":"";
		WebSocketHandler.sentMessageToAllUser(regOpt.getDeptName()+regOpt.getRegionName()+bedStr+regOpt.getName()+"开始手术");
		
		DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
		EvtAnaesEvent anaesevent = new EvtAnaesEvent();
		anaesevent.setCode(EvtAnaesEventService.IN_ROOM);
		Date date = SysUtil.getDate(operTime);
		anaesevent.setOccurTime(date);
		anaesevent.setDocType(1);//麻醉记录单事件
		//anaesevent.setState("04");//必须是术中
		if(null != anaesRecord){
			anaesevent.setDocId(anaesRecord.getAnaRecordId());
		}
		anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
		evtAnaesEventDao.insertSelective(anaesevent);
		
		//存入新点
		List<BasMonitorDisplay> mds = new ArrayList<BasMonitorDisplay>();
		logger.error("----firstSpot---inTime="+inTime+",regOptId="+regOptId);
		// 直接存入b_monitor_display中
		List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid,roomId);
		observes = removeSameObserve(observes, docType);
		
		Timestamp tt = SysUtil.getTimestamp(operTime);
		String operModel = MyConstants.OPERATION_MODEL_NORMAL;
    	String anaRecordId = "";
    	if(null!=anaesRecord){
    		anaRecordId = anaesRecord.getAnaRecordId();
        	SearchFormBean searchBean = new SearchFormBean();
        	searchBean.setDocId(anaRecordId);
        	searchBean.setCurrentState("1");
    		List<EvtRescueevent> list = evtRescueeventDao.searchRescueeventList(searchBean);
    		if(list.size()>0){
    			operModel = list.get(0).getModel();
    			logger.info("firstSpot---getCurrentModel---当前的手术模式==="+operModel);
    		}
    		logger.info("firstSpot---getCurrentModel--返回当前的手术模式operModel==="+operModel+",list的size==="+list.size());
    	}else{
    		logger.error("firstSpot---getCurrentModel----根据regOptId返回的doc_id为空，请检查！");
    	}
    	
    	int collTime = 0;
		if(operModel.equals(MyConstants.OPERATION_MODEL_NORMAL)){
			collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid,roomId).getValue());
		}else if (MyConstants.OPERATION_MODEL_SAVE.equals(operModel)) {
			collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_SAVE, beid,roomId).getValue());
		}
		
		for (Observe observe : observes) {
			BasMonitorDisplay md = new BasMonitorDisplay();
			BeanUtils.copyProperties(observe, md);
			md.setFreq(collTime);
			md.setValue(null); // 设值为0.0f
			// md.setValue(0.0f);
			md.setId(GenerateSequenceUtil.generateSequenceNo());
			md.setObserveName(observe.getName());
			// 设置新增时间
			md.setTime(tt);
			md.setRegOptId(regOptId);// 设置regOptId
			md.setIntervalTime(collTime); // 设置间隔时间
			md.setAmendFlag(1); // 程序修正 
			md.setDocType(docType);
			mds.add(md);
		}

		if (null != mds && mds.size() > 0) {
			for (BasMonitorDisplay md : mds) {
				basMonitorDisplayDao.insertSelective(md);
			}
		}
	}
	
	
	
	@Transactional
	public void firstSpotWholePoint(FirstSpotFormBean formBean) {
		Date inTime = formBean.getInTime();
		String operTime = SysUtil.getTimeFormat(inTime);
		String regOptId = formBean.getRegOptId();
		Integer docType = formBean.getDocType();
		
		basRegOptDao.updateOperTime(operTime,regOptId);
		docAnaesRecordDao.updateAnaesInRoomTime(operTime, regOptId);
		// 取beid
		String beid = formBean.getBeid();
		String roomId  = getCurRoomId(regOptId);
		if(StringUtils.isBlank(beid)){
			beid = getBeid();
			formBean.setBeid(beid);
		}
		//将消息推送到手术室大屏
		BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
		String bedStr = StringUtils.isNotBlank(regOpt.getBed())==true?regOpt.getBed()+"床的":"";
		WebSocketHandler.sentMessageToAllUser(regOpt.getDeptName()+regOpt.getRegionName()+bedStr+regOpt.getName()+"开始手术");
		
		DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
		EvtAnaesEvent anaesevent = new EvtAnaesEvent();
		anaesevent.setCode(EvtAnaesEventService.IN_ROOM);
		Date date = SysUtil.getDate(operTime);
		anaesevent.setOccurTime(date);
		anaesevent.setDocType(1);//麻醉记录单事件
		//anaesevent.setState("04");//必须是术中
		if(null != anaesRecord){
			anaesevent.setDocId(anaesRecord.getAnaRecordId());
		}
		anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
		evtAnaesEventDao.insertSelective(anaesevent);
		
		//存入新点
		List<BasMonitorDisplay> mds = new ArrayList<BasMonitorDisplay>();
		
		// 直接存入b_monitor_display中
		List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid,roomId);
		observes = removeSameObserve(observes, docType);
		
		String operModel = MyConstants.OPERATION_MODEL_NORMAL;
    	String anaRecordId = "";
    	if(null!=anaesRecord){
    		anaRecordId = anaesRecord.getAnaRecordId();
        	SearchFormBean searchBean = new SearchFormBean();
        	searchBean.setDocId(anaRecordId);
        	searchBean.setCurrentState("1");
    		List<EvtRescueevent> list = evtRescueeventDao.searchRescueeventList(searchBean);
    		if(list.size()>0){
    			operModel = list.get(0).getModel();
    			logger.info("firstSpot---getCurrentModel---当前的手术模式==="+operModel);
    		}
    		logger.info("firstSpot---getCurrentModel--返回当前的手术模式operModel==="+operModel+",list的size==="+list.size());
    	}else{
    		logger.error("firstSpot---getCurrentModel----根据regOptId返回的doc_id为空，请检查！");
    	}
    	
    	int collTime = 0;
		if(operModel.equals(MyConstants.OPERATION_MODEL_NORMAL)){
			collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid,roomId).getValue());
		}else if (MyConstants.OPERATION_MODEL_SAVE.equals(operModel)) {
			collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_SAVE, beid,roomId).getValue());
		}
		
		Date firstSpotTime = getFirstSpotTime(inTime,collTime,formBean.getFirstSpotFq());
		logger.error("----firstSpotWholePoint---firstSpotTime="+firstSpotTime+",regOptId="+regOptId);
		Timestamp tt = SysUtil.getTimestamp(SysUtil.getTimeFormat(firstSpotTime));
		
		for (Observe observe : observes) {
			BasMonitorDisplay md = new BasMonitorDisplay();
			BeanUtils.copyProperties(observe, md);
			md.setFreq(collTime);
			md.setValue(null); // 设值为0.0f
			// md.setValue(0.0f);
			md.setId(GenerateSequenceUtil.generateSequenceNo());
			md.setObserveName(observe.getName());
			// 设置新增时间
			md.setTime(tt);
			md.setRegOptId(regOptId);// 设置regOptId
			md.setIntervalTime(collTime); // 设置间隔时间
			md.setAmendFlag(1); // 程序修正 
			md.setDocType(docType);
			mds.add(md);
		}

		if (null != mds && mds.size() > 0) {
			for (BasMonitorDisplay md : mds) {
				basMonitorDisplayDao.insertSelective(md);
			}
		}
	}
	
	
	/**
	 * 根据入室时间、间隔时间获取最近一个时间点（整分）做为第一个点的时间（往前、往后）
	 * @param intime
	 * @param freq
	 * @param firstSpotFq
	 * @return
	 */
	public static Date getFirstSpotTime(Date intime,Integer freq,Integer firstSpotFq){
		long time=intime.getTime();
		if(time%(1000*freq)==0){
			return intime;
		}else{
			//间隔分钟
			int freqMinute = freq/60;
			//firstSpotFq==1往前取最近点，否则往后取最近一个点
			if(!Objects.equals(1, firstSpotFq)){
				intime = DateUtils.addMinutes(intime, freqMinute);
			}
			String firstSpotTime = SysUtil.getDateFormat(intime, "yyyy-MM-dd HH");
			int minute = Integer.parseInt(SysUtil.getDateFormat(intime, "mm"));
			int currMinute = minute - minute%freqMinute;
			
			//如果分钟小于10则需要补0
			if(currMinute<10){
				firstSpotTime += ":0"+currMinute+":00";
			}else{
				firstSpotTime += ":"+currMinute+":00";
			}
			return SysUtil.getDate(firstSpotTime);
		}
	}
	
	
	

    private List<Observe> removeSameObserve(List<Observe> observes, Integer docType)
    {
        List<Observe> observeList = new ArrayList<Observe>();
        String eventStr = "";
        String beid = getBeid();
        boolean flag = false;
        for (Observe observe : observes)
        {
            if (MyConstants.O2_EVENT_ID.equals(observe.getObserveId()))
            {
                flag = true;
            }
            
            BasMonitorConfigDefault monitorConfigDefault = basMonitorConfigDefaultDao.selectByEventName(observe.getName(), beid, docType);
            if (null == monitorConfigDefault)
            {
                observeList.add(observe);
            }
            else if (!eventStr.contains(observe.getName()))
            {
                // 如果监测项有重复，则将监测项的id设置为统一的eventid，并且将其他重复的observe移除
                observe.setObserveId(monitorConfigDefault.getEventId());
                eventStr += observe.getName();
                observeList.add(observe);
            }
        }
        
        //没有氧流量采集项，则添加一个
        if (!flag)
        {
            BasMonitorConfig monitorConfig = basMonitorConfigDao.selectByPrimaryKey(MyConstants.O2_EVENT_ID, beid);
            Observe o2 = new Observe();
            o2.setObserveId(MyConstants.O2_EVENT_ID);
            o2.setName(monitorConfig.getEventName());
            o2.setIcon(monitorConfig.getIconId());
            o2.setColor(monitorConfig.getColor());
            o2.setMax(monitorConfig.getMax());
            o2.setMin(monitorConfig.getMin());
            o2.setPosition(monitorConfig.getPosition());
            observeList.add(o2);
        }
        
        
        return observeList;
    }
	
	@Transactional
	public void changeModel(RescueeventFormBean rescueeventFormBean) {
		//1、修改间隔时间
		EvtRescueevent rescueevent = rescueeventFormBean.getRescueevent();
		
		Date time = rescueeventFormBean.getTime();
		//String timeFormat = SysUtil.getTimeFormat(time);
		String regOptId = rescueeventFormBean.getRegOptId();
		String beid = rescueeventFormBean.getBeid();
		String roomId = getCurRoomId(regOptId);
		if (StringUtils.isBlank(beid))
		{
		    beid = getBeid();
		    rescueeventFormBean.setBeid(beid);
		}
		
		String model = rescueevent.getModel();
		int curFreq = 0;
		int freq = 0;
		if(model.equals(MyConstants.OPERATION_MODEL_NORMAL)){
			curFreq = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid,roomId).getValue());
			freq = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_SAVE, beid,roomId).getValue());
		}else if (MyConstants.OPERATION_MODEL_SAVE.equals(model)) {
			curFreq = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_SAVE, beid,roomId).getValue());
			freq = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid,roomId).getValue());
		}
		List<BasMonitorDisplay> mds = basMonitorDisplayDao.findLastestedMonitors(regOptId, rescueeventFormBean.getDocType());
		if(null != mds && mds.size()>0){
			BasMonitorDisplay monitorDisplay = mds.get(0);
			Date d = monitorDisplay.getTime();
			long t1 = time.getTime();
			long t2 = d.getTime();
			int t =  (int)(t1 - t2)/1000;
			logger.info("time="+time+",d="+d+",t1="+t1+",t2="+t2+",t="+t+",curFreq="+curFreq);
			int interval_time = curFreq + t ;
			for (BasMonitorDisplay md : mds) {
				md.setIntervalTime(interval_time);
				basMonitorDisplayDao.updateIntevalTime(md);
			}
		}
		
		BasMonitorFreqChange entity = new BasMonitorFreqChange();
        entity.setId(GenerateSequenceUtil.generateSequenceNo());
        entity.setRegOptId(regOptId);
        entity.setFreq(curFreq);
        entity.setOldFreq(freq);
        entity.setTime(time);
        basMonitorFreqChangeDao.insert(entity);
		
		//2、存入抢救事件
        //DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(rescueevent.getDocId());
//		rescueevent.setState(anaesRecord.getState());
		evtRescueeventDao.updateCurrentState(rescueevent.getDocId(), "0");
		rescueevent.setCurrentState(1);
		rescueevent.setRescueEventId(GenerateSequenceUtil.generateSequenceNo());
		rescueevent.setStartTime(time);
		evtRescueeventDao.insert(rescueevent);
	}
	
	@Transactional
	public void updateFreq(List<BasMonitorConfigFreq> freqList, String model,Date time,String regOptId) {
		String curBeid = getBeid();
		if(null != freqList && freqList.size()>0){
			//logger.info("-----------updateFreq-------model="+model+",time="+time+",regOptId="+regOptId+",size="+freqList.size());
			for (BasMonitorConfigFreq monitorConfigFreq : freqList) {
				String type = monitorConfigFreq.getType();
				String beid = monitorConfigFreq.getBeid();
				String roomId = getCurRoomId(regOptId);
				monitorConfigFreq.setRoomId(roomId);
				if(StringUtils.isBlank(beid)){
					beid = curBeid;
					monitorConfigFreq.setBeid(beid);
				}
				logger.info("-----------updateFreq-------model="+model+",time="+time+",regOptId="+regOptId+",size="+freqList.size()+",type="+type);
				if(type.equals(model)){//传递过来的数据有当前模式下的频率
					String value = monitorConfigFreq.getValue();
					Integer curFreq = Integer.valueOf(value); //页面传过来的freq
					
					int freq = 0;
					if(model.equals(MyConstants.OPERATION_MODEL_NORMAL)){
						freq = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid,roomId).getValue());
					}else if (MyConstants.OPERATION_MODEL_SAVE.equals(model)) {
						freq = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_SAVE, beid,roomId).getValue());
					}
					if(curFreq != freq){ //频率不相等，则发送修改频率的命令到数据处理模块
						logger.info("updateFreq-----curFreq="+curFreq+",数据库freq="+freq);
						
						BasMonitorFreqChange entity = new BasMonitorFreqChange();
                        entity.setId(GenerateSequenceUtil.generateSequenceNo());
                        entity.setRegOptId(regOptId);
                        entity.setFreq(curFreq);
                        entity.setOldFreq(freq);
                        entity.setTime(time);
                        basMonitorFreqChangeDao.insert(entity);
						
						List<BasMonitorDisplay> mds = basMonitorDisplayDao.findLastestedMonitors(regOptId, null);
						if(null != mds && mds.size()>0){
							BasMonitorDisplay monitorDisplay = mds.get(0);
							Date d = monitorDisplay.getTime();
							long t1 = time.getTime();
							long t2 = d.getTime();
							int t =  (int)(t1 - t2)/1000;
							int interval_time = curFreq + t ;
							logger.info("updateFreq-----time="+time+",d="+d+",t1="+t1+",t2="+t2+",t="+t+",interval_time="+interval_time+",freq="+freq);
							for (BasMonitorDisplay md : mds) {
								md.setIntervalTime(interval_time);
								basMonitorDisplayDao.updateIntevalTime(md);
							}
							basMonitorConfigFreqDao.update(monitorConfigFreq); //只有是普通模式的时候，频率值变化了，才会去修改对应freq
							break;
						}
					}
				}else if(model.equals(MyConstants.OPERATION_MODEL_SAVE)&& !type.equals(model)){  //传递过来的模式与当前模式不相等    场景：在抢救模式，修改普通模式的频率值
					logger.info("updateFreq-----type="+type+",当前模式model="+model);
					String value = monitorConfigFreq.getValue();
					Integer curFreq = Integer.valueOf(value); //页面传过来的freq
					int freq = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid,roomId).getValue());//取普通模式的freq
					logger.info("updateFreq-----curFreq="+curFreq+",数据库freq="+freq);
					
					if(curFreq != freq){ //频率不相等，则发送修改频率的命令到数据处理模块  ，即页面传递过来的freq值 != 普通模式现有的freq值
						logger.info("updateFreq-----curFreq="+curFreq+",数据库freq="+freq+",updateFreq值");
						basMonitorConfigFreqDao.update(monitorConfigFreq);
						break;
					}
				}
			}
		}
	}
	
	
	@Transactional
    public String saveMonitorPupil(BasMonitorPupil mp) {
        if(null != mp.getId() && StringUtils.isNotBlank(mp.getId())){
        	/**
    		 * 2017-10-30
    		 * 将修改痕迹保存到表中
    		 */
        	BasRegOpt regOpt = basRegOptDao.searchRegOptById(mp.getRegOptId());
	        //如果当前状态不为术中时，则需要记录变更信息
        	if(null!=regOpt && !"04".equals(regOpt.getState())){
        		BasMonitorPupil oldEvt = basMonitorPupilDao.selectByPrimaryKey(mp.getId());
				CompareObject compare = new CompareObject();
				List<ChangeValueFormbean> changeList = new ArrayList<ChangeValueFormbean>();
				try {
					changeList = compare.getCompareResultByFields(oldEvt, mp);
					if(null!=changeList && changeList.size()>0){
						
						for (ChangeValueFormbean changeValueFormbean : changeList) {
							//排除非表内字段产生的差异，如medTakeWayIdList等
							Map<String,String> hisMap = compare.getColumnListByTableName("bas_monitor_pupil");
							if(hisMap.containsKey(changeValueFormbean.getModProperty())){
								EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
								evtModRd.setBeid(getBeid());
								evtModRd.setIp(getIP());
								evtModRd.setOperId(getUserName());
								evtModRd.setEventId(mp.getId());
								evtModRd.setRegOptId(mp.getRegOptId());
								evtModRd.setModifyDate(new Date());
								evtModRd.setModTable("bas_monitor_pupil(监测事件表)");
								evtModRd.setOperModule("术中监测");
								evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
								evtModRd.setModProperty(compare.getColumnContentByProperty("bas_monitor_pupil", changeValueFormbean.getModProperty()));
								evtModRd.setOldValue(changeValueFormbean.getOldValue());
								evtModRd.setNewValue(changeValueFormbean.getNewValue());
								evtModRd.setRemark("修改");
								evtAnaesModifyRecordDao.insert(evtModRd);
							}
						}
					}
				} catch (Exception e) {
					logger.info("------getCompareResultByFields-----"+Exceptions.getStackTraceAsString(e));
					throw new CustomException(Exceptions.getStackTraceAsString(e));
				}
	        }
        	
            mp.setAmendFlag(2); //人为修正
            basMonitorPupilDao.updateByPrimaryKeySelective(mp);
            return mp.getId();
        }else{
            String id = GenerateSequenceUtil.generateSequenceNo();
            mp.setId(id);
            mp.setAmendFlag(3);//人为添加
            mp.setBeid(getBeid());
            basMonitorPupilDao.insertSelective(mp);
            
            BasRegOpt regOpt = basRegOptDao.searchRegOptById(mp.getRegOptId());
	        //如果当前状态不为术中时，则需要记录变更信息
        	if(null!=regOpt && !"04".equals(regOpt.getState())){
	            EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
				evtModRd.setBeid(getBeid());
				evtModRd.setIp(getIP());
				evtModRd.setOperId(getUserName());
				evtModRd.setEventId(id);
				evtModRd.setRegOptId(mp.getRegOptId());
				evtModRd.setModifyDate(new Date());
				evtModRd.setModTable("bas_monitor_pupil(监测事件表)");
				evtModRd.setOperModule("术中监测");
				evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
				evtModRd.setModProperty("新增监测项(瞳孔)");
				
				StringBuffer buffer = new StringBuffer();
				buffer.append("开始时间:"+DateUtils.formatDateTime(mp.getTime()));
				if(StringUtils.isNotBlank(mp.getLeft())){
					buffer.append("; 左:"+mp.getLeft());
				}
				if(StringUtils.isNotBlank(mp.getRight())){
					buffer.append("; 右:"+mp.getRight());
				}
				if(StringUtils.isNotBlank(mp.getLightReaction())){
					buffer.append("; 对光反射:"+mp.getLightReaction());
				}
				evtModRd.setNewValue(buffer.toString());
				evtModRd.setRemark("新增");
				evtAnaesModifyRecordDao.insert(evtModRd);
        	}
            
            
            return id;
        }
    }
	
	private List<SysCodeFormbean> getAnaesEventType(String code)
    {
        List<SysCodeFormbean> anaesEventTypeList = new ArrayList<SysCodeFormbean>();
        String beid = getBeid();
        anaesEventTypeList = basAnaesEventDao.selectAnaesEventByCode(code, beid);
        return anaesEventTypeList;
    }
	
	
	
	
	
	/*#####################################################PACU复苏单修改出入室时间######################################################################*/
	
	@Transactional
	public void updPacuEnterRoomTimelt(EnterRoomFormBean formBean,ResponseValue res) {
		//ResultObj result = new ResultObj();
		String docId = formBean.getDocId();
		String regOptId = formBean.getRegOptId();
		Date inTime = formBean.getInTime();
		Date operTime = formBean.getOperTime();
		String time = SysUtil.getTimeFormat(operTime);
		//String in_time = SysUtil.getTimeFormat(inTime);
		Integer code = formBean.getCode();
		String anaEventId = formBean.getAnaEventId();
		Integer docType = formBean.getDocType();
		
		// 取beid
		String beid = formBean.getBeid();
		String roomId = getRoomIdByDocType(regOptId, docType);
		if(StringUtils.isBlank(beid)){
			beid = getBeid();
			formBean.setBeid(beid);
		}
		// 1、删除原有无用数据
        basMonitorDisplayDao.deleteByOperTime(time,regOptId, docType);
		
		//2、根据当前手术时间往前推freq的值，存入b_monitor_display表中
		long inTimeLong = inTime.getTime();
		long operTimeLong = operTime.getTime();
		BasMonitorDisplay mDisplay = basMonitorDisplayDao.findMonitorDisplaybyTime(regOptId, time, docType);
		
		if(null != mDisplay){
			Integer freq = mDisplay.getFreq();
			logger.info("inTimeLong=="+inTimeLong+",operTimeLong=="+operTimeLong+",freq=="+freq);
			operTimeLong -= freq*1000; //防止operTime的数据再次插入
			List<BasMonitorDisplay> mds = null;
			BasMonitorDisplay md = null;
			List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid,roomId);
			observes = removeSameObserve(observes, docType);
			for(;operTimeLong>=inTimeLong;){
				
				mds = new ArrayList<BasMonitorDisplay>();
				Timestamp tt = SysUtil.getCurrentTimeStamp(new Date(operTimeLong));
				for (Observe observe : observes) {
					md = new BasMonitorDisplay();
					BeanUtils.copyProperties(observe, md);
					md.setFreq(freq);
					md.setValue(null); // 设值为0.0f
					md.setId(GenerateSequenceUtil.generateSequenceNo());
					md.setObserveName(observe.getName());
					// 设置新增时间
					md.setTime(tt);
					md.setRegOptId(regOptId);// 设置regOptId
					md.setIntervalTime(freq); //设置间隔时间
					md.setDocType(docType);
					mds.add(md);
				}
				
				if (null != mds && mds.size() > 0) {
					for (BasMonitorDisplay monitorDisplay : mds) {
						basMonitorDisplayDao.insertSelective(monitorDisplay);
					}
				}
				
				operTimeLong -= freq*1000;
			}
		}
		
		DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
		
		//3、记录事件，并修改入室时间
		//DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(docId);
		
		EvtAnaesEvent anaesevent = new EvtAnaesEvent();
		
		//anaesevent.setState(anaesRecord.getState());
		anaesevent.setDocId(docId);
		anaesevent.setCode(code);
		anaesevent.setOccurTime(inTime);
		
		List<EvtAnaesEvent> anaeseventList =  evtAnaesEventDao.selectByCodeAndDocId(anaesevent.getDocId(), anaesevent.getCode());
		
		if(StringUtils.isEmpty(anaEventId)){
			if(anaeseventList!=null && anaeseventList.size()>0){
				//result.setResult(-1);
				//result.setMsg("该事件已存在，不能重复添加！anaEventId="+anaEventId);
				//return "该事件已存在，不能重复添加！anaEventId="+anaEventId;
				//return result;
				res.setResultCode("-1");
				res.setResultMessage("入室事件已存在，不能重复添加！");
				return;
			}else{
				List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
					//Date d = DateUtils.getParseTime(sAnaesEventList.get(0).getOccurtime());
					if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime().getTime())){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-2);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						//return result;
						res.setResultCode("-2");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						return;
					}
				}
				List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
					
					if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-3);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						//return result;
						res.setResultCode("-3");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						return ;
					}
				}
				anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
				anaesevent.setOccurTime(inTime);
				evtAnaesEventDao.insert(anaesevent);
			}
		}else{
			
			List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
				
				if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-2);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-2");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					return ;
				}
			}
			List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
				
				
				if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-3);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-3");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					return;
				}
			}
			anaesevent.setAnaEventId(anaEventId);
			anaesevent.setOccurTime(inTime);
			evtAnaesEventDao.updateByPrimaryKeySelective(anaesevent);
		}
		
		anaesPacuRec.setEnterTime(inTime);
		docAnaesPacuRecDao.updateByPrimaryKeySelective(anaesPacuRec);
		//anaesRecord.setInOperRoomTime(in_time);
		//docAnaesRecordDao.updateByPrimaryKey(anaesRecord);
		
		
		BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
		//如果当前状态不为术中时，则需要记录变更信息
    	if(null!=regOpt && !"05".equals(regOpt.getState())){
    		if(!Objects.equals(inTime, operTime)){
	    		EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
	    		evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
	    		evtModRd.setBeid(getBeid());
	    		evtModRd.setIp(getIP());
	    		evtModRd.setOperId(getUserName());
	    		evtModRd.setEventId(anaesevent.getAnaEventId());
	    		evtModRd.setRegOptId(regOpt.getRegOptId());
	    		evtModRd.setModifyDate(new Date());
	    		evtModRd.setModTable("evt_anaesevent(麻醉事件表)");
	    		evtModRd.setOperModule("麻醉事件");
	    		evtModRd.setModProperty("入PACU时间");
	    		evtModRd.setOldValue(DateUtils.formatDateTime(operTime));
	    		evtModRd.setNewValue(DateUtils.formatDateTime(inTime));
	    		evtModRd.setRemark("修改");
	    		evtAnaesModifyRecordDao.insert(evtModRd);
    		}
    	}
		
		
		//result.setResult(0);
		//result.setMsg("修改入室时间成功！");
		//return result;
		res.setResultCode("1");
		res.setResultMessage("修改入室时间成功！");
	}
	
	
	
	@Transactional
	public void updatePacuEnterRoomTimegt(EnterRoomFormBean formBean,ResponseValue res) {
		//ResultObj result = new ResultObj();
		
		String docId = formBean.getDocId();
		String regOptId = formBean.getRegOptId();
		Date inTime = formBean.getInTime();
		Date operTime = formBean.getOperTime();
		String time = SysUtil.getTimeFormat(operTime);
		//String in_time = SysUtil.getTimeFormat(inTime);
		Integer code = formBean.getCode();
		String anaEventId = formBean.getAnaEventId();
		Integer docType = formBean.getDocType();
		
		// 1、删除原有无用数据
        basMonitorDisplayDao.deleteByOperTime(time,regOptId, docType);
		// 2、记录事件，并修改入室时间
		//DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(docId);
		
		DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
		
		
		EvtAnaesEvent anaesevent = new EvtAnaesEvent();
		
		//anaesevent.setState(anaesRecord.getState());
		anaesevent.setDocId(docId);
		anaesevent.setCode(code);
		anaesevent.setOccurTime(inTime);
		
		List<EvtAnaesEvent> anaeseventList =  evtAnaesEventDao.selectByCodeAndDocId(anaesevent.getDocId(), anaesevent.getCode());
		
		if(StringUtils.isEmpty(anaEventId)){
			if(anaeseventList!=null && anaeseventList.size()>0){
				res.setResultCode("-1");
				res.setResultMessage("入室事件已存在，不能重复添加！");
				//res.setMsg("该事件已存在，不能重复添加！anaEventId="+anaEventId);
				throw new RuntimeException();
			}else{
				List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
					//Date d = DateUtils.getParseTime(sAnaesEventList.get(0).getOccurtime());
					if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime().getTime())){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-2);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						res.setResultCode("-2");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
						throw new RuntimeException();
						//return result;
					}
				}
				List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
				if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
					
					if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
						List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
						List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
						//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
						//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
						//result.setResult(-3);
						//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						//return result;
						res.setResultCode("-3");
						res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
						throw new RuntimeException();
					}
				}
				anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
				anaesevent.setOccurTime(inTime);
				evtAnaesEventDao.insert(anaesevent);
			}
		}else{
			
			List<EvtAnaesEvent> sAnaesEventList = evtAnaesEventDao.selectCodeByDESC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList!=null&&sAnaesEventList.size()>0){
				
				if((sAnaesEventList.get(0).getOccurTime().getTime())>(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-2);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-2");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能小于"+s2.get(0).getCodeName()+"时间");
					throw new RuntimeException();
					
				}
			}
			List<EvtAnaesEvent> sAnaesEventList1 = evtAnaesEventDao.selectCodeByASC(anaesevent.getDocId(), anaesevent.getCode(), docType);
			if(sAnaesEventList1!=null&&sAnaesEventList1.size()>0){
				
				
				if((sAnaesEventList1.get(0).getOccurTime().getTime())<(anaesevent.getOccurTime()).getTime()){
					List<SysCodeFormbean> s1 = getAnaesEventType(anaesevent.getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", anaesevent.getCode()+"", getBeid());
					List<SysCodeFormbean> s2 = getAnaesEventType(sAnaesEventList1.get(0).getCode()+"");
					//basSysCodeDao.searchSysCodeByGroupIdAndCodeValue("anaes_event_type", sAnaesEventList1.get(0).getCode()+"", getBeid());
					//return s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间";
					//result.setResult(-3);
					//result.setMsg(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					//return result;
					res.setResultCode("-3");
					res.setResultMessage(s1.get(0).getCodeName()+"时间不能大于"+s2.get(0).getCodeName()+"时间");
					throw new RuntimeException();
				}
			}
			anaesevent.setAnaEventId(anaEventId);
			anaesevent.setOccurTime(inTime);
			evtAnaesEventDao.updateByPrimaryKeySelective(anaesevent);
		}
		
		anaesPacuRec.setEnterTime(inTime);
		docAnaesPacuRecDao.updateByPrimaryKeySelective(anaesPacuRec);
		
		//anaesRecord.setInOperRoomTime(in_time);
		//docAnaesRecordDao.updateByPrimaryKey(anaesRecord);
		
		
		BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
		//如果当前状态不为术中时，则需要记录变更信息
    	if(null!=regOpt && !"05".equals(regOpt.getState())){
    		if(!Objects.equals(inTime, operTime)){
	    		EvtAnaesModifyRecord evtModRd = new EvtAnaesModifyRecord();
	    		evtModRd.setId(GenerateSequenceUtil.generateSequenceNo());
	    		evtModRd.setBeid(getBeid());
	    		evtModRd.setIp(getIP());
	    		evtModRd.setOperId(getUserName());
	    		evtModRd.setEventId(anaesevent.getAnaEventId());
	    		evtModRd.setRegOptId(regOpt.getRegOptId());
	    		evtModRd.setModifyDate(new Date());
	    		evtModRd.setModTable("evt_anaesevent(麻醉事件表)");
	    		evtModRd.setOperModule("麻醉事件");
	    		evtModRd.setModProperty("入PACU时间");
	    		evtModRd.setOldValue(DateUtils.formatDateTime(operTime));
	    		evtModRd.setNewValue(DateUtils.formatDateTime(inTime));
	    		evtModRd.setRemark("修改麻醉事件");
	    		evtAnaesModifyRecordDao.insert(evtModRd);
    		}
    	}
		
		//result.setResult(0);
		//result.setMsg("修改入室时间成功！");
		res.setResultCode("1");
		res.setResultMessage("修改入室时间成功！");
		//return result;
	}
	
	
	
	
	/**
     * 
     * @param formBean
     */
    @Transactional
    public void updatePacuEndTime(EndOperationFormBean formBean,ResponseValue res) {
        EvtAnaesEvent anaesevent = formBean.getAnaesevent();
        //String docId = anaesevent.getDocId();
        String regOptId = formBean.getRegOptId();
        Integer code = anaesevent.getCode();
        String leaveTo = anaesevent.getLeaveTo();
        //String anaEventId = anaesevent.getAnaEventId();
        
        // 麻醉事件处理
        ResponseValue resp = this.saveAnaes(anaesevent, res);
        if(!resp.getResultCode().equals("1")){
            return;
        }

        //麻醉记录单处理
        //DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordById(docId);
        
        
        DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
        
//        BasRegOpt regOpt = basRegOptDao.searchRegOptById(regOptId);
//        String bedStr = "";
//        if(StringUtils.isNotBlank(regOpt.getBed())){
//            bedStr = regOpt.getBed()+ "床的";
//        }
        //将麻醉记录单表中的去向、出室时间进行修改，并将状态修改成为术后状态
        if(EvtAnaesEventService.OUT_PACU_ROOM.equals(code)){
            logger.info("---进入---复苏结束------ENDOPERATION----出室去向="+leaveTo);
            Controller controller = controllerDao.getControllerById(regOptId);
            //当麻醉事件提交数据为出室时，需要将控制表的状态改成POSTOPERATIVE 术后
            if(null != controller){
                //南华版本，增加去pacu的处理
//                if(("2").equals(leaveTo) &&(!controller.getState().equals(OperationState.POSTOPERATIVE))){
//                    controller.setPreviousState(OperationState.SURGERY);
//                    controller.setState(OperationState.RESUSCITATION);
//                    //存入pacu
//                    DocAnaesPacuRec p = docAnaesPacuRecDao.selectPacuByRegOptId(controller.getRegOptId());
//                    if(p==null){
//                        p = new DocAnaesPacuRec();
//                        p.setRegOptId(controller.getRegOptId());
//                    }
//                    saveAnaesPacuRec(p);
//                    
//                }else 
                if (controller.getState().equals(OperationState.RESUSCITATION)){
                    controller.setPreviousState(OperationState.RESUSCITATION);
                    controller.setState(OperationState.POSTOPERATIVE);
                }
                
                controllerDao.update(controller);
                
                //当状态从术中转术后时，将事件表的数据备份
                anaesPacuRec.setExitTime(anaesevent.getOccurTime());
                anaesPacuRec.setProcessState("END");
                docAnaesPacuRecDao.updateByPrimaryKey(anaesPacuRec); 
            }
            
        }
        
        Date endTime = anaesevent.getOccurTime(); //获取到页面传过来的结束时间
        Integer docType = formBean.getDocType();
        Date dbEndTime = basMonitorDisplayDao.findEndTime(regOptId, docType); //获取数据库的
        BasMonitorDisplay md = basMonitorDisplayDao.findMonitorDisplayByInTimeLimit1(regOptId, dbEndTime, docType); //获取最后一个正常数据点
        if(null != dbEndTime){
            long pageEndTimeLong = endTime.getTime();
            long dbEndTimeLong = dbEndTime.getTime();
            if(pageEndTimeLong > dbEndTimeLong){ // 页面传过来的数据  大于 数据库最后一个点的时间
                if(null != md ){
                    Integer freq = md.getFreq();
                    List<BasMonitorDisplay> mds = null;
                    BasMonitorDisplay mDisplay = null;
                    //List<Observe> observes = basPacuMonitorConfigDao.searchAllPacuObserveList(getBeid());
                    List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(getBeid(),getRoomIdByDocType(regOptId, docType));
                    observes = removeSameObserve(observes, docType);
                    
                    long time = dbEndTimeLong + freq*1000;
                    for(;time <= pageEndTimeLong;){
                        // 新增数据点
                        mds = new ArrayList<BasMonitorDisplay>();
                        Timestamp tt = SysUtil.getCurrentTimeStamp(new Date(time));
                        for (Observe observe : observes) {
                            mDisplay = new BasMonitorDisplay();
                            BeanUtils.copyProperties(observe, mDisplay);
                            mDisplay.setFreq(freq);
                            mDisplay.setValue(null); // 设值为0.0f
                            mDisplay.setId(GenerateSequenceUtil.generateSequenceNo());
                            mDisplay.setObserveName(observe.getName());
                            // 设置新增时间
                            mDisplay.setTime(tt);
                            mDisplay.setRegOptId(regOptId);// 设置regOptId
                            mDisplay.setIntervalTime(freq); //设置间隔时间
                            mDisplay.setAmendFlag(3);//3 ： 人为添加  
                            mDisplay.setOuterFlag(1);//结束手术，数据添加
                            mDisplay.setDocType(docType);
                            mds.add(mDisplay);
                        }
                        
                        if (null != mds && mds.size() > 0) {
                            int count = basMonitorDisplayDao.searchMonitorDisplayByTime(regOptId, SysUtil.getTimeFormat(new Date(time)), docType);//查询数据库是否已存在
                            if(count == 0){ //当没有数据的时候，才添加
                                for (BasMonitorDisplay monitorDisplay : mds) {
                                    basMonitorDisplayDao.insertSelective(monitorDisplay);
                                }
                            }
                        }
                        time += freq*1000;
                    }
                }
                
            }else{ // 页面传过来的时间  小于等于  数据库最后一个点的时间
                //删除掉无用数据
                basMonitorDisplayDao.deleteByEndTime(SysUtil.getTimeFormat(dbEndTime), regOptId, docType);
            }
        }
    }
    
    @Transactional
	public void firstSpotPacu(FirstSpotFormBean formBean) {
		Date inTime = formBean.getInTime();
		String regOptId = formBean.getRegOptId();
		Integer docType = formBean.getDocType();
		
		DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
		
		anaesPacuRec.setOperTime(inTime);
		anaesPacuRec.setEnterTime(inTime);
		anaesPacuRec.setAnabioticState(1);
		docAnaesPacuRecDao.updateByPrimaryKeySelective(anaesPacuRec);
		// 取beid
		String beid = formBean.getBeid();
		if(StringUtils.isBlank(beid)){
			beid = getBeid();
		}
		
		String roomId  = getRoomIdByDocType(regOptId, docType);
		DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
		EvtAnaesEvent anaesevent = new EvtAnaesEvent();
		anaesevent.setCode(EvtAnaesEventService.IN_PACU_ROOM);
		anaesevent.setOccurTime(inTime);
		anaesevent.setDocType(docType);//麻醉记录单事件
		//anaesevent.setState("04");//必须是术中
		if(null != anaesRecord){
			anaesevent.setDocId(anaesRecord.getAnaRecordId());
		}
		anaesevent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
		evtAnaesEventDao.insertSelective(anaesevent);
		
		//存入新点
		List<BasMonitorDisplay> mds = new ArrayList<BasMonitorDisplay>();
		logger.error("----firstSpotPacu---inTime="+inTime+",regOptId="+regOptId);
		// 直接存入b_monitor_display中
		List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid,roomId);
		observes = removeSameObserve(observes, docType);
		
		Timestamp tt = new Timestamp(inTime.getTime());
    	//pacu固定频率
    	int collTime = 0;
    	BasDictItem dictItem = basDictItemDao.getListByGroupIdandCodeName("pacu_freq", "复苏室显示频率",beid);
    	if(null != dictItem){
    		collTime = SysUtil.strParseToInt(dictItem.getCodeValue());
    	}
		
		for (Observe observe : observes) {
			BasMonitorDisplay md = new BasMonitorDisplay();
			BeanUtils.copyProperties(observe, md);
			md.setFreq(collTime);
			md.setValue(null); // 设值为0.0f
			// md.setValue(0.0f);
			md.setId(GenerateSequenceUtil.generateSequenceNo());
			md.setObserveName(observe.getName());
			// 设置新增时间
			md.setTime(tt);
			md.setRegOptId(regOptId);// 设置regOptId
			md.setIntervalTime(collTime); // 设置间隔时间
			md.setAmendFlag(1); // 程序修正 
			md.setDocType(docType); //文书类型
			mds.add(md);
		}

		if (null != mds && mds.size() > 0) {
			for (BasMonitorDisplay md : mds) {
				basMonitorDisplayDao.insertSelective(md);
			}
		}
	}
    
    
    /**
     * 
     * @param formBean
     */
    @Transactional
    public void upDatePacuEndTime(EndOperationFormBean formBean,ResponseValue res) {
        EvtAnaesEvent anaesevent = formBean.getAnaesevent();
        //String docId = anaesevent.getDocId();
        String regOptId = formBean.getRegOptId();
        Integer code = anaesevent.getCode();
        String leaveTo = anaesevent.getLeaveTo();
        //String anaEventId = anaesevent.getAnaEventId();
        anaesevent.setDocType(formBean.getDocType()); 
        // 麻醉事件处理
        ResponseValue resp = this.saveAnaes(anaesevent, res);
        if(!resp.getResultCode().equals("1")){
            return;
        }
        //将麻醉记录单表中的去向、出室时间进行修改，并将状态修改成为术后状态
        if(EvtAnaesEventService.OUT_PACU_ROOM.equals(code)){
            logger.info("---进入---手术结束------ENDOPERATION----出室去向="+leaveTo);
            Controller controller = controllerDao.getControllerById(regOptId);
            //当麻醉事件提交数据为出室时，需要将控制表的状态改成POSTOPERATIVE 术后
            if(null != controller){
                //南华版本，增加去pacu的处理
                if (controller.getState().equals(OperationState.RESUSCITATION)){
                    controller.setPreviousState(OperationState.RESUSCITATION);
                    controller.setState(OperationState.POSTOPERATIVE);
                }
                
                controllerDao.update(controller);
                
                DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
                
                anaesPacuRec.setExitTime(anaesevent.getOccurTime());
                
                //当状态从术中转术后时，将事件表的数据备份
                //anaesRecord.setOutOperRoomTime(DateUtils.formatLongTime(anaesevent.getOccurTime().getTime()));
                
                if(StringUtils.isBlank(anaesPacuRec.getLeaveTo()+"")){
                	anaesPacuRec.setLeaveTo(new Integer(leaveTo));
                }
                anaesPacuRec.setAnabioticState(2);
                
                anaesPacuRec.setProcessState("END");
                //anaesRecord.setPostOperState(Integer.parseInt(controller.getState()));
                docAnaesPacuRecDao.updateByPrimaryKey(anaesPacuRec); 
                
                BasRegionBed regionBed = basRegionBedDao.selectByPrimaryKey(anaesPacuRec.getBedId().toString());
                regionBed.setStatus(0);
                regionBed.setRegOptId("");
                basRegionBedDao.updateByPrimaryKey(regionBed);
                
                //出室时，将采集配置表中手术Id清除掉
                BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(anaesPacuRec.getBedId(), getBeid());
                collectConfig.setPatientId(null);
                basCollectConfigDao.updateByPrimaryKey(collectConfig);
            }
        }
        
        Date endTime = anaesevent.getOccurTime(); //获取到页面传过来的结束时间
        Integer docType = formBean.getDocType();
        Date dbEndTime = basMonitorDisplayDao.findEndTime(regOptId, docType); //获取数据库的
        BasMonitorDisplay md = basMonitorDisplayDao.findMonitorDisplayByInTimeLimit1(regOptId, dbEndTime, docType); //获取最后一个正常数据点
        if(null != dbEndTime){
            long pageEndTimeLong = endTime.getTime();
            long dbEndTimeLong = dbEndTime.getTime();
            if(pageEndTimeLong > dbEndTimeLong){ // 页面传过来的数据  大于 数据库最后一个点的时间
                if(null != md ){
                    Integer freq = md.getFreq();
                    List<BasMonitorDisplay> mds = null;
                    BasMonitorDisplay mDisplay = null;
                    List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(getBeid(),getRoomIdByDocType(regOptId, docType));
                    observes = removeSameObserve(observes, docType);
                    
                    long time = dbEndTimeLong + freq*1000;
                    for(;time <= pageEndTimeLong;){
                        // 新增数据点
                        mds = new ArrayList<BasMonitorDisplay>();
                        Timestamp tt = SysUtil.getCurrentTimeStamp(new Date(time));
                        for (Observe observe : observes) {
                            mDisplay = new BasMonitorDisplay();
                            BeanUtils.copyProperties(observe, mDisplay);
                            mDisplay.setFreq(freq);
                            mDisplay.setValue(null); // 设值为0.0f
                            mDisplay.setId(GenerateSequenceUtil.generateSequenceNo());
                            mDisplay.setObserveName(observe.getName());
                            // 设置新增时间
                            mDisplay.setTime(tt);
                            mDisplay.setRegOptId(regOptId);// 设置regOptId
                            mDisplay.setIntervalTime(freq); //设置间隔时间
                            mDisplay.setAmendFlag(3);//3 ： 人为添加  
                            mDisplay.setOuterFlag(1);//结束手术，数据添加
                            mDisplay.setDocType(docType);
                            mds.add(mDisplay);
                        }
                        
                        if (null != mds && mds.size() > 0) {
                            int count = basMonitorDisplayDao.searchMonitorDisplayByTime(regOptId, SysUtil.getTimeFormat(new Date(time)), docType);//查询数据库是否已存在
                            if(count == 0){ //当没有数据的时候，才添加
                                for (BasMonitorDisplay monitorDisplay : mds) {
                                    basMonitorDisplayDao.insertSelective(monitorDisplay);
                                }
                            }
                        }
                        time += freq*1000;
                    }
                }
                
            }else{ // 页面传过来的时间  小于等于  数据库最后一个点的时间
                //删除掉无用数据
                basMonitorDisplayDao.deleteByEndTime(SysUtil.getTimeFormat(dbEndTime), regOptId, docType);
            }
        }
    }
    
    
	/*#####################################################PACU复苏单修改出入室时间######################################################################*/
    /**
	 * 获取分页的监测项数据
	 * @param bean
	 * @param observeIds
	 * @return
	 */
	public List<BasMonitorDisplay> getobsData(MonitorDataFormBean bean,List<String> observeIds){
		String regOptId = bean.getRegOptId();
		Integer no = bean.getNo();
		Integer size = bean.getSize();
		Date time = bean.getInTime();
		logger.info("getobsData---inTime-----------"+time);
		String inTime = SysUtil.getTimeFormat(time);
		Integer pageNo = 0;
		Integer pageSize = 31;
		if(null != size && 0 != size){
			pageSize = size;
		}
        String outerTime = null;
        if(Objects.equals(2, bean.getDocType())){
        	DocAnaesPacuRec pacuRec = docAnaesPacuRecDao.selectPacuByRegOptId(regOptId);
        	if(null != pacuRec && null != pacuRec.getExitTime()){
				outerTime = DateUtils.formatDateTime(pacuRec.getExitTime());
			}
        }else{
        	DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
        	if(null != anaesRecord){
     			outerTime = anaesRecord.getOutOperRoomTime();
        	}
        }
        
		Integer total = basMonitorDisplayDao.searchMonitorDisplayListTotal(regOptId, observeIds, inTime, outerTime, bean.getDocType());
		pageNo = calcPageNo(total,no,pageSize);
		
		return basMonitorDisplayDao.searchMonitorDisplayList(regOptId, pageNo, pageSize, observeIds, inTime, outerTime, bean.getDocType());
	}
	
	/**
     * 相比于getobsDataNew3，增加了obsLists数据项的特殊处理(去除obsLists的处理)
     * obsLists的特殊处理原因是：血压、etCo2、fio2由于采集设备的原因，不能保证每秒都有数据，就有断点的情况，需要额外去最大时间的点，填充到数据库中，但需额外控制下，间隔时间超过20s，则直接存null值
     * 目前的解决方案:根据传递的采集指标，如果该秒无记录，采集服务会自动补上记录，但value值为null 
     * @param formBean
     * @param observeIds
     * @return
     */
    @Transactional
    public List<BasMonitorDisplay> getobsDataNew4(MonitorDataFormBean formBean,List<String> observeIds){
        Date inTime = formBean.getInTime();
        Date sDate = formBean.getStartTime();
        String regOptId = formBean.getRegOptId();
        Integer docType = formBean.getDocType();
        logger.info("sDate: "+sDate+"---inTime: "+inTime);
        String startTime = SysUtil.getTimeFormat(sDate);
        
        List<BasMonitorDisplay> newMds = null;
        List<BasMonitorDisplay> mds = null;
        BasMonitorDisplay md = null;
        
        // 取beid
        String beid = formBean.getBeid();
        String roomId = getCurRoomId(regOptId);
        if(StringUtils.isBlank(beid)){
            beid = getBeid();
            formBean.setBeid(beid);
        }
        
        List<BasObserveData> observeDatas = basObserveDataDao.findObserveDataListByTime(regOptId,startTime,null, null); //获取新点 把所有b_observe_data都拿到 
        logger.info("getobsDataNew4Base----observeDatas=="+JsonType.jsonType(observeDatas));
        
        int collTime = 0;
        
        if(Objects.equals(2, formBean.getDocType())){
        	BasDictItem dictItem = basDictItemDao.getListByGroupIdandCodeName("pacu_freq", "复苏室显示频率",beid);
        	if(null != dictItem){
        		collTime = SysUtil.strParseToInt(dictItem.getCodeValue());
        	}
        }else{
        	String operModel = MyConstants.OPERATION_MODEL_NORMAL;
            DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
            String anaRecordId = "";
            if(null!=anaesRecord){
                anaRecordId = anaesRecord.getAnaRecordId();
                SearchFormBean searchBean = new SearchFormBean();
                searchBean.setDocId(anaRecordId);
                searchBean.setCurrentState("1");
                List<EvtRescueevent> list = evtRescueeventDao.searchRescueeventList(searchBean);
                if(list.size()>0){
                    operModel = list.get(0).getModel();
                    logger.info("getCurrentModel---当前的手术模式==="+operModel);
                }
                logger.info("getCurrentModel--- 返回当前的手术模式operModel==="+operModel+",list的size==="+list.size());
            }else{
                logger.error("getCurrentModel-------根据regOptId返回的doc_id为空，请检查！");
            }
           
            if(operModel.equals(MyConstants.OPERATION_MODEL_NORMAL)){
                collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid, roomId).getValue());
            }else if (MyConstants.OPERATION_MODEL_SAVE.equals(operModel)) {
                collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_SAVE, beid, roomId).getValue());
            }
            
        }
        List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid, roomId);  //获取数据库配置监测项
        observes = removeSameObserve(observes, docType);
        
        Timestamp maxTime = null;
        if(null == observeDatas || observeDatas.size()<1){
            logger.info("startTime："+startTime+",未取到observeDatas数据，请检查采集服务是否连接!!!=====手术Id： " + regOptId);
        }else{
            maxTime = observeDatas.get(0).getTime();
            logger.info("最近时间： " + maxTime + "=====手术Id： " + regOptId);
            logger.info("最新点时间： " + startTime + "=====手术Id： " + regOptId);
        }
        
        if(null != observeDatas && observeDatas.size()>0 && (Math.abs(sDate.getTime() - maxTime.getTime()) <= MyConstants.TRACE_DATA_TIMEOUT * 1000)){ //查询出来有数据
            mds = new ArrayList<BasMonitorDisplay>();
            newMds = new ArrayList<BasMonitorDisplay>();
            Timestamp tt = SysUtil.getTimestamp(startTime);
            Map<String, BasMonitorDisplay> speObserveMap = new HashMap<String, BasMonitorDisplay>();
            
            
            for (BasObserveData observeData : observeDatas) {
                md = new BasMonitorDisplay();
                BeanUtils.copyProperties(observeData, md);
                md.setFreq(collTime); //修改为系统获取的freq值，而不是b_observe_data的freq
                md.setIntervalTime(collTime); //修改为系统获取的freq值，而不是b_observe_data的freq
                md.setAmendFlag(0); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                md.setDocType(docType); //文书类型  1：麻醉记录单 2：复苏记录单
                md.setTime(sDate);
                md.setRegOptId(regOptId);
                //if(!Objects.equals(2, formBean.getDocType())){
            	//将重复监测项的eventId设置为统一的eventId
                BasMonitorConfigDefault mcd = basMonitorConfigDefaultDao.selectByEventName(md.getObserveName(), beid, docType);
                BasMonitorConfig monitorConfig = basMonitorConfigDao.selectByPrimaryKey(md.getObserveId(), beid);
                if (null != mcd && Objects.equals(monitorConfig.getPosition(), 0))
                {
                    String commonEventId = mcd.getEventId();
                    md.setObserveId(commonEventId);
                    BaseInfoQuery baseQuery = new BaseInfoQuery();
                    baseQuery.setRegOptId(regOptId);
                    baseQuery.setEventId(commonEventId);
                    baseQuery.setBeid(getBeid());
                    baseQuery.setOperRoomId(roomId);
                    baseQuery.setDocType(docType);
                    BasAnaesMonitorConfigFormBean amc = basAnaesMonitorConfigDao.getAnaesMonitorConfigByEventId(baseQuery);
                    BasMonitorDisplay monitorDisplay = speObserveMap.get(commonEventId);
                    //三种情况下，将当前md作为重复监测项的描点数据
                    //1、当前重复监测项没有对应的描点数据
                    if (null == monitorDisplay)
                    {
                        speObserveMap.put(commonEventId, md);
                    }
                    //2、用户选择的设备对应有相应的监测数据
                    else if (null != amc && md.getObserveId().equals(amc.getRealEventId()) && null != md.getValue())
                    {
                        speObserveMap.put(commonEventId, md);
                    }
                    //3、当前放入map中的md没有数据，但是循环的md有数据
                    else if (null == monitorDisplay.getValue()  && null != md.getValue())
                    {
                        speObserveMap.put(commonEventId, md);
                    }
                    continue; //重复监测项，存入bas_monitor_display表中时只存入一条数据
                }
                //}
                
                String observeId = md.getObserveId();
                if(null != observeIds && observeIds.size()>0){
                    for (String oIds : observeIds) {
                        if(observeId.equals(oIds)){
                            newMds.add(md);//只有属于页面需要的observeIds,才放入到list对象中
                            break;
                        }
                    }
                }
                mds.add(md);//全部存入list中，并存入到数据库
            }
            
            //对重复监测项的描点数据进行处理
            for (String eventId : speObserveMap.keySet())
            {
                if (null != observeIds && observeIds.size()>0 && observeIds.contains(eventId))
                {
                    newMds.add(speObserveMap.get(eventId));
                }
                mds.add(speObserveMap.get(eventId));
            }
            
            if(null != observes && observes.size()>0){ //如果mds不全，则从数据库中获取监测项，并将对应需要补齐的，返回给前端
                for (Observe observe : observes) {
                    md = new BasMonitorDisplay();
                    BeanUtils.copyProperties(observe, md);
                    md.setFreq(collTime);
                    md.setValue(null); // 设值为0.0f
                    md.setRegOptId(regOptId);
                    if (MyConstants.O2_EVENT_ID.equals(md.getObserveId()))
                    {
                        //找到前一条O2监测项的值
                        BasMonitorDisplay o2MonitorDisplay = basMonitorDisplayDao.findLastestedDataByObserveId(regOptId,MyConstants.O2_EVENT_ID, formBean.getDocType());
                        if(null!=o2MonitorDisplay){
                        	md.setValue(o2MonitorDisplay.getValue());
                        }
                    }
                    
                    md.setId(GenerateSequenceUtil.generateSequenceNo());
                    md.setObserveName(observe.getName());
                    // 设置新增时间
                    md.setTime(tt);
                    md.setRegOptId(regOptId);// 设置regOptId
                    md.setIntervalTime(collTime); // 设置间隔时间
                    md.setAmendFlag(0); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                    md.setDocType(docType); //文书类型  1：麻醉记录单 2：复苏记录单
                    String observeId = md.getObserveId();
                    if(null != mds && mds.size()>0){ 
                        int flag = 0;
                        for (int i = 0; i < mds.size(); i++) {
                            BasMonitorDisplay monitorDisplay = mds.get(i);
                            if(observeId.equals(monitorDisplay.getObserveId())){ //已经存在mds中，则不继续存入，将flag置为-1
                                flag = -1;
                                break; //有相同的，则直接break
                            }
                        }
                        if(flag == 0){ //当flag为0，则说明mds中没有当前observeId的数据，则需要添加到mds中
                            mds.add(md);
                        }
                    }
                    
                    if(null != observeIds && observeIds.size()>0){ // 页面需要observeIds,传递回去的也是observeIds对应的数据，不够的，则补齐到前端
                        for (String oIds : observeIds) {
                            if(observeId.equals(oIds)){ //是需要传递给前端的observeId 
                                if(null != newMds && newMds.size()>0){ //size大于0，则需要比较 
                                    int flag = 0;
                                    for (int i = 0; i < newMds.size(); i++) {
                                        BasMonitorDisplay monitorDisplay = newMds.get(i);
                                        if(observeId.equals(monitorDisplay.getObserveId())){
                                            flag = -1;
                                            break;
                                        }
                                    }
                                    if(flag == 0){ // 说明newMds中没有当前observeId的数据，需要增加到newMds
                                        newMds.add(md);
                                        break; //添加一项后，break当前for
                                    }else{
                                        break;  //等于-1，则直接break当前for 
                                    }
                                }else{ //size 为0，则需要添加，并发送给前端数据
                                    newMds.add(md);
                                    break;  //添加一项后，break当前for
                                }
                            }
                        }
                    }
                }
            }
        }else{//对应时间点查询不到数据，则存入null值到b_monitor_display表，eventLists中的数据的value值也应该为null，不处理eventLists数据
            mds = new ArrayList<BasMonitorDisplay>();
            newMds = new ArrayList<BasMonitorDisplay>();
            Timestamp tt = SysUtil.getTimestamp(startTime);
            if(null != observes && observes.size()>0){
                for (Observe observe : observes) {
                    md = new BasMonitorDisplay();
                    BeanUtils.copyProperties(observe, md);
                    md.setFreq(collTime);
                    md.setValue(null); // 设值为0.0f
                    
                    if (MyConstants.O2_EVENT_ID.equals(md.getObserveId()))
                    {
                        //找到前一条O2监测项的值
                        BasMonitorDisplay o2MonitorDisplay = basMonitorDisplayDao.findLastestedDataByObserveId(regOptId,MyConstants.O2_EVENT_ID, formBean.getDocType());
                        md.setValue(o2MonitorDisplay.getValue());
                    }
                    
                    md.setId(GenerateSequenceUtil.generateSequenceNo());
                    md.setObserveName(observe.getName());
                    // 设置新增时间
                    md.setTime(tt);
                    md.setRegOptId(regOptId);// 设置regOptId
                    md.setIntervalTime(collTime); // 设置间隔时间
                    md.setAmendFlag(0); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                    md.setDocType(docType); //文书类型  1：麻醉记录单 2：复苏记录单
                    mds.add(md);
                    String observeId = md.getObserveId();
                    if(null != observeIds && observeIds.size()>0){
                        for (String oIds : observeIds) {
                            if(observeId.equals(oIds)){
                                newMds.add(md);
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        if (null != mds && mds.size()>0) { //存入数据库
            logger.info("getobsDataNew4----mds="+JsonType.jsonType(mds));
            int count = basMonitorDisplayDao.searchMonitorDisplayByTime(regOptId,startTime, docType);
            if(count==0){
                for (BasMonitorDisplay monitorDisplay : mds) {
                    basMonitorDisplayDao.insertSelective(monitorDisplay);
                }
            }
        }
        return newMds; 
    }
    
    
    
    
    
    /**
     * 相比于getobsDataNew3，增加了obsLists数据项的特殊处理(去除obsLists的处理)
     * obsLists的特殊处理原因是：血压、etCo2、fio2由于采集设备的原因，不能保证每秒都有数据，就有断点的情况，需要额外去最大时间的点，填充到数据库中，但需额外控制下，间隔时间超过20s，则直接存null值
     * 目前的解决方案:根据传递的采集指标，如果该秒无记录，采集服务会自动补上记录，但value值为null 
     * @param formBean
     * @param observeIds
     * @return
     */
    @Transactional
    public List<BasMonitorDisplay> getobsDataNew4HNHTYY(MonitorDataFormBean formBean,List<String> observeIds){
        Date inTime = formBean.getInTime();
        Date sDate = formBean.getStartTime();
        String regOptId = formBean.getRegOptId();
        Integer docType = formBean.getDocType();
        logger.info("sDate: "+sDate+"---inTime: "+inTime);
        String startTime = SysUtil.getTimeFormat(sDate);
        
        List<BasMonitorDisplay> newMds = null;
        List<BasMonitorDisplay> mds = null;
        BasMonitorDisplay md = null;
        
        // 取beid
        String beid = formBean.getBeid();
        String roomId = getCurRoomId(regOptId);
        if(StringUtils.isBlank(beid)){
            beid = getBeid();
            formBean.setBeid(beid);
        }
        
        List<BasObserveData> observeDatas = basObserveDataDao.findObserveDataListByTime(regOptId,startTime,null, null); //获取新点 把所有b_observe_data都拿到 
        logger.info("getobsDataNew4Base----observeDatas=="+JsonType.jsonType(observeDatas));
        
        int collTime = 0;
        
        if(Objects.equals(2, formBean.getDocType())){
        	BasDictItem dictItem = basDictItemDao.getListByGroupIdandCodeName("pacu_freq", "复苏室显示频率",beid);
        	if(null != dictItem){
        		collTime = SysUtil.strParseToInt(dictItem.getCodeValue());
        	}
        }else{
        	String operModel = MyConstants.OPERATION_MODEL_NORMAL;
            DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
            String anaRecordId = "";
            if(null!=anaesRecord){
                anaRecordId = anaesRecord.getAnaRecordId();
                SearchFormBean searchBean = new SearchFormBean();
                searchBean.setDocId(anaRecordId);
                searchBean.setCurrentState("1");
                List<EvtRescueevent> list = evtRescueeventDao.searchRescueeventList(searchBean);
                if(list.size()>0){
                    operModel = list.get(0).getModel();
                    logger.info("getCurrentModel---当前的手术模式==="+operModel);
                }
                logger.info("getCurrentModel--- 返回当前的手术模式operModel==="+operModel+",list的size==="+list.size());
            }else{
                logger.error("getCurrentModel-------根据regOptId返回的doc_id为空，请检查！");
            }
           
            if(operModel.equals(MyConstants.OPERATION_MODEL_NORMAL)){
                collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_NORMAL, beid, roomId).getValue());
            }else if (MyConstants.OPERATION_MODEL_SAVE.equals(operModel)) {
                collTime = SysUtil.strParseToInt(basMonitorConfigFreqDao.searchMonitorFreqByType(MyConstants.OPERATION_MODEL_SAVE, beid, roomId).getValue());
            }
            
        }
        List<Observe> observes = basMonitorConfigDao.searchAllAnaesObserveList(beid, roomId);  //获取数据库配置监测项
        observes = removeSameObserve(observes, docType);
        
        Timestamp maxTime = null;
        if(null == observeDatas || observeDatas.size()<1){
            logger.info("startTime："+startTime+",未取到observeDatas数据，请检查采集服务是否连接!!!=====手术Id： " + regOptId);
        }else{
            maxTime = observeDatas.get(0).getTime();
            logger.info("最近时间： " + maxTime + "=====手术Id： " + regOptId);
            logger.info("最新点时间： " + startTime + "=====手术Id： " + regOptId);
        }
        
        //如果无创血压和有创血压同时采集，从开始采集有创血压时间点起，自动隐藏无创血压监测值。
        //有创血压
        String ibpLs =  Global.getConfig("ibpArray").trim();
        //无创血压
        String nibpLs = Global.getConfig("nibpArray").trim();
        boolean isIBP = false;//是否存在有创血压
        List<String> nibpArry = new ArrayList<String>();
        
        if(null != observeDatas && observeDatas.size()>0 && (Math.abs(sDate.getTime() - maxTime.getTime()) <= MyConstants.TRACE_DATA_TIMEOUT * 1000)){ //查询出来有数据
            mds = new ArrayList<BasMonitorDisplay>();
            newMds = new ArrayList<BasMonitorDisplay>();
            Timestamp tt = SysUtil.getTimestamp(startTime);
            Map<String, BasMonitorDisplay> speObserveMap = new HashMap<String, BasMonitorDisplay>();
            
            for (BasObserveData observeData : observeDatas) {
            	
            	if(isIBP == false && ibpLs.contains(observeData.getObserveId()) && null != observeData.getValue()){
            		BaseInfoQuery baseQuery = new BaseInfoQuery();
            		baseQuery.setRegOptId(regOptId);
            		baseQuery.setBeid(beid);
            		baseQuery.setEventIds(ibpLs);
            		baseQuery.setPosition("0");
            		baseQuery.setDocType(formBean.getDocType());
            		int cnt = basAnaesMonitorConfigDao.isIbpsList(baseQuery);
            		if(cnt > 0){
            			isIBP = true;
            		}
            	}
            	
            	if(nibpLs.contains(observeData.getObserveId())){
            		nibpArry.add(observeData.getObserveId());
            	}
            	
                md = new BasMonitorDisplay();
                BeanUtils.copyProperties(observeData, md);
                md.setFreq(collTime); //修改为系统获取的freq值，而不是b_observe_data的freq
                md.setIntervalTime(collTime); //修改为系统获取的freq值，而不是b_observe_data的freq
                md.setAmendFlag(0); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                md.setDocType(docType); //文书类型  1：麻醉记录单 2：复苏记录单
                md.setTime(sDate);
                md.setRegOptId(regOptId);
                //if(!Objects.equals(2, formBean.getDocType())){
            	//将重复监测项的eventId设置为统一的eventId
                BasMonitorConfigDefault mcd = basMonitorConfigDefaultDao.selectByEventName(md.getObserveName(), beid, docType);
                BasMonitorConfig monitorConfig = basMonitorConfigDao.selectByPrimaryKey(md.getObserveId(), beid);
                if (null != mcd && Objects.equals(monitorConfig.getPosition(), 0))
                {
                    String commonEventId = mcd.getEventId();
                    md.setObserveId(commonEventId);
                    BaseInfoQuery baseQuery = new BaseInfoQuery();
                    baseQuery.setRegOptId(regOptId);
                    baseQuery.setEventId(commonEventId);
                    baseQuery.setBeid(getBeid());
                    baseQuery.setOperRoomId(roomId);
                    baseQuery.setDocType(docType);
                    BasAnaesMonitorConfigFormBean amc = basAnaesMonitorConfigDao.getAnaesMonitorConfigByEventId(baseQuery);
                    BasMonitorDisplay monitorDisplay = speObserveMap.get(commonEventId);
                    //三种情况下，将当前md作为重复监测项的描点数据
                    //1、当前重复监测项没有对应的描点数据
                    if (null == monitorDisplay)
                    {
                        speObserveMap.put(commonEventId, md);
                    }
                    //2、用户选择的设备对应有相应的监测数据
                    else if (null != amc && md.getObserveId().equals(amc.getRealEventId()) && null != md.getValue())
                    {
                        speObserveMap.put(commonEventId, md);
                    }
                    //3、当前放入map中的md没有数据，但是循环的md有数据
                    else if (null == monitorDisplay.getValue()  && null != md.getValue())
                    {
                        speObserveMap.put(commonEventId, md);
                    }
                    continue; //重复监测项，存入bas_monitor_display表中时只存入一条数据
                }
                //}
                
                String observeId = md.getObserveId();
                if(null != observeIds && observeIds.size()>0){
                    for (String oIds : observeIds) {
                        if(observeId.equals(oIds)){
                            newMds.add(md);//只有属于页面需要的observeIds,才放入到list对象中
                            break;
                        }
                    }
                }
                mds.add(md);//全部存入list中，并存入到数据库
            }
            
            //对重复监测项的描点数据进行处理
            for (String eventId : speObserveMap.keySet())
            {
                if (null != observeIds && observeIds.size()>0 && observeIds.contains(eventId))
                {
                    newMds.add(speObserveMap.get(eventId));
                }
                mds.add(speObserveMap.get(eventId));
            }
            
            if(null != observes && observes.size()>0){ //如果mds不全，则从数据库中获取监测项，并将对应需要补齐的，返回给前端
                for (Observe observe : observes) {
                    md = new BasMonitorDisplay();
                    BeanUtils.copyProperties(observe, md);
                    md.setFreq(collTime);
                    md.setValue(null); // 设值为0.0f
                    md.setRegOptId(regOptId);
                    if (MyConstants.O2_EVENT_ID.equals(md.getObserveId()))
                    {
                        //找到前一条O2监测项的值
                        BasMonitorDisplay o2MonitorDisplay = basMonitorDisplayDao.findLastestedDataByObserveId(regOptId,MyConstants.O2_EVENT_ID, formBean.getDocType());
                        if(null!=o2MonitorDisplay){
                        	md.setValue(o2MonitorDisplay.getValue());
                        }
                    }
                    
                    if (MyConstants.ECG.equals(md.getObserveId()))
                    {
                        //找到前一条ECG监测项的值
                        BasMonitorDisplay o2MonitorDisplay = basMonitorDisplayDao.findLastestedDataByObserveId(regOptId,MyConstants.ECG, formBean.getDocType());
                        if(null!=o2MonitorDisplay){
                        	md.setValue(o2MonitorDisplay.getValue());
                        }
                    }
                    
                    md.setId(GenerateSequenceUtil.generateSequenceNo());
                    md.setObserveName(observe.getName());
                    // 设置新增时间
                    md.setTime(tt);
                    md.setRegOptId(regOptId);// 设置regOptId
                    md.setIntervalTime(collTime); // 设置间隔时间
                    md.setAmendFlag(0); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                    md.setDocType(docType); //文书类型  1：麻醉记录单 2：复苏记录单
                    String observeId = md.getObserveId();
                    if(null != mds && mds.size()>0){ 
                        int flag = 0;
                        for (int i = 0; i < mds.size(); i++) {
                            BasMonitorDisplay monitorDisplay = mds.get(i);
                            if(observeId.equals(monitorDisplay.getObserveId())){ //已经存在mds中，则不继续存入，将flag置为-1
                                flag = -1;
                                break; //有相同的，则直接break
                            }
                        }
                        if(flag == 0){ //当flag为0，则说明mds中没有当前observeId的数据，则需要添加到mds中
                            mds.add(md);
                        }
                    }
                    
                    if(null != observeIds && observeIds.size()>0){ // 页面需要observeIds,传递回去的也是observeIds对应的数据，不够的，则补齐到前端
                        for (String oIds : observeIds) {
                            if(observeId.equals(oIds)){ //是需要传递给前端的observeId 
                                if(null != newMds && newMds.size()>0){ //size大于0，则需要比较 
                                    int flag = 0;
                                    for (int i = 0; i < newMds.size(); i++) {
                                        BasMonitorDisplay monitorDisplay = newMds.get(i);
                                        if(observeId.equals(monitorDisplay.getObserveId())){
                                            flag = -1;
                                            break;
                                        }
                                    }
                                    if(flag == 0){ // 说明newMds中没有当前observeId的数据，需要增加到newMds
                                        newMds.add(md);
                                        break; //添加一项后，break当前for
                                    }else{
                                        break;  //等于-1，则直接break当前for 
                                    }
                                }else{ //size 为0，则需要添加，并发送给前端数据
                                    newMds.add(md);
                                    break;  //添加一项后，break当前for
                                }
                            }
                        }
                    }
                }
            }
        }else{//对应时间点查询不到数据，则存入null值到b_monitor_display表，eventLists中的数据的value值也应该为null，不处理eventLists数据
            mds = new ArrayList<BasMonitorDisplay>();
            newMds = new ArrayList<BasMonitorDisplay>();
            Timestamp tt = SysUtil.getTimestamp(startTime);
            if(null != observes && observes.size()>0){
                for (Observe observe : observes) {
                    md = new BasMonitorDisplay();
                    BeanUtils.copyProperties(observe, md);
                    md.setFreq(collTime);
                    md.setValue(null); // 设值为0.0f
                    
                    if (MyConstants.O2_EVENT_ID.equals(md.getObserveId()))
                    {
                        //找到前一条O2监测项的值
                        BasMonitorDisplay o2MonitorDisplay = basMonitorDisplayDao.findLastestedDataByObserveId(regOptId,MyConstants.O2_EVENT_ID, formBean.getDocType());
                        md.setValue(o2MonitorDisplay.getValue());
                    }
                    
                    if (MyConstants.ECG.equals(md.getObserveId()))
                    {
                        //找到前一条ECG监测项的值
                        BasMonitorDisplay o2MonitorDisplay = basMonitorDisplayDao.findLastestedDataByObserveId(regOptId,MyConstants.ECG, formBean.getDocType());
                        md.setValue(o2MonitorDisplay.getValue());
                    }
                    
                    md.setId(GenerateSequenceUtil.generateSequenceNo());
                    md.setObserveName(observe.getName());
                    // 设置新增时间
                    md.setTime(tt);
                    md.setRegOptId(regOptId);// 设置regOptId
                    md.setIntervalTime(collTime); // 设置间隔时间
                    md.setAmendFlag(0); // 数据状态，0：正常；1：程序修正；2：人为修正；3：人为添加
                    md.setDocType(docType); //文书类型  1：麻醉记录单 2：复苏记录单
                    mds.add(md);
                    String observeId = md.getObserveId();
                    if(null != observeIds && observeIds.size()>0){
                        for (String oIds : observeIds) {
                            if(observeId.equals(oIds)){
                                newMds.add(md);
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        if (null != mds && mds.size()>0) { //存入数据库
            logger.info("getobsDataNew4----mds="+JsonType.jsonType(mds));
            int count = basMonitorDisplayDao.searchMonitorDisplayByTime(regOptId,startTime, docType);
            if(count==0){
                for (BasMonitorDisplay monitorDisplay : mds) {
                	//如果存在有创血压，将无创血压值设置为空;
                	if(isIBP){
                		if(null != nibpArry && nibpArry.size() > 0){
                			if(nibpArry.contains(monitorDisplay.getObserveId())){
                				monitorDisplay.setValue(null);
                			}
                		}
                	}
                    basMonitorDisplayDao.insertSelective(monitorDisplay);
                }
            }
        }
        return newMds; 
    }
    
    
    
    
    
    
    
    
	
	@Transactional
    public void againStartOper(SearchFormBean searchBean,ResponseValue resp){
        
    	String regOptId = searchBean.getRegOptId();
    	
        //麻醉记录单
        DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
        //设置文书ID
        searchBean.setDocId(anaesRecord.getAnaRecordId());
        // 手术信息表
        BasRegOpt opt = basRegOptDao.searchRegOptById(regOptId);
        
        //获取排程信息
        BasDispatch dispatch = basDispatchDao.getDispatchOper(regOptId);
        
        
        String operRoomId = searchBean.getOperRoomId();
        if(StringUtils.isBlank(operRoomId)){
        	resp.setResultCode("40000004");
        	resp.setResultMessage("请选择再次手术转入的手术室ID");
            logger.info("------------------end againStartOper------------------------");
            return;
        }
            
        //如果已经是术后or中止状态，则直接返回给前端
        if(!("05,06").contains(opt.getState())){ //术后状态
        	resp.setResultCode("40000004");
        	resp.setResultMessage("当前患者("+opt.getName()+")的手术状态不为复苏或术后,不允许再次手术!");
            logger.info("------------------end againStartOper------------------------");
            return;
        }
        
        if(!Objects.equals(0, opt.getIsLocalAnaes())){
        	resp.setResultCode("40000004");
        	resp.setResultMessage("当前患者("+opt.getName()+")为局麻手术,不允许再次手术!");
            logger.info("------------------end againStartOper------------------------");
            return;
        }
        
        BaseInfoQuery baseQuery = new BaseInfoQuery();
        baseQuery.setState(OperationState.SURGERY);
        baseQuery.setOperRoomId(operRoomId);
        baseQuery.setBeid(getBeid());
        /*
         * 同一个手术间可开始多台局麻或可开始一台全麻及多台局麻，但是不能同时开多台全麻
         * 
         * 1、当提交开始手术为局麻时直接跳过未完成手术的判断
         * 2、当提交开始手术为全麻时获取未完成的手术列表中存在全麻时，提示有未完成的全麻手术！
         */
        
        if(Objects.equals(0, opt.getIsLocalAnaes())){
            //判断当前手术室是否存在未完成的手术，如果存在则返回错误消息
            List<SearchOperaPatrolRecordFormBean> operaPatrolList = basRegOptDao.getOperaPatrolRecordListByRoomId(baseQuery);
            SearchOperaPatrolRecordFormBean po = null;
            if(operaPatrolList.size()>0){
                for (SearchOperaPatrolRecordFormBean searchOperaPatrolRecordFormBean : operaPatrolList) {
                    if("0".equals(searchOperaPatrolRecordFormBean.getIsLocalAnaes())){
                        po = searchOperaPatrolRecordFormBean;
                    }
                }
                if(null != po && !po.getRegOptId().equals(searchBean.getRegOptId())){
                	resp.setResultCode("40000004");
                	resp.setResultMessage( po.getOperRoomName()+"存在病人信息为："+po.getName()+",手术未完成!");
                	resp.put("id", po.getRegOptId());
                    logger.info("------------------end againStartOper------------------------");
                    return;
                }
            }
        }
       
        Controller controller = controllerDao.getControllerById(regOptId);
        
        if("05".equals(controller.getState())){
        	DocAnaesPacuRec pacuRec =  docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
        	if(null != pacuRec){
        		
        		pacuRec.setExitTime(pacuRec.getExitTime()==null?new Date():pacuRec.getExitTime());
        		
        		if(Objects.equals(pacuRec.getAnabioticState(), 1)){
        			
        			BasRegionBed regionBed = basRegionBedDao.selectByPrimaryKey(pacuRec.getBedId());
    				regionBed.setStatus(0);
    				regionBed.setRegOptId("");
    				basRegionBedDao.updateByPrimaryKey(regionBed);
    				
    				
        		}
        		
        		EvtAnaesEvent anaesEventPacu = new EvtAnaesEvent();
                anaesEventPacu.setCode(EvtAnaesEventService.OUT_ROOM);
                anaesEventPacu.setOccurTime(pacuRec.getExitTime());
                if(null != pacuRec){
                    anaesEventPacu.setDocId(pacuRec.getId());
                }
                anaesEventPacu.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
                anaesEventPacu.setDocType(2);
                evtAnaesEventDao.insert(anaesEventPacu);
        		
        		pacuRec.setProcessState("END");
				pacuRec.setAnabioticState(2);
        		
        		docAnaesPacuRecDao.updateByPrimaryKeySelective(pacuRec);
        		
        		//出室时，将采集配置表中手术Id清除掉
                BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(pacuRec.getBedId(), getBeid());
                if (null != collectConfig)
                {
                    collectConfig.setPatientId(null);
                    basCollectConfigDao.updateByPrimaryKey(collectConfig);
                }
        	}
        }
        
        
        
        if("05,06".contains(controller.getState())){
        	controllerDao.checkOperation(regOptId, OperationState.SURGERY, controller.getState());
        }
        
        /**
         * 再次手术时选择的roomId存入排程表中
         */
        if(StringUtils.isNotBlank(operRoomId)){
        	dispatch.setOperRoomId(operRoomId);
        	BasOperroom operroom = basOperroomDao.queryRoomListById(dispatch.getOperRoomId().toString(), getBeid());
        	dispatch.setOperRoomName(null == operroom ? null : operroom.getName());
        	basDispatchDao.update(dispatch);
        	
        	anaesRecord.setOperRoomName(null == operroom ? null : operroom.getName());
        	anaesRecord.setLeaveTo("");
        	anaesRecord.setOperEndTime("");
        	anaesRecord.setAnaesEndTime("");
        	anaesRecord.setOutOperRoomTime("");
        	anaesRecord.setProcessState("NO_END");
        	docAnaesRecordDao.updateByPrimaryKey(anaesRecord);
        	
        	EvtAnaesEvent operEnd = evtAnaesEventDao.selectAnaesEventByCodeAndDocId(anaesRecord.getAnaRecordId(), EvtAnaesEventService.OPER_END);// 手术结束
        	if(null != operEnd){
        		evtAnaesEventDao.deleteByPrimaryKey(operEnd.getAnaEventId());
        	}
        	EvtAnaesEvent anaesEnd = evtAnaesEventDao.selectAnaesEventByCodeAndDocId(anaesRecord.getAnaRecordId(), EvtAnaesEventService.ANAES_END);// 麻醉结束
        	if(null != anaesEnd){
        		evtAnaesEventDao.deleteByPrimaryKey(anaesEnd.getAnaEventId());
        	}
        	EvtAnaesEvent outEnd = evtAnaesEventDao.selectAnaesEventByCodeAndDocId(anaesRecord.getAnaRecordId(), EvtAnaesEventService.OUT_ROOM);// 出室
        	if(null != outEnd){
        		evtAnaesEventDao.deleteByPrimaryKey(outEnd.getAnaEventId());
        		
        		Date endTime = new Date(); //获取到页面传过来的结束时间
                Date dbEndTime = basMonitorDisplayDao.findEndTime(regOptId, searchBean.getDocType()); //获取数据库的
                if(null != dbEndTime){
                    long pageEndTimeLong = endTime.getTime();
                    long dbEndTimeLong = dbEndTime.getTime();
                    if(pageEndTimeLong < dbEndTimeLong){ // 页面传过来的时间  小于等于  数据库最后一个点的时间
                        //删除掉无用数据
                        basMonitorDisplayDao.deleteByEndTime(SysUtil.getTimeFormat(dbEndTime), regOptId, searchBean.getDocType());
                    }
                }
        	}
        	
        	//将手术Id添加到采集配置表中
            BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(operRoomId, getBeid());
            collectConfig.setPatientId(regOptId);
            basCollectConfigDao.updateByPrimaryKey(collectConfig);
        	
        	/*//添加一条再次手术的开始事件
        	EvtAnaesEvent againEvent = new EvtAnaesEvent();
        	againEvent.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
        	againEvent.setCode(40);
        	againEvent.setDocId(anaesRecord.getAnaRecordId());
        	againEvent.setDocType(1);
        	againEvent.setOccurTime(new Date());
        	evtAnaesEventDao.insertSelective(againEvent);*/
        }

    }
    
	/**
	 * 波形图数据查询
	 * @param bean
	 * @param observeIds
	 * @return
	 */
	public List<BasMonitorDisplay> getobsWave(MonitorDataFormBean bean,List<String> observeIds){
		String regOptId = bean.getRegOptId();
//		Date time = bean.getInTime();
//		logger.info("getobsWave---Time-----------"+time);
//		String inTime = SysUtil.getTimeFormat(time);
		//查询前先删除5秒前的数据,避免表数据量过大
		basObserveDataDao.deleteObsWave();
		return basObserveDataDao.searchObserveWaveList(regOptId, observeIds);
	}
    
	/**
	 * 波形图数据查询
	 * @param bean
	 * @param observeIds
	 * @return
	 */
	public List<BasMonitorDisplay> getobsSeconds(MonitorDataFormBean bean, List<String> observeNames){
		String regOptId = bean.getRegOptId();
		return basObserveDataDao.searchObserveSecondsList(regOptId, observeNames);
	}
}
