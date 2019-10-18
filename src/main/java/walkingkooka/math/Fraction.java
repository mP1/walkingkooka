/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.math;

import java.math.BigInteger;
import java.util.Objects;

/**
 * A fraction holds a numerator and non zero numerator.
 */
public final class Fraction {

    /**
     * Factory that creates a new {@link Fraction}
     */
    public static Fraction with(final BigInteger numerator, final BigInteger denominator) {
        Objects.requireNonNull(numerator, "numerator");
        Objects.requireNonNull(denominator, "denominator");
        if (denominator.signum() == 0) {
            throw new IllegalArgumentException("Denominator must not be zero");
        }

        return new Fraction(numerator, denominator);
    }

    /**
     * Private ctor use factory.
     */
    private Fraction(final BigInteger numerator, final BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public BigInteger numerator() {
        return this.numerator;
    }

    private final BigInteger numerator;

    public BigInteger denominator() {
        return this.denominator;
    }

    private final BigInteger denominator;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.numerator, this.denominator);
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof Fraction)
                && this.equals0((Fraction) other));
    }

    private boolean equals0(final Fraction other) {
        return this.numerator.equals(other.numerator) &&
                this.denominator.equals(other.denominator);
    }

    @Override
    public String toString() {
        return this.numerator + "/" + this.denominator;
    }
}
