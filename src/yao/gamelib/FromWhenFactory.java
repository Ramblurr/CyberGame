package yao.gamelib;
//QuestionGenerator.registerType(Question.Type.FromWhen, new FromWhenFactory() );

/**
 * Creates FromWhen Questions
 */
public class FromWhenFactory implements QuestionFactory {
    public Question makeQuestion() {
        FromWhenQuestion q = new FromWhenQuestion();
        return q;
    }

}
