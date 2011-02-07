package yao.gamelib;

//QuestionGenerator.registerType(Question.Type.FromWhen, new FromWhenFactory() );

/**
 * Creates FromWhen Questions
 */
public class FromWhenFactory extends EmailQuestionFactory {

    public FromWhenFactory(EmailStore store) {
        super(store);
    }

    public Question makeQuestion() {
        FromWhenQuestion q = new FromWhenQuestion();
        
        return setEmailDataInbox(q);
    }

}
