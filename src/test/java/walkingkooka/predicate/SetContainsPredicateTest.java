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

package walkingkooka.predicate;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public final class SetContainsPredicateTest extends PredicateTestCase<SetContainsPredicate<String>, String> {

    @Test(expected = NullPointerException.class)
    public void testWithNullSetFails() {
        SetContainsPredicate.with(null);
    }

    @Test
    public void testWithSetCopied() {
        final Set<String> set = Sets.ordered();
        set.addAll(this.set());

        final SetContainsPredicate<String> predicate = SetContainsPredicate.with(set);
        set.clear();

        this.testTrue(predicate, "A");
    }

    @Test
    public void testContains() {
        this.testTrue("A");
    }

    @Test
    public void testContains2() {
        this.testTrue("Baboon");
    }

    @Test
    public void testDoesntContains() {
        this.testFalse("Zebra");
    }

    @Test
    public void testDoesntContainsDifferentCase() {
        this.testFalse("a");
    }

    @Test
    public void testToString() {
        assertEquals(this.set().toString(), this.createPredicate().toString());
    }

    @Override
    protected SetContainsPredicate<String> createPredicate() {
        return SetContainsPredicate.with(this.set());
    }

    private Set<String> set() {
        return Sets.of("A", "Baboon", "Carrot");
    }

    @Override
    protected Class<SetContainsPredicate<String>> type() {
        return Cast.to(SetContainsPredicate.class);
    }
}
