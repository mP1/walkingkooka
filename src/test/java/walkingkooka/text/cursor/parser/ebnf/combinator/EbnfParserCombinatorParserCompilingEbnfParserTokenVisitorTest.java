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

package walkingkooka.text.cursor.parser.ebnf.combinator;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.ebnf.EbnfIdentifierName;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserToken;
import walkingkooka.text.cursor.parser.ebnf.EbnfParserTokenVisitorTesting;

public final class EbnfParserCombinatorParserCompilingEbnfParserTokenVisitorTest implements EbnfParserTokenVisitorTesting<EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor, EbnfParserToken> {

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createVisitor(), "{abc123=xyz456}");
    }

    @Override
    public EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor createVisitor() {
        return new EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor(Maps.of(EbnfIdentifierName.with("abc123"), Parsers.fake().setToString("xyz456")), null);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor> type() {
        return EbnfParserCombinatorParserCompilingEbnfParserTokenVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return "EbnfParserCombinator";
    }
}
