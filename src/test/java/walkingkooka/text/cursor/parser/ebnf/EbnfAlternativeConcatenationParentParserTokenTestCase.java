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
package walkingkooka.text.cursor.parser.ebnf;

import org.junit.Test;
import walkingkooka.collect.list.Lists;

import java.util.List;

import static org.junit.Assert.assertNotSame;

public abstract class EbnfAlternativeConcatenationParentParserTokenTestCase<T extends EbnfParentParserToken> extends EbnfParentParserTokenTestCase2<T> {

    @Test(expected = IllegalArgumentException.class)
    public final void testOnlyOneTokenIgnoringCommentsSymbolsWhitespaceFails() {
        this.createToken(this.text(), this.identifier("first"), this.comment("(*comment-2*)"));
    }

    @Test
    public void testWithoutCommentsSymbolsWhitespace() {
        final EbnfParserToken identifier1 = this.identifier("identifier1");
        final EbnfParserToken comment2 = this.comment("(*comment2*)");
        final EbnfParserToken identifier3 = this.identifier("identifier3");

        final T token = this.createToken(this.text(), identifier1, comment2, identifier3);
        assertEquals("value", Lists.of(identifier1, comment2, identifier3), token.value());

        final T without = token.withoutCommentsSymbolsOrWhitespace().get().cast();
        assertNotSame(token, without);
        assertEquals("value", Lists.of(identifier1, identifier3), without.value());
    }

    @Override
    final List<EbnfParserToken> tokens() {
        return Lists.of(this.identifier("identifier-1"), this.identifier("identifier-2"));
    }

    @Override
    protected T createDifferentToken() {
        return this.createToken("diff-1" + separatorChar() + "diff-2", this.identifier("diff-1"), this.identifier("diff-2"));
    }

    abstract char separatorChar();
}
