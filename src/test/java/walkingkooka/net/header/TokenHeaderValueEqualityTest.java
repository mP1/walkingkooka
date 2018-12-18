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
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Map;

public final class TokenHeaderValueEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<TokenHeaderValue> {

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
        this.checkNotEquals(this.createObject().setParameters(TokenHeaderValue.NO_PARAMETERS));
    }

    @Override
    protected TokenHeaderValue createObject() {
        return TokenHeaderValue.with(VALUE)
                .setParameters(parameters());
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters() {
        return this.parameters("p1", PARAMETER_VALUE);
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters(final String name, final Object value) {
        return this.parameters(TokenHeaderValueParameterName.with(name), value);
    }

    private Map<TokenHeaderValueParameterName<?>, Object> parameters(final TokenHeaderValueParameterName<?> name, final Object value) {
        return Maps.one(name, value);
    }
}
