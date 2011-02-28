package yao.gameweb.test;

import java.util.Arrays;
import java.util.List;

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

        String sender = "Joe Bob";
        String date = "2011/02/15";
        String subject = "Secret Plans";
        q.setSender(sender);
        q.setDate(date);
        q.setSubject(subject);

        String[] answers = {"Lunch today?", "Bad news...", "Quick Question"};
        q.setFakeAnswers(answers);

        Database db = Database.getInstance();
        int question_id = db.insertQuestion(q);
        Assert.assertTrue( question_id >= 0);

        StoredQuestion q2 = db.retrieveQuestion(question_id);
        Assert.assertNotNull(q2);

        Assert.assertEquals(q.getQuestion(), q2.getQuestion());
        Assert.assertEquals(q.getAnswer(), q2.getAnswer());
        Assert.assertEquals(question_id, q2.getId());

        List<String> answersListExpected = Arrays.asList(q.getFakeAnswers());
        List<String> answersListActual = Arrays.asList(q2.getFakeAnswers());

        Assert.assertTrue( answersListActual.containsAll(answersListExpected) );
        Assert.assertEquals( answersListExpected.size() , answersListActual.size());
        
        StoredQuestion q3 = db.retrieveQuestion(-1);
        Assert.assertNull(q3);
    }

}
