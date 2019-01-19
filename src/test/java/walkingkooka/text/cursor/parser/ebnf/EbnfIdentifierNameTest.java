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

package walkingkooka.text.cursor.parser.ebnf;

import org.junit.Test;
import walkingkooka.naming.NameTestCase;
import walkingkooka.naming.PropertiesPath;
import walkingkooka.text.CaseSensitivity;

import static org.junit.Assert.assertEquals;

final public class EbnfIdentifierNameTest extends NameTestCase<EbnfIdentifierName, EbnfIdentifierName> {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContainsSeparatorFails() {
        EbnfIdentifierName.with("xyz" + PropertiesPath.SEPARATOR.string());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidInitialFails() {
        EbnfIdentifierName.with("1abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidPartFails() {
        EbnfIdentifierName.with("abc$def");
    }

    @Test
    public void testToString() {
        assertEquals("ABC_123", this.createName("ABC_123").toString());
    }

    @Override
    protected EbnfIdentifierName createName(final String name) {
        return EbnfIdentifierName.with(name);
    }

    @Override
    protected CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    protected String nameText() {
        return "identifier123";
    }

    @Override
    protected String differentNameText() {
        return "different";
    }

    @Override
    protected String nameTextLess() {
        return "abc";
    }

    @Override
    protected Class<EbnfIdentifierName> type() {
        return EbnfIdentifierName.class;
    }
}
