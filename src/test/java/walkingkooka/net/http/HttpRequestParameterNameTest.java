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

package walkingkooka.net.http;


import org.junit.Test;
import walkingkooka.naming.NameTestCase;

import static org.junit.Assert.assertEquals;

final public class HttpRequestParameterNameTest extends NameTestCase<HttpRequestParameterName> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        HttpRequestParameterName.with("");
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Abc123");
    }

    @Test
    public void testToString() {
        final String name = "ABC123";
        assertEquals(name, HttpRequestParameterName.with(name).toString());
    }

    @Override
    protected HttpRequestParameterName createName(final String name) {
        return HttpRequestParameterName.with(name);
    }

    @Override
    protected Class<HttpRequestParameterName> type() {
        return HttpRequestParameterName.class;
    }
}
