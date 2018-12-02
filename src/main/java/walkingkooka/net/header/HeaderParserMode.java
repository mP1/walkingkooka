/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http{}//www.apache.org/licenses/LICENSE-2.0
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

import walkingkooka.NeverError;
import walkingkooka.collect.map.Maps;

enum HeaderParserMode {

    VALUE {
        @Override
        void accept(final HeaderParser parser) {
            parser.value();
            parser.mode = VALUE_WHITESPACE;
            parser.parameters = Maps.sorted();;
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.tokenEnd();
        }
    },

    VALUE_WHITESPACE {
        @Override
        void accept(final HeaderParser parser) {
            parser.consumeWhitespace(PARAMETER_SEPARATOR_OR_SEPARATOR);
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_SEPARATOR_OR_SEPARATOR {
        @Override
        void accept(final HeaderParser parser) {
            switch(parser.character()){
                case HeaderParser.PARAMETER_SEPARATOR:
                    parser.position++;
                    parser.mode = PARAMETER_SEPARATOR_WHITESPACE;
                    break;
                case HeaderParser.SEPARATOR:
                    parser.position++;
                    parser.tokenEnd();
                    parser.mode = SEPARATOR_WHITESPACE;
                    break;
                default:
                    parser.failInvalidCharacter();
            }
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_SEPARATOR_WHITESPACE {
        @Override
        void accept(final HeaderParser parser) {
            parser.consumeWhitespace(PARAMETER_NAME);
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_NAME {
        @Override
        void accept(final HeaderParser parser) {
            parser.parameterName();
            parser.mode = PARAMETER_NAME_WHITESPACE;
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_NAME_WHITESPACE {
        @Override
        void accept(final HeaderParser parser) {
            parser.consumeWhitespace(PARAMETER_EQUALS);
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_EQUALS {
        @Override
        void accept(final HeaderParser parser) {
            switch(parser.character()){
                case HeaderParser.EQUALS_SIGN:
                    parser.position++;
                    parser.mode = PARAMETER_EQUALS_WHITESPACE;
                    break;
                default:
                    parser.failInvalidCharacter();
            }
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_EQUALS_WHITESPACE {
        @Override
        void accept(final HeaderParser parser) {
            parser.consumeWhitespace(PARAMETER_VALUE);
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_VALUE {
        @Override
        void accept(final HeaderParser parser) {
            parser.parameterValue();
            parser.mode = PARAMETER_VALUE_WHITESPACE;
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_VALUE_WHITESPACE {
        @Override
        void accept(final HeaderParser parser) {
            parser.consumeWhitespace(PARAMETER_SEPARATOR_OR_SEPARATOR);
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.tokenEnd();
        }
    },

    SEPARATOR {
        @Override
        void accept(final HeaderParser parser) {
            parser.separator();
        }

        @Override
        void endOfText(final HeaderParser parser) {
            parser.tokenEnd();
        }
    },

    SEPARATOR_WHITESPACE {
        @Override
        void accept(final HeaderParser parser) {
            parser.consumeWhitespace(VALUE);
        }

        @Override
        void endOfText(final HeaderParser parser) {
            // nop
        }
    };

    abstract void accept(final HeaderParser parser);

    abstract void endOfText(final HeaderParser parser);

    final void never() {
        NeverError.unhandledCase(this);
    }
}
