/*
 * DocOptCareRecord.java
 * Copyright(C) 2016 digihealth
 * All rights reserved.
 * ----------------------------------------
 * @author cy
 * 2017-03-30 Created
 */
package com.digihealth.anesthesia.doc.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.digihealth.anesthesia.basedata.formbean.DesignedOptCodes;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "手术护理记录对象")
public class DocOptCareRecord {
    @ApiModelProperty(value = "主键id")
     private String id;

    /**
     * 手术id
     */
    @ApiModelProperty(value = "手术id")
     private String regOptId;

    /**
     * END,NO_END
     */
    @ApiModelProperty(value = "是否完成 END,NO_END")
     private String processState;

    /**
     * 入室时间
     */
    @ApiModelProperty(value = "入室时间")
     private String inOperRoomTime;

    /**
     * 出室时间
     */
    @ApiModelProperty(value = "出室时间")
     private String outOperRoomTime;

    /**
     * 药物过敏
     */
    @ApiModelProperty(value = "药物过敏")
     private Integer allergic;

    @ApiModelProperty(value = "手术CODE")
     private String operationCode;

    /**
     * 手术名称
     */
    @ApiModelProperty(value = "手术名称")
     private String operationName;

    /**
     * 神志
     */
    @ApiModelProperty(value = "神志")
     private String senses;

    /**
     * 术前静脉输液
     */
    @ApiModelProperty(value = "术前静脉输液")
     private Integer venousInfusion1;

    /**
     * 深静脉穿刺
     */
    @ApiModelProperty(value = "深静脉穿刺")
     private Integer venipuncture;

    /**
     * 管道
     */
    @ApiModelProperty(value = "管道")
     private String pipeline;

    /**
     * x线片
     */
    @ApiModelProperty(value = "x线片")
     private Integer xray;

    /**
     * CT片
     */
    @ApiModelProperty(value = "CT片")
     private Integer CT;

    /**
     * MRI片
     */
    @ApiModelProperty(value = "MRI片")
     private Integer MRI;

    /**
     * 手术体位
     */
    @ApiModelProperty(value = "手术体位")
     private String optbody;

    /**
     * 手术体位集合
     */
    @ApiModelProperty(value = "手术体位集合")
     private List<String> optbodys;
    
    /**
     * 高频电刀
     */
    @ApiModelProperty(value = "高频电刀")
     private Integer elecKnife;

    /**
     * 标本
     */
    @ApiModelProperty(value = "标本")
     private Integer specimen;

    /**
     * 送检
     */
    @ApiModelProperty(value = "送检")
     private String inspection;

    /**
     * 标本名称
     */
    @ApiModelProperty(value = "标本名称")
     private String specimenName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
     private String remark;

    /**
     * 手术室交班护士
     */
    @ApiModelProperty(value = "手术室交班护士")
     private String shiftChangedNurse;

    /**
     * 手术室接班护士(部分局点用于病房接班)
     */
    @ApiModelProperty(value = "手术室接班护士(部分局点用于病房接班)")
     private String shiftChangeNurse;

    /**
     * 洗手护士ID
     */
    @ApiModelProperty(value = "洗手护士ID")
     private String instrnurseId;

    /**
     * 交班时间
     */
    @ApiModelProperty(value = "交班时间")
     private Date shiftTime;

    /**
     * 术前皮肤情况
     */
    @ApiModelProperty(value = "术前皮肤情况")
     private String skin1;

    /**
     * 负极板位置
     */
    @ApiModelProperty(value = "负极板位置")
     private String negativePosition;

    /**
     * 止血带
     */
    @ApiModelProperty(value = "止血带")
     private String tourniquet;

    /**
     * 体位支持用物
     */
    @ApiModelProperty(value = "体位支持用物")
     private String supportMaterial;

    /**
     * 体内植入物
     */
    @ApiModelProperty(value = "体内植入物")
     private String implants;

    /**
     * 送至
     */
    @ApiModelProperty(value = "送至")
     private String leaveTo;

    /**
     * 送至
     */
    @ApiModelProperty(value = "其他去向")
     private String leaveToOther;
    
    /**
     * 术后静脉输液
     */
    @ApiModelProperty(value = "术后静脉输液")
     private String venousInfusion2;

    /**
     * 引流管
     */
    @ApiModelProperty(value = "引流管")
     private String drainageTube;
    
