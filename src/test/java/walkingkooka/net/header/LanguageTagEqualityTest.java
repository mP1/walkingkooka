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

public final class LanguageTagEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<LanguageTag> {

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(this.createObject().setValue(LanguageTagName.with("fr")));
    }

    @Test
    public void testDifferentParameters() {
        this.checkNotEquals(this.createObject().setParameters(LanguageTag.NO_PARAMETERS));
    }

    @Override
    protected LanguageTag createObject() {
        return LanguageTag.with(this.value()).setParameters(this.parameters());
    }

    private LanguageTagName value() {
        return LanguageTagName.with("en");
    }

    private Map<LanguageTagParameterName<?>, Object> parameters() {
        return Maps.one(LanguageTagParameterName.Q_FACTOR, 0.5f);
    }
}
