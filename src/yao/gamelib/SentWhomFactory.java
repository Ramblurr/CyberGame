package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class SentWhomFactory extends EmailQuestionFactory {
    
    public SentWhomFactory(EmailStore store, String sent_folder, String inbox_folder) {
        super(store, sent_folder, inbox_folder);
    }

    @Override
    public Question makeQuestion() {
        SentWhomQuestion q = new SentWhomQuestion();
        
        return setEmailDataSent(q);
    }

    @Override
    public String makeFakeAnswer( Message m ) throws MessagingException {
        return m.getFrom()[0].toString();
    }

}
