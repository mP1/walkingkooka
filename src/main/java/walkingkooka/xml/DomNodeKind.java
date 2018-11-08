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

import java.util.Objects;

enum DomNodeKind {
    CDATA {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        DomName name(final DomNode node) {
            return DomName.CDATA_SECTION;
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return this.text0(node);
        }
    },
    COMMENT {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        DomName name(final DomNode node) {
            return DomName.COMMENT;
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return this.text0(node);
        }
    },
    DOCUMENT {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        DomName name(final DomNode node) {
            return DomName.DOCUMENT;
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return "";
        }
    },
    DOCUMENT_TYPE {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        DomName name(final DomNode node) {
            final org.w3c.dom.DocumentType documentType = Cast.to(node.node);
            return new DomName(documentType.getName(), this);
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return "";
        }
    },
    ELEMENT {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return new DomElement(document.createElement(name.value()));
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return prefix.createElement(namespaceUri, name, document);
        }

        @Override
        void check(final String name) {
            //Predicates.failIfNullOrFalse(name, DomPredicates.elementName(), "elementNode name %s");
        }

        @Override
        DomName name(final DomNode node) {
            final org.w3c.dom.Element element = Cast.to(node.node);
            return new DomName(element.getTagName(), this);
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return this.text0(node);
        }
    },
    ENTITY {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            Objects.requireNonNull(name, "name");
        }

        @Override
        DomName name(final DomNode node) {
            throw new UnsupportedOperationException();
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return "";
        }
    },

    ENTITY_REFERENCE {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            Objects.requireNonNull(name, "name");
        }

        @Override
        DomName name(final DomNode node) {
            return new DomName(node.node.getNodeName(), this);
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return "";
        }
    },
    NOTATION {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            //Predicates.failIfNullOrFalse(name, DomPredicates.notation(), "notation name %s");
        }

        @Override
        DomName name(final DomNode node) {
            return new DomName(node.node.getNodeName(), this);
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return "";
        }
    },
    PROCESSING_INSTRUCTION {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
            // nop
        }

        @Override
        DomName name(final DomNode node) {
            final org.w3c.dom.ProcessingInstruction pi = Cast.to(node.node);
            return new DomName(pi.getTarget(), this);
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return this.text0(node);
        }
    },
    TEXT {
        @Override
        DomElement createElement(final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        DomElement createElement(final String namespaceUri,
                                 final DomNameSpacePrefix prefix,
                                 final DomName name,
                                 final org.w3c.dom.Document document) {
            return name.failInvalidTagName();
        }

        @Override
        void check(final String name) {
        }

        @Override
        DomName name(final DomNode node) {
            return DomName.TEXT;
        }

        @Override
        String text(final org.w3c.dom.Node node) {
            return this.text0(node);
        }
    };

    final DomName with(final String name) {
        Objects.requireNonNull(name, "name");
        check(name);
        return new DomName(name, this);
    }

    abstract void check(final String name);

    abstract DomElement createElement(final DomName name, final org.w3c.dom.Document document);

    abstract DomElement createElement(final String namespaceUri,
                                      final DomNameSpacePrefix prefix,
                                      final DomName name,
                                      final org.w3c.dom.Document document);

    abstract DomName name(final DomNode node);

    abstract String text(final org.w3c.dom.Node node);

    final String text0(final org.w3c.dom.Node node) {
        return node.getTextContent();
    }
}
