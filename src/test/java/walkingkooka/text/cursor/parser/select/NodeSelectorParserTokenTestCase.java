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
package walkingkooka.text.cursor.parser.select;

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenTestCase;
import walkingkooka.tree.select.NodeSelector;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public abstract class NodeSelectorParserTokenTestCase<T extends NodeSelectorParserToken> extends ParserTokenTestCase<T> {

    @Test
    public final void testPublicStaticFactoryMethod() {
        this.publicStaticFactoryCheck(NodeSelectorParserToken.class, NodeSelector.class.getSimpleName(), ParserToken.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testEmptyTextFails() {
        this.createToken("");
    }

    @Test
    public void testIsMethods() throws Exception {
        final String prefix = NodeSelector.class.getSimpleName();
        final String suffix = ParserToken.class.getSimpleName();

        final T token = this.createToken();
        final String name = token.getClass().getSimpleName();
        assertEquals(name + " starts with " + prefix, true, name.startsWith(prefix));
        assertEquals(name + " ends with " + suffix, true, name.endsWith(suffix));

        final String isMethodName = "is" + CharSequences.capitalize(name.substring(prefix.length(), name.length() - suffix.length()));

        for (Method method : token.getClass().getMethods()) {
            if (MethodAttributes.STATIC.is(method)) {
                continue;
            }
            final String methodName = method.getName();
            if (methodName.equals("isNoise")) {
                continue;
            }
            if (methodName.equals("isSymbol")) {
                continue;
            }

            if (!methodName.startsWith("is")) {
                continue;
            }
            assertEquals(method + " returned",
                    methodName.equals(isMethodName),
                    method.invoke(token));
        }
    }

    // token factories...............................................................................

    final NodeSelectorParserToken absolute() {
        return NodeSelectorParserToken.absolute("/", "/");
    }

    final NodeSelectorParserToken ancestor() {
        return NodeSelectorParserToken.ancestor("ancestor::", "ancestor::");
    }

    final NodeSelectorAndParserToken and(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.and(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken andSymbol() {
        return NodeSelectorParserToken.andSymbol("and", "and");
    }

    final NodeSelectorParserToken atSign() {
        return NodeSelectorParserToken.atSignSymbol("@", "@");
    }

    final NodeSelectorParserToken attributeName() {
        return attributeName("attribute1");
    }

    final NodeSelectorParserToken attributeName2() {
        return attributeName("attribute2");
    }

    final NodeSelectorParserToken attributeName(final String name) {
        return NodeSelectorParserToken.attributeName(NodeSelectorAttributeName.with(name), name);
    }

    final NodeSelectorParserToken bracketOpen() {
        return NodeSelectorParserToken.bracketOpenSymbol("[", "[");
    }

    final NodeSelectorParserToken bracketClose() {
        return NodeSelectorParserToken.bracketCloseSymbol("]", "]");
    }

    final NodeSelectorParserToken child() {
        return NodeSelectorParserToken.child("child::", "child::");
    }

    final NodeSelectorParserToken comma() {
        return NodeSelectorParserToken.parameterSeparatorSymbol(",", ",");
    }

    final NodeSelectorParserToken descendant() {
        return NodeSelectorParserToken.descendant("descendant::", "descendant::");
    }

    final NodeSelectorParserToken descendantSlashSlash() {
        return NodeSelectorParserToken.descendant("//", "//");
    }

    final NodeSelectorEqualsParserToken equalsParserToken(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.equalsParserToken(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken equalsSymbol() {
        return NodeSelectorParserToken.equalsSymbol("=", "=");
    }

    final NodeSelectorExpressionParserToken expression(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.expression(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken firstChild() {
        return NodeSelectorParserToken.firstChild("first-child::", "first-child::");
    }

    final NodeSelectorParserToken following() {
        return NodeSelectorParserToken.following("following::", "following::");
    }

    final NodeSelectorParserToken followingSibling() {
        return NodeSelectorParserToken.followingSibling("following-sibling::", "following-sibling::");
    }

    final NodeSelectorFunctionParserToken function(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.function(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorFunctionParserToken functionWithoutArguments() {
        return function(functionName(), parenthesisOpen(), parenthesisClose());
    }

    final NodeSelectorFunctionParserToken functionWithArguments() {
        return function(functionName(), parenthesisOpen(), number(), comma(), quotedText(), parenthesisClose());
    }

    final NodeSelectorFunctionNameParserToken functionName() {
        return functionName("contains");
    }

    final NodeSelectorFunctionNameParserToken functionName(final String text) {
        return NodeSelectorParserToken.functionName(NodeSelectorFunctionName.with(text), text);
    }

    final NodeSelectorGreaterThanParserToken greaterThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThan(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken greaterThanSymbol() {
        return NodeSelectorParserToken.greaterThanSymbol(">", ">");
    }

    final NodeSelectorGreaterThanEqualsParserToken greaterThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.greaterThanEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken greaterThanEqualsSymbol() {
        return NodeSelectorParserToken.greaterThanEqualsSymbol(">=", ">=");
    }

    final NodeSelectorLessThanParserToken lessThan(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThan(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken lessThanSymbol() {
        return NodeSelectorParserToken.lessThanSymbol("<", "<");
    }

    final NodeSelectorLessThanEqualsParserToken lessThanEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.lessThanEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken lessThanEqualsSymbol() {
        return NodeSelectorParserToken.lessThanEqualsSymbol("<=", "<=");
    }

    final NodeSelectorParserToken lastChild() {
        return NodeSelectorParserToken.lastChild("last-child::", "last-child::");
    }

    final NodeSelectorParserToken nodeName() {
        return nodeName("Node1");
    }

    final NodeSelectorParserToken nodeName2() {
        return nodeName("Node22");
    }

    final NodeSelectorParserToken nodeName(final String name) {
        return NodeSelectorParserToken.nodeName(NodeSelectorNodeName.with(name), name);
    }

    final NodeSelectorNotEqualsParserToken notEquals(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.notEquals(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken notEqualsSymbol() {
        return NodeSelectorParserToken.notEqualsSymbol("!=", "!=");
    }

    final NodeSelectorParserToken number() {
        return number(1);
    }

    final NodeSelectorParserToken number2() {
        return number(23);
    }

    final NodeSelectorParserToken number(final int value) {
        return NodeSelectorParserToken.number(value, String.valueOf(value));
    }

    final NodeSelectorOrParserToken or(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.or(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken orSymbol() {
        return NodeSelectorParserToken.orSymbol("or", "or");
    }

    final NodeSelectorParserToken parenthesisOpen() {
        return NodeSelectorParserToken.parenthesisOpenSymbol("(", "(");
    }

    final NodeSelectorParserToken parenthesisClose() {
        return NodeSelectorParserToken.parenthesisCloseSymbol(")", ")");
    }

    final NodeSelectorParserToken parent() {
        return NodeSelectorParserToken.parentOf("parent::", "parent::");
    }

    final NodeSelectorParserToken parentDotDot() {
        return NodeSelectorParserToken.parentOf("..", "..");
    }

    final NodeSelectorParserToken preceding() {
        return NodeSelectorParserToken.preceding("preceding::", "preceding::");
    }

    final NodeSelectorParserToken precedingSibling() {
        return NodeSelectorParserToken.precedingSibling("preceding-sibling::", "preceding-sibling::");
    }

    final NodeSelectorPredicateParserToken predicate(final NodeSelectorParserToken... tokens) {
        return NodeSelectorParserToken.predicate(Lists.of(tokens), text(tokens));
    }

    final NodeSelectorParserToken quotedText() {
        return quotedText("xyz");
    }

    final NodeSelectorParserToken quotedText2() {
        return quotedText("qrst");
    }

    final NodeSelectorParserToken quotedText(final String value) {
        return NodeSelectorParserToken.quotedText(value, CharSequences.quoteAndEscape(value).toString());
    }

    final NodeSelectorParserToken self() {
        return NodeSelectorParserToken.self("self::", "self::");
    }

    final NodeSelectorParserToken selfDot() {
        return NodeSelectorParserToken.self(".", ".");
    }

    final NodeSelectorParserToken slash() {
        return NodeSelectorParserToken.slashSeparatorSymbol("/", "/");
    }

    final NodeSelectorParserToken whitespace() {
        return NodeSelectorParserToken.whitespace("  ", "  ");
    }

    final NodeSelectorParserToken wildcard() {
        return NodeSelectorParserToken.child("*", "*");
    }

    private static String text(final NodeSelectorParserToken... tokens) {
        return ParserToken.text(Lists.of(tokens));
    }
}
