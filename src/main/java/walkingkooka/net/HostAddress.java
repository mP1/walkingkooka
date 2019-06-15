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

package walkingkooka.net;

import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;

import java.io.Serializable;

/**
 * A {@link Value} that represent a host address which may be a name or ip address in dot form etc. Note the actual address is only validated for
 * syntactical correctness no network query is ever attempted. <br>
 * Most of the tests and info was gathered from <a href="http://www.dominicsayers.com/source/beta/is_email/test/?all"></a>}
 */
public final class HostAddress implements Value<String>,
        Comparable<HostAddress>,
        HashCodeEqualsDefined,
        Serializable {

    /**
     * Creates a {@link HostAddress} after verifying address and components, values etc.
     */
    public static HostAddress with(final String address) {
        Whitespace.failIfNullOrEmptyOrWhitespace(address, "address");

        return HostAddress.with(address, 0, false, false);
    }

    /**
     * Processes an address within an email.
     */
    public static HostAddress withEmail(final String address, final int offset) {
        Whitespace.failIfNullOrEmptyOrWhitespace(address, "address");

        return HostAddress.with(address, offset, true, true);
    }

    /**
     * Useful when one wishes to create a {@link HostAddress} from the end of a {@link String}.
     */
    static private HostAddress with(final String address,
                                    final int offset,
                                    final boolean mayHaveSquareBrackets,
                                    final boolean email) {
        final int length = address.length();
        final int trueLength = length - offset;
        if (trueLength >= HostAddress.MAX_LENGTH) {
            throw new IllegalArgumentException(HostAddress.tooLong(trueLength, address));
        }

        int start = offset;
        int end = length;
        boolean missingClosingBracket = false;

        // if (email) {
        final boolean openSquareBracket = '[' == address.charAt(offset);
        if (openSquareBracket) {
            start++;

            // emails may have IPv6: after a [.
            if (email) {
                if ((start + 5) < length) {
                    if ((address.charAt(start) == 'I') && //
                            (address.charAt(start + 1) == 'P') && //
                            (address.charAt(start + 2) == 'v') && //
                            (address.charAt(start + 3) == '6') && //
                            (address.charAt(start + 4) == ':')) {
                        start = start + 5;
                    }
                }
            }

            end--;
            if (']' != address.charAt(length - 1)) {
                missingClosingBracket = true;
                end++;
            }
        }
        // only complain about [ if not email
        HostAddress hostAddress;

        // try parsing as a name, then ip4 and finally ip6.
        for (; ; ) {
            final HostAddressProblem name = HostAddress.tryParseName(address, start, end);
            if (null == name) {
                hostAddress = new HostAddress(address.substring(offset, length), HostAddress.EMPTY);
                break;
            }
            if (name.stopTrying()) {
                name.report(address);
            }

            final Object ip4 = HostAddress.tryParseIp4(address, start, end, false);
            if (ip4 instanceof Long) {
                hostAddress = new HostAddress(address.substring(offset, length), HostAddress.toBytes((Long) ip4));
                break;
            }
            final HostAddressProblem ip4Problem = (HostAddressProblem) ip4;
            if (ip4Problem.stopTrying()) {
                ip4Problem.report(address);
            }

            final Object ip6 = HostAddress.tryParseIp6(address, start, end);
            if (ip6 instanceof byte[]) {
                hostAddress = new HostAddress(address.substring(offset, length), (byte[]) ip6);
                break;
            }
            final HostAddressProblem ip6Problem = (HostAddressProblem) ip6;
            if (ip6Problem.stopTrying()) {
                ip6Problem.report(address);
            }

            // report the invalid character that has the lowest at
            HostAddressProblem report = name;
            if (report.priority() < ip4Problem.priority()) {
                report = ip4Problem;
            }
            if (report.priority() < ip6Problem.priority()) {
                report = ip6Problem;
            }
            report.report(address);
        }

        // brackets only valid for ip6 OR ip4 AND mayHaveXXX
        if (openSquareBracket) {
            if (false == (hostAddress.isIp6() || (hostAddress.isIp4() && mayHaveSquareBrackets))) {
                HostAddressInvalidCharacterProblem.with(offset).report(address);
            }
        }
        if (missingClosingBracket) {
            HostAddressIncompleteProblem.INSTANCE.report(address);
        }

        return hostAddress;
    }

    /**
     * The entire address is too long.
     */
    static String tooLong(final int length, final String host) {
        return "Host length > " + HostAddress.MAX_LENGTH + "=" + length + " in " + CharSequences.quote(host);
    }

    /**
     * The maximum number of characters that may appear in a host
     */
    final static int MAX_LENGTH = 254;

    /**
     * The number of octets in an ip4 address.
     */
    private final static int IP4_OCTET_COUNT = Ip4Address.OCTET_COUNT;

    /**
     * The number of groups in an ip6 address.
     */
    private final static int IP6_GROUP_COUNT = 8;

    /**
     * The number of characters in an ip6 address block
     */
    private final static int IP6_GROUP_LENGTH = 4;

    /**
     * The size in bytes required to hold an ip6 address
     */
    final static int IP6_OCTET_COUNT = Ip6Address.OCTET_COUNT;

    /**
     * The maximum number of ip6 blocks before an ip4 dot address
     */
    private final static int MAX_BLOCKS_BEFORE_IP4 = 8 - 2;

    /**
     * The maximum number of characters in a label.
     */
    final static int MAX_LABEL_LENGTH = 64;

    /**
     * The maximum value that may be held in an ip4 octet.
     */
    private final static int MAX_OCTET_VALUE = 255;

    /**
     * An empty byte array
     */
    private final static byte[] EMPTY = new byte[0];

    /**
     * Tries to parse an {@link String address} returning null if successful or {@link HostAddressProblem}
     */
    static HostAddressProblem tryParseName(final String address, final int start, final int end) {
        HostAddressProblem problem = null;

        final int last = end - 1;
        int labelStart = start;
        boolean ip4 = true;
        char previous = 0;

        for (int i = start; i < end; i++) {
            final char c = address.charAt(i);
            final char wasPrevious = previous;
            previous = c;

            // special case for the first letter
            if (labelStart == i) {
                if (Ip4Address.SEPARATOR.equals(c) || ('-' == c)) {
                    problem = HostAddressInvalidCharacterProblem.with(i);
                    break;
                }
                if (Character.isLetter(c)) {
                    ip4 = false;
                    continue;
                }
                if (Character.isDigit(c)) {
                    continue;
                }
                problem = HostAddressInvalidCharacterProblem.with(i);
                break;
            }

            if (last == i) {
                if (Ip4Address.SEPARATOR.equals(c)) {
                    problem = HostAddressIncompleteProblem.INSTANCE;
                    break;
                }
                // cannot end with dash
                if ('-' == c) {
                    problem = HostAddressInvalidCharacterProblem.with(i);
                    break;
                }
                if (Character.isLetter(c)) {
                    problem = HostAddress.checkLength(labelStart, i + 1);
                    if (null != problem) {
                        break;
                    }
                    ip4 = false; // cannot be ip4
                    labelStart = i + 1;
                    continue;
                }
                if (Character.isDigit(c)) {
                    problem = HostAddress.checkLength(labelStart, i + 1);
                    if (null != problem) {
                        break;
                    }
                    labelStart = i + 1;
                    continue;
                }
                problem = HostAddressInvalidCharacterProblem.with(i);
                break;
            }

            // not the first character or last in this address
            if (Ip4Address.SEPARATOR.equals(c)) {
                if ('-' == wasPrevious) {
                    problem = HostAddressInvalidCharacterProblem.with(i - 1);
                    break;
                }

                problem = HostAddress.checkLength(labelStart, i);
                if (null != problem) {
                    break;
                }
                labelStart = i + 1;
                continue;
            }
            if (Character.isDigit(c)) {
                continue;
            }
            if (('-' == c) || Character.isLetter(c)) {
                ip4 = false;// ip4 cannot have dashes or letters
                continue;
            }
            if (':' == c) {
                problem = HostAddressProbablyIp6Problem.INSTANCE;
                break;
            }
            problem = HostAddressInvalidCharacterProblem.with(i);
            break;
        }

        // if no letters were found fail as probably an ip4
        if ((null == problem) && ip4) {
            problem = HostAddressProbablyIp4Problem.INSTANCE;
        }

        return problem;
    }

    /**
     * Tries to parse an {@link String address} returning an array with the address octets as a {@link Long } form if successful or
     * {@link HostAddressProblem}
     */
    static Object tryParseIp4(final String address, final int start, final int end, final boolean insideIp6) {
        HostAddressProblem problem = HostAddressIncompleteProblem.INSTANCE;
        long value = 0;

        if (start != end) {
            problem = null;

            final int last = end - 1;
            int octetStart = 0;
            int octetCounter = 0;
            int octetValue = 0;

            for (int i = start; i < end; i++) {
                // check its not too long as in a String of zeroes....
                problem = HostAddress.checkLength(octetStart, i);
                if (null != problem) {
                    break;
                }
                final char c = address.charAt(i);
                if (Ip4Address.SEPARATOR.equals(c)) {
                    // might be starting with dot or double dot (an empty octet).
                    if (i == octetStart) {
                        problem = HostAddressInvalidCharacterProblem.with(i);
                        break;
                    }
                    if (octetValue > HostAddress.MAX_OCTET_VALUE) {
                        problem = HostAddressInvalidValueProblem.with(octetStart);
                        break;
                    }
                    octetCounter++;
                    if (HostAddress.IP4_OCTET_COUNT == octetCounter) {
                        problem = HostAddressInvalidCharacterProblem.with(i);
                        break;
                    }
                    // check if this dot is the last character
                    if (last == i) {
                        problem = HostAddressInvalidCharacterProblem.with(last);
                        break;
                    }
                    octetStart = i + 1;
                    value = (value << 8) | octetValue;
                    octetValue = 0;
                    continue;
                }
                // if not inside ip6 report a probably ip6 for these characters
                if (false == insideIp6) {
                    if (Ip6Address.SEPARATOR.equals(c) || ((c >= 'A') && (c <= 'F')) || ((c >= 'a') && (c <= 'f'))) {
                        problem = HostAddressProbablyIp6Problem.INSTANCE;
                        break;
                    }
                }
                // expecting a digit.
                final int digit = Character.digit(c, 10);
                if (digit < 0) {
                    problem = HostAddressInvalidCharacterProblem.with(i);
                    break;
                }
                octetValue = (octetValue * 10) + digit;
                // double check might be the same digit...if so check all 4 octets exist etc.
                if (last == i) {
                    problem = HostAddress.checkLength(octetStart, end);
                    if (null != problem) {
                        break;
                    }
                    if (octetValue > HostAddress.MAX_OCTET_VALUE) {
                        problem = HostAddressInvalidValueProblem.with(octetStart);
                        break;
                    }
                    value = (value << 8) | octetValue;
                    if (octetCounter != (HostAddress.IP4_OCTET_COUNT - 1)) {
                        problem = HostAddressIncompleteProblem.INSTANCE;
                    }
                    break;
                }
            }
        }

        // if not a problem return value
        return null != problem ? problem : (Long) value;
    }

    /**
     * Tries to parse an {@link String address} returning an array with the ip6 as a byte array form if successful or {@link HostAddressProblem}.
     */
    static Object tryParseIp6(final String address, final int start, final int end) {
        HostAddressProblem problem = HostAddressIncompleteProblem.INSTANCE;
        byte[] values = null;

        if (start != end) {
            problem = null; // clear

            final int last = end - 1; // index of last character to be processed
            values = new byte[HostAddress.IP6_OCTET_COUNT];

            int groupCounter = 0;
            int emptyGroupAt = -1;

            int groupDigitCounter = 0; // complain when more than 4 digits are in sequence
            int groupValue = 0; // the value of hex digits in the current group

            for (int i = start; i < end; i++) {
                final char c = address.charAt(i);

                // end of a group,
                if (Ip6Address.SEPARATOR.equals(c)) {
                    // if not empty (double colon) then invalid character
                    if (start == i) {
                        // if only a solitary colon then incomplete
                        if (i == last) {
                            problem = HostAddressIncompleteProblem.INSTANCE;
                            break;
                        }
                        // complain if next is not also a colon.
                        if (false == Ip6Address.SEPARATOR.equals(address.charAt(i + 1))) {
                            problem = HostAddressInvalidCharacterProblem.with(0);
                            break;
                        }
                        // continue next will be a colon.
                        continue;
                    }

                    // if last character is colon, and not an empty
                    if ((i == last) && (0 != groupDigitCounter)) {
                        problem = HostAddressIncompleteProblem.INSTANCE;
                        break;
                    }

                    // empty found.. verify first and only
                    if (0 == groupDigitCounter) {
                        if (-1 != emptyGroupAt) {
                            problem = HostAddressInvalidCharacterProblem.with(i);
                            break;
                        }
                        emptyGroupAt = groupCounter;
                    }
                    groupCounter++;
                    // too many groups ?
                    if (groupCounter > HostAddress.IP6_GROUP_COUNT) {
                        problem = HostAddressInvalidCharacterProblem.with(i);
                        break;
                    }
                    // if another block follows when already full invalid character
                    if ((last != i) && (groupCounter == HostAddress.IP6_GROUP_COUNT)) {
                        problem = HostAddressInvalidCharacterProblem.with(i);
                        break;
                    }

                    if (-1 == emptyGroupAt) {
                        // if an empty group has not been found save the value in the next slot
                        final int index = (groupCounter - 1) * 2;
                        values[index] = (byte) (groupValue >> 8);
                        values[index + 1] = (byte) (groupValue & 0xff);
                    } else {
                        // shift the values after the fill and then save the value in the last slot.
                        final int index = emptyGroupAt * 2;
                        System.arraycopy(values, index + 2, values, index, 16 - index - 2);

                        values[HostAddress.IP6_OCTET_COUNT - 2] = (byte) (groupValue >> 8);
                        values[HostAddress.IP6_OCTET_COUNT - 1] = (byte) (groupValue & 0xff);
                    }

                    groupDigitCounter = 0;
                    groupValue = 0;
                    continue;
                }
                // if dot found remainder must be in ip4 form.
                if ('.' == c) {
                    // if no empty was found must have already processed 6 groups
                    if (-1 == emptyGroupAt) {
                        if (groupCounter != HostAddress.MAX_BLOCKS_BEFORE_IP4) {
                            problem = HostAddressInvalidCharacterProblem.with(i);
                            break;
                        }
                    } else {
                        if (groupCounter > HostAddress.MAX_BLOCKS_BEFORE_IP4) {
                            problem = HostAddressInvalidCharacterProblem.with(i);
                            break;
                        }
                    }

                    // remainder must be an ip4 back over original chars
                    final Object ip4 = HostAddress.tryParseIp4(address, i - groupDigitCounter, end, true);
                    if (ip4 instanceof HostAddressProblem) {
                        problem = (HostAddressProblem) ip4;
                        break;
                    }

                    // shift bytes after fill before saving value.
                    if (-1 != emptyGroupAt) {
                        final int index = emptyGroupAt * 2;
                        System.arraycopy(values, index + 4, values, index, 16 - index - 4);
                    }
                    // save ip4 in last 4 bytes
                    final long ipLong = (Long) ip4;
                    values[HostAddress.IP6_OCTET_COUNT - 4] = (byte) (ipLong >> 24);
                    values[HostAddress.IP6_OCTET_COUNT - 3] = (byte) (ipLong >> 16);
                    values[HostAddress.IP6_OCTET_COUNT - 2] = (byte) (ipLong >> 8);
                    values[HostAddress.IP6_OCTET_COUNT - 1] = (byte) (ipLong & 0xff);
                    break;
                }

                // expecting a hex digit
                final int digit = Character.digit(c, 16);
                if (digit < 0) {
                    problem = HostAddressInvalidCharacterProblem.with(i);
                    break;
                }
                if (HostAddress.IP6_GROUP_LENGTH == groupDigitCounter) {
                    problem = HostAddressInvalidValueProblem.with(i - groupDigitCounter);// gives start
                    break;
                }
                groupDigitCounter++;
                groupValue = (groupValue * 16) + digit;

                // might be last character copy into array etc.
                if (last == i) {
                    // did not include a empty block and was wrong number of blocks...
                    if ((-1 == emptyGroupAt) && ((groupCounter + 1) != HostAddress.IP6_GROUP_COUNT)) {
                        problem = HostAddressIncompleteProblem.INSTANCE;
                        break;
                    }

                    if (-1 == emptyGroupAt) {
                        final int index = groupCounter * 2;
                        values[index] = (byte) (groupValue >> 8);
                        values[index + 1] = (byte) (groupValue & 0xff);
                    } else {
                        // fill in mode
                        // shift filled in which are right flushed
                        final int index = emptyGroupAt * 2;
                        System.arraycopy(values, index + 2, values, index, 16 - index - 2);
                        values[HostAddress.IP6_OCTET_COUNT - 2] = (byte) (groupValue >> 8);
                        values[HostAddress.IP6_OCTET_COUNT - 1] = (byte) (groupValue & 0xff);
                    }
                    break;// !stop!
                }
            }
        }

        return problem != null ? problem : values;
    }

    /**
     * Helper that performs a hex dump on the array of byte values.
     */
    static String hex(final byte[] values) {
        final StringBuilder builder = new StringBuilder();
        for (final byte value : values) {
            final int unsigned = 0xff & value;
            if (unsigned < 0x10) {
                builder.append('0');
            }

            builder.append(Integer.toHexString(unsigned));
        }
        return builder.toString();
    }

    /**
     * Helper that verifies that a label or octet length is not not too long.
     */
    static private HostAddressProblem checkLength(final int start, final int end) {
        return (end - start) >= HostAddress.MAX_LABEL_LENGTH ?
                HostAddressInvalidLengthProblem.with(start) :
                null;
    }

    /**
     * Accepts a long and returns an array in big endian form.
     */
    static byte[] toBytes(final long value) {
        return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
    }

    /**
     * Private constructor use static factory.
     */
    private HostAddress(final String address, final byte[] values) {
        super();

        this.address = address;
        this.values = values;
    }

    // Value

    /**
     * The host address which may be a name, octets etc.
     */
    @Override
    public String value() {
        return this.address;
    }

    private final String address;

    // HostAddress

    /**
     * Tests and returns true if the host address is a host name.
     */
    public boolean isName() {
        return this.values.length == 0;
    }

    /**
     * Returns an {@link IpAddress} when possible or <code>null</code>
     */
    public IpAddress isIpAddress() {
        IpAddress address = this.ipAddress;

        if (null == address) {
            final byte[] values = this.values;
            if (values.length == HostAddress.IP4_OCTET_COUNT) {
                address = IpAddress.ip4(values);
                this.ipAddress = address;
            }
            if (values.length == HostAddress.IP6_OCTET_COUNT) {
                address = IpAddress.ip6(values);
                this.ipAddress = address;
            }
        }

        return address;
    }

    private transient IpAddress ipAddress;

    /**
     * Tests and returns true if this contains an IP4 octet address.
     *
     * <pre>
     * 1.2.3.4
     * </pre>
     */
    private boolean isIp4() {
        return this.values.length == HostAddress.IP4_OCTET_COUNT;
    }

    /**
     * Tests and returns true if this contains an IP6 value.
     */
    private boolean isIp6() {
        return this.values.length == HostAddress.IP6_OCTET_COUNT;
    }

    /**
     * Returns the bytes from an IP4 or IP6 byte address.
     */
    public byte[] values() {
        return this.values.clone();
    }

    private final byte[] values;

    // Object

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.address);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HostAddress &&
                        this.equals0((HostAddress) other);
    }

    private boolean equals0(final HostAddress other) {
        return CASE_SENSITIVITY.equals(this.address, other.address);
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    @Override
    public String toString() {
        return this.address;
    }

    final void toString0(final StringBuilder b) {
        b.append(this.address);
    }

    // Comparable.............................................................................................

    @Override
    public int compareTo(final HostAddress other) {
        return CASE_SENSITIVITY.comparator().compare(this.value(), other.value());
    }

    // Serializable.........................................................................................

    private final static long serialVersionUID = 1L;
}
