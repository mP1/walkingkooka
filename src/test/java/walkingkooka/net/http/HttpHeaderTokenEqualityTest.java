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
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Map;

public final class HttpHeaderTokenEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<HttpHeaderToken> {

    private final static String VALUE = "abc";
    private final static String PARAMETER_VALUE = "v1";

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(this.createObject().setValue("different"));
    }

    @Test
    public void testDifferentParameters() {
        this.checkNotEquals(this.createObject().setParameters(this.parameters("different", PARAMETER_VALUE)));
    }

    @Test
    public void testDifferentParameters2() {
        this.checkNotEquals(this.createObject().setParameters(HttpHeaderToken.NO_PARAMETERS));
    }

    @Override
    protected HttpHeaderToken createObject() {
        return HttpHeaderToken.with(VALUE, parameters());
    }

    private Map<HttpHeaderParameterName<?>, String> parameters() {
        return this.parameters("p1", PARAMETER_VALUE);
    }

    private Map<HttpHeaderParameterName<?>, String> parameters(final String name, final String value) {
        return this.parameters(HttpHeaderParameterName.with(name), value);
    }

    private Map<HttpHeaderParameterName<?>, String> parameters(final HttpHeaderParameterName<?> name, final String value) {
        return Maps.one(name, value);
    }
}
