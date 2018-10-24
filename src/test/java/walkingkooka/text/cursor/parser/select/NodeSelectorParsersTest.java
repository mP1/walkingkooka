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
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.ParserTestCase3;
import walkingkooka.text.cursor.parser.ParserToken;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public final class NodeSelectorParsersTest extends ParserTestCase3<Parser<NodeSelectorParserToken, NodeSelectorParserContext>,
        NodeSelectorParserToken,
        NodeSelectorParserContext> {

    // descendant ...........................................................................................

    @Test
    public void testDescendantSlashSlash() {
        this.parseAndCheck2(descendantSlashSlash());
    }

    // parentDot ...............................................................................................

    @Test
    public void testParentDotDot() {
        this.parseAndCheck2(parentDotDot());
    }

    // selfDot ...............................................................................................

    @Test
    public void testSelfDot() {
        this.parseAndCheck2(selfDot());
    }

    // absolute node................................................................................................

    @Test
    public void testAbsoluteNodeName() {
        this.parseAndCheck2(absolute(), nodeName());
    }

    // absolute axis node .....................................................................................

    @Test
    public void testAbsoluteAncestorNodeNameMissingFails() {
        this.parseThrows2(absolute(), ancestor());
    }

    @Test
    public void testAbsoluteAncestorNodeName() {
        this.parseAndCheck2(absolute(), ancestor(), nodeName());
    }

    @Test
    public void testAbsoluteChildNodeName() {
        this.parseAndCheck2(absolute(), child(), nodeName());
    }

    @Test
    public void testAbsoluteDescendantNodeName() {
        this.parseAndCheck2(absolute(), descendant(), nodeName());
    }

    @Test
    public void testAbsoluteFirstChildNodeName() {
        this.parseAndCheck2(absolute(), firstChild(), nodeName());
    }

    @Test
    public void testAbsoluteFollowingNodeName() {
        this.parseAndCheck2(absolute(), following(), nodeName());
    }

    @Test
    public void testAbsoluteFollowingSiblingNodeName() {
        this.parseAndCheck2(absolute(), followingSibling(), nodeName());
    }

    @Test
    public void testAbsoluteLastChildNodeName() {
        this.parseAndCheck2(absolute(), lastChild(), nodeName());
    }

    @Test
    public void testAbsolutePrecedingNodeName() {
        this.parseAndCheck2(absolute(), preceding(), nodeName());
    }

    @Test
    public void testAbsolutePrecedingSiblingNodeName() {
        this.parseAndCheck2(absolute(), precedingSibling(), nodeName());
    }

    @Test
    public void testAbsoluteParentNodeName() {
        this.parseAndCheck2(absolute(), parent(), nodeName());
    }

    @Test
    public void testAbsoluteSelfNodeName() {
        this.parseAndCheck2(absolute(), self(), nodeName());
    }

    // absolute nodeName predicate child index ..........................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenIndexBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), number(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(), number(), whitespace(), bracketClose()));
    }

    // absolute nodeName predicate function.....................................................................

    // /nodeName [
    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), whitespace(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(), function(
                        functionName(), parenthesisOpen(), whitespace(), parenthesisClose()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), number(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), number(), comma(), number2(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), number(), comma(), number2(), comma(), number(3), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(),
                                function(functionName("f2"), parenthesisOpen(), quotedText(), parenthesisClose()),
                                parenthesisClose()),
                        bracketClose()));
    }

    // absolute nodeName predicate condition.....................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        equalsParserToken(number(), equalsSymbol(), number2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        equalsParserToken(quotedText(), equalsSymbol(), quotedText2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionEqualsFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        equalsParserToken(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                equalsSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        equalsParserToken(atSign(), attributeName(), equalsSymbol(), atSign(), attributeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        equalsParserToken(atSign(), attributeName(), equalsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameEqualsNodeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        equalsParserToken(nodeName(), equalsSymbol(), nodeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        equalsParserToken(nodeName(), equalsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        equalsParserToken(number(), whitespace(), equalsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThan(number(), greaterThanSymbol(), number2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextGreaterThanQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThan(quotedText(), greaterThanSymbol(), quotedText2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionGreaterThanFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThan(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                greaterThanSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameGreaterThanAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThan(atSign(), attributeName(), greaterThanSymbol(), atSign(), attributeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameGreaterThanQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThan(atSign(), attributeName(), greaterThanSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameGreaterThanNodeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThan(nodeName(), greaterThanSymbol(), nodeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameGreaterThanQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThan(nodeName(), greaterThanSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceGreaterThanWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        greaterThan(number(), whitespace(), greaterThanSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberGreaterThanEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThanEquals(number(), greaterThanEqualsSymbol(), number2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextGreaterThanEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThanEquals(quotedText(), greaterThanEqualsSymbol(), quotedText2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionGreaterThanEqualsFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThanEquals(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                greaterThanEqualsSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameGreaterThanEqualsAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThanEquals(atSign(), attributeName(), greaterThanEqualsSymbol(), atSign(), attributeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameGreaterThanEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThanEquals(atSign(), attributeName(), greaterThanEqualsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameGreaterThanEqualsNodeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThanEquals(nodeName(), greaterThanEqualsSymbol(), nodeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameGreaterThanEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        greaterThanEquals(nodeName(), greaterThanEqualsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceGreaterThanEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        greaterThanEquals(number(), whitespace(), greaterThanEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThan(number(), lessThanSymbol(), number2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextLessThanQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThan(quotedText(), lessThanSymbol(), quotedText2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionLessThanFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThan(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                lessThanSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameLessThanAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThan(atSign(), attributeName(), lessThanSymbol(), atSign(), attributeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameLessThanQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThan(atSign(), attributeName(), lessThanSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameLessThanNodeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThan(nodeName(), lessThanSymbol(), nodeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameLessThanQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThan(nodeName(), lessThanSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceLessThanWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        lessThan(number(), whitespace(), lessThanSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberLessThanEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThanEquals(number(), lessThanEqualsSymbol(), number2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextLessThanEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThanEquals(quotedText(), lessThanEqualsSymbol(), quotedText2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionLessThanEqualsFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThanEquals(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                lessThanEqualsSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameLessThanEqualsAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThanEquals(atSign(), attributeName(), lessThanEqualsSymbol(), atSign(), attributeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameLessThanEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThanEquals(atSign(), attributeName(), lessThanEqualsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameLessThanEqualsNodeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThanEquals(nodeName(), lessThanEqualsSymbol(), nodeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameLessThanEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        lessThanEquals(nodeName(), lessThanEqualsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceLessThanEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        lessThanEquals(number(), whitespace(), lessThanEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNumberNotEqualsNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        notEquals(number(), notEqualsSymbol(), number2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenQuotedTextNotEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        notEquals(quotedText(), notEqualsSymbol(), quotedText2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameNotEqualsAttributeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        notEquals(atSign(), attributeName(), notEqualsSymbol(), atSign(), attributeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenAttributeNameNotEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        notEquals(atSign(), attributeName(), notEqualsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameNotEqualsNodeNameBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        notEquals(nodeName(), notEqualsSymbol(), nodeName2()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenNodeNameNotEqualsQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        notEquals(nodeName(), notEqualsSymbol(), quotedText()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenFunctionNotEqualsFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(),
                        notEquals(
                                function(functionName(), parenthesisOpen(), parenthesisClose()),
                                notEqualsSymbol(),
                                function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose())),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceNotEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        notEquals(number(), whitespace(), notEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    // absolute node name, predicate, predicate .......................................................................

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketCloseBracketOpenWhitespaceNumberWhitespaceEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        equalsParserToken(number(), whitespace(), equalsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()),
                predicate(bracketOpen(), whitespace(),
                        equalsParserToken(number(), whitespace(), equalsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceGreaterThanWhitespaceNumberWhitespaceBracketCloseBracketOpenWhitespaceNumberWhitespaceGreaterThanWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        greaterThan(number(), whitespace(), greaterThanSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()),
                predicate(bracketOpen(), whitespace(),
                        greaterThan(number(), whitespace(), greaterThanSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceGreaterThanEqualsWhitespaceNumberWhitespaceBracketCloseBracketOpenWhitespaceNumberWhitespaceGreaterThanEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        greaterThanEquals(number(), whitespace(), greaterThanEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()),
                predicate(bracketOpen(), whitespace(),
                        greaterThanEquals(number(), whitespace(), greaterThanEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceLessThanWhitespaceNumberWhitespaceBracketCloseBracketOpenWhitespaceNumberWhitespaceLessThanWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        lessThan(number(), whitespace(), lessThanSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()),
                predicate(bracketOpen(), whitespace(),
                        lessThan(number(), whitespace(), lessThanSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceLessThanEqualsWhitespaceNumberWhitespaceBracketCloseBracketOpenWhitespaceNumberWhitespaceLessThanEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        lessThanEquals(number(), whitespace(), lessThanEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()),
                predicate(bracketOpen(), whitespace(),
                        lessThanEquals(number(), whitespace(), lessThanEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteNodeNameBracketOpenWhitespaceNumberWhitespaceNotEqualsWhitespaceNumberWhitespaceBracketCloseBracketOpenWhitespaceNumberWhitespaceNotEqualsWhitespaceNumberWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                nodeName(),
                predicate(bracketOpen(), whitespace(),
                        notEquals(number(), whitespace(), notEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()),
                predicate(bracketOpen(), whitespace(),
                        notEquals(number(), whitespace(), notEqualsSymbol(), whitespace(), number2()),
                        whitespace(), bracketClose()));
    }

    // absolute wildcard................................................................................................

    @Test
    public void testAbsoluteWildcard() {
        this.parseAndCheck2(absolute(), wildcard());
    }

    // absolute axis wildcard....................................................................................

    @Test
    public void testAbsoluteAncestorWildcardFails() {
        this.parseThrows2(absolute(), ancestor());
    }

    @Test
    public void testAbsoluteAncestorWildcard() {
        this.parseAndCheck2(absolute(), ancestor(), wildcard());
    }

    @Test
    public void testAbsoluteChildWildcard() {
        this.parseAndCheck2(absolute(), child(), wildcard());
    }

    @Test
    public void testAbsoluteDescendantWildcard() {
        this.parseAndCheck2(absolute(), descendant(), wildcard());
    }

    @Test
    public void testAbsoluteFirstChildWildcard() {
        this.parseAndCheck2(absolute(), firstChild(), wildcard());
    }

    @Test
    public void testAbsoluteFollowingWildcard() {
        this.parseAndCheck2(absolute(), following(), wildcard());
    }

    @Test
    public void testAbsoluteFollowingSiblingWildcard() {
        this.parseAndCheck2(absolute(), followingSibling(), wildcard());
    }

    @Test
    public void testAbsoluteLastChildWildcard() {
        this.parseAndCheck2(absolute(), lastChild(), wildcard());
    }

    @Test
    public void testAbsolutePrecedingWildcard() {
        this.parseAndCheck2(absolute(), preceding(), wildcard());
    }

    @Test
    public void testAbsolutePrecedingSiblingWildcard() {
        this.parseAndCheck2(absolute(), precedingSibling(), wildcard());
    }

    @Test
    public void testAbsoluteParentWildcard() {
        this.parseAndCheck2(absolute(), parent(), wildcard());
    }

    @Test
    public void testAbsoluteSelfWildcard() {
        this.parseAndCheck2(absolute(), self(), wildcard());
    }

    // absolute wildcard predicate child index ..........................................................................

    @Test
    public void testIndexMissingNumberFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen()));
    }

    @Test
    public void testIndexMissingBracketCloseFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen(), number()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenIndexBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(), number(), bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceIndexWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(), whitespace(), number(), whitespace(), bracketClose()));
    }

    // absolute wildcard predicate function.....................................................................

    // /wildcard [
    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenWhitespaceParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), whitespace(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionNameParenOpenWhitespaceParenCloseWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(), whitespace(), function(
                        functionName(), parenthesisOpen(), whitespace(), parenthesisClose()),
                        whitespace(), bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), number(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), number(), comma(), number2(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenNumberNumberNumberParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), number(), comma(), number2(), comma(), number(3), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpenQuotedTextParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(), quotedText(), parenthesisClose()),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionNameParenOpen_functionNameParenOpenQuotedTextParenClose_ParenCloseBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        function(functionName(), parenthesisOpen(),
                                function(functionName("f2"), parenthesisOpen(), quotedText(), parenthesisClose()),
                                parenthesisClose()),
                        bracketClose()));
    }

    // and ....................................................................................................

    @Test
    public void testAndMissingRightFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        andSymbol()));
    }

    @Test
    public void testAndMissingRightFails2() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        andSymbol(),
                        bracketClose()));
    }

    @Test
    public void testAndMissingBracketCloseFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        andSymbol()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberAndNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        andSymbol(),
                        number2(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextAndQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        andSymbol(),
                        number2(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        functionWithoutArguments(),
                        andSymbol(),
                        whitespace(),
                        functionWithArguments(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndWhitespaceNumberWhitespaceAndWhitespaceQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        functionWithoutArguments(),
                        whitespace(),
                        andSymbol(),
                        whitespace(),
                        number(),
                        whitespace(),
                        andSymbol(),
                        whitespace(),
                        quotedText(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceAndWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        whitespace(),
                        functionWithoutArguments(),
                        whitespace(),
                        andSymbol(),
                        whitespace(),
                        functionWithArguments(),
                        whitespace(),
                        bracketClose()));
    }

    // or ....................................................................................................

    @Test
    public void testOrMissingRightFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        orSymbol()));
    }

    @Test
    public void testOrMissingRightFails2() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        orSymbol(),
                        bracketClose()));
    }

    @Test
    public void testOrMissingBracketCloseFails() {
        this.parseThrows2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        orSymbol(),
                        number()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenNumberOrNumberBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        orSymbol(),
                        number2(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenQuotedTextOrQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        number(),
                        orSymbol(),
                        number2(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrWhitespaceFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        functionWithoutArguments(),
                        orSymbol(),
                        whitespace(),
                        functionWithArguments(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionOrNumberOrQuotedTextBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        functionWithoutArguments(),
                        orSymbol(),
                        whitespace(),
                        number(),
                        orSymbol(),
                        quotedText(),
                        bracketClose()));
    }

    @Test
    public void testAbsoluteWildcardBracketOpenWhitespaceFunctionWhitespaceOrWhitespaceFunctionWhitespaceBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        whitespace(),
                        functionWithoutArguments(),
                        whitespace(),
                        orSymbol(),
                        whitespace(),
                        functionWithArguments(),
                        whitespace(),
                        bracketClose()));
    }

    // and or...............................................................................................

    @Test
    public void testAbsoluteWildcardBracketOpenFunctionAndFunctionOrFunctionBracketClose() {
        this.parseAndCheck2(absolute(),
                wildcard(),
                predicate(bracketOpen(),
                        functionWithoutArguments(),
                        andSymbol(),
                        functionWithArguments(),
                        orSymbol(),
                        functionWithoutArguments(),
                        whitespace(),
                        bracketClose()));
    }

    // relative axis node .....................................................................................

    @Test
    public void testRelativeAncestorNodeNameMissingFails() {
        this.parseThrows2(ancestor());
    }

    @Test
    public void testRelativeAncestorNodeName() {
        this.parseAndCheck2(ancestor(), nodeName());
    }

    @Test
    public void testRelativeChildNodeName() {
        this.parseAndCheck2(child(), nodeName());
    }

    @Test
    public void testRelativeDescendantNodeName() {
        this.parseAndCheck2(descendant(), nodeName());
    }

    @Test
    public void testRelativeFirstChildNodeName() {
        this.parseAndCheck2(firstChild(), nodeName());
    }

    @Test
    public void testRelativeFollowingNodeName() {
        this.parseAndCheck2(following(), nodeName());
    }

    @Test
    public void testRelativeFollowingSiblingNodeName() {
        this.parseAndCheck2(followingSibling(), nodeName());
    }

    @Test
    public void testRelativeLastChildNodeName() {
        this.parseAndCheck2(lastChild(), nodeName());
    }

    @Test
    public void testRelativePrecedingNodeName() {
        this.parseAndCheck2(preceding(), nodeName());
    }

    @Test
    public void testRelativePrecedingSiblingNodeName() {
        this.parseAndCheck2(precedingSibling(), nodeName());
    }

    @Test
    public void testRelativeParentNodeName() {
        this.parseAndCheck2(parent(), nodeName());
    }

    @Test
    public void testRelativeSelfNodeName() {
        this.parseAndCheck2(self(), nodeName());
    }

