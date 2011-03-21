package yao.gamelib;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class SentWhomFactory extends EmailQuestionFactory {
    
    public SentWhomFactory(EmailStore store, String sent_folder, String inbox_folder) {
        super(store, sent_folder, inbox_folder);
    }

    @Override
    public Question makeQuestion() {
        SentWhomQuestion q = new SentWhomQuestion();
        
        return setEmailDataSent(q);
    }

    @Override
    public String makeFakeAnswer( Message m ) throws MessagingException {
        return m.getFrom()[0].toString();
    }

    @Override
    protected boolean isDuplicate(String answer1, String answer2) {
        if (answer1 == null || answer2 == null)
            return false;
        try {
            InternetAddress email1 = new InternetAddress(answer1);
            InternetAddress email2 = new InternetAddress(answer2);
            return email1.equals(email2);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return answer1.equals(answer2);
    }

}
