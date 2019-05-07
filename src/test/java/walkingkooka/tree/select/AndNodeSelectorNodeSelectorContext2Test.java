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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;
import walkingkooka.tree.TestNode;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class AndNodeSelectorNodeSelectorContext2Test extends NodeSelectorContext2TestCase<AndNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object>,
        TestNode, StringName, StringName, Object> {

    @Test
    public void testAll() {
        final AndNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> context = this.createContext();
        assertSame(context, context.all());
    }

    @Test
    public void testToString() {
        final String toString = "Context123";
        this.toStringAndCheck(AndNodeSelectorNodeSelectorContext2.with(this.contextWithToString(toString)), toString + " []");
    }

    @Override
    public AndNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object> createContext() {
        return AndNodeSelectorNodeSelectorContext2.with(null);
    }

    @Override
    public Class<AndNodeSelectorNodeSelectorContext2<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(AndNodeSelectorNodeSelectorContext2.class);
    }
}
