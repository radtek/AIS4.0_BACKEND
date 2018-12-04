package com.digihealth.anesthesia.doc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digihealth.anesthesia.basedata.formbean.SystemSearchFormBean;
import com.digihealth.anesthesia.basedata.po.BasDocument;
import com.digihealth.anesthesia.common.service.BaseService;
import com.digihealth.anesthesia.common.utils.GenerateSequenceUtil;
import com.digihealth.anesthesia.common.utils.SpringContextHolder;
import com.digihealth.anesthesia.doc.formbean.DocMenuFormBean;
import com.digihealth.anesthesia.doc.formbean.DocThemeFormBean;
import com.digihealth.anesthesia.doc.formbean.DocTitleFormBean;
import com.digihealth.anesthesia.doc.formbean.OptForZdyDocFormBean;
import com.digihealth.anesthesia.doc.po.DocTheme;
import com.digihealth.anesthesia.evt.formbean.Filter;
import com.digihealth.anesthesia.sysMng.po.BasDictItem;
import com.digihealth.anesthesia.sysMng.po.BasMenu;
import com.digihealth.anesthesia.sysMng.po.BasRole;
import com.digihealth.anesthesia.sysMng.po.BasRoleMenu;
import com.digihealth.anesthesia.sysMng.service.BasDictItemService;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@Service
public class DocThemeService extends BaseService
{

	//查询局点下的文书的第一级目录
	public List<DocMenuFormBean> findDocMenuPartIdByBeid()
	{
		List<DocMenuFormBean> catalogList = new ArrayList<DocMenuFormBean>();
		String beid = getBeid();
		List<BasMenu> menuList = basMenuDao.findDocMenuPartIdByBeid(beid);
		if(null != menuList && menuList.size()>0)
		{
			for(BasMenu menu : menuList)
			{
				DocMenuFormBean menuBean = new DocMenuFormBean();
				menuBean.setCatalogId(menu.getId());
				menuBean.setCatalogName(menu.getName());
				
				catalogList.add(menuBean);
			}
		}
		
		return catalogList;
	}
	
	@Transactional
	public void saveDocTheme(DocTheme docTheme)
	{
		String docThemeId = docTheme.getDocThemeId();
		//id有值是修改，为空是新增
		if(StringUtils.isNotBlank(docThemeId))
		{
			//查找这个自定义文书
			DocTheme oldDocTheme = docThemeDao.selectByPrimaryKey(docThemeId);
			if(null != oldDocTheme)
			{
				docThemeDao.updateByPrimaryKeySelective(docTheme);
			}
		}else
		{
			docThemeId = GenerateSequenceUtil.generateSequenceNo();
			docTheme.setDocThemeId(docThemeId);
			docThemeDao.insertSelective(docTheme);
		}
	}
	
	//删除自定义文书，标记状态
	public void delDocTheme(String docThemeId)
	{
		DocTheme docTheme = docThemeDao.selectByPrimaryKey(docThemeId);
		if(null != docTheme)
		{
			docTheme.setIsDelete(1);
			docThemeDao.updateByPrimaryKeySelective(docTheme);
		}
	}
	
	//分页查询文书主题
	public List<DocThemeFormBean> seachDocTheme(SystemSearchFormBean systemSearchFormBean)
	{

        if (StringUtils.isEmpty(systemSearchFormBean.getBeid())) 
        {
            systemSearchFormBean.setBeid(getBeid());
        }
        if(StringUtils.isEmpty(systemSearchFormBean.getSort()))
        {
            systemSearchFormBean.setSort("docThemeId");
        }
        if(StringUtils.isEmpty(systemSearchFormBean.getOrderBy()))
        {
            systemSearchFormBean.setOrderBy("ASC");
        }
        String filter = getSysFilter(systemSearchFormBean);
        
        List<DocThemeFormBean> docThemeFormBeanList =  docThemeDao.seachDocTheme(filter, systemSearchFormBean);
        if(null != docThemeFormBeanList && docThemeFormBeanList.size()>0)
        {
        	for(DocThemeFormBean docThemeFormBean : docThemeFormBeanList)
        	{
        		getRoleName(docThemeFormBean);
    			getMenuParentName(docThemeFormBean);
        	}
        }
        return docThemeFormBeanList;
	}
	
