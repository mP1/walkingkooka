package walkingkooka.util;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Currency;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OptionalCurrencyTest implements ClassTesting<OptionalCurrency>,
    HashCodeEqualsDefinedTesting2<OptionalCurrency>,
    ToStringTesting<OptionalCurrency> {

    private final static Optional<Currency> CURRENCY = Optional.of(
        Currency.getInstance("AUD")
    );

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> OptionalCurrency.with(null)
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            OptionalCurrency.with(
                Optional.of(
                    Currency.getInstance("NZD")
                )
            )
        );
    }

    @Override
    public OptionalCurrency createObject() {
        return OptionalCurrency.with(CURRENCY);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "AUD"
        );
    }

    @Test
    public void testToStringWithEmpty() {
        this.toStringAndCheck(
            OptionalCurrency.EMPTY,
            ""
        );
    }

    // class............................................................................................................

    @Override
    public Class<OptionalCurrency> type() {
        return OptionalCurrency.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
