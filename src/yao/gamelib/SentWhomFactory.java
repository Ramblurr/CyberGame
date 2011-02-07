package yao.gamelib;

public class SentWhomFactory extends EmailQuestionFactory {
    
    public SentWhomFactory(EmailStore store) {
        super(store);
    }

    public Question makeQuestion() {
        SentWhomQuestion q = new SentWhomQuestion();
        
        return setEmailDataSent(q);
    }

}