    /**
     * 引流管(南华局点用)
     */
    @ApiModelProperty(value = "引流管")
     private String drainageTube2;

    /**
     * 术后皮肤情况
     */
    @ApiModelProperty(value = "术后皮肤情况")
     private String skin2;

    @ApiModelProperty(value = "手术名称集合")
     private List<DesignedOptCodes> operationNameList;

    @ApiModelProperty(value = "接班巡回护士集合")
     private List<String> shiftChangeNurseList;

    @ApiModelProperty(value = "交班巡回护士集合")
     private List<String> shiftChangedNurseList;

    @ApiModelProperty(value = "洗手护士集合")
     private List<String> instrnurseList;
    
    @ApiModelProperty(value = "管道（有无）")
    private String pipeState;
    
    @ApiModelProperty(value = "送检 快速")
    private Integer inspSpeedy; 
    
    @ApiModelProperty(value = "麻醉方法名称")
    private String anaesMethodName;
    
    @ApiModelProperty(value = "麻醉方法CODE")
    private String anaesMethodCode;
    
    @ApiModelProperty(value = "过敏史")
    private String allergicContents;
    
    private List<String> anaesMethodList;
    
    @ApiModelProperty(value = "是否使用负极板")
    private Integer negativeFlag;
    
    @ApiModelProperty(value = "是否使用体位支持用物")
    private Integer supportMaterialFlag;
    
    @ApiModelProperty(value = "是否使用引流管")
    private Integer drainageTubeFlag;
    
    @ApiModelProperty(value = "管道其他")
    private String pipelineOther;
    
    @ApiModelProperty(value = "体内植入物其他")
    private String implantsOther;
    
    /**
     * 病房交班护士
     */
    @ApiModelProperty(value = "病房交班护士")
    private String wardShiftsNurse;

    /**
     * 病房接班护士
     */
    @ApiModelProperty(value = "病房接班护士")
    private String wardSuccessionNurse;
    
    
    /**
     * 血型
     */
    @ApiModelProperty(value = "血型")
    private String bloodType;

    /**
     * 血液成分
     */
    @ApiModelProperty(value = "血液成分")
    private String bloodConstituent;

    /**
     * 输血量
     */
    @ApiModelProperty(value = "输血量")
    private String bloodTransfusion;

    /**
     * 出血量
     */
    @ApiModelProperty(value = "出血量")
    private Integer bleedingAmount;

    /**
     * 输液量
     */
    @ApiModelProperty(value = "输液量")
    private Integer infusionVolume;

    /**
     * 尿量
     */
    @ApiModelProperty(value = "尿量")
    private Integer urineVolume;

    /**
     * 术前用药
     */
    @ApiModelProperty(value = "术前用药")
    private Integer preoperativeMedication;

    /**
     * 术前用药已用未用
     */
    private Integer medicationUsed;
    
    /**
     * 腕带
     */
    @ApiModelProperty(value = "腕带")
    private Integer wristband;

    /**
     * 体表标志
     */
    @ApiModelProperty(value = "体表标志")
    private Integer bodySurface;

    /**
     * 术前禁饮：1 选中，0没有选中
     */
    @ApiModelProperty(value = "术前禁饮")
    private Integer banDrinking;

    /**
     * 术前禁食：1选中，0没有选中
     */
    @ApiModelProperty(value = "术前禁食")
    private Integer fasting;

    /**
     * 手术用物灭菌指示标记:1选中，0没有选中
     */
    @ApiModelProperty(value = "手术用物灭菌指示标记")
    private Integer sterilizationMark;
    
    /**
     * 体温
     */
    private Float temp;

    /**
     * 脉搏
     */
    private Integer pr;

    /**
     * 呼吸
     */
    private Integer rr;

    /**
     * 舒张压
     */
    private Integer bpDias;

    /**
     * 收缩压
     */
    private Integer bpSys;

    /**
     * 体重
     */
    private Float weight;

    /**
     * 意识状态
     */
    private String conscious;

    /**
     * 意识状态其他
     */
    private String consciousOther;

    /**
     * 术前准备
     */
    private String prePreparation;

    /**
     * 术前说明
     */
    private String preDescription;

    /**
     * 手术间
     */
    private String operroom;

    /**
     * 乙肝
     */
    private String hbv;

    /**
     * HIV
     */
    private String hiv;

    /**
     * 其他传染病
     */
    private String otherInfectiou;

