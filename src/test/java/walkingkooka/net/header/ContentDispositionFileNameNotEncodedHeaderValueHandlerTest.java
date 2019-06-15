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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

public final class ContentDispositionFileNameNotEncodedHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<ContentDispositionFileNameNotEncodedHeaderValueHandler, ContentDispositionFileName> {

    @Override
    public String typeNamePrefix() {
        return ContentDispositionFileName.class.getSimpleName();
    }

    @Test
    public void testFilename() {
        final String filename = "readme.txt";
        this.parseAndToTextAndCheck(filename, ContentDispositionFileName.notEncoded(filename));
    }

    @Override
    ContentDispositionFileNameNotEncodedHeaderValueHandler handler() {
        return ContentDispositionFileNameNotEncodedHeaderValueHandler.INSTANCE;
    }

    @Override
    ContentDispositionParameterName name() {
        return ContentDispositionParameterName.FILENAME;
    }

    @Override
    String invalidHeaderValue() {
        return "\0";
    }

    @Override
    ContentDispositionFileName value() {
        return ContentDispositionFileName.notEncoded("readme.txt");
    }

    @Override
    String valueType() {
        return this.valueType(ContentDispositionFileNameNotEncoded.class);
    }

    @Override
    String handlerToString() {
        return ContentDispositionFileNameNotEncoded.class.getSimpleName();
    }

    @Override
    public Class<ContentDispositionFileNameNotEncodedHeaderValueHandler> type() {
        return ContentDispositionFileNameNotEncodedHeaderValueHandler.class;
    }
}
