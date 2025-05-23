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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class GlobPatternTest implements ClassTesting<GlobPattern>,
    HashCodeEqualsDefinedTesting2<GlobPattern>,
    PredicateTesting2<GlobPattern, CharSequence>,
    HasTextTesting,
    ToStringTesting<GlobPattern> {

    // parse............................................................................................................

    @Test
    public void testParseWithNullPatternFails() {
        assertThrows(
            NullPointerException.class,
            () -> {
                GlobPattern.parse(
                    null,
                    CaseSensitivity.SENSITIVE
                );
            }
        );
    }

    @Test
    public void testParseWithEmptyCaseInsensitive() {
        assertSame(
            GlobPattern.EMPTY_CASE_INSENSITIVE,
            GlobPattern.parse(
                "",
                CaseSensitivity.INSENSITIVE
            )
        );
    }

    @Test
    public void testParseWithEmptyCaseSensitive() {
        assertSame(
            GlobPattern.EMPTY_CASE_SENSITIVE,
            GlobPattern.parse(
                "",
                CaseSensitivity.SENSITIVE
            )
        );
    }

    @Test
    public void testParseTextLiteral() {
        this.parseAndTestCheck(
            "Hello",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.textLiteral("Hello")
        );
    }

    @Test
    public void testParseTextLiteralWithEscape() {
        this.parseAndTestCheck(
            "Hello\\*",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.textLiteral("Hello*")
        );
    }

    @Test
    public void testParseQuestionMark() {
        this.parseAndTestCheck(
            "?",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(1, 1)
        );
    }

    @Test
    public void testParseStar() {
        this.parseAndTestCheck(
            "*",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(0, GlobPatternComponent.STAR_MAX)
        );
    }

    @Test
    public void testParseQuestionMark2() {
        this.parseAndTestCheck(
            "??",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(2, 2)
        );
    }

    @Test
    public void testParseQuestionMark3() {
        this.parseAndTestCheck(
            "???",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(3, 3)
        );
    }

    @Test
    public void testParseStar2() {
        this.parseAndTestCheck(
            "**",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(0, GlobPatternComponent.STAR_MAX)
        );
    }

    @Test
    public void testParseQuestionStar() {
        this.parseAndTestCheck(
            "?*",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(1, GlobPatternComponent.STAR_MAX)
        );
    }

    @Test
    public void testParseStarQuestion() {
        this.parseAndTestCheck(
            "*?",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(1, GlobPatternComponent.STAR_MAX)
        );
    }

    @Test
    public void testParseQuestionStarQuestion() {
        this.parseAndTestCheck(
            "?*?",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(2, GlobPatternComponent.STAR_MAX)
        );
    }

    @Test
    public void testParseQuestionText() {
        this.parseAndTestCheck(
            "?Hello",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(1, 1),
            GlobPatternComponent.textLiteral("Hello")
        );
    }

    @Test
    public void testParseStarText() {
        this.parseAndTestCheck(
            "*Hello",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.wildcard(0, GlobPatternComponent.STAR_MAX),
            GlobPatternComponent.textLiteral("Hello")
        );
    }

    @Test
    public void testParseTextQuestion() {
        this.parseAndTestCheck(
            "Hello?",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.textLiteral("Hello"),
            GlobPatternComponent.wildcard(1, 1)
        );
    }

    @Test
    public void testParseTextQuestionTextStar() {
        this.parseAndTestCheck(
            "Hello?123*",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.textLiteral("Hello"),
            GlobPatternComponent.wildcard(1, 1),
            GlobPatternComponent.textLiteral("123"),
            GlobPatternComponent.wildcard(0, GlobPatternComponent.STAR_MAX)
        );
    }

    @Test
    public void testParseTextIncludesEscapedQuestionMark() {
        this.parseAndTestCheck(
            "Hello\\?",
            CaseSensitivity.SENSITIVE,
            GlobPatternComponent.textLiteral("Hello?")
        );
    }

    private void parseAndTestCheck(final String pattern,
                                   final CaseSensitivity caseSensitivity,
                                   final GlobPatternComponent... component) {
        GlobPattern.setNext(Lists.of(component));

        final GlobPattern parsed = GlobPattern.parse(
            pattern,
            caseSensitivity
        );

        final GlobPatternComponent expectedFirst = new GlobPattern(
            component[0],
            caseSensitivity,
            pattern
        ).first;

        this.checkEquals(
            parsed.first,
            expectedFirst,
            () -> "parse " + CharSequences.quoteAndEscape(pattern) + " " + caseSensitivity
        );

        this.textAndCheck(
            parsed,
            pattern
        );

        this.checkEquals(
            caseSensitivity,
            parsed.caseSensitivity(),
            parsed::toString
        );
    }

    // isOnlyTextLiteral................................................................................................

    @Test
    public void testIsOnlyTextLiteralText() {
        this.parseAndIsOnlyTextLiteralCheck(
            "abc123",
            true
        );
    }

    @Test
    public void testIsOnlyTextLiteralTextWithEscaped() {
        this.parseAndIsOnlyTextLiteralCheck(
            "abc \\* \\? 123",
            true
        );
    }

    @Test
    public void testIsOnlyTextLiteralQuestion() {
        this.parseAndIsOnlyTextLiteralCheck(
            "?",
            false
        );
    }

    @Test
    public void testIsOnlyTextLiteralStar() {
        this.parseAndIsOnlyTextLiteralCheck(
            "*",
            false
        );
    }

    @Test
    public void testIsOnlyTextLiteralTextQuestionText() {
        this.parseAndIsOnlyTextLiteralCheck(
            "/dir/?.txt",
            false
        );
    }

    @Test
    public void testIsOnlyTextLiteralTextStarText() {
        this.parseAndIsOnlyTextLiteralCheck(
            "/dir/*.txt",
            false
        );
    }

    private void parseAndIsOnlyTextLiteralCheck(final String pattern,
                                                final boolean expected) {
        this.checkEquals(
            expected,
            GlobPattern.parse(
                    pattern,
                    CaseSensitivity.SENSITIVE
                ).isOnlyTextLiteral(),
            () -> "parse " + CharSequences.quoteAndEscape(pattern) + " iOnlyTextLiteral()"
        );
    }

    // search...........................................................................................................

    @Test
    public void testSearchStartPositionNegativeFails() {
        this.searchStartPosFails(
            "abc",
            -1
        );
    }

    @Test
    public void testSearchStartPositionOutOfBoundsFails() {
        this.searchStartPosFails(
            "",
            1
        );
    }

    @Test
    public void testSearchStartPositionOutOfBoundsFails2() {
        this.searchStartPosFails(
            "abc",
            4
        );
    }

    private void searchStartPosFails(final String text,
                                     final int startPos) {
        assertThrows(
            StringIndexOutOfBoundsException.class,
            () -> GlobPattern.parse(
                "",
                CaseSensitivity.SENSITIVE
            ).search(text, startPos)
        );
    }

    @Test
    public void testSearchEmptyPatternEmptyText() {
        this.searchAndCheck(
            "",
            "",
            0
        );
    }

    @Test
    public void testSearchStarEmpty() {
        this.searchAndCheck(
            "*",
            "",
            0
        );
    }

    @Test
    public void testSearchQuestionEmpty() {
        this.searchAndCheck(
            "?",
            "",
            -1
        );
    }

    @Test
    public void testSearchTextLiteralEmpty() {
        this.searchAndCheck(
            "",
            "abc",
            0
        );
    }

    @Test
    public void testSearchTextLiteralPartial() {
        this.searchAndCheck(
            "a",
            "abc",
            0
        );
    }

    @Test
    public void testSearchTextLiteralPartial2() {
        this.searchAndCheck(
            "b",
            "abc",
            1
        );
    }

    @Test
    public void testSearchTextLiteralPartial3() {
        this.searchAndCheck(
            "c",
            "abcd",
            2
        );
    }

    @Test
    public void testSearchTextLiteralPartial4() {
        this.searchAndCheck(
            "d",
            "abcd",
            3
        );
    }

    // =SEARCH("the","The cat in the hat") // returns 1 (bias=1)
    @Test
    public void testSearch() {
        this.searchAndCheck(
            "the",
            "The cat in the hat",
            1 - 1
        );
    }

    // = SEARCH("the","The cat in the hat",4) // returns 12
    @Test
    public void testSearch1() {
        this.searchAndCheck(
            "the",
            "The cat in the hat",
            3,
            12 - 1
        );
    }

    // SEARCH("?at","The cat in the hat") // returns 5
    @Test
    public void testSearch2() {
        this.searchAndCheck(
            "?at",
            "The cat in the hat",
            5 - 1
        );
    }

    @Test
    public void testSearch3() {
        this.searchAndCheck(
            "?AT",
            "The cat in the hat",
            5 - 1
        );
    }

    // SEARCH("dog","The cat in the hat") // returns #VALUE!
    @Test
    public void testSearch4() {
        this.searchAndCheck(
            "dog",
            "The cat in the hat",
            -1
        );
    }

    // SEARCH("a","Apple") // returns 1
    @Test
    public void testSearch5() {
        this.searchAndCheck(
            "a",
            "Apple",
            1 - 1
        );
    }

    private void searchAndCheck(final String pattern,
                                final CharSequence search,
                                final int expected) {
        this.searchAndCheck(
            pattern,
            search,
            0,
            expected
        );
    }

    private void searchAndCheck(final String pattern,
                                final CharSequence search,
                                final int startPos,
                                final int expected) {
        this.searchAndCheck(
            pattern,
            CaseSensitivity.INSENSITIVE,
            search,
            startPos,
            expected
        );
    }

    private void searchAndCheck(final String pattern,
                                final CaseSensitivity sensitivity,
                                final CharSequence search,
                                final int startPos,
                                final int expected) {
        this.searchAndCheck(
            GlobPattern.parse(
                pattern,
                sensitivity
            ),
            search,
            startPos,
            expected
        );
    }

    @Test
    public void testSearchTwice() {
        final GlobPattern pattern = GlobPattern.parse(
            "/dir/file-?/*.txt",
            CaseSensitivity.SENSITIVE
        );

        this.searchAndCheck(pattern, "/dir/file-1/big.txt", 0, 0);
        this.searchAndCheck(pattern, "/dir/file-2/small.txt", 0, 0);
    }


    @Test
    public void testSearchTwice2() {
        final GlobPattern pattern = GlobPattern.parse(
            "file-???-*.txt",
            CaseSensitivity.SENSITIVE
        );

        this.searchAndCheck(pattern, "/dir/file-123-hello.txt", 0, 5);
        this.searchAndCheck(pattern, "file-456-important.txt", 0, 0);
    }

    private void searchAndCheck(final GlobPattern pattern,
                                final CharSequence search,
                                final int startPos,
                                final int expected) {
        this.checkEquals(
            expected,
            pattern.search(
                search,
                startPos
            ),
            () -> "search " + pattern + " for " + CharSequences.quoteAndEscape(search) + " startPos=" + startPos
        );
    }

    // Predicate........................................................................................................

    @Test
    public void testTestTextLiteralEmptyText() {
        this.parseThenTestAndCheck(
            "A",
            "",
            false
        );
    }

    @Test
    public void testTestTextOneCharacter() {
        this.parseThenTestAndCheck(
            "A",
            "a",
            true
        );
    }

    @Test
    public void testTestTextOneDifferentCharacter() {
        this.parseThenTestAndCheck(
            "A",
            "!",
            false
        );
    }

    @Test
    public void testTestTextManyCharacters() {
        this.parseThenTestAndCheck(
            "abc",
            "abc",
            true
        );
    }

    @Test
    public void testTestTextWithNumbers() {
        this.parseThenTestAndCheck(
            "abc123",
            "abc123",
            true
        );
    }

    @Test
    public void testTestTextDifferentCaseUnimportant() {
        this.parseThenTestAndCheck(
            "ABC",
            "abc",
            true
        );
    }

    @Test
    public void testTestTextDifferentCaseSignificant() {
        this.parseThenTestAndCheck(
            "ABC",
            CaseSensitivity.SENSITIVE,
            "abc",
            false
        );
    }

    @Test
    public void testTestQuestionEmptyText() {
        this.parseThenTestAndCheck(
            "?",
            "",
            false
        );
    }

    @Test
    public void testTestQuestion() {
        this.parseThenTestAndCheck(
            "?",
            "A",
            true
        );
    }

    @Test
    public void testTestQuestion2() {
        this.parseThenTestAndCheck(
            "??",
            "AB",
            true
        );
    }

    @Test
    public void testTestQuestionExtraText() {
        this.parseThenTestAndCheck(
            "?",
            "AB",
            false
        );
    }

    @Test
    public void testTestQuestionExtraText2() {
        this.parseThenTestAndCheck(
            "??",
            "ABC",
            false
        );
    }

    @Test
    public void testTestQuestionExtraText3() {
        this.parseThenTestAndCheck(
            "??",
            "ABCD",
            false
        );
    }

    @Test
    public void testTestStarEmptyText() {
        this.parseThenTestAndCheck(
            "*",
            "",
            true
        );
    }

    @Test
    public void testTestStar() {
        this.parseThenTestAndCheck(
            "*",
            "A",
            true
        );
    }

    @Test
    public void testTestStar2() {
        this.parseThenTestAndCheck(
            "*",
            "AB",
            true
        );
    }

    @Test
    public void testTestStar3() {
        this.parseThenTestAndCheck(
            "*",
            "ABc123",
            true
        );
    }

    @Test
    public void testTestStarTextStar() {
        this.parseThenTestAndCheck(
            "*Hello*",
            "123Hello456",
            true
        );
    }

    @Test
    public void testTestStarTextStarFalse() {
        this.parseThenTestAndCheck(
            "*Hello*",
            "123Hell456",
            false
        );
    }

    @Test
    public void testTestHyphenQuestion() {
        this.parseThenTestAndCheck(
            "-?",
            "-1",
            true
        );
    }

    @Test
    public void testTestHyphenQuestionFalse() {
        this.parseThenTestAndCheck(
            "-?",
            "-12",
            false
        );
    }

    @Test
    public void testTestHyphenQuestionFalse2() {
        this.parseThenTestAndCheck(
            "-?",
            "-123",
            false
        );
    }

    @Test
    public void testTestQuestionHyphenQuestion() {
        this.parseThenTestAndCheck(
            "?-?",
            "1-2",
            true
        );
    }

    @Test
    public void testTestQuestion3HyphenQuestion2() {
        this.parseThenTestAndCheck(
            "???-??",
            "123-45",
            true
        );
    }

    @Test
    public void testTestQuestion3HyphenQuestion2False() {
        this.parseThenTestAndCheck(
            "???-??",
            "123-456",
            false
        );
    }

    @Test
    public void testTestQuestion3HyphenQuestion2False2() {
        this.parseThenTestAndCheck(
            "???-??",
            "0123-45",
            false
        );
    }

    @Test
    public void testTestWildcardFilenameWithExtension() {
        this.parseThenTestAndCheck(
            "*.txt",
            "file123.txt",
            true
        );
    }

    @Test
    public void testTestWildcardFilenameWithExtension2() {
        this.parseThenTestAndCheck(
            "*.txt",
            "file123.wrong",
            false
        );
    }

    @Test
    public void testTestWildcardFilenameWithExtension3() {
        this.parseThenTestAndCheck(
            "*.txt",
            "file123.txt2",
            false
        );
    }

    @Test
    public void testTestWildcardFilenameWithExtension4() {
        this.parseThenTestAndCheck(
            "?*.txt",
            "file123.txt",
            true
        );
    }

    @Test
    public void testTestWildcardFilenameWithExtension5() {
        this.parseThenTestAndCheck(
            "?*.txt",
            ".txt",
            false
        );
    }

    @Test
    public void testPathWithWildcards() {
        this.parseThenTestAndCheck(
            "/user/Miroslav/*.txt",
            "/user/Miroslav/passwords.txt",
            true
        );
    }

    @Test
    public void testPathWithWildcards2() {
        this.parseThenTestAndCheck(
            "/user/Miroslav/*.txt",
            "/user/NotMe/passwords.txt",
            false
        );
    }

    @Test
    public void testPathWithWildcards3() {
        this.parseThenTestAndCheck(
            "/user/Miroslav/*.txt",
            "/user/Miroslav/Hello.java",
            false
        );
    }

    @Test
    public void testPathWithEscape() {
        this.parseThenTestAndCheck(
            "/user/Miroslav/\\**.txt",
            "/user/Miroslav/*passwords.txt",
            true
        );
    }

    @Test
    public void testPathWithEscape2() {
        this.parseThenTestAndCheck(
            "/user/Miroslav/\\**.txt",
            "/user/Miroslav/passwords.txt",
            false
        );
    }

    private void parseThenTestAndCheck(final String pattern,
                                       final CharSequence test,
                                       final boolean expected) {
        this.parseThenTestAndCheck(
            pattern,
            CaseSensitivity.INSENSITIVE,
            test,
            expected
        );
    }

    private void parseThenTestAndCheck(final String pattern,
                                       final CaseSensitivity sensitivity,
                                       final CharSequence test,
                                       final boolean expected) {
        this.testAndCheck2(
            GlobPattern.parse(
                pattern,
                sensitivity
            ),
            test,
            expected
        );
    }

    private void testAndCheck2(final GlobPattern pattern,
                               final CharSequence test,
                               final boolean expected) {
        this.testAndCheck(
            pattern,
            test,
            expected
        );

        // search should give 0 when test is true
        if (expected) {
            this.searchAndCheck(
                pattern,
                test,
                0,
                0
            );
        }
    }

    @Override
    public GlobPattern createPredicate() {
        return GlobPattern.parse(
            "Hello",
            CaseSensitivity.SENSITIVE
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentComponent() {
        this.checkEquals(
            new GlobPattern(
                GlobPatternComponent.wildcard(0, 2), // ignored by equals
                CaseSensitivity.SENSITIVE,
                "?"
            )
        );
    }

    @Test
    public void testEqualsDifferentCaseSensitivity() {
        this.checkNotEquals(
            new GlobPattern(
                GlobPatternComponent.wildcard(0, 1),
                CaseSensitivity.INSENSITIVE,
                "?"
            )
        );
    }

    @Test
    public void testEqualsDifferentPattern() {
        this.checkNotEquals(
            new GlobPattern(
                GlobPatternComponent.wildcard(0, 1),
                CaseSensitivity.SENSITIVE,
                "????"
            )
        );
    }

    @Override
    public GlobPattern createObject() {
        return new GlobPattern(
            GlobPatternComponent.wildcard(0, 1),
            CaseSensitivity.SENSITIVE,
            "?"
        );
    }

    // class.............................................................................................................

    // Ignore
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<GlobPattern> type() {
        return GlobPattern.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
