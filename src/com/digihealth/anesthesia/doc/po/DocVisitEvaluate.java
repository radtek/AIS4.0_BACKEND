package com.digihealth.anesthesia.doc.po;

public class DocVisitEvaluate {
    private String id;

    /**
     * 手术ID
     */
    private String regOptId;

    /**
     * 文书状态
     */
    private String processState;

    /**
     * 拟行手术
     */
    private String designedOper;

    /**
     * 拟行麻醉
     */
    private String designedAnaesMethod;

    /**
     * 拟洗手护士
     */
    private String designedInstrnurse;

    /**
     * 拟巡回护士
     */
    private String designedCircunurse;

    /**
     * 访视护士
     */
    private String visitNurse;

    /**
     * 访视日期
     */
    private String visitDate;

    /**
     * 体重
     */
    private Float weight;

    /**
     * 身高
     */
    private Float height;

    /**
     * 舒张压
     */
    private Integer bpDias;

    /**
     * 收缩压
     */
    private Integer bpSys;

    /**
     * 心率
     */
    private Integer hr;

    /**
     * 体温
     */
    private Float temp;

    /**
     * 意识状态
     */
    private String conscious;

    /**
     * 意识状态其他
     */
    private String consciousOther;

    /**
     * 术前心理
     */
    private String preMental;

    /**
     * 术前心理其他
     */
    private String preMentalOther;

    /**
     * 睡眠质量
     */
    private String sleepQuality;

    /**
     * 睡眠质量差原因
     */
    private String sleepQualityBadReason;

    /**
     * 沟通障碍
     */
    private String communiBarrier;

    /**
     * 使用语言
     */
    private String useLanguage;

    /**
     * 听力缺陷
     */
    private String hearDefect;

    /**
     * 沟通障碍其他
     */
    private String communiBarrierOther;

    /**
     * 视力障碍
     */
    private String visionDefect;

    /**
     * 视力障碍详情
     */
    private String visionDefectDetail;

    /**
     * 身体状况
     */
    private String bodyStatus;

    /**
     * 体格发育
     */
    private String bodyDevelop;

    /**
     * 体格发育异常详情
     */
    private String bodyAbnormalDetail;

    /**
     * 体格发育异常其他
     */
    private String bodyAbnormalOther;

    /**
     * 肢体感觉
     */
    private String limbFeel;

    /**
     * 肢体感觉部位
     */
    private String limbAbnormalParts;

    /**
     * 肢体感觉性质
     */
    private String limbAbnormalNature;

    /**
     * 血管情况
     */
    private String arteryState;

    /**
     * 皮肤黏膜
     */
    private String skinMucosa;

    /**
     * 皮肤黏膜部位
     */
    private String skinAbnormalParts;

    /**
     * 皮肤黏膜性质
     */
    private String skinAbnormalNature;

    /**
     * 压疮风险
     */
    private String pressureUlcerRisk;

    /**
     * 压疮部位
     */
    private String pressureUlcerParts;

    /**
     * 切口等级
     */
    private String cutLevel;

    /**
     * 过敏史
     */
    private String allergic;

    /**
     * 过敏详情
     */
    private String allergicDetail;

    /**
     * 过敏药物
     */
    private String allergicMedical;

    /**
     * 过敏食物
     */
    private String allergicFood;

    /**
     * 过敏其他
     */
    private String allergicOther;

    /**
     * 感染情况
     */
    private String infection;

    /**
     * 感染详情
     */
    private String infectionDetail;

    /**
     * 感染其他
     */
    private String infectionOther;

    /**
     * 既往病史
     */
    private String medicalHis;

    /**
     * 病史详情
     */
    private String medicalHisDetail;

    /**
     * 病史其他
     */
    private String medicalHisOther;

    /**
     * 手术史
     */
    private String operHis;

    /**
     * 手术史详情
     */
    private String operHisDetail;

    /**
     * 植入史
     */
    private String implantHis;

    /**
     * 植入史详情
     */
    private String implantHisDetail;

    /**
     * 植入史其他
     */
    private String implantHisOther;

