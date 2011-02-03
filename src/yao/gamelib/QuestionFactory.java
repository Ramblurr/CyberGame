package yao.gamelib;

/** 
 * Factory method pattern
 * Encapsulates the algorithm used to generate the different types of questions.
 * @author Casey
 *
 */
public interface QuestionFactory {
    
    public Question makeQuestion(); 

}
