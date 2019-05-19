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
import walkingkooka.Cast;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RoutingTest implements ClassTesting2<Routing<StringName, String>>,
        ToStringTesting<Routing<StringName, String>> {

    private final static Class<StringName> TYPE = StringName.class;
    private final static String TARGET = "Target";

    @Test
    public void testWithNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            Routing.with(null, TARGET);
        });
    }

    @Test
    public void testWithNullTargetFails() {
        assertThrows(NullPointerException.class, () -> {
            Routing.with(TYPE, null);
        });
    }

    @Test
    public void testAndValueEqualsNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createRoute().andValueEquals(null, "value");
        });
    }

    @Test
    public void testANdValueEqualsNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createRoute().andValueEquals(Names.string("name"), null);
        });
    }

    @Test
    public void testAndPredicateTrueNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createRoute().andPredicateTrue(null, Predicates.fake());
        });
    }

    @Test
    public void testAndPredicateTrueNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createRoute().andValueEquals(Names.string("name"), null);
        });
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createRoute(), TARGET);
    }

    private Routing<StringName, String> createRoute() {
        return Routing.with(TYPE, TARGET);
    }

    @Override
    public Class<Routing<StringName, String>> type() {
        return Cast.to(Routing.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
