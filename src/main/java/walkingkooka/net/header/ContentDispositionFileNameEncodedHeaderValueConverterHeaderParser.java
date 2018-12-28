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

/**
 * A {@link HeaderParser} that parsers text into a {@link ContentDispositionFileName}.
 */
final class ContentDispositionFileNameEncodedHeaderValueConverterHeaderParser extends HeaderParser {

    static ContentDispositionFileName parseContentDispositionFileName(final String text) {
        final ContentDispositionFileNameEncodedHeaderValueConverterHeaderParser parser = new ContentDispositionFileNameEncodedHeaderValueConverterHeaderParser(text);
        parser.parse();
        return parser.filename;
    }

    private ContentDispositionFileNameEncodedHeaderValueConverterHeaderParser(final String text) {
        super(text);
    }

    @Override
    void whitespace() {
        this.whitespace0();
    }

    @Override
    void tokenSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void keyValueSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void multiValueSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void wildcard() {
        this.failInvalidCharacter();
    }

    @Override
    void slash() {
        this.failInvalidCharacter();
    }

    @Override
    void quotedText() {
        this.failInvalidCharacter();
    }

    @Override
    void token() {
        this.filename = ContentDispositionFileName.encoded(this.encodedText());
    }

    @Override
    void endOfText() {
        // nop
    }

    @Override
    void missingValue() {
        this.failMissingValue(FILENAME);
    }

    private final static String FILENAME = "filename";

    private ContentDispositionFileName filename;
}
