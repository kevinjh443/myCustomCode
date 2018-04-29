package com.hogee.test;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class MysqlConnection {
    
    private Connection conn = null;
    private Statement stmt = null;
    
    public MysqlConnection() {
        
    }
    
    protected Statement getDbConnectSt() {
        
        return stmt;
    }
    
    protected boolean closeConnectSt() {
        
        return true;
    }

}
