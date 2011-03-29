package yao.gamelib;

import yao.gameweb.util.AnswerStub;

public class StoredQuestion implements Question {
    NoneAboveType mNoneOfTheAbove = Question.NoneAboveType.Normal;
    int mId;
    String mQuestion;
    String mAnswer;
    int mAnswerId;
    AnswerStub[] mFakeAnswerStubs;
    Question.Type mType;
    StoredQuestion() {}

    public StoredQuestion(int id, String question, String answer, int realAnswer_id,
 AnswerStub[] fakeAnswers, Type type, NoneAboveType noneOfTheAbove) {
        this.mId = id;
        this.mQuestion = question;
        this.mAnswer = answer;
        this.mFakeAnswerStubs = fakeAnswers;
        this.mType = type;
        this.mAnswerId = realAnswer_id;
        this.mNoneOfTheAbove = noneOfTheAbove;
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

    public int getAnswerId() {
        return mAnswerId;
    }

    @Override
    public String[] getFakeAnswers() {
        String[] texts = new String[mFakeAnswerStubs.length];
        for (int i = 0; i < mFakeAnswerStubs.length; i++) {
            texts[i] = mFakeAnswerStubs[i].text;
        }
        return texts;
    }

    public AnswerStub[] getFakeAnswerStubs() {
        return mFakeAnswerStubs;
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

    public void setType(Question.Type type) {
        this.mType = type;
    }

    @Override
    public NoneAboveType getNoneOfTheAbove()
    {
        return mNoneOfTheAbove;
    }

    public void setNoneOfTheAbove( NoneAboveType flag ) {
        mNoneOfTheAbove = flag;
    }

}
