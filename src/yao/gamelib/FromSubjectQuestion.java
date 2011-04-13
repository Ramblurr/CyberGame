package yao.gamelib;

import java.util.Date;


public class FromSubjectQuestion extends EmailQuestion {
    private String mQuestionFormat = "What is the subject of the email from '%s' on '%s'";

    public FromSubjectQuestion()
    {
    }

    public FromSubjectQuestion(String sender, String subject, Date date, int id)
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
        return Type.FromSubject;
    }
}
