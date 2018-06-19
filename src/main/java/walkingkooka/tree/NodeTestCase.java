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
 */

package walkingkooka.tree;

import org.junit.Test;
import walkingkooka.naming.Name;
import walkingkooka.test.PackagePrivateClassTestCase;

abstract public class NodeTestCase<N extends Node<N, NAME, ANAME, AVALUE>,
        NAME extends Name,
        ANAME extends Name,
        AVALUE extends Object>
        extends
        PackagePrivateClassTestCase<N> {

    protected NodeTestCase() {
        super();
    }

    @Test final public void testNameCached() {
        final N node = this.createNode();
        this.checkCached(node, "name", node.name(), node.name());
    }

    @Test final public void testParentCached() {
        final N node = this.createNode();
        this.checkCached(node, "parent", node.parent(), node.parent());
    }

    private <T> void checkCached(final N node, final String property, final T value, final T value2) {
        if (value != value2) {
            failNotSame(node + " did not cache " + property, value, value2);
        }
    }

    abstract protected N createNode();
}
