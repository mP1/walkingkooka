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

package walkingkooka.tree.pojo;

import walkingkooka.collect.list.Lists;

import java.util.List;

public final class PojoObjectNodeChildrenValueListTest extends PojoNodeListTestCase<PojoObjectNodeChildrenValueList, Object> {

    @Override
    protected Class<PojoObjectNodeChildrenValueList> type() {
        return PojoObjectNodeChildrenValueList.class;
    }

    @Override
    List<Object> listOfComponents() {
        return Lists.of("abc123", "def234");
    }

    @Override
    List<Object> listOfDifferentComponents() {
        return Lists.of("different123", "different234");
    }

    @Override
    List<Object> list(final List<Object> components) {
        return PojoNode.wrap(PojoName.property("root"),
                new TestBean((String)components.get(0), (String)components.get(1)),
                new ReflectionPojoNodeContext())
        .childrenValues();
    }

    static class TestBean {

        TestBean(final String x, final String y){
            this.x = x;
            this.y = y;
        }

        private String x;
        public String getX(){
            return this.x;
        }
        private String y;
        public String getY() {
            return this.y;
        }
    }
}
