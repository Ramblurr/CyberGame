package yao.gameweb.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import yao.gamelib.Question;
import yao.gamelib.StoredQuestion;

public class Database {
    
    private static class DatabaseHolder  {
        private static final Database INSTANCE = new Database();
    }
    
    public static Database getInstance() {
        return DatabaseHolder.INSTANCE;
    }
    
    Connection mConn;
    static final int VERSION = 0;

    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            mConn = DriverManager.getConnection("jdbc:sqlite:test.db");
            prepareDatabase();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void prepareDatabase() throws SQLException, FileNotFoundException, IOException {
        
        Statement stat = mConn.createStatement();
        stat.executeUpdate(
                "CREATE TABLE if not exists answers (" +
                        "answerId INTEGER PRIMARY KEY," +
                        "answerType TEXT," +
                        "answerText TEXT," +
                        "questionId INTEGER," +
                        "FOREIGN KEY(questionId) REFERENCES questions(questionId)"+
                    ");");
        stat.executeUpdate(
                "CREATE TABLE if not exists questions (" +
                        "questionId INTEGER PRIMARY KEY," +
                        "questionType TEXT," +
                        "questionText TEXT," +
                        "correctAnswerId INTEGER," +
                        "FOREIGN KEY(correctAnswerId) REFERENCES answers(answerId)"+
                    ");");
        stat.executeUpdate(
                "CREATE TABLE if not exists sessions ("+
                    "id TEXT, " +
                    "username TEXT"+
                ");");
    }
    
    public int insertQuestion(Question question) {
        int question_id = -1;
        boolean success = false;
        try {
            mConn.setAutoCommit(false); // this will be one big transaction
            // first, insert the question itself
            PreparedStatement prep = mConn.prepareStatement( "INSERT INTO questions values (?, ?, ?, ?);");
            prep.setNull(1, java.sql.Types.INTEGER); // id
            prep.setString(2, question.getType().toString()); // question type
            prep.setString(3, question.getQuestion()); // question text
            prep.setNull(4, java.sql.Types.INTEGER); // null out the index of the correct answer
            prep.executeUpdate();
            ResultSet rs = prep.getGeneratedKeys();
            rs.next();
            question_id = rs.getInt(1); // save the index of the newly created question
            
            // second, insert the correct answer
            prep = mConn.prepareStatement( "INSERT INTO answers values (?, ?, ?, ?);");
            prep.setNull(1, java.sql.Types.INTEGER);
            prep.setString(2, question.getType().toString());
            prep.setString(3, question.getAnswer());
            prep.setInt(4, question_id);
            
            prep.executeUpdate();
            rs = prep.getGeneratedKeys();
            rs.next();
            int answer_id = rs.getInt(1); // save the index of the correct answer
            
            // third, update the question row with the index of the newly created answer
            prep = mConn.prepareStatement( "UPDATE questions SET correctAnswerId=? WHERE questionId=?;");
            prep.setInt(1, answer_id);
            prep.setInt(2, question_id);
            prep.executeUpdate();
            
            // fourth, insert all the fake questions
            prep = mConn.prepareStatement( "INSERT INTO answers values (?, ?, ?, ?);");
            for( String answer : question.getFakeAnswers() ) {
                prep.setNull(1, java.sql.Types.INTEGER); // id
                prep.setString(2, question.getType().toString()); // question type
                prep.setString(3, answer);
                prep.setInt(4, question_id);
                prep.addBatch();
            }
            prep.executeBatch();
            mConn.commit(); // commit the transaction!
        } catch (SQLException e) {
            System.err.println("Inserting Question failed!");
            System.err.println("Transaction is being rolled back");
            try {
                mConn.rollback();
            } catch (SQLException e1) {
                System.err.println("Rolling back failed!");
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                mConn.setAutoCommit(true);
                success = true;
            } catch (SQLException e) {
                System.err.println("setAutoCommit true failed!");
                e.printStackTrace();
            }
        }
        return success ? question_id : -1;
    }
    
    public StoredQuestion retrieveQuestion(int id) {
        StoredQuestion q = null;
        try {
            PreparedStatement prep = mConn.prepareStatement( "SELECT * FROM questions WHERE questionId=?;");
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            if( rs.next() ) {
                String question_text = rs.getString("questionText");
                Question.Type type = Question.Type.valueOf( rs.getString("questionType") );
                int realAnswer_id = rs.getInt("correctAnswerId");
                String answer_text = "";
                
                prep = mConn.prepareStatement( "SELECT * FROM answers WHERE questionId=?;");
                prep.setInt(1, id);
                rs = prep.executeQuery();
                ArrayList<String> list = new ArrayList<String>(4);
                while( rs.next() ) {
                    if( rs.getInt("answerId") == realAnswer_id)
                        answer_text = rs.getString("answerText");
                    else
                        list.add(rs.getString("answerText"));
                }
                String[] fakeAnswers = new String[list.size()];
                list.toArray(fakeAnswers);
                q = new StoredQuestion(id, question_text, answer_text, fakeAnswers, type);
                q.setId(id);
            }
        } catch (SQLException e) {    
        }
        
        return q;
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
