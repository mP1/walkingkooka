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

public abstract class EbnfParentParserTokenTestCase2<T extends EbnfParentParserToken> extends EbnfParentParserTokenTestCase<T> {

    @Test(expected = IllegalArgumentException.class)
    public final void testOnlyCommentsFails() {
        this.createToken(this.text(), this.comment("(*comment-1*)"), this.comment("(*comment-2*)"));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testOnlySymbolsFails() {
        this.createToken(this.text(), symbol("a"), symbol("z"));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testOnlyWhitespaceFails() {
        this.createToken(this.text(), this.whitespace("   "), this.whitespace(" "));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testOnlyCommentsSymbolsWhitespaceFails() {
        this.createToken(this.text(), this.comment("(*comment-1*)"), symbol("2"), this.whitespace("   "));
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testOnlyCommentWhitespaceFails() {
        this.createToken(this.text(), this.whitespace("   "), this.whitespace(" "));
    }
}
