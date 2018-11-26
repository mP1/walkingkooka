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

package walkingkooka.tree.xml;

import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import java.io.Reader;

public final class DomNotationEqualityTest extends DomLeafNodeEqualityTestCase<DomNotation> {

    @Test
    public void testDifferent() {
        this.checkNotEquals(this.createNode(this.documentBuilder(), "different.xml"));
    }

    @Override
    DomNotation createNode(final Document document) {
        return this.createNode(this.documentBuilder());
    }

    @Override
    final DomNotation createNode(final DocumentBuilder builder) {
        return this.createNode(builder, "default.xml");
    }

    private DomNotation createNode(final DocumentBuilder builder, final String file) {
        try (Reader reader = this.resourceAsReader(this.getClass(), this.getClass().getSimpleName() + "/" + file)) {
            final DomDocument root = DomNode.fromXml(builder, reader);
            return new DomNotation(root.documentNode().getDoctype().getNotations().item(0));
        } catch (final Exception rethrow) {
            throw new RuntimeException(rethrow);
        }
    }
}
