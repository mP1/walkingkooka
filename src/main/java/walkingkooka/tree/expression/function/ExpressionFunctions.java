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

package walkingkooka.tree.expression.function;

import walkingkooka.type.PublicStaticHelper;

/**
 * Collection of static factory methods for numerous {@link ExpressionFunction}.
 */
public final class ExpressionFunctions implements PublicStaticHelper {

    /**
     * {@see ExpressionFunction}
     */
    public static ExpressionFunction<Boolean> booleanExpressionFunction() {
        return ExpressionTemplateFunction.BOOLEAN;
    }

    /**
     * {@see ExpressionConcatFunction}
     */
    public static ExpressionFunction<String> concat() {
        return ExpressionTemplateFunction.CONCAT;
    }

    /**
     * {@see ExpressionContainsFunction}
     */
    public static ExpressionFunction<Boolean> contains() {
        return ExpressionTemplateFunction.CONTAINS;
    }

    /**
     * {@see ExpressionEndsWithFunction}
     */
    public static ExpressionFunction<Boolean> endsWith() {
        return ExpressionTemplateFunction.ENDS_WITH;
    }

    /**
     * {@see ExpressionFalseFunction}
     */
    public static ExpressionFunction<Boolean> falseExpressionFunction() {
        return ExpressionTemplateFunction.FALSE;
    }

    /**
     * {@see ExpressionNormalizeSpaceFunction}
     */
    public static ExpressionFunction<String> normalizeSpace() {
        return ExpressionTemplateFunction.NORMALIZE_SPACE;
    }

    /**
     * {@see ExpressionNodeNameFunction}
     */
    public static ExpressionFunction<String> nodeName() {
        return ExpressionTemplateFunction.NODE_NAME;
    }

    /**
     * {@see ExpressionNodePositionFunction}
     */
    public static ExpressionFunction<Number> nodePosition() {
        return ExpressionTemplateFunction.NODE_POSITION;
    }

    /**
     * {@see ExpressionNumberFunction}
     */
    public static ExpressionFunction<Number> number() {
        return ExpressionTemplateFunction.NUMBER;
    }

    /**
     * {@see ExpressionStartsWithFunction}
     */
    public static ExpressionFunction<Boolean> startsWith() {
        return ExpressionTemplateFunction.STARTS_WITH;
    }

    /**
     * {@see ExpressionStringLengthFunction}
     */
    public static ExpressionFunction<Number> stringLength() {
        return ExpressionTemplateFunction.STRING_LENGTH;
    }

    /**
     * {@see ExpressionSubstringFunction}
     */
    public static ExpressionFunction<String> substring(final int indexBias) {
        return ExpressionTemplateFunction.substring(indexBias);
    }

    /**
     * {@see ExpressionSubstringAfterFunction}
     */
    public static ExpressionFunction<String> substringAfter() {
        return ExpressionTemplateFunction.SUBSTRING_AFTER;
    }

    /**
     * {@see ExpressionSubstringBeforeFunction}
     */
    public static ExpressionFunction<String> substringBefore() {
        return ExpressionTemplateFunction.SUBSTRING_BEFORE;
    }

    /**
     * {@see ExpressionTextFunction}
     */
    public static ExpressionFunction<String> text() {
        return ExpressionTemplateFunction.TEXT;
    }

    /**
     * {@see ExpressionTrueFunction}
     */
    public static ExpressionFunction<Boolean> trueExpressionFunction() {
        return ExpressionTemplateFunction.TRUE;
    }

    /**
     * Stops creation
     */
    private ExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
