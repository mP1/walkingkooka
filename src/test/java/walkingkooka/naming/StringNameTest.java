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
import walkingkooka.test.SerializationTesting;

final public class StringNameTest extends NameTestCase<StringName> implements SerializationTesting<StringName> {

    @Test(expected = IllegalArgumentException.class)
    public void testContainsSeparatorFails() {
        StringName.with("name-" + StringPath.SEPARATOR.string());
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("abc-123");
    }

    @Test
    public void testSerializeRootIsSingleton() throws Exception {
        this.serializeSingletonAndCheck(StringName.ROOT);
    }

    @Override
    protected StringName createName(final String name) {
        return StringName.with(name);
    }

    @Override
    public Class<StringName> type() {
        return StringName.class;
    }

    @Override
    public StringName serializableInstance() {
        return StringName.with("string");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
