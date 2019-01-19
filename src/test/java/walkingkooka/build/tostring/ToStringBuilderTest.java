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

import org.junit.Test;
import walkingkooka.build.BuilderTestCase;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

final public class ToStringBuilderTest extends BuilderTestCase<ToStringBuilder, String> {
    // constants

    private final static Object NULL = null;

    private final static String FULL = "1234567890";
    
    // tests

    @Test
    public void testCreateAndBuild() {
        this.buildAndCheck(ToStringBuilder.empty());
    }

    @Test
    public void testClassIsFinal() {
        assertTrue(ToStringBuilder.class.toString(),
                Modifier.isFinal(ToStringBuilder.class.getModifiers()));
    }

    @Test
    public void testCheckToStringOverridden() {
        this.checkToStringOverridden(ToStringBuilder.class);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLabelFails() {
        ToStringBuilder.empty().label(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullLabelSeparatorFails() {
        ToStringBuilder.empty().labelSeparator(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueSeparatorFails() {
        ToStringBuilder.empty().valueSeparator(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSeparatorFails() {
        ToStringBuilder.empty().separator(null);

    }

    @Test(expected = NullPointerException.class)
    public void testNullDisableFails() {
        ToStringBuilder.empty().disable(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnableFails() {
        ToStringBuilder.empty().enable(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSurroundValuesNullBeforeFails() {
        ToStringBuilder.empty().surroundValues(null, "after");
    }

    @Test(expected = NullPointerException.class)
    public void testSurroundValuesNullAfterFails() {
        ToStringBuilder.empty().surroundValues("before", null);
    }

    // UsesToStringBuilder

    @Test
    public void testEmptyUsesToStringBuilderIgnoresLabel() {
        this.buildAndCheck(ToStringBuilder.empty()//
                        .label("label")//
                        .value(this.createUsesToStringBuilder("")), //
                "");
    }

    @Test
    public void testEmptyUsesToStringBuilderThenValue() {
        this.buildAndCheck(ToStringBuilder.empty()//
                        .separator("%") //
                        .value(this.createUsesToStringBuilder("")) //
                        .value("next"),//
                "\"next\"");
    }

    // separator

    @Test
    public void testSkipSeparatorForValuesWithoutLabelsWithAppendsBetween() {
        this.buildAndCheck(ToStringBuilder.empty()//
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
        this.buildAndCheck(ToStringBuilder.empty()//
                        .separator("*")//
                        .value(1)//
                        .value(2)//
                        .value(3), //
                "1*2*3");
    }

    @Test
    public void testManyLabelsAndValues() {
        this.buildAndCheck(ToStringBuilder.empty()//
                        .label("label1")//
                        .value(1)//
                        .label("label2")//
                        .value(2), //
                "label1=1 label2=2");
    }

    @Test
    public void testManyLabelsAndValuesChangeSeparator() {
        this.buildAndCheck(ToStringBuilder.empty()//
                        .separator("*")//
                        .label("label1")//
                        .value(1)//
                        .label("label2")//
                        .value(2),//
                "label1=1*label2=2");
    }

    @Test
    public void testManyLabelsIncludingNullValue() {
        this.buildAndCheck(ToStringBuilder.empty()//
                        .separator("*")//
                        .label("label1").value(1)//
                        .label("label2").value(ToStringBuilderTest.NULL)//
                        .label("label3").value(3), //
                "label1=1*label3=3");
    }

    @Test
    public void testValueWithoutLabelAndNullValue() {
        this.buildAndCheck(ToStringBuilder.empty()//
                        .separator("*")//
                        .label("label1").value(1)//
                        .label("label2").value(ToStringBuilderTest.NULL)//
                        .value(3), //
                "label1=1*3");
    }

    @Test
    public void testValueImplementsUsesToStringBuilder() {
        this.buildAndCheck(ToStringBuilder.empty()//
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
        this.buildAndCheck(ToStringBuilder.empty()//
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

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidValueLengthFails() {
        ToStringBuilder.empty().valueLength(0);
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
        final ToStringBuilder builder = ToStringBuilder.empty();
        builder.valueLength(length);
        assertEquals("valueLength", length, builder.valueLength);
    }

    @Test
    public void testValueLength() {
        final ToStringBuilder builder = ToStringBuilder.empty();
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.valueSeparator("");
        builder.separator("");

        builder.valueLength(3);
        builder.value("abcdef");
        builder.value("mnopq");
        builder.value(123456);
        assertEquals("abcmno123", builder.build());
    }

    @Test
    public void testValueLengthBytes() {
        final ToStringBuilder builder = ToStringBuilder.empty();
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.enable(ToStringBuilderOption.HEX_BYTES);
        builder.valueSeparator("");
        builder.separator("");

        builder.valueLength(4);
        builder.value(new byte[]{'A','B', 'C', 'D', 'E'});
        assertEquals("4142", builder.build());
    }

    @Test
    public void testValueLengthShorter() {
        final ToStringBuilder builder = ToStringBuilder.empty();
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.valueSeparator("");
        builder.separator("");

        builder.valueLength(3);
        builder.value("x");
        builder.value(1);
        builder.value(2L);
        assertEquals("x12", builder.build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidGlobalLengthFails() {
        ToStringBuilder.empty().globalLength(0);
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
        final ToStringBuilder builder = ToStringBuilder.empty();
        builder.globalLength(length);
        assertEquals("globalLength", length, builder.globalLength);
    }

    // UsesToStringBuilder

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
                        assertEquals("globalLength", 5, builder.globalLength);
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
                        assertEquals("globalLength",
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
                        assertEquals("globalLength",
                                builder.buffer.length() + 5,
                                builder.globalLength);

                        builder.globalLength(100);
                        assertEquals("globalLength",
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
                        assertEquals("globalLength", 5, builder.globalLength);
                        assertEquals("valueLength", 5, builder.valueLength);
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
                        assertEquals("globalLength", 10, builder.globalLength);
                        assertEquals("valueLength", 5, builder.valueLength);

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
                        assertEquals("globalLength", 10, builder.globalLength);
                        assertEquals("valueLength", 5, builder.valueLength);

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
                        assertEquals("globalLength", 10, builder.globalLength);
                        assertEquals("valueLength", 5, builder.valueLength);

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
                        assertEquals("globalLength", 10, builder.globalLength);
                        assertEquals("valueLength", 5, builder.valueLength);

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
                        assertEquals("valueLength", 5, builder.valueLength);
                        assertEquals("globalLength",
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
                        assertEquals("valueLength",
                                5,
                                builder.valueLength); // increase ignored.

                        assertEquals("globalLength",
                                builder.buffer.length() + 5,
                                builder.globalLength);

                        builder.value("abcdefghij"); // expected "abcde" to be added.
                        builder.value("IGNORED");
                    }
                })//
                .value("1234567"), "12345abcde12345");
    }

    // misc

    @Test
    public void testValueBigDecimal() {
        this.buildAndCheck(this.create()//
                        .value(BigDecimal.valueOf(123.5)),
                "123.5");
    }

    // buildFrom

    @Test(expected = NullPointerException.class)
    public void testBuildFromNullFails() {
        ToStringBuilder.buildFrom(null);
    }

    @Test
    public void testBuildFrom() {
        final Object result = 1;
        assertEquals(ToStringBuilder.buildFrom(this.createUsesToStringBuilder(result)),
                result.toString());
    }

    @Test
    public void testToString() {
        assertEquals(
                "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"=\", valueSeparator=\", \", separator=\" \", valueLength=900, globalLength=1000] 7=\"*value*\"",
                //
                ToStringBuilder.empty()//
                        .append("*value*").//
                        toString());
    }

    @Test
    public void testToStringNonStandardSeparators() {
        assertEquals(
                "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"@\", valueSeparator=\"#\", separator=\"$\", valueLength=900, globalLength=1000] 7=\"*value*\"",
                //
                ToStringBuilder.empty()//
                        .labelSeparator("@") //
                        .valueSeparator("#") //
                        .separator("$") //
                        .append("*value*").//
                        toString());
    }

    @Test
    public void testToStringWithEscapedCharacters() {
        assertEquals(
                "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"=\", valueSeparator=\", \", separator=\" \", valueLength=900, globalLength=1000] 7=\"*tab \\t*\"",
                //
                ToStringBuilder.empty()//
                        .append("*tab \t*")//
                        .toString());
    }

    @Override
    protected ToStringBuilder createBuilder() {
        return ToStringBuilder.empty();
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
            assertEquals("options=" + builder.options.toString(),
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
        return ToStringBuilder.empty()//
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

    @Override
    protected Class<String> builderProductType() {
        return String.class;
    }

    @Override
    protected Class<ToStringBuilder> type() {
        return ToStringBuilder.class;
    }
}
