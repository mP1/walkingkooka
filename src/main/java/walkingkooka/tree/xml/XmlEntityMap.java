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

package walkingkooka.tree.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import walkingkooka.collect.map.Maps;

import java.util.Map;

/**
 * Read only map view of entities.
 */
final class XmlEntityMap extends XmlMap<XmlName, XmlEntity> {

    static {
        Maps.registerImmutableType(XmlEntityMap.class);
    }

    static Map<XmlName, XmlEntity> from(final NamedNodeMap entities) {
        return isEmpty(entities) ?
                Maps.empty() :
                new XmlEntityMap(entities);
    }

    private XmlEntityMap(final NamedNodeMap entities) {
        super(entities);
    }

    @Override
    boolean isKey(final Object key) {
        return key instanceof XmlName;
    }

    @Override
    Entry<XmlName, XmlEntity> entry(final Node node) {
        final XmlEntity entity = XmlEntity.with(node);

        return Maps.entry(entity.name(), entity);
    }
}
