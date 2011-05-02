package yao.gamelib;

import org.joda.time.DateTime;

public abstract class EmailQuestion implements Question {

    protected String mSender;
    protected String mSubject;
    protected DateTime mDate;
    protected String[] mFakeAnswers;
    protected NoneAboveType mNoneOfTheAbove = Question.NoneAboveType.Normal;
    protected int mId = -1;

    public EmailQuestion()
    {
    }

    public EmailQuestion(String sender, String subject, DateTime date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    @Override
    public int getId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    abstract public String getQuestion();

    @Override
    abstract public String getAnswer();

    @Override
    abstract public Type getType();

    public DateTime getDate() {
        return mDate;
    }

    public void setSender(String sender){
        mSender = sender;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }
    public void setDate(DateTime date) {
        mDate = date;
    }
    public void setId(int id) {
        mId = id;
    }
    public void setFakeAnswers(String[] answers) {
        mFakeAnswers = answers;
    }
    @Override
    public String[] getFakeAnswers() {
        return mFakeAnswers;
    }

    @Override
    public NoneAboveType getNoneOfTheAbove() {
        return mNoneOfTheAbove;
    }

    public void setNoneOfTheAbove(NoneAboveType flag) {
        mNoneOfTheAbove = flag;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mDate == null) ? 0 : mDate.hashCode());
        result = prime * result + ((mSender == null) ? 0 : mSender.hashCode());
        result = prime * result + ((mSubject == null) ? 0 : mSubject.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmailQuestion other = (EmailQuestion) obj;
        if(mId != -1 && other.getId() != -1) {
            if( mId == other.getId() ){
                return true;
            }
            // else go on and compare the rest of the fields
        }
        if (mDate == null) {
            if (other.mDate != null)
                return false;
        } else if (!mDate.equals(other.mDate))
            return false;
        if (mSender == null) {
            if (other.mSender != null)
                return false;
        } else if (!mSender.equals(other.mSender))
            return false;
        if (mSubject == null) {
            if (other.mSubject != null)
                return false;
        } else if (!mSubject.equals(other.mSubject))
            return false;
        if (getAnswer() == null) {
            if (other.getAnswer() != null)
                return false;
        } else if (!getAnswer().equals(other.getAnswer()))
            return false;
        return true;
    }

}
