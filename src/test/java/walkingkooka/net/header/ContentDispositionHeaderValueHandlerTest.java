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
import walkingkooka.net.email.EmailAddress;

public final class ContentDispositionHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<ContentDispositionHeaderValueHandler, ContentDisposition> {

    @Override
    public String typeNamePrefix() {
        return ContentDisposition.class.getSimpleName();
    }

    @Test
    public void testHeader() {
        this.parseAndToTextAndCheck("attachment; filename=readme.txt",
                this.value());
    }

    @Override
    ContentDispositionHeaderValueHandler handler() {
        return ContentDispositionHeaderValueHandler.INSTANCE;
    }

    @Override
    HttpHeaderName<EmailAddress> name() {
        return HttpHeaderName.FROM;
    }

    @Override
    String invalidHeaderValue() {
        return "\0";
    }

    @Override
    ContentDisposition value() {
        return ContentDispositionType.ATTACHMENT.setFilename(ContentDispositionFileName.notEncoded("readme.txt"));
    }

    @Override
    String valueType() {
        return this.valueType(ContentDisposition.class);
    }

    @Override
    String handlerToString() {
        return ContentDisposition.class.getSimpleName();
    }

    @Override
    public Class<ContentDispositionHeaderValueHandler> type() {
        return ContentDispositionHeaderValueHandler.class;
    }
}
