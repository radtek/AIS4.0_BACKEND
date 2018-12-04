package com.digihealth.anesthesia.operProceed.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digihealth.anesthesia.basedata.formbean.BaseInfoQuery;
import com.digihealth.anesthesia.basedata.formbean.DiagnosedefFormBean;
import com.digihealth.anesthesia.basedata.formbean.OperDefFormBean;
import com.digihealth.anesthesia.basedata.formbean.SysCodeFormbean;
import com.digihealth.anesthesia.basedata.po.BasMonitorConfigFreq;
import com.digihealth.anesthesia.basedata.po.BasRegOpt;
import com.digihealth.anesthesia.common.config.Global;
import com.digihealth.anesthesia.common.entity.ResponseValue;
import com.digihealth.anesthesia.common.utils.BeanHelper;
import com.digihealth.anesthesia.common.utils.DateUtils;
import com.digihealth.anesthesia.common.utils.Exceptions;
import com.digihealth.anesthesia.common.utils.JsonType;
import com.digihealth.anesthesia.common.utils.StringUtils;
import com.digihealth.anesthesia.common.utils.SysUtil;
import com.digihealth.anesthesia.common.web.BaseController;
import com.digihealth.anesthesia.doc.formbean.AnaesRecordFormBean;
import com.digihealth.anesthesia.doc.po.DocAnaesBeforeSafeCheck;
import com.digihealth.anesthesia.doc.po.DocAnaesPacuRec;
import com.digihealth.anesthesia.doc.po.DocAnaesRecord;
import com.digihealth.anesthesia.doc.po.DocExitOperSafeCheck;
import com.digihealth.anesthesia.doc.po.DocOperBeforeSafeCheck;
import com.digihealth.anesthesia.evt.formbean.EvtAnaesMethodFormBean;
import com.digihealth.anesthesia.evt.formbean.OperBodyFormBean;
import com.digihealth.anesthesia.evt.formbean.RegOptOperMedicaleventFormBean;
import com.digihealth.anesthesia.evt.formbean.SearchFormBean;
import com.digihealth.anesthesia.evt.po.EvtAnaesEvent;
import com.digihealth.anesthesia.evt.po.EvtCtlBreath;
import com.digihealth.anesthesia.evt.po.EvtElectDiogData;
import com.digihealth.anesthesia.evt.service.EvtParticipantService;
import com.digihealth.anesthesia.operProceed.core.MyConstants;
import com.digihealth.anesthesia.operProceed.formbean.BasAnaesMonitorConfigFormBean;
import com.digihealth.anesthesia.operProceed.formbean.CtlBreathDateFormBean;
import com.digihealth.anesthesia.operProceed.formbean.EndOperationFormBean;
import com.digihealth.anesthesia.operProceed.formbean.EnterRoomFormBean;
import com.digihealth.anesthesia.operProceed.formbean.FirstSpotFormBean;
import com.digihealth.anesthesia.operProceed.formbean.IntervalDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.LegendData;
import com.digihealth.anesthesia.operProceed.formbean.MonDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.MonitorData;
import com.digihealth.anesthesia.operProceed.formbean.MonitorDataFormBean;
import com.digihealth.anesthesia.operProceed.formbean.SeriesData;
import com.digihealth.anesthesia.operProceed.formbean.SeriesDataObj;
import com.digihealth.anesthesia.operProceed.formbean.SeriesWave;
import com.digihealth.anesthesia.operProceed.formbean.UpdAnaesMonitorConfigFormbean;
import com.digihealth.anesthesia.operProceed.formbean.XAxisData1;
import com.digihealth.anesthesia.operProceed.formbean.XAxisDataBean;
import com.digihealth.anesthesia.operProceed.formbean.YAxisData;
import com.digihealth.anesthesia.operProceed.po.BasAnaesMonitorConfig;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfig;
import com.digihealth.anesthesia.operProceed.po.BasMonitorConfigDefault;
import com.digihealth.anesthesia.operProceed.po.BasMonitorDisplay;
import com.digihealth.anesthesia.operProceed.po.BasMonitorFreqChange;
import com.digihealth.anesthesia.sysMng.formbean.UserInfoFormBean;
import com.digihealth.anesthesia.sysMng.po.BasDictItem;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@org.springframework.stereotype.Controller
@RequestMapping("/operCtl")
@Api(value = "MyOperationDataController", description = "术中总控处理类")
public class MyOperationDataController extends BaseController {
	
