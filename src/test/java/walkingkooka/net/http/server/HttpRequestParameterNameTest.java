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


import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

final public class HttpRequestParameterNameTest extends ClassTestCase<HttpRequestParameterName>
        implements NameTesting<HttpRequestParameterName, HttpRequestParameterName> {

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

    @Override
    public HttpRequestParameterName createName(final String name) {
        return HttpRequestParameterName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "param2";
    }

    @Override
    public String differentNameText() {
        return "param99";
    }

    @Override
    public String nameTextLess() {
        return "param1";
    }

    @Override
    public Class<HttpRequestParameterName> type() {
        return HttpRequestParameterName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
