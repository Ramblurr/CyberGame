package yao.gameweb.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import yao.gamelib.Question;
import yao.gamelib.StoredQuestion;

/**
 * This is a singleton class that encapsulates access to the Database. 
 * @author Casey
 *
 */
public class Database {
    
    private static class DatabaseHolder  {
        private static final Database INSTANCE = new Database();
    }
    
    /**
     * Get the instance of the Database class
     * @return the database
     */
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
                    "userId INTEGER," +
                    "FOREIGN KEY(userId) REFERENCES users(userId)"+
                ");");
        stat.executeUpdate(
                "CREATE TABLE if not exists users ("+
                    "userId INTEGER PRIMARY KEY," +
                    "username TEXT"+
                ");");
        stat.executeUpdate(
                "CREATE TABLE if not exists responses ("+
                    "responseId INTEGER PRIMARY KEY," +
                    "userId INTEGER," +
                    "questionId INTEGER," +
                    "answerId INTEGER," +
                    "FOREIGN KEY(userId) REFERENCES users(userId),"+
                    "FOREIGN KEY(questionId) REFERENCES questions(questionId),"+
                    "FOREIGN KEY(answerId) REFERENCES answers(answerId)"+
                ");");
    }
    
    /**
     * Stores a question in the database. The question text, type, and answers will be stored.
     * @param question the question to store
     * @return the unique id of the question in the database, which can be used for retrieval
     */
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
    
    /**
     * Retrieve a question from the database
     * @param id the unique id of the question to retrieve
     * @return a StoredQuestion containing the question, real answer, and fake answers
     */
    public StoredQuestion retrieveQuestion(int id) {
        StoredQuestion q = null;
        try {
            PreparedStatement prep = mConn.prepareStatement( "SELECT * FROM questions WHERE questionId=?;");
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            if( rs.next() ) {
                q = populateQuestion(rs);
            }
        } catch (SQLException e) {    
        }
        
        return q;
    }

    /**
     * Get some stored questions
     * 
     * @param number
     * @return an array of questions
     */
    public StoredQuestion[] getQuestions(int number) {
        ArrayList<StoredQuestion> questions  = new ArrayList<StoredQuestion>();

        try {
            PreparedStatement prep = mConn.prepareStatement("SELECT * FROM questions LIMIT ?");
            prep.setInt(1, number);
            ResultSet rs = prep.executeQuery();
            while(rs.next()) {
                try {
                    StoredQuestion q = populateQuestion(rs);
                    if (q != null)
                        questions.add(q);
                } catch (SQLException e) {
                }
            }
        } catch (SQLException e) {
        }

        StoredQuestion[] q_arr= questions.toArray(new StoredQuestion[questions.size()]);
        return q_arr;
    }

    private StoredQuestion populateQuestion(ResultSet rs) throws SQLException {
        StoredQuestion q = null;
        int id = rs.getInt("questionId");
        String question_text = rs.getString("questionText");
        Question.Type type = Question.Type.valueOf(rs.getString("questionType"));
        int realAnswer_id = rs.getInt("correctAnswerId");
        String answer_text = "";

        PreparedStatement prep = mConn.prepareStatement("SELECT * FROM answers WHERE questionId=?;");
        prep.setInt(1, id);
        rs = prep.executeQuery();
        ArrayList<AnswerStub> list = new ArrayList<AnswerStub>(4);
        while (rs.next()) {
            if (rs.getInt("answerId") == realAnswer_id)
                answer_text = rs.getString("answerText");
            else {
                AnswerStub stub = new AnswerStub();
                stub.text = rs.getString("answerText");
                stub.id = rs.getInt("answerId");
                list.add(stub);
            }
        }
        AnswerStub[] fakeAnswers = new AnswerStub[list.size()];
        list.toArray(fakeAnswers);
        q = new StoredQuestion(id, question_text, answer_text, realAnswer_id, fakeAnswers, type);
        q.setId(id);
        return q;
    }

    /**
     * Stores a user's response to a question.
     * 
     * @param user_id
     * @param question_id
     * @param answer_id
     */
    public boolean storeResponse(int user_id, int question_id, int answer_id) {
        try {
            PreparedStatement prep = mConn.prepareStatement( "INSERT INTO responses values (?, ?, ?, ?);");
            prep.setNull(1, java.sql.Types.INTEGER); // reponse id
            prep.setInt(2, user_id);
            prep.setInt(3, question_id);
            prep.setInt(4, answer_id);
            prep.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    /**
     * Adds the username to the database, if it does not exist, otherwise 
     * fetches the user from the database.
     * @param username
     * @return the user id
     */
    public int createOrGetUser(String username) {
        int user_id = -1;
        try {
            PreparedStatement prep = mConn.prepareStatement("SELECT * FROM users WHERE username=?;");
            prep.setString(1, username);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                user_id = rs.getInt("userId");
            } else {
                prep = mConn.prepareStatement("INSERT INTO users values (?, ?);");
                prep.setNull(1, java.sql.Types.INTEGER); // user id
                prep.setString(2, username);
                prep.executeUpdate();
                rs = prep.getGeneratedKeys();
                rs.next();
                user_id = rs.getInt(1);
            }
        } catch (SQLException e) {
        }
        return user_id;
    }

    public String getUsername(int userId) {
        String name = null;
        try {
            PreparedStatement prep = mConn.prepareStatement("SELECT * FROM users WHERE userId=?;");
            prep.setInt(1, userId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                name = rs.getString("username");
            }
        } catch (SQLException e) {
        }
        return name;
    }

    /**
     * Create a new user session. Does NOT check if the user has an existing session.
     * 
     * @param user the username of the user
     * @return a new unique session id
     */
    public String makeSession(String user) {
        try {
            int user_id = createOrGetUser(user.toLowerCase().trim());
            String sessionid = UUID.randomUUID().toString();  
            PreparedStatement prep = mConn.prepareStatement( "insert into sessions values (?, ?);");
            prep.setString(1, sessionid);
            prep.setInt(2, user_id);
            prep.addBatch();
            prep.executeBatch();
            return sessionid;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Returns the username associated with the supplied session id.
     * @param sessionid used to look up the user
     * @return the user name
     */
    public int getUserForSession(String sessionid)  {
        Statement stat;
        try {
            stat = mConn.createStatement();
  
            PreparedStatement prep = mConn.prepareStatement( "select userId from sessions where id = ?;");
            prep.setString(1, sessionid);
            ResultSet rs = prep.executeQuery();
            if( rs.next() ) {
                int id = rs.getInt("userId");
                return id;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
    
}
