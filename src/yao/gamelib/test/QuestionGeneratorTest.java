package yao.gamelib.test;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import yao.gamelib.EmailStore;
import yao.gamelib.FromSubjectFactory;
import yao.gamelib.FromSubjectQuestion;
import yao.gamelib.FromWhenFactory;
import yao.gamelib.FromWhenQuestion;
import yao.gamelib.FromWhomFactory;
import yao.gamelib.FromWhomQuestion;
import yao.gamelib.Question;
import yao.gamelib.QuestionGenerator;
import yao.gamelib.SentSubjectFactory;
import yao.gamelib.SentSubjectQuestion;
import yao.gamelib.SentWhenFactory;
import yao.gamelib.SentWhenQuestion;
import yao.gamelib.SentWhomFactory;
import yao.gamelib.SentWhomQuestion;

public class QuestionGeneratorTest {

    QuestionGenerator gen;
    @Before
    public void setUp() throws Exception {
        gen = new QuestionGenerator();

        String user = "danfeng";
        String basedir = "/home/ramblurr/src/workspace/CyberGame";
        String inbox = "inbox";
        String sent = "SENT";
        String[] folders = { inbox, sent };
        EmailStore store = new EmailStore(user, basedir, folders);

        gen.registerType(Question.Type.FromWhen, new FromWhenFactory(store, sent, inbox));
        gen.registerType(Question.Type.FromWhom, new FromWhomFactory(store, sent, inbox));
        gen.registerType(Question.Type.FromSubject, new FromSubjectFactory(store, sent, inbox));
        gen.registerType(Question.Type.SentSubject, new SentSubjectFactory(store, sent, inbox));
        gen.registerType(Question.Type.SentWhen, new SentWhenFactory(store, sent, inbox));
        gen.registerType(Question.Type.SentWhom, new SentWhomFactory(store, sent, inbox));
    }

    @Test
    public void testFromWhenQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.FromWhen);
        FromWhenQuestion fwq = (FromWhenQuestion) q;

        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        for ( String fake : fwq.getFakeAnswers() ) {
            System.out.println( "A:" + fake );
        }
        Assert.assertNotNull(fwq);
    }


    @Test
    public void testFromWhomQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.FromWhom);
        FromWhomQuestion fwq = (FromWhomQuestion) q;

        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        for ( String fake : fwq.getFakeAnswers() ) {
            System.out.println( "A:" + fake );
        }
        Assert.assertNotNull(fwq);
    }


    @Test
    public void testFromSubjectQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.FromSubject);
        FromSubjectQuestion fwq = (FromSubjectQuestion) q;

        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        for ( String fake : fwq.getFakeAnswers() ) {
            System.out.println( "A:" + fake );
        }
        Assert.assertNotNull(fwq);
    }


    @Test
    public void testSentWhenQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.SentWhen);
        SentWhenQuestion fwq = (SentWhenQuestion) q;

        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        for (String fake : fwq.getFakeAnswers()) {
            System.out.println("A:" + fake);
        }
        Assert.assertNotNull(fwq);
    }


    @Test
    public void testSentWhomQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.SentWhom);
        SentWhomQuestion fwq = (SentWhomQuestion) q;

        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        for (String fake : fwq.getFakeAnswers()) {
            System.out.println("A:" + fake);
        }
        Assert.assertNotNull(fwq);
    }


    @Test
    public void testSentSubjectQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.SentSubject);
        SentSubjectQuestion fwq = (SentSubjectQuestion) q;

        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        for (String fake : fwq.getFakeAnswers()) {
            System.out.println("A:" + fake);
        }
        Assert.assertNotNull(fwq);
    }

    @Test
    public void emailEquality() throws AddressException {
        InternetAddress e1 = new InternetAddress("dude@dude.com");
        InternetAddress e2 = new InternetAddress("DUDE@dude.com");
        InternetAddress e3 = new InternetAddress("dude@DUDE.com");
        InternetAddress e4 = new InternetAddress("DUDE@DUDE.COM");
        InternetAddress e5 = new InternetAddress("DudeBro <DUDE@DUDE.COM>");
        InternetAddress e6 = new InternetAddress("dudebro <dude@dude.com>");
        InternetAddress e7 = new InternetAddress("dudebro <dudesomeoenelse@dude.com>");
        Assert.assertTrue(e1.equals(e2) && e1.equals(e3) && e1.equals(e4) && e1.equals(e5) && e1.equals(e6));
        Assert.assertFalse(e1.equals(e7));
    }
}
