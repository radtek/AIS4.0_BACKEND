package com.digihealth.anesthesia.basedata.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.formbean.BasConsumablesOutRecordFormBean;
import com.digihealth.anesthesia.basedata.formbean.BasConsumablesStorageFormbean;
import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.basedata.po.BasConsumablesInRecord;
import com.digihealth.anesthesia.basedata.po.BasConsumablesStorage;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.DateUtils;
import com.digihealth.anesthesia.common.utils.PingYinUtil;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.evt.formbean.Filter;

@Service
public class BasConsumablesStorageService extends BaseService
{
    //将审核后的药物入库
	@Transactional
	public void saveToConsumablesStorage(BasConsumablesInRecord basConsumablesInRecord)
	{
		//药品名字
		String instrumentName = basConsumablesInRecord.getInstrumentName();
		//批次
		String batch = basConsumablesInRecord.getBatch();
		
		BasConsumablesStorage oldBasConsumablesStorage = basConsumablesStorageDao.selectConsumablesByMFSB(instrumentName, batch);
		if(null == oldBasConsumablesStorage)
		{
			BasConsumablesStorage basConsumablesStorage = new BasConsumablesStorage();
			basConsumablesStorage.setBatch(batch);
			basConsumablesStorage.setBeid(getBeid());
			basConsumablesStorage.setEffectiveTime(basConsumablesInRecord.getEffectiveTime());
			//basConsumablesStorage.setFirm(firm);
			basConsumablesStorage.setInstrumentId(basConsumablesInRecord.getInstrumentId());
			basConsumablesStorage.setInstrumentName(instrumentName);
			basConsumablesStorage.setMinPackageUnit(basConsumablesInRecord.getMinPackageUnit());
			basConsumablesStorage.setNumber(basConsumablesInRecord.getNumber());
			//basConsumablesStorage.setSpec(spec);
			basConsumablesStorage.setPinYin(PingYinUtil.getFirstSpell(instrumentName));
			basConsumablesStorageDao.insertSelective(basConsumablesStorage);
		}else{
			int oldNumber = oldBasConsumablesStorage.getNumber();
			oldNumber += basConsumablesInRecord.getNumber();
			oldBasConsumablesStorage.setNumber(oldNumber);
			basConsumablesStorageDao.updateByPrimaryKeySelective(oldBasConsumablesStorage);
		}
		
	}
	
	
	//查询毒麻药入库信息列表,按名字，厂家，规格分组
	public List<BasConsumablesStorage> queryConsumablesListGroupByNFS(SystemSearchFormBean systemSearchFormBean)
	{
		if(StringUtils.isEmpty(systemSearchFormBean.getSort())){
			systemSearchFormBean.setSort("id");
		}
		if(StringUtils.isEmpty(systemSearchFormBean.getOrderBy())){
			systemSearchFormBean.setOrderBy("ASC");
		}
		
		List<Filter> filters = systemSearchFormBean.getFilters();
		if(null == filters)
		{
			filters = new ArrayList<Filter>();
		}
		Filter filter = new Filter();
		filter.setField("beid");
		filter.setValue(getBeid());
		filters.add(filter);
		
		return basConsumablesStorageDao.queryConsumablesListGroupByNFS(filters, systemSearchFormBean);
	}
	
	//查询毒麻药入库信息数量,按名字，厂家，规格分组
	public int queryConsumablesListGroupByNFSTotal(SystemSearchFormBean systemSearchFormBean)
	{
		List<Filter> filters = systemSearchFormBean.getFilters();
		return basConsumablesStorageDao.queryConsumablesListGroupByNFSTotal(filters);
	}
	

