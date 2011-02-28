package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class SentSubjectFactory extends EmailQuestionFactory {

    public SentSubjectFactory(EmailStore store) {
        super(store);
    }

    @Override
    public Question makeQuestion() {
        SentSubjectQuestion q = new SentSubjectQuestion();
        
        return setEmailDataSent(q);
    }

    @Override
    public String makeFakeAnswer( Message m ) throws MessagingException {
        String subj = m.getSubject();
        subj = subj.trim();
        if ( subj.length() == 0 )
            subj = "<no subject>";
        return subj;
    }
}
