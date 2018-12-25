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
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertSame;

public final class LanguageTagWildcardTest extends LanguageTagTestCase<LanguageTagWildcard> {

    @Test
    public void testWith() {
        this.check(LanguageTagWildcard.INSTANCE,
                "*",
                LanguageTag.NO_LOCALE,
                LanguageTag.NO_PARAMETERS);
    }

    @Test
    public void testWithCached() {
        assertSame(LanguageTag.with("*"), LanguageTag.with("*"));
    }

    @Test
    public void testSetParametersDifferentAndBack() {
        assertSame(LanguageTag.wildcard(),
                LanguageTag.wildcard()
                        .setParameters(this.parametersWithQFactor())
                        .setParameters(LanguageTag.NO_PARAMETERS));
    }

    @Override
    protected LanguageTagWildcard createHeaderValueWithParameters() {
        return LanguageTagWildcard.INSTANCE;
    }

    @Override
    protected Class<LanguageTagWildcard> type() {
        return LanguageTagWildcard.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
