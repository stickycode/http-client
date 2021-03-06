package http;

import java.net.URL;

import static http.AbstractClientRequestMethodTest.METHOD_TYPE.INSTANCE;
import static http.Client.TRACE;

/**
 * @author Karl Bennett
 */
public class ClientInstanceTraceTest extends AbstractClientRequestMethodTest {

    public ClientInstanceTraceTest() {
        super(TRACE, INSTANCE,
                new RequestExecutor<String>() {

                    @Override
                    public Response execute(String input) {

                        return new Client().trace(input);
                    }
                },
                new RequestExecutor<URL>() {

                    @Override
                    public Response execute(URL input) {

                        return new Client().trace(input);
                    }
                },
                new RequestExecutor<Request>() {

                    @Override
                    public Response execute(Request input) {

                        return new Client().trace(input);
                    }
                }
        );
    }
}
