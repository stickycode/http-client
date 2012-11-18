package http;

import java.net.URL;

import static http.Client.*;
import static http.RequestExecutor.METHOD_TYPE.INSTANCE;

/**
 * @author Karl Bennett
 */
public class ClientInstanceConnectTest extends AbstractClientRequestMethodTest {

    public ClientInstanceConnectTest() {
        super(CONNECT, INSTANCE,
                new RequestExecutor<String>() {

                    @Override
                    public Response execute(String input) {

                        return new Client().connect(input);
                    }
                },
                new RequestExecutor<URL>() {

                    @Override
                    public Response execute(URL input) {

                        return new Client().connect(input);
                    }
                },
                new RequestExecutor<Request>() {

                    @Override
                    public Response execute(Request input) {

                        return new Client().connect(input);
                    }
                }
        );
    }
}
