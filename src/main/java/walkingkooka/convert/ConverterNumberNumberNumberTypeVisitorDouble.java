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

import java.math.BigDecimal;
import java.math.BigInteger;

final class ConverterNumberNumberNumberTypeVisitorDouble extends ConverterNumberNumberNumberTypeVisitorNumber<Double> {

    static ConverterNumberNumberNumberTypeVisitorDouble with() {
        return new ConverterNumberNumberNumberTypeVisitorDouble();
    }

    ConverterNumberNumberNumberTypeVisitorDouble() {
        super();
    }

    @Override
    protected void visit(final BigDecimal number) {
        final double converted = number.doubleValue();
        if (0 != new BigDecimal(converted).compareTo(number)) {
            this.failConversion(number);
        }
        this.save(converted);
    }

    @Override 
    protected void visit(final BigInteger number) {
        final double converted = number.doubleValue();
        if (!new BigDecimal(converted).toBigInteger().equals(number)) {
            this.failConversion(number);
        }
        this.save(converted);
    }

    @Override
    protected void visit(final Byte number) {
        this.saveDouble(number);
    }

    @Override
    protected void visit(final Double number) {
        this.save(number);
    }

    @Override 
    protected void visit(final Float number) {
        this.saveDouble(number);
    }

    @Override 
    protected void visit(final Integer number) {
        this.saveDouble(number);
    }

    @Override 
    protected void visit(final Long number) {
        final double converted = number.doubleValue();
        if ((long)converted != number) {
            this.failConversion(number);
        }
        this.save(converted);
    }

    @Override 
    protected void visit(final Short number) {
        this.saveDouble(number);
    }

    private void saveDouble(final Number number) {
        this.save(number.doubleValue());
    }

    @Override
    Class<Double> targetType() {
        return Double.class;
    }
}
