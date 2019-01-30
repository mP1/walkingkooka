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
 */
package walkingkooka.tree.pojo;

import org.junit.Test;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

public final class PojoNameTest extends ClassTestCase<PojoName>
        implements NameTesting<PojoName, PojoName> {

    private final static String PROPERTY = "abc";

    @Test(expected = NullPointerException.class)
    public void testPropertyNullFails() {
        PojoName.property(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyEmptyFails() {
        PojoName.property("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyInvalidInitialFails() {
        PojoName.property("9abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyInvalidOtherFails() {
        PojoName.property("abc.");
    }

    @Test
    public void testProperty() {
        this.createNameAndCheck(PROPERTY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexInvalidFails() {
        PojoName.index(-1);
    }

    @Test
    public void testIndex() {
        final PojoName name = PojoName.index(123);
        this.checkValue(name, "123");
    }

    @Override
    public PojoName createName(final String name) {
        return PojoName.property(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "name";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "address";
    }

    @Override
    protected Class<PojoName> type() {
        return PojoName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
