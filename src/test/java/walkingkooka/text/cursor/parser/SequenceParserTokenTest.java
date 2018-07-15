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

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public final class SequenceParserTokenTest extends ParserTokenTestCase<SequenceParserToken> {

    private final static StringParserToken STRING1 = ParserTokens.string("a1", "a1");
    private final static StringParserToken STRING2 = ParserTokens.string("b2", "b2");
    private final static ParserToken MISSING3 =  ParserTokens.missing(StringParserToken.NAME, "");

    @Test(expected = NullPointerException.class)
    public void testWithNullTokensFails() {
        SequenceParserToken.with(null, "tokens");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithZeroTokensFails() {
        SequenceParserToken.with(Lists.of(string("1")), "tokens");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithOneTokenFails() {
        SequenceParserToken.with(Lists.of(STRING1), "tokens");
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTextFails() {
        SequenceParserToken.with(Lists.of(STRING1, STRING2), null);
    }
    
    // optional...............................................................................................

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionalInvalidIndexFails() {
        this.createToken().optional(999, StringParserToken.class);
    }

    @Test(expected = ClassCastException.class)
    public void testOptionalInvalidTypeFails() {
        this.createToken().optional(0, NumberParserToken.class);
    }

    @Test
    public void testOptionalPresent() {
        this.optionalAndGet(0, Optional.of(STRING1));
    }

    @Test
    public void testOptionalPresent2() {
        this.optionalAndGet(1, Optional.of(STRING2));
    }

    @Test
    public void testOptionalMissing() {
        this.optionalAndGet(2, Optional.empty());
    }

    private void optionalAndGet(final int index, final Optional<ParserToken> result) {
        final SequenceParserToken sequence = this.createToken();
        assertEquals("failed to get optional " + index + " from " + sequence,
                result,
                sequence.optional(index, StringParserToken.class));
    }

    // required...............................................................................................

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRequiredInvalidIndexFails() {
        this.createToken().required(999, StringParserToken.class);
    }

    @Test(expected = ClassCastException.class)
    public void testRequiredInvalidTypeFails() {
        this.createToken().required(0, NumberParserToken.class);
    }

    @Test
    public void testRequiredPresent() {
        this.requiredAndGet(0, STRING1);
    }

    @Test
    public void testRequiredPresent2() {
        this.requiredAndGet(1, STRING2);
    }

    @Test(expected = MissingParserTokenException.class)
    public void testRequiredMissingFails() {
        this.createToken().required(2, StringParserToken.class);
    }

    private void requiredAndGet(final int index, final ParserToken result) {
        final SequenceParserToken sequence = this.createToken();
        assertEquals("failed to get required " + index + " from " + sequence,
                result,
                sequence.required(index, StringParserToken.class));
    }
    
    @Override
    protected SequenceParserToken createToken() {
        return sequence(STRING1, STRING2, MISSING3);
    }

    @Override
    protected SequenceParserToken createDifferentToken() {
        return sequence(string("different"), string("different2"));
    }

    private static SequenceParserToken sequence(final ParserToken...tokens) {
        final String text = Arrays.stream(tokens)
                .map(t -> t.text())
                .collect(Collectors.joining());
        return SequenceParserToken.with(Lists.of(tokens), text);
    }

    private static SequenceParserToken sequence(final String text, final ParserToken...tokens) {
        return SequenceParserToken.with(Lists.of(tokens), text);
    }

    private StringParserToken string(final String s) {
        return ParserTokens.string(s, s);
    }

    @Override
    protected Class<SequenceParserToken> type() {
        return SequenceParserToken.class;
    }
}
