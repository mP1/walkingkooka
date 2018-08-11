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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

final public class StringPathSerializationTest extends SerializationTestCase<StringPath> {

    @Test
    public void testRootIsSingleton() throws Exception {
        this.serializeSingletonAndCheck(StringPath.ROOT);
    }

    @Test
    public void testParentNameAndIsRoot() throws Exception {
        final StringPath path = this.cloneUsingSerialization(StringPath.parse("/one/two/three"));
        final StringPath parent = path.parent().get();

        assertEquals("/one/two", parent.value());

        assertFalse(parent.isRoot());
        assertSame(parent, path.parent().get());
        assertEquals(StringName.with("two"), parent.name());

        final StringPath grandParent = parent.parent().get();
        assertEquals("/one", grandParent.value());
        assertFalse(grandParent.isRoot());
        assertEquals(StringName.with("one"), grandParent.name());

        assertSame(StringPath.ROOT, grandParent.parent().get());
    }

    @Override
    protected Class<StringPath> type() {
        return StringPath.class;
    }

    @Override
    protected StringPath create() {
        return StringPath.parse("/path");
    }

    @Override
    protected boolean isSingleton() {
        return false;
    }
}
