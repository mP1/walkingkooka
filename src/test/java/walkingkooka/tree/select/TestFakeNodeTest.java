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

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import walkingkooka.naming.StringName;
import walkingkooka.tree.NodeTestCase2;

public class TestFakeNodeTest extends NodeTestCase2<TestFakeNode, StringName, StringName, Object> {

    @Test
    @Ignore
    public void testReplaceChildDifferentParent() {
        throw new UnsupportedOperationException();
    }

    @Rule
    public TestName name = new TestName();

    @Override
    protected TestFakeNode createNode() {
        return new TestFakeNode(this.name.getMethodName());
    }

    @Override
    protected String requiredNamePrefix() {
        return "Test";
    }

    @Override
    protected Class<TestFakeNode> type() {
        return TestFakeNode.class;
    }
}
