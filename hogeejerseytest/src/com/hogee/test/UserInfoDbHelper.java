package com.hogee.test;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class UserInfoDbHelper {
    
    public static final boolean DEBUG = true;
    //for junit test 
    public static void main(String[] args) {
        if (DEBUG) {
            UserInfoDbHelper helper = new UserInfoDbHelper();
            helper.getUserInfo(10001);
        } else {
            System.out.println("cannot debug");
        }
    }

    public EmployeeModel getUserInfo(int jobId) {
        Connection conn = null;
        Statement stmt = null;
        EmployeeModel model = new EmployeeModel();
        try{
            if (DEBUG) System.out.println("getUserInfo jobid is:"+jobId);
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");
        
            // 打开链接
            conn = (Connection) DriverManager.getConnection(MysqlInfo.DB_URL, MysqlInfo.USER, MysqlInfo.PASS);
        
            // 执行查询
            if (DEBUG) System.out.println(" 实例化Statement对象...");
            stmt = (Statement) conn.createStatement();
            String sql;
            sql = "SELECT * FROM employee where jobid="+jobId;
            ResultSet rs = stmt.executeQuery(sql);
        
            // 展开结果集数据库
            while(rs.next()){
                // 通过字段检索
                model.v_name = rs.getString(EmployeeModel.F_name);
                model.v_email = rs.getString(EmployeeModel.F_email);
                model.v_phone_number = rs.getString(EmployeeModel.F_phone_number);
                model.v_jobinfo = rs.getString(EmployeeModel.F_jobinfo);
    
                if (DEBUG) {
                    // 输出数据
                    System.out.println(EmployeeModel.F_name+": "+model.v_name);
                    System.out.println(EmployeeModel.F_email+": "+model.v_email);
                    System.out.println(EmployeeModel.F_phone_number+": "+model.v_phone_number);
                    System.out.println(EmployeeModel.F_jobinfo+": "+model.v_jobinfo);
                }
            }
            // 完成后关闭
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        
        return model;
    }
}
