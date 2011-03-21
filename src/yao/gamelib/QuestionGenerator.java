package yao.gamelib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * QuestionGenerator is a convenience class used to generate questions from a dataset.
 * It encapsulates the factories used to create questions.
 * @author Casey
 *
 */
public class QuestionGenerator {
    private final int NUM_FAKE_ANSWERS = 4;
    private final Map<Question.Type, QuestionFactory> mMap = new HashMap<Question.Type, QuestionFactory>();
    
    public void registerType(Question.Type quesType, QuestionFactory factory) {
        mMap.put(quesType, factory);
    }
    
    public int getRegisteredTypesCount() {
        return mMap.size();
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

    /**
     * Generates a given number of questions with an even split among all the registered types. Sometimes the requested number of questions cannot be generated, due to there not being enough data, so the size of the returned array might be smaller than the requested size.
     * 
     * @param total the total number of questions to generate
     * @return a shuffled array of questions
     */
    public Question[] createQuestionsEven(int total) {
        if( total <= 0 ) 
            return null;
        ArrayList<Question> questions = new ArrayList<Question>();

        int total_types = getRegisteredTypesCount();
        assert total_types > 0;
        int questions_per_type = total / total_types;
        assert questions_per_type > 0;

        Collection<QuestionFactory> values = mMap.values();
        Iterator<QuestionFactory> it = values.iterator();
        
        while (it.hasNext() && questions.size() < total) {
            QuestionFactory factory = it.next();
            Question question = null;
            if( factory != null ){
                int iteration_limit = 100;
                int curr_iteration = 0;
                int num_curr_type = 0;
                while (curr_iteration < iteration_limit && num_curr_type <= questions_per_type) {
                    question = factory.makeQuestion();
                    if (question != null && question.getFakeAnswers().length == NUM_FAKE_ANSWERS) {
                        questions.add(question);
                        ++num_curr_type;
                    }
                    ++curr_iteration;
                }
            }
        }
        Question[] q_arr = new Question[questions.size()];
        q_arr = questions.toArray(q_arr);
        return q_arr;
    }

}
