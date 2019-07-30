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

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import walkingkooka.Cast;
import walkingkooka.text.CharSequences;

import java.util.Objects;

enum XmlNodeKind {
    CDATA {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        XmlName name(final XmlNode node) {
            return XmlName.CDATA_SECTION;
        }

        @Override
        String text(final Node node) {
            return this.text0(node);
        }
    },
    COMMENT {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        XmlName name(final XmlNode node) {
            return XmlName.COMMENT;
        }

        @Override
        String text(final Node node) {
            return this.text0(node);
        }
    },
    DOCUMENT {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        XmlName name(final XmlNode node) {
            return XmlName.DOCUMENT;
        }

        @Override
        String text(final Node node) {
            return "";
        }
    },
    DOCUMENT_TYPE {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        XmlName name(final XmlNode node) {
            final DocumentType documentType = Cast.to(node.node);
            return XmlName.with(documentType.getName(), this);
        }

        @Override
        String text(final Node node) {
            return "";
        }
    },
    ELEMENT {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return XmlElement.with(document.createElement(name.value()));
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return prefix.createElement(namespaceUri, name, document);
        }

        @Override
        void check(final String name) {
            //Predicates.failIfNullOrFalse(name, DomPredicates.elementName(), "elementNode name %s");
        }

        @Override
        XmlName name(final XmlNode node) {
            final Element element = Cast.to(node.node);
            return XmlName.with(element.getTagName(), this);
        }

        @Override
        String text(final Node node) {
            return this.text0(node);
        }
    },
    ENTITY {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            Objects.requireNonNull(name, "name");
        }

        @Override
        XmlName name(final XmlNode node) {
            throw new UnsupportedOperationException();
        }

        @Override
        String text(final Node node) {
            return "";
        }
    },

    ENTITY_REFERENCE {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            Objects.requireNonNull(name, "name");
        }

        @Override
        XmlName name(final XmlNode node) {
            return XmlName.with(node.node.getNodeName(), this);
        }

        @Override
        String text(final Node node) {
            return "";
        }
    },
    NOTATION {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            //Predicates.failIfNullOrFalse(name, DomPredicates.notation(), "notation name %s");
        }

        @Override
        XmlName name(final XmlNode node) {
            return XmlName.with(node.node.getNodeName(), this);
        }

        @Override
        String text(final Node node) {
            return "";
        }
    },
    PROCESSING_INSTRUCTION {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            // nop
        }

        @Override
        XmlName name(final XmlNode node) {
            final ProcessingInstruction pi = Cast.to(node.node);
            return XmlName.with(pi.getTarget(), this);
        }

        @Override
        String text(final Node node) {
            return this.text0(node);
        }
    },
    TEXT {
        @Override
        XmlElement createElement(final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        XmlElement createElement(final String namespaceUri,
                                 final XmlNameSpacePrefix prefix,
                                 final XmlName name,
                                 final Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        XmlName name(final XmlNode node) {
            return XmlName.TEXT;
        }

        @Override
        String text(final Node node) {
            return this.text0(node);
        }
    };

    final XmlName with(final String name) {
        CharSequences.failIfNullOrEmpty(name, "name");
        check(name);
        return XmlName.with(name, this);
    }

    abstract void check(final String name);

    abstract XmlElement createElement(final XmlName name, final Document document);

    abstract XmlElement createElement(final String namespaceUri,
                                      final XmlNameSpacePrefix prefix,
                                      final XmlName name,
                                      final Document document);

    abstract XmlName name(final XmlNode node);

    abstract String text(final Node node);

    final String text0(final Node node) {
        return node.getTextContent();
    }
}
