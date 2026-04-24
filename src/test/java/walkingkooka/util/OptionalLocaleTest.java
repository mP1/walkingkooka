
package walkingkooka.util;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.ValueTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OptionalLocaleTest implements ClassTesting<OptionalLocale>,
    HashCodeEqualsDefinedTesting2<OptionalLocale>,
    ToStringTesting<OptionalLocale>,
    ValueTesting {

    private final static Optional<Locale> LOCALE = Optional.of(
        Locale.forLanguageTag("en-AU")
    );

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> OptionalLocale.with(null)
        );
    }

    // value............................................................................................................

    @Test
    public void testValue() {
        this.valueAndCheck(
            OptionalLocale.with(LOCALE),
            LOCALE
        );
    }

    @Test
    public void testValueWhenEmptyOptional() {
        final Optional<Locale> locale = Optional.empty();

        this.valueAndCheck(
            OptionalLocale.with(locale),
            locale
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            OptionalLocale.with(
                Optional.of(
                    Locale.forLanguageTag("en-NZ")
                )
            )
        );
    }

    @Override
    public OptionalLocale createObject() {
        return OptionalLocale.with(LOCALE);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "en-AU"
        );
    }

    @Test
    public void testToStringWithEmpty() {
        this.toStringAndCheck(
            OptionalLocale.EMPTY,
            ""
        );
    }

    // class............................................................................................................

    @Override
    public Class<OptionalLocale> type() {
        return OptionalLocale.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
