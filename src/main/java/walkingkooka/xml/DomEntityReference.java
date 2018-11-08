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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.List;
import java.util.Map;

/**
 * A {@link DomNode} that holds an entityDefinition reference.
 *
 * <pre>
 * <!ENTITY entity \"<!--comment-->text\"">
 * </pre>
 */
final public class DomEntityReference extends DomParentNode2 {

    public final static String OPEN = "<!ENTITY ";
    public final static String CLOSE = ">";

    DomEntityReference(final org.w3c.dom.Node reference) {
        super(reference);
    }

    private org.w3c.dom.EntityReference entityReferenceNode() {
        return Cast.to(this.node);
    }

    // children................................................................................................

    @Override
    public DomEntity setChildren(final List<DomNode> children) {
        throw new UnsupportedOperationException();
    }

    // attributes......................................................................................................

    @Override
    public Map<DomAttributeName, String> attributes() {
        return NO_ATTRIBUTES;
    }

    @Override
    public DomNode setAttributes(final Map<DomAttributeName, String> attributes) {
        throw new UnsupportedOperationException();
    }

    // DomNode ...............................................................................................

    @Override
    DomNodeKind kind() {
        return DomNodeKind.ENTITY_REFERENCE;
    }

    @Override
    DomEntityReference wrap0(final org.w3c.dom.Node node) {
        return new DomEntityReference(node);
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
        return other instanceof DomEntityReference;
    }

    @Override
    boolean equalsIgnoringParentAndChildren(final DomNode other) {
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
