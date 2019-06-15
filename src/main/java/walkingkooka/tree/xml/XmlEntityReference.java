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

import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;
import java.util.Map;

/**
 * A {@link XmlNode} that holds an entityDefinition reference.
 *
 * <pre>
 * <!ENTITY entity \"<!--comment-->text\"">
 * </pre>
 */
final public class XmlEntityReference extends XmlParentNode2 {

    public final static String OPEN = "<!ENTITY ";
    public final static String CLOSE = ">";

    static XmlEntityReference with(final Node reference) {
        return new XmlEntityReference(reference);
    }

    private XmlEntityReference(final Node reference) {
        super(reference);
    }

    private EntityReference entityReferenceNode() {
        return Cast.to(this.node);
    }

    // parent................................................................................................

    @Override
    public XmlEntityReference removeParent() {
        return this.removeParent0().cast();
    }

    // children................................................................................................

    @Override
    public XmlEntity setChildren(final List<XmlNode> children) {
        throw new UnsupportedOperationException();
    }

    // attributes......................................................................................................

    @Override
    public Map<XmlAttributeName, String> attributes() {
        return NO_ATTRIBUTES;
    }

    @Override
    public XmlNode setAttributes(final Map<XmlAttributeName, String> attributes) {
        throw new UnsupportedOperationException();
    }

    // XmlNode ...............................................................................................

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.ENTITY_REFERENCE;
    }

    @Override
    XmlEntityReference wrap0(final Node node) {
        return new XmlEntityReference(node);
    }

    @Override
    public boolean isEntityReference() {
        return true;
    }

    @Override
    SearchNode toSearchNode0() {
        return this.toSearchNode1();
    }

    @Override
    SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("EntityReference");

    // Object ...............................................................................................

    @Override
    public int hashCode() {
        return this.name().hashCode();
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof XmlEntityReference;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final XmlNode other) {
        return this.name().equals(other.name());
    }

    // UsesToStringBuilder...........................................................................................

    /**
     * Returns the XML text.
     *
     * <pre>
     * <!ENTITY entityDefinition \"<!--comment-->text\"">
     * </pre>
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.surroundValues("&", ";");
        builder.value(new Object[]{this.name().value()});
    }
}
