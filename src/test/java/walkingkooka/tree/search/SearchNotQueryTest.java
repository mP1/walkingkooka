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

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.text.CaseSensitivity;

public final class SearchNotQueryTest extends SearchParentQueryTestCase<SearchNotQuery>{

    @Test
    public final void testBigDecimal() {
        final SearchBigDecimalNode node = this.bigDecimalNode(VALUE);
        final SearchQuery query = this.bigDecimalQueryValue(VALUE_LT).greaterThan()
                .and(this.bigDecimalQueryValue(VALUE_GT).lessThan())
                .not();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testBigInteger() {
        final SearchBigIntegerNode node = this.bigIntegerNode(VALUE);
        final SearchQuery query = this.bigIntegerQueryValue(VALUE_LT).greaterThan()
                .and(this.bigIntegerQueryValue(VALUE_GT).lessThan())
                .not();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testDouble() {
        final SearchDoubleNode node = this.doubleNode(VALUE);
        final SearchQuery query = this.doubleQueryValue(VALUE_LT).greaterThan()
                .and(this.doubleQueryValue(VALUE_GT).lessThan())
                .not();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLocalDate() {
        final SearchLocalDateNode node = this.localDateNode(DATE);
        final SearchQuery query = this.localDateQueryValue(DATE_LT).greaterThan()
                .and(this.localDateQueryValue(DATE_GT).lessThan())
                .not();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLocalDateTime() {
        final SearchLocalDateTimeNode node = this.localDateTimeNode(DATETIME);
        final SearchQuery query = this.localDateTimeQueryValue(DATETIME_LT).greaterThan()
                .and(this.localDateTimeQueryValue(DATETIME_GT).lessThan())
                .not();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLocalTime() {
        final SearchLocalTimeNode node = this.localTimeNode(TIME);
        final SearchQuery query = this.localTimeQueryValue(TIME_LT).greaterThan()
                .and(this.localTimeQueryValue(TIME_GT).lessThan())
                .not();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLong() {
        final SearchLongNode node = this.longNode(VALUE);
        final SearchQuery query = this.longQueryValue(VALUE_LT).greaterThan()
                .and(this.longQueryValue(VALUE_GT).lessThan())
                .not();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    @Ignore
    public void testNotTwiceGivesOriginalQuery() {
        throw new UnsupportedOperationException();
    }

    @Override
    SearchNotQuery createSearchQuery() {
        return SearchNotQuery.with(this.textQueryValue("query").equalsQuery(CaseSensitivity.SENSITIVE));
    }
    
    @Override
    protected Class<SearchNotQuery> type() {
        return SearchNotQuery.class;
    }
}
