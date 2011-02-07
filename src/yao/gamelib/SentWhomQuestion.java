package yao.gamelib;

public class SentWhomQuestion extends EmailQuestion {
    private String mQuestionFormat = "To whom did you send the email on '%s' with subject '%s'";
    
    public SentWhomQuestion()
    {
    }
    
    public SentWhomQuestion(String sender, String subject, String date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    public String getQuestion() {
        return String.format(mQuestionFormat, mDate, mSubject);
    }
    
    public String getAnswer() {
        return mSender;
    }

    public Type getType() {
        return Type.SentWhom;
    }
}
