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

package walkingkooka.build.tostring;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderTestCase;
import walkingkooka.collect.enumeration.Enumerations;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

final public class ToStringBuilderTest extends BuilderTestCase<ToStringBuilder, String> {
    // constants

    private final static Object NULL = null;

    private final static String FULL = "1234567890";
    private static final byte[] BYTE_ARRAY_NULL = null;
    private static final byte BYTE_1 = (byte) 1;
    private static final byte BYTE_255 = (byte) 0xff;
    private static final byte BYTE_0 = (byte) 0;
    private static final boolean[] BOOLEAN_ARRAY_NULL = null;
    private static final short SHORT_1 = (short) 1;
    private static final int[] INT_ARRAY_NULL = null;
    private static final long[] LONG_ARRAY_NULL = null;
    private static final float[] FLOAT_ARRAY_NULL = null;
    private static final double[] DOUBLE_ARRAY_NULL = null;
    private static final char[] CHAR_ARRAY_NULL = null;
    
    // tests

    @Test
    public void testCreateAndBuild() {
        this.buildAndCheck(ToStringBuilder.create());
    }

    @Test
    public void testClassIsFinal() {
        Assert.assertTrue(ToStringBuilder.class.toString(),
                Modifier.isFinal(ToStringBuilder.class.getModifiers()));
    }

