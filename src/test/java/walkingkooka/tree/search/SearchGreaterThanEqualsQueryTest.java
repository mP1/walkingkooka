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

package walkingkooka.tree.search;

import org.junit.jupiter.api.Test;
import walkingkooka.text.CaseSensitivity;

public final class SearchGreaterThanEqualsQueryTest extends SearchValueComparisonLeafQueryTestCase<SearchGreaterThanEqualsQuery> {

    // BigDecimal......................................................................................

    @Test
    public final void testBigDecimalLess() {
        final SearchBigDecimalNode node = this.bigDecimalNode(VALUE_LT);
        final SearchQuery query = this.bigDecimalQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testBigDecimalEqual() {
        final SearchBigDecimalNode node = this.bigDecimalNode(VALUE);
        final SearchQuery query = this.bigDecimalQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testBigDecimalGreater() {
        final SearchBigDecimalNode node = this.bigDecimalNode(VALUE_GT);
        final SearchQuery query = this.bigDecimalQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testBigDecimalReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchBigDecimalNode eq = this.bigDecimalNode(VALUE);
        final SearchBigDecimalNode lt = this.bigDecimalNode(VALUE_LT);
        final SearchBigDecimalNode gt = this.bigDecimalNode(VALUE_GT);

        final SearchQuery query = this.bigDecimalQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testBigDecimalNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchBigDecimalNode eq = this.bigDecimalNode(VALUE);
        final SearchBigDecimalNode lt = this.bigDecimalNode(VALUE_LT);
        final SearchBigDecimalNode gt = this.bigDecimalNode(VALUE_GT);

        final SearchQuery query = this.bigDecimalQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testBigDecimalIgnoresOtherTypes() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchBigDecimalNode eq = this.bigDecimalNode(VALUE);
        final SearchBigDecimalNode lt = this.bigDecimalNode(VALUE_LT);
        final SearchBigDecimalNode gt = this.bigDecimalNode(VALUE_GT);

        final SearchBigIntegerNode bigIntegerNode = this.bigIntegerNode(VALUE);
        final SearchDoubleNode doubleNode = this.doubleNode(VALUE);
        final SearchLocalDateNode dateNode = this.localDateNode(DATE);
        final SearchLocalDateTimeNode dateTimeNode = this.localDateTimeNode(DATETIME);
        final SearchLocalTimeNode timeNode = this.localTimeNode(TIME);
        final SearchLongNode longNode = this.longNode(VALUE);
        final SearchTextNode textNode = this.textNode(TEXT);

        final SearchQuery query = this.bigDecimalQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after, bigIntegerNode, doubleNode, dateNode, dateTimeNode, timeNode, longNode, textNode),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after, bigIntegerNode, doubleNode, dateNode, dateTimeNode, timeNode, longNode, textNode));
    }

    @Test
    public final void testBigDecimalLessNot() {
        final SearchBigDecimalNode node = this.bigDecimalNode(VALUE_LT);
        final SearchQuery query = this.bigDecimalQueryValue(VALUE).greaterThanEquals().not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // BigInteger......................................................................................

    @Test
    public final void testBigIntegerLess() {
        final SearchBigIntegerNode node = this.bigIntegerNode(VALUE_LT);
        final SearchQuery query = this.bigIntegerQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testBigIntegerEqual() {
        final SearchBigIntegerNode node = this.bigIntegerNode(VALUE);
        final SearchQuery query = this.bigIntegerQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testBigIntegerGreater() {
        final SearchBigIntegerNode node = this.bigIntegerNode(VALUE_GT);
        final SearchQuery query = this.bigIntegerQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testBigIntegerReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchBigIntegerNode eq = this.bigIntegerNode(VALUE);
        final SearchBigIntegerNode lt = this.bigIntegerNode(VALUE_LT);
        final SearchBigIntegerNode gt = this.bigIntegerNode(VALUE_GT);

        final SearchQuery query = this.bigIntegerQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testBigIntegerNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchBigIntegerNode eq = this.bigIntegerNode(VALUE);
        final SearchBigIntegerNode lt = this.bigIntegerNode(VALUE_LT);
        final SearchBigIntegerNode gt = this.bigIntegerNode(VALUE_GT);

        final SearchQuery query = this.bigIntegerQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testBigIntegerIgnoresOtherTypes() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchBigIntegerNode eq = this.bigIntegerNode(VALUE);
        final SearchBigIntegerNode lt = this.bigIntegerNode(VALUE_LT);
        final SearchBigIntegerNode gt = this.bigIntegerNode(VALUE_GT);

        final SearchBigDecimalNode bigDecimalNode = this.bigDecimalNode(VALUE);
        final SearchDoubleNode doubleNode = this.doubleNode(VALUE);
        final SearchLocalDateNode dateNode = this.localDateNode(DATE);
        final SearchLocalDateTimeNode dateTimeNode = this.localDateTimeNode(DATETIME);
        final SearchLocalTimeNode timeNode = this.localTimeNode(TIME);
        final SearchLongNode longNode = this.longNode(VALUE);
        final SearchTextNode textNode = this.textNode(TEXT);

        final SearchQuery query = this.bigIntegerQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after, bigDecimalNode, doubleNode, dateNode, dateTimeNode, timeNode, longNode, textNode),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after, bigDecimalNode, doubleNode, dateNode, dateTimeNode, timeNode, longNode, textNode));
    }

    @Test
    public final void testBigIntegerLessNot() {
        final SearchBigIntegerNode node = this.bigIntegerNode(VALUE_LT);
        final SearchQuery query = this.bigIntegerQueryValue(VALUE).greaterThanEquals().not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // Double......................................................................................

    @Test
    public final void testDoubleLess() {
        final SearchDoubleNode node = this.doubleNode(VALUE_LT);
        final SearchQuery query = this.doubleQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testDoubleEqual() {
        final SearchDoubleNode node = this.doubleNode(VALUE);
        final SearchQuery query = this.doubleQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testDoubleGreater() {
        final SearchDoubleNode node = this.doubleNode(VALUE_GT);
        final SearchQuery query = this.doubleQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testDoubleReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchDoubleNode eq = this.doubleNode(VALUE);
        final SearchDoubleNode lt = this.doubleNode(VALUE_LT);
        final SearchDoubleNode gt = this.doubleNode(VALUE_GT);

        final SearchQuery query = this.doubleQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testDoubleNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchDoubleNode eq = this.doubleNode(VALUE);
        final SearchDoubleNode lt = this.doubleNode(VALUE_LT);
        final SearchDoubleNode gt = this.doubleNode(VALUE_GT);

        final SearchQuery query = this.doubleQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testDoubleIgnoresOtherTypes() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchDoubleNode eq = this.doubleNode(VALUE);
        final SearchDoubleNode lt = this.doubleNode(VALUE_LT);
        final SearchDoubleNode gt = this.doubleNode(VALUE_GT);

        final SearchBigDecimalNode bigDecimalNode = this.bigDecimalNode(VALUE);
        final SearchBigIntegerNode bigIntegerNode = this.bigIntegerNode(VALUE);
        final SearchLocalDateNode dateNode = this.localDateNode(DATE);
        final SearchLocalDateTimeNode dateTimeNode = this.localDateTimeNode(DATETIME);
        final SearchLocalTimeNode timeNode = this.localTimeNode(TIME);
        final SearchLongNode longNode = this.longNode(VALUE);
        final SearchTextNode textNode = this.textNode(TEXT);

        final SearchQuery query = this.doubleQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after, bigDecimalNode, bigIntegerNode, dateNode, dateTimeNode, timeNode, longNode, textNode),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after, bigDecimalNode, bigIntegerNode, dateNode, dateTimeNode, timeNode, longNode, textNode));
    }

    @Test
    public final void testDoubleLessNot() {
        final SearchDoubleNode node = this.doubleNode(VALUE_LT);
        final SearchQuery query = this.doubleQueryValue(VALUE).greaterThanEquals().not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // LocalDate......................................................................................

    @Test
    public final void testLocalDateLess() {
        final SearchLocalDateNode node = this.localDateNode(DATE_LT);
        final SearchQuery query = this.localDateQueryValue(DATE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLocalDateEqual() {
        final SearchLocalDateNode node = this.localDateNode(DATE);
        final SearchQuery query = this.localDateQueryValue(DATE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLocalDateGreater() {
        final SearchLocalDateNode node = this.localDateNode(DATE_GT);
        final SearchQuery query = this.localDateQueryValue(DATE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLocalDateReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalDateNode eq = this.localDateNode(DATE);
        final SearchLocalDateNode lt = this.localDateNode(DATE_LT);
        final SearchLocalDateNode gt = this.localDateNode(DATE_GT);

        final SearchQuery query = this.localDateQueryValue(DATE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLocalDateNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalDateNode eq = this.localDateNode(DATE);
        final SearchLocalDateNode lt = this.localDateNode(DATE_LT);
        final SearchLocalDateNode gt = this.localDateNode(DATE_GT);

        final SearchQuery query = this.localDateQueryValue(DATE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLocalDateIgnoresOtherTypes() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalDateNode eq = this.localDateNode(DATE);
        final SearchLocalDateNode lt = this.localDateNode(DATE_LT);
        final SearchLocalDateNode gt = this.localDateNode(DATE_GT);

        final SearchBigDecimalNode bigDecimalNode = this.bigDecimalNode(VALUE);
        final SearchBigIntegerNode bigIntegerNode = this.bigIntegerNode(VALUE);
        final SearchDoubleNode doubleNode = this.doubleNode(VALUE);
        final SearchLocalDateTimeNode dateTimeNode = this.localDateTimeNode(DATETIME);
        final SearchLocalTimeNode timeNode = this.localTimeNode(TIME);
        final SearchLongNode longNode = this.longNode(VALUE);
        final SearchTextNode textNode = this.textNode(TEXT);

        final SearchQuery query = this.localDateQueryValue(DATE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after, bigDecimalNode, bigIntegerNode, doubleNode, dateTimeNode, timeNode, longNode, textNode),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after, bigDecimalNode, bigIntegerNode, doubleNode, dateTimeNode, timeNode, longNode, textNode));
    }

    @Test
    public final void testLocalDateLessNot() {
        final SearchLocalDateNode node = this.localDateNode(DATE_LT);
        final SearchQuery query = this.localDateQueryValue(DATE).greaterThanEquals().not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // LocalDateTime......................................................................................

    @Test
    public final void testLocalDateTimeLess() {
        final SearchLocalDateTimeNode node = this.localDateTimeNode(DATETIME_LT);
        final SearchQuery query = this.localDateTimeQueryValue(DATETIME).greaterThanEquals();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLocalDateTimeEqual() {
        final SearchLocalDateTimeNode node = this.localDateTimeNode(DATETIME);
        final SearchQuery query = this.localDateTimeQueryValue(DATETIME).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLocalDateTimeGreater() {
        final SearchLocalDateTimeNode node = this.localDateTimeNode(DATETIME_GT);
        final SearchQuery query = this.localDateTimeQueryValue(DATETIME).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLocalDateTimeReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalDateTimeNode eq = this.localDateTimeNode(DATETIME);
        final SearchLocalDateTimeNode lt = this.localDateTimeNode(DATETIME_LT);
        final SearchLocalDateTimeNode gt = this.localDateTimeNode(DATETIME_GT);

        final SearchQuery query = this.localDateTimeQueryValue(DATETIME).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLocalDateTimeNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalDateTimeNode eq = this.localDateTimeNode(DATETIME);
        final SearchLocalDateTimeNode lt = this.localDateTimeNode(DATETIME_LT);
        final SearchLocalDateTimeNode gt = this.localDateTimeNode(DATETIME_GT);

        final SearchQuery query = this.localDateTimeQueryValue(DATETIME).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLocalDateTimeIgnoresOtherTypes() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalDateTimeNode eq = this.localDateTimeNode(DATETIME);
        final SearchLocalDateTimeNode lt = this.localDateTimeNode(DATETIME_LT);
        final SearchLocalDateTimeNode gt = this.localDateTimeNode(DATETIME_GT);

        final SearchBigDecimalNode bigDecimalNode = this.bigDecimalNode(VALUE);
        final SearchBigIntegerNode bigIntegerNode = this.bigIntegerNode(VALUE);
        final SearchDoubleNode doubleNode = this.doubleNode(VALUE);
        final SearchLocalDateNode dateNode = this.localDateNode(DATE);
        final SearchLocalTimeNode timeNode = this.localTimeNode(TIME);
        final SearchLongNode longNode = this.longNode(VALUE);
        final SearchTextNode textNode = this.textNode(TEXT);

        final SearchQuery query = this.localDateTimeQueryValue(DATETIME).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after, bigDecimalNode, bigIntegerNode, doubleNode, dateNode, timeNode, longNode, textNode),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after, bigDecimalNode, bigIntegerNode, doubleNode, dateNode, timeNode, longNode, textNode));
    }

    @Test
    public final void testLocalDateTimeLessNot() {
        final SearchLocalDateTimeNode node = this.localDateTimeNode(DATETIME_LT);
        final SearchQuery query = this.localDateTimeQueryValue(DATETIME).greaterThanEquals().not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // LocalTime......................................................................................

    @Test
    public final void testLocalTimeLess() {
        final SearchLocalTimeNode node = this.localTimeNode(TIME_LT);
        final SearchQuery query = this.localTimeQueryValue(TIME).greaterThanEquals();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLocalTimeEqual() {
        final SearchLocalTimeNode node = this.localTimeNode(TIME);
        final SearchQuery query = this.localTimeQueryValue(TIME).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLocalTimeGreater() {
        final SearchLocalTimeNode node = this.localTimeNode(TIME_GT);
        final SearchQuery query = this.localTimeQueryValue(TIME).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLocalTimeReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalTimeNode eq = this.localTimeNode(TIME);
        final SearchLocalTimeNode lt = this.localTimeNode(TIME_LT);
        final SearchLocalTimeNode gt = this.localTimeNode(TIME_GT);

        final SearchQuery query = this.localTimeQueryValue(TIME).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLocalTimeNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalTimeNode eq = this.localTimeNode(TIME);
        final SearchLocalTimeNode lt = this.localTimeNode(TIME_LT);
        final SearchLocalTimeNode gt = this.localTimeNode(TIME_GT);

        final SearchQuery query = this.localTimeQueryValue(TIME).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLocalTimeIgnoresOtherTypes() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLocalTimeNode eq = this.localTimeNode(TIME);
        final SearchLocalTimeNode lt = this.localTimeNode(TIME_LT);
        final SearchLocalTimeNode gt = this.localTimeNode(TIME_GT);

        final SearchBigDecimalNode bigDecimalNode = this.bigDecimalNode(VALUE);
        final SearchBigIntegerNode bigIntegerNode = this.bigIntegerNode(VALUE);
        final SearchDoubleNode doubleNode = this.doubleNode(VALUE);
        final SearchLocalDateNode dateNode = this.localDateNode(DATE);
        final SearchLocalDateTimeNode dateTimeNode = this.localDateTimeNode(DATETIME);
        final SearchLongNode longNode = this.longNode(VALUE);
        final SearchTextNode textNode = this.textNode(TEXT);

        final SearchQuery query = this.localTimeQueryValue(TIME).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after, bigDecimalNode, bigIntegerNode, doubleNode, dateNode, dateTimeNode, longNode, textNode),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after, bigDecimalNode, bigIntegerNode, doubleNode, dateNode, dateTimeNode, longNode, textNode));
    }

    @Test
    public final void testLocalTimeLessNot() {
        final SearchLocalTimeNode node = this.localTimeNode(TIME_LT);
        final SearchQuery query = this.localTimeQueryValue(TIME).greaterThanEquals().not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // Long......................................................................................

    @Test
    public final void testLongLess() {
        final SearchLongNode node = this.longNode(VALUE_LT);
        final SearchQuery query = this.longQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testLongEqual() {
        final SearchLongNode node = this.longNode(VALUE);
        final SearchQuery query = this.longQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLongGreater() {
        final SearchLongNode node = this.longNode(VALUE_GT);
        final SearchQuery query = this.longQueryValue(VALUE).greaterThanEquals();

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testLongReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLongNode eq = this.longNode(VALUE);
        final SearchLongNode lt = this.longNode(VALUE_LT);
        final SearchLongNode gt = this.longNode(VALUE_GT);

        final SearchQuery query = this.longQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLongNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLongNode eq = this.longNode(VALUE);
        final SearchLongNode lt = this.longNode(VALUE_LT);
        final SearchLongNode gt = this.longNode(VALUE_GT);

        final SearchQuery query = this.longQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after));
    }

    @Test
    public final void testLongIgnoresOtherTypes() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchLongNode eq = this.longNode(VALUE);
        final SearchLongNode lt = this.longNode(VALUE_LT);
        final SearchLongNode gt = this.longNode(VALUE_GT);

        final SearchBigDecimalNode bigDecimalNode = this.bigDecimalNode(VALUE);
        final SearchBigIntegerNode bigIntegerNode = this.bigIntegerNode(VALUE);
        final SearchDoubleNode doubleNode = this.doubleNode(VALUE);
        final SearchLocalDateNode dateNode = this.localDateNode(DATE);
        final SearchLocalDateTimeNode dateTimeNode = this.localDateTimeNode(DATETIME);
        final SearchLocalTimeNode timeNode = this.localTimeNode(TIME);
        final SearchTextNode textNode = this.textNode(TEXT);

        final SearchQuery query = this.longQueryValue(VALUE).greaterThanEquals();
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after, bigDecimalNode, bigIntegerNode, doubleNode, dateNode, dateTimeNode, timeNode, textNode),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after, bigDecimalNode, bigIntegerNode, doubleNode, dateNode, dateTimeNode, timeNode, textNode));
    }

    @Test
    public final void testLongLessNot() {
        final SearchLongNode node = this.longNode(VALUE_LT);
        final SearchQuery query = this.longQueryValue(VALUE).greaterThanEquals().not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // Text......................................................................................

    @Test
    public final void testTextLess() {
        final SearchTextNode node = this.textNode(TEXT_LT);
        final SearchQuery query = this.textQueryValue(TEXT).greaterThanEquals(CaseSensitivity.SENSITIVE);

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testTextEqual() {
        final SearchTextNode node = this.textNode(TEXT);
        final SearchQuery query = this.textQueryValue(TEXT).greaterThanEquals(CaseSensitivity.SENSITIVE);

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testTextGreater() {
        final SearchTextNode node = this.textNode(TEXT_GT);
        final SearchQuery query = this.textQueryValue(TEXT).greaterThanEquals(CaseSensitivity.SENSITIVE);

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testTextReplaceSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode eq = this.textNode(TEXT);
        final SearchTextNode lt = this.textNode(TEXT_LT);
        final SearchTextNode gt = this.textNode(TEXT_GT);

        final SearchQuery query = this.textQueryValue(TEXT).greaterThanEquals(CaseSensitivity.SENSITIVE);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after.selected()),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after.selected()));
    }

    @Test
    public final void testTextNewSelection() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode eq = this.textNode(TEXT);
        final SearchTextNode lt = this.textNode(TEXT_LT);
        final SearchTextNode gt = this.textNode(TEXT_GT);

        final SearchQuery query = this.textQueryValue(TEXT).greaterThanEquals(CaseSensitivity.SENSITIVE);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after.selected()));
    }

    @Test
    public final void testTextLessNot() {
        final SearchTextNode node = this.textNode(TEXT_LT);
        final SearchQuery query = this.textQueryValue(TEXT).greaterThanEquals(CaseSensitivity.SENSITIVE).not();

        this.querySelectAndCheck(query, node, node.selected());
    }

    // Text Insensitive.................................................................................................

    @Test
    public final void testTextLessInsensitive() {
        final SearchTextNode node = this.textNode(TEXT_LT);
        final SearchQuery query = this.textQueryValue(TEXT2).greaterThanEquals(CaseSensitivity.INSENSITIVE);

        this.querySelectAndCheck(query, node, node);
    }

    @Test
    public final void testTextEqualInsensitive() {
        final SearchTextNode node = this.textNode(TEXT);
        final SearchQuery query = this.textQueryValue(TEXT2).greaterThanEquals(CaseSensitivity.INSENSITIVE);

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testTextGreaterInsensitive() {
        final SearchTextNode node = this.textNode(TEXT_GT);
        final SearchQuery query = this.textQueryValue(TEXT2).greaterThanEquals(CaseSensitivity.INSENSITIVE);

        this.querySelectAndCheck(query, node, node.selected());
    }

    @Test
    public final void testTextReplaceSelectionInsensitive() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode eq = this.textNode(TEXT);
        final SearchTextNode lt = this.textNode(TEXT_LT);
        final SearchTextNode gt = this.textNode(TEXT_GT);

        final SearchQuery query = this.textQueryValue(TEXT2).greaterThanEquals(CaseSensitivity.INSENSITIVE);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after.selected()),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after.selected()));
    }

    @Test
    public final void testTextNewSelectionInsensitive() {
        final SearchNode before = this.before();
        final SearchNode after = this.after();

        final SearchTextNode eq = this.textNode(TEXT2);
        final SearchTextNode lt = this.textNode(TEXT2_LT);
        final SearchTextNode gt = this.textNode(TEXT2_GT);

        final SearchQuery query = this.textQueryValue(TEXT2).greaterThanEquals(CaseSensitivity.INSENSITIVE);
        this.querySelectAndCheck(query,
                this.sequenceNode(before, lt, eq, gt, after),
                this.sequenceNode(before, lt, eq.selected(), gt.selected(), after.selected()));
    }

    @Override
    SearchGreaterThanEqualsQuery createSearchQuery(final SearchTextQueryValue value, final SearchQueryTester tester) {
        return SearchGreaterThanEqualsQuery.with(value, tester);
    }

    @Override
    SearchQueryValueSearchQueryTesterComparisonPredicate predicate() {
        return SearchQueryValueSearchQueryTesterComparisonPredicate.GREATER_THAN_EQUALS;
    }

    @Override
    public Class<SearchGreaterThanEqualsQuery> type() {
        return SearchGreaterThanEqualsQuery.class;
    }
}
