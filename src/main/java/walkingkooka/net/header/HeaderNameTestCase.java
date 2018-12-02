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

import org.junit.Test;
import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

public abstract class HeaderNameTestCase<N extends HeaderName<?>> extends NameTestCase<N> {

    // parameterValue...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testToValueNullFails() {
        this.createName().toValue(null);
    }

    protected <T> void toValueAndCheck(final HeaderName<T> name,
                                       final String headerValue,
                                       final T value) {
        assertEquals(name + "=" + CharSequences.quoteIfNecessary(headerValue),
                value,
                name.toValue(headerValue));
    }

    // checkValue...........................................................................................

    @Test(expected = NullPointerException.class)
    public void testCheckValueNullFails() {
        this.checkValue(null);
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckValueInvalidTypeFails() {
        this.checkValue(this);
    }

    protected void checkValue(final Object value) {
        this.createName().checkValue(value);
    }

    protected void checkValue(final HeaderName<?> name,
                              final Object value) {
        name.checkValue(value);
    }

    protected final N createName() {
        return this.createName(this.nameText());
    }

    protected abstract String nameText();
}
