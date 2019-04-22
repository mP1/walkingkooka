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
 * See the License for the specific language governing permissions or
 * limitations under the License.
 *
 *
 */

package walkingkooka.tree.search;

import org.junit.jupiter.api.Test;

public final class SearchOrQueryTest extends SearchBinaryQueryTestCase<SearchOrQuery> {

    @Test
    public final void testNeither() {
        final SearchBigDecimalNode node = this.bigDecimalNode(VALUE);

        final SearchQuery query = this.bigDecimalQueryValue(VALUE_LT).equalsQuery()
                .or(this.bigDecimalQueryValue(VALUE_GT).equalsQuery());

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testBoth() {
        final SearchBigIntegerNode node = this.bigIntegerNode(VALUE);

        final SearchQuery query = this.bigIntegerQueryValue(VALUE_LT).greaterThan()
                .or(this.bigIntegerQueryValue(VALUE_GT).lessThan());

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testOnlyLeft() {
        final SearchDoubleNode node = this.doubleNode(VALUE);

        final SearchQuery query = this.doubleQueryValue(VALUE).equalsQuery()
                .or(this.doubleQueryValue(VALUE_LT).equalsQuery());

        this.querySelectAndCheck(query, node, node.selected());
    }


    @Test
    public final void testOnlyRight() {
        final SearchLongNode node = this.longNode(VALUE);

        final SearchQuery query = this.longQueryValue(VALUE_LT).equalsQuery()
                .or(this.longQueryValue(VALUE).equalsQuery());

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testBothLocalDate() {
        final SearchLocalDateNode node = this.localDateNode(DATE);

        final SearchQuery query = this.localDateQueryValue(DATE_LT).greaterThan()
                .or(this.localDateQueryValue(DATE_GT).lessThan());

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testBothLocalDateTime() {
        final SearchLocalDateTimeNode node = this.localDateTimeNode(DATETIME);

        final SearchQuery query = this.localDateTimeQueryValue(DATETIME_LT).greaterThan()
                .or(this.localDateTimeQueryValue(DATETIME_GT).lessThan());

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testBothLocalTime() {
        final SearchLocalTimeNode node = this.localTimeNode(TIME);

        final SearchQuery query = this.localTimeQueryValue(TIME_LT).greaterThan()
                .or(this.localTimeQueryValue(TIME_GT).lessThan());

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Override
    SearchOrQuery createSearchQuery(final SearchQuery left, final SearchQuery right) {
        return SearchOrQuery.with(left, right);
    }

    @Override
    public Class<SearchOrQuery> type() {
        return SearchOrQuery.class;
    }
}
