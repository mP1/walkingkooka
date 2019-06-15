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

import org.junit.jupiter.api.Test;
import org.w3c.dom.NamedNodeMap;
import walkingkooka.Cast;
import walkingkooka.test.ResourceTesting;

import java.io.Reader;

public final class XmlNotationMapTest extends XmlMapTestCase<XmlNotationMap, XmlName, XmlNotation>
        implements ResourceTesting {

    @Test
    public void testSize() {
        this.sizeAndCheck(this.createMap(), 1);
    }

    @Override
    public XmlNotationMap createMap() {
        return Cast.to(XmlNotationMap.from(this.notations()));
    }

    private NamedNodeMap notations() {
        try (Reader reader = this.resourceAsReader(this.getClass(), this.getClass().getSimpleName() + "/createMap.xml")) {
            final XmlDocument root = XmlNode.fromXml(this.documentBuilder(false, false), reader);
            return root.documentNode().getDoctype().getNotations();
        } catch (final Exception rethrow) {
            throw new Error(rethrow);
        }
    }

    @Override
    public Class<XmlNotationMap> type() {
        return XmlNotationMap.class;
    }
}

