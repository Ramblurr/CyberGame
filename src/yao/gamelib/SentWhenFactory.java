package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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
        Period period = new Period(sent_date, now);
        PeriodFormatter formatter = new PeriodFormatterBuilder()
        .printZeroNever()
        .appendYears().appendSuffix(" year ", " years ")
        .appendSeparator(" and")
        .printZeroRarelyFirst()
        .appendMonths().appendSuffix(" month", " months")
        .appendSeparator(" ")
        .appendWeeks().appendSuffix( " week",  " weeks" )
        .appendSeparator(" ")
        .appendDays().appendSuffix(" day", " days")
        .printZeroNever()
        .toFormatter();
        String answer = formatter.print( period ) + " ago";
        System.out.println(d.getDays());
        // if the answer is more than 14 days ago, append the actual date
        if( Math.abs( Days.daysBetween(sent_date, now).getDays() ) > 14 ) {
            answer += " (" + sent_date.toString( "dd MMM YYYY" ) + ")";
        }
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
