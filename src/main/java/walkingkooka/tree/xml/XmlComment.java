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
import walkingkooka.ToStringBuilder;
import walkingkooka.tree.search.SearchNodeName;

/**
 * A {@link XmlNode} that holds a comment.
 */
final public class XmlComment extends XmlTextNode {

    public final static String OPEN = "<!--";
    public final static String CLOSE = "-->";
    final static String ILLEGAL_CONTENT = "--";

    static XmlComment with(final Node node) {
        return new XmlComment(node);
    }

    private XmlComment(final Node node) {
        super(node);
    }

    @Override
    public XmlComment setText(final String text) {
        checkCommentText(text);
        return this.setText0(text).cast();
    }

    @Override
    public XmlComment removeParent() {
        return this.removeParent0().cast();
    }

    // XmlNode.......................................................................................................

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.COMMENT;
    }

    @Override
    XmlComment wrap0(final Node node) {
        return new XmlComment(node);
    }

    @Override
    public boolean isComment() {
        return true;
    }

    @Override
    final SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("Comment");

    // Object...........................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof XmlComment;
    }

    // UsesToStringBuilder...........................................................................................

    /**
     * Returns the text without escaping as it is not required
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.surroundValues(XmlComment.OPEN, XmlComment.CLOSE);
        builder.value(new Object[]{this.text()});// array required so surroundValues works.
    }
}