    /**
     * 术前检查
     */
    private String preCheck;

    /**
     * 已签知情同意书
     */
    private String signedAgreeDoc;

    /**
     * 已签知情同意书其他
     */
    private String signedAgreeDocOther;

    /**
     * 婚姻
     */
    private String marriage;

    /**
     * 病人支持群体
     */
    private String patSupportGroup;

    /**
     * 缴费方式
     */
    private String payMethod;

    /**
     * 患者宣教
     */
    private String patMission;

    /**
     * 指导训练
     */
    private String guideTrain;

    /**
     * 术前注意事项
     */
    private String prePrecautions;

    /**
     * 术后不适
     */
    private String postDiscomfort;

    /**
     * 术后不适其他
     */
    private String postDiscomfortOther;

    /**
     * 环境介绍
     */
    private String environIntrodu;

    /**
     * 入室须知介绍
     */
    private String enterRoomIntrodu;

    /**
     * 特殊手术体位指导
     */
    private String speOptBodyGuide;

    /**
     * 患者目前存在的护理问题
     */
    private String nurseProblem;

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

    public String getDesignedOper() {
        return designedOper;
    }

    public void setDesignedOper(String designedOper) {
        this.designedOper = designedOper;
    }

    public String getDesignedAnaesMethod() {
        return designedAnaesMethod;
    }

    public void setDesignedAnaesMethod(String designedAnaesMethod) {
        this.designedAnaesMethod = designedAnaesMethod;
    }

    public String getDesignedInstrnurse() {
        return designedInstrnurse;
    }

    public void setDesignedInstrnurse(String designedInstrnurse) {
        this.designedInstrnurse = designedInstrnurse;
    }

    public String getDesignedCircunurse() {
        return designedCircunurse;
    }

    public void setDesignedCircunurse(String designedCircunurse) {
        this.designedCircunurse = designedCircunurse;
    }

    public String getVisitNurse() {
        return visitNurse;
    }