	/**
	 * 描点数据和表格数据(历史)，无需发送采集数据模块 如果是已经修改的值，在具体的点上面增加flag
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getobsData")
	@ResponseBody
	@ApiOperation(value = "描点历史数据查询", httpMethod = "POST", notes = "描点历史数据查询")
	public Map getobsData(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDataFormBean formBean) {
		logger.info("----------------start getobsDataBase------------------------");
		Map map = new HashMap();
		LegendData legend = new LegendData();
		List<String> legendData = new ArrayList<String>();

		List<XAxisData1> xAxis = new ArrayList<XAxisData1>();
		XAxisData1 xaisData = null;
		List<XAxisDataBean> data = new ArrayList<XAxisDataBean>();

		// 建造yAxis数据
		List<YAxisData> yAxis = new ArrayList<YAxisData>();
		YAxisData yd = null;
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("bpm");
        yd.setOrder(1);
        yAxis.add(yd);
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("℃");
        yd.setOrder(2);
        yAxis.add(yd);
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("mmHg");
        yd.setOrder(3);
        yAxis.add(yd);
        Collections.sort(yAxis);

		List<SeriesData> series = new ArrayList<SeriesData>();
		SeriesData seriesdata = null;
		List<SeriesDataObj> da = null;
		SeriesDataObj obj = null;
		Map<String, SeriesData> seriesMap = new HashMap<String, SeriesData>();

		// 获取需要显示的数据
		String regOptId = formBean.getRegOptId();
		Integer position = 0;
		String beid = formBean.getBeid();
		Integer docType = formBean.getDocType();
		if (StringUtils.isBlank(beid))
		{
		    beid = getBeid();
		    formBean.setBeid(beid);
		}

		BaseInfoQuery baseQuery = new BaseInfoQuery();
		baseQuery.setRegOptId(regOptId);
		baseQuery.setPosition(position + "");
		baseQuery.setEnable("1");
		baseQuery.setDocType(docType);
		List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
		
		List<String> observeIds = new ArrayList<String>();

		if (null != anaesLists && anaesLists.size() > 0) {
			for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
				String observeId = bean.getEventId();
				// 获取显示项需要的observeIds
				observeIds.add(observeId);
				String observeName = bean.getEventName();
				String color = bean.getColor();// 对应图标
				String icon = bean.getIconId();// 对应颜色
				String svg = basIconSvgService.getPathByIcon(bean.getIconId(), beid);
				String units = bean.getUnits(); // 默认单位
				Float max = bean.getMax(); // max
				Float min = bean.getMin(); // min
				legendData.add(observeName);

				// ABP_HR，CVP，℃
				if (observeId.equals(MyConstants.TEMP_EVENT_ID)) { // 如果是温度，则y轴为2
					seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 1, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
				} else if (observeId.equals(MyConstants.CVP_SYS_EVENT_ID) || observeId.equals(MyConstants.CVP_DIA_EVENT_ID)) { // cvp,则y轴为1
					seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 2, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
				} else {// abp_hp等
					seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 0, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
				}
			}
		}

		List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsData(formBean, observeIds);

		DocAnaesRecord anaesRecord = docAnaesRecordService.getAnaesRecord(regOptId);
        String docId = "";
        if( null != anaesRecord ){
            docId = anaesRecord.getAnaRecordId();
        }
        SearchFormBean searchBean = new SearchFormBean();
        searchBean.setDocId(docId);
        searchBean.setDocType(docType);
        List<EvtCtlBreath> ctlBreathList = evtCtlBreathService.searchCtlBreathList(searchBean);
        CtlBreathDateFormBean cbFormBean = null;
        List<CtlBreathDateFormBean> cbList = new ArrayList<CtlBreathDateFormBean>();
        
        if(null != ctlBreathList && ctlBreathList.size()>0){
            for (int i = 0; i < ctlBreathList.size(); i++) {
                EvtCtlBreath ctlBreath = ctlBreathList.get(i);
                //String curState = ctlBreath.getCurrentState();
                Integer type = ctlBreath.getType();
                if(i == 0){
                    cbFormBean = new CtlBreathDateFormBean();
                    cbFormBean.setStartTime(ctlBreath.getStartTime());
                    cbFormBean.setType(type);
                    List<SysCodeFormbean> scList = basSysCodeService.searchSysCodeByGroupIdAndCodeName("breath_event", type + "", beid);
                    if(null != scList && scList.size()>0){
                        String codeValue = scList.get(0).getCodeValue();
                        cbFormBean.setCodeValue(codeValue);
                        cbFormBean.setCodeSvg(basIconSvgService.getPathByIcon(codeValue, beid));
                    }
                    cbList.add(cbFormBean);
                }else if(i > 0){
                    cbFormBean = new CtlBreathDateFormBean();
                    cbFormBean.setStartTime(ctlBreath.getStartTime());
                    cbFormBean.setEndTime(cbList.get(cbList.size()-1).getStartTime());
                    cbFormBean.setType(type);
                    List<SysCodeFormbean> scList = basSysCodeService.searchSysCodeByGroupIdAndCodeName("breath_event", type + "", beid);
                    if(null != scList && scList.size()>0){
                        String codeValue = scList.get(0).getCodeValue();
                        cbFormBean.setCodeValue(codeValue);
                        cbFormBean.setCodeSvg(basIconSvgService.getPathByIcon(codeValue, beid));
                    }
                    cbList.add(cbFormBean);
                }
                    
            }
        }

		Date t = new Date(1L);
		if (null != monitorList && monitorList.size() > 0) {
			for (int i = 0; i < monitorList.size(); i++) {
				BasMonitorDisplay md = monitorList.get(i);
				String key = md.getObserveId();
				Date time = md.getTime();

				if (t.getTime() != time.getTime()) {
					t = time;
					XAxisDataBean bean = new XAxisDataBean();
					bean.setValue(t);
					Integer freq = md.getIntervalTime();
					bean.setFreq(freq);
					data.add(bean);
				}
				// series
				if (!seriesMap.containsKey(key)) {
					logger.info("------------------没有当前key" + key + "------------------------");
				} else {
					seriesdata = seriesMap.get(key);
					da = seriesdata.getData();
					seriesdata.setType("line");
					seriesdata.setName(md.getObserveName());

					// 设置指定对应的y轴
					if (MyConstants.TEMP_EVENT_ID.equals(md.getObserveId())) {
						seriesdata.setyAxisIndex(1);
					} else if (MyConstants.CVP_SYS_EVENT_ID.equals(md.getObserveId()) || MyConstants.CVP_DIA_EVENT_ID.equals(md.getObserveId())) {
						seriesdata.setyAxisIndex(2);
					} else {
						seriesdata.setyAxisIndex(0);
					}

					// 增加呼吸事件图标的判断(呼吸机和麻醉机的observeId)
					if (MyConstants.RR_EVENT_ID.equals(md.getObserveId())) {
						if (null != cbList && cbList.size() > 0) {
							int flag = -1;
							for (CtlBreathDateFormBean cb : cbList) {
								Date st = cb.getStartTime();
								Date et = cb.getEndTime();
								// logger.info("getobsData----st="+st+",et="+et+",time="+time);
								if (null != et) {
									if (time.getTime() >= st.getTime() && time.getTime() < et.getTime()) {
										obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue(), md.getValue() != null ? cb.getCodeSvg() : "");
										flag = 0;
										break;
									}
								} else {
									if (time.getTime() >= st.getTime()) {
										obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue(), md.getValue() != null ? cb.getCodeSvg() : "");
										flag = 0;
										break;
									}
								}
							}
							if (flag == -1) {
								obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
							}
						} else {
							obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
						}
					} else {
						obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
					}
					// 如果 amendFlag为2，则设值
					if (null != md.getAmendFlag()) {
						if (md.getAmendFlag() == 2) {
							obj.setAmendFlag(md.getAmendFlag());
						}
					}
					da.add(obj);
					seriesdata.setData(da);
					seriesMap.put(key, seriesdata);
				}

			}

		} else {
			// 无采集数据
			logger.info("----------无采集数据----------------");
		}

		// 循环seriesMap中的数据
		for (String key : seriesMap.keySet()) {
			SeriesData sd = seriesMap.get(key);
			series.add(sd);
		}

		// 添加times到x轴
		xaisData = new XAxisData1();
		xaisData.setData(data);
		xAxis.add(xaisData);

		// 获取总数
		int total = basMonitorDisplayService.getobsDataTotal(formBean, observeIds);

		// 获取修改频率的List
		List<BasMonitorFreqChange> monitorFreqChanges = basMonitorFreqChangeService.getMonitorFreqChanges(regOptId);

		String f = "";
		Integer freq = 0;
		
		if(Objects.equals(2, docType)){
			BasDictItem dictItem = basDictItemService.getListByGroupIdandCodeName("pacu_freq", "复苏室显示频率",beid);
			if(null != dictItem){
				freq = Integer.valueOf(dictItem.getCodeValue());
			}
		}else{
			// 获取当前频率
			String currentModel = basRegOptService.getCurrentModel(regOptId);
			BasMonitorConfigFreq monitorFreq = basMonitorConfigFreqService.searchMonitorFreqByType(currentModel,regOptId);
			if (monitorFreq != null) {
				f = monitorFreq.getValue();
				freq = Integer.valueOf(f);
			}
		}
		
		// 获取最近点
		BasMonitorDisplay md = basMonitorDisplayService.findLastestMonitorDisplay(regOptId, docType);

		map.put("xAxis", xAxis);
		map.put("yAxis", yAxis);
		map.put("series", series);
		// 有显示项 则增加legend数据，传递给前端
		legend.setData(legendData);
		map.put("legend", legend);
		// 获取数据total
		map.put("total", total);
		map.put("monitorFreqChanges", monitorFreqChanges);
		map.put("freq", freq);
		map.put("md", md);

		// 计算偏移量
		Date inTime = formBean.getInTime();

		long offset = 0;// 偏移量
		BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
		DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecService.getAnaesPacuRecByRegOptId(regOptId);
		if ((null == regOpt && Objects.equals(1, docType)) || (null == anaesPacuRec && Objects.equals(2, docType))) {
			logger.info("getobsData----regOpt手术为null或者复苏记录单信息为null！--------------");
			map.put("resultCode", "10000000");
			map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
			return map;
		} else {
		    Date operTime = null;
		    if(Objects.equals(2, docType)){//如果是进入pacu则获取pacu对应的显示指标项
		        operTime = anaesPacuRec.getOperTime();
	        }else{
	            operTime = regOpt.getOperTime();
	        }
			if (null == operTime) {
				logger.info("getobsData----operTime时间为null！--------------");
				map.put("resultCode", "10000000");
				map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
				return map;
			} else {
				BasMonitorFreqChange monitorFreqChange = basMonitorFreqChangeService.selectFirstChangeTime(regOptId, DateUtils.formatDateTime(inTime));
				if (null == monitorFreqChange) {
					map.put("changeFreqTime", null);
				} else {
					map.put("changeFreqTime", monitorFreqChange.getTime());
				}
				long time = inTime.getTime() - operTime.getTime();
				if (time == 0) {
					map.put("offset", offset);
				} else if (time < 0) { // 修改后的入室时间小于第一次的手术命令开始时间
					BasMonitorDisplay firstMd = basMonitorDisplayService.findMonitorDisplaybyTime(regOptId, SysUtil.getTimeFormat(operTime), docType);
					long operFreq = 0;
					if (null != firstMd) {
						operFreq = firstMd.getFreq();
						offset = (Math.abs(time) % (operFreq * 1000)) / 1000;// 取绝对值取mod
						map.put("offset", offset);
					} else {
						logger.info("getobsData----operTime查询出来的MonitorDisplay为null！--------------");
						map.put("resultCode", "10000000");
						map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
						return map;
					}
				} else { // time > 0
					BasMonitorDisplay curMd = basMonitorDisplayService.findMonitorDisplayByInTimeLimit1(regOptId, inTime, docType);
					if (null != curMd) {
						Date curTime = curMd.getTime();
						Integer operFreq = curMd.getFreq();
						Integer intervalTime = curMd.getIntervalTime();
						if (operFreq.intValue() != intervalTime.intValue()) {
							long myTime = ((curTime.getTime() + intervalTime * 1000) - inTime.getTime()) / 1000;
							if (myTime > 0) { // 1、如果最近点+intervalTime大于intime，则 offset =(curTime+interval_time)-inTime
								map.put("offset", myTime);
							} else if (myTime == 0) {
								map.put("offset", 0);
							} else { // curTime+intervalTime 必须大于 inTime ，不然数据有误
								logger.info("getobsData----获取到的curTime+intervalTime小于修改的inTime，数据有误--------------");
								map.put("resultCode", "10000000");
								map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
								return map;
							}
						} else {
							long cTime = (inTime.getTime() - curTime.getTime()) / 1000; // offset = operFreq - (intime -curTime) ;
							if (cTime == 0) {
								map.put("offset", 0);
							} else {
								offset = operFreq - cTime;// 当前频率-（入室时间-当前最近点时间）
								logger.info("offset===" + offset + ",cTime==" + cTime + ",inTime=" + inTime + ",curTime==" + curTime);
								map.put("offset", offset);
							}
						}
					} else {
						logger.info("getobsData----inTime查询出来的最近的右边的MonitorDisplay为null！--------------");
						map.put("resultCode", "10000000");
						map.put("resultMessage", "查询数据有误，请与系统管理员联系!");
						return map;
					}
				}
			}
		}

		map.put("resultCode", "1");
		map.put("resultMessage", "操作成功！");

		logger.info("getobsData---allData===" + JsonType.jsonType(map) + ",inTime==" + formBean.getInTime());
		logger.info("------------------end getobsData------------------------");
		return map;
	}
	
	
	/**
     * 获取新点
     * 
     * @param formBean
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/getobsDataNew")
    @ResponseBody
    @ApiOperation(value = "获取新点", httpMethod = "POST", notes = "获取新点")
    public Map getobsDataNew(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDataFormBean formBean) {
        logger.info("----------------start getobsDataNew------------------------");
        Map map = new HashMap();
        if (StringUtils.isBlank(formBean.getBeid())) {
            formBean.setBeid(getBeid());
        }
        Date inTime = formBean.getInTime();
        Date startTime = formBean.getStartTime();
        Integer docType = formBean.getDocType();
        // Date endTime = formBean.getEndTime();
        if (null == inTime) {
            map.put("resultCode", "30000001");
            map.put("resultMessage", "入室时间不能为空！");
            return map;
        }
        if (null == startTime) {
            map.put("resultCode", "30000001");
            map.put("resultMessage", "开始时间不能为空！");
            return map;
        }

        LegendData legend = new LegendData();
        List<String> legendData = new ArrayList<String>();

        List<XAxisData1> xAxis = new ArrayList<XAxisData1>();
        XAxisData1 xaisData = null;
        List<XAxisDataBean> data = new ArrayList<XAxisDataBean>();

        // 建造yAxis数据
        List<YAxisData> yAxis = new ArrayList<YAxisData>();
        YAxisData yd = null;
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("bpm");
        yd.setOrder(1);
        yAxis.add(yd);
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("℃");
        yd.setOrder(2);
        yAxis.add(yd);
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("mmHg");
        yd.setOrder(3);
        yAxis.add(yd);
        Collections.sort(yAxis);

        List<SeriesData> series = new ArrayList<SeriesData>();
        SeriesData seriesdata = null;
        List<SeriesDataObj> da = null;
        SeriesDataObj obj = null;
        Map<String, SeriesData> seriesMap = new HashMap<String, SeriesData>();

        // 获取需要显示的数据
        String regOptId = formBean.getRegOptId();
        Integer position = 0;
        String beid = formBean.getBeid();
        if (StringUtils.isBlank(beid))
        {
            beid = getBeid();
            formBean.setBeid(getBeid());
        }

        BaseInfoQuery baseQuery = new BaseInfoQuery();
        baseQuery.setRegOptId(regOptId);
        baseQuery.setPosition(position + "");
        baseQuery.setEnable("1");
        baseQuery.setDocType(docType);
        List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
        boolean flag = false;
        List<String> observeIds = new ArrayList<String>();
        if (null != anaesLists && anaesLists.size() > 0) {
            for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
                // 获取显示项需要的observeIds
                String observeId = bean.getEventId();
                if (MyConstants.O2_EVENT_ID.equals(observeId))
                {
                    flag = true;
                }
                observeIds.add(observeId);
                String observeName = bean.getEventName();
                String color = bean.getColor();// 对应颜色
                String icon = bean.getIconId();// 对应图标
                String svg = basIconSvgService.getPathByIcon(icon, beid);
                String units = bean.getUnits(); // 默认单位
                Float max = bean.getMax();
                Float min = bean.getMin();
                legendData.add(observeName);

                // ABP_HR，CVP，℃
                if (observeId.equals(MyConstants.TEMP_EVENT_ID)) { // 如果是温度，则y轴为2
                    seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 1, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                } else if (observeId.equals(MyConstants.CVP_SYS_EVENT_ID) || observeId.equals(MyConstants.CVP_DIA_EVENT_ID)) { // cvp,则y轴为1
                    seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 2, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                } else {// abp_hp等,y轴为0
                    seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 0, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                }
            }
        }
        //没有氧流量ID，则需要添加一个
        if (!flag)
        {
            observeIds.add(MyConstants.O2_EVENT_ID);
        }

        List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsDataNew4(formBean, observeIds); // 增加特殊处理逻辑
        
        SearchFormBean searchBean = new SearchFormBean();
        DocAnaesRecord anaesRecord = docAnaesRecordService.getAnaesRecord(regOptId);
        String docId = "";
        if( null != anaesRecord ){
            docId = anaesRecord.getAnaRecordId();
        }
        searchBean.setDocId(docId);
        searchBean.setDocType(docType);
        
        List<EvtCtlBreath> ctlBreathList = evtCtlBreathService.searchCtlBreathList(searchBean);
        int type = 0;
        if(null != ctlBreathList && ctlBreathList.size()>0){ 
            type = ctlBreathList.get(0).getType();
        }
        List<SysCodeFormbean> sysCodeList = basSysCodeService.searchSysCodeByGroupId("breath_event", formBean.getBeid());
        String code_value_1 = "", svg_value_1 = "";
        String code_value_2 = "", svg_value_2 = "";
        String code_value_3 = "", svg_value_3 = "";
        if(null != sysCodeList && sysCodeList.size()>0){
            for (SysCodeFormbean sysCodeFormbean : sysCodeList) {
                String codeName = sysCodeFormbean.getCodeName();
                if("1".equals(codeName)){
                    code_value_1 = sysCodeFormbean.getCodeValue();
                    svg_value_1 = basIconSvgService.getPathByIcon(code_value_1, beid);
                }else if("2".equals(codeName)){
                    code_value_2 = sysCodeFormbean.getCodeValue();
                    svg_value_2 = basIconSvgService.getPathByIcon(code_value_2, beid);
                }else if("3".equals(codeName)){
                    code_value_3 = sysCodeFormbean.getCodeValue();
                    svg_value_3 = basIconSvgService.getPathByIcon(code_value_3, beid);
                }
            }
        }

        Date t = new Date(1L);
        if (null != monitorList && monitorList.size() > 0) {
            for (int i = 0; i < monitorList.size(); i++) {
                BasMonitorDisplay md = monitorList.get(i);
                String key = md.getObserveId();
                Date time = md.getTime();
                if (t.getTime() != time.getTime()) {
                    t = time;
                    // data.add(t);
                    XAxisDataBean bean = new XAxisDataBean();
                    bean.setValue(t);
                    Integer freq = md.getIntervalTime();
                    bean.setFreq(freq);
                    data.add(bean);
                }
                // series
                if (!seriesMap.containsKey(key)) {
                    logger.info("------------------没有当前key" + key + "------------------------");
                } else {
                    seriesdata = seriesMap.get(key);
                    da = seriesdata.getData();
                    seriesdata.setType("line");
                    seriesdata.setName(md.getObserveName());
                    // 设置指定对应的y轴
                    if (MyConstants.TEMP_EVENT_ID.equals(md.getObserveId())) {
                        seriesdata.setyAxisIndex(1);
                    } else if (MyConstants.CVP_DIA_EVENT_ID.equals(md.getObserveId()) || MyConstants.CVP_SYS_EVENT_ID.equals(md.getObserveId())) {
                        seriesdata.setyAxisIndex(2);
                    } else {
                        seriesdata.setyAxisIndex(0);
                    }
                    // seriesdata.setSymbolSize(8);
                    if (MyConstants.RR_EVENT_ID.equals(md.getObserveId())) {
                        if (1 == type) {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), code_value_1, svg_value_1);
                        } else if (2 == type) {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), code_value_2, svg_value_2);
                        } else if (3 == type) {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), code_value_3, svg_value_3);
                        } else {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
                        }
                    } else {
                        obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
                    }

                    da.add(obj);
                    seriesdata.setData(da);
                    seriesMap.put(key, seriesdata);
                }

            }

        } else {
            // 无采集数据
            logger.info("----------无采集数据----------------");
        }

        // 循环seriesMap中的数据
        for (String key : seriesMap.keySet()) {
            SeriesData sd = seriesMap.get(key);
            series.add(sd);
        }

        // 添加times到x轴
        xaisData = new XAxisData1();
        xaisData.setData(data);
        xAxis.add(xaisData);

        // 获取总数
        int total = basMonitorDisplayService.getobsDataTotal(formBean, observeIds);

       
        String f = "";
		Integer freq = 0;
		
		if(Objects.equals(2, docType)){
			BasDictItem dictItem = basDictItemService.getListByGroupIdandCodeName("pacu_freq", "复苏室显示频率",beid);
			if(null != dictItem){
				freq = Integer.valueOf(dictItem.getCodeValue());
			}
		}else{
			// 获取当前频率
			String currentModel = basRegOptService.getCurrentModel(regOptId);
			BasMonitorConfigFreq monitorFreq = basMonitorConfigFreqService.searchMonitorFreqByType(currentModel,regOptId);
			if (monitorFreq != null) {
				f = monitorFreq.getValue();
				freq = Integer.valueOf(f);
			}
		}
		// 获取修改频率的List
        BasMonitorFreqChange monitorFreqChange = basMonitorFreqChangeService.selectFirstChangeTime(regOptId, DateUtils.formatDateTime(inTime));
        if (null == monitorFreqChange) {
            map.put("changeFreqTime", null);
        } else {
            map.put("changeFreqTime", monitorFreqChange.getTime());
        }

        map.put("xAxis", xAxis);
        map.put("yAxis", yAxis);
        map.put("series", series);
        // 有显示项 则增加legend数据，传递给前端
        legend.setData(legendData);
        map.put("legend", legend);
        // 获取数据total
        map.put("total", total);
        map.put("freq", freq);

        map.put("resultCode", "1");
        map.put("resultMessage", "操作成功！");

        logger.info("------------------end getobsDataNew------------------------");
        return map;
    }
	
    
    
    /**
     * 获取新点
     * 
     * @param formBean
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/getobsDataNewHNHTYY")
    @ResponseBody
    @ApiOperation(value = "获取新点", httpMethod = "POST", notes = "获取新点")
    public Map getobsDataNewHNHTYY(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDataFormBean formBean) {
        logger.info("----------------start getobsDataNew------------------------");
        Map map = new HashMap();
        if (StringUtils.isBlank(formBean.getBeid())) {
            formBean.setBeid(getBeid());
        }
        Date inTime = formBean.getInTime();
        Date startTime = formBean.getStartTime();
        Integer docType = formBean.getDocType();
        // Date endTime = formBean.getEndTime();
        if (null == inTime) {
            map.put("resultCode", "30000001");
            map.put("resultMessage", "入室时间不能为空！");
            return map;
        }
        if (null == startTime) {
            map.put("resultCode", "30000001");
            map.put("resultMessage", "开始时间不能为空！");
            return map;
        }

        LegendData legend = new LegendData();
        List<String> legendData = new ArrayList<String>();

        List<XAxisData1> xAxis = new ArrayList<XAxisData1>();
        XAxisData1 xaisData = null;
        List<XAxisDataBean> data = new ArrayList<XAxisDataBean>();

        // 建造yAxis数据
        List<YAxisData> yAxis = new ArrayList<YAxisData>();
        YAxisData yd = null;
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("bpm");
        yd.setOrder(1);
        yAxis.add(yd);
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("℃");
        yd.setOrder(2);
        yAxis.add(yd);
        yd = new YAxisData();
        yd.setType("value");
        yd.setName("mmHg");
        yd.setOrder(3);
        yAxis.add(yd);
        Collections.sort(yAxis);

        List<SeriesData> series = new ArrayList<SeriesData>();
        SeriesData seriesdata = null;
        List<SeriesDataObj> da = null;
        SeriesDataObj obj = null;
        Map<String, SeriesData> seriesMap = new HashMap<String, SeriesData>();

        // 获取需要显示的数据
        String regOptId = formBean.getRegOptId();
        Integer position = 0;
        String beid = formBean.getBeid();
        if (StringUtils.isBlank(beid))
        {
            beid = getBeid();
            formBean.setBeid(getBeid());
        }

        BaseInfoQuery baseQuery = new BaseInfoQuery();
        baseQuery.setRegOptId(regOptId);
        baseQuery.setPosition(position + "");
        baseQuery.setEnable("1");
        baseQuery.setDocType(docType);
        List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
        
        List<String> observeIds = new ArrayList<String>();
        boolean flag = false;
        if (null != anaesLists && anaesLists.size() > 0) {
            for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
                String observeId = bean.getEventId();
                if (MyConstants.O2_EVENT_ID.equals(observeId))
                {
                    flag = true;
                }
                // 获取显示项需要的observeIds
                observeIds.add(observeId);
                String observeName = bean.getEventName();
                String color = bean.getColor();// 对应颜色
                String icon = bean.getIconId();// 对应图标
                String svg = basIconSvgService.getPathByIcon(icon, beid);
                String units = bean.getUnits(); // 默认单位
                Float max = bean.getMax();
                Float min = bean.getMin();
                legendData.add(observeName);

                // ABP_HR，CVP，℃
                if (observeId.equals(MyConstants.TEMP_EVENT_ID)) { // 如果是温度，则y轴为2
                    seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 1, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                } else if (observeId.equals(MyConstants.CVP_SYS_EVENT_ID) || observeId.equals(MyConstants.CVP_DIA_EVENT_ID)) { // cvp,则y轴为1
                    seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 2, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                } else {// abp_hp等,y轴为0
                    seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 0, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                }

            }
        }
        //没有氧流量ID，则需要添加一个
        if (!flag)
        {
            observeIds.add(MyConstants.O2_EVENT_ID);
        }
        List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsDataNew4HNHTYY(formBean, observeIds); // 增加特殊处理逻辑
        
        SearchFormBean searchBean = new SearchFormBean();
        DocAnaesRecord anaesRecord = docAnaesRecordService.getAnaesRecord(regOptId);
        String docId = "";
        if( null != anaesRecord ){
            docId = anaesRecord.getAnaRecordId();
        }
        searchBean.setDocId(docId);
        searchBean.setDocType(docType);
        
        List<EvtCtlBreath> ctlBreathList = evtCtlBreathService.searchCtlBreathList(searchBean);
        int type = 0;
        if(null != ctlBreathList && ctlBreathList.size()>0){ 
            type = ctlBreathList.get(0).getType();
        }
        List<SysCodeFormbean> sysCodeList = basSysCodeService.searchSysCodeByGroupId("breath_event", formBean.getBeid());
        String code_value_1 = "", svg_value_1 = "";
        String code_value_2 = "", svg_value_2 = "";
        String code_value_3 = "", svg_value_3 = "";
        if(null != sysCodeList && sysCodeList.size()>0){
            for (SysCodeFormbean sysCodeFormbean : sysCodeList) {
                String codeName = sysCodeFormbean.getCodeName();
                if("1".equals(codeName)){
                    code_value_1 = sysCodeFormbean.getCodeValue();
                    svg_value_1 = basIconSvgService.getPathByIcon(code_value_1, beid);
                }else if("2".equals(codeName)){
                    code_value_2 = sysCodeFormbean.getCodeValue();
                    svg_value_2 = basIconSvgService.getPathByIcon(code_value_2, beid);
                }else if("3".equals(codeName)){
                    code_value_3 = sysCodeFormbean.getCodeValue();
                    svg_value_3 = basIconSvgService.getPathByIcon(code_value_3, beid);
                }
            }
        }

        Date t = new Date(1L);
        if (null != monitorList && monitorList.size() > 0) {
            for (int i = 0; i < monitorList.size(); i++) {
                BasMonitorDisplay md = monitorList.get(i);
                String key = md.getObserveId();
                Date time = md.getTime();
                if (t.getTime() != time.getTime()) {
                    t = time;
                    // data.add(t);
                    XAxisDataBean bean = new XAxisDataBean();
                    bean.setValue(t);
                    Integer freq = md.getIntervalTime();
                    bean.setFreq(freq);
                    data.add(bean);
                }
                // series
                if (!seriesMap.containsKey(key)) {
                    logger.info("------------------没有当前key" + key + "------------------------");
                } else {
                    seriesdata = seriesMap.get(key);
                    da = seriesdata.getData();
                    seriesdata.setType("line");
                    seriesdata.setName(md.getObserveName());
                    // 设置指定对应的y轴
                    if (MyConstants.TEMP_EVENT_ID.equals(md.getObserveId())) {
                        seriesdata.setyAxisIndex(1);
                    } else if (MyConstants.CVP_DIA_EVENT_ID.equals(md.getObserveId()) || MyConstants.CVP_SYS_EVENT_ID.equals(md.getObserveId())) {
                        seriesdata.setyAxisIndex(2);
                    } else {
                        seriesdata.setyAxisIndex(0);
                    }
                    // seriesdata.setSymbolSize(8);
                    if (MyConstants.RR_EVENT_ID.equals(md.getObserveId())) {
                        if (1 == type) {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), code_value_1, svg_value_1);
                        } else if (2 == type) {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), code_value_2, svg_value_2);
                        } else if (3 == type) {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), code_value_3, svg_value_3);
                        } else {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
                        }
                    } else {
                        obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
                    }

                    da.add(obj);
                    seriesdata.setData(da);
                    seriesMap.put(key, seriesdata);
                }

            }

        } else {
            // 无采集数据
            logger.info("----------无采集数据----------------");
        }

        // 循环seriesMap中的数据
        for (String key : seriesMap.keySet()) {
            SeriesData sd = seriesMap.get(key);
            series.add(sd);
        }

        // 添加times到x轴
        xaisData = new XAxisData1();
        xaisData.setData(data);
        xAxis.add(xaisData);

        // 获取总数
        int total = basMonitorDisplayService.getobsDataTotal(formBean, observeIds);

       
        String f = "";
		Integer freq = 0;
		
		if(Objects.equals(2, docType)){
			BasDictItem dictItem = basDictItemService.getListByGroupIdandCodeName("pacu_freq", "复苏室显示频率",beid);
			if(null != dictItem){
				freq = Integer.valueOf(dictItem.getCodeValue());
			}
		}else{
			// 获取当前频率
			String currentModel = basRegOptService.getCurrentModel(regOptId);
			BasMonitorConfigFreq monitorFreq = basMonitorConfigFreqService.searchMonitorFreqByType(currentModel,regOptId);
			if (monitorFreq != null) {
				f = monitorFreq.getValue();
				freq = Integer.valueOf(f);
			}
		}
		// 获取修改频率的List
        BasMonitorFreqChange monitorFreqChange = basMonitorFreqChangeService.selectFirstChangeTime(regOptId, DateUtils.formatDateTime(inTime));
        if (null == monitorFreqChange) {
            map.put("changeFreqTime", null);
        } else {
            map.put("changeFreqTime", monitorFreqChange.getTime());
        }

        map.put("xAxis", xAxis);
        map.put("yAxis", yAxis);
        map.put("series", series);
        // 有显示项 则增加legend数据，传递给前端
        legend.setData(legendData);
        map.put("legend", legend);
        // 获取数据total
        map.put("total", total);
        map.put("freq", freq);

        map.put("resultCode", "1");
        map.put("resultMessage", "操作成功！");

        logger.info("------------------end getobsDataNew------------------------");
        return map;
    }
    
    
	
	
	/**
	 * 页面再次打开，间隔点的处理
	 * 
	 * @param formbean
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getIntervalObsData")
	@ResponseBody
	@ApiOperation(value = "页面再次打开，间隔点的处理", httpMethod = "POST", notes = "页面再次打开，间隔点的处理")
    public Map getIntervalObsData(@ApiParam(name = "formbean", value = "参数")@RequestBody IntervalDataFormBean formbean) {
        logger.info("----------------start getIntervalObsData------------------------");
        ResponseValue res = new ResponseValue();
        Map map = new HashMap();
        if (formbean != null) {
            basMonitorDisplayService.getIntervalObsData(formbean); // 获取中间断的点

            LegendData legend = new LegendData();
            List<String> legendData = new ArrayList<String>();

            List<XAxisData1> xAxis = new ArrayList<XAxisData1>();
            XAxisData1 xaisData = null;
            List<XAxisDataBean> data = new ArrayList<XAxisDataBean>();

            // 建造yAxis数据
            List<YAxisData> yAxis = new ArrayList<YAxisData>();
            YAxisData yd = null;
            yd = new YAxisData();
            yd.setType("value");
            yd.setName("bpm");
            yd.setOrder(1);
            yAxis.add(yd);
            yd = new YAxisData();
            yd.setType("value");
            yd.setName("℃");
            yd.setOrder(2);
            yAxis.add(yd);
            yd = new YAxisData();
            yd.setType("value");
            yd.setName("mmHg");
            yd.setOrder(3);
            yAxis.add(yd);
            Collections.sort(yAxis);

            List<SeriesData> series = new ArrayList<SeriesData>();
            SeriesData seriesdata = null;
            List<SeriesDataObj> da = null;
            SeriesDataObj obj = null;
            Map<String, SeriesData> seriesMap = new HashMap<String, SeriesData>();

            // 获取需要显示的数据
            String regOptId = formbean.getRegOptId();
            Integer position = 0;
            String beid = formbean.getBeid();
            Integer docType = formbean.getDocType();
            if (StringUtils.isBlank(beid))
            {
                beid = getBeid();
                formbean.setBeid(beid);
            }

            BaseInfoQuery baseQuery = new BaseInfoQuery();
            baseQuery.setRegOptId(regOptId);
            baseQuery.setPosition(position + "");
            baseQuery.setEnable("1");
            baseQuery.setDocType(docType);
            
            List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
            
            List<String> observeIds = new ArrayList<String>();

            if (null != anaesLists && anaesLists.size() > 0) {
                for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
                    String observeId = bean.getEventId();
                    // 获取显示项需要的observeIds
                    observeIds.add(observeId);
                    String observeName = bean.getEventName();
                    String color = bean.getColor();// 对应图标
                    String icon = bean.getIconId();// 对应颜色
                    String svg = basIconSvgService.getPathByIcon(icon, beid);
                    String units = bean.getUnits(); // 默认单位
                    Float max = bean.getMax(); // max
                    Float min = bean.getMin(); // min
                    legendData.add(observeName);
                    // ABP_HR，CVP，℃
                    if (observeId.equals(MyConstants.TEMP_EVENT_ID)) { // 如果是温度，则y轴为2
                        seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 1, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                    } else if (observeId.equals(MyConstants.CVP_SYS_EVENT_ID) || observeId.equals(MyConstants.CVP_DIA_EVENT_ID)) { // cvp,则y轴为1
                        seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 2, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                    } else {// abp_hp等,y轴为0
                        seriesMap.put(observeId, new SeriesData(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), icon, svg, 0, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
                    }
                }
            }
            MonitorDataFormBean mdFormBean = new MonitorDataFormBean();
            BeanUtils.copyProperties(formbean, mdFormBean);
            List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsData(mdFormBean, observeIds);

            DocAnaesRecord anaesRecord = docAnaesRecordService.getAnaesRecord(regOptId);
            String docId = "";
            if( null != anaesRecord ){
                docId = anaesRecord.getAnaRecordId();
            }
            SearchFormBean searchBean = new SearchFormBean();
            searchBean.setDocId(docId);
            searchBean.setDocType(docType);
            List<EvtCtlBreath> ctlBreathList = evtCtlBreathService.searchCtlBreathList(searchBean);
            CtlBreathDateFormBean cbFormBean = null;
            List<CtlBreathDateFormBean> cbList = new ArrayList<CtlBreathDateFormBean>();
            
            if(null != ctlBreathList && ctlBreathList.size()>0){
                for (int i = 0; i < ctlBreathList.size(); i++) {
                    EvtCtlBreath ctlBreath = ctlBreathList.get(i);
                    //String curState = ctlBreath.getCurrentState();
                    Integer type = ctlBreath.getType();
                    if(i == 0){
                        cbFormBean = new CtlBreathDateFormBean();
                        cbFormBean.setStartTime(ctlBreath.getStartTime());
                        cbFormBean.setType(type);
                        List<SysCodeFormbean> scList = basSysCodeService.searchSysCodeByGroupIdAndCodeName("breath_event", type + "", beid);
                        if(null != scList && scList.size()>0){
                            String codeValue = scList.get(0).getCodeValue();
                            cbFormBean.setCodeValue(codeValue);
                            cbFormBean.setCodeSvg(basIconSvgService.getPathByIcon(codeValue, beid));
                        }
                        cbList.add(cbFormBean);
                    }else if(i > 0){
                        cbFormBean = new CtlBreathDateFormBean();
                        cbFormBean.setStartTime(ctlBreath.getStartTime());
                        cbFormBean.setEndTime(cbList.get(cbList.size()-1).getStartTime());
                        cbFormBean.setType(type);
                        List<SysCodeFormbean> scList = basSysCodeService.searchSysCodeByGroupIdAndCodeName("breath_event", type + "", beid);
                        if(null != scList && scList.size()>0){
                            String codeValue = scList.get(0).getCodeValue();
                            cbFormBean.setCodeValue(codeValue);
                            cbFormBean.setCodeSvg(basIconSvgService.getPathByIcon(codeValue, beid));
                        }
                        cbList.add(cbFormBean);
                    }
                        
                }
            }

            Date t = new Date(1L);
            if (null != monitorList && monitorList.size() > 0) {
                for (int i = 0; i < monitorList.size(); i++) {
                    BasMonitorDisplay md = monitorList.get(i);
                    String key = md.getObserveId();
                    Date time = md.getTime();

                    if (t.getTime() != time.getTime()) {
                        t = time;
                        XAxisDataBean bean = new XAxisDataBean();
                        bean.setValue(t);
                        Integer freq = md.getIntervalTime();
                        bean.setFreq(freq);
                        data.add(bean);
                    }
                    // series
                    if (!seriesMap.containsKey(key)) {
                        logger.info("------------------没有当前key" + key + "------------------------");
                    } else {
                        seriesdata = seriesMap.get(key);
                        da = seriesdata.getData();
                        seriesdata.setType("line");
                        seriesdata.setName(md.getObserveName());
                        // 设置指定对应的y轴
                        if (MyConstants.TEMP_EVENT_ID.equals(md.getObserveId())) {
                            seriesdata.setyAxisIndex(1);
                        } else if (MyConstants.CVP_DIA_EVENT_ID.equals(md.getObserveId()) || MyConstants.CVP_SYS_EVENT_ID.equals(md.getObserveId())) {
                            seriesdata.setyAxisIndex(2);
                        } else {
                            seriesdata.setyAxisIndex(0);
                        }
                        // seriesdata.setSymbolSize(8);

                        // 增加呼吸事件图标的判断
                        if (MyConstants.RR_EVENT_ID.equals(md.getObserveId())) {
                            if (null != cbList && cbList.size() > 0) {
                                int flag = -1;
                                for (CtlBreathDateFormBean cb : cbList) {
                                    Date st = cb.getStartTime();
                                    Date et = cb.getEndTime();
                                    // logger.info("getobsData----st="+st+",et="+et+",time="+time);
                                    if (null != et) {
                                        if (time.getTime() >= st.getTime() && time.getTime() < et.getTime()) {
                                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue(), cb.getCodeSvg());
                                            flag = 0;
                                            break;
                                        }
                                    } else {
                                        if (time.getTime() >= st.getTime()) {
                                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId(), cb.getCodeValue(), cb.getCodeSvg());
                                            flag = 0;
                                            break;
                                        }
                                    }
                                }
                                if (flag == -1) {
                                    obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
                                }
                            } else {
                                obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
                            }
                        } else {
                            obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
                        }
                        // 如果 amendFlag为2，则设值
                        if (null != md.getAmendFlag()) {
                            if (md.getAmendFlag() == 2) {
                                obj.setAmendFlag(md.getAmendFlag());
                            }
                        }
                        da.add(obj);
                        seriesdata.setData(da);
                        seriesMap.put(key, seriesdata);
                    }

                }

            } else {
                // 无采集数据
                logger.info("----------无采集数据----------------");
            }

            // 循环seriesMap中的数据
            for (String key : seriesMap.keySet()) {
                SeriesData sd = seriesMap.get(key);
                series.add(sd);
            }

            // 添加times到x轴
            xaisData = new XAxisData1();
            xaisData.setData(data);
            xAxis.add(xaisData);

            // 获取总数
            int total = basMonitorDisplayService.getobsDataTotal(mdFormBean, observeIds);

            // 获取修改频率的List
            List<BasMonitorFreqChange> monitorFreqChanges = basMonitorFreqChangeService.getMonitorFreqChanges(regOptId);

            String f = "";
            Integer freq = 0;
            
            if(Objects.equals(2, docType)){
            	BasDictItem dictItem = basDictItemService.getListByGroupIdandCodeName("pacu_freq", "复苏室显示频率",beid);
            	if(null != dictItem){
            		freq =  SysUtil.strParseToInt(dictItem.getCodeValue());
            	}
            }else{
            	// 获取当前频率
                String currentModel = basRegOptService.getCurrentModel(regOptId);
                BasMonitorConfigFreq monitorFreq = basMonitorConfigFreqService.searchMonitorFreqByType(currentModel,regOptId);
            	if (monitorFreq != null) {
                    f = monitorFreq.getValue();
                    freq = Integer.valueOf(f);
                }
            }
            
            // 获取最近点
            BasMonitorDisplay md = basMonitorDisplayService.findLastestMonitorDisplay(regOptId, docType);

            map.put("xAxis", xAxis);
            map.put("yAxis", yAxis);
            map.put("series", series);
            // 有显示项 则增加legend数据，传递给前端
            legend.setData(legendData);
            map.put("legend", legend);
            // 获取数据total
            map.put("total", total);
            map.put("monitorFreqChanges", monitorFreqChanges);
            map.put("freq", freq);
            map.put("md", md);

            map.put("resultCode", "1");
            map.put("resultMessage", "操作成功！");

        } else {
            map.put("resultCode", "70000000");
            map.put("resultMessage", Global.getRetMsg(res.getResultCode()));
        }

        logger.info("------------------end getIntervalObsData------------------------");
        return map;
    }
	
	
	
	/**
	 * 数字部分：获取历史数据，需分页
	 * 
	 * @param formbean
	 * @return
	 */
	@RequestMapping("/getmonData")
	@ResponseBody
	@ApiOperation(value = "获取监测项分页历史数据", httpMethod = "POST", notes = "获取监测项分页历史数据")
	public String getmonData(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDataFormBean formBean) {
		logger.info("----------------start getmonData------------------------");
		ResponseValue res = new ResponseValue();
		String regOptId = formBean.getRegOptId();
		Integer docType = formBean.getDocType();
		if (StringUtils.isBlank(regOptId)) {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
			logger.info("----------------end getmonData------------------------");
			return res.getJsonStr();
		}

		BaseInfoQuery baseQuery = new BaseInfoQuery();
		baseQuery.setRegOptId(regOptId);
		baseQuery.setPosition("1");
		baseQuery.setDocType(docType);
		
		List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
		List<String> observeIds = new ArrayList<String>();

		if (null != anaesLists && anaesLists.size() > 0) {
			for (BasAnaesMonitorConfigFormBean mc : anaesLists) {
				if (!observeIds.contains(mc.getEventId() + "")) {
					observeIds.add(mc.getEventId() + "");
				}
			}
		}
		List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsData(formBean, observeIds);
		Map<Date, List<BasMonitorDisplay>> map = new TreeMap<Date, List<BasMonitorDisplay>>();
		List<BasMonitorDisplay> mds = null;
		if (null != monitorList && monitorList.size() > 0) {
			for (BasMonitorDisplay md : monitorList) {
				Date time = md.getTime();
				if (!map.containsKey(time)) {
					mds = new ArrayList<BasMonitorDisplay>();
					mds.add(md);
					map.put(time, mds);
				} else {
					mds = map.get(time);
					//前端为了展示需要，需要将O2放在第一个位置
					if (MyConstants.O2_EVENT_ID.equals(md.getObserveId())) 
					{
					    mds.add(0, md);
					}
					else
					{
					    mds.add(md);
					}
					map.put(time, mds);
				}
			}
		} else {
			// 无采集数据
			logger.info("----------无采集数据----------------");
		}

		List<MonDataFormBean> monDataList = new ArrayList<MonDataFormBean>();
		MonDataFormBean monData = null;
		MonitorData monitorData = null;

		if (!map.isEmpty() && map.size() > 0) {
			// int i = 0;
			int index = 0;
			// 循环seriesMap中的数据
			for (Date key : map.keySet()) {
				// if(i%3==0){
				monData = new MonDataFormBean();
				List<MonitorData> monitorDataList = new ArrayList<MonitorData>();
				List<BasMonitorDisplay> list = map.get(key);
				monData.setTime(key);
				monData.setIndex(index++);
				if (null != list && list.size() > 0) {
					for (BasMonitorDisplay md : list) {
						Integer freq = md.getFreq();
						if (null != freq) {
							monData.setFreq(freq);
						}
						monitorData = new MonitorData();
						BeanUtils.copyProperties(md, monitorData);
						monitorDataList.add(monitorData);
					}
				}
				monData.setMonitorDataList(monitorDataList);
				monDataList.add(monData);
				// }
				// i++;
			}
		}

		// 获取总数
		int total = basMonitorDisplayService.getobsDataTotal(formBean, observeIds);

		res.setResultCode("1");
		res.setResultMessage("查询监测项数据成功！");
		res.put("total", total);
		res.put("monDataList", monDataList);
		logger.info("----------------end getmonData------------------------");
		return res.getJsonStr();
	}
	
	
	
