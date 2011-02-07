package yao.gamelib;


public class SentSubjectQuestion extends EmailQuestion {

    private String mQuestionFormat = "What was the subject of the email you sent to '%s' on '%s'";
    
    public SentSubjectQuestion() {
        
    }
    public SentSubjectQuestion(String sender, String subject, String date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    public String getQuestion() {
        return String.format(mQuestionFormat, mSender, mDate);
    }
    
    public String getAnswer() {
        return mSubject;
    }

    public Type getType() {
        return Type.SentSubject;
    }

}
