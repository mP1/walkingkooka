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

package walkingkooka.xml;

import walkingkooka.collect.map.MapTestCase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public abstract class DomMapTestCase<M extends DomMap<K, V>, K, V> extends MapTestCase<M, K, V> {

    DomMapTestCase() {
        super();
    }

    final DocumentBuilder documentBuilder(final boolean namespaceAware, final boolean entityReferences) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            factory.setValidating(false);
            factory.setExpandEntityReferences(entityReferences);
            return factory.newDocumentBuilder();
        } catch (final Exception cause) {
            throw new RuntimeException(cause);
        }
    }
}
