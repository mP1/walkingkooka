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
package walkingkooka.text.cursor.parser;

import walkingkooka.tree.Node;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Node} wrapper around a {@link ParentParserToken} which means it includes children.
 */
final class ParserTokenParentNode extends ParserTokenNode{

    ParserTokenParentNode(final ParentParserToken token, final Optional<ParserTokenNode> parent, final int index) {
        super(token, parent, index);
        this.childrenParent = Optional.of(this);
    }

    // children ...........................................................................................................

    @Override
    public List<ParserTokenNode> children() {
        if(null == this.children) {
            this.children = new ParserTokenParentNodeList(this);
        }
        return this.children;
    }

    private ParserTokenParentNodeList children;

    final Optional<ParserTokenNode> childrenParent;

    @Override
    public List<ParserToken> childrenValues() {
        return this.valueAsList();
    }
}
