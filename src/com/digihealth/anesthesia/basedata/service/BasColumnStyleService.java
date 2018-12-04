/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.digihealth.anesthesia.basedata.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.po.BasColumnStyle;
import com.digihealth.anesthesia.common.service.BaseService;
/**
 * 
     * Title: ColumnStyleService.java    
     * Description: 日志Service
     * @author liukui       
     * @created 2016-8-7 下午6:00:54
 */
@Service
public class BasColumnStyleService extends BaseService {

	@Transactional
	public void saveColumnStyle(BasColumnStyle record){
		if(record.getId()!=null){
			basColumnStyleDao.updateByPrimaryKeySelective(record);
		}else{
			basColumnStyleDao.insertSelective(record);
		}
	}
	
	public BasColumnStyle getColumnStyle(String id){
		return basColumnStyleDao.selectByPrimaryKey(id);
	}
	
	@Transactional
	public void changeColumnSort(List<BasColumnStyle> ls){
		for (BasColumnStyle columnStyle : ls) {
			basColumnStyleDao.updateByPrimaryKeySelective(columnStyle);
		}
	}
	
	public static void main(String[] args) {
//		for (int i = 180; i <= 211; i++) {
//			String val = "INSERT INTO `bas_observe_wave` (`keyid`, `patientId`, `timesend`, `observeId`, `observevalue`, `state`, `observeName`, `icon`, `color`, `freq`, `position`, `intervalTime`, `deviceId`, `docType`, `ip`, `source`) VALUES('20181109103825201811090925403714" + i + "','201811090925403714','2018-11-09 10:38:25','33001','-0.058593',NULL,'ECGI','','',NULL,NULL,NULL,'',NULL,'192.168.5.88','192.168.5.202');";
//			System.out.println(val);
//		}

		try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 读入TXT文件 */  
            String pathname = "D:\\input.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader  
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = "";  
            line = br.readLine();
            int i = 1;
            while (line != null) {  
                line = br.readLine(); // 一次读入一行数据
                System.out.println("INSERT INTO `bas_observe_wave` (`keyid`, `patientId`, `timesend`, `observeId`, `observevalue`, `state`, `observeName`, `icon`, `color`, `freq`, `position`, `intervalTime`, `deviceId`, `docType`, `ip`, `source`) VALUES('20181109103825201811090925403714" + i + "','201811090925403714','2018-11-09 10:38:25','33001','" + line + "',NULL,'ECGI','','',NULL,NULL,NULL,'',NULL,'192.168.5.88','192.168.5.202');");
                i++;
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
}
