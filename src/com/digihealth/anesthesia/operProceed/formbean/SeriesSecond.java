package com.digihealth.anesthesia.operProceed.formbean;

public class SeriesSecond {
	private String observeId;//监测项id 
	private String name; //监测项名称
	private String data; //数据点

	public SeriesSecond() {
		super();
	}

	public SeriesSecond(String data) {
		super();
		this.data = data;
	}

	public SeriesSecond(String name, String type, String data, String symbol, Integer yAxisIndex, Integer symbolSize) {
		super();
		this.name = name;
		this.data = data;
	}

	public SeriesSecond(String name, String type, String data, String symbol, Integer yAxisIndex, Integer symbolSize, String color) {
		super();
		this.name = name;
		this.data = data;
	}

	public SeriesSecond(String name, String type, String data, String symbol, Integer yAxisIndex, Integer symbolSize, String color, String units) {
		super();
		this.name = name;
		this.data = data;
	}

	public SeriesSecond(String observeId, String name, String type, String data, String symbol, Integer yAxisIndex, Integer symbolSize, String color, String units) {
		super();
		this.observeId = observeId;
		this.name = name;
		this.data = data;
	}

	public SeriesSecond(String observeId, String name, String type, String data, String symbol, Integer yAxisIndex, Integer symbolSize, String color, String units, Float max, Float min) {
		super();
		this.observeId = observeId;
		this.name = name;
		this.data = data;
	}
	
	public SeriesSecond(String observeId, String name, String type, String data, String symbol, String symbolSvg, Integer yAxisIndex, Integer symbolSize, String color, String units, Float max, Float min) {
		super();
		this.observeId = observeId;
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getObserveId() {
		return observeId;
	}

	public void setObserveId(String observeId) {
		this.observeId = observeId;
	}

}
