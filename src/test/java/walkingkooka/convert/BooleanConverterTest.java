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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BooleanConverterTest extends FixedTypeConverterTestCase<BooleanConverter, String> {

    private final static Class<Integer> SOURCE_TYPE = Integer.class;
    private final static Integer FALSE_VALUE = 1;
    private final static Class<String> TARGET_TYPE = String.class;
    private final static String TRUE_ANSWER = "true!!";
    private final static String FALSE_ANSWER = "false!!";

    @Test
    public void testWithNullSourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanConverter.with(null, FALSE_VALUE,TARGET_TYPE, TRUE_ANSWER, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullFalseValueFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanConverter.with(SOURCE_TYPE, null,TARGET_TYPE, TRUE_ANSWER, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanConverter.with(SOURCE_TYPE, FALSE_VALUE,null, TRUE_ANSWER, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullTrueAnswerFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanConverter.with(SOURCE_TYPE, FALSE_VALUE, TARGET_TYPE,null, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullFalseAnswerFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanConverter.with(SOURCE_TYPE, FALSE_VALUE, TARGET_TYPE, TRUE_ANSWER, null);
        });
    }

    @Test
    public void testTrue() {
        this.convertAndCheck(2, TRUE_ANSWER);
    }

    @Test
    public void testFalse() {
        this.convertAndCheck(1, FALSE_ANSWER);
    }

    @Test
    public void testToString() {
        assertEquals(SOURCE_TYPE.getSimpleName() + "->" + TARGET_TYPE.getSimpleName(), this.createConverter().toString());
    }

    @Override
    protected BooleanConverter createConverter() {
        return BooleanConverter.with(SOURCE_TYPE, FALSE_VALUE, TARGET_TYPE, TRUE_ANSWER, FALSE_ANSWER);
    }

    @Override
    protected ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    @Override
    protected Class<String> onlySupportedType() {
        return String.class;
    }

    @Override
    protected Class<BooleanConverter> type() {
        return BooleanConverter.class;
    }
}
