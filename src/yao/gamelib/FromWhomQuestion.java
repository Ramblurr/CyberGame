package yao.gamelib;

import org.joda.time.DateTime;

public class FromWhomQuestion extends EmailQuestion {

    private String mQuestionFormat = "From whom did you receive the email with subject '%s' on %s";

    public FromWhomQuestion()
    {
    }

    public FromWhomQuestion(String sender, String subject, DateTime date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    @Override
    public String getQuestion() {
        return String.format(mQuestionFormat, mSubject, mDate);
    }

    @Override
    public String getAnswer() {
        return mSender;
    }

    @Override
    public Type getType() {
        return Type.FromWhom;
    }
}
