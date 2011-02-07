package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class FromSubjectFactory extends EmailQuestionFactory {

    public FromSubjectFactory(EmailStore store) {
        super(store);
    }

    public Question makeQuestion() {
        FromSubjectQuestion q = new FromSubjectQuestion();
        
        return setEmailDataInbox(q);

    }

}
