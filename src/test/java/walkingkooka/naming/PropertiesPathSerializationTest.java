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

package walkingkooka.naming;

import org.junit.Test;
import walkingkooka.test.SerializationTestCase;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

final public class PropertiesPathSerializationTest extends SerializationTestCase<PropertiesPath> {

    @Test
    public void testGeneral() throws Exception {
        final PropertiesPath path = this.cloneUsingSerialization(PropertiesPath.parse("one.two.three"));
        final PropertiesPath parent = path.parent().get();

        assertEquals("one.two", parent.value());

        assertFalse(parent.isRoot());
        assertSame(parent, path.parent());
        assertEquals(PropertiesName.with("two"), parent.name());

        final PropertiesPath grandParent = parent.parent().get();
        assertEquals("one", grandParent.value());
        assertTrue(grandParent.isRoot());
        assertEquals(PropertiesName.with("one"), grandParent.name());
    }

    @Override
    protected Class<PropertiesPath> type() {
        return PropertiesPath.class;
    }

    @Override
    protected PropertiesPath create() {
        return PropertiesPath.parse("abc.def");
    }

    @Override
    protected boolean isSingleton() {
        return false;
    }
}
