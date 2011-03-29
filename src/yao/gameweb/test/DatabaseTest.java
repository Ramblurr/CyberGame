package yao.gameweb.test;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import yao.gamelib.FromSubjectQuestion;
import yao.gamelib.Question;
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
        Question.NoneAboveType noneAboveType = Question.NoneAboveType.WithRealAnswer;
        q.setSender(sender);
        q.setDate(date);
        q.setSubject(subject);
        q.setNoneOfTheAbove( noneAboveType );

        String[] answers = {"Lunch today?", "Bad news...", "Quick Question", "None of the above"};
        q.setFakeAnswers(answers);

        Database db = Database.getInstance();
        int question_id = db.insertQuestion(q);
        Assert.assertTrue( question_id >= 0);

        StoredQuestion q2 = db.retrieveQuestion(question_id);
        Assert.assertNotNull(q2);

        Assert.assertEquals(q.getQuestion(), q2.getQuestion());
        Assert.assertEquals(q.getAnswer(), q2.getAnswer());
        Assert.assertEquals(question_id, q2.getId());
        Assert.assertEquals( q.getNoneOfTheAbove(), q2.getNoneOfTheAbove() );

        List<String> answersListExpected = Arrays.asList(q.getFakeAnswers());
        List<String> answersListActual = Arrays.asList(q2.getFakeAnswers());

        Assert.assertTrue( answersListActual.containsAll(answersListExpected) );
        Assert.assertEquals( answersListExpected.size() , answersListActual.size());

        StoredQuestion q3 = db.retrieveQuestion(-1);
        Assert.assertNull(q3);
    }

    @Test
    public void createOrGetUser() {
        Database db = Database.getInstance();

        String username = "dude";
        int id = db.createOrGetUser(username);
        Assert.assertTrue(id != -1);
        int id2 = db.createOrGetUser(username);
        Assert.assertEquals(id, id2);
        String username2 = "bro";
        int id3 = db.createOrGetUser(username2);
        Assert.assertTrue(id != -1);
        int id4 = db.createOrGetUser(username2);
        Assert.assertEquals(id3, id4);
        Assert.assertTrue(id != id4);
    }

}
