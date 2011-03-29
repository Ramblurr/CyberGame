package yao.gameweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import yao.gamelib.StoredQuestion;
import yao.gameweb.util.AnswerStub;
import yao.gameweb.util.Database;
import yao.gameweb.util.QuestionStub;
import yao.gameweb.util.TemplateUtil;
import freemarker.template.Template;

public class QuizResource extends GameResource {

    public QuizResource() {
        Set<Method> allowedMethods = new HashSet<Method>();
        allowedMethods.add(Method.POST);
        allowedMethods.add(Method.GET);
        setAllowedMethods(allowedMethods);
    }

    @Get
    public Representation doGet() {
        try {
            Map<String, Object> pageData = new HashMap<String, Object>();
            String username = verifySession();
            if (username == null)
                return null;

            StoredQuestion[] questions = Database.getInstance().getQuestions(5);
            ArrayList<QuestionStub> questionStubs = new ArrayList<QuestionStub>();
            for (StoredQuestion q : questions) {
                QuestionStub qstub = new QuestionStub();
                qstub.text = q.getQuestion();
                qstub.id = q.getId();
                ArrayList<AnswerStub> answerStubs = new ArrayList<AnswerStub>();
                for (AnswerStub stub : q.getFakeAnswerStubs()) {
                    answerStubs.add(stub);
                }
                AnswerStub stub = new AnswerStub();
                stub.id = q.getAnswerId();
                stub.text = q.getAnswer();
                answerStubs.add(stub);
                qstub.answers = answerStubs;
                questionStubs.add(qstub);
            }
            pageData.put("username", username);
            pageData.put("questions", questionStubs);

            Template thtml = TemplateUtil.getInstance().getTemplate("quiz.ftl");
            TemplateRepresentation rep = new TemplateRepresentation(thtml, pageData, MediaType.TEXT_HTML);
            rep.setCharacterSet(CharacterSet.UTF_8);
            return rep;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Post
    public void acceptItem(Representation entity) {
        System.out.println("WTF HAI");
        Representation result = null;
        Representation rep = null;

//        Map<String, Object> pageData = new HashMap<String, Object>();
        String username = verifySession();
        if (username == null)
            return;

        Form form = new Form(entity);
        String resp_text = "";
        String resp_debug = "";
        for(String name : form.getNames() ) {
            resp_debug += name + "\n";
            if(name.startsWith( "question_" )) {
                String id_str = form.getFirstValue( name );
                int qid = Integer.parseInt( id_str );
                String answer = form.getFirstValue( "questions_"+qid );
                resp_text += "For " + qid + " answer: " + answer + "\n";
            }
        }
        setStatus(Status.SUCCESS_CREATED);
        rep = new StringRepresentation(resp_text + "\n\n\n" + resp_debug,
                MediaType.TEXT_PLAIN);
        result = rep;
    }

}
