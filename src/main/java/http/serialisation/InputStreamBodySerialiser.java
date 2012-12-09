package http.serialisation;

import http.header.ContentType;

import java.io.InputStream;

/**
 * An abstract class that sets the input type for any child deserialisers to be an {@link InputStream}.
 *
 * @param <C> the type of {@code Content-Type} the {@code BodyDeserialiser} produces. This should be set using a typed
 *            {@link http.Header} like {@link http.header.JsonContentType} or {@link http.header.XmlContentType}.
 *
 * @author Karl Bennett
 */
public abstract class InputStreamBodySerialiser<C extends ContentType> extends BodySerialiser<InputStream, C> {

    protected InputStreamBodySerialiser(Class<C> contentType) {
        super(InputStream.class, contentType);
    }
}