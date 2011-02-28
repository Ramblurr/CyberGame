package yao.gamelib;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;

public abstract class EmailQuestionFactory implements QuestionFactory {
    protected EmailStore mStore;
    
    public EmailQuestionFactory(EmailStore store) {
        mStore = store;
    }

    @Override
    public abstract Question makeQuestion();

    /**
     * Choose a set of fake answers from the array of messages.
     * 
     * @param m
     *            the message we are creating fake answers for
     * @param msgs
     *            the messages to choose fake answers from
     * @return a string array containing the fake answers
     * @throws MessagingException
     */
    public String[] makeFakeAnswers( Message m, Message[] msgs )
            throws MessagingException {
        return null;
    }

    protected EmailQuestion setEmailDataInbox(EmailQuestion q) {
        try {
            Message m = mStore.getNewMessageInbox();

            q.setFakeAnswers( makeFakeAnswers( m, getRandomRange( m ) ) );
            return setEmailData(m, q);
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected EmailQuestion setEmailDataSent(EmailQuestion q) {
        Message m = mStore.getNewMessageSent();
        
        return setEmailData(m, q);
    }
    
    private EmailQuestion setEmailData( Message m, EmailQuestion q ) {
        try {
            q.setSubject(m.getSubject());
            q.setSender( m.getFrom()[0].toString() ); // TODO this only gets one sender, should we look at all the senders?
            q.setDate(m.getSentDate().toString());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return q; 
    }

    private Message[] getRandomRange( Message m ) throws MessagingException {
        // first, generate the range
        // from 1 to max_range_days
        final int max_range_days = 30; // the range will not be greater than 30
        Random randGen = new Random();
        int range_days = 0;
        do {
            range_days = randGen.nextInt( max_range_days );
        } while (range_days == 0); // we don't want 0 as the upper bound

        System.out.println( "Using date range " + range_days );
        // where in the range will the actual answer fall?
        int date_position = randGen.nextInt( range_days );
        System.out.println( "Using date position " + date_position );
        // calculate the range in days
        int days_after_date = range_days - date_position;
        int days_before_date = date_position;

        // convert those to actual dates based on the date of the message
        Calendar c = Calendar.getInstance();
        Date sent_date = m.getSentDate();

        c.setTime( sent_date );
        c.add( Calendar.DATE, -days_before_date );
        Date begin_range = c.getTime();

        c.clear();
        c.setTime( sent_date );
        c.add( Calendar.DATE, days_after_date );
        Date end_range = c.getTime();

        System.out.println( "Using date range " + begin_range.toString()
                + " to " + end_range.toString() );

        // get all messages within the range
        Message[] msgs = mStore.getMessageInRange( "inbox", begin_range,
                end_range );
        // we need at least 4 messages in the range that aren't the actual
        // message so we expand the range by 1 day on each end until
        // we have at least 5 messages
        while (msgs.length <= 5) {
            c.clear();
            c.setTime( begin_range );
            c.add( Calendar.DATE, -1 );
            begin_range = c.getTime();

            c.clear();
            c.setTime( end_range );
            c.add( Calendar.DATE, 1 );
            end_range = c.getTime();
            msgs = mStore.getMessageInRange( "inbox", begin_range, end_range );
        }
        return msgs;
    }

}
