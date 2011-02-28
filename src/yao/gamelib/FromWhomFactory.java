package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class FromWhomFactory extends EmailQuestionFactory {

    public FromWhomFactory(EmailStore store) {
        super(store);
    }

    @Override
    public Question makeQuestion() {
        FromWhomQuestion q = new FromWhomQuestion();
        return setEmailDataInbox(q);
    }

    @Override
    public String makeFakeAnswer( Message m ) throws MessagingException {
        return m.getFrom()[0].toString();
    }

}
