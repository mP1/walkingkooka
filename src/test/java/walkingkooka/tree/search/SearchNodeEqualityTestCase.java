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
import walkingkooka.tree.NodeEqualityTestCase;

public abstract class SearchNodeEqualityTestCase<N extends SearchNode> extends NodeEqualityTestCase<
        SearchNode,
        SearchNodeName,
        SearchNodeAttributeName,
        String> {

    @Test
    public final void testDifferentName() {
        this.checkNotEquals(this.createObject().setName(SearchNodeName.with("different")));
    }

    @Test
    public final void testDifferentNameBothNotDefault() {
        final SearchNodeName name = SearchNodeName.with("different");
        this.checkEquals(this.createObject().setName(name), this.createObject().setName(name));
    }
}
