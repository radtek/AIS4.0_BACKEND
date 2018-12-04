package com.digihealth.anesthesia.operProceed.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.config.OperationState;
import com.digihealth.anesthesia.basedata.formbean.BaseInfoQuery;
import com.digihealth.anesthesia.basedata.po.BasCollectConfig;
import com.digihealth.anesthesia.basedata.po.BasDispatch;
import com.digihealth.anesthesia.basedata.po.BasOperroom;
import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.basedata.po.Controller;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.common.utils.JsonType;
import com.digihealth.anesthesia.common.utils.SpringContextHolder;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.doc.po.DocAnaesPacuRec;
import com.digihealth.anesthesia.doc.po.DocAnaesRecord;
import com.digihealth.anesthesia.evt.formbean.SearchFormBean;
import com.digihealth.anesthesia.evt.formbean.SearchOperaPatrolRecordFormBean;
import com.digihealth.anesthesia.evt.po.EvtAnaesEvent;
import com.digihealth.anesthesia.evt.po.EvtParticipant;
import com.digihealth.anesthesia.evt.po.EvtRealAnaesMethod;
import com.digihealth.anesthesia.evt.service.EvtAnaesEventService;
import com.digihealth.anesthesia.evt.service.EvtParticipantService;
import com.digihealth.anesthesia.sysMng.formbean.UserInfoFormBean;

@Service
public class MyOperationDataService extends BaseService {
	
	private BasAnaesMonitorConfigService basAnaesMonitorConfigService = SpringContextHolder.getBean(BasAnaesMonitorConfigService.class);
	private BasMonitorConfigService basMonitorConfigService = SpringContextHolder.getBean(BasMonitorConfigService.class);
	
	@Transactional
	public void getStartOperDate(SearchFormBean searchBean,Map result,boolean flagInsert){
		String regOptId = searchBean.getRegOptId();
		
		//麻醉记录单
        DocAnaesRecord anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(regOptId);
        //设置文书ID
        searchBean.setDocId(anaesRecord.getAnaRecordId());
        // 手术信息表
        BasRegOpt opt = basRegOptDao.searchRegOptById(regOptId);
		
		//AccessSource有值时代表是术中模块进入 ,为空值时则表示术后查看麻醉记录单
        if(StringUtils.isNotBlank(searchBean.getAccessSource())){
            
            BaseInfoQuery baseQuery = new BaseInfoQuery();
            //baseQuery.setOperRoomId(Global.getConfig("roomId").toString());
            baseQuery.setState(OperationState.SURGERY);

            /*
             * 同一个手术间可开始多台局麻或可开始一台全麻及多台局麻，但是不能同时开多台全麻
             * 
             * 1、当提交开始手术为局麻时直接跳过未完成手术的判断
             * 2、当提交开始手术为全麻时获取未完成的手术列表中存在全麻时，提示有未完成的全麻手术！
             */
            
            if("0".equals(opt.getIsLocalAnaes())){
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
                        result.put("resultCode", "40000004");
                        result.put("resultMessage", po.getOperRoomName()+"存在病人信息为："+po.getName()+",手术未完成!");
                        result.put("id", po.getRegOptId());
                        logger.info("------------------end startOper------------------------");
                        throw new RuntimeException(result.get("resultMessage").toString());
                        //return JsonType.jsonType(result);
                    }
                }
                
            }
            
            //当source传入start表示进入手术室，需要更新状态为术中，否则不更新当前状态
            if("start".equals(searchBean.getAccessSource())){
                Controller controller = controllerDao.getControllerById(regOptId);
                if(controller.getState().equals(OperationState.PREOPERATIVE)){
                	
                	//Controller controller = getControllerById(regOptId);
                    controllerDao.checkOperation(regOptId, OperationState.SURGERY, controller.getState());
                    //controllerService.changeControllerState(searchBean.getRegOptId(), OperationState.SURGERY);
                    flagInsert = true;
                }
            }
            //麻醉记录单
            anaesRecord = docAnaesRecordDao.searchAnaesRecordByRegOptId(searchBean.getRegOptId());
                