    /**
     * 切口等级
     */
    private String cutLevel;

    /**
     * 入室方式
     */
    private String inOperWay;

    /**
     * 手术体位其他
     */
    private String optBodyOther;

    /**
     * 负极板位置其他
     */
    private String negativePositionOther;

    /**
     * 体位支持用物其他
     */
    private String supportMaterialOther;

    /**
     * 止血带部位
     */
    private String tourniquetPart;

    /**
     * 止血带压力
     */
    private String tourniquetPress;

    /**
     * 回病房时间
     */
    private String backWardTime;

    /**
     * PACU/ICU意识评估
     */
    private String conscious1;

    /**
     * 病房意识评估
     */
    private String conscious2;

    /**
     * PACU/ICU意识评估其他
     */
    private String consciousOther1;

    /**
     * 病房意识评估其他
     */
    private String consciousOther2;

    /**
     * PACU/ICU留置导管
     */
    private String indwellPipe1;

    /**
     * 病房留置导管
     */
    private String indwellPipe2;

    /**
     * PACU/ICU留置导管详情
     */
    private String indwellPipeDetail1;

    /**
     * 病房留置导管详情
     */
    private String indwellPipeDetail2;

    /**
     * PACU/ICU留置导管其他
     */
    private String indwellPipeOther1;

    /**
     * 病房留置导管其他
     */
    private String indwellPipeOther2;

    /**
     * PACU/ICU病历
     */
    private String medicalRec1;

    /**
     * 病房病历
     */
    private String medicalRec2;

    /**
     * PACU/ICU影像学
     */
    private String videography1;

    /**
     * 病房影像学
     */
    private String videography2;

    /**
     * PACU/ICU静脉输液
     */
    private String infusion1;

    /**
     * 病房静脉输液
     */
    private String infusion2;

    /**
     * PACU/ICU受压皮肤
     */
    private String pressSkin1;

    /**
     * 病房受压皮肤
     */
    private String pressSkin2;

    /**
     * PACU/ICU受压皮肤详情
     */
    private String pressSkinDetail1;

    /**
     * 病房受压皮肤详情
     */
    private String pressSkinDetail2;

    /**
     * PACU/ICU带入物品
     */
    private String bringGoods1;

    /**
     * 病房带入物品
     */
    private String bringGoods2;

    /**
     * PACU/ICU带入物品详情
     */
    private String bringGoodsDetail1;

    /**
     * 病房带入物品详情
     */
    private String bringGoodsDetail2;

    /**
     * PACU/ICU带入血液
     */
    private String bringBlood1;

    /**
     * 病房带入血液
     */
    private String bringBlood2;

    /**
     * 术中交班者
     */
    private String midShiftChanged;

    /**
     * 术中接班者
     */
    private String midShiftChange;

    /**
     * PACU/ICU交班者
     */
    private String postShiftChanged1;

    /**
     * PACU/ICU接班者
     */
    private String postShiftChange1;

    /**
     * 病房交班者
     */
    private String postShiftChanged2;

    /**
     * 病房接班者
     */
    private String postShiftChange2;
    
    private Map<String, Object> prePreparationMap;
    /**
     * 上止血带时间
     */
    private String tourniquetStartTime;
    /**
     * 松止血带时间
     * @return
     */
    private String tourniquetEndTime;
    /**
     * 快速送检 文本
     * @return
     */
    private String inspSpeedyMark;
    /**
     * 普通送检 文本
     * @return
     */
    private String inspectionMark;
    /**
     * 压力
     * @return
     */
    private String pressure;
    
    
    
    
    
    
    public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getTourniquetStartTime() {
		return tourniquetStartTime;
	}

	public void setTourniquetStartTime(String tourniquetStartTime) {
		this.tourniquetStartTime = tourniquetStartTime;
	}

	public String getTourniquetEndTime() {
		return tourniquetEndTime;
	}

	public void setTourniquetEndTime(String tourniquetEndTime) {
		this.tourniquetEndTime = tourniquetEndTime;
	}

	public String getInspSpeedyMark() {
		return inspSpeedyMark;
	}

	public void setInspSpeedyMark(String inspSpeedyMark) {
		this.inspSpeedyMark = inspSpeedyMark;
	}

	public String getInspectionMark() {
		return inspectionMark;
	}

	public void setInspectionMark(String inspectionMark) {
		this.inspectionMark = inspectionMark;
	}

