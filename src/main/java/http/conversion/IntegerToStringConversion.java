package http.conversion;

/**
 * Conversion that can be used to convert a {@link Integer} into a {@link String}.
 *
 * @author Karl Bennett
 */
public class IntegerToStringConversion extends Conversion<Integer, String> {

    /**
     * Create a new {@code IntegerToStringConversion}.
     */
    public IntegerToStringConversion() {
        super(Integer.class, String.class);
    }

    /**
     * Convert a {@code Integer} into a {@code String}.
     *
     * @param input the {@code Integer} that will be converted.
     * @return the {@code String} representation of the {@code Integer}.
     */
    @Override
    public String convert(Integer input) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
