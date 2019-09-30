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
import walkingkooka.math.NumberVisitor;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * {@link NumberVisitor} that dispatches based on the {@link Number} type back to the {@link ConverterNumber} to convert.
 */
final class ConverterNumberNumberVisitor<T> extends NumberVisitor {

    static <T> T convert(final ConverterNumber<T> converter,
                         final Number number,
                         final Class<T> type) {
        final ConverterNumberNumberVisitor<T> visitor = new ConverterNumberNumberVisitor<>(converter, type);
        visitor.accept(number);
        return visitor.value;
    }

    ConverterNumberNumberVisitor(final ConverterNumber<T> converter, final Class<T> type) {
        super();
        this.converter = converter;
        this.type = type;
    }

    @Override
    protected void visit(final BigDecimal number) {
        this.value = this.converter.bigDecimal(number);
    }

    @Override
    protected void visit(final BigInteger number) {
        this.value = this.converter.bigInteger(number);
    }

    @Override
    protected void visit(final Byte number) {
        this.value = this.converter.number(number);
    }

    @Override
    protected void visit(final Double number) {
        this.value = this.converter.doubleValue(number);
    }

    @Override
    protected void visit(final Float number) {
        this.value = this.converter.floatValue(number);
    }

    @Override
    protected void visit(final Integer number) {
        this.value = this.converter.number(number);
    }

    @Override
    protected void visit(final Long number) {
        this.value = this.converter.longValue(number);
    }

    @Override
    protected void visit(final Short number) {
        this.value = this.converter.number(number);
    }

    @Override
    protected void visitUnknown(final Number number) {
        this.converter.failConversion(number, this.type);
    }

    private final ConverterNumber<T> converter;

    private T value;
    private final Class<T> type;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.converter)
                .value(this.value)
                .value(this.type.getName())
                .build();
    }
}
