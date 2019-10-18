/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.text.cursor.parser.ebnf;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.naming.NameTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.util.PropertiesPath;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class EbnfIdentifierNameTest implements ClassTesting2<EbnfIdentifierName>,
        NameTesting2<EbnfIdentifierName, EbnfIdentifierName> {

    @Test
    public void testCreateContainsSeparatorFails() {
        assertThrows(InvalidCharacterException.class, () -> EbnfIdentifierName.with("xyz" + PropertiesPath.SEPARATOR.string()));
    }

    @Test
    public void testWithInvalidInitialFails() {
        assertThrows(InvalidCharacterException.class, () -> EbnfIdentifierName.with("1abc"));
    }

    @Test
    public void testWithInvalidPartFails() {
        assertThrows(InvalidCharacterException.class, () -> EbnfIdentifierName.with("abc$def"));
    }

    @Override
    public EbnfIdentifierName createName(final String name) {
        return EbnfIdentifierName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "identifier123";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "abc";
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
        return NameTesting2.subtract(ASCII, ASCII_LETTERS_DIGITS + "_");
    }

    @Override
    public Class<EbnfIdentifierName> type() {
        return EbnfIdentifierName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
