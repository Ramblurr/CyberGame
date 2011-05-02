package yao.gamelib;

import org.joda.time.DateTime;


public class SentSubjectQuestion extends EmailQuestion {

    private String mQuestionFormat = "What was the subject of the email you sent to '%s' on '%s'";

    public SentSubjectQuestion() {

    }
    public SentSubjectQuestion(String sender, String subject, DateTime date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    @Override
    public String getQuestion() {
        return String.format(mQuestionFormat, mSender, mDate);
    }

    @Override
    public String getAnswer() {
        if( mSubject == null )
            return "<no subject>";
        return mSubject;
    }

    @Override
    public Type getType() {
        return Type.SentSubject;
    }

}