	/**
     * 开始手术，术前转术中 ，发送采集数据模块，并发送采集服务
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/startOper")
    @ResponseBody
    public String startOper(@RequestBody SearchFormBean searchBean){
        logger.info("----------------start startOper------------------------");
        
        Map result = new HashMap();
        
        try {
        	
            if (StringUtils.isBlank(searchBean.getBeid()))
            {
                searchBean.setBeid(getBeid());
            }
            
        	String regOptId = searchBean.getRegOptId();
        	
            //麻醉记录单
            DocAnaesRecord anaesRecord = docAnaesRecordService.searchAnaesRecordByRegOptId(regOptId);
            //设置文书ID
            searchBean.setDocId(anaesRecord.getAnaRecordId());
            // 手术信息表
            BasRegOpt opt = basRegOptService.searchRegOptById(regOptId);
            
            //只有手术从术前到术中时，才需要将手术排程的麻醉医生、麻醉方法等数据插入到事件表
            boolean flagInsert = false;
            
            //AccessSource有值时代表是术中模块进入 ,为空值时则表示术后查看麻醉记录单
            if(StringUtils.isNotBlank(searchBean.getAccessSource())){
                //如果已经是术后or中止状态，则直接返回给前端
                if(("06").equals(opt.getState())){ //术后状态
                    result.put("resultCode", "40000004");
                    result.put("resultMessage", "当前患者("+opt.getName()+")的手术已结束!");
                    logger.info("------------------end startOper------------------------");
                    return JsonType.jsonType(result);
                }else if("07".equals(opt.getState())){
                    result.put("resultCode", "40000004");
                    result.put("resultMessage", "当前患者("+opt.getName()+")的手术已中止!");
                    logger.info("------------------end startOper------------------------");
                    return JsonType.jsonType(result);
                }
            }
            
            Integer docType = searchBean.getDocType();
            try {
            	if(Objects.equals(2, docType)){
            		//复苏记录单显示麻醉记录单中的麻醉事件
            		SearchFormBean searchPo = new SearchFormBean();
            		BeanHelper.copyProperties(searchBean, searchPo);
            		searchPo.setDocType(1);
                    List<EvtAnaesEvent> eventListPlus = evtAnaesEventService.searchAnaeseventList(searchPo);
                    result.put("eventListPlus", eventListPlus);   
                	myOperationDataService.getPacuStartOperDate(searchBean, result); 
                }else{
                	myOperationDataService.getStartOperDate(searchBean, result,flagInsert);
                }
			} catch (Exception e) {
				return JsonType.jsonType(result);
			}
            
            // 麻醉事件
            List<EvtAnaesEvent> eventList = evtAnaesEventService.searchAnaeseventList(searchBean);
            
            // ==============================================术中描点监测项处理========================================================
            // 术中监测显示项
            BaseInfoQuery baseQuery = new BaseInfoQuery();
            baseQuery.setRegOptId(searchBean.getRegOptId());
            baseQuery.setDocType(docType);
            baseQuery.setPosition("0");
            
            UpdAnaesMonitorConfigFormbean formbean = new UpdAnaesMonitorConfigFormbean();
            formbean.setRegOptId(regOptId);
            formbean.setDocType(docType);
            
            // 查询出默认配置展示的监测项，即bas_monitor_config表中的mustShow为1
            List<BasAnaesMonitorConfigFormBean> showList = basAnaesMonitorConfigService.getAnaesRecordShowListByRegOptId(baseQuery);
            
            // 查询出需要在页面上面展示的监测项，及保存在bas_anaes_monitor_config表中的监测项
            List<BasAnaesMonitorConfigFormBean> anaesMonitorConfigFormBeanList = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
            
            // 防止出现首次进入术中时没有设备启用，但是后来追加设备启用但是设备并不是默认设备的情况，及时将b_anaes_monitor_config中重复监测项的realEventId更新为启用设备的eventId
            List<BasMonitorConfigDefault> monitorConfigDefaultList = basMonitorConfigDefaultService.selectAll(docType);
            if (null != monitorConfigDefaultList && monitorConfigDefaultList.size() > 0)
            {
                for (BasMonitorConfigDefault monitorConfigDefault : monitorConfigDefaultList)
                {
                    if (null == anaesMonitorConfigFormBeanList || anaesMonitorConfigFormBeanList.size() < 1)
                    {
                        break;
                    }
                    for (BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean : anaesMonitorConfigFormBeanList)
                    {
                        if (monitorConfigDefault.getEventId().equals(anaesMonitorConfigFormBean.getEventId()))
                        {
                            String enableEventId =
                                basMonitorConfigService.selectEventIdByEventName(anaesMonitorConfigFormBean,
                                    searchBean.getRegOptId(),
                                    docType);
                            anaesMonitorConfigFormBean.setRealEventId(enableEventId);
                            BasAnaesMonitorConfig amc = new BasAnaesMonitorConfig();
                            BeanUtils.copyProperties(anaesMonitorConfigFormBean, amc);
                            amc.setRegOptId(searchBean.getRegOptId());
                            basAnaesMonitorConfigService.updateByEventId(amc);
                        }
                    }
                    
                }
            }
            result.put("showList", anaesMonitorConfigFormBeanList);
            
            // 如果anaesMonitorConfigFormBeanList为空，则说明是第一次进入手术室或者是还没有配置任何需要展示在页面的监测项，这时需要将默认需要展示的监测项添加到bas_anaes_monitor_config表中
            if ((null == anaesMonitorConfigFormBeanList || anaesMonitorConfigFormBeanList.size() <= 0)
                && null != showList && showList.size() > 0)
            {
                List<BasAnaesMonitorConfig> anaesMonitorConfigList = new ArrayList<BasAnaesMonitorConfig>();
                for (BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean : showList)
                {
                    BasAnaesMonitorConfig anaesMonitorConfig = new BasAnaesMonitorConfig();
                    BeanUtils.copyProperties(anaesMonitorConfigFormBean, anaesMonitorConfig);
                    anaesMonitorConfig.setRegOptId(searchBean.getRegOptId());
                    anaesMonitorConfig.setDocType(docType);
                    
                    BasMonitorConfigDefault monitorConfigDefault =
                        basMonitorConfigDefaultService.searchByEventName(anaesMonitorConfigFormBean.getEventName(),
                            docType);
                    if (null != monitorConfigDefault)
                    {
                        // 获取到启动设备对应的eventId，如果都没启动，则取默认值
                        String enableEventId =
                            basMonitorConfigService.selectEventIdByEventName(anaesMonitorConfigFormBean,
                                searchBean.getRegOptId(),
                                docType);
                        if (enableEventId.equals(anaesMonitorConfig.getEventId()))
                        {
                            anaesMonitorConfigList.add(anaesMonitorConfig);
                        }
                    }
                    else
                    {
                        anaesMonitorConfigList.add(anaesMonitorConfig);
                    }
                }
                formbean.setCheckList(anaesMonitorConfigList);
                basAnaesMonitorConfigService.saveAnaesMonitorConfig(formbean);
                result.put("showList", anaesMonitorConfigList);
            }
            // ==============================================术中描点监测项处理========================================================
            
            
            // ==============================================术中数字监测项处理========================================================
            baseQuery.setPosition("1");
            
            // 查询出默认配置展示的监测项，即bas_monitor_config表中的mustShow为1
            List<BasAnaesMonitorConfigFormBean> leftShowList = basAnaesMonitorConfigService.getAnaesRecordShowListByRegOptId(baseQuery);
            
            // 查询出需要在页面上面展示的监测项，及保存在bas_anaes_monitor_config表中的监测项
            anaesMonitorConfigFormBeanList = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
            
            if (null != anaesMonitorConfigFormBeanList && anaesMonitorConfigFormBeanList.size() > 0)
            {
                // 返回给前端的数字监测项中，不能包含O2,去除O2的监测项
                for (int i = 0; i < anaesMonitorConfigFormBeanList.size(); i++)
                {
                    BasAnaesMonitorConfigFormBean mc = anaesMonitorConfigFormBeanList.get(i);
                    if (mc.getEventId().equals(MyConstants.O2_EVENT_ID))
                    {
                        anaesMonitorConfigFormBeanList.remove(mc);
                        break;
                    }
                }
            }
            else
            {
                //因为O2为必定展示的数字监测项，所以如果anaesMonitorConfigFormBeanList为空则说明是第一次进入麻醉记录单，则需要添加O2到bas_anaes_monitor_config中
                List<BasAnaesMonitorConfig> anaesMonitorConfigList = new ArrayList<BasAnaesMonitorConfig>();
                BasAnaesMonitorConfig anaesMonitorConfig = new BasAnaesMonitorConfig();
                BasMonitorConfig O2 = basMonitorConfigService.selectByEventId(MyConstants.O2_EVENT_ID);
                BeanUtils.copyProperties(O2, anaesMonitorConfig);
                anaesMonitorConfig.setRegOptId(searchBean.getRegOptId());
                anaesMonitorConfig.setEventId(MyConstants.O2_EVENT_ID);
                anaesMonitorConfig.setDocType(docType);
                anaesMonitorConfigList.add(anaesMonitorConfig);
                formbean.setCheckList(anaesMonitorConfigList);
                basAnaesMonitorConfigService.updAnaesMonitorConfig(formbean);
            }
            
            result.put("leftShowList", anaesMonitorConfigFormBeanList);
            if ((null == anaesMonitorConfigFormBeanList || anaesMonitorConfigFormBeanList.size() <=  0))
            {
                if (null != leftShowList && leftShowList.size() > 0)
                {
                    List<BasAnaesMonitorConfig> anaesMonitorConfigList = new ArrayList<BasAnaesMonitorConfig>();
                    for (BasAnaesMonitorConfigFormBean anaesMonitorConfigFormBean : leftShowList)
                    {
                        BasAnaesMonitorConfig anaesMonitorConfig = new BasAnaesMonitorConfig();
                        BeanUtils.copyProperties(anaesMonitorConfigFormBean, anaesMonitorConfig);
                        anaesMonitorConfig.setRegOptId(searchBean.getRegOptId());
                        anaesMonitorConfig.setDocType(docType);
                        anaesMonitorConfigList.add(anaesMonitorConfig);
                    }
                    formbean.setCheckList(anaesMonitorConfigList);
                    basAnaesMonitorConfigService.updAnaesMonitorConfig(formbean);
                }
                result.put("leftShowList", leftShowList);
            }
            // ==============================================术中数字监测项处理========================================================
            
            
            AnaesRecordFormBean anaesRecordFormBean = new AnaesRecordFormBean();
            BeanUtils.copyProperties(anaesRecord, anaesRecordFormBean);
            anaesRecordFormBean.setOptBodys(StringUtils.getListByString(anaesRecordFormBean.getOptBody()));
            
            // 实际麻醉方法
            List<EvtAnaesMethodFormBean> realAnaesList = evtRealAnaesMethodService.getSelectRealAnaesMethodList(searchBean);
            // 术后诊断
            List<DiagnosedefFormBean> optLatterDiagList = evtOptLatterDiagService.getSelectOptLatterDiagList(searchBean);
            // 实施手术
            List<OperDefFormBean> optRealOperList = evtOptRealOperService.getSelectOptRealOperList(searchBean);
            //麻醉医生列表
            searchBean.setRole(EvtParticipantService.ROLE_ANESTH);
            List<UserInfoFormBean> anesDocList = evtParticipantService.getSelectParticipantList(searchBean);
            
            //手术医生列表
            searchBean.setRole(EvtParticipantService.ROLE_OPERAT);
            List<UserInfoFormBean> operatDocList = evtParticipantService.getSelectParticipantList(searchBean);
            
            //巡回护士列表
            searchBean.setRole(EvtParticipantService.ROLE_NURSE);
            searchBean.setType(EvtParticipantService.OPER_TYPE_TOUR);//05
            List<UserInfoFormBean> nurseList = evtParticipantService.getSelectParticipantList(searchBean);
            
            // 器械护士列表
            searchBean.setRole(EvtParticipantService.ROLE_NURSE);
            searchBean.setType(EvtParticipantService.OPER_TYPE_INSTRUMENT);//04
            List<UserInfoFormBean> instruNurseList = evtParticipantService.getSelectParticipantList(searchBean);
            
            // 镇痛药事件明细   用药 
            searchBean.setType("03");
            List<RegOptOperMedicaleventFormBean> analgesicMedEvtList = evtMedicaleventService.searchMedicaleventGroupByCodeList(searchBean);
            
            //手术体位变更
            List<OperBodyFormBean> operBodyList = evtOperBodyEventService.queryOperBodyEventList(searchBean);
            
            // 心电图数据
            List<EvtElectDiogData> diogDataList = evtElectDiogDataService.searchElectDiogDataList(searchBean);
            
            //安全核查单
            DocAnaesBeforeSafeCheck anaesBeforeSafeCheck = docAnaesBeforeSafeCheckService.searchAnaBeCheckByRegOptId(opt.getRegOptId());
            DocExitOperSafeCheck exitOperSafeCheck = docExitOperSafeCheckService.searchExitOperCheckByRegOptId(opt.getRegOptId());
            DocOperBeforeSafeCheck operBeforeSafeCheck = docOperBeforeSafeCheckService.searchOperBeCheckByRegOptId(opt.getRegOptId());
            
            //O2L/MIN等数据
            //List<EvtOtherData> dataList = evtOtherDataService.selectOtherDataByObserveName(anaesRecord.getOther(), anaesRecord.getAnaRecordId());
            
            opt = basRegOptService.searchRegOptById(regOptId);
            result.put("resultCode", "1");
            result.put("resultMessage", "查询麻醉记录单成功!!!");
            result.put("regOpt", opt);
            result.put("anaesBeforeSafeCheck", null != anaesBeforeSafeCheck ? anaesBeforeSafeCheck.getProcessState() : null);
            result.put("exitOperSafeCheck", null != exitOperSafeCheck ? exitOperSafeCheck.getProcessState() : null);
            result.put("operBeforeSafeCheck", null != operBeforeSafeCheck ? operBeforeSafeCheck.getProcessState() : null);
            result.put("anaesRecord", anaesRecordFormBean);
            //复苏单不显示镇痛用药
            if(Objects.equals(1, docType)){
            	result.put("analgesicMedEvtList", analgesicMedEvtList);
            }
            result.put("realAnaesList", realAnaesList);
            result.put("optLatterDiagList", optLatterDiagList);
            result.put("optRealOperList", optRealOperList);
            result.put("anesDocList", anesDocList);
            result.put("operatDocList", operatDocList);
            result.put("nurseList", nurseList);
            result.put("instruNurseList", instruNurseList);
            result.put("operBodyList", operBodyList);
            result.put("diogDataList", diogDataList);
            result.put("eventList", eventList);   
            logger.info("startOper---result-------"+JsonType.jsonType(result));
            logger.info("------------------end startOper------------------------");
            return JsonType.jsonType(result);
        } catch (Exception e) {
            logger.error("startOper====="+Exceptions.getStackTraceAsString(e));
            result.put("resultCode", "10000000");
            result.put("resultMessage", "系统错误，请与系统管理员联系!");
        }
        logger.info("------------------end startOper------------------------");
        return JsonType.jsonType(result);
    }

    /**
	 * 修改入室时间 时间往前修改，时间往后修改 往后修改，时间高于手术开始命令时间，不做数据处理操作
	 * 往前修改，时间低于手术开始命令时间，先删除原有的不对的数据，然后再添加以频率（第一个点的频率）算好的对应的点 入室事件
	 * 需要记录到s_anaesevent,并将时间存入d_anaesrecord 麻醉记录单中
	 * 
	 * @return
	 */
	@RequestMapping("/updateEnterRoomTime")
	@ResponseBody
	@ApiOperation(value = "修改入PACU室时间", httpMethod = "POST", notes = "修改入室时间")
	public String updateEnterRoomTime(@ApiParam(name = "params", value = "参数") @RequestBody EnterRoomFormBean formBean) {
		// 1、从b_reg_opt表中获取发起手术命令时间
		// 2、比对传入的入室时间和发起手术命令时间的大小 分支：2.1：入室时间大于手术时间，则不修改，删除无用数据，记录事件，并修改入室时间；
		// 2.2：入室时间小于手术时间，删除无用数据，根据当前手术时间往前推freq的值，存入b_monitor_display表中，记录事件，并修改入室时间；
		// 3、根据手术命令时间，查询当前手术的点和freq
		logger.info("----------------start updateEnterRoomTimeBase------------------------");
		ResponseValue res = new ResponseValue();
		String regOptId = formBean.getRegOptId();
		Date inTime = formBean.getInTime();
		String docId = formBean.getDocId();
		if (StringUtils.isNotBlank(regOptId) && inTime != null && StringUtils.isNotBlank(docId)) {

			BasRegOpt regOpt = basRegOptService.searchRegOptById(regOptId);
			if (null == regOpt) {
				logger.info("updatePacuEnterRoomTime----regOpt手术为null！--------------");
				res.setResultCode("10000000");
				res.setResultMessage("查询数据有误，请与系统管理员联系!");
			} else {
				if(Objects.equals(2, formBean.getDocType())){
					DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecService.getAnaesPacuRecByRegOptId(regOptId);
					Date operTime = anaesPacuRec.getOperTime();
					if (null == operTime) {
						logger.info("updatePacuEnterRoomTime----operTime手术时间为null！--------------");
						res.setResultCode("10000000");
						res.setResultMessage("查询数据有误，请与系统管理员联系!");
					} else {
						long inTimeLong = inTime.getTime();
						long operTimeLong = operTime.getTime();
						
						formBean.setOperTime(operTime);
						if (inTimeLong > operTimeLong) { // 入室时间大于手术命令开始时间
							// 1、根据手术命令时间，删除没有用的数据
							// 2、记录事件，并修改入室时间
							basMonitorDisplayService.updatePacuEnterRoomTimegt(formBean, res);
						} else if (inTimeLong < operTimeLong) {// 入室时间小于手术命令开始时间
							// 1、根据手术命令时间，删除没有用的数据
							// 2、根据当前手术时间往前推freq的值，存入b_monitor_display表中
							// 3、记录事件，并修改入室时间
							basMonitorDisplayService.updPacuEnterRoomTimelt(formBean, res);
						} else { // = 不处理

						}
					}
					docAnaesPacuRecService.updatePacuRecEnterRoomTime(inTime, anaesPacuRec.getId()); 
				}else{
					Date operTime = regOpt.getOperTime();
					if (null == operTime) {
						logger.info("updateEnterRoomTime----operTime手术时间为null！--------------");
						res.setResultCode("10000000");
						res.setResultMessage("查询数据有误，请与系统管理员联系!");
					} else {
						long inTimeLong = inTime.getTime();
						long operTimeLong = operTime.getTime();
						
						formBean.setOperTime(operTime);
						if (inTimeLong > operTimeLong) { // 入室时间大于手术命令开始时间
							// 1、根据手术命令时间，删除没有用的数据
							// 2、记录事件，并修改入室时间
							basMonitorDisplayService.updateEnterRoomTimegt(formBean, res);
						} else if (inTimeLong < operTimeLong) {// 入室时间小于手术命令开始时间
							// 1、根据手术命令时间，删除没有用的数据
							// 2、根据当前手术时间往前推freq的值，存入b_monitor_display表中
							// 3、记录事件，并修改入室时间
							basMonitorDisplayService.updEnterRoomTimelt(formBean, res);
						} else { // = 不处理

						}
					}
					docAnaesRecordService.updateAnaesInRoomTime(SysUtil.getTimeFormat(inTime), regOptId); 
				}
			}
			// 根据docId查询对应的事件id
			SearchFormBean searchBean = new SearchFormBean();
			searchBean.setDocId(docId);
			searchBean.setDocType(formBean.getDocType());
			List<EvtAnaesEvent> resultList = evtAnaesEventService.searchAnaeseventList(searchBean);
			res.put("eventList", resultList);
		} else {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
		}

		logger.info("----------------end updateEnterRoomTimeBase------------------------");
		return res.getJsonStr();
	}

