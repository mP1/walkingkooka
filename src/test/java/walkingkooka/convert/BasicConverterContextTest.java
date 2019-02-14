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

package walkingkooka.convert;

import org.junit.jupiter.api.Test;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BasicConverterContextTest implements ClassTesting2<BasicConverterContext>,
        ConverterContextTesting<BasicConverterContext> {

    private final static String CURRENCY = "$$";
    private final static char DECIMAL = 'D';
    private final static char EXPONENT = 'X';
    private final static char GROUPING = 'G';
    private final static char MINUS = 'M';
    private final static char PERCENTAGE = 'R';
    private final static char PLUS = 'P';

    @Test
    public void testWithNullBasicFails() {
        assertThrows(NullPointerException.class, () -> {
            BasicConverterContext.with(null);
        });
    }

    @Test
    public void testWith() {
        final BasicConverterContext context = this.createContext();
        this.checkDecimalPoint(context, DECIMAL);
        this.checkExponentSymbol(context, EXPONENT);
        this.checkMinusSign(context, MINUS);
        this.checkPlusSign(context, PLUS);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), this.basic().toString());
    }

    @Override
    public BasicConverterContext createContext() {
        return BasicConverterContext.with(this.basic());
    }

    private BasicConverterContext basic() {
        return BasicConverterContext.with(DecimalNumberContexts.basic(CURRENCY, DECIMAL, EXPONENT, GROUPING, MINUS, PERCENTAGE, PLUS));
    }

    @Override
    public Class<BasicConverterContext> type() {
        return BasicConverterContext.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
