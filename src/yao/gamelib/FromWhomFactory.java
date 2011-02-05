package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class FromWhomFactory implements QuestionFactory {
    private EmailStore mStore;
    
    public FromWhomFactory(EmailStore store) {
        mStore = store;
    }
    public Question makeQuestion() {
        FromWhomQuestion q = new FromWhomQuestion();
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
