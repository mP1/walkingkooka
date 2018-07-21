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
 */
package walkingkooka.text.cursor.parser.ebnf;

import java.util.function.Consumer;

final class EbnfRangeParserTokenConsumer implements Consumer<EbnfParserToken> {

    @Override
    public void accept(final EbnfParserToken token) {
        if(token.isAlternative() || token.isConcatenation() || token.isGroup() || token.isIdentifier() || token.isOptional() || token.isRepeated() || token.isTerminal()) {
            if(null ==this.begin) {
                if(!token.isTerminal()) {
                    throw new IllegalArgumentException("Range expected begin terminal but got " + token);
                }
                this.begin = token.cast();
            } else {
                if (null == this.end) {
                    if(!token.isTerminal()) {
                        throw new IllegalArgumentException("Range expected end terminal but got " + token);
                    }
                    this.end = token.cast();
                }
            }
        }
    }

    EbnfTerminalParserToken begin;
    EbnfTerminalParserToken end;

}
