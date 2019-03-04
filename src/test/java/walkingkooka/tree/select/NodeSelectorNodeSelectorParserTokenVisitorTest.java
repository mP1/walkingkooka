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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.ConversionException;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.ParserReporters;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.select.NodeSelectorExpressionParserToken;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserContexts;
import walkingkooka.text.cursor.parser.select.NodeSelectorParserTokenVisitorTesting;
import walkingkooka.text.cursor.parser.select.NodeSelectorParsers;
import walkingkooka.tree.TestNode;
import walkingkooka.tree.expression.ExpressionNodeName;
import walkingkooka.tree.expression.function.ExpressionFunctionContext;
import walkingkooka.tree.expression.function.FakeExpressionFunctionContext;
import walkingkooka.type.MemberVisibility;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class NodeSelectorNodeSelectorParserTokenVisitorTest implements NodeSelectorParserTokenVisitorTesting<NodeSelectorNodeSelectorParserTokenVisitor<TestNode, StringName, StringName, Object>> {

    @BeforeEach
    public void beforeEachTest() {
        TestNode.clear();
    }

    @Test
    public void testAbsoluteNodeName() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionAndCheck("/root", root, root);
    }

    @Test
    public void testAbsoluteWildcard() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionAndCheck("/*", root, root);
        this.parseExpressionAndCheck("/*", root.child(0), root);
    }

    @Test
    public void testAbsoluteChildAxisWildcard() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionAndCheck("/child::*", root, branch);
    }

    @Test
    public void testAbsoluteDescendantOrSelfWildcard() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionAndCheck("//*", root, root, branch, leaf);
        this.parseExpressionAndCheck("//*", root.child(0).child(0), leaf);
    }

    @Test
    public void testDescendantOrSelfNodeName() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("//leaf1", root, leaf1);
        this.parseExpressionAndCheck("//leaf1", root.child(1));
    }

    @Test
    public void testDescendantOrSelfWildcard() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("//*", root, root, branch1, leaf1, branch2, leaf2, branch3, leaf3);
        this.parseExpressionAndCheck("//*", root.child(1), branch2, leaf2);
    }

    @Test
    public void testDescendantOfSelfNodeNameWildcard() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("//root/*", root, branch1, branch2, branch3);
        this.parseExpressionAndCheck("//branch1/*", root.child(0), leaf1);
    }

    @Test
    public void testAbsoluteNodeNameDescendantOrSelfWildcard() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branchOfBranch3 = node("branchOfBranch3", leaf3);
        final TestNode branch3 = node("branch3", branchOfBranch3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("/branch2//*", root.child(1));
        this.parseExpressionAndCheck("/root/branch3//*", root.child(2), branch3, branchOfBranch3, leaf3);
    }

    @Test
    public void testRelativeNodeNameDescendantOrSelfWildcard() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branchOfBranch3 = node("branchOfBranch3", leaf3);
        final TestNode branch3 = node("branch3", branchOfBranch3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("branch2//*", root);
        this.parseExpressionAndCheck("branch3//*", root.child(2), branch3, branchOfBranch3, leaf3);
    }

    @Test
    public void testAbsoluteNodeNameNodeName() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("/root/branch2/*", root.child(1), leaf2);
        this.parseExpressionAndCheck("//root/wrong/*", root);
    }

    @Test
    public void testRelativeNodeNameNodeName() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("branch2/leaf2", root);
        this.parseExpressionAndCheck("root/branch3", root.child(2));
        this.parseExpressionAndCheck("root/branch2", root, branch2);
    }

    @Test
    public void testAbsoluteNodeNameWildcard() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("/root/branch2/*", root.child(1), leaf2);
        this.parseExpressionAndCheck("/root/branch3/*", root.child(2), leaf3);
    }

    @Test
    public void testRelativeNodeNameWildcard() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3 = node("leaf3");
        final TestNode branch3 = node("branch3", leaf3);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("branch2/leaf2", root);
        this.parseExpressionAndCheck("branch3/wrong", root.child(2));
        this.parseExpressionAndCheck("branch3/leaf3", root.child(2), leaf3);
    }

    // predicate EQ ...................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsString() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id=\"attribute-value-1\"]", root, leaf1);
        this.parseExpressionAndCheck("//*[@id=\"attribute-value-2\"]", root, leaf2);
        this.parseExpressionAndCheck("//*[@unknown=\"*\"]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsStringMissing() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id=\"attribute-value-1\"]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsNumber() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id=1]", root, leaf1);
        this.parseExpressionAndCheck("//*[@id=2]", root, leaf2);
        this.parseExpressionAndCheck("//*[@id=999]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEqualsNumberConversion() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionAndCheck("//*[@id=1]", root, leaf1);
        this.parseExpressionAndCheck("//*[@id=2]", root, leaf2);
    }

    // predicate GT ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanString() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id>\"a\"]", root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id>\"attribute-value-1\"]", root, leaf2);
        this.parseExpressionAndCheck("//*[@unknown>\"z\"]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanStringMissing() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id>\"a\"]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanNumber() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id>0]", root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id>1]", root, leaf2);
        this.parseExpressionAndCheck("//*[@id>999]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanNumberConversion() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionAndCheck("//*[@id>0]", root, leaf1, leaf2, leaf3);
        this.parseExpressionAndCheck("//*[@id>1]", root, leaf2, leaf3);
        this.parseExpressionAndCheck("//*[@id>999]", root, leaf3);
    }

    // predicate GTE ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsString() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id>=\"attribute-value-1\"]", root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id>=\"attribute-value-2\"]", root, leaf2);
        this.parseExpressionAndCheck("//*[@unknown>=\"z\"]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsStringMissing() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id>=\"a\"]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsNumber() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id>=0]", root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id>=2]", root, leaf2);
        this.parseExpressionAndCheck("//*[@id>=999]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueGreaterThanEqualsNumberConversion() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        //assertTrue("0".compareTo("NAN") >= 0);
        this.parseExpressionAndCheck("//*[@id>=0]", root, leaf1, leaf2, leaf3);
        this.parseExpressionAndCheck("//*[@id>=2]", root, leaf2, leaf3);
        this.parseExpressionAndCheck("//*[@id>=999]", root, leaf3);
    }

    // predicate LT ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanString() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id<\"attribute-value-2\"]", root, root, leaf1);
        this.parseExpressionAndCheck("//*[@id<\"attribute-value-1\"]", root, root);
        this.parseExpressionAndCheck("//*[@unknown<\".\"]", root, root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanStringMissing() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id<\"z\"]", root, root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanNumber() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id<3]", root, root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id<2]", root, root, leaf1);
        this.parseExpressionAndCheck("//*[@id<-1]", root, root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanNumberConversion() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionAndCheck("//*[@id<7]", root, root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id<2]", root, root, leaf1);
        this.parseExpressionAndCheck("//*[@id<1]", root, root);
        this.parseExpressionAndCheck("//*[@id<-999]", root, root);
    }

    // predicate LTE ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsString() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id<=\"attribute-value-2\"]", root, root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id<=\"attribute-value-1\"]", root, root, leaf1);
        this.parseExpressionAndCheck("//*[@unknown<=\".\"]", root, root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsStringMissing() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id<=\"z\"]", root, root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsNumber() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id<=2]", root, root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id<=1]", root, root, leaf1);
        this.parseExpressionAndCheck("//*[@id<=-1]", root, root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueLessThanEqualsNumberConversion() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionAndCheck("//*[@id<=77]", root, root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id<=3]", root, root, leaf1, leaf2);
        this.parseExpressionAndCheck("//*[@id<=1]", root, root, leaf1);
        this.parseExpressionAndCheck("//*[@id<=-999]", root, root);
    }

    // predicate NE ...............................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsString() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2", "attribute-value-2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id!=\"attribute-value-1\"]", root, root, leaf2);
        this.parseExpressionAndCheck("//*[@id!=\"attribute-value-2\"]", root, root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsStringMissing() {
        final TestNode leaf1 = node("leaf1", "attribute-value-1");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id!=\"attribute-value-1\"]", root, root, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsNumber() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 2);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id!=1]", root, root, leaf2);
        this.parseExpressionAndCheck("//*[@id!=2]", root, root, leaf1);
        this.parseExpressionAndCheck("//*[@id!=999]", root, root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueNotEqualsNumberConversion() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", "2");
        final TestNode leaf3 = node("leaf3", "NAN");

        final TestNode root = node("root", leaf1, leaf2, leaf3);

        this.parseExpressionAndCheck("//*[@id!=1]", root, root, leaf2, leaf3);
        this.parseExpressionAndCheck("//*[@id!=2]", root, root, leaf1, leaf3);
    }

    // concat ....................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueConcat() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "x1");

        final TestNode root = node("root", "zzz", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id=concat(\"a\", \"b\", \"c\")]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueConcatMissingAttribute() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id=concat(@missing, \"abc\")]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueConcatInteger() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "1x2");

        final TestNode root = node("root", "zzzzz", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id=concat(1, \"x\", 2)]", root, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueConcatInteger2() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", 123);

        final TestNode root = node("root", 999, leaf1, leaf2);

        this.parseExpressionAndCheck("//*[@id=concat(\"123\")]", root, leaf2);
    }

    // ends with ....................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueEndsWith() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "x1");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[ends-with(@id, \"c\")]", root, leaf1);
        this.parseExpressionAndCheck("//*[ends-with(@id, \"1\")]", root, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEndsWithMissingAttribute() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[ends-with(@id, \"c\")]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEndsWithIntegerValue() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "x1");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[ends-with(@id, 1)]", root, leaf2);
        this.parseExpressionAndCheck("//*[ends-with(@id, 9)]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueEndsWithIntegerValue2() {
        final TestNode leaf1 = node("leaf1", 123);
        final TestNode leaf2 = node("leaf2", 456);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[ends-with(@id, \"3\")]", root, leaf1);
        this.parseExpressionAndCheck("//*[ends-with(@id, \"56\")]", root, leaf2);
    }

    // starts with ....................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWith() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "1x");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[starts-with(@id, \"a\")]", root, leaf1);
        this.parseExpressionAndCheck("//*[starts-with(@id, \"1\")]", root, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWithMissingAttribute() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[starts-with(@id, \"a\")]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWithIntegerValue() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "1a");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[starts-with(@id, 1)]", root, leaf2);
        this.parseExpressionAndCheck("//*[starts-with(@id, 9)]", root);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStartsWithIntegerValue2() {
        final TestNode leaf1 = node("leaf1", 345);
        final TestNode leaf2 = node("leaf2", 567);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[starts-with(@id, \"3\")]", root, leaf1);
        this.parseExpressionAndCheck("//*[starts-with(@id, \"56\")]", root, leaf2);
    }

    // string-length ....................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueStringLength() {
        final TestNode leaf1 = node("leaf1", "a");
        final TestNode leaf2 = node("leaf2", "zz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=1]", root, leaf1);
        this.parseExpressionAndCheck("//*[string-length(@id)=2]", root, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStringLengthMissingAttribute() {
        final TestNode leaf1 = node("leaf1", "a");
        final TestNode leaf2 = node("leaf2");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=0]", root, root, leaf2);
        this.parseExpressionAndCheck("//*[string-length(@id)=1]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStringLengthIntegerValue() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 23);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=1]", root, leaf1);
        this.parseExpressionAndCheck("//*[string-length(@id)=2]", root, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameAttributeValueStringLengthIntegerValue2() {
        final TestNode leaf1 = node("leaf1", 1);
        final TestNode leaf2 = node("leaf2", 23);

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=1]", root, leaf1);
        this.parseExpressionAndCheck("//*[string-length(@id)=2]", root, leaf2);
    }

    // multiple predicates ........................................................................................................

    @Test
    public void testAbsoluteNodeNameAttributeValueStringTwice() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "x");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=3][@id=\"abc\"]", root, leaf1);
        this.parseExpressionAndCheck("//*[@id=\"abc\"][string-length(@id)=3]", root, leaf1);
    }

    // ancestor.......................................................................................

    @Test
    public void testAncestor() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("ancestor::*", root.child(0), root);
    }

    // ancestor-or-self.......................................................................................

    @Test
    public void testAncestorOrSelf() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("ancestor-or-self::*", root.child(0), root, branch1);
    }

    // child.......................................................................................

    @Test
    public void testChild() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("/child::*", root, branch1, branch2);
        this.parseExpressionAndCheck("/root/branch1/child::*", root.child(0), leaf1);
    }

    // descendant.......................................................................................

    @Test
    public void testDescendant() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("descendant::*", root.child(0), leaf1);
    }

    // descendant-or-self.......................................................................................

    @Test
    public void testDescendantOrSelf() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("descendant-or-self::*", root.child(0), branch1, leaf1);
    }


    // first-child.......................................................................................

    @Test
    public void testFirstChild() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("//first-child::*", root, branch1, leaf1, leaf2);
        this.parseExpressionAndCheck("first-child::*", root.child(0), leaf1);
    }

    // following...........................................................................................

    @Test
    public void testFollowing() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("following::*", root, branch1, leaf1, branch2, leaf2);
        this.parseExpressionAndCheck("following::*", root.child(0), leaf1, branch2, leaf2);
    }

    // following-sibling...........................................................................................

    @Test
    public void testFollowingSibling() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("following-sibling::*", root.child(0), branch2);
        this.parseExpressionAndCheck("following-sibling::*", root.child(1));
    }

    // indexed...........................................................................................

    @Test
    public void testAbsoluteWildcardIndex() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("/*[1]", root, branch1);
        this.parseExpressionAndCheck("/*[2]", root, branch2);
    }

    @Test
    public void testRelativeWildcardIndex() {
        final TestNode branch1 = node("branch1");
        final TestNode branch2 = node("branch2");
        final TestNode branch3 = node("branch3");

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("*[0]", root);
        this.parseExpressionAndCheck("*[1]", root, branch1);
        this.parseExpressionAndCheck("*[3]", root, branch3);
        this.parseExpressionAndCheck("*[4]", root);
    }

    // last-child.......................................................................................

    @Test
    public void testLastChild() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("//last-child::*", root, leaf1, branch2, leaf2);
        this.parseExpressionAndCheck("last-child::*", root.child(0), leaf1);
    }

    // parent.......................................................................................

    @Test
    public void testParentDotDot() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("..", root.child(0), root);
    }

    @Test
    public void testParentAxis() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("parent::*", root.child(0), root);
    }

    // preceding...........................................................................................

    @Test
    public void testPreceding() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("preceding::*", root.child(0), leaf1);
        this.parseExpressionAndCheck("preceding::*", root.child(1), leaf2, branch1, leaf1);
    }

    // preceding-sibling...........................................................................................

    @Test
    public void testPrecedingSibling() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("preceding-sibling::*", root.child(1), branch1);
        this.parseExpressionAndCheck("preceding-sibling::*", root.child(0));
    }

    // self.......................................................................................

    @Test
    public void testSelfAxis() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("self::*", root, root);
        this.parseExpressionAndCheck("self::*", root.child(0), branch1);
    }

    @Test
    public void testSelfDot() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck(".", root, root);
        this.parseExpressionAndCheck(".", root.child(0), branch1);
    }

    // function: boolean().......................................................................................

    @Test
    public void testBoolean() {
        final TestNode branch = node("branch");
        final TestNode root = node("root", branch);

        this.parseExpressionAndCheck("//*[boolean(starts-with(name(), \"b\"))]", root, branch);
    }

    // function: name.......................................................................................

    @Test
    public void testCurrentTestName() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode root = node("root", branch1, branch2);

        this.parseExpressionAndCheck("//*[name()=\"leaf1\"]", root, leaf1);
        this.parseExpressionAndCheck("//*[name()=\"branch2\"]", root, branch2);
        this.parseExpressionAndCheck("//*[starts-with(name(), \"leaf\")]", root, leaf1, leaf2);
    }

    // function: number().......................................................................................

    @Test
    public void testNumber() {
        final TestNode leaf = node("leaf", 789);
        final TestNode branch = node("branch", "123", leaf);
        final TestNode root = node("root", "456", branch);

        this.parseExpressionAndCheck("//*[number(@id) > 300]", root, leaf);
    }

    // function: position().......................................................................................

    @Test
    public void testPosition() {
        final TestNode leaf1 = node("leaf1");
        final TestNode branch1 = node("branch1", leaf1);

        final TestNode leaf2 = node("leaf2");
        final TestNode branch2 = node("branch2", leaf2);

        final TestNode leaf3a = node("leaf3a");
        final TestNode leaf3b = node("leaf3b");
        final TestNode branch3 = node("branch3", leaf3a, leaf3b);

        final TestNode root = node("root", branch1, branch2, branch3);

        this.parseExpressionAndCheck("//*[position() = 2]", root, branch2, leaf3b);
        this.parseExpressionAndCheck("//*[position() = 3]", root, branch3);
    }
    
    // function: text().......................................................................................

    @Test
    public void testText() {
        final TestNode branch = node("branch", 123);
        final TestNode root = node("root", 456, branch);

        this.parseExpressionAndCheck("//*[starts-with(text(@id), \"1\")]", root, branch);
    }

    // function: true().......................................................................................

    @Test
    public void testTrue() {
        final TestNode branch = node("branch");
        final TestNode root = node("root", branch);

        this.parseExpressionAndCheck("//*[starts-with(name(), \"b\")=true()]", root, branch);
    }

    // function: false().......................................................................................

    @Test
    public void testFalse() {
        final TestNode leaf = node("leaf");
        final TestNode branch = node("branch", leaf);
        final TestNode root = node("root", branch);

        this.parseExpressionAndCheck("//*[starts-with(name(), \"r\")=false()]", root, branch, leaf);
    }

    // and ........................................................................................................

    @Test
    public void testAbsoluteNodeNameTrueAndTrue() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[true() AND true()]", root, root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameFunctionAndFunction() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=3 AND @id=\"abc\"]", root, leaf1);
    }

    // or ........................................................................................................

    @Test
    public void testAbsoluteNodeNameTrueOrTrue() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[false() OR true()]", root, root, leaf1, leaf2);
    }

    @Test
    public void testAbsoluteNodeNameFunctionOrFunction() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=4 OR @id=\"abc\"]", root, leaf1);
    }

    @Test
    public void testAbsoluteNodeNameFunctionOrFunction2() {
        final TestNode leaf1 = node("leaf1", "abc");
        final TestNode leaf2 = node("leaf2", "xyz");

        final TestNode root = node("root", leaf1, leaf2);

        this.parseExpressionAndCheck("//*[string-length(@id)=3 OR @id=\"abc\"]", root, leaf1, leaf2);
    }

    // helpers ..................................................................................................

    private TestNode node(final String name, final TestNode... nodes) {
        return TestNode.with(name, nodes);
    }

    private TestNode node(final String name, final String idAttributeValue, final TestNode... nodes) {
        return TestNode.with(name, nodes).setAttributes(Maps.one(Names.string("id"), idAttributeValue));
    }

    private TestNode node(final String name, final Number idAttributeValue, final TestNode... nodes) {
        return TestNode.with(name, nodes).setAttributes(Maps.one(Names.string("id"), idAttributeValue));
    }

    private void parseExpressionAndCheck(final String expression, final TestNode root) {
        this.parseExpressionAndCheck(expression, root, Sets.empty());
    }

    private void parseExpressionAndCheck(final String expression, final TestNode root, final TestNode... expected) {
        this.parseExpressionAndCheck(expression, root, names(Lists.of(expected)));
    }

    private void parseExpressionAndCheck(final String expression, final TestNode root, final Set<String> expected) {
        final NodeSelector<TestNode, StringName, StringName, Object> selector = this.parseExpression(expression);

        final Set<TestNode> selected = Sets.ordered();
        selector.accept(root, new FakeNodeSelectorContext<TestNode, StringName, StringName, Object>(){
            @Override
            public void potential(final TestNode node) {
                this.node = node;
            }

            private TestNode node;

            @Override
            public void selected(final TestNode node) {
                selected.add(node);
            }

            @Override
            public Object function(final ExpressionNodeName name, final List<Object> parameters) {
                assertNotNull(this.node, "node missing");

                final List<Object> thisAndParameters = Lists.array();
                thisAndParameters.add(this.node);
                thisAndParameters.addAll(parameters);


                return NodeSelectorContexts.basicFunctions().apply(name)
                        .get()
                        .apply(thisAndParameters, this.expressionFunctionContext());
            }

            private ExpressionFunctionContext expressionFunctionContext() {
                return new FakeExpressionFunctionContext() {
                    @Override
                    public <T> T convert(Object value, Class<T> target) {
                        return convert0(value, target);
                    }
                };
            }

            private <T> T convert0(final Object value, final Class<T> target) {
                return this.convert(value, target);
            }

            @Override
            public <T> T convert(final Object value, final Class<T> target) {
                Objects.requireNonNull(value, "value");
                Objects.requireNonNull(target, "target");

                return Cast.to(target.isInstance(value) ?
                        target.cast(value) :
                        target == BigDecimal.class ?
                                this.convertToBigDecimal(value) :
                                target == Boolean.class ?
                                        this.convertToBoolean(value) :
                                        target == Integer.class ?
                                                this.convertToInteger(value) :
                                                target == Number.class ?
                                                        this.convertToNumber(value) :
                                                        target == String.class ?
                                                                this.convertToString(value) :
                                                                this.failConversion(value, target));
            }

            /**
             * Currently {@link walkingkooka.tree.expression.ExpressionNode} will convert a pair of {@link Boolean} into
             * {@link BigDecimal} prior to performing the operation such as equals.
             */
            private BigDecimal convertToBigDecimal(final Object value) {
                final Converter converter = value instanceof Boolean ?
                        Converters.booleanConverter(Boolean.class, Boolean.TRUE, BigDecimal.class, BigDecimal.ONE, BigDecimal.ZERO) :
                        value instanceof String ?
                                Converters.parser(BigDecimal.class, Parsers.bigDecimal(MathContext.DECIMAL32), (c) -> ParserContexts.basic(c)) :
                                Converters.numberBigDecimal();
                return converter.convert(value, BigDecimal.class, this.converterContext);
            }

            private Boolean convertToBoolean(final Object value) {
                return Converters.truthyNumberBoolean().convert(value, Boolean.class, this.converterContext);
            }

            private Number convertToNumber(final Object value) {
                return Converters.booleanConverter(Boolean.class, Boolean.TRUE, Number.class, 1L, 0L)
                        .convert(value, Number.class, ConverterContexts.fake());
            }
            
            private Integer convertToInteger(final Object value) {
                if(value instanceof Number) {
                    return Number.class.cast(value).intValue();
                }
                return Integer.parseInt(String.valueOf(value));
            }

            private String convertToString(final Object value) {
                return Converters.string().convert(value, String.class, this.converterContext);
            }

            private <T> T failConversion(final Object value, final Class<T> target) {
                throw new ConversionException("Failed to convert " + CharSequences.quoteIfChars(value) + " to " + target.getSimpleName());
            }

            private final ConverterContext converterContext = ConverterContexts.basic(DecimalNumberContexts.basic("$", '.', 'E', ',', '-', '%', '+'));
        });

        assertEquals(expected, names(selected), () -> expression + "\n" + root);
    }

    private Set<String> names(final Collection<TestNode> nodes) {
        return nodes.stream().map(n -> n.name().value()).collect(Collectors.toCollection(Sets::ordered));
    }

    private NodeSelector<TestNode, StringName, StringName, Object> parseExpression(final String expression) {
        return NodeSelectorNodeSelectorParserTokenVisitor.<TestNode, StringName, StringName, Object>with(this.parseOrFail(expression),
                (s) -> Names.string(s.value()),
                Predicates.always(),
                TestNode.class);
    }

    private NodeSelectorExpressionParserToken parseOrFail(final String expression) {
        return NodeSelectorParsers.expression()
                .orFailIfCursorNotEmpty(ParserReporters.basic())
                .parse(TextCursors.charSequence(expression),
                        NodeSelectorParserContexts.basic())
                .orElseThrow(() -> new UnsupportedOperationException(expression))
                .cast();
    }

    @Override
    public NodeSelectorNodeSelectorParserTokenVisitor<TestNode, StringName, StringName, Object> createVisitor() {
        return new NodeSelectorNodeSelectorParserTokenVisitor<>(null, null, null);
    }

    @Override
    public String typeNamePrefix() {
        return NodeSelector.class.getSimpleName();
    }

    @Override
    public Class<NodeSelectorNodeSelectorParserTokenVisitor<TestNode, StringName, StringName, Object>> type() {
        return Cast.to(NodeSelectorNodeSelectorParserTokenVisitor.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