	public Map<String, Object> getPrePreparationMap()
    {
        return null == prePreparationMap ? new HashMap<String, Object>() : prePreparationMap;
    }

    public void setPrePreparationMap(Map<String, Object> prePreparationMap)
    {
        this.prePreparationMap = prePreparationMap;
    }

    public Float getTemp()
    {
        return temp;
    }

    public void setTemp(Float temp)
    {
        this.temp = temp;
    }

    public Integer getPr()
    {
        return pr;
    }

    public void setPr(Integer pr)
    {
        this.pr = pr;
    }

    public Integer getRr()
    {
        return rr;
    }

    public void setRr(Integer rr)
    {
        this.rr = rr;
    }

    public Integer getBpDias()
    {
        return bpDias;
    }

    public void setBpDias(Integer bpDias)
    {
        this.bpDias = bpDias;
    }

    public Integer getBpSys()
    {
        return bpSys;
    }

    public void setBpSys(Integer bpSys)
    {
        this.bpSys = bpSys;
    }

    public Float getWeight()
    {
        return weight;
    }

    public void setWeight(Float weight)
    {
        this.weight = weight;
    }

    public String getConscious()
    {
        return conscious;
    }

    public void setConscious(String conscious)
    {
        this.conscious = conscious;
    }

    public String getConsciousOther()
    {
        return consciousOther;
    }

    public void setConsciousOther(String consciousOther)
    {
        this.consciousOther = consciousOther;
    }

    public String getPrePreparation()
    {
        return prePreparation;
    }

    public void setPrePreparation(String prePreparation)
    {
        this.prePreparation = prePreparation;
    }

    public String getPreDescription()
    {
        return preDescription;
    }

    public void setPreDescription(String preDescription)
    {
        this.preDescription = preDescription;
    }

    public String getOperroom()
    {
        return operroom;
    }

    public void setOperroom(String operroom)
    {
        this.operroom = operroom;
    }

    public String getHbv()
    {
        return hbv;
    }

    public void setHbv(String hbv)
    {
        this.hbv = hbv;
    }

    public String getHiv()
    {
        return hiv;
    }

    public void setHiv(String hiv)
    {
        this.hiv = hiv;
    }

    public String getOtherInfectiou()
    {
        return otherInfectiou;
    }

    public void setOtherInfectiou(String otherInfectiou)
    {
        this.otherInfectiou = otherInfectiou;
    }

    public String getCutLevel()
    {
        return cutLevel;
    }

    public void setCutLevel(String cutLevel)
    {
        this.cutLevel = cutLevel;
    }

    public String getInOperWay()
    {
        return inOperWay;
    }

    public void setInOperWay(String inOperWay)
    {
        this.inOperWay = inOperWay;
    }

    public String getOptBodyOther()
    {
        return optBodyOther;
    }

    public void setOptBodyOther(String optBodyOther)
    {
        this.optBodyOther = optBodyOther;
    }

    public String getNegativePositionOther()
    {
        return negativePositionOther;
    }

    public void setNegativePositionOther(String negativePositionOther)
    {
        this.negativePositionOther = negativePositionOther;
    }

    public String getSupportMaterialOther()
    {
        return supportMaterialOther;
    }

    public void setSupportMaterialOther(String supportMaterialOther)
    {
        this.supportMaterialOther = supportMaterialOther;
    }

    public String getTourniquetPart()
    {
        return tourniquetPart;
    }

    public void setTourniquetPart(String tourniquetPart)
    {
        this.tourniquetPart = tourniquetPart;
    }

    public String getTourniquetPress()
    {
        return tourniquetPress;
    }

    public void setTourniquetPress(String tourniquetPress)
    {
        this.tourniquetPress = tourniquetPress;
    }

    public String getBackWardTime()
    {
        return backWardTime;
    }

    public void setBackWardTime(String backWardTime)
    {
        this.backWardTime = backWardTime;
    }

    public String getConscious1()
    {
        return conscious1;
    }

    public void setConscious1(String conscious1)
    {
        this.conscious1 = conscious1;
    }

    public String getConscious2()
    {
        return conscious2;
    }

    public void setConscious2(String conscious2)
    {
        this.conscious2 = conscious2;
    }

    public String getConsciousOther1()
    {
        return consciousOther1;
    }

    public void setConsciousOther1(String consciousOther1)
    {
        this.consciousOther1 = consciousOther1;
    }

