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

package walkingkooka.tree.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link List} for all collections. Items are lazily wrapped upon the first fetch.
 */
final class PojoArrayOrCollectionNodeChildrenList<P extends PojoArrayOrCollectionNode> extends PojoNodeChildrenList<P> {

    static <P extends PojoArrayOrCollectionNode> PojoArrayOrCollectionNodeChildrenList<P> with(final P parent) {
        return new PojoArrayOrCollectionNodeChildrenList<P>(parent);
    }

    private PojoArrayOrCollectionNodeChildrenList(final P parent) {
        super(parent);
        this.nodes = new ArrayList<>();
    }

    @Override
    public final PojoNode get(final int index) {
        // expand list as necessary and lazily...
        final int size = this.parent.childrenCount() + 1;
        for (int i = index; i < size; i++) {
            this.nodes.add(null);
        }

        PojoNode node = this.nodes.get(index);
        if (null == node) {
            node = this.replace(index);
            this.nodes.set(index, node);
        }
        return node;
    }

    @Override
    void clearChildrenNodeCache() {
        final int size = this.parent.childrenCount();
        for (int i = 0; i < size; i++) {
            this.nodes.set(i, null);
        }
    }

    /**
     * Lazily wrapped pojo nodes. This may or may not expand as necessary particularly for collections which may mutate.
     */
    private final ArrayList<PojoNode> nodes;

    @Override
    Object elementValue(final int index) {
        return this.parent.valueAsList().get(index);
    }

    @Override
    PojoNode replace(final int index) {
        return this.replace0(PojoName.index(index),
                this.elementValue(index),
                index);
    }

    @Override
    boolean isSameType(final Object other) {
        return other instanceof PojoArrayOrCollectionNodeChildrenList;
    }
}
