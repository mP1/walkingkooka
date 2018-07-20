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
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;

import java.util.List;

public abstract class EbnfGroupOptionalRepeatParentParserTokenTestCase<T extends EbnfParentParserToken> extends EbnfParentParserTokenTestCase2<T> {

    @Test(expected = NullPointerException.class)
    public final void testWithNullTokenFails() {
        this.createToken(this.text(), Cast.<List<EbnfParserToken>>to(null));
    }

    @Override
    protected T createDifferentToken() {
        return this.createToken(this.openChar() + "(*comment-3*)(*comment-4*)" + this.closeChar(), Lists.of(this.comment("(*comment-3*)"), this.comment("(*comment-4*)"))
        );
    }

    @Override
    final String text() {
        return this.openChar() + "(*comment-1*)" + this.closeChar();
    }

    abstract char openChar();

    abstract char closeChar();
}
