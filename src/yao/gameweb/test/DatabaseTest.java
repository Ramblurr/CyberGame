package yao.gameweb.test;

import junit.framework.Assert;

import org.junit.Test;

import yao.gamelib.FromSubjectFactory;
import yao.gamelib.FromSubjectQuestion;
import yao.gamelib.StoredQuestion;
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
        int question_id = db.insertQuestion(q);
        Assert.assertTrue( question_id >= 0);
        
        StoredQuestion q2 = db.retrieveQuestion(question_id);
        Assert.assertNotNull(q2);
        
        StoredQuestion q3 = db.retrieveQuestion(-1);
        Assert.assertNull(q3);
    }

}
