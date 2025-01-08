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

package walkingkooka.reflect;

import org.junit.jupiter.api.Test;
import walkingkooka.predicate.Predicates;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BeanPropertiesTestingTest implements BeanPropertiesTesting {

    @Test
    public void testAllPropertiesNeverReturnNullCheck() throws Exception {
        this.allPropertiesNeverReturnNullCheck(new TestAllPropertiesNeverReturnNullCheck(),
            Predicates.never());
    }

    static class TestAllPropertiesNeverReturnNullCheck {

        public Object property() {
            return this;
        }
    }

    @Test
    public void testAllPropertiesNeverReturnNullCheckFilter() throws Exception {
        this.allPropertiesNeverReturnNullCheck(new TestAllPropertiesNeverReturnNullCheckFilter(),
            (m) -> m.getName().equals("propertyReturnsNull"));
    }

    static class TestAllPropertiesNeverReturnNullCheckFilter {

        public Object property() {
            return this;
        }

        public Object propertyReturnsNull() {
            return null;
        }
    }

    @Test
    public void testAllPropertiesNeverReturnNullCheckFails() {
        assertThrows(AssertionError.class,
            () -> this.allPropertiesNeverReturnNullCheck(new TestAllPropertiesNeverReturnNullCheckFails(),
                Predicates.never()));
    }

    static class TestAllPropertiesNeverReturnNullCheckFails {

        public Object property() {
            return null;
        }
    }
}
