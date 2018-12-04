/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.digihealth.anesthesia.basedata.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.formbean.BasDeviceConfigFormBean;
import com.digihealth.anesthesia.basedata.formbean.BasDeviceConfigOperateFormBean;
import com.digihealth.anesthesia.basedata.formbean.BaseInfoQuery;
import com.digihealth.anesthesia.basedata.formbean.OperRoomFormBean;
import com.digihealth.anesthesia.basedata.po.BasDeviceConfig;
import com.digihealth.anesthesia.basedata.po.BasMonitorConfigFreq;
import com.digihealth.anesthesia.basedata.po.BasRegionBed;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfig;

/**
 * 
 * Title: DeviceConfigService.java Description: 设备型号Service
 * 
 * @author liukui
 * @created 2015-10-7 下午6:00:54
 */
@Service
public class BasDeviceConfigService extends BaseService {
	private static final Integer ENBLE = 1; // 床旁设备可用

	public List<BasDeviceConfig> getDeviceConfigList(String bedId) {
		return basDeviceConfigDao.getDeviceConfigList(getBeid(),StringUtils.isNotBlank(bedId) ? bedId : getCurRoomId(null));
	}

	public List<BasDeviceConfig> getEnableDeviceConfigList(String roomId) {
		return basDeviceConfigDao.getEnableDeviceConfigList(getBeid(),StringUtils.isNotBlank(roomId) ? roomId : getCurRoomId(null));
	}

	@Transactional
	public void saveDeviceConfig(BasDeviceConfigOperateFormBean deviceConfigOperateFormBean) {
		if (deviceConfigOperateFormBean != null) {
			
			String roomId = getCurRoomId(null);

			// 只有当传入的deviceConfig及子集不为空才保存当下数据
			if (deviceConfigOperateFormBean.getDeviceConfig() != null && deviceConfigOperateFormBean.getDeviceMonitorConfigList() != null) {

				BasDeviceConfig deviceConfig = deviceConfigOperateFormBean.getDeviceConfig();
				if (StringUtils.isBlank(deviceConfig.getBeid())) {
					deviceConfig.setBeid(getBeid());
				}

				deviceConfig.setRoomId(roomId);
				deviceConfig.setId(GenerateSequenceUtil.generateSequenceNo());
				// 先删除床旁设备配置表的数据，再做新增处理
				basDeviceConfigDao.deleteByPrimaryKey(deviceConfig);
				if (null == deviceConfig.getEnable()) {
					deviceConfig.setEnable(ENBLE);
				}
				basDeviceConfigDao.insert(deviceConfig);
			}
			// 保存采集频率配置
			if (deviceConfigOperateFormBean.getMonitorConfigFreqList() != null) {
				List<BasMonitorConfigFreq> freqList = deviceConfigOperateFormBean.getMonitorConfigFreqList();
				for (BasMonitorConfigFreq monitorConfigFreq : freqList) {
					if (StringUtils.isBlank(monitorConfigFreq.getBeid())) {
						monitorConfigFreq.setBeid(getBeid());
					}
					basMonitorConfigFreqDao.update(monitorConfigFreq);
				}
			}
		}
	}

	/**
	 * 获取可用串口信息列表
	 *//*
	@Transactional
	@SuppressWarnings("rawtypes")
	public void listPortChoices() {
		logger.info(" ---- start get Serial port ---- ");
		
		*//**
		 * 如果是直接访问控制中心，直接在BasDictItem手动配置串口信息
		 *//*
		if(CustomConfigureUtil.isSync()){
			return;
		}
		
		CommPortIdentifier portId;
		Enumeration en = CommPortIdentifier.getPortIdentifiers();
		// iterate through the ports.
		// 删除码表中配置的串口信息
		basDictItemDao.deleteDictItemGroup("serial_port", getBeid()+getCurRoomId(null));

		boolean hasSerial = en.hasMoreElements();

		if (!hasSerial) {
			logger.info(" ---- current system has no Serial port ---- ");
		}
		int i = 1;
		while (en.hasMoreElements()) {
			portId = (CommPortIdentifier) en.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				String str = portId.getName();
				logger.info("listPortChoices------" + str);

				BasDictItem dictItem = new BasDictItem();
				dictItem.setGroupId("serial_port");
				dictItem.setCodeValue(str);
				dictItem.setCodeName(str);
				dictItem.setOrder(i++);
				dictItem.setBeid(getBeid()+getCurRoomId(null));
				basDictItemDao.insertSelective(dictItem);
			}
		}
	}*/
	