// relative axis wildcard....................................................................................

    @Test
    public void testRelativeAncestorWildcardFails() {
        this.parseThrows2(ancestor());
    }

    @Test
    public void testRelativeAncestorWildcard() {
        this.parseAndCheck2(ancestor(), wildcard());
    }

    @Test
    public void testRelativeChildWildcard() {
        this.parseAndCheck2(child(), wildcard());
    }

    @Test
    public void testRelativeDescendantWildcard() {
        this.parseAndCheck2(descendant(), wildcard());
    }

    @Test
    public void testRelativeFirstChildWildcard() {
        this.parseAndCheck2(firstChild(), wildcard());
    }

    @Test
    public void testRelativeFollowingWildcard() {
        this.parseAndCheck2(following(), wildcard());
    }

    @Test
    public void testRelativeFollowingSiblingWildcard() {
        this.parseAndCheck2(followingSibling(), wildcard());
    }

    @Test
    public void testRelativeLastChildWildcard() {
        this.parseAndCheck2(lastChild(), wildcard());
    }

    @Test
    public void testRelativePrecedingWildcard() {
        this.parseAndCheck2(preceding(), wildcard());
    }

    @Test
    public void testRelativePrecedingSiblingWildcard() {
        this.parseAndCheck2(precedingSibling(), wildcard());
    }

    @Test
    public void testRelativeParentWildcard() {
        this.parseAndCheck2(parent(), wildcard());
    }

    @Test
    public void testRelativeSelfWildcard() {
        this.parseAndCheck2(self(), wildcard());
    }


    // helpers....................................................................................................

    @Override
    protected Parser<NodeSelectorParserToken, NodeSelectorParserContext> createParser() {
        return NodeSelectorParsers.expression()
                .orReport(ParserReporters.basic())
                .cast();
    }

    @Override
    protected NodeSelectorParserContext createContext() {
        return NodeSelectorParserContexts.basic();
    }

    // helpers................................................................................................

    private void parseAndCheck2(final NodeSelectorParserToken... tokens) {
        final List<ParserToken> list = Lists.of(tokens);
        final String text = ParserToken.text(list);

        assertEquals("text should be all upper case", text, text);

        final Parser<NodeSelectorParserToken, NodeSelectorParserContext> parser = this.createParser();
        this.parseAndCheck(parser,
                text,
                NodeSelectorParserToken.expression(list, text),
                text);

        final List<ParserToken> lower = Arrays.stream(tokens)
                .map(t -> NodeSelectorParsersTestNodeSelectorParserTokenVisitor.toUpper(t))
                .collect(Collectors.toList());
        final String textUpper = ParserToken.text(lower);

        this.parseAndCheck(parser,
                textUpper,
                NodeSelectorParserToken.expression(lower, textUpper),
                textUpper);
    }

    private void parseThrows2(final NodeSelectorParserToken... tokens) {
        this.parseThrows(this.createParser().orFailIfCursorNotEmpty(ParserReporters.basic()),
                ParserToken.text(Lists.of(tokens)));
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

    final NodeSelectorParserToken functionName() {
        return functionName("contains");
    }

    final NodeSelectorParserToken functionName(final String text) {
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

    @Override
    protected String toString(final ParserToken token) {
        return NodeSelectorParserPrettyNodeSelectorParserTokenVisitor.toString(token);
    }
}
