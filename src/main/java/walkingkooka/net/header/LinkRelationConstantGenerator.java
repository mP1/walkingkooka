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

package walkingkooka.net.header;

import walkingkooka.convert.Converters;
import walkingkooka.io.printer.Printer;
import walkingkooka.io.printer.Printers;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.predicate.Predicates;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.select.NodeSelectorContext;
import walkingkooka.tree.select.NodeSelectorContexts;
import walkingkooka.tree.xml.XmlAttributeName;
import walkingkooka.tree.xml.XmlDocument;
import walkingkooka.tree.xml.XmlElement;
import walkingkooka.tree.xml.XmlName;
import walkingkooka.tree.xml.XmlNode;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A simple tool used to parse the xml at <a href="https://www.iana.org/assignments/link-relations/link-relations.xml"></a> and print
 * constant declarations for each record.
 */
final class LinkRelationConstantGenerator {

    public static void main(final String[] args) throws Exception {
        final XmlDocument document = XmlDocument.fromXml(DocumentBuilderFactory.newInstance().newDocumentBuilder(),
                file());
        XmlNode.absoluteNodeSelector()
                .descendant()
                .named(XmlName.element("record"))
                .apply(document, context());
    }

    private static BufferedReader file() {
        return new BufferedReader(new InputStreamReader(LinkRelationConstantGenerator.class.getResourceAsStream("link-relations.xml")));
    }

    private static NodeSelectorContext<XmlNode, XmlName, XmlAttributeName, String> context() {
        return NodeSelectorContexts.basic(() -> false, // dont stop!
                Predicates.always(), // filter match all
                LinkRelationConstantGenerator::record,
                LinkRelationConstantGenerator::functions,
                Converters.fake(),
                DecimalNumberContexts.fake(),
                XmlNode.class);
    }

    /**
     * <pre>
     * <record date="2016-08-12">
     *   <value>convertedFrom</value>
     *   <description>The document linked to was later converted to the
     *     document that contains this link relation.  For example, an RFC can
     *     have a link to the Internet-Draft that became the RFC; in that case,
     *     the link relation would be "convertedFrom".</description>
     *   <spec><xref type="rfc" data="rfc7991"/></spec>
     *   <note>This relation is different than "predecessor-version" in that
     *     "predecessor-version" is for items in a version control system.  It
     *     is also different than "previous" in that this relation is used for
     *     converted resources, not those that are part of a sequence of
     *     resources.</note>
     * </record>
     * </pre>
     */
    private static XmlNode record(final XmlNode node) {
        record0(XmlElement.class.cast(node));
        return node;
    }

    private static void record0(final XmlElement element) {
        final List<XmlNode> children = element.children()
                .stream()
                .filter(e -> e.isElement())
                .collect(Collectors.toList());

        final String value = children.get(0).text();
        final String description = children.get(1).text();

        final Map<XmlAttributeName, String> specAttributes = children.get(2).attributes();
        final String specType = specAttributes.get(XmlAttributeName.with("type", XmlAttributeName.NO_PREFIX));
        final String specData = specAttributes.get(XmlAttributeName.with("data", XmlAttributeName.NO_PREFIX));

        String javadoc = "/**" + description;

        if ("uri".equals(specType)) {
            javadoc = javadoc + "<br><a href=\"" + specData + "\"></a>";
        }

        if (children.size() > 3) {
            javadoc = javadoc + "<br>" + children.get(3).text();
        }

        for (; ; ) {
            final String after = javadoc.replace("\r\n", "\n")
                    .replace("\n", " ")
                    .replace("  ", " ");
            if (after.equals(javadoc)) {
                break;
            }
            javadoc = after;
        }

        javadoc = javadoc + "*/";

        printer.print(javadoc);
        lineEnding();

        printer.print("  public final static " + LinkRelation.class.getSimpleName() + " " + value.toUpperCase().replace("-", "_") + "=registerConstant(\"" + value + "\");");
        lineEnding();
        lineEnding();
        printer.flush();
    }

    private static void lineEnding() {
        printer.print(printer.lineEnding());
    }

    private final static Printer printer = Printers.sysOut();

    private static Optional<ExpressionFunction<?>> functions(final ExpressionNodeName name) {
        throw new UnsupportedOperationException();
    }
}
