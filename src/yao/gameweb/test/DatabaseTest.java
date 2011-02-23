package yao.gameweb.test;

import junit.framework.Assert;

import org.junit.Test;

import yao.gamelib.FromSubjectFactory;
import yao.gamelib.FromSubjectQuestion;
import yao.gameweb.util.Database;


public class DatabaseTest {
    
    @Test
    public void testDBConnection() {
        Database db = Database.getInstance();
    }
    
    @Test
    public void testInsertRetrieveQuestion() {
        FromSubjectQuestion q = new FromSubjectQuestion();
        
        q.setSender("Joe Bob");
        q.setDate("2011/02/15");
        q.setSubject("Secret Plans");
        
        String[] answers = {"Lunch today?", "Bad news...", "Quick Question"};
        q.setFakeAnswers(answers);
        
        Database db = Database.getInstance();
        Assert.assertTrue( db.insertQuestion(q) );
    }

}
