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

import walkingkooka.reflect.TypeNameTesting;

import java.util.Map.Entry;
import java.util.Set;

public abstract class ImmutableMapEntrySetTestCase<S extends ImmutableMapEntrySet<Entry<String, Integer>>> extends ImmutableMapTestCase<S>
    implements TypeNameTesting<S> {

    ImmutableMapEntrySetTestCase() {
        super();
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Entry.class.getSimpleName() + Set.class.getSimpleName();
    }
}
