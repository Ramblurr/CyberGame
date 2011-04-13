package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

//QuestionGenerator.registerType(Question.Type.FromWhen, new FromWhenFactory() );

/**
 * Creates FromWhen Questions
 */
public class FromWhenFactory extends EmailQuestionFactory {

    public FromWhenFactory(EmailStore store, String sent_folder, String inbox_folder) {
        super(store, sent_folder, inbox_folder);
    }

    @Override
    public Question makeQuestion() {
        FromWhenQuestion q = new FromWhenQuestion();
        return setEmailDataInbox(q);
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
