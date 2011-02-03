package yao.gamelib;

public class FromWhenQuestion implements Question {
    private String mQuestionFormat = "When did you receive the email from '%s' with subject '%s'";
    
    private String mSender; 
    private String mSubject;
    private String mDate; // the answer
    private int mId;
    
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

    public int getId() {
        // TODO Auto-generated method stub
        return 0;
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
    
    public void setSender(String sender){
        mSender = sender;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }
    public void setDate(String date) {
        mDate = date;
    }
    public void setId(int id) {
        mId = id;
    }

}
