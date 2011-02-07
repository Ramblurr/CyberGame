package yao.gamelib;

public class FromWhomFactory extends EmailQuestionFactory {

    public FromWhomFactory(EmailStore store) {
        super(store);
    }

    public Question makeQuestion() {
        FromWhomQuestion q = new FromWhomQuestion();
        return setEmailDataInbox(q);
    }

}
