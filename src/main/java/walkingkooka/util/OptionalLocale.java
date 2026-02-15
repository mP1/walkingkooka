package walkingkooka.util;

import walkingkooka.Cast;
import walkingkooka.Value;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * A typed {@link Optional} necessary because generic types are lost in java.
 * This class is intended to be a target when converting a cell to a {@link Locale} to patch cells.
 */
public final class OptionalLocale implements Value<Optional<Locale>> {

    public final static OptionalLocale EMPTY = new OptionalLocale(Optional.empty());

    public static OptionalLocale with(final Optional<Locale> value) {
        Objects.requireNonNull(value, "value");

        return value.isPresent() ?
            new OptionalLocale(value) :
            EMPTY;
    }

    private OptionalLocale(final Optional<Locale> value) {
        this.value = value;
    }

    @Override
    public Optional<Locale> value() {
        return this.value;
    }

    private final Optional<Locale> value;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof OptionalLocale &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final OptionalLocale other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.value.map(Locale::toLanguageTag)
            .orElse("");
    }
}
