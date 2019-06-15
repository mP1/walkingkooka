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
import org.w3c.dom.Notation;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link XmlNode} that notation.
 *
 * <pre>
 * <!NOTATION nname PUBLIC std>
 * <!NOTATION nname SYSTEM url>
 * </pre>
 */
final public class XmlNotation extends XmlLeafNode implements HasXmlPublicId, HasXmlSystemId {

    private final static String START = "<!NOTATION ";
    private final static String END = ">";

    static XmlNotation with(final Node node) {
        return new XmlNotation(node);
    }

    private XmlNotation(final Node node) {
        super(node);
    }

    private Notation notationNode() {
        return Cast.to(this.node);
    }

    // HasPublicId ........................................................................................

    @Override
    public Optional<XmlPublicId> publicId() {
        if (null == this.publicId) {
            this.publicId = XmlPublicId.with(this.notationNode().getPublicId());
        }
        return this.publicId;
    }

    private Optional<XmlPublicId> publicId;

    // HasSystemId ........................................................................................

    @Override
    public Optional<XmlSystemId> systemId() {
        if (null == this.systemId) {
            this.systemId = XmlSystemId.with(this.notationNode().getSystemId());
        }
        return this.systemId;
    }

    private Optional<XmlSystemId> systemId;

    // XmlNode .................................................................................................

    @Override
    public XmlNotation removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    XmlNotation wrap0(final Node node) {
        return new XmlNotation(node);
    }

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.NOTATION;
    }

    @Override
    public boolean isNotation() {
        return true;
    }

    // toSearchNode...............................................................................................

    @Override
    SearchNode toSearchNode0() {
        final List<SearchNode> searchNodes = Lists.array();

        this.publicId().ifPresent((p)-> {
            searchNodes.add(p.toSearchNode());
        });

        this.systemId().ifPresent((s)-> {
            searchNodes.add(s.toSearchNode());
        });

        return SearchNode.sequence(searchNodes);
    }

    @Override
    SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("Notation");

    // Object...............................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.name(), this.publicId, this.systemId);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof XmlNotation;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final XmlNode other) {
        return equalsIgnoringParentAndChildren0(other.cast());
    }

    private boolean equalsIgnoringParentAndChildren0(final XmlNotation other) {
        return this.name().equals(other.name()) &&
                this.publicId().equals(other.publicId()) &&
                this.systemId().equals(other.systemId());
    }

    // UsesToStringBuilder...........................................................................................

    /**
     * <pre>
     * <!NOTATION name PUBLIC "publicId" SYSTEM "systemId">
     * </pre>
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.append(XmlNotation.START);
        builder.value(this.name);

        buildToString(this.publicId(), this.systemId(), builder);

        builder.append(XmlNotation.END);
    }
}
