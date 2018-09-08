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

public abstract class SearchParentNodeEqualityTestCase<N extends SearchParentNode> extends SearchNodeEqualityTestCase<N> {

    @Test
    public final void testDifferentChild() {
        this.checkNotEquals(this.createSearchNode(this.text("different-child")));
    }

    public final N createObject() {
        return this.createSearchNode(this.child());
    }

    abstract N createSearchNode(SearchNode child);

    final SearchNode child() {
        return this.text("child");
    }

    final SearchNode text(final String text) {
        return SearchNode.text(text, text);
    }
}