	@Transactional
	public void saveBeidDeviceConfig(BasDeviceConfigFormBean record) {
		
		BasDeviceConfig basDeviceConfig = record.getBasDeviceConfig();
		basDeviceConfig.setEnable(0);
		
		//手术室启用处理
		if("1".equals(record.getChecked())){
		    basDeviceConfig.setRoomId("0");
		    BasDeviceConfig deviceConfig = basDeviceConfigDao.selectByPrimaryKey("0", basDeviceConfig.getDeviceId(), basDeviceConfig.getBeid());
            if (null == deviceConfig)
            {
    		    basDeviceConfigDao.insert(basDeviceConfig);
    			
    			List<BasMonitorConfig> monitorList = basMonitorConfigDao.selectMonitorListByDeviceType(basDeviceConfig.getBeid(), basDeviceConfig.getDeviceType());
    			if(null!=monitorList && monitorList.size()>0){
    				for (BasMonitorConfig basMonitorConfig : monitorList) {
    					basMonitorConfig.setBeid(basDeviceConfig.getBeid());
    					basMonitorConfigDao.insert(basMonitorConfig);
    				}
    			}
    			
    			BaseInfoQuery baseQuery = new BaseInfoQuery();
                baseQuery.setBeid(record.getBeid());
                List<OperRoomFormBean> roomList = basOperroomDao.findList(baseQuery);
                
                for (OperRoomFormBean operRoomFormBean : roomList) {
                    basDeviceConfig.setRoomId(operRoomFormBean.getOperRoomId());
                    basDeviceConfigDao.insert(basDeviceConfig);
                }
            }
		}else{//否则就删除局点对应的设备信息
			basDeviceConfigDao.deleteDeviceConfig(basDeviceConfig.getBeid(), basDeviceConfig.getDeviceId(), "0");
			BaseInfoQuery baseQuery = new BaseInfoQuery();
            baseQuery.setBeid(record.getBeid());
            List<OperRoomFormBean> roomList = basOperroomDao.findList(baseQuery);
            for (OperRoomFormBean operRoomFormBean : roomList) {
                basDeviceConfigDao.deleteDeviceConfig(basDeviceConfig.getBeid(), basDeviceConfig.getDeviceId(), operRoomFormBean.getOperRoomId());
            }
		}
		
		//复苏室启用处理
        if("1".equals(record.getPacuChecked())){
            basDeviceConfig.setRoomId("00");
            BasDeviceConfig deviceConfig = null;
            deviceConfig = basDeviceConfigDao.selectByPrimaryKey("00", basDeviceConfig.getDeviceId(), basDeviceConfig.getBeid());
            if (null == deviceConfig)
            {
                basDeviceConfigDao.insert(basDeviceConfig);
                List<BasMonitorConfig> monitorList = basMonitorConfigDao.selectMonitorListByDeviceType(basDeviceConfig.getBeid(), basDeviceConfig.getDeviceType());
                if(null!=monitorList && monitorList.size()>0){
                    for (BasMonitorConfig basMonitorConfig : monitorList) {
                        basMonitorConfig.setBeid(basDeviceConfig.getBeid());
                        basMonitorConfigDao.insert(basMonitorConfig);
                    }
                }
                
                List<BasRegionBed>  pacuBedList = basRegionBedDao.getregionbedList(null, basDeviceConfig.getBeid());
                for (BasRegionBed bed : pacuBedList) {
                    basDeviceConfig.setRoomId(bed.getId());
                    basDeviceConfigDao.insert(basDeviceConfig);
                }
            }
        }else{//否则就删除局点对应的设备信息
            basDeviceConfigDao.deleteDeviceConfig(basDeviceConfig.getBeid(), basDeviceConfig.getDeviceId(), "00");
            List<BasRegionBed>  pacuBedList = basRegionBedDao.getregionbedList(null, basDeviceConfig.getBeid());
            for (BasRegionBed bed : pacuBedList) {
                basDeviceConfigDao.deleteDeviceConfig(basDeviceConfig.getBeid(), basDeviceConfig.getDeviceId(), bed.getId());
            }
        }

	}
}
