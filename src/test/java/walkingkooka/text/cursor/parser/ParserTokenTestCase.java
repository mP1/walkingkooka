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

package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

public abstract class ParserTokenTestCase<T extends ParserToken> implements ClassTesting2<T>,
        ParserTokenTesting<T> {

    ParserTokenTestCase() {
        super();
    }

    @Test
    public final void testAccept2() {
        new ParserTokenVisitor() {
        }.accept(this.createToken());
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
