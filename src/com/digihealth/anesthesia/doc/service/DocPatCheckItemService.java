package com.digihealth.anesthesia.doc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.doc.po.DocPatCheckItem;
import com.digihealth.anesthesia.evt.formbean.Filter;
@Service
@Transactional(readOnly = true)
public class DocPatCheckItemService extends BaseService{
	
	public List<DocPatCheckItem> getPatCheckItemList(SystemSearchFormBean searchFormBean){
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
		return docPatCheckItemDao.getPatCheckItemList(searchFormBean,filters);
	}
}
