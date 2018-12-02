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

final public class CharsetHeaderValueEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<CharsetHeaderValue> {

    private final static CharsetName CHARSET_NAME = CharsetName.UTF_8;
    private final static float Q = 0.5f;

    @Test
    public void testDifferentCharset() {
        this.checkNotEquals(CharsetHeaderValue.with(CharsetName.UTF_16)
                .setParameters(this.parameters()));
    }

    @Test
    public void testDifferentParameterValue() {
        this.checkNotEquals(CharsetHeaderValue.with(CHARSET_NAME)
                .setParameters(this.parameters(Q + 0.5f)));
    }

    @Test
    public void testDifferentParameter() {
        this.checkNotEquals(CharsetHeaderValue.with(CHARSET_NAME)
                .setParameters(this.parameters(CharsetHeaderValueParameterName.with("different"), "xyz")));
    }

    @Override
    protected CharsetHeaderValue createObject() {
        return CharsetHeaderValue.with(CHARSET_NAME).setParameters(this.parameters());
    }

    private Map<CharsetHeaderValueParameterName<?>, Object> parameters() {
        return this.parameters(Q);
    }

    private Map<CharsetHeaderValueParameterName<?>, Object> parameters(final float q) {
        return this.parameters(CharsetHeaderValueParameterName.Q_FACTOR, q);
    }

    private <T> Map<CharsetHeaderValueParameterName<?>, Object> parameters(final CharsetHeaderValueParameterName<T> name,
                                                                           final Object value) {
        return Maps.one(name, value);
    }
}
