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

import org.w3c.dom.Node;
import org.w3c.dom.Text;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.tree.search.SearchNodeName;

/**
 * Represents a text node within a xml document.
 */
public final class XmlText extends XmlTextNode {

    static XmlText with(final Node node) {
        return new XmlText(node);
    }

    private XmlText(final Node node) {
        super(node);
    }

    private Text textNode() {
        return Cast.to(this.node);
    }

    public boolean isElementContentWhitespace() {
        return this.textNode().isElementContentWhitespace();
    }

    @Override
    public XmlText setText(final String text) {
        checkTextText(text);
        return this.setText0(text).cast();
    }

    @Override
    public XmlText removeParent() {
        return this.removeParent0().cast();
    }

    // XmlNode....................................................................................................

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.TEXT;
    }

    @Override
    XmlText wrap0(final Node node) {
        return new XmlText(node);
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
        return other instanceof XmlText;
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
