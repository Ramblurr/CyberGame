package yao.gamelib;

public class StoredQuestion implements Question {
    int mId;
    String mQuestion;
    String mAnswer;
    String[] mFakeAnswers;
    Question.Type mType;
    StoredQuestion() {}
    
    public StoredQuestion(int id, String question, String answer,
            String[] fakeAnswers, Type type) {
        super();
        this.mId = id;
        this.mQuestion = question;
        this.mAnswer = answer;
        this.mFakeAnswers = fakeAnswers;
        this.mType = type;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public String getQuestion() {
        return mQuestion;
    }

    @Override
    public String getAnswer() {
        return mAnswer;
    }

    @Override
    public String[] getFakeAnswers() {
        return mFakeAnswers;
    }

    @Override
    public Type getType() {
        return mType;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setQuestion(String question) {
        this.mQuestion = question;
    }

    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }

    public void setFakeAnswers(String[] fakeAnswers) {
        this.mFakeAnswers = fakeAnswers;
    }

    public void setType(Question.Type type) {
        this.mType = type;
    }

}
