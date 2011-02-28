package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class SentWhenFactory extends EmailQuestionFactory {

    public SentWhenFactory(EmailStore store) {
        super(store);
    }

    @Override
    public Question makeQuestion() {
        SentWhenQuestion q = new SentWhenQuestion();
        
        return setEmailDataSent(q);
    }

    @Override
    public String makeFakeAnswer( Message m ) throws MessagingException {
        return m.getSentDate().toString();
    }
}
