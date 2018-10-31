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

package walkingkooka.tree.expression;

import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public final class ExpressionNodeTest extends PublicClassTestCase<ExpressionNode> {

    @Test(expected = NullPointerException.class)
    public void testValueOrFailNullFails() {
        ExpressionNode.valueOrFail(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOrFailUnknownValueTypeFails() {
        ExpressionNode.valueOrFail(this);
    }

    @Test
    public void testValueOrFailBigInteger() {
        this.valueOrFailAndCheck(BigInteger.valueOf(123), ExpressionBigIntegerNode.class);
    }

    @Test
    public void testValueOrFailBigDecimal() {
        this.valueOrFailAndCheck(BigDecimal.valueOf(123.5), ExpressionBigDecimalNode.class);
    }

    @Test
    public void testValueOrFailBooleanTrue() {
        this.valueOrFailAndCheck(true, ExpressionBooleanNode.class);
    }

    @Test
    public void testValueOrFailBooleanFalse() {
        this.valueOrFailAndCheck(false, ExpressionBooleanNode.class);
    }

    @Test
    public void testValueOrFailFloat() {
        this.valueOrFailAndCheck(123.5f, ExpressionDoubleNode.class, 123.5);
    }

    @Test
    public void testValueOrFailDouble() {
        this.valueOrFailAndCheck(123.5, ExpressionDoubleNode.class);
    }

    @Test
    public void testValueOrFailByte() {
        this.valueOrFailAndCheck((byte) 123, ExpressionLongNode.class, 123L);
    }

    @Test
    public void testValueOrFailShort() {
        this.valueOrFailAndCheck((short) 123, ExpressionLongNode.class, 123L);
    }

    @Test
    public void testValueOrFailInteger() {
        this.valueOrFailAndCheck(123, ExpressionLongNode.class, 123L);
    }

    @Test
    public void testValueOrFailLong() {
        this.valueOrFailAndCheck(123L, ExpressionLongNode.class);
    }

    @Test
    public void testValueOrFailLocalDate() {
        this.valueOrFailAndCheck(LocalDate.of(2000, 12, 31), ExpressionLocalDateNode.class);
    }

    @Test
    public void testValueOrFailLocalDateTime() {
        this.valueOrFailAndCheck(LocalDateTime.of(2000, 12, 31, 12, 58, 59),
                ExpressionLocalDateTimeNode.class);
    }

    @Test
    public void testValueOrFailLocalTime() {
        this.valueOrFailAndCheck(LocalTime.of(12, 58, 59), ExpressionLocalTimeNode.class);
    }

    @Test
    public void testValueOrFailText() {
        this.valueOrFailAndCheck("abc123", ExpressionTextNode.class);
    }

    private void valueOrFailAndCheck(final Object value, final Class<? extends ExpressionValueNode> type) {
        valueOrFailAndCheck(value, type, value);
    }

    private void valueOrFailAndCheck(final Object value, final Class<? extends ExpressionValueNode> type, final Object expected) {
        final ExpressionNode node = ExpressionNode.valueOrFail(value);
        assertEquals("node type of " + value, type, node.getClass());
        assertEquals("value", expected, type.cast(node).value());
    }

    @Override
    protected Class<ExpressionNode> type() {
        return ExpressionNode.class;
    }
}
