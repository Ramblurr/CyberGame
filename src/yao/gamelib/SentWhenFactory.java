package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

public class SentWhenFactory extends EmailQuestionFactory {

    public SentWhenFactory(EmailStore store, String sent_folder, String inbox_folder) {
        super(store, sent_folder, inbox_folder);
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

    @Override
    protected int getMinimumMinutesBuffer()
    {
        return 1440; // 1 day = 1440 minutes
    }
}
