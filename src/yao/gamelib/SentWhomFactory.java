package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class SentWhomFactory extends EmailQuestionFactory {
    
    public SentWhomFactory(EmailStore store) {
        super(store);
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