    public void setVisitNurse(String visitNurse) {
        this.visitNurse = visitNurse;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Integer getBpDias() {
        return bpDias;
    }

    public void setBpDias(Integer bpDias) {
        this.bpDias = bpDias;
    }

    public Integer getBpSys() {
        return bpSys;
    }

    public void setBpSys(Integer bpSys) {
        this.bpSys = bpSys;
    }

    public Integer getHr() {
        return hr;
    }

    public void setHr(Integer hr) {
        this.hr = hr;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public String getConscious() {
        return conscious;
    }

    public void setConscious(String conscious) {
        this.conscious = conscious;
    }

    public String getConsciousOther() {
        return consciousOther;
    }

    public void setConsciousOther(String consciousOther) {
        this.consciousOther = consciousOther;
    }

    public String getPreMental() {
        return preMental;
    }

    public void setPreMental(String preMental) {
        this.preMental = preMental;
    }

    public String getPreMentalOther() {
        return preMentalOther;
    }

    public void setPreMentalOther(String preMentalOther) {
        this.preMentalOther = preMentalOther;
    }

    public String getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(String sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public String getSleepQualityBadReason() {
        return sleepQualityBadReason;
    }

    public void setSleepQualityBadReason(String sleepQualityBadReason) {
        this.sleepQualityBadReason = sleepQualityBadReason;
    }

    public String getCommuniBarrier() {
        return communiBarrier;
    }

    public void setCommuniBarrier(String communiBarrier) {
        this.communiBarrier = communiBarrier;
    }

    public String getUseLanguage() {
        return useLanguage;
    }

    public void setUseLanguage(String useLanguage) {
        this.useLanguage = useLanguage;
    }

    public String getHearDefect() {
        return hearDefect;
    }

    public void setHearDefect(String hearDefect) {
        this.hearDefect = hearDefect;
    }

    public String getCommuniBarrierOther() {
        return communiBarrierOther;
    }

    public void setCommuniBarrierOther(String communiBarrierOther) {
        this.communiBarrierOther = communiBarrierOther;
    }

    public String getVisionDefect() {
        return visionDefect;
    }

    public void setVisionDefect(String visionDefect) {
        this.visionDefect = visionDefect;
    }

    public String getVisionDefectDetail() {
        return visionDefectDetail;
    }

    public void setVisionDefectDetail(String visionDefectDetail) {
        this.visionDefectDetail = visionDefectDetail;
    }

    public String getBodyStatus() {
        return bodyStatus;
    }

    public void setBodyStatus(String bodyStatus) {
        this.bodyStatus = bodyStatus;
    }

    public String getBodyDevelop() {
        return bodyDevelop;
    }

    public void setBodyDevelop(String bodyDevelop) {
        this.bodyDevelop = bodyDevelop;
    }

    public String getBodyAbnormalDetail() {
        return bodyAbnormalDetail;
    }

    public void setBodyAbnormalDetail(String bodyAbnormalDetail) {
        this.bodyAbnormalDetail = bodyAbnormalDetail;
    }

    public String getBodyAbnormalOther() {
        return bodyAbnormalOther;
    }

    public void setBodyAbnormalOther(String bodyAbnormalOther) {
        this.bodyAbnormalOther = bodyAbnormalOther;
    }

    public String getLimbFeel() {
        return limbFeel;
    }

    public void setLimbFeel(String limbFeel) {
        this.limbFeel = limbFeel;
    }

    public String getLimbAbnormalParts() {
        return limbAbnormalParts;
    }

    public void setLimbAbnormalParts(String limbAbnormalParts) {
        this.limbAbnormalParts = limbAbnormalParts;
    }

    public String getLimbAbnormalNature() {
        return limbAbnormalNature;
    }

    public void setLimbAbnormalNature(String limbAbnormalNature) {
        this.limbAbnormalNature = limbAbnormalNature;
    }

    public String getArteryState() {
        return arteryState;
    }

    public void setArteryState(String arteryState) {
        this.arteryState = arteryState;
    }

    public String getSkinMucosa() {
        return skinMucosa;
    }

    public void setSkinMucosa(String skinMucosa) {
        this.skinMucosa = skinMucosa;
    }

    public String getSkinAbnormalParts() {
        return skinAbnormalParts;
    }

    public void setSkinAbnormalParts(String skinAbnormalParts) {
        this.skinAbnormalParts = skinAbnormalParts;
    }

    public String getSkinAbnormalNature() {
        return skinAbnormalNature;
    }

    public void setSkinAbnormalNature(String skinAbnormalNature) {
        this.skinAbnormalNature = skinAbnormalNature;
    }

    public String getPressureUlcerRisk() {
        return pressureUlcerRisk;
    }

    public void setPressureUlcerRisk(String pressureUlcerRisk) {
        this.pressureUlcerRisk = pressureUlcerRisk;
    }

    public String getPressureUlcerParts() {
        return pressureUlcerParts;
    }

    public void setPressureUlcerParts(String pressureUlcerParts) {
        this.pressureUlcerParts = pressureUlcerParts;
    }

    public String getCutLevel() {
        return cutLevel;
    }

    public void setCutLevel(String cutLevel) {
        this.cutLevel = cutLevel;
    }

    public String getAllergic() {
        return allergic;
    }

    public void setAllergic(String allergic) {
        this.allergic = allergic;
    }

    public String getAllergicDetail() {
        return allergicDetail;
    }

    public void setAllergicDetail(String allergicDetail) {
        this.allergicDetail = allergicDetail;
    }

    public String getAllergicMedical() {
        return allergicMedical;
    }

    public void setAllergicMedical(String allergicMedical) {
        this.allergicMedical = allergicMedical;
    }

    public String getAllergicFood() {
        return allergicFood;
    }

    public void setAllergicFood(String allergicFood) {
        this.allergicFood = allergicFood;
    }

    public String getAllergicOther() {
        return allergicOther;
    }

    public void setAllergicOther(String allergicOther) {
        this.allergicOther = allergicOther;
    }

    public String getInfection() {
        return infection;
    }

    public void setInfection(String infection) {
        this.infection = infection;
    }

    public String getInfectionDetail() {
        return infectionDetail;
    }

    public void setInfectionDetail(String infectionDetail) {
        this.infectionDetail = infectionDetail;
    }

    public String getInfectionOther() {
        return infectionOther;
    }

    public void setInfectionOther(String infectionOther) {
        this.infectionOther = infectionOther;
    }

    public String getMedicalHis() {
        return medicalHis;
    }

    public void setMedicalHis(String medicalHis) {
        this.medicalHis = medicalHis;
    }

    public String getMedicalHisDetail() {
        return medicalHisDetail;
    }

    public void setMedicalHisDetail(String medicalHisDetail) {
        this.medicalHisDetail = medicalHisDetail;
    }

    public String getMedicalHisOther() {
        return medicalHisOther;
    }

    public void setMedicalHisOther(String medicalHisOther) {
        this.medicalHisOther = medicalHisOther;
    }

    public String getOperHis() {
        return operHis;
    }

    public void setOperHis(String operHis) {
        this.operHis = operHis;
    }

    public String getOperHisDetail() {
        return operHisDetail;
    }

    public void setOperHisDetail(String operHisDetail) {
        this.operHisDetail = operHisDetail;
    }

    public String getImplantHis() {
        return implantHis;
    }

    public void setImplantHis(String implantHis) {
        this.implantHis = implantHis;
    }

    public String getImplantHisDetail() {
        return implantHisDetail;
    }

    public void setImplantHisDetail(String implantHisDetail) {
        this.implantHisDetail = implantHisDetail;
    }

    public String getImplantHisOther() {
        return implantHisOther;
    }

    public void setImplantHisOther(String implantHisOther) {
        this.implantHisOther = implantHisOther;
    }

    public String getPreCheck() {
        return preCheck;
    }

    public void setPreCheck(String preCheck) {
        this.preCheck = preCheck;
    }

    public String getSignedAgreeDoc() {
        return signedAgreeDoc;
    }

    public void setSignedAgreeDoc(String signedAgreeDoc) {
        this.signedAgreeDoc = signedAgreeDoc;
    }

    public String getSignedAgreeDocOther() {
        return signedAgreeDocOther;
    }

    public void setSignedAgreeDocOther(String signedAgreeDocOther) {
        this.signedAgreeDocOther = signedAgreeDocOther;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getPatSupportGroup() {
        return patSupportGroup;
    }

    public void setPatSupportGroup(String patSupportGroup) {
        this.patSupportGroup = patSupportGroup;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPatMission() {
        return patMission;
    }

    public void setPatMission(String patMission) {
        this.patMission = patMission;
    }

    public String getGuideTrain() {
        return guideTrain;
    }

    public void setGuideTrain(String guideTrain) {
        this.guideTrain = guideTrain;
    }

    public String getPrePrecautions() {
        return prePrecautions;
    }

    public void setPrePrecautions(String prePrecautions) {
        this.prePrecautions = prePrecautions;
    }

    public String getPostDiscomfort() {
        return postDiscomfort;
    }

    public void setPostDiscomfort(String postDiscomfort) {
        this.postDiscomfort = postDiscomfort;
    }

    public String getPostDiscomfortOther() {
        return postDiscomfortOther;
    }

    public void setPostDiscomfortOther(String postDiscomfortOther) {
        this.postDiscomfortOther = postDiscomfortOther;
    }

    public String getEnvironIntrodu() {
        return environIntrodu;
    }

    public void setEnvironIntrodu(String environIntrodu) {
        this.environIntrodu = environIntrodu;
    }

    public String getEnterRoomIntrodu() {
        return enterRoomIntrodu;
    }

    public void setEnterRoomIntrodu(String enterRoomIntrodu) {
        this.enterRoomIntrodu = enterRoomIntrodu;
    }

    public String getSpeOptBodyGuide() {
        return speOptBodyGuide;
    }

    public void setSpeOptBodyGuide(String speOptBodyGuide) {
        this.speOptBodyGuide = speOptBodyGuide;
    }

    public String getNurseProblem() {
        return nurseProblem;
    }

    public void setNurseProblem(String nurseProblem) {
        this.nurseProblem = nurseProblem;
    }
}