    public String getConsciousOther2()
    {
        return consciousOther2;
    }

    public void setConsciousOther2(String consciousOther2)
    {
        this.consciousOther2 = consciousOther2;
    }

    public String getIndwellPipe1()
    {
        return indwellPipe1;
    }

    public void setIndwellPipe1(String indwellPipe1)
    {
        this.indwellPipe1 = indwellPipe1;
    }

    public String getIndwellPipe2()
    {
        return indwellPipe2;
    }

    public void setIndwellPipe2(String indwellPipe2)
    {
        this.indwellPipe2 = indwellPipe2;
    }

    public String getIndwellPipeDetail1()
    {
        return indwellPipeDetail1;
    }

    public void setIndwellPipeDetail1(String indwellPipeDetail1)
    {
        this.indwellPipeDetail1 = indwellPipeDetail1;
    }

    public String getIndwellPipeDetail2()
    {
        return indwellPipeDetail2;
    }

    public void setIndwellPipeDetail2(String indwellPipeDetail2)
    {
        this.indwellPipeDetail2 = indwellPipeDetail2;
    }

    public String getIndwellPipeOther1()
    {
        return indwellPipeOther1;
    }

    public void setIndwellPipeOther1(String indwellPipeOther1)
    {
        this.indwellPipeOther1 = indwellPipeOther1;
    }

    public String getIndwellPipeOther2()
    {
        return indwellPipeOther2;
    }

    public void setIndwellPipeOther2(String indwellPipeOther2)
    {
        this.indwellPipeOther2 = indwellPipeOther2;
    }

    public String getMedicalRec1()
    {
        return medicalRec1;
    }

    public void setMedicalRec1(String medicalRec1)
    {
        this.medicalRec1 = medicalRec1;
    }

    public String getMedicalRec2()
    {
        return medicalRec2;
    }

    public void setMedicalRec2(String medicalRec2)
    {
        this.medicalRec2 = medicalRec2;
    }

    public String getVideography1()
    {
        return videography1;
    }

    public void setVideography1(String videography1)
    {
        this.videography1 = videography1;
    }

    public String getVideography2()
    {
        return videography2;
    }

    public void setVideography2(String videography2)
    {
        this.videography2 = videography2;
    }

    public String getInfusion1()
    {
        return infusion1;
    }

    public void setInfusion1(String infusion1)
    {
        this.infusion1 = infusion1;
    }

    public String getInfusion2()
    {
        return infusion2;
    }

    public void setInfusion2(String infusion2)
    {
        this.infusion2 = infusion2;
    }

    public String getPressSkin1()
    {
        return pressSkin1;
    }

    public void setPressSkin1(String pressSkin1)
    {
        this.pressSkin1 = pressSkin1;
    }

    public String getPressSkin2()
    {
        return pressSkin2;
    }

    public void setPressSkin2(String pressSkin2)
    {
        this.pressSkin2 = pressSkin2;
    }

    public String getPressSkinDetail1()
    {
        return pressSkinDetail1;
    }

    public void setPressSkinDetail1(String pressSkinDetail1)
    {
        this.pressSkinDetail1 = pressSkinDetail1;
    }

    public String getPressSkinDetail2()
    {
        return pressSkinDetail2;
    }

    public void setPressSkinDetail2(String pressSkinDetail2)
    {
        this.pressSkinDetail2 = pressSkinDetail2;
    }

    public String getBringGoods1()
    {
        return bringGoods1;
    }

    public void setBringGoods1(String bringGoods1)
    {
        this.bringGoods1 = bringGoods1;
    }

    public String getBringGoods2()
    {
        return bringGoods2;
    }

    public void setBringGoods2(String bringGoods2)
    {
        this.bringGoods2 = bringGoods2;
    }

    public String getBringGoodsDetail1()
    {
        return bringGoodsDetail1;
    }

    public void setBringGoodsDetail1(String bringGoodsDetail1)
    {
        this.bringGoodsDetail1 = bringGoodsDetail1;
    }

    public String getBringGoodsDetail2()
    {
        return bringGoodsDetail2;
    }

    public void setBringGoodsDetail2(String bringGoodsDetail2)
    {
        this.bringGoodsDetail2 = bringGoodsDetail2;
    }

    public String getBringBlood1()
    {
        return bringBlood1;
    }

    public void setBringBlood1(String bringBlood1)
    {
        this.bringBlood1 = bringBlood1;
    }

