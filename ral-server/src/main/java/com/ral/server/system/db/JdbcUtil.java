package com.ral.server.system.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtil {


    private Logger logger = LoggerFactory.getLogger(JdbcUtil.class);


    //数据库地址
    public  String url = "jdbc:mysql://localhost:3306/awmlog?useSSL=false&characterEncoding=utf8";
    //驱动信息
    public  String driver = "com.mysql.jdbc.Driver";
    //用户名 userName为数据库用户名
    public  String user = "root";
    //密码 password为数据库密码
    public  String password = "123456";


    //jdbc连接
    private Connection conn = null;

    private PreparedStatement pstm = null;

    private ResultSet rs = null;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * 构造函数
     */
    public JdbcUtil() throws Exception{
        //加载数据库驱动程序
        try {
            Class.forName(driver);
        } catch (Exception e) {
            logger.error("jdbc构造函数失败"+e);
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 获取数据库连接 默认连接
     * @return
     */
    public Connection getConnection(String url, String user, String password) throws SQLException, ClassNotFoundException {
        try {
            //获得到数据库的连接
            url = url+"?setUnicode=true&characterEncoding=utf-8&useSSL=false&rewriteBatchedStatements=true";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,password);
            return conn;
        } catch (Exception e) {
            logger.error("JDBCUtil getConnection error"+e);
            throw e;
        }
    }

    /**
     * 关闭数据路连接
     */
    public void releaseConnectn() throws SQLException {
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("关闭数据库连接rs:"+e);
                throw e;
            }
        }

        if (pstm != null){
            try {
                pstm.close();
            } catch (SQLException e) {
                logger.error("关闭数据库连接pstm:"+e);
                throw e;
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch(SQLException e) {
                logger.error("关闭数据库连接conn:"+e);
                throw e;
            }
        }

    }



    public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
        return executeQuery(sql,null);
    }


    /**
     * 查询多条记录返回map对象
     * @param sql sql语句
     * @param params sql语句参数
     * @return 返回结果表，每个元素为一行结果
     */
    public List<Map<String, Object>> executeQuery(String sql, List<Object> params) throws SQLException {
        List<Map<String,Object>> list = null;
        int index = 1;
        try {
            pstm = conn.prepareStatement(sql);//sql语句被预编译存储在prepareStatement对象中，然后可以使用此对象多次高效地执行该语句
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstm.setObject(index++, params.get(i));
                }
            }
            rs = pstm.executeQuery();
            list = new ArrayList<Map<String, Object>>();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map<String,Object> map = new HashMap<String,Object>();
                for (int j = 1; j <= rsmd.getColumnCount(); j++) {
                    String col_key = rsmd.getColumnName(j);
                    Object col_value = rs.getObject(col_key);
                    if (col_value == null) {
                        col_value = "";
                    }
                    map.put(col_key,col_value);
                }
                list.add(map);
            }
        } catch (Exception e) {
            logger.error(sql);
            logger.error("查询多条记录返回map报错:"+e);
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e2) {
                logger.error("查询多条记录返回finally报错e2"+e2);
                throw e2;
            }
        }
        return list;
    }

}
