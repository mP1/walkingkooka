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

package walkingkooka.collect.list;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * An immutable list of {@link Boolean} that allows null elements.
 */
public final class BooleanList extends AbstractList<Boolean>
    implements ImmutableListDefaults<BooleanList, Boolean> {

    /**
     * An empty {@link BooleanList}.
     */
    public final static BooleanList EMPTY = new BooleanList(
        Lists.empty()
    );

    // @VisibleForTesting
    BooleanList(final List<Boolean> booleans) {
        this.booleans = booleans;
    }

    @Override
    public Boolean get(int index) {
        return this.booleans.get(index);
    }

    @Override
    public int size() {
        return this.booleans.size();
    }

    private final List<Boolean> booleans;

    @Override
    public void elementCheck(final Boolean booleanValue) {
        // nulls are allowed.
    }

    @Override
    public BooleanList setElements(final Collection<Boolean> booleans) {
        BooleanList booleanList;

        if (booleans instanceof BooleanList) {
            booleanList = (BooleanList) booleans;
        } else {
            final List<Boolean> copy = Lists.array();
            copy.addAll(
                Objects.requireNonNull(booleans, "booleans")
            );

            switch (booleans.size()) {
                case 0:
                    booleanList = EMPTY;
                    break;
                default:
                    booleanList = new BooleanList(copy);
                    break;
            }
        }

        return this.equals(booleanList) ?
            this :
            booleanList;
    }
}
