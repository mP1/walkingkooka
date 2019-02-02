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

import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting2;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PojoNameTest extends ClassTestCase<PojoName>
        implements NameTesting2<PojoName, PojoName> {

    private final static String PROPERTY = "abc";

    @Test
    public void testPropertyNullFails() {
        assertThrows(NullPointerException.class, () -> {
            PojoName.property(null);
        });
    }

    @Test
    public void testPropertyEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            PojoName.property("");
        });
    }

    @Test
    public void testPropertyInvalidInitialFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            PojoName.property("9abc");
        });
    }

    @Test
    public void testPropertyInvalidOtherFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            PojoName.property("abc.");
        });
    }

    @Test
    public void testProperty() {
        this.createNameAndCheck(PROPERTY);
    }

    @Test
    public void testIndexInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            PojoName.index(-1);
        });
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
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String possibleValidChars(final int position) {
        return 0 == position ?
                ASCII_LETTERS :
                ASCII_LETTERS_DIGITS;
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return CONTROL + WHITESPACE;
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
