
package walkingkooka.util;

import org.junit.jupiter.api.Test;
import walkingkooka.HasValueTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.PublicClassTesting;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OptionalLocaleTest implements PublicClassTesting<OptionalLocale>,
    HashCodeEqualsDefinedTesting2<OptionalLocale>,
    HasLocaleTesting,
    ToStringTesting,
    HasValueTesting {


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
            OptionalLocale.with(OPTIONAL_LOCALE),
            OPTIONAL_LOCALE
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
            OptionalLocale.with(OPTIONAL_DIFFERENT_LOCALE)
        );
    }

    @Override
    public OptionalLocale createObject() {
        return OptionalLocale.with(OPTIONAL_LOCALE);
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
}
