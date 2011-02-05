package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class FromSubjectFactory implements QuestionFactory {
    private EmailStore mStore;
    
    public FromSubjectFactory(EmailStore store) {
        mStore = store;
    }
    public Question makeQuestion() {
        FromSubjectQuestion q = new FromSubjectQuestion();
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
