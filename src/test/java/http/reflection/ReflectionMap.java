package http.reflection;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A map that will contain all the requested reflective members from a supplied {@link Class} mapped to their respective
 * name. Static member keys contain the {@link Class#getName()} prefixed and delimited with a dot ('.').
 * <p/>
 * This map will be end up populated with all the members from the supplied type and all it's inherited classes and
 * interfaces. Regardless of whether a {@code Class#get*} or {@code Class#getDeclared*} method is in the
 * {@link PropertiesInvoker}.
 * <p/>
 * When there is an instance member name clash the member at the lowest inheritance level will take precedence.
 * <p/>
 * Example:
 * <code>
 *      interface TestInterface {
 * <p/>
 *           public static final String TEST = "Test";
 *           public static final int ZERO = 0;
 * <p/>
 *           String getTest();
 *      }
 * <p/>
 *      class TestOne implements TestInterface {
 * <p/>
 *           public static final String TEST = "Test One";
 *           public static final int ONE = 1;
 * <p/>
 *           private String test;
 * <p/>
 *           TestOne(String test) {
 *                this.test = test;
 *           }
 *
 *           @Override public String getTest() {
 *                return test;
 *           }
 *      }
 * <p/>
 *      class TestTwo extends TestOne {
 *           public static final String TEST = "Test Two";
 *           public static final int TWO = 2;
 * <p/>
 *           private String test;
 * <p/>
 *           TestTwo(String test) {
 *                super(test);
 *                this.test = test;
 *           }
 * <p/>
 *           public String getTest() {
 *                return test;
 *           }
 *      }
 * <p/>
 *      Map<String, Field> fieldMap = new ReflectionMap<Field>(new ReflectionMap.PropertiesInvoker<Field>() {
 *           @Override public Field[] invoke(Class type) {
 *                return type.getDeclaredFields();
 *           }
 *      }, TestTwo.class);
 * <p/>
 *      // fieldMap {
 *      //     "test.TestInterface.TEST" => Field(test.TestInterface.TEST),
 *      //     "test.TestOne.TEST" => Field(test.TestOne.TEST),
 *      //     "test.TestTwo.TEST" => Field(test.TestTwo.TEST),
 *      //     "test.TestInterface.ZERO" => Field(test.TestInterface.ZERO),
 *      //     "test.TestOne.ONE" => Field(test.TestOne.ONE),
 *      //     "test.TestTwo.TWO" => Field(test.TestTwo.TWO),
 *      //     "test" => Field(test.TestTwo.test)
 *      // }
 * <p/>
 *      Map<String, Method> methodMap = new ReflectionMap<Method>(new ReflectionMap.PropertiesInvoker<Method>() {
 *           @Override
 *           public Method[] invoke(Class type) {
 *                return type.getDeclaredMethods();
 *           }
 *      }, TestTwo.class);
 * <p/>
 *      // methodMap {
 *      //     "getTest" => Field(test.TestTwo.getTest())
 *      // }
 * </code>
 *
 * @author Karl Bennett
 */
public class ReflectionMap<R extends Member> extends AbstractMap<String, R> {

    /**
     * Check if the supplied type has any super classes. This will fail if the types super class is either
     * {@link Object} or {@code null}.
     *
     * @param type the class type to check.
     * @return {@code true} if the class has a super class other than {@code Object} or {@code null}, otherwise
     *         {@code false}.
     */
    public static boolean hasNoSuperClass(Class type) {

        return null == type.getSuperclass() || Object.class.equals(type.getSuperclass());
    }

    /**
     * An invoker interface that is used decouple the call to reflection methods
     * ({@link Class#getFields()}, {@link Class#getDeclaredFields()}, {@link Class#getMethods()}...) from any
     * surrounding logic.
     *
     * @param <R> the type of reflective property class that will be produced by the invoker e.g.
     *            ({@link java.lang.reflect.Field}, {@link java.lang.reflect.Method},
     *            {@link java.lang.reflect.Constructor}...)
     */
    public static interface PropertiesInvoker<R extends Member> {

        public R[] invoke(Class type);

    }


    private PropertiesInvoker<R> invoker;
    private Class type;
    private Set<Entry<String, R>> entries;


    /**
     * Create a new {@code ReflectionMap} with the supplied invoker and type.
     *
     * @param invoker the invoker that will carry out the reflective member request e.g. {@link Class#getFields()},
     *                {@link Class#getDeclaredFields()}, {@link Class#getMethods()}...
     * @param type    the class type that will have it's reflective members extracted.
     */
    public ReflectionMap(PropertiesInvoker<R> invoker, Class type) {

        this.invoker = invoker;
        this.type = type;

        entries = extractClassReflectiveObjects(type);
    }

    @Override
    public Set<Entry<String, R>> entrySet() {

        return entries;
    }

    /**
     * Extract all the reflective members from the supplied class using the invoker and then add them to a {@link Set}
     * as {@link Entry}s containing with the member name as the key and the member as the value. If the member is static
     * the members declaring {@link Class#getName()} will be prefixed and delimited with a dot ('.').
     *
     * @param type the class type that will have it's reflective members extracted.
     * @return a {@code Set} contain reflective member {@code Entry}s.
     */
    private Set<Entry<String, R>> extractReflectiveObjects(Class type) {

        Set<Entry<String, R>> entries = new HashSet<Entry<String, R>>();

        for (final R reflectiveObject : invoker.invoke(type)) {

            entries.add(new Entry<String, R>() {

                private String key = Modifier.isStatic(reflectiveObject.getModifiers()) ?
                        reflectiveObject.getDeclaringClass().getName() + "." + reflectiveObject.getName() :
                        reflectiveObject.getName();
                private R value = reflectiveObject;

                @Override
                public String getKey() {

                    return key;
                }

                @Override
                public R getValue() {

                    return value;
                }

                @Override
                public R setValue(R r) {

                    return value = r;
                }

                @Override
                public boolean equals(Object o) {

                    if (this == o) return true;

                    if (o == null || getClass() != o.getClass()) return false;

                    Entry myEntry = (Entry) o;

                    if (!key.equals(myEntry.getKey())) return false;

                    return true;
                }

                @Override
                public int hashCode() {

                    return key.hashCode();
                }
            });
        }

        return entries;
    }

    /**
     * Extract all the reflected members using the {@link ReflectionMap}'s invoker from the supplied type. This method
     * will extract private and public members from the type and all it's parent classes.
     *
     * @param type
     * @return
     */
    private Set<Entry<String, R>> extractClassReflectiveObjects(Class type) {

        Set<Entry<String, R>> entries = new HashSet<Entry<String, R>>();

        for (Class interfaceType : type.getInterfaces()) {

            entries.addAll(extractReflectiveObjects(interfaceType));
        }

        entries.addAll(extractReflectiveObjects(type));

        if (hasNoSuperClass(type)) return entries;

        entries.addAll(extractClassReflectiveObjects(type.getSuperclass()));

        return entries;
    }
}