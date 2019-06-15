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
import org.w3c.dom.ProcessingInstruction;
import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.SearchNode;
import walkingkooka.tree.search.SearchNodeName;

import java.util.Objects;

/**
 * A {@link XmlNode} that represents a processing instruction.
 *
 * <pre>
 * &lt;?PITARGET DATA?gt;
 * </pre>
 */
final public class XmlProcessingInstruction extends XmlLeafNode implements Value<String> {

    private final static String START = "<?";
    private final static String END = "?>";

    static XmlProcessingInstruction with(final Node node) {
        return new XmlProcessingInstruction(node);
    }

    private XmlProcessingInstruction(final Node node) {
        super(node);
    }

    /**
     * Returns the target.
     */
    public String target() {
        return this.processingInstructionNode().getTarget();
    }

    /**
     * Returns the data portion.
     */
    public String data() {
        return this.processingInstructionNode().getData();
    }

    // Value ......................................................................................

    @Override
    public String value() {
        return this.data();
    }

    @Override
    public XmlProcessingInstruction removeParent() {
        return this.removeParent0().cast();
    }

    // XmlNode ......................................................................................

    final ProcessingInstruction processingInstructionNode() {
        return Cast.to(this.node);
    }

    @Override
    XmlProcessingInstruction wrap0(final Node node) {
        return new XmlProcessingInstruction(node);
    }

    @Override
    XmlNodeKind kind() {
        return XmlNodeKind.PROCESSING_INSTRUCTION;
    }

    /**
     * Always returns true.
     */
    @Override
    public boolean isProcessingInstruction() {
        return true;
    }

    // toSearchNode...............................................................................................

    @Override
    SearchNode toSearchNode0() {
        return SearchNode.sequence(Lists.of(
                textSearchNode(this.target()),
                textSearchNode(this.data())
        ));
    }

    @Override
    SearchNodeName searchNodeName() {
        return SEARCH_NODE_NAME;
    }

    private final static SearchNodeName SEARCH_NODE_NAME = SearchNodeName.with("ProcessingInstruction");

    // Object ...........................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.target(), this.data());
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof XmlProcessingInstruction;
    }

    @Override
    final boolean equalsIgnoringParentAndChildren(final XmlNode other) {
        return this.equalsIgnoringParentAndChildren0(other.cast());
    }

    private boolean equalsIgnoringParentAndChildren0(final XmlProcessingInstruction other) {
        return this.target().equals(other.target()) &&
                this.data().equals(other.data());
    }

    // UsesToStringBuilder...........................................................................................

    /**
     * <a>https://en.wikipedia.org/wiki/Processing_Instruction</a>
     *
     * <pre>
     * An XML processing instruction is enclosed within <? and ?>, and contains a target and optionally some content, which is the node value, that cannot contain the sequence ?>.[4]
     *
     * <?PITarget PIContent?>
     * </pre>
     */
    @Override
    void buildDomNodeToString(final ToStringBuilder builder) {
        builder.append(XmlProcessingInstruction.START);
        builder.append(this.target());
        builder.append(' ');
        builder.append(this.data());
        builder.append(XmlProcessingInstruction.END);
    }
}