            logger.info("startOper===anaesRecord---"+JsonType.jsonType(anaesRecord)+"---------");
            //设置文书ID
            searchBean.setDocId(anaesRecord.getAnaRecordId());
            // 手术信息表
            opt = basRegOptDao.searchRegOptById(regOptId);
            //获取排程信息
            BasDispatch dispatch = basDispatchDao.getDispatchOper(regOptId);
            
            boolean flag = false;
            //当麻醉记录表中的手术体位为空时则将排程表中的手术体位字段写入
            if(StringUtils.isBlank(anaesRecord.getOptBody()) && StringUtils.isNotBlank(dispatch.getOptBody())){
                anaesRecord.setOptBody(dispatch.getOptBody());
                flag = true;
            }
            
            //当麻醉记录表中的手术等级为空时则将基本信息表中的手术等级字段写入
            if(StringUtils.isBlank(anaesRecord.getOptLevel()) && StringUtils.isNotBlank(opt.getOptLevel())){
                anaesRecord.setOptLevel(opt.getOptLevel());
                flag = true;
            }
            
            //当麻醉记录表中的手术室为空时则将排程表中的手术室字段写入
            String operRoomId = dispatch.getOperRoomId();
            if(StringUtils.isBlank(anaesRecord.getOperRoomName()) && null != operRoomId)
            {
                BasOperroom operroom = basOperroomDao.queryRoomListById(operRoomId.toString(),getBeid());
                anaesRecord.setOperRoomName(null == operroom ? null : operroom.getName()); 
                flag = true;
            }
            if (flag)
            {
            	docAnaesRecordDao.updateByPrimaryKeySelective(anaesRecord);
            }
            
