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
 * A function that returns a substring of another string.<br>
 * <a href="https://developer.mozilla.org/en-US/docs/Web/XPath/Functions/substring"></a>
 */
final class NodeSelectorPredicateSubstringFunction extends NodeSelectorPredicateFunction<String> {

    /**
     * Singleton
     */
    static final NodeSelectorPredicateSubstringFunction INSTANCE = new NodeSelectorPredicateSubstringFunction();

    /**
     * Private ctor
     */
    private NodeSelectorPredicateSubstringFunction() {
        super();
    }

    @Override
    public String apply(final List<Object> parameters, final NodeSelectorPredicateExpressionEvaluationContext context) {
        final int parameterCount = parameters.size();
        switch (parameterCount) {
            case 2:
                break;
            case 3:
                break;
            default:
                throw new IllegalArgumentException("Expected 2 or 3 parameters (String, offset, [length])=" + parameters);
        }

        final String string = context.string(parameters, 0);

        final int offset = context.integer(parameters, 1);
        final int zeroOffset = offset - NodeSelector.INDEX_BIAS;

        final int length = parameterCount == 3 ?
                context.integer(parameters, 2) :
                string.length() - offset + NodeSelector.INDEX_BIAS;

        return string.substring(zeroOffset, length + zeroOffset);
    }

    @Override
    public String toString() {
        return "substring";
    }
}
