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

import java.util.List;

/**
 * A base node for any primitive array.
 */
abstract class PojoArrayNode extends PojoArrayOrCollectionNode {

    PojoArrayNode(final PojoName name,
                  final Object value,
                  final int index,
                  final PojoNodeContext context) {
        super(name, value, index, context);
    }

    // children ..................................................................................

    @Override
    final List<Object> valueAsList() {
        if(null == this.valueAsList){
            this.valueAsList = PojoArrayNodeChildrenValueList.with(this);
        }
        return this.valueAsList;
    }

    private List<Object> valueAsList;

    @Override
    public final List<Object> childrenValues() {
        return this.valueAsList();
    }

    /**
     * Used by {@link PojoArrayNodeChildrenValueList} to fetch individual elements.
     */
    abstract Object elementValue(final int index);

    @Override
    final public PojoArrayOrCollectionNode removeChild(final int child) {
        throw new UnsupportedOperationException();
    }
}
