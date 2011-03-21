package yao.gameweb;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;

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

            pageData.put("username", username);

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

}
