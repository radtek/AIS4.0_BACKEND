package com.digihealth.anesthesia.operProceed.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfigDefault;

@Service
public class BasMonitorConfigDefaultService extends BaseService {
	public BasMonitorConfigDefault searchByEventName(String eventName, Integer docType) {
		return basMonitorConfigDefaultDao.selectByEventName(eventName, getBeid(), docType);
	}

	public List<BasMonitorConfigDefault> selectAll(Integer docType) {
		return basMonitorConfigDefaultDao.selectAll(getBeid(), docType);
	}
}
