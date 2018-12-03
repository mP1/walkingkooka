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
import walkingkooka.net.http.HttpHeaderName;

public final class ContentDispositionFilenameHeaderValueConverterTest extends
        HeaderValueConverterTestCase<ContentDispositionFilenameHeaderValueConverter, ContentDispositionFilename> {
    @Override
    protected String requiredPrefix() {
        return ContentDispositionFilename.class.getSimpleName();
    }

    @Test
    public void testFilename() {
        final String filename = "readme.txt";
        this.parseAndToTextAndCheck(filename, ContentDispositionFilename.with(filename));
    }

    @Override
    protected ContentDispositionFilenameHeaderValueConverter converter() {
        return ContentDispositionFilenameHeaderValueConverter.INSTANCE;
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
    protected ContentDispositionFilename value() {
        return ContentDispositionFilename.with("readme.txt");
    }

    @Override
    protected String converterToString() {
        return ContentDispositionFilename.class.getSimpleName();
    }

    @Override
    protected Class<ContentDispositionFilenameHeaderValueConverter> type() {
        return ContentDispositionFilenameHeaderValueConverter.class;
    }
}
