package http;

import java.net.URL;

import static http.AbstractClientRequestMethodTest.METHOD_TYPE.STATIC;
import static http.Client.PUT;

/**
 * @author Karl Bennett
 */
public class ClientStaticPutTest extends AbstractClientRequestMethodTest {

    public ClientStaticPutTest() {
        super(PUT, STATIC,
                new RequestExecutor<String>() {

                    @Override
                    public Response execute(String input) {

                        return PUT(input);
                    }
                },
                new RequestExecutor<URL>() {

                    @Override
                    public Response execute(URL input) {

                        return PUT(input);
                    }
                },
                new RequestExecutor<Request>() {

                    @Override
                    public Response execute(Request input) {

                        return PUT(input);
                    }
                }
        );
    }
}
