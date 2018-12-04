package com.digihealth.anesthesia.operProceed.core;

public interface MyConstants {
    
    
    public static String DATA_SYNC_WORKSTATION_MAINQUEUE = "Slave2Master";
    
    public static String DATA_SYNC_WORKSTATION_MASTER = "master";
    
    public static String DATA_SYNC_WORKSTATION_SLAVE = "slave";
    
    /**
     * 手术模式 普通模式
     */
    public static String OPERATION_MODEL_NORMAL = "normal";
    
    /**
     * 手术模式 抢救模式
     */
    public static String OPERATION_MODEL_SAVE = "save";
    
    /**
     * 氧流量
     */
    public static String O2_EVENT_ID = "50021"; 
    
    /**
     * ECG
     */
    public static String ECG = "91003"; 
    
    
    /**
     * 呼吸率
     */
    public static String RR_EVENT_ID = "100001";
    
    /**
     * 体温
     */
    public static String TEMP_EVENT_ID = "30021";
    
    /**
     * 中心静脉收缩压
     */
    public static String CVP_SYS_EVENT_ID = "30061";
    
    /**
     * 中心静脉舒张压
     */
    public static String CVP_DIA_EVENT_ID = "30062";
    
    /**
     * 麻醉记录单中使用的size 线的大小
     */
    public static int SYMBOL_OBSDATA = 12;
    
    /**
     * 打印图标使用的size 线的大小
     */
    public static int SYMBOL_PRINTDATA = 11;
    
    /**
     * 实时数据时间取值范围
     */
    public static int REAL_TIME_DATA_TIMEOUT = 300;
    
    /**
     * 描点数据时间取值范围
     */
    public static int TRACE_DATA_TIMEOUT = 40;
}
