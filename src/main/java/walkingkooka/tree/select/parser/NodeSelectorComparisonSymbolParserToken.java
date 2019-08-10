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
package walkingkooka.tree.select.parser;

/**
 * Base class for all binary arithmetic operand symbols
 */
abstract class NodeSelectorComparisonSymbolParserToken extends NodeSelectorBinaryOperandSymbolParserToken {

    NodeSelectorComparisonSymbolParserToken(final String value, final String text) {
        super(value, text);
    }

    @Override
    public final boolean isAndSymbol() {
        return false;
    }
    
    @Override
    public final boolean isDivideSymbol() {
        return false;
    }

    @Override
    public final boolean isMinusSymbol() {
        return false;
    }

    @Override
    public final boolean isModuloSymbol() {
        return false;
    }

    @Override
    public final boolean isMultiplySymbol() {
        return false;
    }

    @Override
    public final boolean isOrSymbol() {
        return false;
    }

    @Override
    public final boolean isPlusSymbol() {
        return false;
    }
}