    public String getBringBlood2()
    {
        return bringBlood2;
    }

    public void setBringBlood2(String bringBlood2)
    {
        this.bringBlood2 = bringBlood2;
    }

    public String getMidShiftChanged()
    {
        return midShiftChanged;
    }

    public void setMidShiftChanged(String midShiftChanged)
    {
        this.midShiftChanged = midShiftChanged;
    }

    public String getMidShiftChange()
    {
        return midShiftChange;
    }

    public void setMidShiftChange(String midShiftChange)
    {
        this.midShiftChange = midShiftChange;
    }

    public String getPostShiftChanged1()
    {
        return postShiftChanged1;
    }

    public void setPostShiftChanged1(String postShiftChanged1)
    {
        this.postShiftChanged1 = postShiftChanged1;
    }

    public String getPostShiftChange1()
    {
        return postShiftChange1;
    }

    public void setPostShiftChange1(String postShiftChange1)
    {
        this.postShiftChange1 = postShiftChange1;
    }

    public String getPostShiftChanged2()
    {
        return postShiftChanged2;
    }

    public void setPostShiftChanged2(String postShiftChanged2)
    {
        this.postShiftChanged2 = postShiftChanged2;
    }

    public String getPostShiftChange2()
    {
        return postShiftChange2;
    }

    public void setPostShiftChange2(String postShiftChange2)
    {
        this.postShiftChange2 = postShiftChange2;
    }

    public String getPipelineOther()
    {
        return pipelineOther;
    }

    public void setPipelineOther(String pipelineOther)
    {
        this.pipelineOther = pipelineOther;
    }

    public String getImplantsOther()
    {
        return implantsOther;
    }

    public void setImplantsOther(String implantsOther)
    {
        this.implantsOther = implantsOther;
    }

    public Integer getNegativeFlag()
    {
        return negativeFlag;
    }

    public void setNegativeFlag(Integer negativeFlag)
    {
        this.negativeFlag = negativeFlag;
    }

    public Integer getSupportMaterialFlag()
    {
        return supportMaterialFlag;
    }

    public void setSupportMaterialFlag(Integer supportMaterialFlag)
    {
        this.supportMaterialFlag = supportMaterialFlag;
    }

    public Integer getDrainageTubeFlag()
    {
        return drainageTubeFlag;
    }

    public void setDrainageTubeFlag(Integer drainageTubeFlag)
    {
        this.drainageTubeFlag = drainageTubeFlag;
    }

    public List<String> getAnaesMethodList()
    {
        return anaesMethodList;
    }

    public void setAnaesMethodList(List<String> anaesMethodList)
    {
        this.anaesMethodList = anaesMethodList;
    }

    public String getDrainageTube2()
    {
        return drainageTube2;
    }

    public void setDrainageTube2(String drainageTube2)
    {
        this.drainageTube2 = drainageTube2;
    }

    public String getPipeState()
    {
        return pipeState;
    }

    public void setPipeState(String pipeState)
    {
        this.pipeState = pipeState;
    }

    public Integer getInspSpeedy()
    {
        return inspSpeedy;
    }

    public void setInspSpeedy(Integer inspSpeedy)
    {
        this.inspSpeedy = inspSpeedy;
    }

    public String getAnaesMethodName()
    {
        return anaesMethodName;
    }

    public void setAnaesMethodName(String anaesMethodName)
    {
        this.anaesMethodName = anaesMethodName;
    }

    public String getAnaesMethodCode()
    {
        return anaesMethodCode;
    }

    public void setAnaesMethodCode(String anaesMethodCode)
    {
        this.anaesMethodCode = anaesMethodCode;
    }

    public String getAllergicContents()
    {
        return allergicContents;
    }

