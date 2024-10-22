package dat;

import dat.config.ApplicationConfig;
import dat.config.Populate;
import dat.controllers.impl.ExceptionController;
import dat.exceptions.ApiException;
import dat.exceptions.Message;
import dat.routes.Routes;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        //Populate.main(args);
        ApplicationConfig.startServer(7070);
    }
//f
    //adad
    //test
}