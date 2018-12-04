package com.digihealth.anesthesia.common.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.digihealth.anesthesia.common.config.Global;

public class ConnectionManager {
	private static Logger logger = Logger.getLogger(ConnectionManager.class);
	
	private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>();
	
    /**
     * 南华大学附属南华医院HIS数据库连接
     */
    private static String NHDXFSYY_HIS_JDBC_DRIVER_CLASS = Global.getJdbcConfig("nhdxfsyy.jdbc.driver.sqlserver");
    private static String NHDXFSYY_HIS_JDBC_URL = Global.getJdbcConfig("nhdxfsyy.jdbc.url.sqlserver");
    private static String NHDXFSYY_HIS_JDBC_USERNAME = Global.getJdbcConfig("nhdxfsyy.jdbc.username.sqlserver");
    private static String NHDXFSYY_HIS_JDBC_PASSWORD = Global.getJdbcConfig("nhdxfsyy.jdbc.password.sqlserver");
  
    /**
     * 湖南航天医院检查报告数据库连接
     */
    private static String HNHTYY_HIS_JDBC_DRIVER_CLASS = Global.getJdbcConfig("hnhtyy.jdbc.driver.sqlserver");
    private static String HNHTYY_HIS_JDBC_URL = Global.getJdbcConfig("hnhtyy.jdbc.url.sqlserver");
    private static String HNHTYY_HIS_JDBC_USERNAME = Global.getJdbcConfig("hnhtyy.jdbc.username.sqlserver");
    private static String HNHTYY_HIS_JDBC_PASSWORD = Global.getJdbcConfig("hnhtyy.jdbc.password.sqlserver");
    

	/**
	 * 从连接池拿Connection
	 * 
	 * getConnection和connectionHolder.get()的区别
	 * connectionHolder.get()是尝试从ThreadLocal中获取Connection,如果没有,返回null,如果有,直接返回.
	 * getConnection也是尝试从ThreadLocal中获取Connection,如果没有,则创建一个,然后返回,如果有,直接返回.
	 */
	public static Connection getConnection(String jdbcDriveClass,String jdbcUrl,String jdbcUserName,String jdbcPassword) {

		Connection connection = connectionHolder.get();

		if (connection == null) {
			// 1.连接池可以理解是一个java类,必须实现接口DateSource
			// 2.DBCP连接池也是一个java类,Tomcat为其new了对象并注册到JNDI,同时Tomcat实现了JavaEE规范之一的JNDI
			// 3.Context接口的lookup()可以从JNDI获取对象,通过名值对的形式;下面语句获取连接池对象(DateSource类型)
			try {
				Class.forName(jdbcDriveClass); // classLoader,加载对应驱动
				connection = (Connection) DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("ConnectionManager--getConnection(SQLException):"+e.getMessage());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				logger.error("ConnectionManager--getConnection(ClassNotFoundException):"+e.getMessage());
			}
			// 直接从连接池拿连接,不是此时才让Oracle去创建连接
			connectionHolder.set(connection);
		}
		return connection;
	}
    
    //--------------------------------------------------永兴人民医院--------------------------------------------------------------
    /**
     * 获取南华大学附属南华医院
     * @return
     */
    public static Connection getNHDXFSYYHisConnection(){
        return getConnection(NHDXFSYY_HIS_JDBC_DRIVER_CLASS,NHDXFSYY_HIS_JDBC_URL,NHDXFSYY_HIS_JDBC_USERNAME,NHDXFSYY_HIS_JDBC_PASSWORD);
    }
    
    
    /**
     * 航天医院检查报告
     * @return
     */
    public static Connection getHNHTYYHisConnection(){
        return getConnection(HNHTYY_HIS_JDBC_DRIVER_CLASS,HNHTYY_HIS_JDBC_URL,HNHTYY_HIS_JDBC_USERNAME,HNHTYY_HIS_JDBC_PASSWORD);
    }
    
    //--------------------------------------------------永兴人民医院--------------------------------------------------------------
    
    
    /**
	 * Connection使用完毕,关闭
	 * 此处的Connection是从连接池中拿出来的,关闭Connection实质上是让Connection恢复空闲状态
	 */
	public static void closeConnection() {
		// 尝试从ThreadLocal获取Connection,如果没有,关闭Connection失去意义.
		Connection connection = connectionHolder.get();

		if (connection != null) {
			try {
				connection.close();
				connectionHolder.remove();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("ConnectionManager--closeConnection(SQLException):"+e.getMessage());
			}
		}
	}
	
	/**
     * Connection使用完毕,关闭
     * 此处的Connection是从连接池中拿出来的,关闭Connection实质上是让Connection恢复空闲状态
     */
    public static void closeConnection(Connection connection) {
        // 尝试从ThreadLocal获取Connection,如果没有,关闭Connection失去意义.
        //Connection connection = connectionHolder.get();

        if (connection != null) {
            try {
                connection.close();
                //connectionHolder.remove();
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error("ConnectionManager--closeConnection(SQLException):"+Exceptions.getStackTraceAsString(e));
            }
        }
    }
}
