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

package walkingkooka.naming;

import walkingkooka.CanBeEmpty;
import walkingkooka.Value;
import walkingkooka.io.FileExtension;
import walkingkooka.io.HasFileExtension;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.HasCaseSensitivity;
import walkingkooka.text.HasText;

import java.util.Comparator;
import java.util.Optional;

/**
 * Interface implemented by names. Names are immutable and should also implement {@link Comparable}.
 */
public interface Name extends Value<String>,
    HasCaseSensitivity,
    HasFileExtension,
    HasText,
    CanBeEmpty {

    /**
     * A {@link Comparator} for {@link Name} using the given {@link CaseSensitivity}.
     */
    static <T extends Name> Comparator<T> comparator(final CaseSensitivity caseSensitivity) {
        return NameComparator.with(caseSensitivity);
    }

    @Override
    default String text() {
        return this.value();
    }

    // HasFileExtension.................................................................................................

    @Override
    default Optional<FileExtension> fileExtension() {
        return FileExtension.extract(
            this.value()
        );
    }

    // CanBeEmpty.......................................................................................................

    @Override
    default boolean isEmpty() {
        return this.text()
            .isEmpty();
    }
}
