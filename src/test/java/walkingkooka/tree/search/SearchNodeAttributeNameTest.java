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

package walkingkooka.tree.search;

import org.junit.Test;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class SearchNodeAttributeNameTest extends ClassTestCase<SearchNodeAttributeName>
        implements NameTesting<SearchNodeAttributeName, SearchNodeAttributeName> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialFails() {
        this.createName("9abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartFails() {
        this.createName("abc123!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithDotDotFails() {
        this.createName("abc..def");
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("abc");
    }

    @Test
    public void testWithDot() {
        this.createNameAndCheck("abc.def");
    }

    @Test
    public void testWithDot2() {
        this.createNameAndCheck("abc.def.ghi.123");
    }

    @Test
    public void testWith2() {
        this.createNameAndCheck("abc123");
    }

    @Test
    public void testToString() {
        assertEquals("abc", this.createName("abc").toString());
    }

    @Override
    public SearchNodeAttributeName createName(final String name) {
        return SearchNodeAttributeName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "language";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "country";
    }

    @Override
    protected Class<SearchNodeAttributeName> type() {
        return SearchNodeAttributeName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
