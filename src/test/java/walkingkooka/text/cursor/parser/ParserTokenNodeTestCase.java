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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.tree.NodeTestCase;

import static org.junit.Assert.assertSame;

public abstract class ParserTokenNodeTestCase<N extends ParserTokenNode> extends NodeTestCase<ParserTokenNode,
        ParserTokenNodeName,
        ParserTokenNodeAttributeName,
        String> {

    @Test(expected = NullPointerException.class)
    public void testSetAttributeNullFails() {
        this.createNode().setAttributes(null);
    }

    @Test
    public void testSetAttributesSame() {
        final N node = this.createParserTokenNode();
        assertSame(node, node.setAttributes(node.attributes()));
    }

    final protected N createNode() {
        return this.createParserTokenNode();
    }

    abstract N createParserTokenNode();

    static StringParserToken string(final String text) {
        return ParserTokens.string(text, text);
    }

    @Override
    protected final String requiredNamePrefix() {
        return ParserToken.class.getSimpleName();
    }

    @Override
    final protected boolean typeMustBePublic() {
        return false;
    }

    @Override
    protected final Class<ParserTokenNode> type() {
        return Cast.to(this.parserTokenNodeType());
    }

    abstract Class<N> parserTokenNodeType();
}
