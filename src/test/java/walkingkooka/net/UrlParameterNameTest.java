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

package walkingkooka.net;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.NameTesting;
import walkingkooka.net.http.server.FakeHttpRequest;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class UrlParameterNameTest implements ClassTesting2<UrlParameterName>,
        NameTesting<UrlParameterName, UrlParameterName>,
        SerializationTesting<UrlParameterName> {

    @Override
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWithEncoding() {
        this.createNameAndCheck("abc%20xyz");
    }

    // HttpRequestAttribute...............................................

    @Test
    public void testParameterValueRequest() {
        final UrlParameterName name = this.createName("param1");
        assertEquals(Optional.of(Lists.of("value1")),
                name.parameterValue(new FakeHttpRequest() {

                    @Override
                    public RelativeUrl url() {
                        return Url.parseRelative("/file?param2=value2&param1=value1");
                    }
                }));
    }

    @Test
    public void testParameterValueMap() {
        final UrlParameterName name = this.createName("param1");
        assertEquals(Optional.of(Lists.of("value1")),
                name.parameterValue(UrlQueryString.with("param1=value1&param2=value2").parameters()));
    }

    @Override
    public UrlParameterName createName(final String name) {
        return UrlParameterName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "param-1";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "aaa";
    }

    @Override
    public Class<UrlParameterName> type() {
        return UrlParameterName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public UrlParameterName serializableInstance() {
        return UrlParameterName.with("name");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
