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
 */

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for all {@link Predicate} in this package.
 */
abstract public class PredicateTestCase<P extends Predicate<T>, T>
        extends ClassTestCase<P>
        implements PredicateTesting<P, T> {

    PredicateTestCase() {
        super();
    }

    @Test
    public void testTestNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.test(null);
        });
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
