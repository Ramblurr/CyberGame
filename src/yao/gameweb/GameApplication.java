package yao.gameweb;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

public class GameApplication extends Application {

    public static final String ROOT_URI = "file://C:/Users/Casey/Documents/workspace/Restlet/res/";
    
    public static final String SESSIONKEY = "session";
    public GameApplication() {
//        super(context);
   }
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(this.getContext());

        // handles the index page
        router.attach("/login", LoginResource.class);
        router.attach("/quiz", QuizResource.class);

        // handles static content: css, images, etc
        router.attach("/res", new Directory(getContext(), ROOT_URI) );
        router.attach("/", RootResource.class);

//        router.attach("/race/{race_id}", RaceResource.class);
//        router.attach("/race/{race_id}/runner", RaceRunnersResource.class);
//        router.attach("/race/{race_id}/runner/{runner_id}", RaceRunnerResource.class);
        return router;
       }

}
