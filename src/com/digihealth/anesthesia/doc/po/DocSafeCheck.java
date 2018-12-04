/*
 * DocSafeCheck.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-03-30 Created
 */
package com.digihealth.anesthesia.doc.po;

import java.util.ArrayList;
import java.util.List;

import com.digihealth.anesthesia.basedata.po.BasDiagnosedef;
import com.digihealth.anesthesia.basedata.po.BasOperdef;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "安全核查对象")
public class DocSafeCheck {
	@ApiModelProperty(value = "主键id")
    private String safCheckId;

	@ApiModelProperty(value = "患者ID")
    private String regOptId;

    /**
     * 特殊耗材条码粘贴处
     */
	@ApiModelProperty(value = "特殊耗材条码粘贴处")
    private String remarks;

    /**
     * 签字时间
     */
	@ApiModelProperty(value = "签字时间")
    private String signTime;

    /**
     * 巡回护士签名
     */
	@ApiModelProperty(value = "巡回护士签名")
    private String circunurseId;
	
	@ApiModelProperty(value = "巡回护士签名")
    private String circunurseName;

	@ApiModelProperty(value = "麻醉医生签名")
    private String anesthetistId;
	
	@ApiModelProperty(value = "手术医师签名")
    private String operatorId;
    /**
     * END,NO_END
     */
	@ApiModelProperty(value = "是否完成 END,NO_END")
    private String processState;

	@ApiModelProperty(value = "巡回护士签名")
    private List<String> circunurseIdList;

    @ApiModelProperty(value = "麻醉医生签名")
    private List<String> anesthetistIdList;
    
    @ApiModelProperty(value = "手术医师签名")
    private List<String> operatorIdList;
    
    @ApiModelProperty(value = "实施麻醉方法")
	private String realAnaesMethodName;
    
    @ApiModelProperty(value = "术后诊断")
	private String realDiagnosisName;
    
    @ApiModelProperty(value = "实施手术")
	private String realOptName;
    
	@ApiModelProperty(value = "实施手术")
    private List<BasOperdef> realOptNameList;

    @ApiModelProperty(value = "实施麻醉方法")
    private List<String> realAnaesMethodNameList;
    
    @ApiModelProperty(value = "术后诊断")
    private List<BasDiagnosedef> realDiagnosisNameList;
	
    
	
    public List<BasOperdef> getRealOptNameList() {
		return realOptNameList;
	}

	public void setRealOptNameList(List<BasOperdef> realOptNameList) {
		this.realOptNameList = realOptNameList;
	}

	public List<String> getRealAnaesMethodNameList() {
		return realAnaesMethodNameList==null?new ArrayList<String>():realAnaesMethodNameList;
	}

	public void setRealAnaesMethodNameList(List<String> realAnaesMethodNameList) {
		this.realAnaesMethodNameList = realAnaesMethodNameList;
	}

	public List<BasDiagnosedef> getRealDiagnosisNameList() {
		return realDiagnosisNameList;
	}

	public void setRealDiagnosisNameList(List<BasDiagnosedef> realDiagnosisNameList) {
		this.realDiagnosisNameList = realDiagnosisNameList;
	}

	public String getRealAnaesMethodName() {
		return realAnaesMethodName;
	}

	public void setRealAnaesMethodName(String realAnaesMethodName) {
		this.realAnaesMethodName = realAnaesMethodName;
	}

	public String getRealDiagnosisName() {
		return realDiagnosisName;
	}

	public void setRealDiagnosisName(String realDiagnosisName) {
		this.realDiagnosisName = realDiagnosisName;
	}

	public String getRealOptName() {
		return realOptName;
	}

	public void setRealOptName(String realOptName) {
		this.realOptName = realOptName;
	}

	public String getAnesthetistId()
    {
        return anesthetistId;
    }

    public void setAnesthetistId(String anesthetistId)
    {
        this.anesthetistId = anesthetistId;
    }

    public String getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId(String operatorId)
    {
        this.operatorId = operatorId;
    }

    public List<String> getCircunurseIdList()
    {
        return circunurseIdList;
    }

    public void setCircunurseIdList(List<String> circunurseIdList)
    {
        this.circunurseIdList = circunurseIdList;
    }

    public List<String> getAnesthetistIdList()
    {
        return anesthetistIdList;
    }

    public void setAnesthetistIdList(List<String> anesthetistIdList)
    {
        this.anesthetistIdList = anesthetistIdList;
    }

    public List<String> getOperatorIdList()
    {
        return operatorIdList;
    }

    public void setOperatorIdList(List<String> operatorIdList)
    {
        this.operatorIdList = operatorIdList;
    }

    public String getSafCheckId() {
        return safCheckId;
    }

    public void setSafCheckId(String safCheckId) {
        this.safCheckId = safCheckId == null ? null : safCheckId.trim();
    }

    public String getRegOptId() {
        return regOptId;
    }

    public void setRegOptId(String regOptId) {
        this.regOptId = regOptId == null ? null : regOptId.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getCircunurseId() {
		return circunurseId;
	}

	public void setCircunurseId(String circunurseId) {
		this.circunurseId = circunurseId;
	}

	public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState == null ? null : processState.trim();
    }

    public String getCircunurseName()
    {
        return circunurseName;
    }

    public void setCircunurseName(String circunurseName)
    {
        this.circunurseName = circunurseName;
    }
}