	/**
     * 修改出室时间
     */
    @RequestMapping("/updateOuterTime")
    @ResponseBody
    public String updateOuterTime(@RequestBody EndOperationFormBean formBean){
        logger.info("----------------start updateOuterTimeBase------------------------");
        ResponseValue res = new ResponseValue();
        EvtAnaesEvent anaesevent = formBean.getAnaesevent();
        if (anaesevent != null)
        {
        	if(java.util.Objects.equals(2, formBean.getDocType())){
        		basMonitorDisplayService.updatePacuEndTime(formBean, res);
			}else{
				basMonitorDisplayService.updateEndTime(formBean,res);
			}
        	
            SearchFormBean searchBean = new SearchFormBean();
            searchBean.setDocId(anaesevent.getDocId());
            List<EvtAnaesEvent> resultList = new ArrayList<EvtAnaesEvent>();
            resultList = evtAnaesEventService.searchAnaeseventList(searchBean);
            res.put("result", true);
            res.put("eventList", resultList);
        }
        else
        {
            res.setResultCode("70000000");
            res.setResultMessage(Global.getRetMsg(res.getResultCode()));
        }
        logger.info("------------------end updateOuterTimeBase------------------------");
        return res.getJsonStr();
    }
	
	/**
	 * 1、第一次进入手术室的时候，存入手术命令开始时间，存入入室事件，存入开始手术时间 2、存入第一个数据点 3、返回麻醉时间list
	 */
	@RequestMapping("/firstSpot")
	@ResponseBody
	@ApiOperation(value = "首次进入手术", httpMethod = "POST", notes = "首次进入手术")
	public String firstSpot(@RequestBody FirstSpotFormBean formBean) {
		logger.info("----------------start firstSpot------------------------");
		ResponseValue res = new ResponseValue(); 
		if (null != formBean) {
			
			if(Objects.equals(2, formBean.getDocType())){
				basMonitorDisplayService.firstSpotPacu(formBean);
			}else{
				basMonitorDisplayService.firstSpot(formBean);
			}
			// 根据docId查询对应的事件id
			SearchFormBean searchBean = new SearchFormBean();
			searchBean.setDocId(formBean.getDocId());
			searchBean.setDocType(formBean.getDocType());
			List<EvtAnaesEvent> resultList = new ArrayList<EvtAnaesEvent>();
			resultList = evtAnaesEventService.searchAnaeseventList(searchBean);
			res.put("eventList", resultList);
		} else {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
		}
		logger.info("----------------end firstSpot------------------------");
		return res.getJsonStr();
	}
	
	
	/**
	 * 
	 * 将第一个点的时候往整300秒靠，且不允许修改显示间隔
	 * 1、第一次进入手术室的时候，存入手术命令开始时间，存入入室事件，存入开始手术时间 2、存入第一个数据点 3、返回麻醉时间list
	 */
	@RequestMapping("/firstSpotWholePoint")
	@ResponseBody
	@ApiOperation(value = "首次进入手术", httpMethod = "POST", notes = "首次进入手术")
	public String firstSpotWholePoint(@RequestBody FirstSpotFormBean formBean) {
		logger.info("----------------start firstSpotWholePoint------------------------");
		ResponseValue res = new ResponseValue(); 
		if (null != formBean) {
			
			if(Objects.equals(2, formBean.getDocType())){
				basMonitorDisplayService.firstSpotPacu(formBean);
			}else{
				basMonitorDisplayService.firstSpotWholePoint(formBean);
			}
			// 根据docId查询对应的事件id
			SearchFormBean searchBean = new SearchFormBean();
			searchBean.setDocId(formBean.getDocId());
			searchBean.setDocType(formBean.getDocType());
			List<EvtAnaesEvent> resultList = new ArrayList<EvtAnaesEvent>();
			resultList = evtAnaesEventService.searchAnaeseventList(searchBean);
			res.put("eventList", resultList);
		} else {
			res.setResultCode("70000000");
			res.setResultMessage(Global.getRetMsg(res.getResultCode()));
		}
		logger.info("----------------end firstSpotWholePoint------------------------");
		return res.getJsonStr();
	}
	
	
	
