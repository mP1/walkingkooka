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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class LanguageTagTest extends LanguageTagTestCase<LanguageTag> {

    // toHeaderTextList.......................................................................................

    @Test(expected = NullPointerException.class)
    public void testToHeaderTextListListNullFails() {
        LanguageTag.toHeaderTextList(null);
    }

    @Test
    public void testToHeaderTextListListOfOne() {
        this.toHeaderTextListAndCheck("fr",
                this.fr());
    }

    @Test
    public void testToHeaderTextListListOfOneWithParameters() {
        this.toHeaderTextListAndCheck("en; q=0.75",
                this.en075());
    }

    @Test
    public void testToHeaderTextListListOfOneWildcard() {
        this.toHeaderTextListAndCheck("*",
                LanguageTag.wildcard());
    }

    @Test
    public void testToHeaderTextListListOfMany() {
        this.toHeaderTextListAndCheck("en; q=0.75, fr",
                this.en075(),
                this.fr());
    }

    private LanguageTag en075() {
        return LanguageTag.with("en")
                .setParameters(Maps.one(LanguageTagParameterName.Q_FACTOR, 0.75f));
    }

    private LanguageTag fr() {
        return LanguageTag.with("fr");
    }

    private void toHeaderTextListAndCheck(final String toString, final LanguageTag... tags) {
        assertEquals("LanguageTag.toString(List) failed =" + CharSequences.quote(toString),
                toString,
                LanguageTag.toHeaderTextList(Lists.of(tags)));
    }

    @Override
    protected LanguageTag createHeaderValueWithParameters() {
        return LanguageTag.wildcard();
    }

    @Override
    protected Class<LanguageTag> type() {
        return LanguageTag.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

}
