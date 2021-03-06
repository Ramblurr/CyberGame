package yao.gamelib;

import org.joda.time.DateTime;

public class SentWhomQuestion extends EmailQuestion {
    private String mQuestionFormat = "To whom did you send the email on '%s' with subject '%s'";

    public SentWhomQuestion()
    {
    }

    public SentWhomQuestion(String sender, String subject, DateTime date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    @Override
    public String getQuestion() {
        return String.format(mQuestionFormat, mDate, mSubject);
    }

    @Override
    public String getAnswer() {
        return mSender;
    }

    @Override
    public Type getType() {
        return Type.SentWhom;
    }
}
