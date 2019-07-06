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
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class EmailAddressParserTryParseTest extends EmailAddressParserTestCase<EmailAddressParserTryParse> {

    @Test
    public void testToString() {
        this.toStringAndCheck(new EmailAddressParserTryParse(), "EmailAddress.tryParse");
    }

    @Override
    void parse(final String text) {
        EmailAddressParserTryParse.tryParse(text);
    }

    @Override
    void parseFails(final String text) {
        assertEquals(Optional.empty(),
                EmailAddressParserTryParse.tryParse(text),
                () -> "parse " + CharSequences.quoteAndEscape(text));
    }

    @Override
    public Class<EmailAddressParserTryParse> type() {
        return EmailAddressParserTryParse.class;
    }
}
