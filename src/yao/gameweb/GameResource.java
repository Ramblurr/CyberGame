package yao.gameweb;

import org.restlet.resource.ServerResource;

import yao.gameweb.util.Database;

public abstract class GameResource extends ServerResource {

    protected String verifySession() {
        String sessionToken = this.getRequest().getCookies().getFirstValue(GameApplication.SESSIONKEY);
        if (sessionToken == null) {
            // user is not authenticated
            getResponse().redirectSeeOther("/login");
            return null;
        } else {
            //verify 
            int userId = Database.getInstance().getUserForSession(sessionToken);
            if (userId == -1) {
                getResponse().redirectSeeOther("/login");
                return null;
            }
            return Database.getInstance().getUsername(userId);
        }
    }
}
