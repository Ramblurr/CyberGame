package yao.gamelib;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;


public class Utils
{
    static String formatDateAnswer(DateTime start, DateTime end) {
        Period period = new Period(start, end);
        String answer = Utils.formatPeriod(period) + " ago";
        // if the answer is more than 14 days ago, append the actual date
        if( Days.daysBetween(start, end).getDays() > 14 ) {
            answer += " (" + start.toString( "dd MMM YYYY" ) + ")";
        }
        return answer;
    }
    static String formatPeriod(Period period) {
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
        return formatter.print( period );
    }
}
