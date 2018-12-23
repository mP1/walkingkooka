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
import walkingkooka.net.email.EmailAddress;

public final class ContentDispositionFileNameHeaderValueConverterTest extends
        HeaderValueConverterTestCase<ContentDispositionFileNameHeaderValueConverter, ContentDispositionFileName> {
    @Override
    protected String requiredPrefix() {
        return ContentDispositionFileName.class.getSimpleName();
    }

    @Test
    public void testFilename() {
        final String filename = "readme.txt";
        this.parseAndToTextAndCheck(filename, ContentDispositionFileName.with(filename));
    }

    @Override
    protected ContentDispositionFileNameHeaderValueConverter converter() {
        return ContentDispositionFileNameHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<EmailAddress> name() {
        return HttpHeaderName.FROM;
    }

    @Override
    protected String invalidHeaderValue() {
        return "\0";
    }

    @Override
    protected ContentDispositionFileName value() {
        return ContentDispositionFileName.with("readme.txt");
    }

    @Override
    protected String converterToString() {
        return ContentDispositionFileName.class.getSimpleName();
    }

    @Override
    protected Class<ContentDispositionFileNameHeaderValueConverter> type() {
        return ContentDispositionFileNameHeaderValueConverter.class;
    }
}
