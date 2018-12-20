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

import static org.junit.Assert.assertEquals;

public abstract class HeaderParser2TestCase<P extends HeaderParser2<N>,
        N extends HeaderParameterName<?>,
        V> extends HeaderParserTestCase<P, V> {

    HeaderParser2TestCase() {
        super();
    }

    @Test
    public final void testToString() {
        assertEquals("0 in \"123\"", this.createHeaderParser("123").toString());
    }

    abstract P createHeaderParser(final String text);
}
