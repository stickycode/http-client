package http;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Karl Bennett
 */
public abstract class AbstractMessageAttributeTest<M, A extends Attribute> extends AbstractMessagePropertyTest<M, A> {

    private PropertyExecutor<A> propertyExecutor;
    private MessageExecutor<M, A> messageExecutor;
    private A attributeOne;
    private A attributeTwo;
    private A attributeThree;
    private Collection<A> attributes;


    protected AbstractMessageAttributeTest(PropertyExecutor<A> propertyExecutor, MessageExecutor<M, A> messageExecutor) {
        super(propertyExecutor, messageExecutor);

        this.propertyExecutor = propertyExecutor;
        this.messageExecutor = messageExecutor;
    }


    @Override
    public void exposeProperties(A propertyOne, A propertyTwo, A propertyThree, Collection<A> properties) {
        super.exposeProperties(propertyOne, propertyTwo, propertyThree, properties);

        this.attributeOne = propertyOne;
        this.attributeTwo = propertyTwo;
        this.attributeThree = propertyThree;
        this.attributes = properties;
    }


    @Test
    public void testAddValueToExistingAttribute() throws Exception {

        M message = messageExecutor.newMessage();

        messageExecutor.setProperties(message, attributes);

        messageExecutor.addProperty(message, propertyExecutor.newProperty(attributeOne.getName(),
                attributeOne.getValue()));

        assertEquals("three attributes should exist when a value has been added to attribute one", 3,
                messageExecutor.getProperties(message).size());
        assertEquals("two values should exist when a value has been added to attribute one", 2,
                messageExecutor.getProperty(message, attributeOne.getName()).getValues().size());
        assertEquals("attribute one should have value one.", attributeOne.getValue(),
                messageExecutor.getProperty(message, attributeOne.getName()).getValue());
        assertEquals("attribute one should have value two.", attributeTwo.getValue(),
                messageExecutor.getProperty(message, attributeOne.getName()).getValues().get(1));
    }

    @Test
    public void testAddEmptyValueToExistingAttribute() throws Exception {

        addBlankAttributeValueTest("");
    }

    @Test
    public void testAddHeaderWithNameAndNullValue() throws Exception {

        addBlankAttributeValueTest(null);
    }


    private void addBlankAttributeValueTest(Object blank) {

        M message = messageExecutor.newMessage();

        A attribute = propertyExecutor.newProperty(attributeOne.getName(), blank);

        messageExecutor.addProperty(message, attribute);

        assertEquals("attribute one should have one value", 1,
                messageExecutor.getProperty(message, attributeOne.getName()).getValues().size());

        messageExecutor.addProperty(message, attribute);

        assertEquals("attribute one should still only have have one value", 1,
                messageExecutor.getProperty(message, attributeOne.getName()).getValues().size());
    }
}