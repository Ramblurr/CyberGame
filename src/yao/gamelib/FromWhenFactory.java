package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;
//QuestionGenerator.registerType(Question.Type.FromWhen, new FromWhenFactory() );

/**
 * Creates FromWhen Questions
 */
public class FromWhenFactory implements QuestionFactory {
    private EmailStore mStore;
    
    public FromWhenFactory(EmailStore store) {
        mStore = store;
    }
    public Question makeQuestion() {
        FromWhenQuestion q = new FromWhenQuestion();
        Message m = mStore.getNewMessageInbox();
        
        try {
            q.setSubject(m.getSubject());
            q.setSender( m.getFrom()[0].toString() ); // TODO this only gets one sender, should we look at all the senders?
            q.setDate(m.getSentDate().toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return q;
    }

}
