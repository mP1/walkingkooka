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

import walkingkooka.collect.list.Lists;

public abstract class EbnfAlternativeConcatenationParentParserTokenTestCase<T extends EbnfParentParserToken> extends EbnfParentParserTokenTestCase2<T> {

    @Override
    protected T createDifferentToken() {
        return this.createToken("(*comment-3*)" + separatorChar() + "(*comment-4*)", Lists.of(this.comment("(*comment-3*)"), this.comment("(*comment-4*)"))
        );
    }

    abstract char separatorChar();
}
