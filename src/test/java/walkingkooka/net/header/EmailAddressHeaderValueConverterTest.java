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

public final class EmailAddressHeaderValueConverterTest extends
        HeaderValueConverterTestCase<EmailAddressHeaderValueConverter, EmailAddress> {

    @Override
    protected String requiredPrefix() {
        return EmailAddress.class.getSimpleName();
    }

    @Test
    public void testFrom() {
        final String url = "user@example.com";
        this.parseAndFormatAndCheck(url, EmailAddress.with(url));
    }

    @Override
    protected EmailAddressHeaderValueConverter converter() {
        return EmailAddressHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<EmailAddress> name() {
        return HttpHeaderName.FROM;
    }

    @Override
    protected String invalidHeaderValue() {
        return "/relative/url/must/fail";
    }

    @Override
    protected EmailAddress value() {
        return EmailAddress.with("user@example.com");
    }

    @Override
    protected String converterToString() {
        return EmailAddress.class.getSimpleName();
    }

    @Override
    protected Class<EmailAddressHeaderValueConverter> type() {
        return EmailAddressHeaderValueConverter.class;
    }
}
