package yao.gamelib;

public interface Question {
    public enum Type {
        FromWhom, FromSubject, FromWhen, SentWhen, SentSubject, SentWhom
    }

    public int getId();
    public String getQuestion();
    public String getAnswer();
    public Type getType();
    
}
