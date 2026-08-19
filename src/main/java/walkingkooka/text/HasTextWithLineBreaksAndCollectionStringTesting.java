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
import walkingkooka.collect.list.Lists;

import java.util.Collection;

public interface HasTextWithLineBreaksAndCollectionStringTesting extends HasTextWithLineBreaksTesting {

    @Test
    default void testTextWithLineBreaksWithCr() {
        this.textWithLineBreaksAndCheck(
            this.createCollection(
                "111",
                "222"
            ),
            LineEnding.CR,
            "111\r" +
                "222\r"
        );
    }

    @Test
    default void testTextWithLineBreaksWithNl() {
        this.textWithLineBreaksAndCheck(
            this.createCollection(
                "111",
                "222"
            ),
            LineEnding.NL,
            "111\n" +
                "222\n"
        );
    }

    @Test
    default void testTextWithLineBreaksWithNlAndStringIncludeCr() {
        this.textWithLineBreaksAndCheck(
            this.createCollection(
                "111\r",
                "222\r"
            ),
            LineEnding.NL,
            "111\\r\n" +
                "222\\r\n"
        );
    }

    @Test
    default void testTextWithLineBreaksWithNlAndStringIncludeNl() {
        this.textWithLineBreaksAndCheck(
            this.createCollection(
                "111\n",
                "222\n"
            ),
            LineEnding.NL,
            "111\\n\n" +
                "222\\n\n"
        );
    }

    @Test
    default void testTextWithLineBreaksWithCommasAndDoubleQuotes() {
        // strings also sorted so SortedSets expectations will also pass

        this.textWithLineBreaksAndCheck(
            this.createCollection(
                "comma,",
                "double-quote\"",
                "last"
            ),
            LineEnding.NL,
            "comma,\n" +
                "double-quote\"\n" +
                "last\n"
        );
    }

    default HasTextWithLineBreaksAndCollectionString createCollection(final String... strings) {
        return this.createCollection(
            Lists.of(strings)
        );
    }

    HasTextWithLineBreaksAndCollectionString createCollection(final Collection<String> strings);
}
