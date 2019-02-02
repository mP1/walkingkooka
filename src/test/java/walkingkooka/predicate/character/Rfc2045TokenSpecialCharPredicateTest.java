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
 *
 */

package walkingkooka.predicate.character;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * <a href="https://tools.ietf.org/html/rfc2045#page-5"></a>
 * <pre>
 * token := 1*<any (US-ASCII) CHAR except SPACE, CTLs,
 *                  or tspecials>
 *
 *      tspecials :=  "(" / ")" / "<" / ">" / "@" /
 *                    "," / ";" / ":" / "\" / <">
 *                    "/" / "[" / "]" / "?" / "="
 *                    ; Must be in quoted-string,
 *                    ; to use within parameter values
 * </pre>
 */
public final class Rfc2045TokenSpecialCharPredicateTest extends CharPredicateTestCase<Rfc2045TokenSpecialCharPredicate> {

    @Test
    public void testNulCharFails() {
        this.testFalse('\0');
    }

    @Test
    public void testSpaceFails() {
        this.testFalse(' ');
    }

    @Test
    public void testParenOpen() {
        this.testTrue('(');
    }

    @Test
    public void testParenClose() {
        this.testTrue(')');
    }

    @Test
    public void testLessThan() {
        this.testTrue('<');
    }

    @Test
    public void testGreaterThan() {
        this.testTrue('>');
    }

    @Test
    public void testAtSign() {
        this.testTrue('@');
    }

    @Test
    public void testComma() {
        this.testTrue(',');
    }

    @Test
    public void testSemiColon() {
        this.testTrue(';');
    }

    @Test
    public void testColon() {
        this.testTrue(':');
    }

    @Test
    public void testBackslash() {
        this.testTrue('\\');
    }

    @Test
    public void testDoubleQuote() {
        this.testTrue('"');
    }

    @Test
    public void testForwardSlash() {
        this.testTrue('/');
    }

    @Test
    public void testBracketOpen() {
        this.testTrue('[');
    }

    @Test
    public void testBracketClose() {
        this.testTrue(']');
    }

    @Test
    public void testQuestionMark() {
        this.testTrue('?');
    }

    @Test
    public void testEqualsSign() {
        this.testTrue('=');
    }

    @Test
    public void testLowercaseFails() {
        for(char c : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            this.testFalse(c);
        }
    }

    @Test
    public void testUppercaseFails() {
        for(char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            this.testFalse(c);
        }
    }

    @Test
    public void testDigitsFails() {
        for(char c : "0123456789".toCharArray()) {
            this.testFalse(c);
        }
    }

    @Test
    public void testRfc2045TokenCharPredicateFails() {
        final CharPredicate special = Rfc2045TokenSpecialCharPredicate.INSTANCE;
        final CharPredicate rfc2045 = CharPredicates.rfc2045Token();

        for(int i= 0; i < Character.MAX_VALUE; i++) {
            final char c = (char)i;
            if(special.test(c)) {
                this.testFalse(rfc2045, c);
            }
        }
    }

    @Test
    public void testToString() {
        assertEquals("RFC2045TokenSpecial", this.createCharPredicate().toString());
    }

    @Override
    protected Rfc2045TokenSpecialCharPredicate createCharPredicate() {
        return Rfc2045TokenSpecialCharPredicate.INSTANCE;
    }

    @Override
    protected Class<Rfc2045TokenSpecialCharPredicate> type() {
        return Rfc2045TokenSpecialCharPredicate.class;
    }
}
