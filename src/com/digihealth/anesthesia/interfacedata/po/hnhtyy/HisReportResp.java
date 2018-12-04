package com.digihealth.anesthesia.interfacedata.po.hnhtyy;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.digihealth.anesthesia.interfacedata.formbean.hnhtyy.HisDocPatInspectRecFormBean;

@XmlRootElement(name = "Response")
public class HisReportResp
{
    private String resultCode;
    private String resultMessage;
    private List<HisDocPatInspectRecFormBean> inspectRec;
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
	public List<HisDocPatInspectRecFormBean> getInspectRec() {
		return inspectRec;
	}

	public void setInspectRec(List<HisDocPatInspectRecFormBean> inspectRec) {
		this.inspectRec = inspectRec;
	}
}
