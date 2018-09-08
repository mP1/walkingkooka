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

public final class SearchSequenceNodeEqualityTest extends SearchParentNodeEqualityTestCase<SearchSequenceNode> {

    @Test
    public void testDifferentExtraChild() {
        this.checkNotEquals(SearchSequenceNode.with(Lists.of(this.child(), this.text("extra"))));
    }

    @Override
    SearchSequenceNode createSearchNode(final SearchNode child) {
        return SearchSequenceNode.with(Lists.of(child));
    }
}
