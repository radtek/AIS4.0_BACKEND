package com.digihealth.anesthesia.operProceed.formbean;

import java.util.List;

public class SeriesWave {
	private String observeId;//监测项id 
	private String name; //监测项名称
	private String type; // line 
	private List<SeriesDataObj> data; //数据点
	private String secondData;
	private Integer yAxisIndex; //对应哪个y轴
	private String color;//颜色
	private String units;//单位
	private Float max; //当前监测点的最大值
	private Float min; //当前监测点的最小值

	public SeriesWave() {
		super();
	}

	public SeriesWave(List<SeriesDataObj> data) {
		super();
		this.data = data;
	}

	public SeriesWave(String name, String type, List<SeriesDataObj> data, String symbol, Integer yAxisIndex, Integer symbolSize) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
		this.yAxisIndex = yAxisIndex;
	}

	public SeriesWave(String name, String type, List<SeriesDataObj> data, String symbol, Integer yAxisIndex, Integer symbolSize, String color) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
		this.yAxisIndex = yAxisIndex;
		this.color = color;
	}

	public SeriesWave(String name, String type, List<SeriesDataObj> data, String symbol, Integer yAxisIndex, Integer symbolSize, String color, String units) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
		this.yAxisIndex = yAxisIndex;
		this.color = color;
		this.units = units;
	}

	public SeriesWave(String observeId, String name, String type, List<SeriesDataObj> data, String symbol, Integer yAxisIndex, Integer symbolSize, String color, String units) {
		super();
		this.observeId = observeId;
		this.name = name;
		this.type = type;
		this.data = data;
		this.yAxisIndex = yAxisIndex;
		this.color = color;
		this.units = units;
	}

	public SeriesWave(String observeId, String name, String type, List<SeriesDataObj> data, String symbol, Integer yAxisIndex, Integer symbolSize, String color, String units, Float max, Float min) {
		super();
		this.observeId = observeId;
		this.name = name;
		this.type = type;
		this.data = data;
		this.yAxisIndex = yAxisIndex;
		this.color = color;
		this.units = units;
		this.max = max;
		this.min = min;
	}
	
	public SeriesWave(String observeId, String name, String type, List<SeriesDataObj> data, String secondData, String symbol, String symbolSvg, Integer yAxisIndex, Integer symbolSize, String color, String units, Float max, Float min) {
		super();
		this.observeId = observeId;
		this.name = name;
		this.type = type;
		this.data = data;
		this.secondData = secondData;
		this.yAxisIndex = yAxisIndex;
		this.color = color;
		this.units = units;
		this.max = max;
		this.min = min;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SeriesDataObj> getData() {
		return data;
	}

	public void setData(List<SeriesDataObj> data) {
		this.data = data;
	}

	public String getSecondData() {
		return secondData;
	}

	public void setSecondData(String secondData) {
		this.secondData = secondData;
	}

	public Integer getyAxisIndex() {
		return yAxisIndex;
	}

	public void setyAxisIndex(Integer yAxisIndex) {
		this.yAxisIndex = yAxisIndex;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getObserveId() {
		return observeId;
	}

	public void setObserveId(String observeId) {
		this.observeId = observeId;
	}

	public Float getMax() {
		return max;
	}

	public void setMax(Float max) {
		this.max = max;
	}

	public Float getMin() {
		return min;
	}

	public void setMin(Float min) {
		this.min = min;
	}

}
