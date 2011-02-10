package yao.gameweb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.restlet.Restlet;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource ;
import org.restlet.routing.Router;
import freemarker.template.Template;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;

import yao.gameweb.util.TemplateUtil;

public class RootResource extends ServerResource  {

    public RootResource() {
    }
    
    @Get  
    public Representation  get() {
        try {
            Map<String, Object> pageData = new HashMap<String, Object>();
        
        
            Template thtml = TemplateUtil.getInstance().getTemplate("index-html.ftl");
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
