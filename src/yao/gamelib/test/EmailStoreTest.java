package yao.gamelib.test;

import java.util.Calendar;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import yao.gamelib.EmailStore;


public class EmailStoreTest {
    String user = "";
    String     basedir = "C:\\Users\\Casey\\Documents\\workspace\\tmp";
    EmailStore store   = null;
    @Before
    public void before() {
        try {
            store = new EmailStore(user, basedir, null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Test
    public void testGetNewMessage() {
        try {
            store.openFolder( "Inbox" );
            Message first = null; //store the first message to test below
            int count = store.getMessageCountInbox();
            for( int i=0; i < count; i++) {
                //iterate through all the messages
                Message m = store.getNewMessageInbox();
                if( i == 0)
                    first = m;
                Assert.assertNotNull(m);
                // System.out.println( m.getSubject() + " "
                // + m.getSentDate().toString() );
            }
            
            // System.out.println(count);
            
            // attempting to get another message should result in a null ptr
            Message m = store.getNewMessageInbox();
            Assert.assertNull(m);
            
            //test reset message
            store.resetInbox();
            m = store.getNewMessageInbox();
            Assert.assertNotNull(m);
            Assert.assertEquals(first, m);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRange() {
        Calendar c = Calendar.getInstance();
        c.set( 2011, 1, 15 );
        Date startDate = c.getTime();

        c.clear();
        c.set( 2011, 1, 16 );
        Date endDate = c.getTime();
        
        Message[] msgs = store.getMessageInRange( "inbox", startDate, endDate );
        try {
            for ( Message m : msgs ) {

                System.out.println( m.getSubject() + " "
                        + m.getSentDate().toString() );

            }
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
