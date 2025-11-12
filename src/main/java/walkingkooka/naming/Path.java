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
import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A path is hierarchical composed of multiple names.
 */
public interface Path<P extends Path<P, N>, N extends Name> extends Value<String>,
    HasName<N>,
    Iterable<N>,
    CanBeEmpty {

    /**
     * Path segment for the current directory
     */
    String CURRENT = ".";

    /**
     * Path segment for the parent directory
     */
    String PARENT = "..";

    /**
     * Returns the parent {@link Path}. For a root this will return null.
     */
    Optional<P> parent();

    /**
     * returns true if this {@link Path} is the root.
     */
    default boolean isRoot() {
        return false == this.parent().isPresent();
    }

    /**
     * Returns true if this path begins with the {@link #separator()}.
     */
    default boolean isStartsWithSeparator() {
        final String value = this.value();
        return value.length() > 0 &&
            value.charAt(0) == this.separator()
                .character();
    }

    /**
     * Appends or concatenates the given {@link Name} onto the end of this {@link Path} creating a new {@link Path}.
     */
    P append(N name);

    /**
     * Appends the given {@link Path} to this returning the result.
     */
    default P append(final P path) {
        Objects.requireNonNull(path, "path");

        P result = Cast.to(this);

        for (N component : path) {
            result = result.append(component);
        }

        return result;
    }

    /**
     * Returns a {@link List} of {@link Name} with the ROOT as the first element.
     */
    default List<N> namesList() {
        final List<N> names = Lists.array();
        PathHelper.gatherNames(
            this,
            names
        );
        return Lists.readOnly(names);
    }

    /**
     * Returns an iterator containing but not including the root name.
     */
    @Override
    default Iterator<N> iterator() {
        return PathIterator.with(this);
    }

    /**
     * The path as a {@link String}.
     */
    @Override
    String value();

    /**
     * Getter that returns the {@link PathSeparator}.
     */
    PathSeparator separator();

    // CanBeEmpty.......................................................................................................

    @Override
    default boolean isEmpty() {
        return this.value()
            .isEmpty();
    }
}
