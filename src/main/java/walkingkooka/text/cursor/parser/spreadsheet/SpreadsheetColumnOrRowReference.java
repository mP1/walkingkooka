/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
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
 *
 */

package walkingkooka.text.cursor.parser.spreadsheet;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Objects;

/**
 * Captures the common features shared by a row or column.
 */
abstract class SpreadsheetColumnOrRowReference implements Value<Integer>, HashCodeEqualsDefined {

    SpreadsheetColumnOrRowReference(final int value, final SpreadsheetReferenceKind referenceKind) {
        this.value = value;
        this.referenceKind = referenceKind;
    }

    @Override
    public final Integer value() {
        return this.value;
    }

    final int value;

    public final SpreadsheetReferenceKind referenceKind() {
        return this.referenceKind;
    }

    private final SpreadsheetReferenceKind referenceKind;

    @Override
    public final int hashCode() {
        return Objects.hash(this.value, this.referenceKind);
    }

    public boolean equals(final Object other) {
        return this == other ||
               this.canBeEqual(other) &&
               this.equals0(Cast.to(other));
    }

    abstract boolean canBeEqual(Object other);

    private boolean equals0(final SpreadsheetColumnOrRowReference other) {
        return this.value == other.value &&
               this.referenceKind == other.referenceKind;
    }

    @Override
    abstract public String toString();
}