	// 分页查询文书主题
	public Integer seachDocThemeTotal(SystemSearchFormBean systemSearchFormBean)
	{

		if (StringUtils.isEmpty(systemSearchFormBean.getBeid()))
		{
			systemSearchFormBean.setBeid(getBeid());
		}
		if (StringUtils.isEmpty(systemSearchFormBean.getSort()))
		{
			systemSearchFormBean.setSort("docThemeId");
		}
		if (StringUtils.isEmpty(systemSearchFormBean.getOrderBy()))
		{
			systemSearchFormBean.setOrderBy("ASC");
		}
		String filter = getSysFilter(systemSearchFormBean);

		return docThemeDao.seachDocThemeTotal(filter, systemSearchFormBean);
	}
	
	//通过主题ID查询主题对象详情
	public DocThemeFormBean seachDocThemeById(String docThemeId)
	{
		DocThemeFormBean docThemeFormBean = docThemeDao.seachDocThemeById(docThemeId);
		
		if(null != docThemeFormBean)
		{
			getRoleName(docThemeFormBean);
			getMenuParentName(docThemeFormBean);
		}
		
		return  docThemeFormBean;
		
	}
	
	//文书审核通过
	@Transactional
	public void docThemeExaminationPassed(String docThemeId)
	{
		DocTheme docTheme = docThemeDao.selectByPrimaryKey(docThemeId);
		if(null != docTheme)
		{
			List<String> menuIds = new ArrayList<String>();
			//生成对应的菜单
			addDocThemeMenu(docTheme,menuIds);
			StringBuffer newmenuids = new StringBuffer();
			for(int k = 0;k<menuIds.size();k++)
			{
				String newmenuid = menuIds.get(k);
				if(k == menuIds.size() -1 )
				{
					newmenuids.append(newmenuid);
				}else
				{
					newmenuids.append(newmenuid);
					newmenuids.append(",");
				}
			}
			//插入角色菜单表
			addThemeMenuRole(docTheme, menuIds);
			docTheme.setMenuId(newmenuids.toString());
			//生成对应的表格
			String sql = "";
			String tableName = "";
			List<DocTitleFormBean> docTitleList = docTitleDao.searchDocTitleByThemeId(docThemeId);
			if(null != docTitleList && docTitleList.size()>0)
			{
				tableName = docTitleList.get(0).getTableName();
				sql = "create table "+tableName+"( id int(11) NOT NULL AUTO_INCREMENT, regOptId varchar(40) DEFAULT NULL  COMMENT '患者编号', processState varchar(8) DEFAULT NULL COMMENT '文书状态', ";
				for(DocTitleFormBean docTitle : docTitleList)
				{
					sql += docTitle.getFieldName() + " " + docTitle.getFieldType() +"("+docTitle.getMaxlength()+") DEFAULT NULL COMMENT" +"'"+docTitle.getTitle()+"', ";
				}
				sql += " beid varchar(20) NOT NULL COMMENT '基线id', PRIMARY KEY (id) ) ENGINE=InnoDB DEFAULT CHARSET=gbk COMMENT='"+docTheme.getDocThemeName()+"'";
			}
			Db.update(sql);
			
			//生成必须完成的列表信息
			BasDocument basDocument = new BasDocument();
			basDocument.setDocId(GenerateSequenceUtil.generateSequenceNo());
			basDocument.setAliasName("");
			basDocument.setBeid(docTheme.getBeid());
			basDocument.setEnable(1);
			basDocument.setIsNeed(1);
			basDocument.setIsOperShow(0);
			basDocument.setName(docTheme.getDocThemeName());
			basDocument.setOperationState("03");//默认术前，可在界面配置
			basDocument.setRequired("");
			basDocument.setTable(tableName);
			basDocumentDao.insert(basDocument);
			
			//修改状态
			docTheme.setThemeState(3);
			docThemeDao.updateByPrimaryKeySelective(docTheme);
			
		}
	}
	
