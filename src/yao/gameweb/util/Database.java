package yao.gameweb.util;

import java.sql.*;
import java.util.UUID;

public class Database {
    
    private static class DatabaseHolder  {
        private static final Database INSTANCE = new Database();
    }
    
    public static Database getInstance() {
        return DatabaseHolder.INSTANCE;
    }
    
    Connection mConn;
    static final int VERSION = 0;
    public static final String SALT ="Z6BsoDFamnMQFtNHkByJB1Um"; //128 bits

    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            mConn = DriverManager.getConnection("jdbc:sqlite:test.db");
            prepareDatabase();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void prepareDatabase() throws SQLException {
        Statement stat = mConn.createStatement();
        stat.executeUpdate("create table if not exists sessions (id, username);");
    }
    
    public String makeSession(String user) {
        try {
            String sessionid = UUID.randomUUID().toString();  
            PreparedStatement prep = mConn.prepareStatement( "insert into sessions values (?, ?);");
            prep.setString(1, sessionid);
            prep.setString(2, user);
            prep.addBatch();
            prep.executeBatch();
            return sessionid;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    
    public String getUserForSession(String sessionid)  {
        Statement stat;
        try {
            stat = mConn.createStatement();
  
            PreparedStatement prep = mConn.prepareStatement( "select username from sessions where id = ?;");
            prep.setString(1, sessionid);
            ResultSet rs = prep.executeQuery();
            if( rs.next() ) {
                String user = rs.getString("username");
                return user;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
}
