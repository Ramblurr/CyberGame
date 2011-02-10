package yao.gameweb;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.CookieSetting;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class GameServer extends ServerResource {
    
    public static void main(String[] args) throws Exception {  
        // Create a new Restlet component and add a HTTP server connector to it  
        Component component = new Component();  
        
        component.getServers().add(Protocol.HTTP, 8182);  
        component.getClients().add(Protocol.FILE);
     
        
        GameApplication app = new GameApplication();
        component.getDefaultHost().attach(app);
      
        // Now, let's start the component!  
        // Note that the HTTP server connector is also automatically started.  
        component.start();  
    }  
}
