package yao.gameweb;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

public class GameApplication extends Application {

    public static final String ROOT_URI = "file:///home/ramblurr/src/workspace/CyberGame/res";

    public static final String SESSIONKEY = "session";
    public GameApplication() {
//        super(context);
   }
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(this.getContext());

        router.attach("/login", LoginResource.class);
        router.attach("/quiz", QuizResource.class);
        router.attach("/quiz/answer", QuizResource.class);

        // handles static content: css, images, etc
        router.attach("/res", new Directory(getContext(), ROOT_URI) );
        router.attach("/", RootResource.class);

        return router;
       }

}
