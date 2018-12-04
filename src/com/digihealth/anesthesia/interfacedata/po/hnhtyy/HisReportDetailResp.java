package com.digihealth.anesthesia.interfacedata.po.hnhtyy;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.digihealth.anesthesia.interfacedata.formbean.hnhtyy.HisDocPatInspectRecDetailFormBean;
import com.digihealth.anesthesia.interfacedata.formbean.hnhtyy.HisDocPatInspectRecFormBean;
@XmlRootElement(name = "Response")
public class HisReportDetailResp
{
	
	private String inspectId;
    private String inspectName;
    private String deptName;
    private String doctorName;
    private String inspectTime;
    private String reportTime;
	private String resultCode;
    private String resultMessage;
    
    
    private List<HisDocPatInspectRecDetailFormBean> inspectRecDetail;
    public String getResultCode()
    {
        return resultCode;
    }
    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
    }
    public String getResultMessage()
    {
        return resultMessage;
    }
    public void setResultMessage(String resultMessage)
    {
        this.resultMessage = resultMessage;
    }
    
    @XmlElement(name = "item")
	public List<HisDocPatInspectRecDetailFormBean> getInspectRecDetail() {
		return inspectRecDetail;
	}
	public void setInspectRecDetail(List<HisDocPatInspectRecDetailFormBean> inspectRecDetail) {
		this.inspectRecDetail = inspectRecDetail;
	}
	public String getInspectId() {
		return inspectId;
	}
	public void setInspectId(String inspectId) {
		this.inspectId = inspectId;
	}
	public String getInspectName() {
		return inspectName;
	}
	public void setInspectName(String inspectName) {
		this.inspectName = inspectName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getInspectTime() {
		return inspectTime;
	}
	public void setInspectTime(String inspectTime) {
		this.inspectTime = inspectTime;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	
	

}
