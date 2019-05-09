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

import walkingkooka.naming.StringName;
import walkingkooka.test.Fake;
import walkingkooka.tree.TestNode;

class FakeNodeSelector extends NodeSelector<TestNode, StringName, StringName, Object> implements Fake {

    @Override
    NodeSelector<TestNode, StringName, StringName, Object> append0(final NodeSelector<TestNode, StringName, StringName, Object> selector) {
        throw new UnsupportedOperationException();
    }

    @Override
    NodeSelectorContext2<TestNode, StringName, StringName, Object> beginPrepareContext(final NodeSelectorContext2<TestNode, StringName, StringName, Object> context) {
        throw new UnsupportedOperationException();
    }

    @Override
    NodeSelectorContext2<TestNode, StringName, StringName, Object> finishPrepareContext(final NodeSelectorContext2<TestNode, StringName, StringName, Object> context) {
        throw new UnsupportedOperationException();
    }

    @Override
    TestNode apply1(final TestNode node, final NodeSelectorContext2<TestNode, StringName, StringName, Object> context) {
        throw new UnsupportedOperationException();
    }

    @Override
    TestNode select(TestNode node, NodeSelectorContext2<TestNode, StringName, StringName, Object> context) {
        throw new UnsupportedOperationException();
    }

    @Override
    void toString0(final NodeSelectorToStringBuilder b) {
        b.append(this.getClass().getSimpleName());
    }

    @Override
    NodeSelector<TestNode, StringName, StringName, Object> unwrapIfCustomToStringNodeSelector() {
        throw new UnsupportedOperationException();
    }
}
