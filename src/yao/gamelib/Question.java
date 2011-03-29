package yao.gamelib;

public interface Question {
    public enum Type {
        FromWhom, FromSubject, FromWhen, SentWhen, SentSubject, SentWhom
    }

    public enum NoneAboveType {
        Normal, WithRealAnswer, WithoutRealAnswer;
    }

    public int getId();
    public String getQuestion();
    public String getAnswer();
    public String[] getFakeAnswers();
    public Type getType();
    public NoneAboveType getNoneOfTheAbove();

}