	/**
     * 正常结束手术，发送采集数据模块，并发送采集服务(黔南州中医院定制)
     * 
     * @return
     */
    @RequestMapping("/endOperation")
    @ResponseBody
    @ApiOperation(value = "正常结束手术", httpMethod = "POST", notes = "正常结束手术")
    public String endOperation(@ApiParam(name = "params", value = "参数") @RequestBody EndOperationFormBean formBean) {
        logger.info("----------------start endOperation------------------------");
        ResponseValue res = new ResponseValue();
        EvtAnaesEvent anaesevent = formBean.getAnaesevent();
        logger.info("endOperation----" + anaesevent);
        if (anaesevent != null) {
            List<EvtAnaesEvent> resultList = new ArrayList<EvtAnaesEvent>();
            SearchFormBean searchBean = new SearchFormBean();
            searchBean.setDocId(anaesevent.getDocId());
        	if(Objects.equals(2, formBean.getDocType())){
        		 basMonitorDisplayService.upDatePacuEndTime(formBean, res);
        		 if (res.getResultCode().equals("1")) { // 只有成功了，才执行正常结束手术命令
                    resultList = evtAnaesEventService.searchAnaeseventList(searchBean);
                    /*DocAnaesPacuRec anaesPacuRec = docAnaesPacuRecService.getAnaesPacuRecByRegOptId(regOptId);
                    if(null != anaesPacuRec){
	                    CmdMsg msg = new CmdMsg();
	         			msg.setMsgType(com.digihealth.anesthesia.pacu.core.MyConstants.STATUS_END);
	         			msg.setBedId(anaesPacuRec.getBedId());
	         			msg.setRegOptId(regOptId);
	         			MsgProcess.process(msg);
                    }*/
                 }
        	}else{
        		 basMonitorDisplayService.updateEndTime(formBean,res);
        		 if (res.getResultCode().equals("1")) { // 只有成功了，才执行正常结束手术命令
                     resultList = evtAnaesEventService.searchAnaeseventList(searchBean);
                    /* CmdMsg msg = new CmdMsg();
                     msg.setMsgType(MyConstants.OPERATION_STATUS_END);
                     msg.setRegOptId(regOptId);
                     res = MessageProcess.process(msg);
                     res.put("eventList", resultList);*/
                 }
        	}
        	 res.put("eventList", resultList);
        } else {
            res.setResultCode("70000000");
            res.setResultMessage(Global.getRetMsg(res.getResultCode()));
        }
        logger.info("------------------end endOperation------------------------");
        return res.getJsonStr();
    }
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/getobsWave")
	@ResponseBody
	@ApiOperation(value = "波形图数据查询", httpMethod = "POST", notes = "波形图数据查询")
	public Map getobsWave(@ApiParam(name = "params", value = "参数") @RequestBody MonitorDataFormBean formBean) {
		logger.info("----------------start getobsWave------------------------");
		Map map = new HashMap();
		LegendData legend = new LegendData();
		List<String> legendData = new ArrayList<String>();

		List<XAxisData1> xAxis = new ArrayList<XAxisData1>();
		XAxisData1 xaisData = null;
		List<XAxisDataBean> data = new ArrayList<XAxisDataBean>();

		List<SeriesWave> series = new ArrayList<SeriesWave>();
		SeriesWave seriesdata = null;
		List<SeriesDataObj> da = null;
		SeriesDataObj obj = null;
		Map<String, SeriesWave> seriesMap = new HashMap<String, SeriesWave>();

		// 获取需要显示的数据
		String regOptId = formBean.getRegOptId();
		Integer position = 0;
		String beid = formBean.getBeid();
		Integer docType = formBean.getDocType();
		if (StringUtils.isBlank(beid))
		{
		    beid = getBeid();
		    formBean.setBeid(beid);
		}

		BaseInfoQuery baseQuery = new BaseInfoQuery();
		baseQuery.setRegOptId("1000001");
		baseQuery.setPosition(position + "");
		baseQuery.setEnable("1");
		baseQuery.setDocType(docType);
		baseQuery.setBeid("209");
		List<BasAnaesMonitorConfigFormBean> anaesLists = basAnaesMonitorConfigService.finaAnaesList(baseQuery);
		
		List<String> observeIds = new ArrayList<String>();

		if (null != anaesLists && anaesLists.size() > 0) {
			for (BasAnaesMonitorConfigFormBean bean : anaesLists) {
				String observeId = bean.getEventId();
				// 获取显示项需要的observeIds
				observeIds.add(observeId);
				String observeName = bean.getEventName();
				String color = bean.getColor();// 对应图标
				String icon = bean.getIconId();// 对应颜色
//				String svg = basIconSvgService.getPathByIcon(bean.getIconId(), beid);
				String svg = "";
				String units = bean.getUnits(); // 默认单位
				Float max = bean.getMax(); // max
				Float min = bean.getMin(); // min
				String secondData = "";
				List<String> observeNames = new ArrayList<String>();
				if ("RESP".equals(observeName)) {
					observeNames.add("awRR");
				}else if ("CO2".equals(observeName)) {
					observeNames.add("FiCO2");
					observeNames.add("EtCO2");
				}else if ("ART".equals(observeName)) {
					observeNames.add("ART_DIA");
					observeNames.add("ART_SYS");
				}else {
					observeNames.add(observeName);
				}
				List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsSeconds(formBean, observeNames);
				for (BasMonitorDisplay basMonitorDisplay : monitorList) {
					if (StringUtils.isBlank(secondData)) {
						secondData = String.valueOf(basMonitorDisplay.getValue());
					}else {
						secondData += "/" + String.valueOf(basMonitorDisplay.getValue());
					}
				}
				legendData.add(observeName);
				seriesMap.put(observeId, new SeriesWave(observeId, observeName, "line", new ArrayList<SeriesDataObj>(), secondData, icon, svg, 0, MyConstants.SYMBOL_OBSDATA, color, units, max, min));
			}
		}

		List<BasMonitorDisplay> monitorList = basMonitorDisplayService.getobsWave(formBean, observeIds);

		Date t = new Date(1L);
		if (null != monitorList && monitorList.size() > 0) {
			for (int i = 0; i < monitorList.size(); i++) {
				BasMonitorDisplay md = monitorList.get(i);
				String key = md.getObserveId();
				Date time = md.getTime();

				if (t.getTime() != time.getTime()) {
					t = time;
					XAxisDataBean bean = new XAxisDataBean();
					bean.setValue(t);
					Integer freq = md.getIntervalTime();
					bean.setFreq(freq);
					data.add(bean);
				}
				// series
				if (!seriesMap.containsKey(key)) {
					logger.info("------------------没有当前key" + key + "------------------------");
				} else {
					seriesdata = seriesMap.get(key);
					da = seriesdata.getData();
					seriesdata.setType("line");
//					seriesdata.setName(md.getObserveName());

					// 设置指定对应的y轴
					seriesdata.setyAxisIndex(0);
					obj = new SeriesDataObj(md.getId(), md.getValue(), md.getTime(), md.getObserveId());
					da.add(obj);
					seriesdata.setData(da);
					seriesMap.put(key, seriesdata);
				}
			}
		} else {
			// 无采集数据
			logger.info("----------无采集数据----------------");
		}

		// 循环seriesMap中的数据
		for (String key : seriesMap.keySet()) {
			SeriesWave sd = seriesMap.get(key);
			series.add(sd);
		}

		// 添加times到x轴
		xaisData = new XAxisData1();
		xaisData.setData(data);
		xAxis.add(xaisData);
		map.put("series", series);
		// 有显示项 则增加legend数据，传递给前端
		legend.setData(legendData);

		map.put("resultCode", "1");
		map.put("resultMessage", "操作成功！");

//		logger.info("getobsData---allData===" + JsonType.jsonType(map) + ",inTime==" + formBean.getInTime());
		logger.info("------------------end getobsWave------------------------");
		return map;
	}
}
