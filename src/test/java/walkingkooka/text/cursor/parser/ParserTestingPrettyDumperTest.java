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

package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ParserTestingPrettyDumperTest implements ClassTesting2<ParserTestingPrettyDumper> {

    @Test
    public void testDumpStringParserToken() {
        this.dumpAndCheck(ParserTokens.string("abc123", "abc123"),
                "String=abc123\n");
    }

    @Test
    public void testDumpSequenceParserToken() {
        final List<ParserToken> tokens = Lists.of(ParserTokens.string("a1", "a1"),
                ParserTokens.bigDecimal(BigDecimal.valueOf(1.5), "1.5"),
                ParserTokens.bigInteger(BigInteger.valueOf(23), "23"));

        this.dumpAndCheck(
                ParserTokens.sequence(tokens, ParserToken.text(tokens))
                , "Sequence\n" +
                        "  String=a1\n" +
                        "  BigDecimal=1.5\n" +
                        "  BigInteger=23\n");
    }

    private void dumpAndCheck(final ParserToken token,
                              final String expected) {
        assertEquals(expected,
                ParserTestingPrettyDumper.dump(Optional.of(token)),
                () -> "" + token);
    }

    @Override
    public Class<ParserTestingPrettyDumper> type() {
        return ParserTestingPrettyDumper.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
