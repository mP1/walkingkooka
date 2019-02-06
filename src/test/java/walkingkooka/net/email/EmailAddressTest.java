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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.net.HostAddressProblem;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


final public class EmailAddressTest extends ClassTestCase<EmailAddress>
        implements HashCodeEqualsDefinedTesting<EmailAddress>,
        ParseStringTesting<EmailAddress>,
        SerializationTesting<EmailAddress>,
        ToStringTesting<EmailAddress> {

    @Test
    public void testTryParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            EmailAddress.tryParse(null);
        });
    }

    @Test
    public void testOnlyWhitespaceFails() {
        this.parseFails("    ");
    }

    @Test
    public void testTooLongFails() {
        final char[] array = new char[EmailAddress.MAX_EMAIL_LENGTH - 5];
        Arrays.fill(array, 'x');
        final String email = "user@" + new String(array);
        this.parseFails(email, EmailAddress.EMAIL_TOO_LONG);
    }

    @Test
    public void testTooShortFails() {
        this.parseFails(".");
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
        assertNotEquals(-1, at, "invalid character '" + c + "' does not appear in email=" + email);
        this.parseFails(email, new InvalidCharacterException(email, at).getMessage());
    }

    @Test
    public void testServerContainsInvalidCharacterFails() {
        final String email = "user@s erver";
        this.parseFails(email, HostAddressProblem.invalidCharacter(email.indexOf(' ')));
    }

    @Test
    public void testExtraAtSignFails() {
        final String email = "user@extra@atsign";
        this.parseFails(email, HostAddressProblem.invalidCharacter(email.lastIndexOf('@')));
    }

    @Test
    public void testWithoutUserFails() {
        final String email = "@server";
        this.parseFails(email, EmailAddress.missingUser(email));
    }

    @Test
    public void testWithoutHostFails() {
        final String email = "user@";
        this.parseFails(email, EmailAddress.missingHost(email));
    }

    @Test
    public void testDoubleDotFails() {
        final String email = "use..r@serve";
        this.parseFails(email, new InvalidCharacterException(email, email.indexOf("..") + 1).getMessage());
    }

    @Test
    public void testIp4MissingClosingBracketFails() {
        final String email = "user@[1.2.3.4";
        this.parseFails(email, HostAddressProblem.incomplete());
    }

    @Test
    public void testIp6MissingClosingBracketFails() {
        final String email = "user@[1:2:3:4:5:6:7:8";
        this.parseFails(email, HostAddressProblem.incomplete());
    }

    @Test
    public void testIp4UnnecesssaryClosingBracketFails() {
        final String email = "user@1.2.3.4]";
        this.parseFails(email, HostAddressProblem.invalidCharacter(email.indexOf(']')));
    }

    @Test
    public void testIp6EmbeddedIp4WithInvalidValueFails() {
        final String email = "user@1:2:3:4:5:6:7.888.9.0";
        this.parseFails(email, HostAddressProblem.invalidValue(email.indexOf('8')));
    }

    @Test
    public void testIp6UnnecessaryClosingBracketFails() {
        final String email = "user@1:2:3:4:5:6:7:8]";
        this.parseFails(email, HostAddressProblem.invalidCharacter(email.indexOf(']')));
    }

    @Test
    public void testUsernameTooLongFails() {
        final char[] user = new char[EmailAddress.MAX_LOCAL_LENGTH];
        Arrays.fill(user, 'a');
        this.parseFails(new String(user) + "@example.com", EmailAddress.userNameTooLong(EmailAddress.MAX_LOCAL_LENGTH));
    }

    private void parseFails(final String email) {
        this.parseFails(email, (String) null);
    }

    private void parseFails(final String address, final HostAddressProblem problem) {
        this.parseFails(address, problem.message(address));
    }

    private void parseFails(final String email, final String expectedMessage) {
        withFails(email, expectedMessage);
        assertEquals(Optional.empty(), EmailAddress.tryParse(email), email);
    }

    private void withFails(final String email, final String message) {
        final IllegalArgumentException expected = assertThrows(IllegalArgumentException.class, () -> {
            EmailAddress.parse(email);
        });
        expected.printStackTrace();

        if(null!=message) {
            assertEquals(message, expected.getMessage(), "message");
        }
    }

    @Test
    public void testWith() {
        this.createAndCheck("user", "server");
    }

    @Test
    public void testWith2() {
        this.createAndCheck("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!.#$%&'*+-/=?^_`{|}~", "server");
    }

    @Test
    public void testUsernameWithQuotedSquareBrackets() {
        this.createAndCheck("user\"[]\"", "server");
    }

    @Test
    public void testUsernameWithQuotedLessAndGreaterThan() {
        this.createAndCheck("user\"<>\"", "server");
    }

    @Test
    public void testUsernameWithQuotedColon() {
        this.createAndCheck("user\":\"", "server");
    }

    @Test
    public void testUsernameWithQuotedSemiColon() {
        this.createAndCheck("user\":\"", "server");
    }

    @Test
    public void testUsernameWithQuotedAtSign() {
        this.createAndCheck("user\"@\"", "server");
    }

    @Test
    public void testUsernameWithQuotedBackslash() {
        this.createAndCheck("user\"\\\\\"", "server");
    }

    @Test
    public void testIp4Address() {
        this.createAndCheck("user", "1.2.3.4");
    }

    @Test
    public void testIp4AddressSurroundedBySquareBrackets() {
        this.createAndCheck("user", "[1.2.3.4]");
    }

    @Test
    public void testIp6Address() {
        this.createAndCheck("user", "1111:2222:3333:4444:5555:6666:7777:8888");
    }

    @Test
    public void testIp6AddressWithEmbeddedIp4() {
        this.createAndCheck("user", "1111:2222:3333:4444:5555:6666:255.7.8.9");
    }

    @Test
    public void testIp6AddressSurroundedBySquareBrackets() {
        this.createAndCheck("user", "[1111:2222:3333:4444:5555:6666:7777:8888]");
    }

    @Test
    public void testManyNoneConsecutiveDots() {
        this.createAndCheck("first.middle.last", "server");
    }

    private void createAndCheck(final String user, final String server) {
        final String address = user + '@' + server;
        final EmailAddress emailAddress = EmailAddress.parse(address);
        this.parseSuccessful(user, server, emailAddress);

        final Optional<EmailAddress> parsed = EmailAddress.tryParse(address);
        assertNotEquals(Optional.empty(), parsed, "tryParse failed");
        this.parseSuccessful(user, server, parsed.get());
    }

    private void parseSuccessful(final String user, final String server, final EmailAddress emailAddress) {
        final String address = user + '@' + server;
        assertEquals(address, emailAddress.value(),"address");
        assertEquals(user, emailAddress.user(),"user");
        assertEquals(server, emailAddress.host().value(),"host");
    }

    // --- start of tests generator: DominicsayersComIsemailEmailAddressTestGenerator file: www.dominicsayers.com-isemail-tests.xml ---
    @Test
    public void test001__first_DOT_last_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("first.last@iana.org");
    }

    @Test
    public void test002__1234567890123456789012345678901234567890123456789012345678901234_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("1234567890123456789012345678901234567890123456789012345678901234@iana.org");
    }

    @Test
    public void test003__first_DOT_last_ATSIGN_sub_DOT_docom__Mistyped_comma_instead_of_dot__LEFT_PAREN_replaces_old_3_which_was_the_same_as_57_RIGHT_PAREN_() {
        this.parseFails2("first.last@sub.do,com",
                "Mistyped comma instead of dot (replaces old #3 which was the same as #57)");
    }

    @Test
    public void test004__firstlast_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"first\\\"last\"@iana.org");
    }

    @Test
    public void test005__first_ATSIGN_last_ATSIGN_iana_DOT_org__Escaping_can_only_happen_within_a_quoted_string() {
        this.parseFails2("first\\@last@iana.org", "Escaping can only happen within a quoted string");
    }

    @Test
    public void test006__first_ATSIGN_last_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"first@last\"@iana.org");
    }

    @Test
    public void test007__firstlast_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"first\\\\last\"@iana.org");
    }

    @Test
    public void test008__x_ATSIGN_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x2__Total_length_reduced_to_254_characters_so_its_still_valid() {
        this.parseSuccessful(
                "x@x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x2",
                "Total length reduced to 254 characters so it\'s still valid");
    }

    @Test
    public void test009__1234567890123456789012345678901234567890123456789012345678_ATSIGN_12345678901234567890123456789012345678901234567890123456789_DOT_12345678901234567890123456789012345678901234567890123456789_DOT_123456789012345678901234567890123456789012345678901234567890123_DOT_iana_DOT_org__Total_length_reduced_to_254_characters_so_its_still_valid() {
        this.parseSuccessful(
                "1234567890123456789012345678901234567890123456789012345678@12345678901234567890123456789012345678901234567890123456789.12345678901234567890123456789012345678901234567890123456789.123456789012345678901234567890123456789012345678901234567890123.iana.org",
                "Total length reduced to 254 characters so it\'s still valid");
    }

    @Test
    public void test010__first_DOT_last_ATSIGN_12_DOT_34_DOT_56_DOT_78() {
        this.parseSuccessful("first.last@[12.34.56.78]");
    }

    @Test
    public void test011__first_DOT_last_ATSIGN_IPv612_DOT_34_DOT_56_DOT_78() {
        this.parseSuccessful("first.last@[IPv6:::12.34.56.78]");
    }

    @Test
    public void test012__first_DOT_last_ATSIGN_IPv6111122223333444412_DOT_34_DOT_56_DOT_78() {
        this.parseSuccessful("first.last@[IPv6:1111:2222:3333::4444:12.34.56.78]");
    }

    @Test
    public void test013__first_DOT_last_ATSIGN_IPv611112222333344445555666612_DOT_34_DOT_56_DOT_78() {
        this.parseSuccessful("first.last@[IPv6:1111:2222:3333:4444:5555:6666:12.34.56.78]");
    }

    @Test
    public void test014__first_DOT_last_ATSIGN_IPv6111122223333444455556666() {
        this.parseSuccessful("first.last@[IPv6:::1111:2222:3333:4444:5555:6666]");
    }

    @Test
    public void test015__first_DOT_last_ATSIGN_IPv6111122223333444455556666() {
        this.parseSuccessful("first.last@[IPv6:1111:2222:3333::4444:5555:6666]");
    }

    @Test
    public void test016__first_DOT_last_ATSIGN_IPv6111122223333444455556666() {
        this.parseSuccessful("first.last@[IPv6:1111:2222:3333:4444:5555:6666::]");
    }

    @Test
    public void test017__first_DOT_last_ATSIGN_IPv611112222333344445555666677778888() {
        this.parseSuccessful("first.last@[IPv6:1111:2222:3333:4444:5555:6666:7777:8888]");
    }

    @Test
    public void test018__first_DOT_last_ATSIGN_x23456789012345678901234567890123456789012345678901234567890123_DOT_iana_DOT_org() {
        this.parseSuccessful("first.last@x23456789012345678901234567890123456789012345678901234567890123.iana.org");
    }

    @Test
    public void test019__first_DOT_last_ATSIGN_3com_DOT_com() {
        this.parseSuccessful("first.last@3com.com");
    }

    @Test
    public void test020__first_DOT_last_ATSIGN_123_DOT_iana_DOT_org() {
        this.parseSuccessful("first.last@123.iana.org");
    }

    @Test
    public void test021__123456789012345678901234567890123456789012345678901234567890_ATSIGN_12345678901234567890123456789012345678901234567890123456789_DOT_12345678901234567890123456789012345678901234567890123456789_DOT_12345678901234567890123456789012345678901234567890123456789_DOT_12345_DOT_iana_DOT_org__Entire_address_is_longer_than_254_characters() {
        this.parseFails2(
                "123456789012345678901234567890123456789012345678901234567890@12345678901234567890123456789012345678901234567890123456789.12345678901234567890123456789012345678901234567890123456789.12345678901234567890123456789012345678901234567890123456789.12345.iana.org",
                "Entire address is longer than 254 characters");
    }

    @Test
    public void test022__first_DOT_last__No__ATSIGN_() {
        this.parseFails2("first.last", "No @");
    }

    @Test
    public void test023__12345678901234567890123456789012345678901234567890123456789012345_ATSIGN_iana_DOT_org__Local_part_more_than_64_characters() {
        this.parseFails2("12345678901234567890123456789012345678901234567890123456789012345@iana.org",
                "Local part more than 64 characters");
    }

    @Test
    public void test024___DOT_first_DOT_last_ATSIGN_iana_DOT_org__Local_part_starts_with_a_dot() {
        this.parseFails2(".first.last@iana.org", "Local part starts with a dot");
    }

    @Test
    public void test025__first_DOT_last_DOT__ATSIGN_iana_DOT_org__Local_part_ends_with_a_dot() {
        this.parseFails2("first.last.@iana.org", "Local part ends with a dot");
    }

    @Test
    public void test026__first_DOT__DOT_last_ATSIGN_iana_DOT_org__Local_part_has_consecutive_dots() {
        this.parseFails2("first..last@iana.org", "Local part has consecutive dots");
    }

    @Test
    public void test027__firstlast_ATSIGN_iana_DOT_org__Local_part_contains_unescaped_excluded_characters() {
        this.parseFails2("\"first\"last\"@iana.org", "Local part contains unescaped excluded characters");
    }

    @Test
    public void test028__firstlast_ATSIGN_iana_DOT_org__Any_character_can_be_escaped_in_a_quoted_string() {
        this.parseSuccessful("\"first\\last\"@iana.org", "Any character can be escaped in a quoted string");
    }

    @Test
    public void test029___ATSIGN_iana_DOT_org__Local_part_contains_unescaped_excluded_characters() {
        this.parseFails2("\"\"\"@iana.org", "Local part contains unescaped excluded characters");
    }

    @Test
    public void test030___ATSIGN_iana_DOT_org__Local_part_cannot_end_with_a_backslash() {
        this.parseFails2("\"\\\"@iana.org", "Local part cannot end with a backslash");
    }

    @Test
    public void test031___ATSIGN_iana_DOT_org__Local_part_is_effectively_empty() {
        this.parseFails2("\"\"@iana.org", "Local part is effectively empty");
    }

    @Test
    public void test032__first_ATSIGN_last_ATSIGN_iana_DOT_org__Local_part_contains_unescaped_excluded_characters() {
        this.parseFails2("first\\\\@last@iana.org", "Local part contains unescaped excluded characters");
    }

    @Test
    public void test033__first_DOT_last_ATSIGN___No_domain() {
        this.parseFails2("first.last@", "No domain");
    }

    @Test
    public void test034__x_ATSIGN_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456789_DOT_x23456__Domain_exceeds_255_chars() {
        this.parseFails2(
                "x@x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456789.x23456",
                "Domain exceeds 255 chars");
    }

    @Test
    public void test035__first_DOT_last_ATSIGN__DOT_12_DOT_34_DOT_56_DOT_78__Only_char_that_can_precede_IPv4_address_is_() {
        this.parseFails2("first.last@[.12.34.56.78]", "Only char that can precede IPv4 address is \':\'");
    }

    @Test
    public void test036__first_DOT_last_ATSIGN_12_DOT_34_DOT_56_DOT_789__Cant_be_interpreted_as_IPv4_so_IPv6_tag_is_missing() {
        this.parseFails2("first.last@[12.34.56.789]", "Can\'t be interpreted as IPv4 so IPv6 tag is missing");
    }

    @Test
    @Disabled
    public void test037__first_DOT_last_ATSIGN_12_DOT_34_DOT_56_DOT_78__IPv6_tag_is_missing() {
        this.parseFails2("first.last@[::12.34.56.78]", "IPv6 tag is missing");
    }

    @Test
    public void test038__first_DOT_last_ATSIGN_IPv512_DOT_34_DOT_56_DOT_78__IPv6_tag_is_wrong() {
        this.parseFails2("first.last@[IPv5:::12.34.56.78]", "IPv6 tag is wrong");
    }

    @Test
    public void test039__first_DOT_last_ATSIGN_IPv61111222233334444555512_DOT_34_DOT_56_DOT_78__RFC_4291_disagrees_with_RFC_5321_but_is_cited_by_it() {
        this.parseSuccessful("first.last@[IPv6:1111:2222:3333::4444:5555:12.34.56.78]",
                "RFC 4291 disagrees with RFC 5321 but is cited by it");
    }

    @Test
    public void test040__first_DOT_last_ATSIGN_IPv61111222233334444555512_DOT_34_DOT_56_DOT_78__Not_enough_IPv6_groups() {
        this.parseFails2("first.last@[IPv6:1111:2222:3333:4444:5555:12.34.56.78]", "Not enough IPv6 groups");
    }

    @Test
    public void test041__first_DOT_last_ATSIGN_IPv6111122223333444455556666777712_DOT_34_DOT_56_DOT_78__Too_many_IPv6_groups__LEFT_PAREN_6_max_RIGHT_PAREN_() {
        this.parseFails2("first.last@[IPv6:1111:2222:3333:4444:5555:6666:7777:12.34.56.78]", "Too many IPv6 groups (6 max)");
    }

    @Test
    public void test042__first_DOT_last_ATSIGN_IPv61111222233334444555566667777__Not_enough_IPv6_groups() {
        this.parseFails2("first.last@[IPv6:1111:2222:3333:4444:5555:6666:7777]", "Not enough IPv6 groups");
    }

    @Test
    public void test043__first_DOT_last_ATSIGN_IPv6111122223333444455556666777788889999__Too_many_IPv6_groups__LEFT_PAREN_8_max_RIGHT_PAREN_() {
        this.parseFails2("first.last@[IPv6:1111:2222:3333:4444:5555:6666:7777:8888:9999]", "Too many IPv6 groups (8 max)");
    }

    @Test
    public void test044__first_DOT_last_ATSIGN_IPv6111122223333444455556666__Too_many___LEFT_PAREN_can_be_none_or_one_RIGHT_PAREN_() {
        this.parseFails2("first.last@[IPv6:1111:2222::3333::4444:5555:6666]", "Too many \'::\' (can be none or one)");
    }

    @Test
    public void test045__first_DOT_last_ATSIGN_IPv61111222233334444555566667777__RFC_4291_disagrees_with_RFC_5321_but_is_cited_by_it() {
        this.parseSuccessful("first.last@[IPv6:1111:2222:3333::4444:5555:6666:7777]",
                "RFC 4291 disagrees with RFC 5321 but is cited by it");
    }

    @Test
    public void test046__first_DOT_last_ATSIGN_IPv611112222333x44445555__x_is_not_valid_in_an_IPv6_address() {
        this.parseFails2("first.last@[IPv6:1111:2222:333x::4444:5555]", "x is not valid in an IPv6 address");
    }

    @Test
    public void test047__first_DOT_last_ATSIGN_IPv6111122223333344445555__33333_is_not_a_valid_group_in_an_IPv6_address() {
        this.parseFails2("first.last@[IPv6:1111:2222:33333::4444:5555]", "33333 is not a valid group in an IPv6 address");
    }

    @Test
    public void test048__first_DOT_last_ATSIGN_example_DOT_123__TLD_cant_be_all_digits() {
        this.parseSuccessful("first.last@example.123", "TLD can\'t be all digits");
    }

    @Test
    public void test049__first_DOT_last_ATSIGN_com__Mail_host_must_be_second_or_lower_level() {
        this.parseSuccessful("first.last@com", "Mail host must be second- or lower level");
    }

    @Test
    public void test050__first_DOT_last_ATSIGN_xample_DOT_com__Label_cant_begin_with_a_hyphen() {
        this.parseFails2("first.last@-xample.com", "Label can\'t begin with a hyphen");
    }

    @Test
    public void test051__first_DOT_last_ATSIGN_exampl_DOT_com__Label_cant_end_with_a_hyphen() {
        this.parseFails2("first.last@exampl-.com", "Label can\'t end with a hyphen");
    }

    @Test
    public void test052__first_DOT_last_ATSIGN_x234567890123456789012345678901234567890123456789012345678901234_DOT_iana_DOT_org__Label_cant_be_longer_than_63_octets() {
        this.parseFails2("first.last@x234567890123456789012345678901234567890123456789012345678901234.iana.org",
                "Label can\'t be longer than 63 octets");
    }

    @Test
    public void test053__Abc_ATSIGN_def_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Abc\\@def\"@iana.org");
    }

    @Test
    public void test054__Fred_Bloggs_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Fred\\ Bloggs\"@iana.org");
    }

    @Test
    public void test055__Joe_DOT_Blow_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Joe.\\\\Blow\"@iana.org");
    }

    @Test
    public void test056__Abc_ATSIGN_def_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Abc@def\"@iana.org");
    }

    @Test
    public void test057__Fred_Bloggs_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Fred Bloggs\"@iana.org");
    }

    @Test
    public void test058__usermailbox_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("user+mailbox@iana.org");
    }

    @Test
    public void test059__customerdepartmentshipping_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("customer/department=shipping@iana.org");
    }

    @Test
    public void test060__$A12345_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("$A12345@iana.org");
    }

    @Test
    public void test061__defxyzabc_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("!def!xyz%abc@iana.org");
    }

    @Test
    public void test062___somename_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("_somename@iana.org");
    }

    @Test
    public void test063__dclo_ATSIGN_us_DOT_ibm_DOT_com() {
        this.parseSuccessful("dclo@us.ibm.com");
    }

    @Test
    public void test064__abc_ATSIGN_def_ATSIGN_iana_DOT_org__This_example_from_RFC_3696_was_corrected_in_an_erratum() {
        this.parseFails2("abc\\@def@iana.org", "This example from RFC 3696 was corrected in an erratum");
    }

    @Test
    public void test065__abc_ATSIGN_iana_DOT_org__This_example_from_RFC_3696_was_corrected_in_an_erratum() {
        this.parseFails2("abc\\\\@iana.org", "This example from RFC 3696 was corrected in an erratum");
    }

    @Test
    public void test066__peter_DOT_piper_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("peter.piper@iana.org");
    }

    @Test
    public void test067__Doug_Ace_Lovell_ATSIGN_iana_DOT_org__Escaping_can_only_happen_in_a_quoted_string() {
        this.parseFails2("Doug\\ \\\"Ace\\\"\\ Lovell@iana.org", "Escaping can only happen in a quoted string");
    }

    @Test
    public void test068__Doug_Ace_L_DOT__ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Doug \\\"Ace\\\" L.\"@iana.org");
    }

    @Test
    public void test069__abc_ATSIGN_def_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("abc@def@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test070__abc_ATSIGN_def_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("abc\\\\@def@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test071__abc_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("abc\\@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test072___ATSIGN_iana_DOT_org__No_local_part() {
        this.parseFails2("@iana.org", "No local part");
    }

    @Test
    public void test073__doug_ATSIGN___Doug_Lovell_says_this_should_fail() {
        this.parseFails2("doug@", "Doug Lovell says this should fail");
    }

    @Test
    public void test074__qu_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("\"qu@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test075__ote_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("ote\"@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test076___DOT_dot_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2(".dot@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test077__dot_DOT__ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("dot.@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test078__two_DOT__DOT_dot_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("two..dot@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    @Disabled
    public void test079__Doug_Ace_L_DOT__ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("\"Doug \"Ace\" L.\"@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test080__Doug_Ace_L_DOT__ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("Doug\\ \\\"Ace\\\"\\ L\\.@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test081__hello_world_ATSIGN_iana_DOT_org__Doug_Lovell_says_this_should_fail() {
        this.parseFails2("hello world@iana.org", "Doug Lovell says this should fail");
    }

    @Test
    public void test082__gatsby_ATSIGN_f_DOT_sc_DOT_ot_DOT_t_DOT_f_DOT_i_DOT_tzg_DOT_era_DOT_l_DOT_d_DOT___Doug_Lovell_says_this_should_fail() {
        this.parseFails2("gatsby@f.sc.ot.t.f.i.tzg.era.l.d.", "Doug Lovell says this should fail");
    }

    @Test
    public void test083__test_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("test@iana.org");
    }

    @Test
    public void test084__TEST_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("TEST@iana.org");
    }

    @Test
    public void test085__1234567890_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("1234567890@iana.org");
    }

    @Test
    public void test086__testtest_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("test+test@iana.org");
    }

    @Test
    public void test087__testtest_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("test-test@iana.org");
    }

    @Test
    public void test088__test_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("t*est@iana.org");
    }

    @Test
    public void test089__11_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("+1~1+@iana.org");
    }

    @Test
    public void test090___test__ATSIGN_iana_DOT_org() {
        this.parseSuccessful("{_test_}@iana.org");
    }

    @Test
    public void test091___test__ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"[[ test ]]\"@iana.org");
    }

    @Test
    public void test092__test_DOT_test_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("test.test@iana.org");
    }

    @Test
    public void test093__test_DOT_test_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"test.test\"@iana.org");
    }

    @Test
    public void test094__test_DOT_test_ATSIGN_iana_DOT_org__Obsolete_form_but_documented_in_RFC_5322() {
        this.parseSuccessful("test.\"test\"@iana.org", "Obsolete form, but documented in RFC 5322");
    }

    @Test
    public void test095__test_ATSIGN_test_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"test@test\"@iana.org");
    }

    @Test
    public void test096__test_ATSIGN_123_DOT_123_DOT_123_DOT_x123() {
        this.parseSuccessful("test@123.123.123.x123");
    }

    @Test
    public void test097__test_ATSIGN_123_DOT_123_DOT_123_DOT_123__Top_Level_Domain_wont_be_allnumeric__LEFT_PAREN_see_RFC_3696_Section_2_RIGHT_PAREN__DOT__I_disagree_with_Dave_Child_on_this_one_DOT_() {
        this.parseSuccessful("test@123.123.123.123",
                "Top Level Domain won\'t be all-numeric (see RFC 3696 Section 2). I disagree with Dave Child on this one.");
    }

    @Test
    public void test098__test_ATSIGN_123_DOT_123_DOT_123_DOT_123() {
        this.parseSuccessful("test@[123.123.123.123]");
    }

    @Test
    public void test099__test_ATSIGN_example_DOT_iana_DOT_org() {
        this.parseSuccessful("test@example.iana.org");
    }

    @Test
    public void test100__test_ATSIGN_example_DOT_example_DOT_iana_DOT_org() {
        this.parseSuccessful("test@example.example.iana.org");
    }

    @Test
    public void test101__test_DOT_iana_DOT_org() {
        this.parseFails2("test.iana.org");
    }

    @Test
    public void test102__test_DOT__ATSIGN_iana_DOT_org() {
        this.parseFails2("test.@iana.org");
    }

    @Test
    public void test103__test_DOT__DOT_test_ATSIGN_iana_DOT_org() {
        this.parseFails2("test..test@iana.org");
    }

    @Test
    public void test104___DOT_test_ATSIGN_iana_DOT_org() {
        this.parseFails2(".test@iana.org");
    }

    @Test
    public void test105__test_ATSIGN_test_ATSIGN_iana_DOT_org() {
        this.parseFails2("test@test@iana.org");
    }

    @Test
    public void test106__test_ATSIGN__ATSIGN_iana_DOT_org() {
        this.parseFails2("test@@iana.org");
    }

    @Test
    public void test107___test__ATSIGN_iana_DOT_org__No_spaces_allowed_in_local_part() {
        this.parseFails2("-- test --@iana.org", "No spaces allowed in local part");
    }

    @Test
    public void test108__test_ATSIGN_iana_DOT_org__Square_brackets_only_allowed_within_quotes() {
        this.parseFails2("[test]@iana.org", "Square brackets only allowed within quotes");
    }

    @Test
    public void test109__testtest_ATSIGN_iana_DOT_org__Any_character_can_be_escaped_in_a_quoted_string() {
        this.parseSuccessful("\"test\\test\"@iana.org", "Any character can be escaped in a quoted string");
    }

    @Test
    public void test110__testtest_ATSIGN_iana_DOT_org__Quotes_cannot_be_nested() {
        this.parseFails2("\"test\"test\"@iana.org", "Quotes cannot be nested");
    }

    @Test
    public void test112__test_ATSIGN__DOT___Dave_Child_says_so() {
        this.parseFails2("test@.", "Dave Child says so");
    }

    @Test
    public void test113__test_ATSIGN_example_DOT___Dave_Child_says_so() {
        this.parseFails2("test@example.", "Dave Child says so");
    }

    @Test
    public void test114__test_ATSIGN__DOT_org__Dave_Child_says_so() {
        this.parseFails2("test@.org", "Dave Child says so");
    }

    @Test
    public void test115__test_ATSIGN_123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012_DOT_com__255_characters_is_maximum_length_for_domain_DOT__This_is_256_DOT_() {
        this.parseFails2(
                "test@123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012.com",
                "255 characters is maximum length for domain. This is 256.");
    }

    @Test
    public void test116__test_ATSIGN_example__Dave_Child_says_so() {
        this.parseSuccessful("test@example", "Dave Child says so");
    }

    @Test
    public void test117__test_ATSIGN_123_DOT_123_DOT_123_DOT_123__Dave_Child_says_so() {
        this.parseFails2("test@[123.123.123.123", "Dave Child says so");
    }

    @Test
    public void test118__test_ATSIGN_123_DOT_123_DOT_123_DOT_123__Dave_Child_says_so() {
        this.parseFails2("test@123.123.123.123]", "Dave Child says so");
    }

    @Test
    public void test119__NotAnEmail__Phil_Haack_says_so() {
        this.parseFails2("NotAnEmail", "Phil Haack says so");
    }

    @Test
    public void test120___ATSIGN_NotAnEmail__Phil_Haack_says_so() {
        this.parseFails2("@NotAnEmail", "Phil Haack says so");
    }

    @Test
    public void test121__testblah_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"test\\\\blah\"@iana.org");
    }

    @Test
    public void test122__testblah_ATSIGN_iana_DOT_org__Any_character_can_be_escaped_in_a_quoted_string() {
        this.parseSuccessful("\"test\\blah\"@iana.org", "Any character can be escaped in a quoted string");
    }

    @Test
    public void test123__testblah_ATSIGN_iana_DOT_org__Quoted_string_specifically_excludes_carriage_returns_unless_escaped() {
        this.parseSuccessful("\"test\\\rblah\"@iana.org", "Quoted string specifically excludes carriage returns unless escaped");
    }

    @Test
    @Disabled
    public void test124__testblah_ATSIGN_iana_DOT_org__Quoted_string_specifically_excludes_carriage_returns() {
        this.parseFails2("\"test\rblah\"@iana.org", "Quoted string specifically excludes carriage returns");
    }

    @Test
    public void test125__testblah_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"test\\\"blah\"@iana.org");
    }

    @Test
    public void test126__testblah_ATSIGN_iana_DOT_org__Phil_Haack_says_so() {
        this.parseFails2("\"test\"blah\"@iana.org", "Phil Haack says so");
    }

    @Test
    public void test127__customerdepartment_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("customer/department@iana.org");
    }

    @Test
    public void test128___Yosemite_DOT_Sam_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("_Yosemite.Sam@iana.org");
    }

    @Test
    public void test129___ATSIGN_iana_DOT_org() {
        this.parseSuccessful("~@iana.org");
    }

    @Test
    public void test130___DOT_wooly_ATSIGN_iana_DOT_org__Phil_Haack_says_so() {
        this.parseFails2(".wooly@iana.org", "Phil Haack says so");
    }

    @Test
    public void test131__wo_DOT__DOT_oly_ATSIGN_iana_DOT_org__Phil_Haack_says_so() {
        this.parseFails2("wo..oly@iana.org", "Phil Haack says so");
    }

    @Test
    public void test132__pootietang_DOT__ATSIGN_iana_DOT_org__Phil_Haack_says_so() {
        this.parseFails2("pootietang.@iana.org", "Phil Haack says so");
    }

    @Test
    public void test133___DOT__ATSIGN_iana_DOT_org__Phil_Haack_says_so() {
        this.parseFails2(".@iana.org", "Phil Haack says so");
    }

    @Test
    public void test134__Austin_ATSIGN_Powers_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Austin@Powers\"@iana.org");
    }

    @Test
    public void test135__Ima_DOT_Fool_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("Ima.Fool@iana.org");
    }

    @Test
    public void test136__Ima_DOT_Fool_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Ima.Fool\"@iana.org");
    }

    @Test
    public void test137__Ima_Fool_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Ima Fool\"@iana.org");
    }

    @Test
    public void test138__Ima_Fool_ATSIGN_iana_DOT_org__Phil_Haack_says_so() {
        this.parseFails2("Ima Fool@iana.org", "Phil Haack says so");
    }

    @Test
    public void test139__phil_DOT_h_ATSIGN__ATSIGN_ck_ATSIGN_haacked_DOT_com__Escaping_can_only_happen_in_a_quoted_string() {
        this.parseFails2("phil.h\\@\\@ck@haacked.com", "Escaping can only happen in a quoted string");
    }

    @Test
    public void test140__first_DOT_last_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"first\".\"last\"@iana.org");
    }

    @Test
    public void test141__first_DOT_middle_DOT_last_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"first\".middle.\"last\"@iana.org");
    }

    @Test
    public void test142__firstlast_ATSIGN_iana_DOT_org__Contains_an_unescaped_quote() {
        this.parseFails2("\"first\\\\\"last\"@iana.org", "Contains an unescaped quote");
    }

    @Test
    public void test143__first_DOT_last_ATSIGN_iana_DOT_org__obslocalpart_form_as_described_in_RFC_5322() {
        this.parseSuccessful("\"first\".last@iana.org", "obs-local-part form as described in RFC 5322");
    }

    @Test
    public void test144__first_DOT_last_ATSIGN_iana_DOT_org__obslocalpart_form_as_described_in_RFC_5322() {
        this.parseSuccessful("first.\"last\"@iana.org", "obs-local-part form as described in RFC 5322");
    }

    @Test
    public void test145__first_DOT_middle_DOT_last_ATSIGN_iana_DOT_org__obslocalpart_form_as_described_in_RFC_5322() {
        this.parseSuccessful("\"first\".\"middle\".\"last\"@iana.org", "obs-local-part form as described in RFC 5322");
    }

    @Test
    public void test146__first_DOT_middle_DOT_last_ATSIGN_iana_DOT_org__obslocalpart_form_as_described_in_RFC_5322() {
        this.parseSuccessful("\"first.middle\".\"last\"@iana.org", "obs-local-part form as described in RFC 5322");
    }

    @Test
    public void test147__first_DOT_middle_DOT_last_ATSIGN_iana_DOT_org__obslocalpart_form_as_described_in_RFC_5322() {
        this.parseSuccessful("\"first.middle.last\"@iana.org", "obs-local-part form as described in RFC 5322");
    }

    @Test
    public void test148__first_DOT__DOT_last_ATSIGN_iana_DOT_org__obslocalpart_form_as_described_in_RFC_5322() {
        this.parseSuccessful("\"first..last\"@iana.org", "obs-local-part form as described in RFC 5322");
    }

    @Test
    public void test149__foo_ATSIGN_1_DOT_2_DOT_3_DOT_4__RFC_5321_specifies_the_syntax_for_addressliteral_and_does_not_allow_escaping() {
        this.parseFails2("foo@[\\1.2.3.4]", "RFC 5321 specifies the syntax for address-literal and does not allow escaping");
    }

    @Test
    public void test150__firstlast_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"first\\\\\\\"last\"@iana.org");
    }

    @Test
    public void test151__first_DOT_middle_DOT_last_ATSIGN_iana_DOT_org__Backslash_can_escape_anything_but_must_escape_something() {
        this.parseSuccessful("first.\"mid\\dle\".\"last\"@iana.org", "Backslash can escape anything but must escape something");
    }

    @Test
    @Disabled
    public void test152__Test_DOT__Folding_DOT__Whitespace_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("Test.\r\n Folding.\r\n Whitespace@iana.org");
    }

    @Test
    public void test153__first_DOT__DOT_last_ATSIGN_iana_DOT_org__Contains_a_zerolength_element() {
        this.parseFails2("first.\"\".last@iana.org", "Contains a zero-length element");
    }

    @Test
    public void test154__firstlast_ATSIGN_iana_DOT_org__Unquoted_string_must_be_an_atom() {
        this.parseFails2("first\\last@iana.org", "Unquoted string must be an atom");
    }

    @Test
    public void test155__Abc_ATSIGN_def_ATSIGN_iana_DOT_org__Was_incorrectly_given_as_a_valid_address_in_the_original_RFC_3696() {
        this.parseFails2("Abc\\@def@iana.org", "Was incorrectly given as a valid address in the original RFC 3696");
    }

    @Test
    public void test156__Fred_Bloggs_ATSIGN_iana_DOT_org__Was_incorrectly_given_as_a_valid_address_in_the_original_RFC_3696() {
        this.parseFails2("Fred\\ Bloggs@iana.org", "Was incorrectly given as a valid address in the original RFC 3696");
    }

    @Test
    public void test157__Joe_DOT_Blow_ATSIGN_iana_DOT_org__Was_incorrectly_given_as_a_valid_address_in_the_original_RFC_3696() {
        this.parseFails2("Joe.\\\\Blow@iana.org", "Was incorrectly given as a valid address in the original RFC 3696");
    }

    @Test
    public void test158__first_DOT_last_ATSIGN_IPv611112222333344445555666612_DOT_34_DOT_567_DOT_89__IPv4_part_contains_an_invalid_octet() {
        this.parseFails2("first.last@[IPv6:1111:2222:3333:4444:5555:6666:12.34.567.89]",
                "IPv4 part contains an invalid octet");
    }

    @Test
    @Disabled
    public void test159__test_blah_ATSIGN_iana_DOT_org__Folding_white_space_cant_appear_within_a_quoted_pair() {
        this.parseFails2("\"test\\\r\n blah\"@iana.org", "Folding white space can\'t appear within a quoted pair");
    }

    @Test
    public void test160__test_blah_ATSIGN_iana_DOT_org__This_is_a_valid_quoted_string_with_folding_white_space() {
        this.parseSuccessful("\"test\r\n blah\"@iana.org", "This is a valid quoted string with folding white space");
    }

    @Test
    public void test161__c_ATSIGN_Dog_ATSIGN_cartoon_DOT_com__This_is_a_throwaway_example_from_Doug_Lovells_article_DOT__Actually_its_not_a_valid_address_DOT_() {
        this.parseFails2("{^c\\@**Dog^}@cartoon.com",
                "This is a throwaway example from Doug Lovell\'s article. Actually it\'s not a valid address.");
    }

    @Test
    public void test188__name_DOT_lastname_ATSIGN_domain_DOT_com() {
        this.parseSuccessful("name.lastname@domain.com");
    }

    @Test
    public void test189___DOT__ATSIGN_() {
        this.parseFails2(".@");
    }

    @Test
    public void test190__a_ATSIGN_b() {
        this.parseSuccessful("a@b");
    }

    @Test
    public void test191___ATSIGN_bar_DOT_com() {
        this.parseFails2("@bar.com");
    }

    @Test
    public void test192___ATSIGN__ATSIGN_bar_DOT_com() {
        this.parseFails2("@@bar.com");
    }

    @Test
    public void test193__a_ATSIGN_bar_DOT_com() {
        this.parseSuccessful("a@bar.com");
    }

    @Test
    public void test194__aaa_DOT_com() {
        this.parseFails2("aaa.com");
    }

    @Test
    public void test195__aaa_ATSIGN__DOT_com() {
        this.parseFails2("aaa@.com");
    }

    @Test
    public void test196__aaa_ATSIGN__DOT_123() {
        this.parseFails2("aaa@.123");
    }

    @Test
    public void test197__aaa_ATSIGN_123_DOT_123_DOT_123_DOT_123() {
        this.parseSuccessful("aaa@[123.123.123.123]");
    }

    @Test
    public void test198__aaa_ATSIGN_123_DOT_123_DOT_123_DOT_123a__extra_data_outside_ip() {
        this.parseFails2("aaa@[123.123.123.123]a", "extra data outside ip");
    }

    @Test
    public void test199__aaa_ATSIGN_123_DOT_123_DOT_123_DOT_333__not_a_valid_IP() {
        this.parseFails2("aaa@[123.123.123.333]", "not a valid IP");
    }

    @Test
    public void test200__a_ATSIGN_bar_DOT_com_DOT_() {
        this.parseFails2("a@bar.com.");
    }

    @Test
    public void test201__a_ATSIGN_bar() {
        this.parseSuccessful("a@bar");
    }

    @Test
    public void test202__ab_ATSIGN_bar_DOT_com() {
        this.parseSuccessful("a-b@bar.com");
    }

    @Test
    public void test203___ATSIGN_b_DOT_c__TLDs_can_be_any_length() {
        this.parseSuccessful("+@b.c", "TLDs can be any length");
    }

    @Test
    public void test204___ATSIGN_b_DOT_com() {
        this.parseSuccessful("+@b.com");
    }

    @Test
    public void test205__a_ATSIGN_b_DOT_com() {
        this.parseFails2("a@-b.com");
    }

    @Test
    public void test206__a_ATSIGN_b_DOT_com() {
        this.parseFails2("a@b-.com");
    }

    @Test
    public void test207___ATSIGN__DOT__DOT_com() {
        this.parseFails2("-@..com");
    }

    @Test
    public void test208___ATSIGN_a_DOT__DOT_com() {
        this.parseFails2("-@a..com");
    }

    @Test
    public void test209__a_ATSIGN_b_DOT_cofoo_DOT_uk() {
        this.parseSuccessful("a@b.co-foo.uk");
    }

    @Test
    public void test210__hello_my_name_is_ATSIGN_stutter_DOT_com() {
        this.parseSuccessful("\"hello my name is\"@stutter.com");
    }

    @Test
    public void test211__Test_Fail_Ing_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Test \\\"Fail\\\" Ing\"@iana.org");
    }

    @Test
    public void test212__valid_ATSIGN_about_DOT_museum() {
        this.parseSuccessful("valid@about.museum");
    }

    @Test
    public void test213__invalid_ATSIGN_about_DOT_museum() {
        this.parseFails2("invalid@about.museum-");
    }

    @Test
    public void test214__shaitan_ATSIGN_mydomain_DOT_thisisminekthx__Disagree_with_Paul_Gregg_here() {
        this.parseSuccessful("shaitan@my-domain.thisisminekthx", "Disagree with Paul Gregg here");
    }

    @Test
    public void test215__test_ATSIGN__DOT__DOT__DOT__DOT__DOT__DOT__DOT__DOT__DOT__DOT__DOT_com___DOT__DOT__DOT__DOT__DOT__DOT_() {
        this.parseFails2("test@...........com", "......");
    }

    @Test
    public void test216__foobar_ATSIGN_192_DOT_168_DOT_0_DOT_1__ip_need_to_be_() {
        this.parseSuccessful("foobar@192.168.0.1", "ip need to be []");
    }

    @Test
    public void test217__JoeBlow_ATSIGN_iana_DOT_org() {
        this.parseSuccessful("\"Joe\\\\Blow\"@iana.org");
    }

    @Test
    public void test218__Invalid__Folding__Whitespace_ATSIGN_iana_DOT_org__This_isnt_FWS_so_Dominic_Sayers_says_its_invalid() {
        this.parseFails2("Invalid \\\n Folding \\\n Whitespace@iana.org",
                "This isn\'t FWS so Dominic Sayers says it\'s invalid");
    }

    @Test
    public void test220__useruucppath_ATSIGN_berkeley_DOT_edu() {
        this.parseSuccessful("user%uucp!path@berkeley.edu");
    }

    @Test
    @Disabled
    public void test223__first_DOT_last__ATSIGN_iana_DOT_org__FWS_is_allowed_after_local_part__LEFT_PAREN_this_is_similar_to_152_but_is_the_test_proposed_by_John_Kloor_RIGHT_PAREN_() {
        this.parseSuccessful("first.last @iana.org",
                "FWS is allowed after local part (this is similar to #152 but is the test proposed by John Kloor)");
    }

    @Test
    @Disabled
    public void test224__test_DOT____obs_ATSIGN_syntax_DOT_com__obsfws_allows_multiple_lines__LEFT_PAREN_test_2_space_before_break_RIGHT_PAREN_() {
        this.parseSuccessful("test. \r\n \r\n obs@syntax.com", "obs-fws allows multiple lines (test 2: space before break)");
    }

    @Test
    public void test225__test_DOT__obs_ATSIGN_syntax_DOT_com__obsfws_must_have_at_least_one_WSP_per_line() {
        this.parseFails2("test.\r\n\r\n obs@syntax.com", "obs-fws must have at least one WSP per line");
    }

    @Test
    public void test226__Unicode_NULL__ATSIGN_char_DOT_com__Can_have_escaped_Unicode_Character_NULL__LEFT_PAREN_U0000_RIGHT_PAREN_() {
        this.parseSuccessful("\"Unicode NULL \\\0\"@char.com", "Can have escaped Unicode Character \'NULL\' (U+0000)");
    }

    @Test
    @Disabled
    public void test227__Unicode_NULL__ATSIGN_char_DOT_com__Cannot_have_unescaped_Unicode_Character_NULL__LEFT_PAREN_U0000_RIGHT_PAREN_() {
        this.parseFails2("\"Unicode NULL \0\"@char.com", "Cannot have unescaped Unicode Character \'NULL\' (U+0000)");
    }

    @Test
    public void test228__Unicode_NULL__ATSIGN_char_DOT_com__Escaped_Unicode_Character_NULL__LEFT_PAREN_U0000_RIGHT_PAREN__must_be_in_quoted_string() {
        this.parseFails2("Unicode NULL \\\0@char.com",
                "Escaped Unicode Character \'NULL\' (U+0000) must be in quoted string");
    }

    @Test
    public void test229__cdburgess$_test_ATSIGN_gmail_DOT_com__Example_given_in_comments() {
        this.parseSuccessful("cdburgess+!#$%&\'*-/=?+_{}|~test@gmail.com", "Example given in comments");
    }

    @Test
    public void test230__first_DOT_last_ATSIGN_IPv6a2a3a4b1b2b3b4___only_elides_one_zero_group__LEFT_PAREN_IPv6_authority_is_RFC_4291_RIGHT_PAREN_() {
        this.parseSuccessful("first.last@[IPv6:::a2:a3:a4:b1:b2:b3:b4]",
                ":: only elides one zero group (IPv6 authority is RFC 4291)");
    }

    @Test
    public void test231__first_DOT_last_ATSIGN_IPv6a1a2a3a4b1b2b3___only_elides_one_zero_group__LEFT_PAREN_IPv6_authority_is_RFC_4291_RIGHT_PAREN_() {
        this.parseSuccessful("first.last@[IPv6:a1:a2:a3:a4:b1:b2:b3::]",
                ":: only elides one zero group (IPv6 authority is RFC 4291)");
    }

    @Test
    public void test232__first_DOT_last_ATSIGN_IPv6__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test233__first_DOT_last_ATSIGN_IPv6__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test234__first_DOT_last_ATSIGN_IPv6__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test235__first_DOT_last_ATSIGN_IPv6b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test236__first_DOT_last_ATSIGN_IPv6b4__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:::b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test237__first_DOT_last_ATSIGN_IPv6b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::::b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test238__first_DOT_last_ATSIGN_IPv6b3b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::b3:b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test239__first_DOT_last_ATSIGN_IPv6b3b4__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:::b3:b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test240__first_DOT_last_ATSIGN_IPv6b3b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::::b3:b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test241__first_DOT_last_ATSIGN_IPv6a1b4__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:a1::b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test242__first_DOT_last_ATSIGN_IPv6a1b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:::b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test243__first_DOT_last_ATSIGN_IPv6a1__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test244__first_DOT_last_ATSIGN_IPv6a1__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:a1::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test245__first_DOT_last_ATSIGN_IPv6a1__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test246__first_DOT_last_ATSIGN_IPv6a1a2__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:a2:]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test247__first_DOT_last_ATSIGN_IPv6a1a2__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:a1:a2::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test248__first_DOT_last_ATSIGN_IPv6a1a2__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:a2:::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test249__first_DOT_last_ATSIGN_IPv60123456789abcdef__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:0123:4567:89ab:cdef::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test250__first_DOT_last_ATSIGN_IPv60123456789abCDEF__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:0123:4567:89ab:CDEF::]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test251__first_DOT_last_ATSIGN_IPv6a3a4b1ffff11_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:::a3:a4:b1:ffff:11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test252__first_DOT_last_ATSIGN_IPv6a2a3a4b1ffff11_DOT_22_DOT_33_DOT_44___only_elides_one_zero_group__LEFT_PAREN_IPv6_authority_is_RFC_4291_RIGHT_PAREN_() {
        this.parseSuccessful("first.last@[IPv6:::a2:a3:a4:b1:ffff:11.22.33.44]",
                ":: only elides one zero group (IPv6 authority is RFC 4291)");
    }

    @Test
    public void test253__first_DOT_last_ATSIGN_IPv6a1a2a3a411_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:a1:a2:a3:a4::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test254__first_DOT_last_ATSIGN_IPv6a1a2a3a4b111_DOT_22_DOT_33_DOT_44___only_elides_one_zero_group__LEFT_PAREN_IPv6_authority_is_RFC_4291_RIGHT_PAREN_() {
        this.parseSuccessful("first.last@[IPv6:a1:a2:a3:a4:b1::11.22.33.44]",
                ":: only elides one zero group (IPv6 authority is RFC 4291)");
    }

    @Test
    public void test255__first_DOT_last_ATSIGN_IPv611_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test256__first_DOT_last_ATSIGN_IPv611_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test257__first_DOT_last_ATSIGN_IPv6a111_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test258__first_DOT_last_ATSIGN_IPv6a111_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:a1::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test259__first_DOT_last_ATSIGN_IPv6a111_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test260__first_DOT_last_ATSIGN_IPv6a1a211_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:a1:a2::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test261__first_DOT_last_ATSIGN_IPv6a1a211_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:a2:::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test262__first_DOT_last_ATSIGN_IPv60123456789abcdef11_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:0123:4567:89ab:cdef::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test263__first_DOT_last_ATSIGN_IPv60123456789abcdef11_DOT_22_DOT_33_DOT_xx__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:0123:4567:89ab:cdef::11.22.33.xx]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test264__first_DOT_last_ATSIGN_IPv60123456789abCDEF11_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:0123:4567:89ab:CDEF::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test265__first_DOT_last_ATSIGN_IPv60123456789abCDEFF11_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:0123:4567:89ab:CDEFF::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test266__first_DOT_last_ATSIGN_IPv6a1a4b1b411_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1::a4:b1::b4:11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test267__first_DOT_last_ATSIGN_IPv6a111_DOT_22_DOT_33__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1::11.22.33]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test268__first_DOT_last_ATSIGN_IPv6a111_DOT_22_DOT_33_DOT_44_DOT_55__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1::11.22.33.44.55]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test269__first_DOT_last_ATSIGN_IPv6a1b211_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1::b211.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test270__first_DOT_last_ATSIGN_IPv6a1b211_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseSuccessful("first.last@[IPv6:a1::b2:11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test271__first_DOT_last_ATSIGN_IPv6a1b211_DOT_22_DOT_33_DOT_44__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1::b2::11.22.33.44]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test272__first_DOT_last_ATSIGN_IPv6a1b3__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1::b3:]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test273__first_DOT_last_ATSIGN_IPv6a2b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::a2::b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test274__first_DOT_last_ATSIGN_IPv6a1a2a3a4b1b2b3__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:a2:a3:a4:b1:b2:b3:]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test275__first_DOT_last_ATSIGN_IPv6a2a3a4b1b2b3b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6::a2:a3:a4:b1:b2:b3:b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test276__first_DOT_last_ATSIGN_IPv6a1a2a3a4b1b2b3b4__IPv6_authority_is_RFC_4291() {
        this.parseFails2("first.last@[IPv6:a1:a2:a3:a4::b1:b2:b3:b4]", "IPv6 authority is RFC 4291");
    }

    @Test
    public void test277__test_ATSIGN_test_DOT_com__test_DOT_com_has_an_Arecord_but_not_an_MXrecord() {
        this.parseSuccessful("test@test.com", "test.com has an A-record but not an MX-record");
    }

    @Test
    @Disabled
    public void test278__test_ATSIGN_example_DOT_com__Address_has_a_newline_at_the_end() {
        this.parseFails2("test@example.com", "Address has a newline at the end");
    }

    @Test
    public void test279__test_ATSIGN_xnexample_DOT_com__Address_is_at_an_Internationalized_Domain_Name__LEFT_PAREN_Punycode_RIGHT_PAREN_() {
        this.parseSuccessful("test@xn--example.com", "Address is at an Internationalized Domain Name (Punycode)");
    }

    @Test
    @Disabled
    public void test280__test_ATSIGN_BXcher_DOT_ch__Address_is_at_an_Internationalized_Domain_Name__LEFT_PAREN_UTF8_RIGHT_PAREN_() {
        this.parseSuccessful("test@B�cher.ch", "Address is at an Internationalized Domain Name (UTF-8)");
    }

    // --- end of tests generator: DominicsayersComIsemailEmailAddressTestGenerator file: www.dominicsayers.com-isemail-tests.xml ---

    // helpers

    private void parseSuccessful(final String address) {
        this.parseSuccessful(address, null);
    }

    private void parseSuccessful(final String address, final String comment) {
        try {
            EmailAddress.parse(address);
        } catch (final RuntimeException expected) {
            throw new IllegalArgumentException(address + '=' + expected.getMessage() + " " + this.makeEmptyIfNull(comment),
                    expected);
        }
    }

    private void parseFails2(final String address) {
        this.parseFails2(address, null);
    }

    private void parseFails2(final String address, final String comment) {
        assertThrows(IllegalArgumentException.class, () -> {
            this.parse(address);
        }, "Invalid email " + CharSequences.quoteAndEscape(address) + " should have failed="
                + this.makeEmptyIfNull(comment));
    }

    private String makeEmptyIfNull(final String string) {
        return null != string ? string : "";
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentEmail() {
        this.checkNotEquals(EmailAddress.parse("different@server"));
    }

    // toString.................................................................

    @Test
    public void testToString() {
        final String email = "hello@example.com";
        this.toStringAndCheck(EmailAddress.parse(email), email);
    }

    @Override
    public Class<EmailAddress> type() {
        return EmailAddress.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public EmailAddress createObject() {
        return EmailAddress.parse("user@example.com");
    }

    // ParseStringTesting ..................................................................................................

    @Override
    public EmailAddress parse(final String text) {
        return EmailAddress.parse(text);
    }

    @Override
    public RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    // SerializationTesting ..................................................................................................

    @Override
    public EmailAddress serializableInstance() {
        return EmailAddress.parse("user@server");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
