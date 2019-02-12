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
 *
 */

package walkingkooka.routing;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface RouterTesting<R extends Router<K, T>, K, T> extends ToStringTesting<R>,
        TypeNameTesting<R> {

    @Test
    default void testRouteNullParametersFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createRouter().route(null);
        });
    }

    R createRouter();

    default void routeAndCheck(final Router<K, T> routers, final Map<K, Object> parameters, final T target) {
        assertEquals(Optional.of(target),
                routers.route(parameters),
                () -> "Routing of parameters=" + parameters + " failed");
    }

    default void routeFails(final Router<K, T> routers, final Map<K, Object> parameters) {
        assertEquals(Optional.empty(),
                routers.route(parameters),
                () -> "Routing of parameters=" + parameters + " should have failed");
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Router.class.getSimpleName();
    }
}

