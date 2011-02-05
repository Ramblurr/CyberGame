package yao.gamelib;

public class FromWhenQuestion extends EmailQuestion {
    private String mQuestionFormat = "When did you receive the email from '%s' with subject '%s'";
        
    public FromWhenQuestion()
    {
    }
    
    public FromWhenQuestion(String sender, String subject, String date, int id)
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
        return Type.FromWhen;
    }
}