    @Test
    public void testCheckToStringOverridden() {
        this.checkToStringOverridden(ToStringBuilder.class);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLabelFails() {
        ToStringBuilder.create().label(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLabelSeparatorFails() {
        ToStringBuilder.create().labelSeparator(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueSeparatorFails() {
        ToStringBuilder.create().valueSeparator(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSeparatorFails() {
        ToStringBuilder.create().separator(null);

    }

    @Test(expected = NullPointerException.class)
    public void testNullDisableFails() {
        ToStringBuilder.create().disable(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnableFails() {
        ToStringBuilder.create().enable(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSurroundValuesNullBeforeFails() {
        ToStringBuilder.create().surroundValues(null, "after");
    }

    @Test(expected = NullPointerException.class)
    public void testSurroundValuesNullAfterFails() {
        ToStringBuilder.create().surroundValues("before", null);
    }

    // boolean

    @Test
    public void testAppendBooleanTrue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(true)//
                , "true");
    }

    @Test
    public void testAppendBooleanFalse() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(false),//
                "false");
    }

    @Test
    public void testAppendBooleanClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(false)// clears label
                        .value("value"),//
                "false\"value\"");
    }

    @Test
    public void testTrue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(true), //
                "true");
    }

    @Test
    public void testFalse() {
        this.buildAndCheck(ToStringBuilder.create().value(false));
    }

    @Test
    public void testFalseWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(false),//
                "false");
    }

    @Test
    public void testTrueWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(true),//
                "label=true");
    }

    @Test
    public void testTrueWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(true),//
                "@@@label=true");
    }

    @Test
    public void testFalseWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(false));
    }

    @Test
    public void testNullBooleanArray() {
        this.buildAndCheck(ToStringBuilder.create().value(BOOLEAN_ARRAY_NULL));
    }

    @Test
    public void testNullBooleanArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(BOOLEAN_ARRAY_NULL));
    }

    @Test
    public void testNullBooleanArrayWithLabelWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(BOOLEAN_ARRAY_NULL),//
                "label=null");
    }

    @Test
    public void testEmptyBooleanArray() {
        this.buildAndCheck(ToStringBuilder.create().value(new boolean[0]));
    }

    @Test
    public void testEmptyBooleanArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new boolean[0]),//
                "");
    }

    @Test
    public void testEmptyBooleanArrayWithLabelWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(new boolean[0]),//
                "label=");
    }

    @Test
    public void testBooleanArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new boolean[]{true, false}), //
                "true, false");
    }

    @Test
    public void testBooleanArrayValueSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .value(new boolean[]{true, false}), //
                "true!false");
    }

    @Test
    public void testBooleanArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .valueSeparator("!")//
                        .value(new boolean[]{true, false}),//
                "[true!false]");
    }

    @Test
    public void testBooleanArrayWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .valueSeparator("!")//
                        .value(new boolean[]{true, false}), //
                "label=[true!false]");
    }

    @Test
    public void testBooleanArrayWithOneAndZeroBooleanArrays() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS)//
                        .value(new boolean[]{true, false}),//
                "10");
    }

    @Test
    public void testBooleanArrayWithValueSeparatorOneAndZeroBooleanArrays() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS)//
                        .value(new boolean[]{true, false}), //
                "10");
    }

    @Test
    public void testBooleanArrayWithSurroundValuesAndOneAndZeroBooleanArrays() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .valueSeparator("!")//
                        .enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS)//
                        .value(new boolean[]{true, false}), //
                "10");
    }

    @Test
    public void testBooleanArraysWithLabelSurroundValuesAndOneAndZeroBooleanArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .valueSeparator("!")//
                        .enable(ToStringBuilderOption.ONE_AND_ZERO_BOOLEAN_ARRAYS)//
                        .value(new boolean[]{true, false}),//
                "label=10");
    }

    // byte

    @Test
    public void testAppendByte() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(BYTE_1), //
                "1");
    }

    @Test
    public void testAppendByteClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(BYTE_1)//
                        .value("value"),//
                "1\"value\"");
    }

    @Test
    public void testByte() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(BYTE_1), //
                "1");
    }

    @Test
    public void testByteWithHexBytes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_BYTES)//
                        .value(BYTE_1),//
                "01");
    }

    @Test
    public void testByteWithHexBytes2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_BYTES)//
                        .value(BYTE_255),//
                "FF");
    }

    @Test
    public void testByteWithHexWholeNumbers() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(BYTE_1),//
                "01");
    }

    @Test
    public void testByteWithHexWholeNumbers2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(BYTE_255),//
                "FF");
    }

    @Test
    public void testZeroByte() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(BYTE_0), //
                "");
    }

    @Test
    public void testZeroByteWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(BYTE_0), //
                "0");
    }

    @Test
    public void testLabelBooleanTrue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(true), //
                "label=true");
    }

    @Test
    public void testLabelBooleanFalse() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(false), //
                "");
    }

    @Test
    public void testLabelBooleanFalseWithoutSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(false), //
                "label=false");
    }

    @Test
    public void testLabelByte() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(1), //
                "label=1");
    }

    @Test
    public void testLabelByteWithAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(1), //
                "@@@label=1");
    }

    @Test
    public void testNullByteArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                .value(BYTE_ARRAY_NULL));
    }

    @Test
    public void testNullByteArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(BYTE_ARRAY_NULL));
    }

    @Test
    public void testNullByteArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(BYTE_ARRAY_NULL),//
                "label=null");
    }

    @Test
    public void testEmptyByteArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                .value(new byte[0]));
    }

    @Test
    public void testEmptyByteArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(new byte[0]));
    }

    @Test
    public void testEmptyByteArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(new byte[0]),//
                "label=");
    }

    @Test
    public void testByteArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new byte[]{0, 1}), //
                "0, 1");
    }

    @Test
    public void testByteArrayWithHexBytes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_BYTES)//
                        .value(new byte[]{0, 1, BYTE_255}),//
                "00, 01, FF");
    }

    @Test
    public void testByteArrayWithHexWholeNumbers() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(new byte[]{0, 1, BYTE_255}),//
                "00, 01, FF");
    }

    @Test
    public void testByteArrayValueSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .value(new byte[]{0, 1}), //
                "0!1");
    }

    @Test
    public void testByteArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new byte[]{0, 1}), //
                "label=0, 1");
    }

    @Test
    public void testByteArrayWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new byte[]{0, 1}), //
                "@@@label=0, 1");
    }

    @Test
    public void testByteArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .valueSeparator("!")//
                        .value(new byte[]{0, 1}),//
                "[0!1]");
    }

    @Test
    public void testByteArrayWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .valueSeparator("!")//
                        .value(new byte[]{0, 1}),//
                "label=[0!1]");
    }

    // char

    @Test
    public void testAppendChar() {
        this.buildAndCheck(ToStringBuilder.create().append('a'), "a");
    }

    @Test
    public void testAppendCharClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append('a')//
                        .value("value"),//
                "a\"value\"");
    }

    @Test
    public void testChar() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value('a'), //
                "'a'");
    }

    @Test
    public void testCharWithoutQuotes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.QUOTE)//
                        .value('a'),//
                "a");
    }

    @Test
    public void testCharEscape() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.ESCAPE)//
                        .value('\t'),//
                "'\\t\'");
    }

    @Test
    public void testZeroChar() {
        this.buildAndCheck(ToStringBuilder.create().value('\0'));
    }

    @Test
    public void testZeroCharWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value('\0'),//
                "'\0'");
    }

    @Test
    public void testCharWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value('a'), //
                "label='a'");
    }

    @Test
    public void testCharWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value('a'), //
                "@@@label='a'");
    }

    @Test
    public void testNullCharArray() {
        this.buildAndCheck(ToStringBuilder.create().value(CHAR_ARRAY_NULL));
    }

    @Test
    public void testNullCharArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(CHAR_ARRAY_NULL));
    }

    @Test
    public void testNullCharArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label").value(CHAR_ARRAY_NULL),//
                "label=null");
    }

    @Test
    public void testEmptyCharArray() {
        this.buildAndCheck(ToStringBuilder.create().value(new char[0]));
    }

    @Test
    public void testEmptyCharArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create().label("label").value(new char[0]));
    }

    @Test
    public void testEmptyCharArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(new char[0]), //
                "label=\"\"");
    }

    @Test
    public void testEmptyCharArrayWithLabelDisabledSkipDefaultWithoutQuotes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .disable(ToStringBuilderOption.QUOTE)//
                        .label("label")//
                        .value(new char[0]), //
                "label=");
    }

    @Test
    public void testCharArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new char[]{'a', 'b'}), //
                "\"ab\"");
    }

    @Test
    public void testCharArraySurroundValuesIgnore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new char[]{'a', 'b'}), //
                "\"ab\"");
    }

    @Test
    public void testCharArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new char[]{'a', 'b'}), //
                "label=\"ab\"");
    }

    @Test
    public void testCharArrayWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new char[]{'a', 'b'}), //
                "@@@label=\"ab\"");
    }

    // double

    @Test
    public void testAppendDouble() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(1.0), //
                "1.0");
    }

    @Test
    public void testAppendDoubleClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(1.0)//
                        .value("value"), //
                "1.0\"value\"");
    }

    @Test
    public void testDouble() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(1.0), //
                "1.0");
    }

    @Test
    public void testZeroDouble() {
        this.buildAndCheck(ToStringBuilder.create().value(0.0));
    }

    @Test
    public void testZeroDoubleWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(0.0), //
                "0.0");
    }

    @Test
    public void testDoubleWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(1.0), //
                "label=1.0");
    }

    @Test
    public void testDoubleWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(1.0), //
                "@@@label=1.0");
    }

    @Test
    public void testNullDoubleArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                .value(DOUBLE_ARRAY_NULL));
    }

    @Test
    public void testNullDoubleArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(DOUBLE_ARRAY_NULL));
    }

    @Test
    public void testNullDoubleArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(DOUBLE_ARRAY_NULL), //
                "label=null");
    }

    @Test
    public void testEmptyDoubleArray() {
        this.buildAndCheck(ToStringBuilder.create().value(new double[0]));
    }

    @Test
    public void testEmptyDoubleArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create().label("label").value(new double[0]));
    }

    @Test
    public void testEmptyDoubleArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(new double[0]), //
                "label=");
    }

    @Test
    public void testDoubleArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new double[]{0.0, 1.0}), //
                "0.0, 1.0");
    }

    @Test
    public void testDoubleArrayValueSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .value(new double[]{0.0, 1.0}), //
                "0.0!1.0");
    }

    @Test
    public void testDoubleArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new double[]{0.0, 1.0}), //
                "label=0.0, 1.0");
    }

    @Test
    public void testDoubleArrayWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new double[]{0.0, 1.0}), //
                "@@@label=0.0, 1.0");
    }

    @Test
    public void testDoubleArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new double[]{0.0, 1.0}), //
                "[0.0, 1.0]");
    }

    @Test
    public void testDoubleArrayWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(new double[]{0.0, 1.0}), //
                "label=[0.0, 1.0]");
    }

    // float

    @Test
    public void testAppendFloat() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(1.0f), //
                "1.0");
    }

    @Test
    public void testAppendFloatClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(1.0)//
                        .value("value"), //
                "1.0\"value\"");
    }

    @Test
    public void testFloat() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(1.0f), //
                "1.0");
    }

    @Test
    public void testZeroFloat() {
        this.buildAndCheck(ToStringBuilder.create().value(0.0f));
    }

    @Test
    public void testZeroFloatWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(0.0f), //
                "0.0");
    }

    @Test
    public void testFloatWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(1.0f), //
                "label=1.0");
    }

    @Test
    public void testFloatWithLabelAndPreviousApend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(1.0f), //
                "@@@label=1.0");
    }

    @Test
    public void testNullFloatArray() {
        this.buildAndCheck(ToStringBuilder.create().value(FLOAT_ARRAY_NULL));
    }

    @Test
    public void testNullFloatArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(FLOAT_ARRAY_NULL));
    }

    @Test
    public void testNullFloatArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(FLOAT_ARRAY_NULL), //
                "label=null");
    }

    @Test
    public void testEmptyFloatArray() {
        this.buildAndCheck(ToStringBuilder.create().value(new float[0]));
    }

    @Test
    public void testEmptyFloatArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create().label("label").value(new float[0]));
    }

    @Test
    public void testEmptyFloatArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(new float[0]), //
                "label=");
    }

    @Test
    public void testFloatArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new float[]{0.0f, 1.0f}), //
                "0.0, 1.0");
    }

    @Test
    public void testFloatArrayValueSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .value(new float[]{0.0f, 1.0f}), //
                "0.0!1.0");
    }

    @Test
    public void testFloatArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new float[]{0.0f, 1.0f}), //
                "label=0.0, 1.0");
    }

    @Test
    public void testFloatArrayWithLabelAndPreviousAPpend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new float[]{0.0f, 1.0f}), //
                "@@@label=0.0, 1.0");
    }

    @Test
    public void testFloatArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new float[]{0.0f, 1.0f}), //
                "[0.0, 1.0]");
    }

    @Test
    public void testFloatArrayWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(new float[]{0.0f, 1.0f}), //
                "label=[0.0, 1.0]");
    }

    // int

    @Test
    public void testAppendInt() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(1), //
                "1");
    }

    @Test
    public void testAppendIntClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(1)//
                        .value("value"), //
                "1\"value\"");
    }

    @Test
    public void testInt() {
        this.buildAndCheck(ToStringBuilder.create().value(1), "1");
    }

    @Test
    public void testIntWithHexWholeNumbers() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(1), //
                "00000001");
    }

    @Test
    public void testZeroInt() {
        this.buildAndCheck(ToStringBuilder.create().value(0));
    }

    @Test
    public void testZeroIntWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(0), //
                "0");
    }

    @Test
    public void testIntWithHexWholeNumbers2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(0xFECDBA98), //
                "FECDBA98");
    }

    @Test
    public void testIntWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(1), //
                "label=1");
    }

    @Test
    public void testIntWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(1), //
                "@@@label=1");
    }

    @Test
    public void testNullIntArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                .value(INT_ARRAY_NULL));
    }

    @Test
    public void testNullIntArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(INT_ARRAY_NULL));
    }

    @Test
    public void testNullIntArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(INT_ARRAY_NULL), //
                "label=null");
    }

    @Test
    public void testEmptyIntArray() {
        this.buildAndCheck(ToStringBuilder.create().value(new int[0]));
    }

    @Test
    public void testEmptyIntArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(new int[0]));
    }

    @Test
    public void testEmptyIntArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(new int[0]), //
                "label=");
    }

    @Test
    public void testIntArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new int[]{0, 1}), //
                "0, 1");
    }

    @Test
    public void testIntArrayWithHexWholeNumbers() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(new int[]{0, 1, 0xFEDCBA98}), //
                "00000000, 00000001, FEDCBA98");
    }

    @Test
    public void testIntArrayValueSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .value(new int[]{0, 1}), //
                "0!1");
    }

    @Test
    public void testIntArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new int[]{0, 1}), //
                "label=0, 1");
    }

    @Test
    public void testIntArrayWithLabelAndAppendBefore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new int[]{0, 1}), //
                "@@@label=0, 1");
    }

    @Test
    public void testIntArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new int[]{0, 1}), //
                "[0, 1]");
    }

    @Test
    public void testIntArrayWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create().//
                        label("label")//
                        .surroundValues("[", "]")//
                        .value(new int[]{0, 1}), //
                "label=[0, 1]");
    }

    // long

    @Test
    public void testAppendLong() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(1L), //
                "1");
    }

    @Test
    public void testAppendLongClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(1L)//
                        .value("value"), //
                "1\"value\"");
    }

    @Test
    public void testLong() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(1L), //
                "1");
    }

    @Test
    public void testLongWithHexWholeNumbers() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS).value(1L), //
                "0000000000000001");
    }

    @Test
    public void testLongWithHexWholeNumbers2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(0x123456789ABCDEF0L), //
                "123456789ABCDEF0");
    }

    @Test
    public void testZeroLong() {
        this.buildAndCheck(ToStringBuilder.create().value(0L));
    }

    @Test
    public void testZeroLongWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(0L), //
                "0");
    }

    @Test
    public void testLongWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(1L), //
                "label=1");
    }

    @Test
    public void testLongWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(1L), //
                "@@@label=1");
    }

    @Test
    public void testNullLongArray() {
        this.buildAndCheck(ToStringBuilder.create().value(LONG_ARRAY_NULL));
    }

    @Test
    public void testNullLongArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(LONG_ARRAY_NULL));
    }

    @Test
    public void testNullLongArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(LONG_ARRAY_NULL),//
                "label=null");
    }

    @Test
    public void testEmptyLongArray() {
        this.buildAndCheck(ToStringBuilder.create().value(new long[0]));
    }

    @Test
    public void testEmptyLongArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create().label("label").value(new long[0]));
    }

    @Test
    public void testEmptyLongArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(new long[0]), //
                "label=");
    }

    @Test
    public void testLongArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new long[]{0, 1}), //
                "0, 1");
    }

    @Test
    public void testLongArrayWithHexWholeNumbers() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(new long[]{0, 1, 0x123456789ABCDEF0L}), //
                "0000000000000000, 0000000000000001, 123456789ABCDEF0");
    }

    @Test
    public void testLongArrayValueSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .value(new long[]{0L, 1L}), //
                "0!1");
    }

    @Test
    public void testLongArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new long[]{0L, 1L}), //
                "label=0, 1");
    }

    @Test
    public void testLongArrayWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new long[]{0L, 1L}), //
                "@@@label=0, 1");
    }

    @Test
    public void testLongArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new long[]{0L, 1L}), //
                "[0, 1]");
    }

    @Test
    public void testLongArrayWithLabelSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(new long[]{0L, 1L}), //
                "label=[0, 1]");
    }

    // short

    @Test
    public void testAppendShort() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(SHORT_1), //
                "1");
    }

    @Test
    public void testAppendShortClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(SHORT_1)//
                        .value("value"), //
                "1\"value\"");
    }

    @Test
    public void testShort() {
        this.buildAndCheck(ToStringBuilder.create().//
                        value(SHORT_1), //
                "1");
    }

    @Test
    public void testShortWithHexWholeNumbers() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value(SHORT_1), //
                "0001");
    }

    @Test
    public void testShortWithHexWholeNumbers2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.HEX_WHOLE_NUMBERS)//
                        .value((short) 0xFEDC), //
                "FEDC");
    }

    @Test
    public void testZeroShort() {
        this.buildAndCheck(ToStringBuilder.create().value((short) 0));
    }

    @Test
    public void testZeroShortWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value((short) 0), //
                "0");
    }

    @Test
    public void testLabelShort() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(SHORT_1), //
                "label=1");
    }

    @Test
    public void testLabelShortAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(SHORT_1), //
                "@@@label=1");
    }

    @Test
    public void testNullShortArray() {
        this.buildAndCheck(ToStringBuilder.create().value((short[]) null));
    }

    @Test
    public void testNullShortArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create().label("label").value((short[]) null));
    }

    @Test
    public void testNullShortArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value((short[]) null), //
                "label=null");
    }

    @Test
    public void testEmptyShortArray() {
        this.buildAndCheck(ToStringBuilder.create().value(new short[0]));
    }

    @Test
    public void testEmptyShortArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create().label("label").value(new short[0]));
    }

    @Test
    public void testEmptyShortArrayWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                .label("label")//
                .value(new short[0]), "label=");
    }

    @Test
    public void testShortArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new short[]{0, 1}), //
                "0, 1");
    }

    @Test
    public void testShortArrayValueSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .valueSeparator("!")//
                        .value(new short[]{0, 1}), //
                "0!1");
    }

    @Test
    public void testShortArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new short[]{0, 1}), //
                "label=0, 1");
    }

    @Test
    public void testShortArrayWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new short[]{0, 1}), //
                "@@@label=0, 1");
    }

    @Test
    public void testShortArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new short[]{0, 1}), //
                "[0, 1]");
    }

    @Test
    public void testShortArrayWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(new short[]{0, 1}), //
                "label=[0, 1]");
    }

    // string

    @Test
    public void testAppendCharSequence() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("abc"), //
                "abc");
    }

    @Test
    public void testAppendNullCharSequence() {
        final String string = null;
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(string), //
                String.valueOf(string));
    }

    @Test
    public void testAppendCharSequenceClearsLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append("string")//
                        .value("value"), //
                "string\"value\"");
    }

    // String

    @Test
    public void testString() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value("abc"), //
                "\"abc\"");
    }

    @Test
    public void testStringWithoutQuotes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.QUOTE)//
                        .value("abc"), //
                "abc");
    }

    @Test
    public void testStringEscapeWithoutQuotes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.ESCAPE)//
                        .disable(ToStringBuilderOption.QUOTE)//
                        .value("before tab\t"), //
                "before tab\\t");
    }

    @Test
    public void testStringEscapeWithQuotes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.ESCAPE)//
                        .enable(ToStringBuilderOption.QUOTE).value("before tab\t"), //
                "\"before tab\\t\"");
    }

    @Test
    public void testStringSurroundValuesIgnore() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value("abc"), //
                "\"abc\"");
    }

    @Test
    public void testEmptyString() {
        this.buildAndCheck(ToStringBuilder.create().value(""));
    }

    @Test
    public void testEmptyStringWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(""), //
                "\"\"");
    }

    @Test
    public void testEmptyStringAfterLabelValueWithSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("@")//
                        .label("first")//
                        .value(1)//
                        .value(""), //
                "first=1");
    }

    @Test
    public void testStringWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value("abc"), //
                "label=\"abc\"");
    }

    @Test
    public void testStringWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value("abc"), //
                "@@@label=\"abc\"");
    }

    @Test
    public void testEmptyStringWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(""));
    }

    @Test
    public void testEmptyStringWithLabelAndSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("@")//
                        .label("first")//
                        .value(1)//
                        .label("label")//
                        .value(""), //
                "first=1");
    }

    // CharSequence

    @Test
    public void testCharSequence() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new StringBuilder("abc")), //
                "\"abc\"");
    }

    @Test
    public void testCharSequenceWithoutQuotes() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.QUOTE)//
                        .value(new StringBuilder("abc")), //
                "abc");
    }

    @Test
    public void testCharSequenceEscape() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .enable(ToStringBuilderOption.ESCAPE)//
                        .value(new StringBuilder("before tab\t")),//
                "\"before tab\\t\"");
    }

    @Test
    public void testCharSequenceIgnoresSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new StringBuilder("abc")), //
                "\"abc\"");
    }

    @Test
    public void testEmptyCharSequence() {
        this.buildAndCheck(ToStringBuilder.create().value(new StringBuilder()));
    }

    @Test
    public void testEmptyCharSequenceWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(new StringBuilder()),//
                "\"\"");
    }

    @Test
    public void testEmptyCharSequenceAfterLabelValueWithSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("@")//
                        .label("first")//
                        .value(1)//
                        .value(new StringBuilder()), //
                "first=1");
    }

    // object

    @Test
    public void testAppendObject() {
        final Object object = new Object();
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(object), //
                object.toString());
    }

    @Test
    public void testAppendNullObject() {
        final Object object = null;
        this.buildAndCheck(ToStringBuilder.create()//
                        .append(object), //
                String.valueOf(object));
    }

    @Test
    public void testAppendObjectClearsLabel() {
        final Object object = new Object();
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .append(object)//
                        .value("value"), //
                object + "\"value\"");
    }

    @Test
    public void testObject() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(ToStringBuilderTest.x), //
                "xyz");
    }

    @Test
    public void testNullObject() {
        this.buildAndCheck(ToStringBuilder.create().value(ToStringBuilderTest.NULL));
    }

    @Test
    public void testNullObjectWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(ToStringBuilderTest.NULL), //
                "null");
    }

    @Test
    public void testObjectEmptyStringAfterLabelValueWithSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("@")//
                        .label("first")//
                        .value(1)//
                        .value(new Object() {
                            @Override
                            public String toString() {
                                return "";
                            }
                        }), //
                "first=1");
    }

    @Test
    public void testObjectWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(ToStringBuilderTest.x), //
                "label=xyz");
    }

    @Test
    public void testObjectWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(ToStringBuilderTest.x), //
                "@@@label=xyz");
    }

    // Optional

    @Test
    public void testOptionalNull() {
        this.buildAndCheck(ToStringBuilder.create().value(Cast.<Optional<?>>to(null)), "");
    }

    @Test
    public void testOptionalEmpty() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.empty()), "");
    }

    @Test
    public void testOptionalBooleanFalse() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(false)), "false");
    }

    @Test
    public void testOptionalBooleanFalseSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .value(Optional.of(false)),
                "false");
    }

    @Test
    public void testOptionalBooleanTrue() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(true)), "true");
    }

    @Test
    public void testOptionalByteZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(0)), "");
    }

    @Test
    public void testOptionalByteZeroSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(Optional.of(BYTE_0)),
                "0");
    }

    @Test
    public void testOptionalByteNonZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of((byte)123)), "123");
    }

    @Test
    public void testOptionalShortZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of((short)0)), "");
    }

    @Test
    public void testOptionalShortZeroSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(Optional.of((short)0)),
                "0");
    }

    @Test
    public void testOptionalShortNonZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of((short)123)), "123");
    }

    @Test
    public void testOptionalIntZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(0)), "");
    }

    @Test
    public void testOptionalIntZeroSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(Optional.of(0)),
                "0");
    }

    @Test
    public void testOptionalIntNonZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(123)), "123");
    }

    @Test
    public void testOptionalLongZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(0L)), "");
    }

    @Test
    public void testOptionalLongZeroSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(Optional.of(0L)),
                "0");
    }

    @Test
    public void testOptionalLongNonZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(123L)), "123");
    }

    @Test
    public void testOptionalFloatZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of((float)0.0)), "");
    }

    @Test
    public void testOptionalFloatZeroSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(Optional.of((float)0.0)),
                "0.0");
    }

    @Test
    public void testOptionalFloatNonZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of((float)1.23)), "1.23");
    }

    @Test
    public void testOptionalDoubleZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(0.0)), "");
    }

    @Test
    public void testOptionalDoubleZeroSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(Optional.of(0.0)),
                "0.0");
    }

    @Test
    public void testOptionalDoubleNonZero() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(1.23)), "1.23");
    }

    @Test
    public void testOptionalNullCharSequence() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.ofNullable(null)), "");
    }

    @Test
    public void testOptionalEmptyCharSequence() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of("")), "");
    }

    @Test
    public void testOptionalEmptyCharSequenceSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                        .value(Optional.of("")),
                "\"\"");
    }

    @Test
    public void testOptionalNonEmptyCharSequence() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of("abc")), "\"abc\"");
    }

    @Test
    public void testOptionalEmptyList() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(Lists.empty())), "");
    }

    @Test
    public void testOptionalList() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(Lists.of(1,2,3))), "1, 2, 3");
    }

    @Test
    public void testOptionalEmptyMap() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(Maps.empty())), "");
    }

    @Test
    public void testOptionalMap() {
        this.buildAndCheck(ToStringBuilder.create().value(Optional.of(Collections.singletonMap(1, 2))), "1=2");
    }

    @Test
    public void testOptionalWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()
                .label("abc")
                .value("123"),
                "abc=123");
    }
    
    // List

    @Test
    public void testEmptyList() {
        this.buildAndCheck(ToStringBuilder.create().value(Lists.empty()));
    }

    @Test
    public void testEmptyListWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(Lists.empty()));
    }

    @Test
    public void testList() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Lists.of(ToStringBuilderTest.x)), //
                "xyz");
    }

    @Test
    public void testList2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Lists.of(1, 2, 3)), //
                "1, 2, 3");
    }

    @Test
    public void testListOfStrings() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Lists.of("abc", "def")), //
                "\"abc\", \"def\"");
    }

    @Test
    public void testListOfCharSequences() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Lists.of(new StringBuilder("abc"), new StringBuilder("def"))),//
                "\"abc\", \"def\"");
    }

    @Test
    public void testListIncludesNull() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Lists.of((Object) null)), //
                "null");
    }

    @Test
    public void testListWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(Lists.of(1, 2, 3)), //
                "label=1, 2, 3");
    }

    @Test
    public void testListWithLabelWithPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(Lists.of(1, 2, 3)), //
                "@@@label=1, 2, 3");
    }

    @Test
    public void testListSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(Lists.of(1, 2, 3)), //
                "[1, 2, 3]");
    }

    @Test
    public void testListWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(Lists.of(1, 2, 3)), //
                "label=[1, 2, 3]");
    }

    // Iterable

    @Test
    public void testEmptyIterable() {
        this.buildAndCheck(ToStringBuilder.create().value(this.iterable()));
    }

    @Test
    public void testEmptyIterableWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(this.iterable()));
    }

    @Test
    public void testEmptyIterableWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(this.iterable()), //
                "label=");
    }

    @Test
    public void testIterable() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterable(ToStringBuilderTest.x)),//
                "xyz");
    }

    @Test
    public void testIterable2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterable(1, 2, 3)), //
                "1, 2, 3");
    }

    @Test
    public void testIterableOfStrings() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterable("abc", "def")), //
                "\"abc\", \"def\"");
    }

    @Test
    public void testIterableOfCharSequences() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterable(new StringBuilder("abc"), new StringBuilder("def"))),//
                "\"abc\", \"def\"");
    }

    @Test
    public void testIterableIncludesNull() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterable(ToStringBuilderTest.NULL)), //
                "null");
    }

    @Test
    public void testIterableWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(this.iterable(1, 2, 3)), //
                "label=1, 2, 3");
    }

    @Test
    public void testIterableSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(this.iterable(1, 2, 3)), //
                "[1, 2, 3]");
    }

    // Iterator

    @Test
    public void testEmptyIterator() {
        this.buildAndCheck(ToStringBuilder.create().value(this.iterator()));
    }

    @Test
    public void testEmptyIteratorWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(this.iterator()));
    }

    @Test
    public void testEmptyIteratorWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(this.iterator()), //
                "label=");
    }

    @Test
    public void testIterator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterator(ToStringBuilderTest.x)), //
                "xyz");
    }

    @Test
    public void testIterator2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterator(1, 2, 3)), //
                "1, 2, 3");
    }

    @Test
    public void testIteratorSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(this.iterator(1, 2, 3)), //
                "[1, 2, 3]");
    }

    @Test
    public void testIteratorOfStrings() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterator("abc", "def")), //
                "\"abc\", \"def\"");
    }

    @Test
    public void testIteratorOfCharSequences() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterator(new StringBuilder("abc"), new StringBuilder("def"))),//
                "\"abc\", \"def\"");
    }

    @Test
    public void testIteratorIncludesNull() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.iterator(ToStringBuilderTest.NULL)), //
                "null");
    }

    @Test
    public void testIteratorWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(this.iterator(1, 2, 3)), //
                "label=1, 2, 3");
    }

    @Test
    public void testIteratorWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(this.iterator(1, 2, 3)), //
                "@@@label=1, 2, 3");
    }

    @Test
    public void testIteratorWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(this.iterator(1, 2, 3)),//
                "label=[1, 2, 3]");
    }

    // Enumeration

    @Test
    public void testEmptyEnumeration() {
        this.buildAndCheck(ToStringBuilder.create().value(this.enumeration()));
    }

    @Test
    public void testEmptyEnumerationWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(this.enumeration()));
    }

    @Test
    public void testEmptyEnumerationWithLabelDisabledSkipDefault() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .label("label")//
                        .value(this.enumeration()), //
                "label=");
    }

    @Test
    public void testEnumeration() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.enumeration(ToStringBuilderTest.x)), //
                "xyz");
    }

    @Test
    public void testEnumeration2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.enumeration(1, 2, 3)), //
                "1, 2, 3");
    }

    @Test
    public void testEnumerationSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(this.enumeration(1, 2, 3)), //
                "[1, 2, 3]");
    }

    @Test
    public void testEnumerationOfStrings() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.enumeration("abc", "def")), //
                "\"abc\", \"def\"");
    }

    @Test
    public void testEnumerationOfCharSequences() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.enumeration(new StringBuilder("abc"), new StringBuilder("def"))),//
                "\"abc\", \"def\"");
    }

    @Test
    public void testEnumerationIncludesNull() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(this.enumeration(ToStringBuilderTest.NULL)), //
                "null");
    }

    @Test
    public void testEnumerationWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(this.enumeration(1, 2, 3)), //
                "label=1, 2, 3");
    }

    @Test
    public void testEnumerationWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(this.enumeration(1, 2, 3)), //
                "@@@label=1, 2, 3");
    }

    @Test
    public void testEnumerationWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(this.enumeration(1, 2, 3)),//
                "label=[1, 2, 3]");
    }

    // Set

    @Test
    public void testEmptySet() {
        this.buildAndCheck(ToStringBuilder.create().value(Sets.empty()));
    }

    @Test
    public void testEmptySetWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(Sets.empty()));
    }

    @Test
    public void testSet() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Sets.of(ToStringBuilderTest.x)), //
                "xyz");
    }

    @Test
    public void testSet2() {
        final Set<Integer> values = Sets.sorted();
        values.add(1);
        values.add(2);
        values.add(3);
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(values), //
                "1, 2, 3");
    }

    @Test
    public void testSetOfStrings() {
        final Set<String> values = Sets.sorted();
        values.add("abc");
        values.add("def");
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(values), //
                "\"abc\", \"def\"");
    }

    @Test
    public void testSetOfCharSequences() {
        final Set<StringBuilder> values = Sets.ordered();
        values.add(new StringBuilder("abc"));
        values.add(new StringBuilder("def"));
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(values), //
                "\"abc\", \"def\"");
    }

    @Test
    public void testSetIncludesNull() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Sets.of((Object) null)), //
                "null");
    }

    @Test
    public void testSetWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(Lists.of(1, 2)), //
                "label=1, 2");
    }

    @Test
    public void testSetWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(Lists.of(1, 2)), //
                "@@@label=1, 2");
    }

    @Test
    public void testSetSurroundValues() {
        final Set<Integer> values = Sets.sorted();
        values.add(1);
        values.add(2);
        values.add(3);
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(values), //
                "[1, 2, 3]");
    }

    @Test
    public void testSetWithLabelAndSurroundValues() {
        final Set<Integer> values = Sets.sorted();
        values.add(1);
        values.add(2);
        values.add(3);
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(values), //
                "label=[1, 2, 3]");
    }

    // Map

    @Test
    public void testEmptyMap() {
        this.buildAndCheck(ToStringBuilder.create().value(Maps.empty()));
    }

    @Test
    public void testEmptyMapWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                .label("label")//
                .value(Maps.empty()));
    }

    @Test
    public void testMap() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Maps.one("key", 1)), //
                "key=1");
    }

    @Test
    public void testMapStringValue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Maps.one("key", "value")), //
                "key=\"value\"");
    }

    @Test
    public void testMapCharSequenceValue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Maps.one(new StringBuilder("key"), new StringBuilder("value"))),//
                "key=\"value\"");
    }

    @Test
    public void testMapIncludesNullValue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(Maps.one("key", null)), //
                "key=null");
    }

    @Test
    public void testMap2() {
        final Map<String, Integer> values = Maps.sorted();
        values.put("a", 1);
        values.put("b", 2);
        values.put("c", 3);
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(values), //
                "a=1, b=2, c=3");
    }

    @Test
    public void testMapWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(Maps.one("key", 1)), //
                "label=key=1");
    }

    @Test
    public void testMapWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(Maps.one("key", 1)), //
                "@@@label=key=1");
    }

    @Test
    public void testMapSurroundValues() {
        final Map<String, Integer> values = Maps.sorted();
        values.put("a", 1);
        values.put("b", 2);
        values.put("c", 3);
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(values), //
                "[a=1, b=2, c=3]");
    }

    @Test
    public void testMapWithLabelAndSurroundValues() {
        final Map<String, Integer> values = Maps.sorted();
        values.put("a", 1);
        values.put("b", 2);
        values.put("c", 3);
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(values), //
                "label=[a=1, b=2, c=3]");
    }

    @Test
    public void testArray() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .value(new Object[]{ToStringBuilderTest.x}), //
                "xyz");
    }

    @Test
    public void testNullArray() {
        this.buildAndCheck(ToStringBuilder.create().value((Object[]) null));
    }

    @Test
    public void testNullArrayWithoutSkip() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)//
                        .value(ToStringBuilderTest.NULL), //
                "null");
    }

    @Test
    public void testEmptyLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("")//
                        .value("xyz"), //
                "\"xyz\"");
    }

    @Test
    public void testArrayWithLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new Object[]{ToStringBuilderTest.x}),//
                "label=xyz");
    }

    @Test
    public void testArrayWithLabel2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(new Object[]{ToStringBuilderTest.x, true, BYTE_1, 'a', 2.0, 3.0f, 4L,
                                (short) 5}),//
                "label=xyz, true, 1, 'a', 2.0, 3.0, 4, 5");
    }

    @Test
    public void testArrayWithLabelAndPreviousAppend() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .append("@@@")//
                        .label("label")//
                        .value(new Object[]{ToStringBuilderTest.x}),//
                "@@@label=xyz");
    }

    @Test
    public void testArraySurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(new Object[]{ToStringBuilderTest.x}),//
                "[xyz]");
    }

    @Test
    public void testArrayWithLabelAndSurroundValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .surroundValues("[", "]")//
                        .value(new Object[]{ToStringBuilderTest.x}), //
                "label=[xyz]");
    }

    private final static X x = new X();

    private static class X {
        @Override
        public String toString() {
            return "xyz";
        }
    }

    // UsesToStringBuilder

    @Test
    public void testEmptyUsesToStringBuilderIgnoresLabel() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label")//
                        .value(this.createUsesToStringBuilder("")), //
                "");
    }

    @Test
    public void testEmptyUsesToStringBuilderThenValue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("%") //
                        .value(this.createUsesToStringBuilder("")) //
                        .value("next"),//
                "\"next\"");
    }

    // surroundValues

    @Test
    public void testSurroundValuesSingleValueUnsurrounded() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .value(1), //
                "1");
    }

    @Test
    public void testSurroundValuesLabelAndSingleValueUnsurrounded() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .label("label")//
                        .value(1),//
                "label=1");
    }

    @Test
    public void testSurroundValuesMultipleValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .valueSeparator(",")//
                        .value(new Object[]{1, 2, 3}),//
                "[1,2,3]");
    }

    @Test
    public void testSurroundValuesLabelAndMultipleValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .valueSeparator(",")//
                        .label("label").value(new Object[]{1, 2, 3}),//
                "label=[1,2,3]");
    }

    @Test
    public void testSurroundValuesEmptyBeforeAndMultipleValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("", "]")//
                        .valueSeparator(",")//
                        .value(new Object[]{1, 2, 3}),//
                "1,2,3]");
    }

    @Test
    public void testSurroundValuesEmptyAfterAndMultipleValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "")//
                        .valueSeparator(",")//
                        .value(new Object[]{1, 2, 3}),//
                "[1,2,3");
    }

    @Test
    public void testSurroundValuesIgnoredByOneUsesToStringBuilder() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .separator("") //
                        .valueSeparator(",")//
                        .value(this.createUsesToStringBuilder(1)),//
                "1");
    }

    @Test
    public void testSurroundValuesUsesToStringBuilder2() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .surroundValues("[", "]")//
                        .separator("") //
                        .valueSeparator(",")//
                        .value(this.createUsesToStringBuilder(1, 2)),//
                "[1,2]");
    }

    // separator

    @Test
    public void testSkipSeparatorForValuesWithoutLabelsWithAppendsBetween() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("*")//
                        .value(1)//
                        .append(",")//
                        .value(2)//
                        .append(",")//
                        .value(3), //
                "1,2,3");
    }

    @Test
    public void testAddsSeparatorBetweenValuesWithoutLabels() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("*")//
                        .value(1)//
                        .value(2)//
                        .value(3), //
                "1*2*3");
    }

    @Test
    public void testManyLabelsAndValues() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .label("label1")//
                        .value(1)//
                        .label("label2")//
                        .value(2), //
                "label1=1 label2=2");
    }

    @Test
    public void testManyLabelsAndValuesChangeSeparator() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("*")//
                        .label("label1")//
                        .value(1)//
                        .label("label2")//
                        .value(2),//
                "label1=1*label2=2");
    }

    @Test
    public void testManyLabelsIncludingNullValue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("*")//
                        .label("label1").value(1)//
                        .label("label2").value(ToStringBuilderTest.NULL)//
                        .label("label3").value(3), //
                "label1=1*label3=3");
    }

    @Test
    public void testValueWithoutLabelAndNullValue() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("*")//
                        .label("label1").value(1)//
                        .label("label2").value(ToStringBuilderTest.NULL)//
                        .value(3), //
                "label1=1*3");
    }

    @Test
    public void testValueImplementsUsesToStringBuilder() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("*")//
                        .labelSeparator("=")//
                        .valueSeparator(",")//
                        .disable(ToStringBuilderOption.QUOTE)//
                        .label("label1").value(1) //
                        .label("label2").value(this.createUsesToStringBuilder(2))//
                        .label("label3").value(3), //
                "label1=1*label2=2*label3=3");
    }

    @Test
    public void testUsesToStringBuilderChangesBuilderState() {
        this.buildAndCheck(ToStringBuilder.create()//
                        .separator("*")
                        .labelSeparator("=")
                        .valueSeparator(",")
                        .disable(ToStringBuilderOption.QUOTE)//
                        .label("label1")
                        .value("value1") //
                        .label("label2")
                        .value(new UsesToStringBuilder() {

                            @Override
                            public void buildToString(final ToStringBuilder builder) {
                                builder.separator("/")
                                        .labelSeparator(":")
                                        .valueSeparator(";")
                                        .enable(ToStringBuilderOption.QUOTE);
                                builder.value("value2");
                                builder.label("label3").value(Lists.of(30, 31));
                            }
                        })//
                        .label("label4")
                        .value(Lists.of("value4", "value4b")), //
                "label1=value1*label2=\"value2\"/label3:30;31*label4=value4,value4b");
    }

    // valueLength and globalLength

    @Test
    public void testInvalidValueLengthFails() {
        final ToStringBuilder builder = ToStringBuilder.create();
        try {
            builder.valueLength(0);
            Assert.fail();
        } catch (final IllegalArgumentException expected) {
        }
    }

    @Test
    public void testValueLengthMore() {
        this.valueLengthAndCheck(ToStringBuilder.VALUE_LENGTH + 100);
    }

    @Test
    public void testValueLengthLess() {
        this.valueLengthAndCheck(ToStringBuilder.VALUE_LENGTH - 100);
    }

    private void valueLengthAndCheck(final int length) {
        final ToStringBuilder builder = ToStringBuilder.create();
        builder.valueLength(length);
        Assert.assertEquals("valueLength", length, builder.valueLength);
    }

    @Test
    public void testInvalidGlobalLengthFails() {
        final ToStringBuilder builder = ToStringBuilder.create();
        try {
            builder.globalLength(0);
            Assert.fail();
        } catch (final IllegalArgumentException expected) {
        }
    }

    @Test
    public void testGlobalLengthMore() {
        this.globalLengthAndCheck(ToStringBuilder.GLOBAL_LENGTH + 100);
    }

    @Test
    public void testGlobalLengthLess() {
        this.globalLengthAndCheck(ToStringBuilder.GLOBAL_LENGTH - 100);
    }

    private void globalLengthAndCheck(final int length) {
        final ToStringBuilder builder = ToStringBuilder.create();
        builder.globalLength(length);
        Assert.assertEquals("globalLength", length, builder.globalLength);
    }

    // append

    @Test
    public void testAppendBooleanWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(true));
    }

    @Test
    public void testAppendBooleanArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new boolean[]{true}));
    }

    @Test
    public void testAppendByteWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(true));
    }

    @Test
    public void testAppendByteArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new byte[]{1}));
    }

    @Test
    public void testAppendShortWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(SHORT_1));
    }

    @Test
    public void testAppendShortArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new short[]{1}));
    }

    @Test
    public void testAppendIntWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(1));
    }

    @Test
    public void testAppendIntArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new int[]{1}));
    }

    @Test
    public void testAppendLongWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(1L));
    }

    @Test
    public void testAppendLongArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new long[]{1}));
    }

    @Test
    public void testAppendFloatWhenFull() {
        this.buildAndCheckFull(this.createFull().append(1.0f));
    }

    @Test
    public void testAppendFloatArrayWhenFull() {
        this.buildAndCheckFull(this.createFull().append(new float[]{1}));
    }

    @Test
    public void testAppendDoubleWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(1.0));
    }

    @Test
    public void testAppendDoubleArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new double[]{1}));
    }

    @Test
    public void testAppendCharWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append('a'));
    }

    @Test
    public void testAppendCharArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new char[]{'a'}));
    }

    @Test
    public void testAppendObjectWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new Object()));
    }

    @Test
    public void testAppendCharObjectWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .append(new Object[]{new Object()}));
    }

    // value

    @Test
    public void testBooleanWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(true));
    }

    @Test
    public void testBooleanArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new boolean[]{true}));
    }

    @Test
    public void testByteWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(true));
    }

    @Test
    public void testByteArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new byte[]{1}));
    }

    @Test
    public void testShortWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(SHORT_1));
    }

    @Test
    public void testShortArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new short[]{1}));
    }

    @Test
    public void testIntWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(1));
    }

    @Test
    public void testIntArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new int[]{1}));
    }

    @Test
    public void testLongWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(1L));
    }

    @Test
    public void testLongArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new long[]{1}));
    }

    @Test
    public void testFloatWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(1.0f));
    }

    @Test
    public void testFloatArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new float[]{1}));
    }

    @Test
    public void testDoubleWhenFull() {
        this.buildAndCheckFull(this.createFull() //
                .value(1.0));
    }

    @Test
    public void testDoubleArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new double[]{1}));
    }

    @Test
    public void testCharWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value('a'));
    }

    @Test
    public void testCharArrayWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new char[]{'a'}));
    }

    @Test
    public void testObjectWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new Object()));
    }

    @Test
    public void testCharObjectWhenFull() {
        this.buildAndCheckFull(this.createFull()//
                .value(new Object[]{new Object()}));
    }

    // label+value

    @Test
    public void testBooleanWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(true));
    }

    @Test
    public void testBooleanArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new boolean[]{true}));
    }

    @Test
    public void testByteWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(true));
    }

    @Test
    public void testByteArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new byte[]{1}));
    }

    @Test
    public void testShortWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(SHORT_1));
    }

    @Test
    public void testShortArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new short[]{1}));
    }

    @Test
    public void testIntWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel() //
                .value(1));
    }

    @Test
    public void testIntArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new int[]{1}));
    }

    @Test
    public void testLongWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(1L));
    }

    @Test
    public void testLongArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new long[]{1}));
    }

    @Test
    public void testFloatWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(1.0f));
    }

    @Test
    public void testFloatArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new float[]{1}));
    }

    @Test
    public void testDoubleWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(1.0));
    }

    @Test
    public void testDoubleArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new double[]{1}));
    }

    @Test
    public void testCharWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value('a'));
    }

    @Test
    public void testCharArrayWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new char[]{'a'}));
    }

    @Test
    public void testObjectWithLabelWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new Object()));
    }

    @Test
    public void testUsesToStringBuilderWhenFull() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(this.createUsesToStringBuilder("1")));
    }

    @Test
    public void testUsesToStringBuilderParent() {
        final UsesToStringBuilder[] uses = new UsesToStringBuilder[1];
        uses[0] = new UsesToStringBuilder() {

            @Override
            public void buildToString(final ToStringBuilder builder) {
                assertSame("no parent", null, builder.parent());
                builder.value("1");
                builder.value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        assertSame("parent should be outer UsesToStringBuilder",
                                uses[0],
                                builder.parent());
                        builder.value("2");
                    }
                });
            }
        };

        this.buildAndCheck(this.create()//
                        .value(uses[0]),//
                "12");
    }

    @Test
    public void testUsesToStringBuilderGlobalLength() {
        this.buildAndCheckFull(this.createFullWithLabel()//
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.globalLength(5);
                        Assert.assertEquals("globalLength", 5, builder.globalLength);
                        builder.value("IGNORED!");
                    }
                }));
    }

    @Test
    public void testUsesToStringBuilderGlobalLength2() {
        this.buildAndCheck(this.create()//
                .globalLength(15)//
                .value("1234567890") //
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.globalLength(100);
                        Assert.assertEquals("globalLength",
                                builder.buffer.length() + 100,
                                builder.globalLength);
                        builder.value("abcdefghij");
                        builder.value("ignored");
                    }
                }), "1234567890abcde");
    }

    @Test
    public void testUsesToStringBuilderGlobalLength3() {
        this.buildAndCheck(this.create()//
                .globalLength(15)//
                .value("1234567890") //
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.globalLength(5);
                        Assert.assertEquals("globalLength",
                                builder.buffer.length() + 5,
                                builder.globalLength);

                        builder.globalLength(100);
                        Assert.assertEquals("globalLength",
                                builder.buffer.length() + 100,
                                builder.globalLength);

                        builder.value("abcdefghij");
                        builder.value("ignored");
                    }
                }), "1234567890abcde");
    }

    @Test
    public void testUsesToStringBuilderValueLength() {
        this.buildAndCheck(this.create(5, 10)//
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.valueLength(10);
                        Assert.assertEquals("globalLength", 5, builder.globalLength);
                        Assert.assertEquals("valueLength", 5, builder.valueLength);
                        builder.value("abcdefghij");
                    }
                }), "abcde");
    }

    @Test
    public void testUsesToStringBuilderValueLength2() {
        this.buildAndCheck(this.create()//
                .valueLength(10)//
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.valueLength(5);
                        Assert.assertEquals("globalLength", 10, builder.globalLength);
                        Assert.assertEquals("valueLength", 5, builder.valueLength);

                        builder.value("abcdefghij");
                    }
                }), "abcde");
    }

    @Test
    public void testUsesToStringBuilderValueLength3() {
        this.buildAndCheck(this.create()//
                .valueLength(10)//
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.valueLength(5);
                        Assert.assertEquals("globalLength", 10, builder.globalLength);
                        Assert.assertEquals("valueLength", 5, builder.valueLength);

                        builder.value("abcdefghij");
                        builder.value("12345678");
                    }
                }), "abcde12345");
    }

    @Test
    public void testUsesToStringBuilderValueLengthIncreases() {
        this.buildAndCheck(this.create()//
                .valueLength(10)//
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.valueLength(4);
                        builder.valueLength(5);
                        Assert.assertEquals("globalLength", 10, builder.globalLength);
                        Assert.assertEquals("valueLength", 5, builder.valueLength);

                        builder.value("abcdefghij");
                        builder.value("12345678");
                    }
                }), "abcde12345");
    }

    @Test
    public void testUsesToStringBuilderValueLengthDecreaseIncrease() {
        this.buildAndCheck(this.create()//
                .valueLength(10)//
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.valueLength(4);
                        builder.valueLength(3);
                        builder.valueLength(5);
                        Assert.assertEquals("globalLength", 10, builder.globalLength);
                        Assert.assertEquals("valueLength", 5, builder.valueLength);

                        builder.value("abcdefghij");
                        builder.value("12345678");
                    }
                }), "abcde12345");
    }

    @Test
    public void testUsesToStringBuilderValueLengthAndGlobalLength() {
        this.buildAndCheck(this.create()//
                .globalLength(15)//
                .valueLength(5)//
                .value("1234567") //
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        Assert.assertEquals("valueLength", 5, builder.valueLength);
                        Assert.assertEquals("globalLength",
                                builder.buffer.length() + 5,
                                builder.globalLength);

                        builder.value("abcdefghij"); // expected "abcde" to be added.
                        builder.value("IGNORED");
                    }
                })//
                .value("1234567"), "12345abcde12345");
    }

    @Test
    public void testUsesToStringBuilderValueLengthAndGlobalLength2() {
        this.buildAndCheck(this.create()//
                .globalLength(15)//
                .valueLength(5)//
                .value("1234567") //
                .value(new UsesToStringBuilder() {

                    @Override
                    public void buildToString(final ToStringBuilder builder) {
                        builder.valueLength(6); //
                        Assert.assertEquals("valueLength",
                                5,
                                builder.valueLength); // increase ignored.

                        Assert.assertEquals("globalLength",
                                builder.buffer.length() + 5,
                                builder.globalLength);

                        builder.value("abcdefghij"); // expected "abcde" to be added.
                        builder.value("IGNORED");
                    }
                })//
                .value("1234567"), "12345abcde12345");
    }

    // buildFrom

    @Test
    public void testBuildFromNullFails() {
        try {
            ToStringBuilder.buildFrom(null);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    public void testBuildFrom() {
        final Object result = 1;
        Assert.assertEquals(ToStringBuilder.buildFrom(this.createUsesToStringBuilder(result)),
                result.toString());
    }

    @Test
    public void testToString() {
        Assert.assertEquals(
                "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"=\", valueSeparator=\", \", separator=\" \", valueLength=900, globalLength=1000] 7=\"*value*\"",
                //
                ToStringBuilder.create()//
                        .append("*value*").//
                        toString());
    }

    @Test
    public void testToStringNonStandardSeparators() {
        Assert.assertEquals(
                "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"@\", valueSeparator=\"#\", separator=\"$\", valueLength=900, globalLength=1000] 7=\"*value*\"",
                //
                ToStringBuilder.create()//
                        .labelSeparator("@") //
                        .valueSeparator("#") //
                        .separator("$") //
                        .append("*value*").//
                        toString());
    }

    @Test
    public void testToStringWithEscapedCharacters() {
        Assert.assertEquals(
                "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"=\", valueSeparator=\", \", separator=\" \", valueLength=900, globalLength=1000] 7=\"*tab \\t*\"",
                //
                ToStringBuilder.create()//
                        .append("*tab \t*")//
                        .toString());
    }

    @Override
    protected ToStringBuilder createBuilder() {
        return ToStringBuilder.create();
    }

    private <T> Iterable<T> iterable(final T... items) {
        return Iterables.iterator(this.iterator(items));
    }

    private <T> Iterator<T> iterator(final T... items) {
        return Iterators.array(items);
    }

    private <T> Enumeration<T> enumeration(final T... items) {
        return Enumerations.array(items);
    }

    private Object[] createUsesToStringBuilder(final Object... results) {
        final Object[] uses = new Object[results.length];
        for (int i = 0; i < results.length; i++) {
            uses[i] = this.createUsesToStringBuilder(results[i]);
        }
        return uses;
    }

    private UsesToStringBuilder createUsesToStringBuilder(final Object result) {
        return new UsesToStringBuilder() {

            @Override
            public void buildToString(final ToStringBuilder builder) {
                builder.value(result);
            }
        };
    }

    private void buildAndCheck(final ToStringBuilder builder) {
        this.buildAndCheck(builder, "");
    }

    private void buildAndCheck(final ToStringBuilder builder, final String expected) {
        final String built = builder.build();
        if (false == expected.equals(built)) {
            failNotEquals("options=" + builder.options.toString(),
                    ToStringBuilderTest.format(expected),
                    ToStringBuilderTest.format(built));
        }
    }

    private static String format(final String string) {
        return string.length() + "=" + CharSequences.quoteAndEscape(string);
    }

    /**
     * Creates a {@link ToStringBuilder} with quoting disabled and the separator set to nothing.
     */
    private ToStringBuilder create() {
        return ToStringBuilder.create()//
                .disable(ToStringBuilderOption.QUOTE)//
                .separator("");
    }

    private ToStringBuilder create(final int valueLength, final int globalLength) {
        return this.create()//
                .valueLength(valueLength)//
                .globalLength(globalLength);
    }

    private ToStringBuilder createFull() {
        return this.create(ToStringBuilderTest.FULL.length(), ToStringBuilderTest.FULL.length())
                .append(ToStringBuilderTest.FULL);
    }

    private ToStringBuilder createFullWithLabel() {
        return this.createFull().label("label");
    }

    private void buildAndCheckFull(final ToStringBuilder builder) {
        this.buildAndCheck(builder, ToStringBuilderTest.FULL);
    }
}
