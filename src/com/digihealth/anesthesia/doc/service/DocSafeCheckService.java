/**     
 * @discription 在此输入一句话描述此文件的作用
 * @author chengwang       
 * @created 2015-10-10 下午5:32:33    
 * tags     
 * see_to_target     
 */

package com.digihealth.anesthesia.doc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.po.BasDiagnosedef;
import com.digihealth.anesthesia.basedata.po.BasOperdef;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.common.utils.PingYinUtil;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.doc.po.DocSafeCheck;
import com.digihealth.anesthesia.evt.po.EvtOptLatterDiag;
import com.digihealth.anesthesia.evt.po.EvtOptRealOper;

/**
 * Title: SafeCheckService.java Description: 描述
 * 
 * @author chengwang
 * @created 2015-10-10 下午5:32:33
 */
@Service
public class DocSafeCheckService extends BaseService {

	/**
	 * 
	 * @discription 根据手术ID获取手术核查
	 * @author chengwang
	 * @created 2015-10-10 下午5:13:48
	 * @param regOptId
	 * @return
	 */
	public DocSafeCheck searchSafeCheckByRegOptId(String regOptId) {
		return docSafeCheckDao.searchSafeCheckByRegOptId(regOptId, getBeid());
	}

	/**
	 * 
	 * @discription 通过ID查询手术核查
	 * @author chengwang
	 * @created 2015-10-20 下午1:44:32
	 * @param id
	 * @return
	 */
	public DocSafeCheck searchSafeCheckById(String id) {
		return docSafeCheckDao.searchSafeCheckById(id);
	}

	/**
	 * 
	 * @discription 保存手术核查
	 * @author chengwang
	 * @created 2015-10-20 下午1:44:18
	 * @param preVisit
	 * @return
	 */
	@Transactional
	public String updateSafeCheck(DocSafeCheck safeCheck) {
		
		String realOptName = "";
		List<BasOperdef> optRealOperList = safeCheck.getRealOptNameList();
		if (null != optRealOperList && optRealOperList.size() > 0){
            for (BasOperdef optRealOper : optRealOperList)
            {
                if (StringUtils.isBlank(optRealOper.getOperdefId()))
                {
                    String operDefId = GenerateSequenceUtil.generateSequenceNo();
                    optRealOper.setOperdefId(operDefId);
                    BasOperdef operdef = new BasOperdef();
                    operdef.setOperdefId(operDefId);
                    operdef.setName(optRealOper.getName());
                    operdef.setPinYin(PingYinUtil.getFirstSpell(optRealOper.getName()));
                    operdef.setEnable(1);
                    operdef.setBeid(getBeid());
                    basOperdefDao.insert(operdef);
                }
                realOptName += optRealOper.getOperdefId()+",";
            }
        }
		if(StringUtils.isNotBlank(realOptName)){
			realOptName = realOptName.substring(0, realOptName.length()-1);
		}
		safeCheck.setRealOptName(realOptName);
		
		String realDiagnosisName = "";
		List<BasDiagnosedef> optLatterDiagList = safeCheck.getRealDiagnosisNameList();
		if (null != optLatterDiagList && optLatterDiagList.size() > 0) {
			for (BasDiagnosedef optLatterDiag : optLatterDiagList) {
				if (StringUtils.isBlank(optLatterDiag.getDiagDefId())) {
				    String diagDefId = GenerateSequenceUtil.generateSequenceNo();
					optLatterDiag.setDiagDefId(diagDefId);
					BasDiagnosedef diagnosedef = new BasDiagnosedef();
	                diagnosedef.setDiagDefId(diagDefId);
	                diagnosedef.setName(optLatterDiag.getName());
	                diagnosedef.setPinYin(PingYinUtil.getFirstSpell(optLatterDiag.getName()));
	                diagnosedef.setEnable(1);
	                diagnosedef.setBeid(getBeid());
	                basDiagnosedefDao.insert(diagnosedef);
				}
				realDiagnosisName += optLatterDiag.getDiagDefId()+",";
			}
		}
		if(StringUtils.isNotBlank(realDiagnosisName)){
			realDiagnosisName = realDiagnosisName.substring(0, realDiagnosisName.length()-1);
		}
		safeCheck.setRealDiagnosisName(realDiagnosisName);
		
//		Controller controller = controllerDao.getControllerById(safeCheck
//				.getRegOptId());
//		DocSafeCheck oldSafeCheck = searchSafeCheckById(safeCheck
//				.getSafCheckId());
		// if (controller.getState().equals(oldSafeCheck.getState())) {
		docSafeCheckDao.updateByPrimaryKey(safeCheck);
		// } else {
		// // oldSafeCheck.setFlag("0");
		// docSafeCheckDao.updateSafeCheck(oldSafeCheck);
		// SafeCheck newSafeCheck = new SafeCheck();
		// BeanHelper.copyProperties(safeCheck, newSafeCheck);
		// newSafeCheck.setState(controller.getState());
		// newSafeCheck.setFlag("1");
		// safeCheckDao.insert(newSafeCheck);
		// }
		return "true";
	}

}
