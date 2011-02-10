package yao.gameweb.util;

import java.io.IOException;

import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateUtil {
    private static Configuration templateConfig = new Configuration();
    
    private static class TemplateUtilHolder {
        private static final TemplateUtil INSTANCE = new TemplateUtil();
    }
    
    public static TemplateUtil getInstance() {
        return TemplateUtilHolder.INSTANCE;
    }
    
    private TemplateUtil() {
        templateConfig.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "templates/"));
        BeansWrapper bw = new BeansWrapper();
        bw.setExposeFields(true);
        templateConfig.setObjectWrapper(bw);
    }
        
    public Template getTemplate(String tname) throws IOException {
        return templateConfig.getTemplate(tname);
    }
    
}
