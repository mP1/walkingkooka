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

public final class ContentDispositionHeaderValueConverterTest extends
        HeaderValueConverterTestCase<ContentDispositionHeaderValueConverter, ContentDisposition> {
    @Override
    protected String requiredPrefix() {
        return ContentDisposition.class.getSimpleName();
    }

    @Test
    public void testHeader() {
        this.parseAndFormatAndCheck("attachment; filename=readme.txt",
                this.value());
    }

    @Override
    protected ContentDispositionHeaderValueConverter converter() {
        return ContentDispositionHeaderValueConverter.INSTANCE;
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
    protected ContentDisposition value() {
        return ContentDispositionType.ATTACHMENT.setFilename(ContentDispositionFilename.with("readme.txt"));
    }

    @Override
    protected String converterToString() {
        return ContentDisposition.class.getSimpleName();
    }

    @Override
    protected Class<ContentDispositionHeaderValueConverter> type() {
        return ContentDispositionHeaderValueConverter.class;
    }
}
