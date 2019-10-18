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

package walkingkooka.datetime;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;
import walkingkooka.visit.Visiting;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DateTimeFormatterPatternVisitorTest extends DateTimeFormatterPatternVisitorTestCase<DateTimeFormatterPatternVisitor>
        implements DateTimeFormatterPatternVisitorTesting<DateTimeFormatterPatternVisitor> {

    // tests............................................................................................................

    @Test
    public void testEra() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitEra(final int width, final DateTimeFormatterPatternComponentKind kind) {
                                   checkText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'G'
        );
    }

    @Test
    public void testYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitYear(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'u'
        );
    }

    @Test
    public void testYearOfEra() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitYearOfEra(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'y'
        );
    }

    @Test
    public void testDayOfYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitDayOfYear(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'D'
        );
    }

    @Test
    public void testMonthOfYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitMonthOfYear(final int width,
                                                               final DateTimeFormatterPatternComponentKind kind) {
                                   checkNumberOrText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'M'
        );
    }

    @Test
    public void testStandaloneMonthOfYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitStandaloneMonthOfYear(final int width,
                                                                         final DateTimeFormatterPatternComponentKind kind) {
                                   checkNumberOrText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'L'
        );
    }

    @Test
    public void testDayOfMonth() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitDayOfMonth(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'd'
        );
    }

    @Test
    public void testModifiedJulianDay() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitModifiedJulianDay(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'g'
        );
    }

    @Test
    public void testQuarterOfYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitQuarterOfYear(final int width,
                                                                 final DateTimeFormatterPatternComponentKind kind) {
                                   checkNumberOrText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'Q'
        );
    }

    @Test
    public void testStandaloneQuarterOfYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitStandaloneQuarterOfYear(final int width,
                                                                           final DateTimeFormatterPatternComponentKind kind) {
                                   checkNumberOrText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'q'
        );
    }

    @Test
    public void testWeekBasedYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitWeekBasedYear(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'Y'
        );
    }

    @Test
    public void testWeekOfWeekBasedYear() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitWeekOfWeekBasedYear(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'w'
        );
    }

    @Test
    public void testWeekOfMonthW() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitWeekOfMonthW(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'W'
        );
    }

    @Test
    public void testDayOfWeek() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitDayOfWeek(final int width,
                                                             final DateTimeFormatterPatternComponentKind kind) {
                                   checkText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'E'
        );
    }

    @Test
    public void testLocalizedDayOfWeek() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLocalizedDayOfWeek(final int width,
                                                                      final DateTimeFormatterPatternComponentKind kind) {
                                   checkNumberOrText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'e'
        );
    }

    @Test
    public void testDateTimeFormatterOfTwoLittleCFails() {
        assertThrows(IllegalArgumentException.class, () -> DateTimeFormatter.ofPattern("cc"));
    }

    @Test
    public void testStandaloneLocalizedDayOfWeek() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitStandaloneLocalizedDayOfWeek(final int width,
                                                                                final DateTimeFormatterPatternComponentKind kind) {
                                   checkNumberOrText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'c'
        );
    }

    @Test
    public void testWeekOfMonthF() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitWeekOfMonthF(final int width) {
                                   this.add(width);
                               }
                           },
                'F'
        );
    }

    @Test
    public void testAmpmOfDay() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitAmpmOfDay(final int width,
                                                             final DateTimeFormatterPatternComponentKind kind) {
                                   checkText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'a'
        );
    }

    @Test
    public void testClockHourOfAmpm12() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitClockHourOfAmpm12(final int width) {
                                   this.add(width);
                               }
                           },
                'h'
        );
    }

    @Test
    public void testHourOfAmpm11() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitHourOfAmpm11(final int width) {
                                   this.add(width);
                               }
                           },
                'K'
        );
    }

    @Test
    public void testClockHourOfAmpm24() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitClockHourOfAmpm24(final int width) {
                                   this.add(width);
                               }
                           },
                'k'
        );
    }

    @Test
    public void testHourOfDay23() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitHourOfDay23(final int width) {
                                   this.add(width);
                               }
                           },
                'H'
        );
    }

    @Test
    public void testMinuteOfHour() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitMinuteOfHour(final int width) {
                                   this.add(width);
                               }
                           },
                'm'
        );
    }

    @Test
    public void testSecondOfMinute() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitSecondOfMinute(final int width) {
                                   this.add(width);
                               }
                           },
                's'
        );
    }

    @Test
    public void testFractionOfSecond() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitFractionOfSecond(final int width) {
                                   this.add(width);
                               }
                           },
                'S'
        );
    }

    @Test
    public void testMilliOfDay() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitMilliOfDay(final int width) {
                                   this.add(width);
                               }
                           },
                'A'
        );
    }

    @Test
    public void testNanoOfSecond() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitNanoOfSecond(final int width) {
                                   this.add(width);
                               }
                           },
                'n'
        );
    }

    @Test
    public void testNanoOfDay() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitNanoOfDay(final int width) {
                                   this.add(width);
                               }
                           },
                'N'
        );
    }

    @Test
    public void testTimeZoneId() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitTimeZoneId(final int width) {
                                   this.add(width);
                               }
                           },
                'V'
        );
    }

    @Test
    public void testGenericTimeZoneName() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitGenericTimeZoneName(final int width,
                                                                       final DateTimeFormatterPatternComponentKind kind) {
                                   checkText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'v'
        );
    }

    @Test
    public void testTimeZoneName() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitTimeZoneName(final int width,
                                                                final DateTimeFormatterPatternComponentKind kind) {
                                   checkText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'z'
        );
    }

    @Test
    public void testDateTimeFormatterOfPatternLocalizedZoneOffset1() {
        DateTimeFormatter.ofPattern("O");
    }

    @Test
    public void testDateTimeFormatterOfPatternLocalizedZoneOffset2Fails() {
        assertThrows(IllegalArgumentException.class, () -> DateTimeFormatter.ofPattern("OO"));
    }

    @Test
    public void testDateTimeFormatterOfPatternLocalizedZoneOffset3Fails() {
        assertThrows(IllegalArgumentException.class, () -> DateTimeFormatter.ofPattern("OOO"));
    }

    @Test
    public void testDateTimeFormatterOfPatternLocalizedZoneOffset4() {
        DateTimeFormatter.ofPattern("OOOO");
    }

    @Test
    public void testLocalizedZoneOffset() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLocalizedZoneOffset(final int width,
                                                                       final DateTimeFormatterPatternComponentKind kind) {
                                   checkNumberOrText(width, kind);
                                   this.add(width, kind);
                               }
                           },
                'O'
        );
    }

    @Test
    public void testZoneOffsetBigX() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitZoneOffsetBigX(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'X'
        );
    }

    @Test
    public void testZoneOffsetSmallX() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitZoneOffsetSmallX(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'x'
        );
    }


    @Test
    public void testZoneOffsetZ() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitZoneOffsetZ(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                'Z'
        );
    }

    @Test
    public void testOptionalStart() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitOptionalStart(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                '['
        );
    }

    @Test
    public void testOptionalStartOptionalEnd() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitOptionalStart(final int width) {
                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitOptionalEnd(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                "[]",
                "s0 [,1,e0 [,s1 ],1,e1 ]"
        );
    }

    @Test
    public void testHash() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitIllegal(final String component) {
                                   this.add(component);
                               }
                           },
                '#'
        );
    }

    @Test
    public void testBraceOpen() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitIllegal(final String component) {
                                   this.add(component);
                               }
                           },
                '{'
        );
    }

    @Test
    public void testBraceClose() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitIllegal(final String component) {
                                   this.add(component);
                               }
                           },
                '}'
        );
    }

    @Test
    public void testDateTimeFormatterOfPatternEscaped() {
        DateTimeFormatter.ofPattern("'abc \t xyz'");
    }

    @Test
    public void testDateTimeFormatterOfPatternSingleQuoteSingleQuote() {
        DateTimeFormatter.ofPattern("''");
    }

    @Test
    public void testLiteralSingleQuote() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLiteral(final String text) {
                                   this.add(text);
                               }
                           },
                "''",
                "s0 '',',e0 ''"
        );
    }

    @Test
    public void testLiteral() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLiteral(final String text) {
                                   this.add(text);
                               }
                           },
                "'hello'",
                "s0 'hello',hello,e0 'hello'"
        );
    }

    @Test
    public void testLiteralUnescaped() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLiteral(final String text) {
                                   assertEquals("hello\t", text, "text");
                                   this.add(text);
                               }
                           },
                "'hello\\t'",
                "s0 'hello\\t',hello\t,e0 'hello\\t'"
        );
    }

    @Test
    public void testLiteralSlash() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLiteral(final String text) {
                                   this.add(text);
                               }
                           },
                "/",
                "s0 /,/,e0 /"
        );
    }

    // test tries all non letter non digit ascii chars.

    @Test
    public void testPad() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {

                               @Override
                               protected void visitHourOfDay23(final int width) {
                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitPad(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                "pH",
                "s0 p,1,e0 p,s1 H,1,e1 H"
        );
    }

    @Test
    public void testPad2() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {

                               @Override
                               protected void visitHourOfDay23(final int width) {
                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitPad(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                "ppH",
                "s0 pp,2,e0 pp,s2 H,1,e2 H"
        );
    }

    @Test
    public void testPad3() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {

                               @Override
                               protected void visitHourOfDay23(final int width) {
                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitPad(final int width) {
                                   check(width);
                                   this.add(width);
                               }
                           },
                "pppH",
                "s0 ppp,3,e0 ppp,s3 H,1,e3 H"
        );
    }

    // symbols become literal...

    @Test
    public void testLettersNeverLiteral() {
        for (int i = 'A'; i <= 'Z'; i++) {
            this.visitAndNeverLiteralCheck((char) i);
        }

        for (int i = 'a'; i <= 'z'; i++) {
            if ('p' == i) {
                continue;
            }
            this.visitAndNeverLiteralCheck((char) i);
        }
    }

    private void visitAndNeverLiteralCheck(final char c) {
        final String pattern = String.valueOf(c);
        boolean notIllegal;
        try {
            DateTimeFormatter.ofPattern(pattern);
            notIllegal = true;
        } catch (final IllegalArgumentException cause) {
            notIllegal = false;
        }

        if (notIllegal) {
            new DateTimeFormatterPatternVisitor() {
                @Override
                protected void visitLiteral(final String component) {
                    throw new UnsupportedOperationException("visitLiteral " + CharSequences.quoteAndEscape(component));
                }
            }.accept(pattern);
        } else {
            visitIllegalAndCheck(pattern);
        }
    }

    @Test
    public void testNonLettersAreLiteral() {
        Loop:
//
        for (int i = 32; i < 127; i++) {
            final char c = (char) i;
            if (Character.isLetter(c)) {
                continue;
            }
            switch (c) {
                case '#':
                case '\'':
                case '[':
                case ']':
                case '{':
                case '}':
                    continue Loop;
            }
            final String pattern = String.valueOf(c);
            DateTimeFormatter.ofPattern(pattern);
            this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                                   @Override
                                   protected void visitLiteral(final String unescapedText) {
                                       this.add(unescapedText);
                                   }
                               },
                    pattern,
                    "s0 " + c + "," + c + ",e0 " + c);
        }
    }

    // multiple components..............................................................................................

    @Test
    public void testDDDmmuuuu() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {

                               @Override
                               protected void visitDayOfYear(final int width) {
                                   assertEquals(3, width);
                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitMonthOfYear(final int width,
                                                               final DateTimeFormatterPatternComponentKind kind) {
                                   assertEquals(2, width);
                                   assertEquals(DateTimeFormatterPatternComponentKind.NUMBER, kind);

                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitYear(final int width) {
                                   assertEquals(4, width);

                                   check(width);
                                   this.add(width);
                               }
                           },
                "DDDMMuuuu",
                "s0 DDD,3,e0 DDD,s3 MM,2,e3 MM,s5 uuuu,4,e5 uuuu"
        );
    }

    @Test
    public void testLiteralDDDmmuuuu() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLiteral(final String unescapedText) {
                                   assertEquals("hello", unescapedText);
                                   this.add(unescapedText);
                               }

                               @Override
                               protected void visitDayOfYear(final int width) {
                                   assertEquals(3, width);
                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitMonthOfYear(final int width,
                                                               final DateTimeFormatterPatternComponentKind kind) {
                                   assertEquals(2, width);
                                   assertEquals(DateTimeFormatterPatternComponentKind.NUMBER, kind);

                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitYear(final int width) {
                                   assertEquals(4, width);

                                   check(width);
                                   this.add(width);
                               }
                           },
                "'hello'DDDMMuuuu",
                "s0 'hello',hello,e0 'hello',s7 DDD,3,e7 DDD,s10 MM,2,e10 MM,s12 uuuu,4,e12 uuuu"
        );
    }

    @Test
    public void testLiteralDDDmmLiteraluuuu() {
        this.visitAndCheck(new TestDateTimeFormatterPatternVisitor() {
                               @Override
                               protected void visitLiteral(final String unescapedText) {
                                   this.add(unescapedText);
                               }

                               @Override
                               protected void visitDayOfYear(final int width) {
                                   assertEquals(3, width);
                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitMonthOfYear(final int width,
                                                               final DateTimeFormatterPatternComponentKind kind) {
                                   assertEquals(2, width);
                                   assertEquals(DateTimeFormatterPatternComponentKind.NUMBER, kind);

                                   check(width);
                                   this.add(width);
                               }

                               @Override
                               protected void visitYear(final int width) {
                                   assertEquals(4, width);

                                   check(width);
                                   this.add(width);
                               }
                           },
                "'hello'DDDMM/uuuu",
                "s0 'hello',hello,e0 'hello',s7 DDD,3,e7 DDD,s10 MM,2,e10 MM,s12 /,/,e12 /,s13 uuuu,4,e13 uuuu"
        );
    }

    // ignored..........................................................................................................

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
    }

    @Override
    public void testAllConstructorsVisibility() {
    }

    // helper...........................................................................................................

    private void visitAndCheck(final TestDateTimeFormatterPatternVisitor visitor,
                               final char c) {
        for (int width = 1; width < 16; width++) {
            final String pattern = pattern(c, width);

            boolean notIllegal;
            try {
                DateTimeFormatter.ofPattern(pattern);
                notIllegal = true;
            } catch (final IllegalArgumentException cause) {
                notIllegal = false;
            }
            if (notIllegal) {
                this.visitAndCheck(visitor,
                        pattern,
                        "s0 " + pattern + "," + width + ",e0 " + pattern);
            } else {
                this.visitIllegalAndCheck(pattern);
            }
        }
    }

    private String pattern(final char c,
                           final int width) {
        return CharSequences.repeating(c, width).toString();
    }

    private void visitAndCheck(final TestDateTimeFormatterPatternVisitor visitor,
                               final String pattern,
                               final String expected) {
        try {
            DateTimeFormatter.ofPattern(pattern);
        } catch (final IllegalArgumentException cause) {
            throw new AssertionError("Pattern " + CharSequences.quoteAndEscape(pattern), cause);
        }
        visitor.acceptAndCheck(pattern, expected);

        new DateTimeFormatterPatternVisitor() {
        }.accept(pattern);
    }

    private void visitIllegalAndCheck(final String pattern) {
        assertThrows(IllegalArgumentException.class, () -> DateTimeFormatter.ofPattern(pattern));
        new TestDateTimeFormatterPatternVisitor() {
            @Override
            protected void visitIllegal(final String p) {
                assertEquals(pattern, p, () -> "character, pattern: " + CharSequences.quoteAndEscape(pattern));

                this.add("illegal " + p);
            }
        }.acceptAndCheck(pattern, "s0 " + pattern + ",illegal " + pattern + ",e0 " + pattern);

        new DateTimeFormatterPatternVisitor() {
        }.accept(pattern);
    }

    private static void check(final int width) {
        assertNotEquals(0, width, "width");
    }

    private static void checkNumberOrText(final int width,
                                          final DateTimeFormatterPatternComponentKind kind) {
        check(width);
        assertNotEquals(null, kind, "kind");

        DateTimeFormatterPatternComponentKind expected;

        switch (width) {
            case 1:
            case 2:
                expected = DateTimeFormatterPatternComponentKind.NUMBER;
                break;
            case 3:
                expected = DateTimeFormatterPatternComponentKind.SHORT_TEXT;
                break;
            case 4:
                expected = DateTimeFormatterPatternComponentKind.LONG_TEXT;
                break;
            default:
                expected = DateTimeFormatterPatternComponentKind.NARROW_TEXT;
                break;
        }
        assertEquals(expected, kind, "incorrect kind for " + width);
    }

    private static void checkText(final int width,
                                  final DateTimeFormatterPatternComponentKind kind) {
        check(width);
        assertNotEquals(null, kind, "kind");

        DateTimeFormatterPatternComponentKind expected;

        switch (width) {
            case 1:
            case 2:
            case 3:
                expected = DateTimeFormatterPatternComponentKind.SHORT_TEXT;
                break;
            case 4:
                expected = DateTimeFormatterPatternComponentKind.LONG_TEXT;
                break;
            default:
                expected = DateTimeFormatterPatternComponentKind.NARROW_TEXT;
                break;
        }
        assertEquals(expected, kind, "incorrect kind for " + width);
    }

    abstract static class TestDateTimeFormatterPatternVisitor extends FakeDateTimeFormatterPatternVisitor {

        void acceptAndCheck(final String pattern, final String expected) {
            this.visited.clear();
            this.accept(pattern);
            this.checkVisited(pattern, expected);
        }

        @Override
        protected final Visiting startVisitComponent(final int position, final String text) {
            this.position = position;
            this.text = text;

            this.add("s" + position + " " + text);

            return Visiting.CONTINUE;
        }

        @Override
        protected final void endVisitComponent(final int position, final String text) {
            assertEquals(this.position, position, "position doesnt match startVisitComponent parameter");
            assertEquals(this.text, text, "text doesnt match startVisitComponent parameter");

            this.position = -1;
            this.text = null;

            this.add("e" + position + " " + text);
        }

        @Override
        protected void visitIllegal(final String component) {
            throw new InvalidCharacterException(component, this.position);
        }

        final void add(final int width) {
            this.add(String.valueOf(width));
        }

        final void add(final int width, final DateTimeFormatterPatternComponentKind kind) {
            this.add(width);
        }

        final void add(final String visit) {
            assertNotEquals("", visit);
            this.visited.add(visit);
        }

        private final List<String> visited = Lists.array();
        private int position;
        private String text;

        final void checkVisited(final String pattern,
                                final String expected) {
            assertEquals(expected,
                    String.join(",", this.visited),
                    () -> "Pattern " + CharSequences.quoteAndEscape(pattern));
        }
    }

    // skipped..........................................................................................................

    @Override
    public void testCheckToStringOverridden() {
    }

    // DateTimeFormatterPatternVisitorTesting...........................................................................

    @Override
    public DateTimeFormatterPatternVisitor createVisitor() {
        return new DateTimeFormatterPatternVisitor() {
        };
    }

    @Override
    public Class<DateTimeFormatterPatternVisitor> type() {
        return DateTimeFormatterPatternVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
