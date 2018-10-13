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

package walkingkooka.net.email;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import walkingkooka.net.HostAddressProblem;
import walkingkooka.test.PublicClassTestCase;

import java.util.Arrays;

import static org.junit.Assert.assertNotEquals;


final public class EmailAddressTest extends PublicClassTestCase<EmailAddress> {


    @Test
    public void testNullFails() {
        this.withFails(null);
    }

    @Test
    public void testEmptyAddressFails() {
        this.withFails("");
    }

    @Test
    public void testOnlyWhitespaceFails() {
        this.withFails("    ");
    }

    @Test
    public void testTooLongFails() {
        final char[] array = new char[EmailAddress.MAX_EMAIL_LENGTH - 5];
        Arrays.fill(array, 'x');
        final String email = "user@" + new String(array);
        this.withFails(email, EmailAddress.EMAIL_TOO_LONG);
    }

    @Test
    public void testTooShortFails() {
        this.withFails(".");
    }


    @Test
    public void testUsernameWithLessThanFails() {
        this.invalidUserNameCharacter("user<@server", '<');
    }


    @Test
    public void testUsernameWithGreaterThanFails() {
        this.invalidUserNameCharacter("user>@server", '>');
    }


    @Test
    public void testUsernameWithOpeningSquareBracketFails() {
        this.invalidUserNameCharacter("user[@server", '[');
    }


    @Test
    public void testUsernameWithClosingSquareBracketFails() {
        this.invalidUserNameCharacter("user]@server", ']');
    }


    @Test
    public void testUsernameWithBackslashFails() {
        this.invalidUserNameCharacter("user\\@server", '\\');
    }


    @Test
    public void testUsernameWithInvalidFails() {
        final char c = 255;
        this.invalidUserNameCharacter("user" + c + "@server", c);
    }

    private void invalidUserNameCharacter(final String email, final char c) {
        final int at = email.indexOf(c);
        assertNotEquals("invalid character '" + c + "' does not appear in email=" + email, -1, at);
        this.withFails(email, EmailAddress.invalidCharacter(email, at));
    }


    @Test
    public void testServerContainsInvalidCharacterFails() {
        final String email = "user@s erver";
        this.withFails(email, HostAddressProblem.invalidCharacter(email.indexOf(' ')));
    }


    @Test
    public void testExtraAtSignFails() {
        final String email = "user@extra@atsign";
        this.withFails(email, HostAddressProblem.invalidCharacter(email.lastIndexOf('@')));
    }


    @Test
    public void testWithoutUserFails() {
        final String email = "@server";
        this.withFails(email, EmailAddress.missingUser(email));
    }


    @Test
    public void testWithoutHostFails() {
        final String email = "user@";
        this.withFails(email, EmailAddress.missingHost(email));
    }


    @Test
    public void testDoubleDotFails() {
        final String email = "use..r@serve";
        this.withFails(email, EmailAddress.invalidCharacter(email, email.indexOf("..") + 1));
    }


    @Test
    public void testIp4MissingClosingBracketFails() {
        final String email = "user@[1.2.3.4";
        this.withFails(email, HostAddressProblem.incomplete());
    }

    @Test
    public void testIp6MissingClosingBracketFails() {
        final String email = "user@[1:2:3:4:5:6:7:8";
        this.withFails(email, HostAddressProblem.incomplete());
    }

    @Test
    public void testIp4UnnecesssaryClosingBracketFails() {
        final String email = "user@1.2.3.4]";
        this.withFails(email, HostAddressProblem.invalidCharacter(email.indexOf(']')));
    }

    @Test
    public void testIp6EmbeddedIp4WithInvalidValueFails() {
        final String email = "user@1:2:3:4:5:6:7.888.9.0";
        this.withFails(email, HostAddressProblem.invalidValue(email.indexOf('8')));
    }

    @Test
    public void testIp6UnnecessaryClosingBracketFails() {
        final String email = "user@1:2:3:4:5:6:7:8]";
        this.withFails(email, HostAddressProblem.invalidCharacter(email.indexOf(']')));
    }

    private void withFails(final String email) {
        this.withFails(email, (String) null);
    }

    private void withFails(final String address, final HostAddressProblem problem) {
        this.withFails(address, problem.message(address));
    }

    private void withFails(final String email, final String expectedMessage) {
        try {
            EmailAddress.with(email);
            Assert.fail();
        } catch (final RuntimeException expected) {
            if (null != expectedMessage) {
                if (false == expectedMessage.equals(expected.getMessage())) {
                    expected.printStackTrace();
                    TestCase.failNotEquals("message", expectedMessage, expected.getMessage());
                }
            }
        }
    }

    @Test
    public void testWith() {
        this.with("user", "server");
    }

    @Test
    public void testWith2() {
        this.with("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!.#$%&'*+-/=?^_`{|}~", "server");
    }

    @Test
    public void testUsernameWithQuotedSquareBrackets() {
        this.with("user\"[]\"", "server");
    }

    @Test
    public void testUsernameWithQuotedLessAndGreaterThan() {
        this.with("user\"<>\"", "server");
    }

    @Test
    public void testUsernameWithQuotedColon() {
        this.with("user\":\"", "server");
    }

    @Test
    public void testUsernameWithQuotedSemiColon() {
        this.with("user\":\"", "server");
    }

    @Test
    public void testUsernameWithQuotedAtSign() {
        this.with("user\"@\"", "server");
    }

    @Test
    public void testUsernameWithQuotedBackslash() {
        this.with("user\"\\\\\"", "server");
    }

    @Test
    public void testIp4Address() {
        this.with("user", "1.2.3.4");
    }

    @Test
    public void testIp4AddressSurroundedBySquareBrackets() {
        this.with("user", "[1.2.3.4]");
    }

    @Test
    public void testIp6Address() {
        this.with("user", "1111:2222:3333:4444:5555:6666:7777:8888");
    }

    @Test
    public void testIp6AddressWithEmbeddedIp4() {
        this.with("user", "1111:2222:3333:4444:5555:6666:255.7.8.9");
    }

    @Test
    public void testIp6AddressSurroundedBySquareBrackets() {
        this.with("user", "[1111:2222:3333:4444:5555:6666:7777:8888]");
    }

    @Test
    public void testManyNoneConsecutiveDots() {
        this.with("first.middle.last", "server");
    }

    private void with(final String user, final String server) {
        final String address = user + '@' + server;
        final EmailAddress email = EmailAddress.with(address);
        Assert.assertEquals("address", address, email.value());
        Assert.assertEquals("user", user, email.user());
        Assert.assertEquals("host", server, email.host().value());
    }

    @Override
    protected Class<EmailAddress> type() {
        return EmailAddress.class;
    }
}
