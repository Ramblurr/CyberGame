package yao.gamelib;

public abstract class EmailQuestion implements Question {
   
    protected String mSender; 
    protected String mSubject;
    protected String mDate;
    protected String[] mFakeAnswers;
    protected int mId;
    
    public EmailQuestion()
    {
    }
    
    public EmailQuestion(String sender, String subject, String date, int id)
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

    abstract public String getQuestion();
    
    abstract public String getAnswer();

    abstract public Type getType();
    
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
    public void setFakeAnswers(String[] answers) {
        mFakeAnswers = answers;
    }
    public String[] getFakeAnswers() {
        return mFakeAnswers;
    }
}
