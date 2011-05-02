package yao.gamelib;

import org.joda.time.DateTime;

public class FromWhenQuestion extends EmailQuestion {
    private String mQuestionFormat = "When did you receive the email from '%s' with subject '%s'";

    public FromWhenQuestion()
    {
    }

    public FromWhenQuestion(String sender, String subject, DateTime date, int id)
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
        DateTime now = new DateTime();
        DateTime my_date = new DateTime(getDate());
        return Utils.formatDateAnswer( my_date, now );
    }

    @Override
    public Type getType() {
        return Type.FromWhen;
    }
}
