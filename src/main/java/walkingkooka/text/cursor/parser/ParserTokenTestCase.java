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
import walkingkooka.collect.list.Lists;
import walkingkooka.test.PublicClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchSequenceNode;
import walkingkooka.tree.visit.Visiting;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public abstract class ParserTokenTestCase<T extends ParserToken> extends PublicClassTestCase<T> {

    @Test
    public final void testNaming() {
        this.checkNaming(ParserToken.class);
    }

    @Test
    public final void testNameConstantPresent() throws Exception {
        final Class<T> type = this.type();
        final Field field = type.getField("NAME");
        assertEquals("NAME constant has incorrect value", ParserTokenNodeName.fromClass(type), field.get(null));
    }

    @Test
    public final void testImplementsEitherParentParserTokenOrLeafParserToken() {
        for(;;){
            final Class<T> type = this.type();
            final boolean leaf = LeafParserToken.class.isAssignableFrom(type);
            final boolean parent = ParentParserToken.class.isAssignableFrom(type);
            if(leaf){
                assertFalse("Type " + type.getName() + " must implement either " + LeafParserToken.class.getName() + " or " + ParentParserToken.class.getName() + " but not both", parent);
                break;
            }
            if(parent){
                break;
            }
            fail("Type " + type.getName() + " must implement either " + LeafParserToken.class.getName() + " or " + ParentParserToken.class.getName());
        }
    }

    @Test
    public final void testValueType() {
        for(;;){
            final T token = this.createToken();
            if(token instanceof LeafParserToken){
                final Object value = LeafParserToken.class.cast(token).value();
                assertFalse(token + " value must not be a Collection but was " + toString(value), value instanceof Collection);
                break;
            }
            if(token instanceof ParentParserToken){
                final Object value = ParentParserToken.class.cast(token).value();
                assertTrue(token + " value must be a Collection but was " + toString(value), value instanceof Collection);
                break;
            }
            fail("ParserToken: " + token + " must implement either " + LeafParserToken.class.getName() + " or " + ParentParserToken.class.getName());
        }
    }

    /**
     * If a type class name includes Whitespace its {@link ParserToken#isWhitespace()} should also return true.
     */
    @Test
    public final void testIsWhitespace() {
        final String type = this.type().getSimpleName();
        final boolean whitespace = type.contains(WHITESPACE);

        final T token = this.createToken();
        assertEquals(token + " isWhitespace must be true if " + type + " contains " + CharSequences.quote(WHITESPACE), whitespace, token.isWhitespace());

        if(whitespace) {
            assertEquals(token + " isWhitespace==true, isNoise must also be true", whitespace, token.isNoise());
        }
    }

    private final static String WHITESPACE = "Whitespace";

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        this.createToken(null);
    }

    @Test
    public final void testText() {
        assertEquals(this.text(), this.createToken().text());
    }

    @Test
    public void testToSearchNode() {
        final T token = this.createToken();
        final SearchNode searchNode = token.toSearchNode();
        assertEquals("text", token.text(), searchNode.text());

        for(;;) {
            if (token instanceof LeafParserToken) {
                break;
            }
            if (token instanceof ParentParserToken) {
                assertTrue("SearchNode should be a SearchSequenceNode=" + searchNode, searchNode.isSequence());

                final ParentParserToken<?> parent = ParentParserToken.class.cast(token);
                assertEquals("child count should be the same", parent.value().size(), SearchSequenceNode.class.cast(searchNode).children().size());
                break;
            }
        }
    }

    @Test
    public void testToSearchNode2() {
        final T token = this.createToken();
        assertEquals(token.toSearchNode(), token.toSearchNode());
    }

    private static String toString(final Object instance) {
        return null != instance ?
               instance.getClass().getName() + "=" + instance :
               "null";
    }

    @Test
    public void testPublicStaticFactoryMethod() {
        this.publicStaticFactoryCheck(ParserTokens.class, "", ParserToken.class);
    }

    @Test(expected = NullPointerException.class)
    public final void testSetTextNullFails() {
        this.createToken().setText(null);
    }

    @Test
    public final void testSetTextSame() {
        final T token = this.createToken();
        assertSame(token, token.setText(token.text()));
    }

    @Test
    public final void testSetTextDifferent() {
        final T token = this.createToken();
        final String differentText = this.createDifferentToken().text();
        assertNotEquals("different text must be different from tokens", token.text(), differentText);

        final ParserToken token2 = token.setText(differentText);
        assertNotSame(token, token2);
        checkText(token2, differentText);
        assertEquals("type of token after set must remain the same=" + token2, token.getClass(), token2.getClass());

        assertNotEquals("tokens must be different", token, token2);

        final ParserToken token3 = token2.setText(token.text());
        assertEquals("after setting original text tokens must be equal", token, token3);
    }

    @Test
    public final void testAcceptStartParserTokenSkip() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final T token = this.createToken();

        new FakeParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                b.append("1");
                visited.add(t);
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                assertSame(token, t);
                b.append("2");
                visited.add(t);
            }
        }.accept(token);
        assertEquals("12", b.toString());
        assertEquals("visited tokens", Lists.of(token, token), visited);
    }

    @Test
    public void testIsNoisyGuess() {
        final T token = this.createToken();
        final String className = token.getClass().getSimpleName();
        assertEquals(className.contains("Whitespace") | className.contains("Symbol") | className.contains("Comment"), token.isNoise());
    }

    @Test
    public void testPropertiesNeverReturnNull() throws Exception {
        propertiesNeverReturnNullCheck(this.createToken());
    }

    @Test
    public void testToString() {
        assertEquals(this.text(), this.createToken().toString());
    }

    @Test
    public final void testEqualsNull() {
        assertNotEquals(this.createToken(), null);
    }

    @Test
    public final void testEqualsObject() {
        assertNotEquals(this.createToken(), new Object());
    }

    @Test
    public final void testEqualsSame() {
        final T token = this.createToken();
        assertEquals(token, token);
    }

    @Test
    public final void testEqualsEqual() {
        assertEquals(this.createToken(), this.createToken());
    }

    @Test
    public final void testEqualsDifferent() {
        assertNotEquals(this.createToken(), this.createDifferentToken());
    }

    protected T createToken() {
        return this.createToken(this.text());
    }

    abstract protected String text();

    protected abstract T createToken(final String text);

    protected abstract T createDifferentToken();

    protected void checkText(final ParserToken token, final String text) {
        assertEquals("text of " + token, text, token.text());
    }
}