	//通过菜单id查询自定义文书
	public DocThemeFormBean selectDocThemeByMenuId(String menuId,String regOptId)
	{
		DocThemeFormBean formBean =  docThemeDao.seachDocThemeByMenuId(menuId);
		if(null != formBean)
		{
			String docThemeId  = formBean.getDocThemeId();
			List<DocTitleFormBean> titleList = docTitleDao.searchDocTitleByThemeId(docThemeId);
			if(null != titleList && titleList.size()>0)
			{
				formBean.setDocTitleFormbean(titleList);
				String tableName  = titleList.get(0).getTableName();
				List<Record> recordList = Db.find("select *,COUNT(id) AS total  from "+tableName+" where regOptId = "+regOptId);
				if(null != recordList && recordList.size()>0)
				{
					formBean.setRecord(recordList.get(0));
				}
			}
		}
		return formBean;
	}
	
	//保存自定义文书
	public void saveZdyTable(String tableName , Record record)
	{
		if(null != record)
		{
			Integer id = record.getInt("id");
			if(null == id)
			{
				Db.save(tableName, record);
			}else
			{
				Db.update(tableName, record);	
			}
		}
	}

	private void getMenuParentName(DocThemeFormBean docThemeFormBean)
	{
		//取得应用模块名字
		String beid = docThemeFormBean.getBeid();
		String menuParentId = docThemeFormBean.getMenuParentId();
		if(StringUtils.isNotBlank(menuParentId))
		{
			StringBuffer parentNameBuffer = new StringBuffer();
			String parentIds [] = menuParentId.split(",");
			if(null != parentIds && parentIds.length>0)
			{
				for(int i=0;i<parentIds.length;i++)
				{
					String id = parentIds[i];
					if(StringUtils.isNotBlank(id))
					{
						BasMenu basMenu = basMenuDao.selectByPrimaryKey(id, beid);
						if(null != basMenu)
						{
							if(i == parentIds.length -1)
							{
								parentNameBuffer.append(basMenu.getName());
							}else
							{
								parentNameBuffer.append(basMenu.getName());
								parentNameBuffer.append(",");
							}
						}
					}
				}
				docThemeFormBean.setMenuParterNames(parentNameBuffer.toString());
			}
		}
	}

	private String getRoleName(DocThemeFormBean docThemeFormBean)
	{
		//取得角色名字
		String beid = docThemeFormBean.getBeid();
		String roleIds = docThemeFormBean.getRoleIds();
		if(StringUtils.isNotBlank(roleIds))
		{
			StringBuffer roleNameBuffer = new StringBuffer();
			String ids [] = roleIds.split(",");
			if(null != ids && ids.length>0)
			{
				for(int i=0;i<ids.length;i++)
				{
					String id = ids[i];
					if(StringUtils.isNotBlank(id))
					{
						BasRole basRole = basRoleDao.searchRoleById(id, beid);
						if(null != basRole)
						{
							if(i == ids.length -1)
							{
								roleNameBuffer.append(basRole.getName());
							}else
							{
								roleNameBuffer.append(basRole.getName());
								roleNameBuffer.append(",");
							}
						}
					}
				}
				docThemeFormBean.setRoleNames(roleNameBuffer.toString());
			}
		}
		return beid;
	}

	 public String getSysFilter(SystemSearchFormBean systemSearchFormBean) 
	 {
	    String filter = "";
	    List<Filter> filters = systemSearchFormBean.getFilters();
	    if(filters!=null&&filters.size()>0){
	        for(int i = 0;i<filters.size();i++){
	            if(!StringUtils.isEmpty(filters.get(i).getValue().toString())){
	                    filter += " AND "+filters.get(i).getField() +" like '%"+filters.get(i).getValue()+"%' ";
	            }
	        }
	   }
	   return filter;
	}
	
