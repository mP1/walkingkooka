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

package walkingkooka.tree.select;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

public final class CustomToStringNodeSelectorEqualityTest extends NodeSelectorEqualityTestCase<CustomToStringNodeSelector<TestFakeNode, StringName, StringName, Object>>{

    @Test
    public void testDifferentWrappedNodeSelector() {
        this.checkNotEquals(CustomToStringNodeSelector.with(NodeSelector.<TestFakeNode, StringName, StringName, Object>self(), this.customToString()));
    }

    @Test
    public void testDifferentCustomToString() {
        this.checkNotEquals(CustomToStringNodeSelector.with(this.wrapped(), "different"));
    }

    @Override
    CustomToStringNodeSelector<TestFakeNode, StringName, StringName, Object> createNodeSelector() {
        return Cast.to(CustomToStringNodeSelector.with(this.wrapped(), this.customToString()));
    }

    private NodeSelector<TestFakeNode, StringName, StringName, Object> wrapped() {
        return NodeSelector.terminal();
    }

    private String customToString() {
        return "custom to string";
    }
}
