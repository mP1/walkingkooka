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
import walkingkooka.Cast;
import walkingkooka.naming.Names;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;

public final class RouteTest extends PublicClassTestCase<Route<String>> {

    private final static String TARGET = "Target";

    @Test(expected = NullPointerException.class)
    public void testWithNullTargetFails() {
        Route.with(null);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsValueNullNameFails() {
        this.createRoute().equalsValue(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsValueNullValueFails() {
        this.createRoute().equalsValue(Names.string("name"), null);
    }

    @Test(expected = NullPointerException.class)
    public void testPredicateNullNameFails() {
        this.createRoute().predicate(null, Predicates.fake());
    }

    @Test(expected = NullPointerException.class)
    public void testPredicateNullPredicateFails() {
        this.createRoute().equalsValue(Names.string("name"), null);
    }

    @Test
    public void testToString() {
        assertEquals(TARGET, this.createRoute().toString());
    }
    
    private Route<String> createRoute() {
        return Route.with(TARGET);
    }

    @Override
    protected Class<Route<String>> type() {
        return Cast.to(Route.class);
    }
}
