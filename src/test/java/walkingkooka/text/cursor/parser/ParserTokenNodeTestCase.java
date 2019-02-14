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
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.NodeTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ParserTokenNodeTestCase<N extends ParserTokenNode> implements ClassTesting2<ParserTokenNode>,
        NodeTesting<ParserTokenNode,
        ParserTokenNodeName,
        ParserTokenNodeAttributeName,
        String> {

    ParserTokenNodeTestCase() {
        super();
    }

    @Test
    public void testSetAttributeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createNode().setAttributes(null);
        });
    }

    @Test
    public void testSetAttributesSame() {
        final N node = this.createParserTokenNode();
        assertSame(node, node.setAttributes(node.attributes()));
    }

    final public N createNode() {
        return this.createParserTokenNode();
    }

    abstract N createParserTokenNode();

    static StringParserToken string(final String text) {
        return ParserTokens.string(text, text);
    }

    @Override
    public final String typeNamePrefix() {
        return ParserToken.class.getSimpleName();
    }

    @Override
    public final Class<ParserTokenNode> type() {
        return Cast.to(this.parserTokenNodeType());
    }

    abstract Class<N> parserTokenNodeType();

    @Override
    final public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
