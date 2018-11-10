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

package walkingkooka.tree.pojo;

import org.junit.Test;
import walkingkooka.Cast;

public final class PojoObjectNodeEqualityTest extends PojoNode2EqualityTestCase<PojoObjectNode> {

    @Test
    public void testDifferentValue() {
        this.createPojoNode(new TestValue("different456"));
    }

    @Override
    protected PojoNode createObject() {
        return this.createPojoNode(new TestValue("value123"));
    }

    private PojoObjectNode createPojoNode(final Object value) {
        return Cast.to(PojoNode.wrap(PojoName.property("root"),
                value,
                new ReflectionPojoNodeContext()));
    }

    static class TestValue {

        TestValue(final String value) {
            super();
            this.value = value;
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public boolean equals(final Object other) {
            return this.value.equals(TestValue.class.cast(other).value);
        }

        private final String value;
    }
}