    public void setAllergicContents(String allergicContents)
    {
        this.allergicContents = allergicContents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getRegOptId() {
        return regOptId;
    }

    public void setRegOptId(String regOptId) {
        this.regOptId = regOptId == null ? null : regOptId.trim();
    }

    public String getProcessState() {
        return processState;
    }

    public void setProcessState(String processState) {
        this.processState = processState == null ? null : processState.trim();
    }

    public String getInOperRoomTime() {
        return inOperRoomTime;
    }

    public void setInOperRoomTime(String inOperRoomTime) {
        this.inOperRoomTime = inOperRoomTime == null ? null : inOperRoomTime.trim();
    }

    public String getOutOperRoomTime() {
        return outOperRoomTime;
    }

    public void setOutOperRoomTime(String outOperRoomTime) {
        this.outOperRoomTime = outOperRoomTime == null ? null : outOperRoomTime.trim();
    }

    public Integer getAllergic() {
        return allergic;
    }

    public void setAllergic(Integer allergic) {
        this.allergic = allergic;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode == null ? null : operationCode.trim();
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName == null ? null : operationName.trim();
    }

    public String getSenses() {
        return senses;
    }

    public void setSenses(String senses) {
        this.senses = senses == null ? null : senses.trim();
    }

    public Integer getVenousInfusion1() {
        return venousInfusion1;
    }

    public void setVenousInfusion1(Integer venousInfusion1) {
        this.venousInfusion1 = venousInfusion1;
    }

    public Integer getVenipuncture() {
        return venipuncture;
    }

    public void setVenipuncture(Integer venipuncture) {
        this.venipuncture = venipuncture;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline == null ? null : pipeline.trim();
    }

    public Integer getXray() {
        return xray;
    }

    public void setXray(Integer xray) {
        this.xray = xray;
    }

    public Integer getCT() {
        return CT;
    }

    public void setCT(Integer CT) {
        this.CT = CT;
    }

    public Integer getMRI() {
        return MRI;
    }

    public void setMRI(Integer MRI) {
        this.MRI = MRI;
    }

    public String getOptbody() {
        return optbody;
    }

    public void setOptbody(String optbody) {
        this.optbody = optbody == null ? null : optbody.trim();
    }

    public List<String> getOptbodys() {
		return null == optbodys ? new ArrayList<String>() : optbodys;
	}

	public void setOptbodys(List<String> optbodys) {
		this.optbodys = optbodys;
	}

	public Integer getElecKnife() {
        return elecKnife;
    }

    public void setElecKnife(Integer elecKnife) {
        this.elecKnife = elecKnife;
    }

    public Integer getSpecimen() {
        return specimen;
    }

    public void setSpecimen(Integer specimen) {
        this.specimen = specimen;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getSpecimenName() {
        return specimenName;
    }

    public void setSpecimenName(String specimenName) {
        this.specimenName = specimenName == null ? null : specimenName.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getShiftChangedNurse() {
        return shiftChangedNurse;
    }

    public void setShiftChangedNurse(String shiftChangedNurse) {
        this.shiftChangedNurse = shiftChangedNurse == null ? null : shiftChangedNurse.trim();
    }

    public String getShiftChangeNurse() {
        return shiftChangeNurse;
    }

    public void setShiftChangeNurse(String shiftChangeNurse) {
        this.shiftChangeNurse = shiftChangeNurse == null ? null : shiftChangeNurse.trim();
    }

    public String getInstrnurseId() {
        return instrnurseId;
    }

    public void setInstrnurseId(String instrnurseId) {
        this.instrnurseId = instrnurseId == null ? null : instrnurseId.trim();
    }

    public Date getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(Date shiftTime) {
        this.shiftTime = shiftTime;
    }

    public String getSkin1() {
        return skin1;
    }

    public void setSkin1(String skin1) {
        this.skin1 = skin1 == null ? null : skin1.trim();
    }

    public String getNegativePosition() {
        return negativePosition;
    }

    public void setNegativePosition(String negativePosition) {
        this.negativePosition = negativePosition == null ? null : negativePosition.trim();
    }

    public String getTourniquet() {
        return tourniquet;
    }

    public void setTourniquet(String tourniquet) {
        this.tourniquet = tourniquet == null ? null : tourniquet.trim();
    }

    public String getSupportMaterial() {
        return supportMaterial;
    }

    public void setSupportMaterial(String supportMaterial) {
        this.supportMaterial = supportMaterial == null ? null : supportMaterial.trim();
    }

    public String getImplants() {
        return implants;
    }

    public void setImplants(String implants) {
        this.implants = implants == null ? null : implants.trim();
    }

    public String getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(String leaveTo) {
        this.leaveTo = leaveTo == null ? null : leaveTo.trim();
    }

    public String getVenousInfusion2() {
        return venousInfusion2;
    }

    public void setVenousInfusion2(String venousInfusion2) {
        this.venousInfusion2 = venousInfusion2 == null ? null : venousInfusion2.trim();
    }

    public String getDrainageTube() {
        return drainageTube;
    }

    public void setDrainageTube(String drainageTube) {
        this.drainageTube = drainageTube == null ? null : drainageTube.trim();
    }

    public String getSkin2() {
        return skin2;
    }

    public void setSkin2(String skin2) {
        this.skin2 = skin2 == null ? null : skin2.trim();
    }

	public List<DesignedOptCodes> getOperationNameList() {
		return operationNameList;
	}

	public void setOperationNameList(List<DesignedOptCodes> operationNameList) {
		this.operationNameList = operationNameList;
	}

	public List<String> getShiftChangeNurseList() {
		return shiftChangeNurseList;
	}

	public void setShiftChangeNurseList(List<String> shiftChangeNurseList) {
		this.shiftChangeNurseList = shiftChangeNurseList;
	}

	public List<String> getShiftChangedNurseList() {
		return shiftChangedNurseList;
	}

	public void setShiftChangedNurseList(List<String> shiftChangedNurseList) {
		this.shiftChangedNurseList = shiftChangedNurseList;
	}

	public List<String> getInstrnurseList() {
		return instrnurseList;
	}

	public void setInstrnurseList(List<String> instrnurseList) {
		this.instrnurseList = instrnurseList;
	}

	public String getLeaveToOther() {
		return leaveToOther;
	}

	public void setLeaveToOther(String leaveToOther) {
		this.leaveToOther = leaveToOther;
	}

	public String getWardShiftsNurse()
	{
		return wardShiftsNurse;
	}

	public void setWardShiftsNurse(String wardShiftsNurse)
	{
		this.wardShiftsNurse = wardShiftsNurse;
	}

	public String getWardSuccessionNurse()
	{
		return wardSuccessionNurse;
	}

	public void setWardSuccessionNurse(String wardSuccessionNurse)
	{
		this.wardSuccessionNurse = wardSuccessionNurse;
	}

	public String getBloodType()
	{
		return bloodType;
	}

	public void setBloodType(String bloodType)
	{
		this.bloodType = bloodType == null ? null : bloodType.trim();
	}

	public String getBloodConstituent()
	{
		return bloodConstituent;
	}

	public void setBloodConstituent(String bloodConstituent)
	{
		this.bloodConstituent = bloodConstituent == null ? null
				: bloodConstituent.trim();
	}

	public String getBloodTransfusion()
	{
		return bloodTransfusion;
	}

	public void setBloodTransfusion(String bloodTransfusion)
	{
		this.bloodTransfusion = bloodTransfusion == null ? null
				: bloodTransfusion.trim();
	}

	public Integer getBleedingAmount()
	{
		return bleedingAmount;
	}

	public void setBleedingAmount(Integer bleedingAmount)
	{
		this.bleedingAmount = bleedingAmount;
	}

	public Integer getInfusionVolume()
	{
		return infusionVolume;
	}

	public void setInfusionVolume(Integer infusionVolume)
	{
		this.infusionVolume = infusionVolume;
	}

	public Integer getUrineVolume()
	{
		return urineVolume;
	}

	public void setUrineVolume(Integer urineVolume)
	{
		this.urineVolume = urineVolume;
	}

	public Integer getPreoperativeMedication()
	{
		return preoperativeMedication;
	}

	public void setPreoperativeMedication(Integer preoperativeMedication)
	{
		this.preoperativeMedication = preoperativeMedication;
	}

	public Integer getMedicationUsed()
	{
		return medicationUsed;
	}

	public void setMedicationUsed(Integer medicationUsed)
	{
		this.medicationUsed = medicationUsed;
	}

	public Integer getWristband()
	{
		return wristband;
	}

	public void setWristband(Integer wristband)
	{
		this.wristband = wristband;
	}

	public Integer getBodySurface()
	{
		return bodySurface;
	}

	public void setBodySurface(Integer bodySurface)
	{
		this.bodySurface = bodySurface;
	}

	public Integer getBanDrinking()
	{
		return banDrinking;
	}

	public void setBanDrinking(Integer banDrinking)
	{
		this.banDrinking = banDrinking;
	}

	public Integer getFasting()
	{
		return fasting;
	}

	public void setFasting(Integer fasting)
	{
		this.fasting = fasting;
	}

	public Integer getSterilizationMark()
	{
		return sterilizationMark;
	}

	public void setSterilizationMark(Integer sterilizationMark)
	{
		this.sterilizationMark = sterilizationMark;
	}
}