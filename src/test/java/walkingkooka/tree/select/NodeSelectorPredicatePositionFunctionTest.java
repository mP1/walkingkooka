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

import static org.junit.Assert.assertEquals;

public final class NodeSelectorPredicatePositionFunctionTest extends NodeSelectorPredicateFunctionTestCase<NodeSelectorPredicatePositionFunction, Long> {

    @Test
    public void testInteger() {
        final int index = 123;
        this.applyAndCheck2(
                this.createBiFunction(),
                parameters(new TestFakeNode("node") {
                    @Override
                    public int index() {
                        return index;
                    }
                }),
                Long.valueOf(index + NodeSelector.INDEX_BIAS));
    }

    @Test
    public void testToString() {
        assertEquals("position", this.createBiFunction().toString());
    }

    @Override
    protected NodeSelectorPredicatePositionFunction createBiFunction() {
        return NodeSelectorPredicatePositionFunction.INSTANCE;
    }

    @Override
    protected Class<NodeSelectorPredicatePositionFunction> type() {
        return NodeSelectorPredicatePositionFunction.class;
    }
}