            if(flagInsert){
                //开始手术时，将regOptId写入到采集配置表中对应的手术室中
                BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(operRoomId, getBeid());
                collectConfig.setPatientId(regOptId);
                basCollectConfigDao.updateByPrimaryKey(collectConfig);
                
                // 实际麻醉方法
                List<EvtRealAnaesMethod> realList = evtRealAnaesMethodDao.searchRealAnaesMethodList(searchBean);
                if(realList.size()<1){
                    if(StringUtils.isNotBlank(opt.getDesignedAnaesMethodCode())){
                        String[] methodArray = opt.getDesignedAnaesMethodCode().split(",");
                        String[] methodNameArray = opt.getDesignedAnaesMethodName().split(",");
                        for (int i = 0; i < methodArray.length; i++) {
                            String anaMedId = methodArray[i];
                            String realAnaMedName = methodNameArray[i];
                            EvtRealAnaesMethod realAnaesMethod = new EvtRealAnaesMethod();
                            realAnaesMethod.setDocId(searchBean.getDocId());
                            realAnaesMethod.setAnaMedId(anaMedId);
                            realAnaesMethod.setName(realAnaMedName);
                            realAnaesMethod.setRealAnaMedId(GenerateSequenceUtil.generateSequenceNo());
                            evtRealAnaesMethodDao.insertSelective(realAnaesMethod);
                        }
                    }
                }
                // 手术医生人员
                searchBean.setRole(EvtParticipantService.ROLE_OPERAT);
                List<EvtParticipant> participantList = evtParticipantDao.searchParticipantList(searchBean,getBeid());
                /*
                 * 进入手术时，判断是否编辑了手术人员信息
                 * 如果存在则跳过读取regOpt表的手术人员记录
                 */
                if(participantList.size()<1){
                     //将RegOpt表中的主刀手术医生字段写入到participant表
                    if(StringUtils.isNotBlank(opt.getOperatorId())){
                        String[] optArray = opt.getOperatorId().split(",");
                        for (int i = 0; i < optArray.length; i++) {
                            String operatorId = optArray[i];
                            EvtParticipant participant = new EvtParticipant();
                            participant.setDocId(searchBean.getDocId());
                            participant.setRole(EvtParticipantService.ROLE_OPERAT);
                            participant.setOperatorType("07"); //主刀医生
                            participant.setUserLoginName(operatorId);
                            participant.setPartpId(GenerateSequenceUtil.generateSequenceNo());
                            evtParticipantDao.insertSelective(participant);
                        }
                    }
                    //助手医生
                    if(StringUtils.isNotBlank(opt.getAssistantId())){
                        String[] assArray = opt.getAssistantId().split(",");
                        for (int i = 0; i < assArray.length; i++) {
                            String assistantId = assArray[i];
                            EvtParticipant participant = new EvtParticipant();
                            participant.setDocId(searchBean.getDocId());
                            participant.setRole(EvtParticipantService.ROLE_OPERAT);
                            participant.setOperatorType("07"); //助手医生
                            participant.setUserLoginName(assistantId);
                            participant.setPartpId(GenerateSequenceUtil.generateSequenceNo());
                            evtParticipantDao.insertSelective(participant);
                        }
                    }
                }
            }
        }
        
	}
	/**
	 * 复苏记录单开始手术接口
	 * @param searchBean
	 * @param result
	 */
	@Transactional
	public void getPacuStartOperDate(SearchFormBean searchBean,Map result){
		
		String regOptId = searchBean.getRegOptId();
		DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
		
		//AccessSource有值时代表是术中模块进入 ,为空值时则表示术后查看麻醉记录单
        if(StringUtils.isNotBlank(searchBean.getAccessSource())){
        	//当source传入start表示进入手术室，需要更新状态为术中，否则不更新当前状态
            if("start".equals(searchBean.getAccessSource())){
                
                //开始复苏时，将regOptId写入到采集配置表中对应的手术室中
                BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(anaesPacuRec.getBedId(), getBeid());
                collectConfig.setPatientId(regOptId);
                basCollectConfigDao.updateByPrimaryKey(collectConfig);

                EvtAnaesEvent outPacu = evtAnaesEventDao.selectAnaesEventByCodeAndDocId(searchBean.getDocId(), EvtAnaesEventService.OUT_PACU_ROOM);
                //pacu出室事件不为空，说明是再次进入复苏室
                if (null != outPacu)
                {
                    evtAnaesEventDao.deleteByPrimaryKey(outPacu.getAnaEventId());
                    anaesPacuRec.setExitTime(null);
                    anaesPacuRec.setProcessState("NO_END");
                    anaesPacuRec.setLeaveTo(null);
                    docAnaesPacuRecDao.updateByPrimaryKey(anaesPacuRec);
                }
            }
        }
        
        if (StringUtils.isNotBlank(anaesPacuRec.getDoctorId()))
        {
            List<UserInfoFormBean> anaDocList = new ArrayList<UserInfoFormBean>();
            String[] doctorIds = anaesPacuRec.getDoctorId().split(",");
            for (String id : doctorIds)
            {
                UserInfoFormBean userInfo = new UserInfoFormBean();
                String name = basUserDao.selectNameByUserName(id, getBeid());
                userInfo.setId(id);
                userInfo.setName(name);
                anaDocList.add(userInfo);
            }
            anaesPacuRec.setAnesDocList(anaDocList);
        }
        
        if (StringUtils.isNotBlank(anaesPacuRec.getNurseId()))
        {
            List<UserInfoFormBean> nurseList = new ArrayList<UserInfoFormBean>();
            String[] nurseIds = anaesPacuRec.getNurseId().split(",");
            for (String id : nurseIds)
            {
                UserInfoFormBean userInfo = new UserInfoFormBean();
                String name = basUserDao.selectNameByUserName(id, getBeid());
                userInfo.setId(id);
                userInfo.setName(name);
                nurseList.add(userInfo);
            }
            anaesPacuRec.setNurseList(nurseList);
        }
        
        result.put("anaesPacuRec", anaesPacuRec);
	}
}
