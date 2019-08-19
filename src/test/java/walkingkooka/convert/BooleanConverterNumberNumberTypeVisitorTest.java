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

import org.junit.jupiter.api.Test;
import walkingkooka.math.NumberTypeVisitorTesting;
import walkingkooka.type.JavaVisibility;

public final class BooleanConverterNumberNumberTypeVisitorTest implements NumberTypeVisitorTesting<BooleanConverterNumberNumberTypeVisitor> {

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createVisitor(), "Boolean->Number true null");
    }

    @Test
    public void testToString2() {
        final BooleanConverterNumberNumberTypeVisitor visitor = new BooleanConverterNumberNumberTypeVisitor(true);
        visitor.number = 123;
        this.toStringAndCheck(visitor, "Boolean->Number true 123");
    }

    @Test
    public void testToStringFalseZero() {
        final BooleanConverterNumberNumberTypeVisitor visitor = new BooleanConverterNumberNumberTypeVisitor(false);
        visitor.number = 0;
        this.toStringAndCheck(visitor, "Boolean->Number false 0");
    }

    @Override
    public BooleanConverterNumberNumberTypeVisitor createVisitor() {
        return new BooleanConverterNumberNumberTypeVisitor(true);
    }

    @Override
    public Class<BooleanConverterNumberNumberTypeVisitor> type() {
        return BooleanConverterNumberNumberTypeVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return BooleanConverterNumberNumberTypeVisitor.class.getSimpleName();
    }
}
