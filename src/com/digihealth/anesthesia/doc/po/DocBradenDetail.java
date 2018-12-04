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
     * 评估主表ID
     */
    private String evaluateId;

    /**
     * 类型
     */
    private String type;

    /**
     * 年龄分值
     */
    private Integer ageSco;

    /**
     * 体质指数分值
     */
    private Integer bmiSco;

    /**
     * 受力点皮肤分值
     */
    private Integer skinSco;

    /**
     * 手术体位分值
     */
    private Integer optBodySco;

    /**
     * 预计术中施加外力分值
     */
    private Integer exforceSco;

    /**
     * 预计手术时间分值
     */
    private Integer operTimeSco;

    /**
     * 特殊手术因素分值
     */
    private Integer operFactorSco;

    /**
     * 评价总分
     */
    private Integer totalSco;

    /**
     * 危险程度
     */
    private String dangerLevel;

    /**
     * 评估者
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