	//查询毒麻药入库信息列表
	public List<BasConsumablesStorage> queryConsumablesList(SystemSearchFormBean systemSearchFormBean)
	{
		if(StringUtils.isEmpty(systemSearchFormBean.getSort())){
			systemSearchFormBean.setSort("id");
		}
		if(StringUtils.isEmpty(systemSearchFormBean.getOrderBy())){
			systemSearchFormBean.setOrderBy("ASC");
		}
		
		List<Filter> filters = systemSearchFormBean.getFilters();
		if(null == filters)
		{
			filters = new ArrayList<Filter>();
		}
		Filter filter = new Filter();
		filter.setField("beid");
		filter.setValue(getBeid());
		filters.add(filter);
		
		return basConsumablesStorageDao.queryConsumablesList(filters, systemSearchFormBean);
	}
	
	//查询毒麻药入库信息数量
	public int queryConsumablesListTotal(SystemSearchFormBean systemSearchFormBean)
	{
		List<Filter> filters = systemSearchFormBean.getFilters();
		return basConsumablesStorageDao.queryConsumablesListTotal(filters);
	}
	
	//按月统计库存记录
	public List<BasConsumablesStorageFormbean> queryConsumablesByMonth(SystemSearchFormBean systemSearchFormBean,ResponseValue resp)
	{
		if(StringUtils.isEmpty(systemSearchFormBean.getSort())){
			systemSearchFormBean.setSort("id");
		}
		if(StringUtils.isEmpty(systemSearchFormBean.getOrderBy())){
			systemSearchFormBean.setOrderBy("ASC");
		}
		
		List<Filter> filters = systemSearchFormBean.getFilters();
		if(null == filters)
		{
			filters = new ArrayList<Filter>();
		}
		//指定的查询月份
		String queryMonth = "";
		for (Filter filter : filters)
		{
			if (filter.getField().equals("queryMonth"))
			{
				queryMonth = filter.getValue();
			}
		}
		//检查时间
		int month = Integer.parseInt(DateUtils.getMonth());
		int year = Integer.parseInt(queryMonth.substring(0, 4));
		int subMonth = Integer.parseInt(queryMonth.substring(5,7));
		if(year==Integer.parseInt(DateUtils.getYear()) && subMonth >= month)
        {
            resp.setResultCode("30000001");
         	resp.setResultMessage("查询月份没有过完，统计数据还有变动，不能进行统计！");
         	return null;
        }
		
		Filter filter = new Filter();
		filter.setField("beid");
		filter.setValue(getBeid());
		filters.add(filter);
		
		
		
		return basConsumablesStorageDao.queryConsumablesByMonth(filters, systemSearchFormBean,queryMonth);
	}
	
	//按月统计库存记录数量
	public int queryConsumablesByMonthTotal(SystemSearchFormBean systemSearchFormBean)
	{
		List<Filter> filters = systemSearchFormBean.getFilters();
		return basConsumablesStorageDao.queryConsumablesByMonthTotal(filters);
	}
	
	
	//按月统计个人用药情况
	public List<BasConsumablesOutRecordFormBean> queryConsumablesByPersonal(SystemSearchFormBean systemSearchFormBean)
	{
		if(StringUtils.isEmpty(systemSearchFormBean.getSort())){
			systemSearchFormBean.setSort("id");
		}
		if(StringUtils.isEmpty(systemSearchFormBean.getOrderBy())){
			systemSearchFormBean.setOrderBy("ASC");
		}
		
		List<Filter> filters = systemSearchFormBean.getFilters();
		if(null == filters)
		{
			filters = new ArrayList<Filter>();
		}
		Filter filter = new Filter();
		filter.setField("beid");
		filter.setValue(getBeid());
		filters.add(filter);
		
		
		return basConsumablesStorageDao.queryConsumablesByPersonal(filters, systemSearchFormBean);
	}
	
	//按月统计个人用药情况
	public int queryConsumablesByPersonalTotal(SystemSearchFormBean systemSearchFormBean)
	{
		List<Filter> filters = systemSearchFormBean.getFilters();
		return basConsumablesStorageDao.queryConsumablesByPersonalTotal(filters);
	}
	
	//查询当前局点下的库存记录
	public List<BasConsumablesStorage> queryConsumablesStorageByBeid()
	{
		return basConsumablesStorageDao.queryConsumablesStorageByBeid(getBeid());
	}
}
