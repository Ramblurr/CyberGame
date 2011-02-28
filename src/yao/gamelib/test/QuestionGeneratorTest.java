package yao.gamelib.test;


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
import yao.gamelib.SentSubjectQuestion;
import yao.gamelib.SentWhenQuestion;
import yao.gamelib.SentWhomQuestion;

public class QuestionGeneratorTest {

    QuestionGenerator gen;
    @Before
    public void setUp() throws Exception {
        gen = new QuestionGenerator();

        String user = "ctlink@binaryelysium.com";
        String basedir = "C:\\Users\\Casey\\Documents\\workspace\\tmp";
        EmailStore store = new EmailStore(user, basedir);
        
        
        gen.registerType(Question.Type.FromWhen, new FromWhenFactory(store) );
        gen.registerType( Question.Type.FromWhom, new FromWhomFactory( store ) );
        gen.registerType( Question.Type.FromSubject, new FromSubjectFactory(
                store ) );
        // gen.registerType(Question.Type.SentSubject, new
        // SentSubjectFactory(store) );
        // gen.registerType(Question.Type.SentWhen, new SentWhenFactory(store)
        // );
        // gen.registerType(Question.Type.SentWhom, new SentWhomFactory(store)
        // );
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
    

    // @Test
    public void testSentWhenQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.SentWhen);
        SentWhenQuestion fwq = (SentWhenQuestion) q;
        
        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        Assert.assertNotNull(fwq);
    }
    

    // @Test
    public void testSentWhomQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.SentWhom);
        SentWhomQuestion fwq = (SentWhomQuestion) q;
        
        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        Assert.assertNotNull(fwq);
    }
    

    // @Test
    public void testSentSubjectQuestion() {
        Question q = gen.createQuestionFromType(Question.Type.SentSubject);
        SentSubjectQuestion fwq = (SentSubjectQuestion) q;
        
        System.out.println("Q:" + fwq.getQuestion());
        System.out.println("A:" + fwq.getAnswer());
        Assert.assertNotNull(fwq);
    }

}
