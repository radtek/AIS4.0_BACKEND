/**
 * 
 */
package com.digihealth.anesthesia.research.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.doc.po.DocPostFollowGeneral;
import com.digihealth.anesthesia.doc.po.DocPostFollowSpinal;
import com.digihealth.anesthesia.evt.formbean.SearchFormBean;
import com.digihealth.anesthesia.research.formbean.SearchOperFormBean;

/**
 * @author ChengW
 * 
 */
@Service
public class QCManagerService extends BaseService {

	public List<SearchOperFormBean> searchOperByCondition(SearchFormBean searchBean) {
		return statisticsDao.searchOperByCondition(searchBean);
	}

	public List<SearchOperFormBean> searchToICUOper(SearchFormBean searchBean) {
		return statisticsDao.searchToICUOper(searchBean, getBeid());
	}

	public List<SearchOperFormBean> searchOptVenipunctyreList(SearchFormBean searchBean)
    {
        return statisticsDao.searchOptVenipunctyreList(searchBean);
    }
	
	public List<SearchOperFormBean> searchVenipunctyreList(SearchFormBean searchBean) {
		return statisticsDao.searchVenipunctyreList(searchBean);
	}

	public List<SearchOperFormBean> searchCancleOperAfterAnaes(SearchFormBean searchBean) {
		return statisticsDao.searchCancleOperAfterAnaes(searchBean);
	}

	public List<SearchOperFormBean> searchASALevelOper(SearchFormBean searchBean) {
		return statisticsDao.searchASALevelOper(searchBean);
	}

	public List<SearchOperFormBean> searchAutograftBloodTrans400mlInfo(SearchFormBean searchBean) {
		return statisticsDao.searchAutograftBloodTrans400mlInfo(searchBean);
	}

	public List<SearchOperFormBean> searchBloodTrans400mlInfo(SearchFormBean searchBean) {
		return statisticsDao.searchBloodTrans400mlInfo(searchBean);
	}

    public List<SearchOperFormBean> searchAnaesPacuDelayRate(SearchFormBean searchFormBean){
    	return statisticsDao.searchAnaesPacuDelayRate(searchFormBean);
    }
    
    public List<SearchOperFormBean> searchEnterPacuNum(SearchFormBean searchFormBean){
    	if(StringUtils.isBlank(searchFormBean.getBeid())) {
    		searchFormBean.setBeid(getBeid());
    	}
    	return statisticsDao.searchEnterPacuNum(searchFormBean);
    }

	public List<SearchOperFormBean> searchAnaesPacuLowTemp(SearchFormBean searchFormBean){
		return statisticsDao.searchAnaesPacuLowTemp(searchFormBean);
	}

	public List<SearchOperFormBean> searchAnesSecondIntubatList(SearchFormBean searchFormBean){
		return statisticsDao.searchAnesSecondIntubatList(searchFormBean);
	}

	public List<SearchOperFormBean> searchAnesExtubatList(SearchFormBean searchFormBean){
		return statisticsDao.searchAnesExtubatList(searchFormBean);
	}

	public List<SearchOperFormBean> searchOptAllergicReactionList(SearchFormBean searchFormBean){
		return statisticsDao.searchOptAllergicReactionList(searchFormBean);
	}

	public List<SearchOperFormBean> searchOtherAnesList(SearchFormBean searchFormBean){
		return statisticsDao.searchOtherAnesList(searchFormBean);
	}

	public List<DocPostFollowSpinal> searchSpinalAneshrListInHours(SearchFormBean searchFormBean,Integer hours){
		return statisticsDao.searchSpinalAneshrListInHours(searchFormBean,hours);
	}

	public List<DocPostFollowGeneral> searchExtubat72hrHoarseList(SearchFormBean searchFormBean){
		return statisticsDao.searchExtubat72hrHoarseList(searchFormBean);
	}

	public List<SearchOperFormBean> searchAnes24hrCardiacArrest(SearchFormBean searchFormBean){
		return statisticsDao.searchAnes24hrCardiacArrest(searchFormBean);
	}

	public List<SearchOperFormBean> search24HourDeathRate(SearchFormBean searchFormBean){
		return statisticsDao.search24HourDeathRate(searchFormBean);
	}

	public List<SearchOperFormBean> searchQmFjgIcuNum(SearchFormBean searchFormBean){
		return statisticsDao.searchQmFjgIcuNum(searchFormBean);
	}
	
	public List<SearchOperFormBean> searchQmIcuNum(SearchFormBean searchFormBean){
		return statisticsDao.searchQmIcuNum(searchFormBean);
	}
	
	public List<SearchOperFormBean> searchJmIcuNum(SearchFormBean searchFormBean){
		return statisticsDao.searchJmIcuNum(searchFormBean);
	}

	public List<SearchOperFormBean> searchReunitWihtAnesList(SearchFormBean searchFormBean){
		return statisticsDao.searchReunitWihtAnesList(searchFormBean);
	}
	
	public List<SearchOperFormBean> searchAnaesQualityControlByType(String qcType, SearchFormBean searchFormBean)
	{
	    return statisticsDao.searchAnaesQualityControlByType(qcType, searchFormBean);
	}
	
	public List<SearchOperFormBean> searchAnaesPacuQualityControlByType(String qcType, SearchFormBean searchFormBean)
    {
        return statisticsDao.searchAnaesPacuQualityControlByType(qcType, searchFormBean);
    }
	
	public List<SearchOperFormBean> searchOperByConsciousness(SearchFormBean searchFormBean,String coma){
        return statisticsDao.searchOperByConsciousness(searchFormBean,coma);
    }
    
    public List<SearchOperFormBean> searchOperByHoarse(SearchFormBean searchFormBean,String hoarse, String tube){
        return statisticsDao.searchOperByHoarse(searchFormBean,hoarse,tube);
    }
}
