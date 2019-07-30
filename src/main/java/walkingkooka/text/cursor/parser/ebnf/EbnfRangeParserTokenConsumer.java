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
package walkingkooka.text.cursor.parser.ebnf;

import java.util.function.Consumer;

final class EbnfRangeParserTokenConsumer implements Consumer<EbnfParserToken> {

    static EbnfRangeParserTokenConsumer with() {
        return new EbnfRangeParserTokenConsumer();
    }

    private EbnfRangeParserTokenConsumer() {
        super();
    }

    @Override
    public void accept(final EbnfParserToken token) {
        if (!token.isNoise()) {
            if (null == this.begin) {
                this.complainIfNeitherTerminalOrIdentifier(token, "begin");
                this.begin = token;
            } else {
                if (null == this.end) {
                    this.complainIfNeitherTerminalOrIdentifier(token, "end");
                    this.end = token;
                }
            }
        }
    }

    private void complainIfNeitherTerminalOrIdentifier(final EbnfParserToken token, final String beginOrEnd) {
        if (!token.isTerminal() && !token.isIdentifier()) {
            throw new IllegalArgumentException("Range expected " + beginOrEnd + " terminal|identifier but got " + token);
        }
    }

    EbnfParserToken begin;
    EbnfParserToken end;

}
