package yao.gamelib;

public class SentWhenFactory extends EmailQuestionFactory {

    public SentWhenFactory(EmailStore store) {
        super(store);
    }

    public Question makeQuestion() {
        SentWhenQuestion q = new SentWhenQuestion();
        
        return setEmailDataSent(q);
    }
}
