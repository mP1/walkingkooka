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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.tree.search.SearchNodeName;

/**
 * Represents a text node within a xml document.
 */
public final class DomText extends DomTextNode {

    DomText(final org.w3c.dom.Node node) {
        super(node);
    }

    private org.w3c.dom.Text textNode() {
        return Cast.to(this.node);
    }

    public boolean isElementContentWhitespace() {
        return this.textNode().isElementContentWhitespace();
    }

    @Override
    public DomText setText(final String text) {
        checkTextText(text);
        return this.setText0(text).cast();
    }

    // DomNode....................................................................................................

    @Override
    DomNodeKind kind() {
        return DomNodeKind.TEXT;
    }

    @Override
    DomText wrap0(final org.w3c.dom.Node node) {
        return new DomText(node);
    }

    @Override
    public boolean isText() {
        return true;
    }

    @Override
    final SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("Text");

    // Object....................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof DomText;
    }

    // UsesToStringBuilder...........................................................................................

    /**
     * Returns the text escaped.
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.value(Xmls.encode(this.text()));
    }
}
