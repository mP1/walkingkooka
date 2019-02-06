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

package walkingkooka.tree.search;

import org.junit.jupiter.api.Test;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;

import static org.junit.jupiter.api.Assertions.assertFalse;

public final class SearchContainsQueryTest extends SearchLeafQueryTestCase<SearchContainsQuery> {

    private final static CaseSensitivity SENSITIVITY = CaseSensitivity.INSENSITIVE;
    
    @Override
    public void testNot() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testNotTwiceGivesOriginalQuery() {
        throw new UnsupportedOperationException();
    }

    // BigDecimal......................................................................................

    @Test
    public void testBigDecimal() {
        this.querySensitiveSelectAndCheck(this.bigDecimalNode(VALUE));
    }

    // BigInteger......................................................................................

    @Test
    public void testBigInteger() {
        this.querySensitiveSelectAndCheck(this.bigIntegerNode(VALUE));
    }

    // Double......................................................................................

    @Test
    public void testDouble() {
        this.querySensitiveSelectAndCheck(this.doubleNode(VALUE));
    }

    // LocalDate......................................................................................

    @Test
    public void testLocalDate() {
        this.querySensitiveSelectAndCheck(this.localDateNode(DATE));
    }

    // LocalDateTime......................................................................................

    @Test
    public final void testLocalDateTime() {
        this.querySensitiveSelectAndCheck(this.localDateTimeNode(DATETIME));
    }

    // LocalTime......................................................................................

    @Test
    public final void testLocalTime() {
        this.querySensitiveSelectAndCheck(this.localTimeNode(TIME));
    }

    // Long......................................................................................

    @Test
    public final void testLong() {
        this.querySensitiveSelectAndCheck(this.longNode(VALUE));
    }

    // Text......................................................................................

    @Test
    public final void testTextAbsent() {
        this.querySelectAndFail(this.textQueryValue("$")
                .contains(SENSITIVITY),
                this.textNode(TEXT));
    }

    @Test
    public final void testText() {
        this.querySensitiveSelectAndCheck(this.textNode(TEXT));
    }

    @Test
    public final void testTextMultiple() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("Q1Q");
        final SearchNode selected = this.textNode("Q")
                .selected();

        final SearchQuery query = this.textQueryValue("Q").contains(SENSITIVITY);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(selected, this.textNode("1"), selected),
                                after));
    }

    @Test
    public final void testTextMultiple2() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("Q-Q+QQ");
        final SearchNode selected = this.textNode("Q")
                .selected();

        final SearchQuery query = this.textQueryValue("Q").contains(SENSITIVITY);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(selected, this.textNode("-"), selected, this.textNode("+"), selected, selected),
                        after));
    }

    @Test
    public final void testTextContainsOrNotEquals() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("123");
        final SearchNode selected = text.selected();

        final SearchQuery query = this.textQueryValue("123").
                contains(SENSITIVITY)
                .or(SearchQueryValue.text("Q").notEquals(SENSITIVITY));

        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        selected,
                        after));
    }

    @Test
    public final void testTextContainsOrNotEquals2() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("123");

        final SearchQuery query = this.textQueryValue("2").
                contains(SENSITIVITY)
                .or(SearchQueryValue.text("Q").notEquals(SENSITIVITY));

        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(this.textNode("1"), this.textNode("2").selected(), this.textNode("3")),
                        after));
    }

    @Test
    public final void testTextContainsOrContains() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("123");

        final SearchQuery query = this.textQueryValue("2").
                contains(SENSITIVITY)
                .or(SearchQueryValue.text("2").contains(SENSITIVITY));

        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(this.textNode("1"), this.textNode("2").selected(), this.textNode("3")),
                        after));
    }

    @Test
    public final void testTextContainsOrContains2() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("12345");

        final SearchQuery query = this.textQueryValue("234").
                contains(SENSITIVITY)
                .or(SearchQueryValue.text("3").contains(SENSITIVITY));

        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(this.textNode("1"), this.textNode("234").selected(), this.textNode("5")),
                        after));
    }

    @Test
    public final void testTextContainsAndContains() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("12345");

        final SearchQuery query = this.textQueryValue("234").
                contains(SENSITIVITY)
                .or(SearchQueryValue.text("3").contains(SENSITIVITY));

        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(this.textNode("1"), this.textNode("234").selected(), this.textNode("5")),
                        after));
    }

    // Text Insensitive.................................................................................................

    @Test
    public final void testTextInsensitiveAbsent() {
        this.querySelectAndFail(this.textQueryValue("$")
                        .contains(CaseSensitivity.INSENSITIVE),
                this.textNode(TEXT));
    }

    @Test
    public final void testTextInsensitive() {
        this.queryInsensitiveSelectAndCheck(this.textNode(TEXT));
    }

    @Test
    public final void testTextInsensitiveMultiple() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("q1Q");

        final SearchQuery query = this.textQueryValue("Q").contains(CaseSensitivity.INSENSITIVE);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(this.textNode("q").selected(), this.textNode("1"), this.textNode("Q").selected()),
                        after));
    }

    @Test
    public final void testTextInsensitiveMultiple2() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode text = this.textNode("Q-q+qq");
        final SearchNode selected = this.textNode("q")
                .selected();

        final SearchQuery query = this.textQueryValue("Q").contains(CaseSensitivity.INSENSITIVE);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, text, after),
                this.sequenceNode(before,
                        this.sequenceNode(this.textNode("Q").selected(), this.textNode("-"), selected, this.textNode("+"), selected, selected),
                        after));
    }

    private void querySensitiveSelectAndCheck(final SearchNode node) {
        this.querySelectAndCheck2(node, SENSITIVITY);
    }

    private void queryInsensitiveSelectAndCheck(final SearchNode node) {
        this.querySelectAndCheck2(node, CaseSensitivity.INSENSITIVE);
    }

    private void querySelectAndCheck2(final SearchNode node, final CaseSensitivity caseSensitivity) {
        final String text = node.text();
        final String first = text.substring(0, 1);
        final String remainder = text.substring(1);
        assertFalse(remainder.contains(first),
                () -> "First letter " + CharSequences.quoteAndEscape(text.charAt(0)) + " must only appear once in text " + CharSequences.quoteAndEscape(text));

        this.querySelectAndCheck3(first,
                caseSensitivity,
                node,
                this.textNode(first).selected(), this.textNode(remainder));
    }

    private void querySelectAndCheck3(final String text, final CaseSensitivity sensitivity, final SearchNode node, final SearchNode...expected) {
        this.querySelectAndCheck(SearchQueryValue.text(text).contains(sensitivity),
                node,
                this.sequenceNode(expected));
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(SearchContainsQuery.with(this.textQueryValue("different"), this.caseSensitivity()));
    }

    @Test
    public void testEqualsDifferentCaseSensitivity() {
        this.checkNotEquals(SearchContainsQuery.with(this.queryTextValue(), SENSITIVITY.invert()));
    }

    @Override
    SearchContainsQuery createSearchQuery() {
        return SearchContainsQuery.with(this.queryTextValue(), this.caseSensitivity());
    }

    private SearchTextQueryValue queryTextValue() {
        return this.textQueryValue("query");
    }

    private CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public Class<SearchContainsQuery> type() {
        return SearchContainsQuery.class;
    }
}
