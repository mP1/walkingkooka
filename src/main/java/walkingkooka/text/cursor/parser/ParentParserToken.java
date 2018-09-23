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

package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.list.Lists;
import walkingkooka.tree.search.HasSearchNode;
import walkingkooka.tree.search.SearchNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface that all parent parser tokens must implement.
 */
public interface ParentParserToken<P extends ParentParserToken> extends ParserToken, Value<List<ParserToken>>, HasSearchNode {

    /**
     * Returns a {@link List} without any {@link ParserToken tokens} that return true for {@link #isNoise()}.
     */
    static List<ParserToken> filterWithoutNoise(final List<ParserToken> value){
        return value.stream()
                .filter(t -> !t.isNoise())
                .collect(Collectors.toList());
    }

    /**
     * Would be setter that returns an instance with the given tokens or value, creating a new instance if necessary.
     */
    P setValue(List<ParserToken> tokens);

    /**
     * Concatenates the text from all child values and returns an instance with that text.
     */
    default P setTextFromValues() {
        return Cast.to(this.setText(ParserToken.text(this.value())));
    }

    /**
     * Creates a {@link SearchNode#sequence(List)} from all the converted child tokens.
     */
    default SearchNode toSearchNode() {
        final List<SearchNode> children = Lists.array();

        for(ParserToken child : this.value()) {
            children.add(child.toSearchNode());
        }

        return SearchNode.sequence(children);
    }
}
