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

import walkingkooka.text.CharSequences;

/**
 * Base class which parses text containing one or many media types.
 */
abstract class MediaTypeHeaderParser extends HeaderParser<MediaTypeParameterName<?>>{

    // @VisibleForTesting
    MediaTypeHeaderParser(final String text) {
        super(text);
    }

    @Override
    final void value() {
        this.start = this.position;

        // type
        this.type = this.tokenText(RFC2045TOKEN);
        if(!this.hasMoreCharacters()) {
            fail("Missing sub type at " + this.position + " in " + CharSequences.quoteAndEscape(this.text));
        }

        if(this.character()!=SLASH){
            this.failInvalidCharacter();
        }
        if(this.type.isEmpty()) {
           this.failEmptyToken(TYPE);
        }

        this.position++;

        // sub type
        this.subType = this.tokenText(RFC2045TOKEN);
        this.failNotIfWhitespaceOrParameterSeparatorOrSeparator();
        if(this.subType.isEmpty()) {
            this.failEmptyToken(SUBTYPE);
        }
    }

    private final static char SLASH = '/';
    private final static String TYPE = "type";
    private final static String SUBTYPE = "sub type";


    @Override
    final void parameterName() {
        this.parseParameterName(RFC2045TOKEN, MediaTypeParameterName::with);
    }

    @Override
    void parameterValue() {
        final char c = this.character();
        if(DOUBLE_QUOTE ==c) {
            this.position++;
            this.quotedParameterValue();
        } else {
            this.parseParameterValue(RFC2045TOKEN);
        }
    }

    private void quotedParameterValue() {
        final int start = this.position;
        boolean escaping = false;

        Exit:
        for(;;) {
            if(!this.hasMoreCharacters()) {
                fail("Missing closing " + CharSequences.quoteAndEscape(DOUBLE_QUOTE) + " in " + CharSequences.quoteAndEscape(this.text));
            }
            final char c = this.character();
            this.position++;

            if(escaping) {
                switch(c){
                    case BACKSLASH:
                    case DOUBLE_QUOTE:
                        break;
                    default:
                        this.position--;
                        this.failInvalidCharacter();
                        break;
                }
                escaping = false;
                continue;
            }

            switch(c){
                case BACKSLASH:
                    escaping = true;
                    break;
                case DOUBLE_QUOTE:
                    this.addParameter(this.text.substring(start, this.position-1));
                    break Exit;
                default:
                    break;
            }
        }
    }

    private final static char BACKSLASH = '\\';
    private final static char DOUBLE_QUOTE = '"';

    @Override
    final void missingParameterValue() {
        this.failEmptyParameterValue();
    }

    @Override
    final void tokenEnd() {
        this.mediaType = MediaType.with(this.type,
                this.subType,
                this.parameters,
                this.text.substring(this.start, this.position));
        this.mediaTypeEnd();
    }

    private int start;
    private String type;
    private String subType;
    MediaType mediaType;

    abstract void mediaTypeEnd();
}
