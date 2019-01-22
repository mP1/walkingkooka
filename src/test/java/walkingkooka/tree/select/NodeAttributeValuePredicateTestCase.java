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

package walkingkooka.tree.select;

import org.junit.Before;
import org.junit.Test;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.PredicateTestCase;
import walkingkooka.tree.TestNode;

import java.util.Collections;

public abstract class NodeAttributeValuePredicateTestCase<N extends NodeAttributeValuePredicate<TestNode, StringName, StringName, Object>>
        extends PredicateTestCase<N, TestNode> {

    @Before
    public void beforeEachTest() {
        TestNode.clear();
    }

    final static StringName ATTRIBUTE_NAME1 = Names.string("attribute-1");
    final static StringName ATTRIBUTE_NAME2 = Names.string("attribute-2");
    final static String VALUE = "123";

    NodeAttributeValuePredicateTestCase() {
        super();
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        this.createPredicate(ATTRIBUTE_NAME1, null);
    }

    @Test
    public final void testAttributeMissing() {
        this.testFalse(TestNode.with("nodeName").setAttributes(Collections.singletonMap(ATTRIBUTE_NAME2, "value")));
    }

    @Test
    public final void testAttributeDifferentValue() {
        this.testFalse(TestNode.with("nodeName").setAttributes(Collections.singletonMap(ATTRIBUTE_NAME1, "*DIFFERENT*")));
    }

    protected N createPredicate() {
        return this.createPredicate(ATTRIBUTE_NAME1);
    }

    final N createPredicate(final StringName name) {
        return this.createPredicate(name, VALUE);
    }

    abstract N createPredicate(final StringName name, final Object value);


}
