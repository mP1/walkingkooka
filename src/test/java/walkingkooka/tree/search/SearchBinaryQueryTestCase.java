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
import walkingkooka.text.CaseSensitivity;

public abstract class SearchBinaryQueryTestCase<Q extends SearchBinaryQuery> extends SearchQueryTestCase<Q> {

    SearchBinaryQueryTestCase() {
        super();
    }

    @Test
    public void testEqualsDifferentLeftQuery() {
        this.checkNotEquals(this.createSearchQuery(this.differentQuery(), this.right()));
    }

    @Test
    public void testEqualsDifferentRightQuery() {
        this.checkNotEquals(this.createSearchQuery(this.left(), this.differentQuery()));
    }

    final Q createSearchQuery() {
        return this.createSearchQuery(this.left(), this.right());
    }

    abstract Q createSearchQuery(final SearchQuery left, final SearchQuery right);

    final SearchQuery left() {
        return this.textQueryValue("left").equalsQuery(CaseSensitivity.SENSITIVE);
    }

    final SearchQuery right() {
        return this.textQueryValue("right").equalsQuery(CaseSensitivity.SENSITIVE);
    }

    final SearchQuery differentQuery() {
        return this.textQueryValue("different-text").equalsQuery(CaseSensitivity.SENSITIVE);
    }
}
