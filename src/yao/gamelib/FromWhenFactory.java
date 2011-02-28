package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

//QuestionGenerator.registerType(Question.Type.FromWhen, new FromWhenFactory() );

/**
 * Creates FromWhen Questions
 */
public class FromWhenFactory extends EmailQuestionFactory {

    public FromWhenFactory(EmailStore store) {
        super(store);
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

}
