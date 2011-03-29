package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class FromSubjectFactory extends EmailQuestionFactory {

    public FromSubjectFactory(EmailStore store, String sent_folder, String inbox_folder) {
        super(store, sent_folder, inbox_folder);
    }

    @Override
    public Question makeQuestion() {
        FromSubjectQuestion q = new FromSubjectQuestion();

        return setEmailDataInbox(q);

    }

    @Override
    public String makeFakeAnswer( Message m ) throws MessagingException {
        String subj = m.getSubject();
        if ( subj == null || subj.trim().length() == 0 )
            subj = "<no subject>";
        return subj.trim();
    }

}
