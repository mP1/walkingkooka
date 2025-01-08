package walkingkooka.collect.enumeration;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Enumeration;
import java.util.Vector;

final public class EnumerationChainTest implements EnumerationTesting,
    ClassTesting<EnumerationChain<String>>,
    ToStringTesting<EnumerationChain<String>> {

    public void testConsume() {
        final Vector<String> first = new Vector<>();
        first.add("1a");
        first.add("2b");

        final Vector<String> second = new Vector<>();
        second.add("3c");
        second.add("4d");

        this.enumerateAndCheck(
            EnumerationChain.with(
                Lists.of(
                    first.elements(),
                    second.elements()
                )
            ),
            "1a",
            "2b",
            "3c",
            "4d"
        );
    }

    public void testConsumeSkipsEmptyEnumerations() {
        final Vector<String> first = new Vector<>();
        first.add("1a");
        first.add("2b");

        final Vector<String> second = new Vector<>();
        second.add("3c");
        second.add("4d");

        this.enumerateAndCheck(
            EnumerationChain.with(
                Lists.of(
                    first.elements(),
                    new Vector<String>().elements(),
                    new Vector<String>().elements(),
                    second.elements()
                )
            ),
            "1a",
            "2b",
            "3c",
            "4d"
        );
    }

    public void testConsumeUsingHasMoreElements() {
        final Vector<String> first = new Vector<>();
        first.add("1a");
        first.add("2b");
        final Vector<String> second = new Vector<>();
        second.add("3c");
        second.add("4d");

        this.enumerateUsingHasMoreElementsAndCheck(
            EnumerationChain.with(
                Lists.of(
                    first.elements(),
                    second.elements()
                )
            ),
            "1a",
            "2b",
            "3c",
            "4d"
        );
    }

    @Test
    public void testToString() {
        final Vector<String> first = new Vector<>();
        first.add("1a");
        first.add("2b");
        final Vector<String> second = new Vector<>();
        second.add("3c");
        second.add("4d");

        final Enumeration<String> enumeration1 = first.elements();

        this.toStringAndCheck(
            EnumerationChain.with(
                Lists.of(
                    enumeration1,
                    second.elements()
                )
            ),
            enumeration1 + "..."
        );
    }

    @Test
    public void testToStringWhenEmpty() {
        final Vector<String> first = Lists.vector();
        final Vector<String> second = Lists.vector();
        second.add("1");

        final EnumerationChain<String> enumerator = EnumerationChain.with(
            Lists.of(
                first.elements(),
                second.elements()
            )
        );
        enumerator.nextElement();
        enumerator.hasMoreElements();

        toStringAndCheck(
            enumerator.toString(),
            ""
        );
    }

    @Override
    public Class<EnumerationChain<String>> type() {
        return Cast.to(EnumerationChain.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
