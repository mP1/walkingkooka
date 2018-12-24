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
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class RoutingTest extends ClassTestCase<Routing<StringName, String>> {

    private final static Class<StringName> TYPE = StringName.class;
    private final static String TARGET = "Target";

    @Test(expected = NullPointerException.class)
    public void testWithNullNameFails() {
        Routing.with(null, TARGET);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullTargetFails() {
        Routing.with(TYPE, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAndValueEqualsNullNameFails() {
        this.createRoute().andValueEquals(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testANdValueEqualsNullValueFails() {
        this.createRoute().andValueEquals(Names.string("name"), null);
    }

    @Test(expected = NullPointerException.class)
    public void testAndPredicateTrueNullNameFails() {
        this.createRoute().andPredicateTrue(null, Predicates.fake());
    }

    @Test(expected = NullPointerException.class)
    public void testAndPredicateTrueNullPredicateFails() {
        this.createRoute().andValueEquals(Names.string("name"), null);
    }

    @Test
    public void testToString() {
        assertEquals(TARGET, this.createRoute().toString());
    }

    private Routing<StringName, String> createRoute() {
        return Routing.with(TYPE, TARGET);
    }

    @Override
    protected Class<Routing<StringName, String>> type() {
        return Cast.to(Routing.class);
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
