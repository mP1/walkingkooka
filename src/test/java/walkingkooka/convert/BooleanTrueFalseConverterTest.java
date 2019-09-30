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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class BooleanTrueFalseConverterTest extends ConverterTestCase2<BooleanTrueFalseConverter> {

    private final static Class<Integer> SOURCE_TYPE = Integer.class;
    private final static Integer FALSE_VALUE = 1;
    private final static Class<String> TARGET_TYPE = String.class;
    private final static String TRUE_ANSWER = "true!!";
    private final static String FALSE_ANSWER = "false!!";

    @Test
    public void testWithNullSourceTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanTrueFalseConverter.with(null, FALSE_VALUE, TARGET_TYPE, TRUE_ANSWER, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullFalseValueFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanTrueFalseConverter.with(SOURCE_TYPE, null, TARGET_TYPE, TRUE_ANSWER, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullTargetTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanTrueFalseConverter.with(SOURCE_TYPE, FALSE_VALUE, null, TRUE_ANSWER, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullTrueAnswerFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanTrueFalseConverter.with(SOURCE_TYPE, FALSE_VALUE, TARGET_TYPE, null, FALSE_ANSWER);
        });
    }

    @Test
    public void testWithNullFalseAnswerFails() {
        assertThrows(NullPointerException.class, () -> {
            BooleanTrueFalseConverter.with(SOURCE_TYPE, FALSE_VALUE, TARGET_TYPE, TRUE_ANSWER, null);
        });
    }

    @Test
    public void testTrue() {
        this.convertAndCheck2(2, TRUE_ANSWER);
    }

    @Test
    public void testFalse() {
        this.convertAndCheck2(1, FALSE_ANSWER);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createConverter(),
                SOURCE_TYPE.getSimpleName() + "->" + TARGET_TYPE.getSimpleName());
    }

    @Override
    public BooleanTrueFalseConverter createConverter() {
        return BooleanTrueFalseConverter.with(SOURCE_TYPE, FALSE_VALUE, TARGET_TYPE, TRUE_ANSWER, FALSE_ANSWER);
    }

    @Override
    public ConverterContext createContext() {
        return ConverterContexts.fake();
    }

    final void convertAndCheck2(final Object value,
                                final String expected) {
        this.convertAndCheck(this.createConverter(),
                value,
                String.class,
                expected);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<BooleanTrueFalseConverter> type() {
        return BooleanTrueFalseConverter.class;
    }
}
