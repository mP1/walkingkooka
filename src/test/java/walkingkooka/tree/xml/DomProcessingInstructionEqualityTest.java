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

public final class DomProcessingInstructionEqualityTest extends DomLeafNodeEqualityTestCase<DomProcessingInstruction> {

    private final String TARGET = "target-abc";
    private final String PROCESSING_INSTRUCTION = "pi-123";

    // toSearchNode.....................................................................................................

    @Test
    public void testDifferentTarget() {
        this.checkNotEquals(this.createNode("different", PROCESSING_INSTRUCTION));
    }

    @Test
    public void testDifferentProcessingInstruction() {
        this.checkNotEquals(this.createNode(TARGET, "different"));
    }

    @Override
    DomProcessingInstruction createNode(final Document document) {
        return createNode(document, TARGET, PROCESSING_INSTRUCTION);
    }

    private DomProcessingInstruction createNode(final String target,
                                                final String processingInstruction) {
        return this.createNode(this.document(), target, processingInstruction);
    }

    private DomProcessingInstruction createNode(final Document document,
                                                final String target,
                                                final String processingInstruction) {
        return new DomProcessingInstruction(document.createProcessingInstruction(target, processingInstruction));
    }

    final DomProcessingInstruction createNode(final DocumentBuilder builder) {
        return this.createNode(builder.newDocument());
    }
}
