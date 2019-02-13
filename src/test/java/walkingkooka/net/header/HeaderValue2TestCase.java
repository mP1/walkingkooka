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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class HeaderValue2TestCase<H extends HeaderValue2<V>, V> extends HeaderValueTestCase<H> {

    HeaderValue2TestCase() {
        super();
    }

    @Test
    public final void testWith() {
        final V value = this.value();
        final H headerValue = this.createHeaderValue(value);
        assertEquals(value, headerValue.value(), "value");
    }

    @Test
    public void testWith2() {
        final V value = this.differentValue();
        final H headerValue = this.createHeaderValue(value);
        assertEquals(value, headerValue.value(), "value");
    }

    @Test
    public final void testEqualsDifferentValue() {
        this.checkNotEquals(this.createHeaderValue(this.differentValue()));
    }

    @Override
    public final H createHeaderValue() {
        return this.createHeaderValue(this.value());
    }

    abstract H createHeaderValue(final V value);

    abstract V value();

    abstract V differentValue();
}
