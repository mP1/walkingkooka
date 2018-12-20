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

enum HeaderParser2Mode {

    WHITESPACE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.consumeWhitespace(VALUE);
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.position--;
            parser.failInvalidCharacter();
        }
    },

    VALUE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.value();
            parser.mode = VALUE_WHITESPACE;
            parser.parameters = Maps.sorted();;
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.failMissingValue();
        }
    },

    VALUE_WHITESPACE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.consumeWhitespace(PARAMETER_SEPARATOR_OR_SEPARATOR);
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_SEPARATOR_OR_SEPARATOR {
        @Override
        void accept(final HeaderParser2<?> parser) {
            switch(parser.character()){
                case HeaderParser2.PARAMETER_SEPARATOR:
                    parser.position++;
                    parser.mode = PARAMETER_SEPARATOR_WHITESPACE;
                    break;
                case HeaderParser2.SEPARATOR:
                    parser.separator();
                    parser.position++;
                    parser.tokenEnd();
                    parser.mode = WHITESPACE;
                    break;
                default:
                    parser.failInvalidCharacter();
            }
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_SEPARATOR_WHITESPACE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.consumeWhitespace(PARAMETER_NAME);
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_NAME {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.parameterName();
            parser.mode = PARAMETER_NAME_WHITESPACE;
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.tokenEnd();
        }
    },

    PARAMETER_NAME_WHITESPACE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.consumeWhitespace(PARAMETER_EQUALS);
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_EQUALS {
        @Override
        void accept(final HeaderParser2<?> parser) {
            switch(parser.character()){
                case HeaderParser2.PARAMETER_NAME_VALUE_SEPARATOR:
                    parser.position++;
                    parser.mode = PARAMETER_EQUALS_WHITESPACE;
                    break;
                default:
                    parser.failInvalidCharacter();
            }
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_EQUALS_WHITESPACE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.consumeWhitespace(PARAMETER_VALUE);
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_VALUE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.parameterValue();
            parser.mode = PARAMETER_VALUE_WHITESPACE;
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.position--;
            parser.missingParameterValue();
        }
    },

    PARAMETER_VALUE_WHITESPACE {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.consumeWhitespace(PARAMETER_SEPARATOR_OR_SEPARATOR);
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.tokenEnd();
        }
    },

    SEPARATOR {
        @Override
        void accept(final HeaderParser2<?> parser) {
            parser.separator();
        }

        @Override
        void endOfText(final HeaderParser2<?> parser) {
            parser.tokenEnd();
        }
    };

    abstract void accept(final HeaderParser2<?> parser);

    abstract void endOfText(final HeaderParser2<?> parser);

    final void never() {
        NeverError.unhandledCase(this);
    }
}
