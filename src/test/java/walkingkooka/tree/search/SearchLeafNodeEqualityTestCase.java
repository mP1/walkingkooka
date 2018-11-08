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

public abstract class SearchLeafNodeEqualityTestCase<N extends SearchLeafNode, V> extends SearchNodeEqualityTestCase<N> {

    @Test
    public final void testDifferentValue() {
        this.checkNotEquals(this.createSearchNode(this.differentValue()));
    }

    @Test
    public final void testDifferentValueBoth() {
        final V differentValue = this.differentValue();
        this.checkEquals(this.createSearchNode(differentValue), this.createSearchNode(differentValue));
    }

    public final N createObject() {
        return this.createSearchNode(this.value());
    }

    abstract N createSearchNode(V value);

    abstract V value();

    abstract V differentValue();
}
