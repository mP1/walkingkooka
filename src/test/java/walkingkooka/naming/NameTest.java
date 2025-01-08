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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidTextLengthException;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NameTest implements ClassTesting<Name> {

    // checkLength......................................................................................................

    @Test
    public void testCheckLengthLessThanMin() {
        final InvalidTextLengthException thrown = assertThrows(
            InvalidTextLengthException.class,
            () -> Name.checkLength(
                "Label123",
                "A",
                2,
                3
            )
        );
        this.checkEquals(
            thrown.getMessage(),
            "Length 1 of \"Label123\" not between 2..3 = \"A\""
        );
    }

    @Test
    public void testCheckLengthLessThanMax() {
        final InvalidTextLengthException thrown = assertThrows(
            InvalidTextLengthException.class,
            () -> Name.checkLength(
                "Label123",
                "ABCD",
                2,
                3
            )
        );
        this.checkEquals(
            thrown.getMessage(),
            "Length 4 of \"Label123\" not between 2..3 = \"ABCD\""
        );
    }

    @Test
    public void testCheckLength() {
        Name.checkLength(
            "Label123",
            "AB",
            2,
            3
        );
    }

    // class............................................................................................................

    @Override
    public Class<Name> type() {
        return Name.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
