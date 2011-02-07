package yao.gamelib;

public class SentWhenQuestion extends EmailQuestion {
    private String mQuestionFormat = "When did you send the email to '%s' with subject '%s'";
    
    public SentWhenQuestion()
    {
    }
    
    public SentWhenQuestion(String sender, String subject, String date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    public String getQuestion() {
        return String.format(mQuestionFormat, mSender, mSubject);
    }
    
    public String getAnswer() {
        return mDate;
    }

    public Type getType() {
        return Type.SentWhen;
    }
}
