/*
 * DocBradenDetail.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2018-10-17 Created
 */
package com.digihealth.anesthesia.doc.po;

public class DocBradenDetail {
    private String id;

    /**
     * ��������ID
     */
    private String evaluateId;

    /**
     * ����
     */
    private String type;

    /**
     * �����ֵ
     */
    private Integer ageSco;

    /**
     * ����ָ����ֵ
     */
    private Integer bmiSco;

    /**
     * ������Ƥ����ֵ
     */
    private Integer skinSco;

    /**
     * ������λ��ֵ
     */
    private Integer optBodySco;

    /**
     * Ԥ������ʩ��������ֵ
     */
    private Integer exforceSco;

    /**
     * Ԥ������ʱ���ֵ
     */
    private Integer operTimeSco;

    /**
     * �����������ط�ֵ
     */
    private Integer operFactorSco;

    /**
     * �����ܷ�
     */
    private Integer totalSco;

    /**
     * Σ�ճ̶�
     */
    private String dangerLevel;

    /**
     * ������
     */
    private String evaluator;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(String evaluateId) {
        this.evaluateId = evaluateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAgeSco() {
        return ageSco;
    }

    public void setAgeSco(Integer ageSco) {
        this.ageSco = ageSco;
    }

    public Integer getBmiSco() {
        return bmiSco;
    }

    public void setBmiSco(Integer bmiSco) {
        this.bmiSco = bmiSco;
    }

    public Integer getSkinSco() {
        return skinSco;
    }

    public void setSkinSco(Integer skinSco) {
        this.skinSco = skinSco;
    }

    public Integer getOptBodySco() {
        return optBodySco;
    }

    public void setOptBodySco(Integer optBodySco) {
        this.optBodySco = optBodySco;
    }

    public Integer getExforceSco() {
        return exforceSco;
    }

    public void setExforceSco(Integer exforceSco) {
        this.exforceSco = exforceSco;
    }

    public Integer getOperTimeSco() {
        return operTimeSco;
    }

    public void setOperTimeSco(Integer operTimeSco) {
        this.operTimeSco = operTimeSco;
    }

    public Integer getOperFactorSco() {
        return operFactorSco;
    }

    public void setOperFactorSco(Integer operFactorSco) {
        this.operFactorSco = operFactorSco;
    }

    public Integer getTotalSco() {
        return totalSco;
    }

    public void setTotalSco(Integer totalSco) {
        this.totalSco = totalSco;
    }

    public String getDangerLevel() {
        return dangerLevel;
    }

    public void setDangerLevel(String dangerLevel) {
        this.dangerLevel = dangerLevel;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }
}