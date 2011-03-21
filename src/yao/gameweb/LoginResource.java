package yao.gameweb;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.restlet.data.CharacterSet;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import yao.gameweb.util.Database;
import yao.gameweb.util.TemplateUtil;
import freemarker.template.Template;

public class LoginResource  extends ServerResource {

    public LoginResource() {
        Set<Method> allowedMethods = new HashSet<Method>();
        allowedMethods.add(Method.POST);
        allowedMethods.add(Method.GET);
        setAllowedMethods(allowedMethods);
    }
    @Override
    @Get  
    public Representation  get() {
        try {
            Map<String, Object> pageData = new HashMap<String, Object>();
            String sessionToken = this.getRequest().getCookies().getFirstValue(GameApplication.SESSIONKEY);
            
            boolean valid = false;
            if (sessionToken != null) {
                int userId = Database.getInstance().getUserForSession(sessionToken);
                if (userId > 0)
                    valid = true;
            }
            
            if (!valid) {
                // user is not authenticated
                Template thtml = TemplateUtil.getInstance().getTemplate("index-html.ftl");
                TemplateRepresentation rep = new TemplateRepresentation(thtml, pageData, MediaType.TEXT_HTML);
                rep.setCharacterSet(CharacterSet.UTF_8);
                return rep;
            } else {
                //redirect to quiz
                getResponse().redirectSeeOther("/quiz");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    @Post  
    public void acceptItem(Representation entity) {
        Representation result = null;
        Representation rep = null;
        Form form = new Form(entity);  
        String username = form.getFirstValue("user-name").trim();

        String existing_sessionToken = this.getRequest().getCookies().getFirstValue(GameApplication.SESSIONKEY);
        if( existing_sessionToken != null) {
            int user_id = Database.getInstance().getUserForSession(existing_sessionToken);
            String stored_user = Database.getInstance().getUsername(user_id);
            if (username.equals(stored_user)) {
                // user is relogging in
                setStatus(Status.SUCCESS_CREATED);
                getResponse().redirectSeeOther("/quiz");
                getResponse().setEntity("already logged in", MediaType.TEXT_PLAIN);
                return;
            } // else the user is logging in as someone else
        }

        if( username.length() == 0 ) {
            setStatus( Status.CLIENT_ERROR_BAD_REQUEST);
            rep = new StringRepresentation("Your username must not be blank.",  
                    MediaType.TEXT_PLAIN);
        } else {
            setStatus(Status.SUCCESS_CREATED);
            
            String new_sessionToken = Database.getInstance().makeSession(username);
            getResponse().getCookieSettings().add(new CookieSetting(GameApplication.SESSIONKEY, 
                    new_sessionToken));
            rep = new StringRepresentation("Your session token is:"+new_sessionToken,  
                MediaType.TEXT_PLAIN);
        }
        result = rep;
        getResponse().redirectSeeOther("/quiz");
    }
    
    
    
}
