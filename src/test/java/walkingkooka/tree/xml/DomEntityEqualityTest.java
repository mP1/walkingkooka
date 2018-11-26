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
import walkingkooka.Cast;

import javax.xml.parsers.DocumentBuilder;
import java.io.Reader;

public final class DomEntityEqualityTest extends DomParentNodeEqualityTestCase<DomEntity> {

    @Test
    public void testDifferent() {
        this.checkNotEquals(this.createNode("different.xml"));
    }

    @Override
    DomEntity createNode(final Document document) {
        return this.createNode(this.documentBuilder());
    }

    @Override
    final DomEntity createNode(final DocumentBuilder builder) {
        return this.createNode("default.xml");
    }

    private DomEntity createNode(final String file) {
        try (Reader reader = this.resourceAsReader(this.getClass(), this.getClass().getSimpleName() + "/" + file)) {
            final DomDocument root = DomNode.fromXml(this.documentBuilder(false, true), reader);
            final org.w3c.dom.DocumentType documentType = Cast.to(root.node.getChildNodes().item(0));
            final org.w3c.dom.Entity entity = Cast.to(documentType.getEntities().item(0));
            return new DomEntity(entity);
        } catch (final Exception rethrow) {
            throw new RuntimeException(rethrow);
        }
    }
}