	//增加角色
	private void addThemeMenuRole(DocTheme docTheme,
			List<String> menuIds)
	{
		if(null != docTheme)
		{
			String roleIds = docTheme.getRoleIds();
			String beid = docTheme.getBeid();
			if(StringUtils.isNotBlank(roleIds))
			{
				String [] roles = roleIds.split(",");
				if(null != roles && roles.length>0)
				{
					for(int j=0;j<roles.length;j++)
					{
						String roleid = roles[j];
						if(null != menuIds && menuIds.size()>0)
						{
							for(int k = 0;k<menuIds.size();k++)
							{
								String newmenuid = menuIds.get(k);
								BasRoleMenu basRoleMenu = new BasRoleMenu();
								basRoleMenu.setBeid(beid);
								basRoleMenu.setMenuId(newmenuid);
								basRoleMenu.setPermission("Q_LIST,Q_DET,UPD,DEL,PRINT,SUBM");
								basRoleMenu.setRoleId(roleid);
								basRoleMenuDao.insertSelective(basRoleMenu);
							}
						}
					}
				}
			}
		}
	}

	//增加菜单
	private void addDocThemeMenu(DocTheme docTheme,List<String> menuIds)
	{
		if(null != docTheme)
		{
			String docThemeId = docTheme.getDocThemeId();
			String beid=docTheme.getBeid();
			String catalogIds=docTheme.getMenuParentId();
			String themeName=docTheme.getDocThemeName();
			
			int urlNum = 0;
			BasDictItemService basDictItemService = SpringContextHolder.getBean("basDictItemService"); 
			BasDictItem basDictItem = basDictItemService.getListByGroupIdandCodeName("customize_url_num", "docUrlNum", beid);
			if(null != basDictItem )
			{
				String codeValue = basDictItem.getCodeValue();
				if(StringUtils.isNotBlank(codeValue))
				{
					urlNum = Integer.valueOf(codeValue);
				}
			}
			
			if(StringUtils.isNotBlank(catalogIds))
			{
				String [] firstIds = catalogIds.split(",");
				if(null != firstIds && firstIds.length>0)
				{
					for(int i = 0; i<firstIds.length;i++)
					{
						urlNum++;
						String firstId = firstIds[i];
						if(StringUtils.isBlank(firstId))
						{
							continue;
						}
						BasMenu secondMenu = basMenuDao.findDocMenuSunId(firstId);
						String parentId = "";
						String module = "CTRLCENT";
						if(null != secondMenu)
						{
							parentId = secondMenu.getId();
							module = secondMenu.getModule();
						}
						String parentIds = "0,"+firstId+","+parentId;
						BasMenu newMenu = new BasMenu();
						String newMenuId = GenerateSequenceUtil.generateSequenceNo();
						menuIds.add(newMenuId);
						newMenu.setId(newMenuId);
						newMenu.setName(themeName);
						newMenu.setBeid(beid);
						newMenu.setEnable(1);
						newMenu.setDocTableId(docThemeId);
						newMenu.setIsLeftMenu(0);
						newMenu.setModule(module);
						newMenu.setParentId(parentId);
						newMenu.setParentIds(parentIds);
						newMenu.setPermission("Q_LIST,Q_DET,UPD,DEL,PRINT,SUBM");
						newMenu.setTarget("management");
						newMenu.setType(3);
						newMenu.setUrl("customize_"+urlNum);
						newMenu.setUrlType("link");
						newMenu.setSort(20);
						
						basMenuDao.insertSelective(newMenu);
					}
					if(null != basDictItem )
					{
						basDictItem.setCodeValue(""+urlNum);
						basDictItemService.upBasDictItem(basDictItem);
					}
				}
			}
		}
	}

	//为自定义文书查询患者基本信息
	public OptForZdyDocFormBean selectOptForZdyDoc(String regOptId)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
