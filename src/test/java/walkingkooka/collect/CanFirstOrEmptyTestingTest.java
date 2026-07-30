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

package walkingkooka.collect;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

public final class CanFirstOrEmptyTestingTest implements CanFirstOrEmptyTesting,
    ClassTesting2<CanFirstOrEmpty<String>> {

    @Test
    public void testFirstOrEmptyMissing() {
        this.firstOrEmptyAndCheck(
            new TestCanFirstOrEmpty(
                Optional.empty()
            )
        );
    }

    @Test
    public void testFirstOrEmptyPresent() {
        final String first = "First123";

        this.firstOrEmptyAndCheck(
            new TestCanFirstOrEmpty(
                Optional.of(first)
            ),
            first
        );
    }

    // class............................................................................................................

    @Override
    public Class<CanFirstOrEmpty<String>> type() {
        return Cast.to(CanFirstOrEmpty.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    final static class TestCanFirstOrEmpty implements CanFirstOrEmpty<String> {

        TestCanFirstOrEmpty(final Optional<String> first) {
            this.first = first;
        }

        @Override
        public Optional<String> firstOrEmpty() {
            return this.first;
        }

        private final Optional<String> first;
    }
}
