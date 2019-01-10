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

package walkingkooka.net.http.server;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.PredicateTestCase;
import walkingkooka.predicate.Predicates;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;

public final class HttpRequestAttributeRoutingBuilderParameterValuePredicateTest extends
        PredicateTestCase<HttpRequestAttributeRoutingBuilderParameterValuePredicate, List<String>> {

    private final static String VALUE = "value123";

    @Ignore
    @Test
    public void testTestNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testValueMissing() {
        this.testFalse(null);
    }

    @Test
    public void testOnlyMatched() {
        this.testTrue(Lists.of(VALUE));
    }

    @Test
    public void testIncluded() {
        this.testTrue(Lists.of("different", VALUE));
    }

    @Test
    public void testNone() {
        this.testFalse(Lists.of("different", "different-2"));
    }

    @Test
    public void testToString() {
        assertEquals(this.wrappedPredicate().toString(), this.createPredicate().toString());
    }

    @Override
    protected HttpRequestAttributeRoutingBuilderParameterValuePredicate createPredicate() {
        return HttpRequestAttributeRoutingBuilderParameterValuePredicate.with(this.wrappedPredicate());
    }

    private Predicate<String> wrappedPredicate() {
        return Predicates.is(VALUE);
    }

    @Override
    protected Class<HttpRequestAttributeRoutingBuilderParameterValuePredicate> type() {
        return HttpRequestAttributeRoutingBuilderParameterValuePredicate.class;
    }
}
