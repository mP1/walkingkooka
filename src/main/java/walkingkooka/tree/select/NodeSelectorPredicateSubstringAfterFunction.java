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

package walkingkooka.tree.select;

import java.util.List;

/**
 * A function that returns the part of string1 after the first occurrence of string2
 */
final class NodeSelectorPredicateSubstringAfterFunction extends NodeSelectorPredicateFunction<String> {

    /**
     * Singleton
     */
    static final NodeSelectorPredicateSubstringAfterFunction INSTANCE = new NodeSelectorPredicateSubstringAfterFunction();

    /**
     * Private ctor
     */
    private NodeSelectorPredicateSubstringAfterFunction() {
        super();
    }

    @Override
    public String apply(final List<Object> parameters,
                        final NodeSelectorPredicateExpressionEvaluationContext<?, ?, ?, ?> context) {
        this.checkParameterCount(parameters, 2);

        final String string = context.string(parameters, 0);
        final String find = context.string(parameters, 1);
        final int offset = string.indexOf(find);

        return -1 != offset ?
                string.substring(offset + find.length()) :
                "";
    }

    @Override
    public String toString() {
        return "substring-after";
    }
}
