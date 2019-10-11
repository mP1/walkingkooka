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

package walkingkooka.text.cursor.parser;

import walkingkooka.Value;
import walkingkooka.naming.Name;
import walkingkooka.tree.search.SearchNode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Interface that all leaf parser tokens must implement. A leaf must not have further children or a List value.
 */
public interface LeafParserToken<T> extends ParserToken, Value<T> {

    /**
     * Examines the value of this {@link ParserToken} and creates one of the specialised
     * {@link SearchNode} or defaults to {@link SearchNode#text(String, String)}
     */
    default SearchNode toSearchNode() {
        SearchNode node;

        for (; ; ) {
            final String text = this.text();
            final Object value = this.value();
            if (value instanceof BigDecimal) {
                node = SearchNode.bigDecimal(text, (BigDecimal) value);
                break;
            }
            if (value instanceof BigInteger) {
                node = SearchNode.bigInteger(text, (BigInteger) value);
                break;
            }
            if (value instanceof Double) {
                node = SearchNode.doubleNode(text, (Double) value);
                break;
            }
            if (value instanceof LocalDate) {
                node = SearchNode.localDate(text, (LocalDate) value);
                break;
            }
            if (value instanceof LocalDateTime) {
                node = SearchNode.localDateTime(text, (LocalDateTime) value);
                break;
            }
            if (value instanceof LocalTime) {
                node = SearchNode.localTime(text, (LocalTime) value);
                break;
            }
            if (value instanceof Long) {
                node = SearchNode.longNode(text, (Long) value);
                break;
            }
            if (value instanceof Name) {
                node = SearchNode.text(text, ((Name) value).value());
                break;
            }
            if (value instanceof String) {
                node = SearchNode.text(text, (String) value);
                break;
            }

            // default is simply use the text and ignore the value.
            node = SearchNode.text(text, text);
            break;
        }
        return node;
    }
}
