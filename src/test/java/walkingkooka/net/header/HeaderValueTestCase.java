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

package walkingkooka.net.header;

import walkingkooka.test.ClassTesting2;

/**
 * Base class for all {@link HeaderValue} in this package.
 */
public abstract class HeaderValueTestCase<V extends HeaderValue> implements ClassTesting2<V>,
        HeaderValueTesting<V> {

    HeaderValueTestCase() {
        super();
    }

    @Override
    public final RuntimeException parseFailedExpected(final RuntimeException expected) {
        return new HeaderValueException(expected.getMessage(), expected);
    }

    @Override
    public final Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return HeaderValueException.class;
    }
}
