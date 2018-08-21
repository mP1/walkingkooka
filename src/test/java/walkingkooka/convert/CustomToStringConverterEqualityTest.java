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

package walkingkooka.convert;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class CustomToStringConverterEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<CustomToStringConverter> {

    final private static Converter WRAPPED = Converters.string();
    final private static String CUSTOM_TOSTRING = "custom-to-string";

    @Test
    public void testDifferentWrappedConverter() {
        this.checkNotEquals(CustomToStringConverter.wrap(Converters.stringBoolean(), CUSTOM_TOSTRING));
    }

    @Test
    public void testDifferentCustomToString() {
        this.checkNotEquals(CustomToStringConverter.wrap(WRAPPED, "different"));
    }

    // helpers

    @Override
    protected CustomToStringConverter createObject() {
        return Cast.to(CustomToStringConverter.wrap(WRAPPED, CUSTOM_TOSTRING));
    }
}
