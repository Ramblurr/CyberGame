package yao.gamelib;

import java.util.Date;

public class SentWhenQuestion extends EmailQuestion {
    private String mQuestionFormat = "When did you send the email to '%s' with subject '%s'";

    public SentWhenQuestion()
    {
    }

    public SentWhenQuestion(String sender, String subject, Date date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    @Override
    public String getQuestion() {
        return String.format(mQuestionFormat, mSender, mSubject);
    }

    @Override
    public String getAnswer() {
        return mDate.toString();
    }

    @Override
    public Type getType() {
        return Type.SentWhen;
    }
}
