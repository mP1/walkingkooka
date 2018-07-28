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

import walkingkooka.type.PublicStaticHelper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public final class ParserTokens implements PublicStaticHelper {

    /**
     * {@see CharacterParserToken}
     */
    public static CharacterParserToken character(final char value, final String text) {
        return CharacterParserToken.with(value, text);
    }

    /**
     * {@see DecimalParserToken}
     */
    public static DecimalParserToken decimal(final BigDecimal value, final String text) {
        return DecimalParserToken.with(value, text);
    }

    /**
     * {@see DoubleParserToken}
     */
    public static DoubleParserToken doubleParserToken(final double value, final String text) {
        return DoubleParserToken.with(value, text);
    }
    
    /**
     * {@see DoubleQuotedParserToken}
     */
    public static DoubleQuotedParserToken doubleQuoted(final String value, final String text) {
        return DoubleQuotedParserToken.with(value, text);
    }

    /**
     * {@see LongParserToken}
     */
    public static LongParserToken longParserToken(final long value, final String text) {
        return LongParserToken.with(value, text);
    }
    
    /**
     * {@see MissingParserToken}
     */
    public static MissingParserToken missing(final ParserTokenNodeName name, final String text) {
        return MissingParserToken.with(name, text);
    }
    
    /**
     * {@see NumberParserToken}
     */
    public static NumberParserToken number(final BigInteger value, final String text) {
        return NumberParserToken.with(value, text);
    }

    /**
     * {@see RepeatedParserToken}
     */
    public static <T extends ParserToken> RepeatedParserToken repeated(final List<ParserToken> tokens, final String text) {
        return RepeatedParserToken.with(tokens, text);
    }

    /**
     * {@see SequenceParserToken}
     */
    public static SequenceParserToken sequence(final List<? extends ParserToken> tokens, final String text) {
        return SequenceParserToken.with(tokens, text);
    }

    /**
     * {@see SignParserToken}
     */
    public static SignParserToken sign(final boolean value, final String text) {
        return SignParserToken.with(value, text);
    }

    /**
     * {@see SingleQuotedParserToken}
     */
    public static SingleQuotedParserToken singleQuoted(final String value, final String text) {
       return SingleQuotedParserToken.with(value, text);
    }

    /**
     * {@see StringParserToken}
     */
    public static StringParserToken string(final String value, final String text) {
        return StringParserToken.with(value, text);
    }

    /**
     * Stop creation
     */
    private ParserTokens() {
        throw new UnsupportedOperationException();
    }
}
