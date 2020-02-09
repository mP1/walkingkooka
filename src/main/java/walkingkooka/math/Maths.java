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

import walkingkooka.NeverError;
import walkingkooka.reflect.PublicStaticHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

public final class Maths implements PublicStaticHelper {

    /**
     * Attempts to convert the given {@link Number number} to a {@link BigDecimal}.
     */
    public static Optional<BigDecimal> toBigDecimal(final Number value) {
        return MathsToBigDecimalNumberVisitor.toBigDecimal(value);
    }

    /**
     * Converts a {@link RoundingMode} to equivalent {@link BigDecimal} rounding constant.
     */
    public static int toBigDecimalRoundingMode(final RoundingMode mode) {
        Objects.requireNonNull(mode, "RoundingMode");

        final int bigDecimal;

        switch (mode) {
            case CEILING:
                bigDecimal = BigDecimal.ROUND_CEILING;
                break;
            case DOWN:
                bigDecimal = BigDecimal.ROUND_DOWN;
                break;
            case FLOOR:
                bigDecimal = BigDecimal.ROUND_FLOOR;
                break;
            case HALF_DOWN:
                bigDecimal = BigDecimal.ROUND_HALF_DOWN;
                break;
            case HALF_EVEN:
                bigDecimal = BigDecimal.ROUND_HALF_EVEN;
                break;
            case HALF_UP:
                bigDecimal = BigDecimal.ROUND_HALF_UP;
                break;
            case UNNECESSARY:
                bigDecimal = BigDecimal.ROUND_UNNECESSARY;
                break;
            case UP:
                bigDecimal = BigDecimal.ROUND_UP;
                break;
            default:
                bigDecimal = NeverError.unhandledCase(mode, RoundingMode.values());
                break;
        }

        return bigDecimal;
    }

    /**
     * Stop creation
     */
    private Maths() {
        throw new UnsupportedOperationException();
    }
}
