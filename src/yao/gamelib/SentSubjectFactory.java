package yao.gamelib;

public class SentSubjectFactory extends EmailQuestionFactory {

    public SentSubjectFactory(EmailStore store) {
        super(store);
    }

    public Question makeQuestion() {
        SentSubjectQuestion q = new SentSubjectQuestion();
        
        return setEmailDataSent(q);
    }
}
