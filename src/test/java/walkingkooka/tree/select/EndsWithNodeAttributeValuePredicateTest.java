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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.naming.StringName;

import java.util.Collections;

public class EndsWithNodeAttributeValuePredicateTest
        extends NodeAttributeValuePredicateTestCase<EndsWithNodeAttributeValuePredicate<TestFakeNode, StringName, StringName, Object>> {

    @Test
    public void tesEndsWith() {
        this.testTrue(new TestFakeNode("node").setAttributes(Collections.singletonMap(ATTRIBUTE_NAME1, "!!!" + VALUE)));
    }

    @Test
    public void testWrongEndsWith() {
        this.testFalse(new TestFakeNode("node").setAttributes(Collections.singletonMap(ATTRIBUTE_NAME1, VALUE + "!!!")));
    }

    @Test
    public void testToString() {
        assertEquals("[ends-with(@\"attribute-1\",\"123\")]", this.createPredicate().toString());
    }

    @Override
    EndsWithNodeAttributeValuePredicate<TestFakeNode, StringName, StringName, Object> createPredicate(final StringName name, final Object value) {
        return new EndsWithNodeAttributeValuePredicate(name, value);
    }

    @Override
    protected Class<EndsWithNodeAttributeValuePredicate<TestFakeNode, StringName, StringName, Object>> type() {
        return Cast.to(EndsWithNodeAttributeValuePredicate.class);
    }
}
