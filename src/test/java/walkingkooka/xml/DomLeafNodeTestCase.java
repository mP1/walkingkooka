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

package walkingkooka.xml;

import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;

public abstract class DomLeafNodeTestCase<N extends DomLeafNode> extends DomNodeTestCase<N> {

    @Test(expected = UnsupportedOperationException.class)
    public final void testSetChildrenFails() {
        this.createNode().setChildren(DomNode.NO_CHILDREN);
    }

    @Test(expected = UnsupportedOperationException.class)
    public final void testRemoveChildFails() {
        this.createNode().removeChild(0);
    }

    @Override
    final N createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }
}
