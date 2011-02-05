package yao.gamelib;

import yao.gamelib.Question.Type;

public class FromWhomQuestion extends EmailQuestion {

    private String mQuestionFormat = "From whom did you receive the email with subject '%s' on %s";
    
    public FromWhomQuestion()
    {
    }
    
    public FromWhomQuestion(String sender, String subject, String date, int id)
    {
        mSender = sender;
        mSubject = subject;
        mDate = date;
        mId = id;
    }

    public String getQuestion() {
        return String.format(mQuestionFormat, mSubject, mDate);
    }
    
    public String getAnswer() {
        return mSender;
    }

    public Type getType() {
        return Type.FromWhom;
    }
}
