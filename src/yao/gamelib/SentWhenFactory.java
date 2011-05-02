package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class SentWhenFactory extends EmailQuestionFactory {
    protected SentWhenQuestion mQuestion;

    public SentWhenFactory(EmailStore store, String sent_folder, String inbox_folder) {
        super(store, sent_folder, inbox_folder);
    }

    @Override
    public Question makeQuestion() {
        mQuestion = new SentWhenQuestion();

        return setEmailDataSent(mQuestion);
    }

    @Override
    public String makeFakeAnswer( Message m ) throws MessagingException {
        DateTime sent_date = new DateTime(m.getSentDate());
        DateTime my_date = new DateTime(mQuestion.getDate());
        Days d = Days.daysBetween(sent_date, my_date);
        if( Math.abs( d.getDays() ) <= getMinimumDaysBuffer() ) {
            return null;
        }

        DateTime now = new DateTime();
        String answer = Utils.formatDateAnswer( sent_date, now );
        return answer;

    }

    @Override
    protected int getMinimumDaysBuffer()
    {
        DateTime my_date = new DateTime(mQuestion.getDate());
        DateTime now = new DateTime();
        int days_ago = Days.daysBetween(my_date, now).getDays();
        if(  days_ago <= 7 ) {
            return 2;
        } else if(  days_ago <= 14 ) {
            return 3;
        } else {
            return 5;
        }
    }
}
