package yao.gamelib;

import yao.gamelib.Question.Type;

public class FromSubjectQuestion extends EmailQuestion {
    private String mQuestionFormat = "What is the subject of the email from '%s' on '%s'";
    
    public FromSubjectQuestion()
    {
    }
    
    public FromSubjectQuestion(String sender, String subject, String date, int id)
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
        return Type.FromSubject;
    }

}
