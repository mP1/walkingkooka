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

package walkingkooka.tree.json.marshall;

import walkingkooka.test.Fake;
import walkingkooka.tree.json.JsonStringNode;

import java.util.Objects;
import java.util.Optional;

/**
 * A fake {@link JsonNodeContext }
 */
public class FakeJsonNodeContext implements JsonNodeContext, Fake {
    @Override
    public Optional<Class<?>> registeredType(final JsonStringNode name) {
        Objects.requireNonNull(name, "name");
        return Optional.empty();
    }

    @Override
    public Optional<JsonStringNode> typeName(final Class<?> type) {
        Objects.requireNonNull(type, "type");
        return Optional.empty();
    }
}
