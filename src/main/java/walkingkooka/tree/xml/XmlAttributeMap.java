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

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;

import java.util.Map;

/**
 * Read only map view of attributes.
 */
final class XmlAttributeMap extends XmlMap<XmlAttributeName, String> {

    static {
        Maps.registerImmutableType(XmlAttributeMap.class);
    }

    static Map<XmlAttributeName, String> from(final NamedNodeMap attributes) {
        return isEmpty(attributes) ?
                Maps.empty() :
                new XmlAttributeMap(attributes);
    }

    private XmlAttributeMap(final NamedNodeMap attributes) {
        super(attributes);
    }

    @Override
    boolean isKey(final Object key) {
        return key instanceof XmlAttributeName;
    }

    @Override
    Entry<XmlAttributeName, String> entry(final Node node) {
        final Attr attr = Cast.to(node);
        return Maps.entry(XmlAttributeName.wrap(attr), attr.getValue());
    }
}
