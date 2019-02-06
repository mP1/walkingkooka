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

package walkingkooka.text.cursor.parser.select;

import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting2;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class NodeSelectorFunctionNameTest extends ClassTestCase<NodeSelectorFunctionName>
        implements NameTesting2<NodeSelectorFunctionName, NodeSelectorFunctionName> {

    @Test
    public void testWithInvalidInitialFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodeSelectorFunctionName.with("1abc");
        });
    }

    @Test
    public void testWithInvalidPartFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            NodeSelectorFunctionName.with("abc$def");
        });
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("Abc-123");
    }

    @Override
    public NodeSelectorFunctionName createName(final String name) {
        return NodeSelectorFunctionName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "function-22";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "function-1";
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return NodeSelectorFunctionName.MAX_LENGTH;
    }

    @Override
    public String possibleValidChars(final int position) {
        return 0 == position ?
                ASCII_LETTERS :
                ASCII_LETTERS_DIGITS + "_";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return CONTROL + BYTE_NON_ASCII;
    }

    @Override
    public Class<NodeSelectorFunctionName> type() {
        return NodeSelectorFunctionName.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
