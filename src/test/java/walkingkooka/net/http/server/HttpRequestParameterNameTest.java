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

package walkingkooka.net.http.server;


import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.NameTestCase;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class HttpRequestParameterNameTest extends NameTestCase<HttpRequestParameterName> {

    @Test
    public void testWith() {
        this.createNameAndCheck("Abc123");
    }

    @Test
    public void testParameterValue() {
        final HttpRequestParameterName name = HttpRequestParameterName.with("parameter1");
        final List<String> value = Lists.of("a", "b", "c");

        assertEquals(Optional.of(value),
                name.parameterValue(new FakeHttpRequest() {
                    @Override
                    public List<String> parameterValues(final HttpRequestParameterName n) {
                        assertSame(name, n);
                        return value;
                    }
                }));
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
