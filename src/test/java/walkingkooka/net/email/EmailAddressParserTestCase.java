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

package walkingkooka.net.email;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.JavaVisibility;

public abstract class EmailAddressParserTestCase<P extends EmailAddressParser> implements ClassTesting2<P>,
        ToStringTesting<P> {

    EmailAddressParserTestCase() {
        super();
    }

    @Test
    public final void testValid() {
        this.parse("user@example.com");
    }

    abstract void parse(final String text);

    @Test
    public final void testEmailTooLongFails() {
        this.parseFails("user1234567890@example" + CharSequences.repeating('0', 255));
    }

    @Test
    public final void testMissingUserFails() {
        this.parseFails("@example.com");
    }

    @Test
    public final void testUserNameTooLongFails() {
        this.parseFails("user" + CharSequences.repeating('0', 100) + "@example.com");
    }

    @Test
    public final void testMissingHostFails() {
        this.parseFails("user@");
    }

    @Test
    public final void testMissingHostFails2() {
        this.parseFails("user");
    }

    @Test
    public final void testInvalidCharacterFails() {
        this.parseFails("user@:example.com");
    }

    abstract void parseFails(final String text);

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
