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

package walkingkooka.net.header;

import walkingkooka.naming.Name;

/**
 * A {@link HeaderValueConverter} that parses a header text into a q weights and verifies the value is within the
 * accept range of 0.0 and 1.0 (inclusive).
 */
final class QWeightHeaderValueConverter extends NonStringHeaderValueConverter<Float> {

    /**
     * Singleton
     */
    final static QWeightHeaderValueConverter INSTANCE = new QWeightHeaderValueConverter();

    /**
     * Private ctor use singleton.
     */
    private QWeightHeaderValueConverter() {
        super();
    }

    @Override
    Float parse0(final String text, final Name name) {
        return this.checkValue(Float.parseFloat(text.trim()));
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkValue(this.checkType(value, Float.class, name));
    }

    // https://restfulapi.net/q-parameter-in-http-accept-header/
    private float checkValue(final float value) {
        if (value < 0 || value > 1.0) {
            throw new HeaderValueException("Q weight " + value + " must be bewteen 0.0 and 1.0");
        }
        return value;
    }

    @Override
    String toText0(final Float value, final Name name) {
        return value.toString();
    }

    @Override
    public String toString() {
        return "QWeight";
    }
}
