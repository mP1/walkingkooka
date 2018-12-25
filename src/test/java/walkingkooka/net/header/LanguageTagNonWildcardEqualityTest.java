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

public final class LanguageTagNonWildcardEqualityTest extends LanguageTagEqualityTestCase<LanguageTagNonWildcard> {

    @Test
    public void testWildcard() {
        this.checkNotEquals(LanguageTag.wildcard());
    }

    @Test
    public void testDifferentParameters() {
        this.checkNotEquals(LanguageTagNonWildcard.nonWildcard("en", Maps.one(LanguageTagParameterName.Q_FACTOR, 0.5f)));
    }

    @Override
    protected LanguageTagNonWildcard createObject() {
        return LanguageTagNonWildcard.nonWildcard("en", LanguageTag.NO_PARAMETERS);
    }
}
