package yao.gamelib;

import java.util.Date;

public class FromWhenQuestion extends EmailQuestion {
    private String mQuestionFormat = "When did you receive the email from '%s' with subject '%s'";

    public FromWhenQuestion()
    {
    }

    public FromWhenQuestion(String sender, String subject, Date date, int id)
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
        return Type.FromWhen;
    }
}
