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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.naming.NameTestCase;
import walkingkooka.test.SerializationTesting;
import walkingkooka.text.CaseSensitivity;

public final class UrlParameterNameTest extends NameTestCase<UrlParameterName, UrlParameterName>
        implements SerializationTesting<UrlParameterName> {

    @Test
    @Ignore
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWithEncoding() {
        this.createNameAndCheck("abc%20xyz");
    }

    @Test
    public void testDifferentName() {
        this.checkNotEquals(UrlParameterName.with("different"));
    }

    @Test
    public void testCaseSignificant() {
        this.checkNotEquals(UrlParameterName.with("PARAM1"));
    }

    @Override
    protected UrlParameterName createName(final String name) {
        return UrlParameterName.with(name);
    }

    @Override
    protected CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    protected String nameText() {
        return "param-1";
    }

    @Override
    protected String differentNameText() {
        return "different";
    }

    @Override
    protected String nameTextLess() {
        return "aaa";
    }

    @Override
    public Class<UrlParameterName> type() {
        return UrlParameterName.class;
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
