/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.tree.pojo;

import java.util.List;
import java.util.Objects;

public final class PojoObjectNodeChildrenListTest extends PojoNodeListTestCase<PojoObjectNodeChildrenList, PojoNode> {

    @Override
    public Class<PojoObjectNodeChildrenList> type() {
        return PojoObjectNodeChildrenList.class;
    }

    @Override
    List<PojoNode> components() {
        return list0("abc123", "def234");
    }

    @Override
    List<PojoNode> differentComponents() {
        return list0("different123", "different234");
    }

    @Override
    List<PojoNode> createList(final List<PojoNode> components) {
        return list0((String) components.get(0).value(),
                (String) components.get(1).value());
    }

    private List<PojoNode> list0(final String component0, final String component1) {
        return PojoNode.wrap(PojoName.property("root"),
                new PojoObjectNodeChildrenListTest.TestBean(component0, component1),
                new ReflectionPojoNodeContext())
                .children();
    }

    static class TestBean {

        TestBean(final String x, final String y) {
            this.x = x;
            this.y = y;
        }

        private final String x;

        public String getX() {
            return this.x;
        }

        private final String y;

        public String getY() {
            return this.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof TestBean && equals0((TestBean) other);
        }

        private boolean equals0(final TestBean other) {
            return this.x.equals(other.x) && this.y.equals(other.y);
        }
    }
}

