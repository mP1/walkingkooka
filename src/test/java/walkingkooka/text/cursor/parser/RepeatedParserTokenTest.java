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
import walkingkooka.tree.visit.Visiting;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public final class RepeatedParserTokenTest extends ParserTokenTestCase<RepeatedParserToken> {

    private final static StringParserToken STRING1 = ParserTokens.string("a1", "a1");
    private final static StringParserToken STRING2 = ParserTokens.string("b2", "b2");
    private final static StringParserToken STRING4 = ParserTokens.string("d4", "d4");
    private final static StringParserToken STRING5 = ParserTokens.string("e5", "e5");
    private final static StringParserToken STRING6 = ParserTokens.string("f6", "f6");
    
    @Test(expected = NullPointerException.class)
    public void testWithNullContentFails() {
        RepeatedParserToken.with(null, "tokens");
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        RepeatedParserToken.with(Lists.of(string("abc")), null);
    }

    // setValue...........................................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetValueNullFails(){
        this.createToken().setValue(null);
    }

    @Test
    public void testSetValueSame(){
        final RepeatedParserToken token = this.createToken();
        assertSame(token, token.setValue(token.value()));
    }

    @Test
    public void testSetValueDifferent() {
        final RepeatedParserToken token = this.createToken();
        final List<ParserToken> differentTokens = this.createDifferentToken().value();
        final RepeatedParserToken different = token.setValue(differentTokens);
        assertNotSame(token, different);
        assertEquals("tokens", differentTokens, different.value());
    }
    
    @Test
    public void testFlat() {
        final RepeatedParserToken token = this.createToken();
        assertSame(token, token.flat());
    }

    @Test
    public void testFlatRequired() {
        final RepeatedParserToken child = repeated(STRING4, STRING5);
        final RepeatedParserToken parent = repeated(STRING1, STRING2, child);
        final RepeatedParserToken flat = parent.flat();
        assertNotSame(parent, flat);
        assertEquals("values after flattening", Lists.of(STRING1, STRING2, STRING4, STRING5), flat.value());
        this.checkText(flat,"a1b2d4e5");
    }

    @Test
    public void testFlatRequired2() {
        final RepeatedParserToken childChild = repeated(STRING5, STRING6);
        final RepeatedParserToken child = repeated(STRING4, childChild);
        final RepeatedParserToken parent = repeated(STRING1, STRING2, child);
        final RepeatedParserToken flat = parent.flat();
        assertNotSame(parent, flat);
        assertEquals("values after flattening", Lists.of(STRING1, STRING2, STRING4, STRING5, STRING6), flat.value());
        this.checkText(flat,"a1b2d4e5f6");
    }

    @Test
    public void testAccept() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final StringParserToken string = this.string("abc");
        final RepeatedParserToken token = this.repeated(string);

        new FakeParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                b.append("1");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                b.append("2");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final RepeatedParserToken t) {
                assertSame(token, t);
                b.append("3");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final RepeatedParserToken t) {
                assertSame(token, t);
                b.append("4");
                visited.add(t);
            }

            @Override
            protected void visit(final StringParserToken t) {
                b.append("5");
                visited.add(t);
            }
        }.accept(token);
        assertEquals("1315242", b.toString());
        assertEquals("visited tokens", Lists.of(token, token, string, string, string, token, token), visited);
    }

    @Test
    public void testAcceptSkip() {
        final StringBuilder b = new StringBuilder();
        final List<ParserToken> visited = Lists.array();

        final StringParserToken string = this.string("abc");
        final RepeatedParserToken token = this.repeated(string);

        new FakeParserTokenVisitor() {
            @Override
            protected Visiting startVisit(final ParserToken t) {
                b.append("1");
                visited.add(t);
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final ParserToken t) {
                assertSame(token, t);
                b.append("2");
                visited.add(t);
            }

            @Override
            protected Visiting startVisit(final RepeatedParserToken t) {
                assertSame(token, t);
                b.append("3");
                visited.add(t);
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final RepeatedParserToken t) {
                assertSame(token, t);
                b.append("4");
                visited.add(t);
            }
        }.accept(token);
        assertEquals("1342", b.toString());
        assertEquals("visited tokens", Lists.of(token, token, token, token), visited);
    }
    
    @Override
    protected RepeatedParserToken createToken() {
        return repeated("abc", string("abc"));
    }

    @Override
    protected RepeatedParserToken createDifferentToken() {
        return repeated("different", string("different"));
    }

    private RepeatedParserToken repeated(final ParserToken...tokens) {
        return repeated(
                Arrays.stream(tokens).map(t -> t.text()).collect(Collectors.joining()),
                tokens);
    }

    private RepeatedParserToken repeated(final String text, final ParserToken...tokens) {
        return RepeatedParserToken.with(Lists.of(tokens), text);
    }

    private StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    protected Class<RepeatedParserToken> type() {
        return RepeatedParserToken.class;
    }
}
