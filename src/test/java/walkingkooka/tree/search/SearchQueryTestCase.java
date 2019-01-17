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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.type.MemberVisibility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public abstract class SearchQueryTestCase<Q extends SearchQuery> extends ClassTestCase<Q>
        implements HashCodeEqualsDefinedTesting<Q> {

    static final String DATE = "2000-01-31";
    static final String DATE_LT = "1999-12-31";
    static final String DATE_GT = "2002-02-02";

    static final String DATETIME = "2000-01-31T13:58:59";
    static final String DATETIME_LT = "1999-12-31T06:00:00";
    static final String DATETIME_GT = "2002-02-02T02:22:22";

    static final String TIME = "12:58:59";
    static final String TIME_LT = "06:00:00";
    static final String TIME_GT = "18:18:18";

    static final int VALUE = 123;
    static final int VALUE_LT = 99;
    static final int VALUE_GT = 234;

    static final String TEXT = "mnop";
    static final String TEXT_LT = "abc";
    static final String TEXT_GT = "xyz";

    static final String TEXT2 = "mNop";
    static final String TEXT2_LT = "aBc";
    static final String TEXT2_GT = "xYz";

    SearchQueryTestCase() {
        super();
    }

    // tests.........................................................................................................

    @Test
    public void testNot() {
        final Q query = this.createSearchQuery();
        final SearchQuery not = query.not();
        assertNotEquals(query, not);
    }

    @Test
    public void testNotTwiceGivesOriginalQuery() {
        final Q query = this.createSearchQuery();
        assertEquals(query, query.not().not());
    }

    // query testing.....................................................................................................

    final void querySelectAndCheck(final SearchNode node, final SearchNode expected) {
        this.querySelectAndCheck(this.createSearchQuery(), node, expected);
    }

    final void querySelectAndCheck(final SearchQuery query, final SearchNode node, final SearchNode expected) {
        final SearchNode result = query.select(node);
        this.checkEquals("Query " + query + " returned wrong result", expected, result);
    }

    final void querySelectAndFail(final SearchQuery query, final SearchNode node) {
        final SearchNode after = query.select(node);
        this.checkSame("Query " + query + " should have selected nothing", node, after);
    }

    private void checkSame(final String message, final SearchNode expected, final SearchNode actual) {
        if(expected!=actual){
            assertSame(message,
                    this.toString(expected),
                    this.toString(expected));
        }
    }

    private void checkEquals(final String message, final SearchNode expected, final SearchNode actual) {
        if(!expected.equals(actual)){
            assertEquals(message,
                    this.toString(expected),
                    this.toString(actual));
        }
    }

    final String toString(final SearchNode node) {
        return SearchPrettySearchNodeVisitor.toString(node);
    }

    abstract Q createSearchQuery();

    // factory.....................................................................................................

    final BigDecimal bigDecimal(final double value) {
        return BigDecimal.valueOf(value);
    }

    final BigInteger bigInteger(final long value) {
        return BigInteger.valueOf(value);
    }

    final LocalDate localDate(final String value) {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    final LocalDateTime localDateTime(final String value) {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    final LocalTime localTime(final String value) {
        return LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME);
    }
    
    // Node...............................................................................

    final SearchNode before() {
        return this.textNode("a");
    }

    final SearchNode after() {
        return this.textNode("z");
    }

    final SearchBigDecimalNode bigDecimalNode(final double value) {
        return this.bigDecimalNode(this.bigDecimal(value));
    }

    final SearchBigDecimalNode bigDecimalNode(final BigDecimal value) {
        return SearchNode.bigDecimal(value.toString(), value);
    }

    final SearchBigIntegerNode bigIntegerNode(final long value) {
        return this.bigIntegerNode(this.bigInteger(value));
    }

    final SearchBigIntegerNode bigIntegerNode(final BigInteger value) {
        return SearchNode.bigInteger(value.toString(), value);
    }

    final SearchDoubleNode doubleNode(final double value) {
        return SearchNode.doubleNode(String.valueOf(value), value);
    }

    final SearchLocalDateNode localDateNode(final String value) {
        return this.localDateNode(this.localDate(value));
    }

    final SearchLocalDateNode localDateNode(final LocalDate value) {
        return SearchNode.localDate(DateTimeFormatter.ISO_LOCAL_DATE.format(value), value);
    }

    final SearchLocalDateTimeNode localDateTimeNode(final String value) {
        return this.localDateTimeNode(this.localDateTime(value));
    }

    final SearchLocalDateTimeNode localDateTimeNode(final LocalDateTime value) {
        return SearchNode.localDateTime(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value), value);
    }

    final SearchLocalTimeNode localTimeNode(final String value) {
        return this.localTimeNode(this.localTime(value));
    }

    final SearchLocalTimeNode localTimeNode(final LocalTime value) {
        return SearchNode.localTime(DateTimeFormatter.ISO_LOCAL_TIME.format(value), value);
    }
    
    final SearchLongNode longNode(final long value) {
        return SearchNode.longNode(String.valueOf(value), value);
    }

    final SearchTextNode textNode(final String value) {
        return SearchNode.text(value, value);
    }

    final SearchSelectNode selectNode(final SearchNode child) {
        return SearchNode.select(child);
    }

    final SearchSequenceNode sequenceNode(final SearchNode...children) {
        return SearchNode.sequence(Lists.of(children));
    }

    // QueryValue...............................................................................

    final SearchBigDecimalQueryValue bigDecimalQueryValue(final double value) {
        return this.bigDecimalQueryValue(this.bigDecimal(value));
    }

    final SearchBigDecimalQueryValue bigDecimalQueryValue(final BigDecimal value) {
        return SearchQueryValue.bigDecimal(value);
    }

    final SearchBigIntegerQueryValue bigIntegerQueryValue(final long value) {
        return this.bigIntegerQueryValue(this.bigInteger(value));
    }

    final SearchBigIntegerQueryValue bigIntegerQueryValue(final BigInteger value) {
        return SearchQueryValue.bigInteger(value);
    }

    final SearchDoubleQueryValue doubleQueryValue(final double value) {
        return SearchQueryValue.doubleValue(value);
    }

    final SearchLocalDateQueryValue localDateQueryValue(final String value) {
        return this.localDateQueryValue(this.localDate(value));
    }

    final SearchLocalDateQueryValue localDateQueryValue(final LocalDate value) {
        return SearchQueryValue.localDate(value);
    }

    final SearchLocalDateTimeQueryValue localDateTimeQueryValue(final String value) {
        return this.localDateTimeQueryValue(this.localDateTime(value));
    }

    final SearchLocalDateTimeQueryValue localDateTimeQueryValue(final LocalDateTime value) {
        return SearchQueryValue.localDateTime(value);
    }

    final SearchLocalTimeQueryValue localTimeQueryValue(final String value) {
        return this.localTimeQueryValue(this.localTime(value));
    }

    final SearchLocalTimeQueryValue localTimeQueryValue(final LocalTime value) {
        return SearchQueryValue.localTime(value);
    }

    final SearchLongQueryValue longQueryValue(final long value) {
        return SearchQueryValue.longValue(value);
    }

    final SearchTextQueryValue textQueryValue(final String value) {
        return SearchQueryValue.text(value);
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public final Q createObject() {
        return this.createSearchQuery();
    }
}
