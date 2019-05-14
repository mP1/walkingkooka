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

package walkingkooka.stream.push;

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.List;

/**
 * A {@link CloseableCollection} with at least one {@link Runnable}.
 */
final class NonEmptyCloseableCollection extends CloseableCollection implements HashCodeEqualsDefined {

    static NonEmptyCloseableCollection with(final List<Runnable> closeables) {
        return new NonEmptyCloseableCollection(closeables);
    }

    private NonEmptyCloseableCollection(final List<Runnable> closeables) {
        super();
        this.closeables = closeables;
    }

    @Override
    NonEmptyCloseableCollection add0(final Runnable closeable) {
        final List<Runnable> copy = Lists.array();
        copy.addAll(this.closeables);
        copy.add(closeable);
        return new NonEmptyCloseableCollection(copy);
    }

    @Override
    public void close() {
        RuntimeException thrown = null;

        for (Runnable closeable : this.closeables) {
            try {
                closeable.run();
            } catch (final RuntimeException cause) {
                if (null == thrown) {
                    thrown = cause;
                } else {
                    thrown.addSuppressed(cause);
                }
            }
        }

        if (null != thrown) {
            throw thrown;
        }
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.closeables.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof NonEmptyCloseableCollection && this.equals0(Cast.to(other));
    }

    private boolean equals0(final NonEmptyCloseableCollection other) {
        return this.closeables.equals(other.closeables);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.valueSeparator(", ");
        builder.value(this.closeables);
    }

    // VisibleForTesting
    final List<Runnable> closeables;
}
