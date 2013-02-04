package http;

import http.header.Header;

import java.util.Collection;

import static http.Cookie.COOKIE;

/**
 * @author Karl Bennett
 */
public class MessageHeaderNameAndValueTest extends AbstractMessageHeaderTest<Message<Object>> {

    public MessageHeaderNameAndValueTest() {

        super(new MessageExecutor<Message<Object>, Header>() {

            @Override
            public Message<Object> newMessage() {

                return new Message<Object>(COOKIE);
            }

            @Override
            public Header getProperty(Message<Object> message, String name) {

                return message.getHeader(name);
            }

            @Override
            public void addProperty(Message<Object> message, Header property) {

                message.addHeader(property.getName(), property.getValue());
            }

            @Override
            public Collection<Header> getProperties(Message<Object> message) {

                return message.getHeaders();
            }

            @Override
            public void setProperties(Message<Object> message, Collection<Header> properties) {

                message.setHeaders(properties);
            }
        }
        );
    }
}
