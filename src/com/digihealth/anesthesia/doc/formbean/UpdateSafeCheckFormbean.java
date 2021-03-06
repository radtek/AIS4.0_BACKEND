package com.digihealth.anesthesia.doc.formbean;

import java.io.Serializable;

import com.digihealth.anesthesia.doc.po.DocAnaesBeforeSafeCheck;
import com.digihealth.anesthesia.doc.po.DocExitOperSafeCheck;
import com.digihealth.anesthesia.doc.po.DocOperBeforeSafeCheck;
import com.digihealth.anesthesia.doc.po.DocSafeCheck;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "手术安全核查单参数对象")
public class UpdateSafeCheckFormbean implements Serializable {

	@ApiModelProperty(value = "麻醉实施前安全核查单")
	private DocAnaesBeforeSafeCheck anaesBeforeSafeCheck;
	
	@ApiModelProperty(value = "手术开始前安全核查单")
	private DocOperBeforeSafeCheck operBeforeSafeCheck;

	@ApiModelProperty(value = "患者离开手术室前安全核查单")
	private DocExitOperSafeCheck exitOperSafeCheck;
	
	@ApiModelProperty(value = "手术安全核查单")
	private DocSafeCheck safeCheck;
	
	public DocAnaesBeforeSafeCheck getAnaesBeforeSafeCheck() {
		return anaesBeforeSafeCheck;
	}
	public void setAnaesBeforeSafeCheck(DocAnaesBeforeSafeCheck anaesBeforeSafeCheck) {
		this.anaesBeforeSafeCheck = anaesBeforeSafeCheck;
	}
	public DocExitOperSafeCheck getExitOperSafeCheck() {
		return exitOperSafeCheck;
	}
	public void setExitOperSafeCheck(DocExitOperSafeCheck exitOperSafeCheck) {
		this.exitOperSafeCheck = exitOperSafeCheck;
	}
	public DocOperBeforeSafeCheck getOperBeforeSafeCheck() {
		return operBeforeSafeCheck;
	}
	public void setOperBeforeSafeCheck(DocOperBeforeSafeCheck operBeforeSafeCheck) {
		this.operBeforeSafeCheck = operBeforeSafeCheck;
	}
    public DocSafeCheck getSafeCheck()
    {
        return safeCheck;
    }
    public void setSafeCheck(DocSafeCheck safeCheck)
    {
        this.safeCheck = safeCheck;
    }
	
}
