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

package walkingkooka.collect.map;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.Map;

public abstract class ImmutableMapTestCase2<M extends ImmutableMap<String, Integer>> extends ImmutableMapTestCase<M>
    implements MapTesting<M, String, Integer>,
    IteratorTesting,
    ToStringTesting<M>,
    TypeNameTesting<M> {

    ImmutableMapTestCase2() {
        super();
    }

    @Test
    public final void testContainsKeyAbsent() {
        this.containsKeyAndCheckAbsent("Absent!");
    }

    @Test
    public final void testContainsValueAbsent() {
        this.containsValueAndCheckAbsent(this.createMap(), Integer.MAX_VALUE);
    }

    @Test
    public final void testGetUnknown() {
        this.getAndCheckAbsent("unknown!");
    }

    @Test
    public final void testIsEmptyNot() {
        this.isEmptyAndCheck(this.createMap(), false);
    }

    @Test
    public final void testPutFails() {
        this.putFails(this.createMap(), "new-key!", Integer.MAX_VALUE);
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Immutable" + Map.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
