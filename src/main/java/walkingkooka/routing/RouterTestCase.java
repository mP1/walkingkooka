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

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public abstract class RouterTestCase<R extends Router<K, T>, K, T> extends ClassTestCase<R> {

    @Test(expected = NullPointerException.class)
    public void testNullParametersFails() {
        this.createRouter().route(null);
    }

    protected abstract R createRouter();

    protected void routeAndCheck(final Router<K, T> routers, final Map<K, Object> parameters, final T target) {
        assertEquals("Routing of parameters=" + parameters + " failed",
                Optional.of(target),
                routers.route(parameters));
    }

    protected void routeFails(final Router<K, T> routers, final Map<K, Object> parameters) {
        assertEquals("Routing of parameters=" + parameters + " should have failed",
                Optional.empty(),
                routers.route(parameters));
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}

