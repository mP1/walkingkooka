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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.tree.search.SearchNodeName;

/**
 * A {@link DomNode} that holds a CDATA section.
 */
final public class DomCDataSection extends DomTextNode {

    public final static String OPEN = "<![CDATA[";
    public final static String CLOSE = "]]>";

    DomCDataSection(final org.w3c.dom.Node node) {
        super(node);
    }

    @Override
    public DomCDataSection setText(final String text) {
        checkCDataSectionText(text);
        return this.setText0(text).cast();
    }

    // DomNode...........................................................................................

    @Override
    DomNodeKind kind() {
        return DomNodeKind.CDATA;
    }

    @Override
    DomCDataSection wrap0(final org.w3c.dom.Node node) {
        return new DomCDataSection(node);
    }

    @Override
    public boolean isCDataSection() {
        return true;
    }

    @Override
    final SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("CData");

    // Object...........................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DomCDataSection;
    }

    // UsesToStringBuilder...........................................................................................

    /**
     * Adds the data surrounded by the start and end markers
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.surroundValues(DomCDataSection.OPEN, DomCDataSection.CLOSE);
        builder.value(new Object[]{this.text()});// array required so surroundValues works.
    }
}
