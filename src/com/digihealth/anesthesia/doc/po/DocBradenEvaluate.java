/*
 * DocBradenEvaluate.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2018-10-17 Created
 */
package com.digihealth.anesthesia.doc.po;

public class DocBradenEvaluate {
    private String id;

    /**
     * 手术Id
     */
    private String regOptId;

    /**
     * 文书状态
     */
    private String processState;

    /**
     * 体位
     */
    private String optBody;

    /**
     * 体位其他
     */
    private String optBodyOther;

    /**
     * 受压部位
     */
    private String pressurePart;

    /**
     * 受压部位其他
     */
    private String pressurePartOther;

    /**
     * 术前预防措施
     */
    private String prePrecaution;

    /**
     * 皮肤完整性
     */
    private String skinIntegrity;

    /**
     * 受压皮肤凝胶用具的使用
     */
    private String gelAppliance;

    /**
     * 术中预防措施
     */
    private String midPrecaution;

    /**
     * 改变局部受压力度
     */
    private String chgLocalPress;

    /**
     * 术中其他措施
     */
    private String midPrecautionOther;

    /**
     * 特殊情况备注
     */
    private String specialCase;

    /**
     * 特殊情况其他
     */
    private String specialCaseOther;

    /**
     * 术后皮肤情况
     */
    private String postSkinCase;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegOptId() {
        return regOptId;
    }

    public void setRegOptId(String regOptId) {
        this.regOptId = regOptId;
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public String getOptBody() {
        return optBody;
    }

    public void setOptBody(String optBody) {
        this.optBody = optBody;
    }

    public String getOptBodyOther() {
        return optBodyOther;
    }

    public void setOptBodyOther(String optBodyOther) {
        this.optBodyOther = optBodyOther;
    }

    public String getPressurePart() {
        return pressurePart;
    }

    public void setPressurePart(String pressurePart) {
        this.pressurePart = pressurePart;
    }

    public String getPressurePartOther() {
        return pressurePartOther;
    }

    public void setPressurePartOther(String pressurePartOther) {
        this.pressurePartOther = pressurePartOther;
    }

    public String getPrePrecaution() {
        return prePrecaution;
    }

    public void setPrePrecaution(String prePrecaution) {
        this.prePrecaution = prePrecaution;
    }

    public String getSkinIntegrity() {
        return skinIntegrity;
    }

    public void setSkinIntegrity(String skinIntegrity) {
        this.skinIntegrity = skinIntegrity;
    }

    public String getGelAppliance() {
        return gelAppliance;
    }

    public void setGelAppliance(String gelAppliance) {
        this.gelAppliance = gelAppliance;
    }

    public String getMidPrecaution() {
        return midPrecaution;
    }

    public void setMidPrecaution(String midPrecaution) {
        this.midPrecaution = midPrecaution;
    }

    public String getChgLocalPress() {
        return chgLocalPress;
    }

    public void setChgLocalPress(String chgLocalPress) {
        this.chgLocalPress = chgLocalPress;
    }

    public String getMidPrecautionOther() {
        return midPrecautionOther;
    }

    public void setMidPrecautionOther(String midPrecautionOther) {
        this.midPrecautionOther = midPrecautionOther;
    }

    public String getSpecialCase() {
        return specialCase;
    }

    public void setSpecialCase(String specialCase) {
        this.specialCase = specialCase;
    }

    public String getSpecialCaseOther() {
        return specialCaseOther;
    }

    public void setSpecialCaseOther(String specialCaseOther) {
        this.specialCaseOther = specialCaseOther;
    }

    public String getPostSkinCase() {
        return postSkinCase;
    }

    public void setPostSkinCase(String postSkinCase) {
        this.postSkinCase = postSkinCase;
    }
}