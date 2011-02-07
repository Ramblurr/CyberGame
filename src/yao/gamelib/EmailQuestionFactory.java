package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public abstract class EmailQuestionFactory implements QuestionFactory {
    private EmailStore mStore;
    
    public EmailQuestionFactory(EmailStore store) {
        mStore = store;
    }

    public abstract Question makeQuestion();

    protected EmailQuestion setEmailDataInbox(EmailQuestion q) {
        Message m = mStore.getNewMessageInbox();
        
        return setEmailData(m, q);
    }
    
    protected EmailQuestion setEmailDataSent(EmailQuestion q) {
        Message m = mStore.getNewMessageSent();
        
        return setEmailData(m, q);
    }
    
    private EmailQuestion setEmailData( Message m, EmailQuestion q ) {
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
