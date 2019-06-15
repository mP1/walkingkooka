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
import org.w3c.dom.Document;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

public final class XmlProcessingInstructionTest extends XmlLeafNodeTestCase<XmlProcessingInstruction> {

    private final String TARGET = "target-abc";
    private final String PROCESSING_INSTRUCTION = "pi-123";

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentTarget() {
        this.checkNotEquals(this.createNode("different", PROCESSING_INSTRUCTION));
    }

    @Test
    public void testEqualsDifferentProcessingInstruction() {
        this.checkNotEquals(this.createNode(TARGET, "different"));
    }

    private XmlProcessingInstruction createNode(final String target,
                                                final String processingInstruction) {
        return this.createNode(this.document(), target, processingInstruction);
    }

    private XmlProcessingInstruction createNode(final Document document,
                                                final String target,
                                                final String processingInstruction) {
        return XmlProcessingInstruction.with(document.createProcessingInstruction(target, processingInstruction));
    }

    // toSearchNode.....................................................................................................

    @Test
    public void testToSearchNode() {
        final XmlProcessingInstruction pi = this.createNode();

        this.toSearchNodeAndCheck(pi,
                SearchNode.sequence(Lists.of(
                        SearchNode.text(TARGET, TARGET),
                        SearchNode.text(PROCESSING_INSTRUCTION, PROCESSING_INSTRUCTION)
                )).setName(SearchNodeName.with("ProcessingInstruction")));
    }

    // toString.....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createNode(), "<?target-abc pi-123?>");
    }

    // helpers............................................................................................

    @Override
    XmlProcessingInstruction createNode(final Document document) {
        return XmlProcessingInstruction.with(document.createProcessingInstruction(TARGET, PROCESSING_INSTRUCTION));
    }

    @Override
    String text() {
        return PROCESSING_INSTRUCTION;
    }

    @Override
    Class<XmlProcessingInstruction> nodeType() {
        return XmlProcessingInstruction.class;
    }
}
