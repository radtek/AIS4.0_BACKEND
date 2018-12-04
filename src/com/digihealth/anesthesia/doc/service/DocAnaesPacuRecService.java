/**     
 * @discription 在此输入一句话描述此文件的作用
 * @author chengwang       
 * @created 2015-10-10 下午5:32:33    
 * tags     
 * see_to_target     
 */

package com.digihealth.anesthesia.doc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.basedata.po.BasCollectConfig;
import com.digihealth.anesthesia.basedata.po.BasDeviceConfig;
import com.digihealth.anesthesia.basedata.po.BasRegionBed;
import com.digihealth.anesthesia.basedata.po.Controller;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.doc.formbean.AnaesPacuChangeBedFormBean;
import com.digihealth.anesthesia.doc.formbean.AnaesPacuRecFormBean;
import com.digihealth.anesthesia.doc.po.DocAnaesPacuRec;
import com.digihealth.anesthesia.evt.formbean.Filter;
import com.digihealth.anesthesia.evt.po.EvtAnaesEvent;
import com.digihealth.anesthesia.evt.service.EvtAnaesEventService;
import com.digihealth.anesthesia.sysMng.formbean.UserInfoFormBean;
import com.google.common.base.Objects;

/**
 * Title: PreVisitService.java Description: 描述
 * 
 * @author chengwang
 * @created 2015-10-10 下午5:32:33
 */
@Service
public class DocAnaesPacuRecService extends BaseService {
	
	/**
	 * 恢复室卡片列表
	 * @return
	 */
	public List<AnaesPacuRecFormBean> getAnaesPacuRecCard(){
		return docAnaesPacuRecDao.getAnaesPacuRecCard(getBeid());
	}
	
	
	public AnaesPacuRecFormBean getOptInfoByPacuId(String id){
		return docAnaesPacuRecDao.getOptInfoByPacuId(id);
	}

	public List<AnaesPacuRecFormBean> searchAnaesPacuRecList(SystemSearchFormBean searchFormBean){
		if(StringUtils.isBlank(searchFormBean.getSort())){
			searchFormBean.setSort("id");
		}
		if(StringUtils.isBlank(searchFormBean.getOrderBy())){
			searchFormBean.setOrderBy("DESC");
		}
		List<Filter> filters = searchFormBean.getFilters();
		if(filters.size()==0){
			filters = new ArrayList<Filter>();
		}
		return docAnaesPacuRecDao.searchAnaesPacuRecList(searchFormBean,filters,getBeid());
	}
	public int searchTotalAnaesPacuRecList(SystemSearchFormBean searchFormBean){
		if(StringUtils.isBlank(searchFormBean.getSort())){
			searchFormBean.setSort("id");
		}
		if(StringUtils.isBlank(searchFormBean.getOrderBy())){
			searchFormBean.setOrderBy("DESC");
		}
		List<Filter> filters = searchFormBean.getFilters();
		if(filters.size()==0){
			filters = new ArrayList<Filter>();
		}
		return docAnaesPacuRecDao.searchTotalAnaesPacuRecList(searchFormBean,filters,getBeid());
	}
	
	@Transactional
	public void saveAnaesPacuRec(DocAnaesPacuRec record,ResponseValue resp) {
		//if(StringUtils.isBlank(record.getId())){
			String bedId = record.getBedId();
			String regOptId = record.getRegOptId();
            if(StringUtils.isNotBlank(bedId)){
				BasRegionBed regionBed = basRegionBedDao.selectByPrimaryKey(bedId);
				if(null != regionBed && !regOptId.equals(regionBed.getRegOptId()) && !Objects.equal(2, record.getAnabioticState())){
					if(regionBed.getStatus()==1){
						resp.setResultCode("100000000");
						resp.setResultMessage("该床位已被占用请选择其他床位!");
						return;
					}
					regionBed.setRegOptId(regOptId);
					regionBed.setStatus(1);
					basRegionBedDao.updateByPrimaryKey(regionBed);
				}
			}
			
			record.setPortablePipe(StringUtils.getStringByList(record.getPortablePipeList()));
			record.setPortableRes(StringUtils.getStringByList(record.getPortableResList()));
			
			if (null != record.getAnesDocList() && record.getAnesDocList().size() > 0)
			{
			    String doctorId = "";
			    for (UserInfoFormBean anaesDoc : record.getAnesDocList())
			    {
			        if ("".equals(doctorId))
			        {
			            doctorId = anaesDoc.getId();
			        }
			        else
			        {
			            doctorId = doctorId + "," + anaesDoc.getId();
			        }
			    }
			    record.setDoctorId(doctorId);
			}
			
			if (null != record.getNurseList() && record.getNurseList().size() > 0)
            {
                String nurseId = "";
                for (UserInfoFormBean nurse : record.getNurseList())
                {
                    if ("".equals(nurseId))
                    {
                        nurseId = nurse.getId();
                    }
                    else
                    {
                        nurseId = nurseId + "," + nurse.getId();
                    }
                }
                record.setNurseId(nurseId);
            }
			
			/*String processState = record.getProcessState();
			if(StringUtils.isNotBlank(processState) && "END".equals(processState)){
				BasRegionBed regionBed = basRegionBedDao.selectByPrimaryKey(record.getBedId().toString());
				regionBed.setStatus(0);
				regionBed.setRegOptId("");
				basRegionBedDao.updateByPrimaryKey(regionBed);
				record.setProcessState("END");
				record.setAnabioticState(2);
				Controller controller = controllerDao.getControllerById(regOptId);
				controller.setPreviousState(controller.getState());
				controller.setState("06");
				controllerDao.update(controller);
				//basRegOptDao.updateState(record.getRegOptId(), "06");
				
				EvtAnaesEvent anaesEventPacu = new EvtAnaesEvent();
                anaesEventPacu.setCode(EvtAnaesEventService.OUT_ROOM);
                anaesEventPacu.setOccurTime(record.getExitTime());
                if(null != record){
                    anaesEventPacu.setDocId(record.getId());
                }
                anaesEventPacu.setAnaEventId(GenerateSequenceUtil.generateSequenceNo());
                anaesEventPacu.setDocType(2);
                evtAnaesEventDao.insert(anaesEventPacu);
				
			}else{
				//如果手术状态为已结束，则直接将复苏状态改成已结束
				Controller controller = controllerDao.getControllerById(regOptId);
				if("06".equals(controller.getState())){
					record.setAnabioticState(2);
					record.setProcessState("END");
				}else{
					record.setAnabioticState(1);
				}
			}*/
			
			docAnaesPacuRecDao.updateByPrimaryKeySelective(record);
			
		//}
		
	}
	
