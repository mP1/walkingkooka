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

import org.junit.Test;

public final class FloatHeaderValueConverterTest extends
        HeaderValueConverterTestCase<FloatHeaderValueConverter, Float> {

    @Override
    protected String requiredPrefix() {
        return Float.class.getSimpleName();
    }

    @Test
    public void testFloat() {
        this.parseAndFormatAndCheck("123.5", 123.5f);
    }

    protected @Override
    FloatHeaderValueConverter converter() {
        return FloatHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HeaderValueTokenParameterName name() {
        return HeaderValueTokenParameterName.Q;
    }

    @Override
    protected String invalidHeaderValue() {
        return "abc";
    }

    @Override
    protected Float value() {
        return 123.5f;
    }

    @Override
    protected String converterToString() {
        return Float.class.getSimpleName();
    }

    @Override
    protected Class<FloatHeaderValueConverter> type() {
        return FloatHeaderValueConverter.class;
    }
}
