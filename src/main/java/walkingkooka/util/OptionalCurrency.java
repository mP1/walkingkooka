package walkingkooka.util;

import walkingkooka.Cast;
import walkingkooka.Value;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;

/**
 * A typed {@link Optional} necessary because generic types are lost in java.
 * This class is intended to be a target when converting a cell to a {@link Currency} to patch cells.
 */
public final class OptionalCurrency implements Value<Optional<Currency>> {

    public final static OptionalCurrency EMPTY = new OptionalCurrency(Optional.empty());

    public static OptionalCurrency with(final Optional<Currency> value) {
        Objects.requireNonNull(value, "value");

        return value.isPresent() ?
            new OptionalCurrency(value) :
            EMPTY;
    }

    private OptionalCurrency(final Optional<Currency> value) {
        this.value = value;
    }

    @Override
    public Optional<Currency> value() {
        return this.value;
    }

    private final Optional<Currency> value;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof OptionalCurrency &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final OptionalCurrency other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.value.map(Object::toString)
            .orElse("");
    }
}
