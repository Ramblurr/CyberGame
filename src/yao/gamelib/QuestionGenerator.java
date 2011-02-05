package yao.gamelib;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * QuestionGenerator is a convenience class used to generate questions from a dataset.
 * It encapsulates the factories used to create questions.
 * @author Casey
 *
 */
public class QuestionGenerator {
    private Map<Question.Type, QuestionFactory> mMap = new HashMap<Question.Type, QuestionFactory>();
    
    public void registerType(Question.Type quesType, QuestionFactory factory) {
        mMap.put(quesType, factory);
    }
    
    public QuestionGenerator() {
    }
    
    /**
     * Creates a random type of question.
     * @return a question of random type
     */
    public Question createQuestion() {
        Random generator = new Random();
        Object[] values = mMap.values().toArray();
        QuestionFactory randomFactory = (QuestionFactory) values[generator.nextInt(values.length)];
        
        if( randomFactory != null)
            return randomFactory.makeQuestion();
        return null;
    }
    
    /**
     * Creates a question of a certain type
     * @param type the type of question to be generated
     * @return a question of type type
     */
    public Question createQuestionFromType(Question.Type type) {
        QuestionFactory factory = mMap.get(type);
        
        if( factory != null ){
            return factory.makeQuestion();
        }
        return null;
    }

}