	public DocAnaesPacuRec getAnaesPacuRecById(String id){
		return docAnaesPacuRecDao.selectByPrimaryKey(id);
	}
	
	public DocAnaesPacuRec getAnaesPacuRecByRegOptId(String regOptId){
		return docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(regOptId);
	}
	
	public boolean hasAnaesPacuByRegOptId(String regOptId){
		boolean flag = false;
		Integer count = docAnaesPacuRecDao.hasAnaesPacuRecByRegOptId(regOptId);
		if(count > 0) {
			flag =  true;
		}
		return flag;
	}
	
	@Transactional
    public void updatePacuRecEnterRoomTime(Date enterTime,String pacuRecId){
	    docAnaesPacuRecDao.updatePacuRecEnterRoomTime(enterTime,pacuRecId);
    }

	public List<BasDeviceConfig> getPacuDeviceByType(String bedId, String beid) {
		return basDeviceConfigDao.getDeviceConfigList(beid,bedId);
	}
	
	public List<BasDeviceConfig> getPacuDeviceConfigList(String bedId, String beid) {
		List<BasDeviceConfig> deviceConfigList = basDeviceConfigDao.getEnableDeviceConfigList(beid, bedId);
		return deviceConfigList;
	}

	/*@Transactional
	public void savePacuDeviceConfig(PacuDeviceEventFormBean bean) {
		BasPacuDeviceConfig pacuDeviceConfig = bean.getPacuDeviceConfig();
        if (StringUtils.isBlank(pacuDeviceConfig.getBeid())) {
			pacuDeviceConfig.setBeid(getBeid());
		}
        String beid = pacuDeviceConfig.getBeid();
		List<BasPacuBedEventConfig> bedEventConfigList = bean.getBedEventConfigList();
		//1、修改pacuDeviceConfig
		String bedId = pacuDeviceConfig.getBedId();
        BasPacuDeviceConfig pdc = basPacuDeviceConfigDao.selectByPrimaryKey(pacuDeviceConfig.getDeviceId(), bedId);
		if(null!=pdc){
			if(null == pacuDeviceConfig.getRoomId() || StringUtils.isBlank(pacuDeviceConfig.getRoomId())){
				String roomId = "";
				List<SysCodeFormbean> list = basDictItemDao.searchSysCodeByGroupId("revive_room", getBeid());
				if(null != list && list.size()>0){
					SysCodeFormbean sysCodeFormbean = list.get(0);
					roomId = sysCodeFormbean.getCodeValue();
					pacuDeviceConfig.setRoomId(roomId);
				}
			}
			basPacuDeviceConfigDao.updateByPrimaryKeySelective(pacuDeviceConfig);
		}else{
			if(null == pacuDeviceConfig.getRoomId() || StringUtils.isBlank(pacuDeviceConfig.getRoomId())){
				String roomId = "";
				List<SysCodeFormbean> list = basDictItemDao.searchSysCodeByGroupId("revive_room", getBeid());
				if(null != list && list.size()>0){
					SysCodeFormbean sysCodeFormbean = list.get(0);
					roomId = sysCodeFormbean.getCodeValue();
					pacuDeviceConfig.setRoomId(roomId);
				}
			}
			if(null == pacuDeviceConfig.getStatus()){
				pacuDeviceConfig.setStatus(-1);
			}
			basPacuDeviceConfigDao.insertSelective(pacuDeviceConfig);
		}
		//2、删除床位和event的列表
		List<PacuBedEventConfigFormBean> pacuBedList = basPacuBedEventConfigDao.selectByBedId(pacuDeviceConfig.getDeviceId(), bedId, beid);
		if(null != pacuBedList && pacuBedList.size()>0){
			basPacuBedEventConfigDao.deleteByBedId(pacuDeviceConfig.getDeviceId(), "", bedId);
		}
		// 新增b_pacu_bed_event_config
		if(null != bedEventConfigList && bedEventConfigList.size()>0){
			for (BasPacuBedEventConfig pacuBedEventConfig : bedEventConfigList) {
				pacuBedEventConfig.setBedId(bedId);
				pacuBedEventConfig.setDeviceId(pacuDeviceConfig.getDeviceId());
				pacuBedEventConfig.setBeid(getBeid()); 
				basPacuBedEventConfigDao.insertSelective(pacuBedEventConfig); 
			}
		}
		
		BasCollectConfig collectConfig = basCollectConfigDao.selectByPrimaryKey(bedId, beid);
		List<PacuDeviceConfigFormBean> deviceConfigList = basPacuDeviceConfigDao.selectByBedId(bedId, beid);
		if (null == deviceConfigList || deviceConfigList.size() < 1)
		{
		    collectConfig.setDevicesUsed(null);
		}
		else
        {
            String devicesUsed = "";
            for (PacuDeviceConfigFormBean device : deviceConfigList)
            {
                if ("".equals(devicesUsed))
                {
                    devicesUsed = device.getDevicemodel();
                }
                else
                {
                    devicesUsed = devicesUsed + "," + device.getDevicemodel();
                }
            }
            collectConfig.setDevicesUsed(devicesUsed);
        }
        basCollectConfigDao.updateByPrimaryKey(collectConfig);
	}

	@Transactional
	public void deletePacuDeviceConfig(String bedId, String deviceId) {
		basPacuDeviceConfigDao.deleteByPrimaryKey(deviceId, bedId);//删除pacu设备配置
		basPacuBedEventConfigDao.deleteByBedId(deviceId, "", bedId); //删除pacu采集配置记录列表
	}*/
	
	
	/**
	 * pacu换床
	 * @param bean
	 */
	@Transactional
	public void changeBedByPacuRec(AnaesPacuChangeBedFormBean bean,ResponseValue resp) {
		if(null != bean && StringUtils.isNotBlank(bean.getSourceBed()) && StringUtils.isNotBlank(bean.getTargetBed()) && StringUtils.isNotBlank(bean.getSourceRegOptId())){
			
			BasRegionBed targetBed = basRegionBedDao.selectByPrimaryKey(bean.getTargetBed());
			DocAnaesPacuRec targetRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(targetBed.getRegOptId());
			
			BasRegionBed soureceBed = basRegionBedDao.selectByPrimaryKey(bean.getSourceBed());
			DocAnaesPacuRec sourceRec = docAnaesPacuRecDao.getAnaesPacuRecByRegOptId(bean.getSourceRegOptId());
			
			//换床时，两床位都存在患者时
			if(targetBed!=null && StringUtils.isNotBlank(targetBed.getRegOptId())){
				String tarOptId = targetBed.getRegOptId();
				String souOptId = soureceBed.getRegOptId();
				targetBed.setRegOptId(souOptId);
				soureceBed.setRegOptId(tarOptId);
				
				basRegionBedDao.updateByPrimaryKeySelective(targetBed);
				basRegionBedDao.updateByPrimaryKeySelective(soureceBed);
				
				if(null!=targetRec){
					targetRec.setBedId(soureceBed.getId());
					docAnaesPacuRecDao.updateByPrimaryKey(targetRec);
					
				}
				
				BasCollectConfig souCollectConfig = basCollectConfigDao.selectByPrimaryKey(soureceBed.getId(), getBeid());
				souCollectConfig.setPatientId(tarOptId);
				basCollectConfigDao.updateByPrimaryKey(souCollectConfig);
				
			}else{
				String souOptId = soureceBed.getRegOptId();
				targetBed.setRegOptId(souOptId);
				targetBed.setStatus(1);
				basRegionBedDao.updateByPrimaryKey(targetBed);
				
				soureceBed.setRegOptId("");
				soureceBed.setStatus(0);
				basRegionBedDao.updateByPrimaryKey(soureceBed);
				
				BasCollectConfig souCollectConfig = basCollectConfigDao.selectByPrimaryKey(soureceBed.getId(), getBeid());
				souCollectConfig.setPatientId("");
				basCollectConfigDao.updateByPrimaryKey(souCollectConfig);
				
			}
			
			BasCollectConfig tarCollectConfig = basCollectConfigDao.selectByPrimaryKey(targetBed.getId(), getBeid());
			tarCollectConfig.setPatientId(bean.getSourceRegOptId());
			basCollectConfigDao.updateByPrimaryKey(tarCollectConfig);
			
			sourceRec.setBedId(targetBed.getId());
			docAnaesPacuRecDao.updateByPrimaryKey(sourceRec);
			
			
		}
		
	}
}
