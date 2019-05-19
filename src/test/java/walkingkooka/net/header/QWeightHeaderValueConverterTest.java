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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class QWeightHeaderValueConverterTest extends
        HeaderValueConverterTestCase<QWeightHeaderValueConverter, Float> {

    @Override
    public String typeNamePrefix() {
        return "QWeight";
    }

    @Test
    public void testParseNegativeFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.parse("-0.1");
        });
    }

    @Test
    public void testMoreThanOneFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.parse("1.01");
        });
    }

    @Test
    public void testZero() {
        this.parseAndToTextAndCheck("0.0", 0f);
    }

    @Test
    public void testHalf() {
        this.parseAndToTextAndCheck("0.5", 0.5f);
    }

    @Test
    public void testOne() {
        this.parseAndToTextAndCheck("1.0", 1.0f);
    }

    @Override
    QWeightHeaderValueConverter converter() {
        return QWeightHeaderValueConverter.INSTANCE;
    }

    @Override
    TokenHeaderValueParameterName name() {
        return TokenHeaderValueParameterName.Q;
    }

    @Override
    String invalidHeaderValue() {
        return "abc";
    }

    @Override
    Float value() {
        return 0.25f;
    }

    @Override
    String valueType() {
        return this.valueType(Float.class);
    }

    @Override
    String converterToString() {
        return "QWeight";
    }

    @Override
    public Class<QWeightHeaderValueConverter> type() {
        return QWeightHeaderValueConverter.class;
    }
}
