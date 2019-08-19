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

package walkingkooka.convert;

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.math.NumberTypeVisitor;
import walkingkooka.math.NumberVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * A {@link NumberVisitor} that converts {@link Number} to a {@link Boolean} equivalent.
 */
final class BooleanConverterNumberNumberTypeVisitor extends NumberTypeVisitor {

    static Number convert(final Boolean value,
                          final Class<?> type) {
        final BooleanConverterNumberNumberTypeVisitor visitor = new BooleanConverterNumberNumberTypeVisitor(value);
        visitor.accept(type);
        return visitor.number;
    }

    BooleanConverterNumberNumberTypeVisitor(final boolean booleanValue) {
        super();
        this.booleanValue = booleanValue;
    }

    @Override
    protected void visitBigDecimal() {
        this.number = this.booleanValue ? BigDecimal.ONE : BigDecimal.ZERO;
    }

    @Override
    protected void visitBigInteger() {
        this.number = this.booleanValue ? BigInteger.ONE : BigInteger.ZERO;
    }

    @Override
    protected void visitByte() {
        this.number = this.booleanValue ? BYTE_TRUE : BYTE_FALSE;
    }

    private final static Byte BYTE_TRUE = (byte) 1;
    private final static Byte BYTE_FALSE = (byte) 0;

    @Override
    protected void visitDouble() {
        this.number = this.booleanValue ? DOUBLE_TRUE : DOUBLE_FALSE;
    }

    private final static Double DOUBLE_TRUE = 1.0;
    private final static Double DOUBLE_FALSE = 0.0;

    @Override
    protected void visitFloat() {
        this.number = this.booleanValue ? FLOAT_TRUE : FLOAT_FALSE;
    }

    private final static Float FLOAT_TRUE = 1.0f;
    private final static Float FLOAT_FALSE = 0.0f;

    @Override
    protected void visitInteger() {
        this.number = this.booleanValue ? INTEGER_TRUE : INTEGER_FALSE;
    }

    private final static Integer INTEGER_TRUE = 1;
    private final static Integer INTEGER_FALSE = 0;

    @Override
    protected void visitLong() {
        this.number = this.booleanValue ? LONG_TRUE : LONG_FALSE;
    }

    private final static Long LONG_TRUE = 1L;
    private final static Long LONG_FALSE = 0L;

    @Override
    protected void visitShort() {
        this.number = this.booleanValue ? SHORT_TRUE : SHORT_FALSE;
    }

    private final static Short SHORT_TRUE = (short) 1;
    private final static Short SHORT_FALSE = (short) 0;

    Number number;

    @Override
    protected void visitUnknown(final Class<?> numberType) {
        throw new FailedConversionException(this.booleanValue, numberType);
    }

    /**
     * The source boolean value
     */
    private final boolean booleanValue;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.QUOTE)
                .valueSeparator(" ")
                .value("Boolean->Number")
                .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .value(this.booleanValue)
                .value(this.number)
                .build();
    }
}
