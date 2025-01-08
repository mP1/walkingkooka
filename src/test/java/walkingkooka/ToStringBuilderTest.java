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

package walkingkooka;

import org.junit.jupiter.api.Test;
import walkingkooka.build.BuilderTesting;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final public class ToStringBuilderTest extends ToStringBuilderTestCase<ToStringBuilder>
    implements BuilderTesting<ToStringBuilder, String> {
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
        assertTrue(Modifier.isFinal(ToStringBuilder.class.getModifiers()), ToStringBuilder.class.toString());
    }

    @Test
    public void testNullLabelFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().label(null));
    }

    @Test
    public void testNullLabelSeparatorFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().labelSeparator(null));
    }

    @Test
    public void testNullValueSeparatorFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().valueSeparator(null));
    }

    @Test
    public void testNullSeparatorFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().separator(null));
    }

    @Test
    public void testNullDisableFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().disable(null));
    }

    @Test
    public void testNullEnableFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().enable(null));
    }

    @Test
    public void testSurroundValuesNullBeforeFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().surroundValues(null, "after"));
    }

    @Test
    public void testSurroundValuesNullAfterFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.empty().surroundValues("before", null));
    }

    // defaults .........................................................................................................

    @Test
    public void testDefaults() {
        final String built = ToStringBuilder.empty()
            .label("label1")
            .value("value2")
            .label("label2").value(Lists.of(1, 2, 3))
            .build();

        final ToStringBuilder builder = ToStringBuilder.empty()
            .labelSeparator("xxx")
            .valueSeparator("yyyy")
            .separator("zzzz");
        assertSame(builder, builder.defaults());

        builder.label("label1")
            .value("value2")
            .label("label2").value(Lists.of(1, 2, 3));

        this.buildAndCheck(builder, built);
    }

    // separator........................................................................................................

    @Test
    public void testValueAppendValueAppendValueSkipsSeparatorBecauseOfAppends() {
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
    public void testValueValueValueInsertsSeparator() {
        this.buildAndCheck(ToStringBuilder.empty()//
                .separator("*")//
                .value(1)//
                .value(2)//
                .value(3), //
            "1*2*3");
    }

    @Test
    public void testLabelValueLabelValueInsertsDefaultSeparatorBetween() {
        this.buildAndCheck(ToStringBuilder.empty()//
                .label("label1")//
                .value(1)//
                .label("label2")//
                .value(2), //
            "label1=1 label2=2");
    }

    @Test
    public void testLabelValueLabelValue2() {
        this.buildAndCheck(ToStringBuilder.empty()//
                .separator("*")//
                .label("label1")//
                .value(1)//
                .label("label2")//
                .value(2),//
            "label1=1*label2=2");
    }

    @Test
    public void testLabelValueLabelNullValueLabelValue() {
        this.buildAndCheck(ToStringBuilder.empty()//
                .separator("*")//
                .label("label1").value(1)//
                .label("label2").value(NULL)//
                .label("label3").value(3), //
            "label1=1*label3=3");
    }

    // valueLength and globalLength....................................................................................

    @Test
    public void testInvalidValueLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> ToStringBuilder.empty().valueLength(0));
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
        this.checkEquals(length, builder.valueLength, "valueLength");
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
        this.checkEquals("abcmno123", builder.build());
    }

    @Test
    public void testValueLengthBytes() {
        final ToStringBuilder builder = ToStringBuilder.empty();
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.enable(ToStringBuilderOption.HEX_BYTES);
        builder.valueSeparator("");
        builder.separator("");

        builder.valueLength(4);
        builder.value(new byte[]{'A', 'B', 'C', 'D', 'E'});
        this.checkEquals("4142", builder.build());
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
        this.checkEquals("x12", builder.build());
    }

    @Test
    public void testInvalidGlobalLengthFails() {
        assertThrows(IllegalArgumentException.class, () -> ToStringBuilder.empty().globalLength(0));
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
        this.checkEquals(length, builder.globalLength, "globalLength");
    }

    // Optional.........................................................................................................

    @Test
    public void testOptionalEmpty() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(Optional.empty());

        this.buildAndCheck(
            builder,
            "label1="
        );
    }

    @Test
    public void testOptionalEmptySkipIfDefaultValue() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(Optional.empty());

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalNull() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value((Object) null);

        this.buildAndCheck(
            builder,
            "label1=null"
        );
    }

    @Test
    public void testOptionalNonNull() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .label("label1")
            .value("hello");

        this.buildAndCheck(
            builder,
            "label1=\"hello\""
        );
    }

    @Test
    public void testOptionalBooleanFalse() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(false);

        this.buildAndCheck(
            builder,
            "label1=false"
        );
    }

    @Test
    public void testOptionalBooleanTrue() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(true);

        this.buildAndCheck(
            builder,
            "label1=true"
        );
    }

    @Test
    public void testOptionalNullSkipDefaults() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value((Object) null);

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalFalseSkipDefaults() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(false);

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalTrueSkipDefaults() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(true);

        this.buildAndCheck(
            builder,
            "label1=true"
        );
    }

    @Test
    public void testOptionalZeroSkipDefaults() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(0);

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalNonZeroSkipDefaults() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(123);

        this.buildAndCheck(
            builder,
            "label1=123"
        );
    }

    @Test
    public void testOptionalWithoutLabelZeroSkipDefaults() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .value(0);

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalWithoutLabelNonZeroSkipDefaults() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .value(123);

        this.buildAndCheck(
            builder,
            "123"
        );
    }

    // OptionalDouble...................................................................................................

    @Test
    public void testOptionalDoubleEmpty() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalDouble.empty());

        this.buildAndCheck(
            builder,
            "label1="
        );
    }

    @Test
    public void testOptionalDoubleZero() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalDouble.of(0.0));

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalDoubleZeroSkipIfDefaultDisabled() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalDouble.of(0.0));

        this.buildAndCheck(
            builder,
            "label1=0.0"
        );
    }

    @Test
    public void testOptionalDoubleNonZero() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalDouble.of(123.5));

        this.buildAndCheck(
            builder,
            "label1=123.5"
        );
    }

    // OptionalInt...................................................................................................

    @Test
    public void testOptionalIntEmpty() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalInt.empty());

        this.buildAndCheck(
            builder,
            "label1="
        );
    }

    @Test
    public void testOptionalIntZero() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalInt.of(0));

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalIntZeroSkipIfDefaultDisabled() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalInt.of(0));

        this.buildAndCheck(
            builder,
            "label1=0"
        );
    }

    @Test
    public void testOptionalIntNonZero() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalInt.of(123));

        this.buildAndCheck(
            builder,
            "label1=123"
        );
    }

    // OptionalLong...................................................................................................

    @Test
    public void testOptionalLongEmpty() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalLong.empty());

        this.buildAndCheck(
            builder,
            "label1="
        );
    }

    @Test
    public void testOptionalLongZero() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalLong.of(0));

        this.buildAndCheck(
            builder,
            ""
        );
    }

    @Test
    public void testOptionalLongZeroSkipIfDefaultDisabled() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalLong.of(0));

        this.buildAndCheck(
            builder,
            "label1=0"
        );
    }

    @Test
    public void testOptionalLongNonZero() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .enable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
            .label("label1")
            .value(OptionalLong.of(123));

        this.buildAndCheck(
            builder,
            "label1=123"
        );
    }

    // UsesToStringBuilder..............................................................................................

    @Test
    public void testFullLabelValueUsesToStringBuilder() {
        this.buildAndCheckFull(this.createFullWithLabel()//
            .value(this.usesToStringBuilder("1")));
    }

    @Test
    public void testGlobalLengthValueFullValueUsesToStringBuilder() {
        this.buildAndCheckFull(this.createFullWithLabel()//
            .value(builder -> {
                builder.globalLength(5);
                this.checkEquals(5, builder.globalLength, "globalLength");
                builder.value("IGNORED!");
            }));
    }

    @Test
    public void testGlobalLengthValueFullValueUsesToStringBuilder2() {
        this.buildAndCheck(this.builder()//
            .globalLength(15)//
            .value("1234567890") //
            .value(builder -> {
                builder.globalLength(100);
                this.checkEquals(builder.buffer.length() + 100,
                    builder.globalLength,
                    "globalLength");
                builder.value("abcdefghij");
                builder.value("ignored");
            }), "1234567890abcde");
    }

    @Test
    public void testGlobalLengthValueFullValueUsesToStringBuilder3() {
        this.buildAndCheck(this.builder()//
            .globalLength(15)//
            .value("1234567890") //
            .value(builder -> {
                builder.globalLength(5);
                this.checkEquals(builder.buffer.length() + 5,
                    builder.globalLength,
                    "globalLength");

                builder.globalLength(100);
                this.checkEquals(builder.buffer.length() + 100,
                    builder.globalLength,
                    "globalLength");

                builder.value("abcdefghij");
                builder.value("ignored");
            }), "1234567890abcde");
    }

    @Test
    public void testGlobalLengthValueLengthValueFullValueUsesToStringBuilder() {
        this.buildAndCheck(this.builder(5, 10)//
            .value(builder -> {
                builder.valueLength(10);
                this.checkEquals(5, builder.globalLength, "globalLength");
                this.checkEquals(5, builder.valueLength, "valueLength");
                builder.value("abcdefghij");
            }), "abcde");
    }

    @Test
    public void testGlobalLengthValueLengthValueFullValueUsesToStringBuilder2() {
        this.buildAndCheck(this.builder()//
            .valueLength(10)//
            .value(builder -> {
                builder.valueLength(5);
                this.checkEquals(10, builder.globalLength, "globalLength");
                this.checkEquals(5, builder.valueLength, "valueLength");

                builder.value("abcdefghij");
            }), "abcde");
    }

    @Test
    public void testGlobalLengthValueLengthValueFullValueUsesToStringBuilder3() {
        this.buildAndCheck(this.builder()//
            .valueLength(10)//
            .value(builder -> {
                builder.valueLength(5);
                this.checkEquals(10, builder.globalLength, "globalLength");
                this.checkEquals(5, builder.valueLength, "valueLength");

                builder.value("abcdefghij");
                builder.value("12345678");
            }), "abcde12345");
    }

    @Test
    public void testValueLengthUsesToStringBuilderValueLengthIncreases() {
        this.buildAndCheck(this.builder()//
            .valueLength(10)//
            .value(builder -> {
                builder.valueLength(4);
                builder.valueLength(5);
                this.checkEquals(10, builder.globalLength, "globalLength");
                this.checkEquals(5, builder.valueLength, "valueLength");

                builder.value("abcdefghij");
                builder.value("12345678");
            }), "abcde12345");
    }

    @Test
    public void testValueLengthUsesToStringBuilderValueLengthDecreaseIncrease() {
        this.buildAndCheck(this.builder()//
            .valueLength(10)//
            .value(builder -> {
                builder.valueLength(4);
                builder.valueLength(3);
                builder.valueLength(5);
                this.checkEquals(10, builder.globalLength, "globalLength");
                this.checkEquals(5, builder.valueLength, "valueLength");

                builder.value("abcdefghij");
                builder.value("12345678");
            }), "abcde12345");
    }

    @Test
    public void testGlobalLengthValueLengthValueValueUsesToStringBuilderChops() {
        this.buildAndCheck(this.builder()//
            .globalLength(15)//
            .valueLength(5)//
            .value("1234567") //
            .value(builder -> {
                this.checkEquals(5, builder.valueLength, "valueLength");
                this.checkEquals(builder.buffer.length() + 5,
                    builder.globalLength,
                    "globalLength");

                builder.value("abcdefghij"); // expected "abcde" to be added.
                builder.value("IGNORED");
            })//
            .value("1234567"), "12345abcde12345");
    }

    @Test
    public void testGlobalLengthValueLengthValueValueUsesToStringBuilderChops2() {
        this.buildAndCheck(this.builder()//
            .globalLength(15)//
            .valueLength(5)//
            .value("1234567") //
            .value(builder -> {
                builder.valueLength(6); //
                this.checkEquals(5, builder.valueLength, "valueLength"); // increase ignored.

                this.checkEquals(builder.buffer.length() + 5, builder.globalLength, "globalLength");

                builder.value("abcdefghij"); // expected "abcde" to be added.
                builder.value("IGNORED");
            })//
            .value("1234567"), "12345abcde12345");
    }

    // special cases etc................................................................................................

    @Test
    public void testValueBigDecimal() {
        this.buildAndCheck(this.builder()//
                .value(BigDecimal.valueOf(123.5)),
            "123.5");
    }

    @Test
    public void testValueEnumerationNotConsumed() {
        final Enumeration<String> enumeration = new Enumeration<>() {
            @Override
            public boolean hasMoreElements() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String nextElement() {
                throw new UnsupportedOperationException();
            }

            @Override
            public String toString() {
                return "Enumeration123";
            }
        };
        this.buildAndCheck(this.builder()//
                .value(enumeration),
            enumeration.toString());
    }

    @Test
    public void testValueIteratorNotConsumed() {
        final Iterator<String> iterator = Iterators.fake();
        this.buildAndCheck(this.builder()//
                .value(iterator),
            iterator.toString());
    }

    // buildFrom

    @Test
    public void testBuildFromNullFails() {
        assertThrows(NullPointerException.class, () -> ToStringBuilder.buildFrom(null));
    }

    @Test
    public void testBuildFrom() {
        final Object result = 1;
        this.checkEquals(ToStringBuilder.buildFrom(this.usesToStringBuilder(result)),
            result.toString());
    }

    // ToString.......................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(ToStringBuilder.empty()//
                .append("*value*"),
            "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"=\", valueSeparator=\", \", separator=\" \", valueLength=900, globalLength=1000] 7=\"*value*\"");
    }

    @Test
    public void testToStringNonStandardSeparators() {
        this.toStringAndCheck(ToStringBuilder.empty()//
                .labelSeparator("@") //
                .valueSeparator("#") //
                .separator("$") //
                .append("*value*"),
            "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"@\", valueSeparator=\"#\", separator=\"$\", valueLength=900, globalLength=1000] 7=\"*value*\"");
    }

    @Test
    public void testToStringWithEscapedCharacters() {
        this.toStringAndCheck(ToStringBuilder.empty()//
                .append("*tab \t*"),
            "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"=\", valueSeparator=\", \", separator=\" \", valueLength=900, globalLength=1000] 7=\"*tab \\t*\"");
    }

    @Test
    public void testToStringFull() {
        this.toStringAndCheck(ToStringBuilder.empty()//
                .append("1234567890abcdef")
                .globalLength(10),
            "[QUOTE, INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE, labelSeparator=\"=\", valueSeparator=\", \", separator=\" \", FULL] 16=\"1234567890abcdef\"");
    }

    @Override
    public ToStringBuilder createBuilder() {
        return ToStringBuilder.empty();
    }

    private UsesToStringBuilder usesToStringBuilder(final Object value) {
        return this.usesToStringBuilder(null, value);
    }

    private UsesToStringBuilder usesToStringBuilder(final String label, final Object value) {
        return builder -> {
            if (null != label) {
                builder.label(label);
            }
            builder.value(value);
        };
    }

    private void buildAndCheck(final ToStringBuilder builder) {
        this.buildAndCheck(builder, "");
    }

    private void buildAndCheck(final ToStringBuilder builder, final String expected) {
        final String built = builder.build();
        if (false == expected.equals(built)) {
            this.checkEquals(format(expected),
                format(built),
                () -> "options=" + builder.options.toString());
        }
    }

    private static String format(final String string) {
        return string.length() + "=" + CharSequences.quoteAndEscape(string);
    }

    /**
     * Creates a {@link ToStringBuilder} with quoting disabled and the separator set to nothing.
     */
    private ToStringBuilder builder() {
        return ToStringBuilder.empty()//
            .disable(ToStringBuilderOption.QUOTE)//
            .separator("");
    }

    private ToStringBuilder builder(final int valueLength, final int globalLength) {
        return this.builder()//
            .valueLength(valueLength)//
            .globalLength(globalLength);
    }

    private ToStringBuilder builderFull() {
        return this.builder(FULL.length(), FULL.length())
            .append(FULL);
    }

    private ToStringBuilder createFullWithLabel() {
        return this.builderFull().label("label");
    }

    private void buildAndCheckFull(final ToStringBuilder builder) {
        this.buildAndCheck(builder, FULL);
    }

    // BuilderTesting...................................................................................................

    @Override
    public Class<ToStringBuilder> type() {
        return ToStringBuilder.class;
    }

    @Override
    public Class<String> builderProductType() {
        return String.class;
    }

    // ClassTesting.....................................................................................................

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
