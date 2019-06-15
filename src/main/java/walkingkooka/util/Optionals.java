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

package walkingkooka.util;

import walkingkooka.type.PublicStaticHelper;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

final public class Optionals implements PublicStaticHelper {

    /**
     * Optional#stream is not available in 1.8, and was introduced in 1.9.
     */
    public static <T> Stream<T> stream(final Optional<T> optional) {
        Objects.requireNonNull(optional, "optional");
        return optional.isPresent() ?
                Stream.of(optional.get()) :
                Stream.empty();
    }

    /**
     * Remove after upgrading to JDK 9.
     */
    public static <T> void ifPresentOrElse(final Optional<T> optional,
                                           final Consumer<? super T> action,
                                           final Runnable emptyAction) {
        if (optional.isPresent()) {
            action.accept(optional.get());
        } else {
            emptyAction.run();
        }
    }

    /**
     * Stop creation
     */
    private Optionals() {
        throw new UnsupportedOperationException();
    }
}
