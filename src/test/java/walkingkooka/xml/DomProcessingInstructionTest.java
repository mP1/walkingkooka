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
 */

package walkingkooka.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import walkingkooka.Cast;

public final class DomProcessingInstructionTest extends DomLeafNodeTestCase<DomProcessingInstruction> {

    private final String TARGET = "abc";
    private final String PROCESSING_INSTRUCTION = "123";

    @Test
    public void testToString() {
        assertEquals("<?abc 123?>", this.createNode().toString());
    }

    // helpers............................................................................................

    @Override
    DomProcessingInstruction createNode(final Document document) {
        return new DomProcessingInstruction(document.createProcessingInstruction(TARGET, PROCESSING_INSTRUCTION));
    }

    @Override
    String text() {
        return PROCESSING_INSTRUCTION;
    }

    @Override protected Class<DomNode> type() {
        return Cast.to(DomProcessingInstruction.class);
    }
}
