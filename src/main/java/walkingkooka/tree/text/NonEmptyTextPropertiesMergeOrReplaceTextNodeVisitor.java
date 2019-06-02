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
 *
 */

package walkingkooka.tree.text;

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.visit.Visiting;

/**
 * Takes a {@link TextNode} wrapping if not a {@link TextPropertiesNode}, merging/replaces properties
 */
final class NonEmptyTextPropertiesMergeOrReplaceTextNodeVisitor extends TextNodeVisitor {

    static TextNode acceptAndMergeOrReplace(final TextProperties textProperties,
                                            final TextNode node,
                                            final NonEmptyTextPropertiesMergeOrReplace mergeOrReplace) {
        final NonEmptyTextPropertiesMergeOrReplaceTextNodeVisitor visitor = new NonEmptyTextPropertiesMergeOrReplaceTextNodeVisitor(textProperties,
                mergeOrReplace);
        visitor.accept(node);
        return visitor.node;
    }

    NonEmptyTextPropertiesMergeOrReplaceTextNodeVisitor(final TextProperties textProperties,
                                                        final NonEmptyTextPropertiesMergeOrReplace mergeOrReplace) {
        super();
        this.textProperties = textProperties;
        this.mergeOrReplace = mergeOrReplace;
    }

    @Override
    protected Visiting startVisit(final TextPropertiesNode node) {
        this.node = this.mergeOrReplace.accept(node, textProperties);
        return Visiting.SKIP;
    }

    @Override
    protected Visiting startVisit(final TextStyleNameNode node) {
        this.setChild(node);
        return Visiting.SKIP;
    }

    @Override
    protected void visit(final TextPlaceholderNode node) {
        this.setChild(node);
    }

    @Override
    protected void visit(final Text node) {
        this.setChild(node);
    }

    private void setChild(final TextNode child) {
        this.node = this.textProperties.setChildren(Lists.of(child));
    }

    private final TextProperties textProperties;
    private final NonEmptyTextPropertiesMergeOrReplace mergeOrReplace;
    private TextNode node;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.textProperties)
                .value(this.mergeOrReplace)
                .value(this.node)
                .build();
    }
}
