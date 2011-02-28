package yao.gamelib;

import java.util.Date;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;

//QuestionGenerator.registerType(Question.Type.FromWhen, new FromWhenFactory() );

/**
 * Creates FromWhen Questions
 */
public class FromWhenFactory extends EmailQuestionFactory {

    public FromWhenFactory(EmailStore store) {
        super(store);
    }

    @Override
    public Question makeQuestion() {
        FromWhenQuestion q = new FromWhenQuestion();
        return setEmailDataInbox(q);
    }

    @Override
    public String[] makeFakeAnswers( Message m, Message[] msgs )
            throws MessagingException {
        Random randGen = new Random();
        String[] answers = new String[4];
        // randomly pick messages in the range to be fake answers
        for ( int i = 0; i < answers.length; ++i ) {
            int rand_index = randGen.nextInt( msgs.length );
            Date ans_date = msgs[rand_index].getSentDate();
            if( ans_date.equals( m.getSentDate() ) )
                --i;
            else {
                System.out.println( "Using index: " + rand_index );
                answers[i] = ans_date.toString();
            }
        }
        return answers;
    }

}
