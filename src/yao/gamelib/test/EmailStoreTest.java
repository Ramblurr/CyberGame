package yao.gamelib.test;

import javax.mail.Message;

import junit.framework.Assert;

import org.junit.Test;

import yao.gamelib.EmailStore;


public class EmailStoreTest {
    @Test
    public void testGetNewMessage() {
        String user = "ctlink@binaryelysium.com";
        String basedir = "C:\\Users\\Casey\\Documents\\workspace\\cybergame\\tmp";
        try {
            EmailStore store = new EmailStore(user, basedir);
            store.openFolder("SentMail");
            Message first = null; //store the first message to test below
            int count = store.getMessageCountInbox();
            for( int i=0; i < count; i++) {
                //iterate through all the messages
                Message m = store.getNewMessageInbox();
                if( i == 0)
                    first = m;
                Assert.assertNotNull(m);
//                System.out.println(m.getSubject() +" " + m.getSentDate().toString());
            }
            
//            System.out.println(count);
            
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
}
