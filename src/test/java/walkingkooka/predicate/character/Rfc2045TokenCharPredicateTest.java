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
public final class Rfc2045TokenCharPredicateTest implements CharPredicateTesting<Rfc2045TokenCharPredicate> {

    @Test
    public void testNulCharFails() {
        this.testFalse('\0');
    }

    @Test
    public void testParenOpenFails() {
        this.testFalse('(');
    }

    @Test
    public void testParenCloseFails() {
        this.testFalse(')');
    }

    @Test
    public void testLessThanFails() {
        this.testFalse('<');
    }

    @Test
    public void testGreaterThanFails() {
        this.testFalse('>');
    }

    @Test
    public void testAtSignFails() {
        this.testFalse('@');
    }

    @Test
    public void testCommaFails() {
        this.testFalse(',');
    }

    @Test
    public void testSemiColonFails() {
        this.testFalse(';');
    }

    @Test
    public void testColonFails() {
        this.testFalse(':');
    }

    @Test
    public void testBackslashFails() {
        this.testFalse('\\');
    }

    @Test
    public void testDoubleQuoteFails() {
        this.testFalse('"');
    }

    @Test
    public void testForwardSlashFails() {
        this.testFalse('/');
    }

    @Test
    public void testBracketOpenFails() {
        this.testFalse('[');
    }

    @Test
    public void testBracketCloseFails() {
        this.testFalse(']');
    }

    @Test
    public void testQuestionMarkFails() {
        this.testFalse('?');
    }

    @Test
    public void testEqualsSignFails() {
        this.testFalse('=');
    }

    @Test
    public void testSpaceFails() {
        this.testFalse(' ');
    }

    @Test
    public void testLowercase() {
        for(char c : "abcdefghijklmnopqrstuvwxyz".toCharArray()) {
            this.testTrue(c);
        }
    }

    @Test
    public void testUppercase() {
        for(char c : "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
            this.testTrue(c);
        }
    }

    @Test
    public void testDigits() {
        for(char c : "0123456789".toCharArray()) {
            this.testTrue(c);
        }
    }

    @Test
    public void testRfc2045SpecialCharPredicateFails() {
        final CharPredicate rfc2045 = CharPredicates.rfc2045Token();
        final CharPredicate special = CharPredicates.rfc2045TokenSpecial();

        for(int i= 0; i < Character.MAX_VALUE; i++) {
            final char c = (char)i;
            if(rfc2045.test(c)) {
                this.testFalse(special, c);
            }
        }
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCharPredicate(), "RFC2045Token");
    }

    @Override
    public Rfc2045TokenCharPredicate createCharPredicate() {
        return Rfc2045TokenCharPredicate.INSTANCE;
    }

    @Override
    public Class<Rfc2045TokenCharPredicate> type() {
        return Rfc2045TokenCharPredicate.class;
    }
}
