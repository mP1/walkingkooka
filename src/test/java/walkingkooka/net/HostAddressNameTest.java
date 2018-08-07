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

package walkingkooka.net;

import junit.framework.Assert;
import org.junit.Test;
import walkingkooka.test.TestCase;

import java.util.Arrays;

public final class HostAddressNameTest extends TestCase {

    @Test
    public void testProbablyIp6Fails() {
        this.parseFails("a.b:c", HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testDotAtStartFails() {
        this.parseFails(".n", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testDotAtEndFails() {
        this.parseFails("n.", HostAddressIncompleteProblem.INSTANCE);
    }

    @Test
    public void testDashAtStartFails() {
        this.parseFails("-n", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testDashAtEndFails() {
        final String name = "n-";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('-')));
    }

    @Test
    public void testOneLabelInvalidAtStartFails() {
        this.parseFails("!b", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testOneLabelInvalidAtStarts2Fails() {
        this.parseFails("!bc", HostAddressInvalidCharacterProblem.with(0));
    }

    @Test
    public void testOneLabelInvalidFails() {
        this.parseFails("n!ame", HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testOneLabelEndsWithInvalidFails() {
        this.parseFails("a!", HostAddressInvalidCharacterProblem.with(1));
    }

    @Test
    public void testOneLabelEndsWithInvalid2Fails() {
        this.parseFails("ab!", HostAddressInvalidCharacterProblem.with(2));
    }

    @Test
    public void testDashThenDotFails() {
        final String address = "exampl-.com";
        this.parseFails(address, HostAddressInvalidCharacterProblem.with(address.indexOf('-')));
    }

    @Test
    public void testSecondLabelInvalidAtStartFails() {
        final String name = "a.!b";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testSecondLabelInvalidFails() {
        final String name = "a.b!c.d";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testSecondLabelEndsWithInvalidFails() {
        final String name = "a.b!.c";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testSecondLabelEndsWithDashFails() {
        final String name = "a.b-.c";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('-')));
    }

    @Test
    public void testLastLabelEndWithInvalidFails() {
        final String name = "a.b!";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('!')));
    }

    @Test
    public void testLastLabelEndsWithDashFails() {
        final String name = "a.b-";
        this.parseFails(name, HostAddressInvalidCharacterProblem.with(name.indexOf('-')));
    }

    @Test
    public void testIp4PartialFails() {
        final String name = "1.2";
        this.parseFails(name, HostAddressProbablyIp4Problem.INSTANCE);
    }

    @Test
    public void testIp4Fails() {
        final String name = "1.2.3.4";
        this.parseFails(name, HostAddressProbablyIp4Problem.INSTANCE);
    }

    @Test
    public void testIp4WithExtraOctetsFails() {
        final String name = "1.2.3.4.5.6";
        this.parseFails(name, HostAddressProbablyIp4Problem.INSTANCE);
    }

    @Test
    public void testIp6Fails() {
        final String name = "1:2:3:4:5:6:7:8";
        this.parseFails(name, HostAddressProbablyIp6Problem.INSTANCE);
    }

    @Test
    public void testLabelTooLongFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        final String name = new String(array);
        this.parseFails(name, HostAddressInvalidLengthProblem.with(0));
    }

    @Test
    public void testLabelTooLongFirstFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        final String name = new String(array);
        this.parseFails(name + ".second", HostAddressInvalidLengthProblem.with(0));
    }

    public void testLabelTooLongSecondFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        this.parseFails("first." + new String(array) + ".last", HostAddressInvalidLengthProblem.with("first.".length()));
    }

    @Test
    public void testLabelTooLongLastFails() {
        final char[] array = new char[HostAddress.MAX_LABEL_LENGTH];
        Arrays.fill(array, 'x');
        this.parseFails("first." + new String(array), HostAddressInvalidLengthProblem.with("first.".length()));
    }

    @Test
    public void testOneLetter() {
        this.parseAndCheck("A");
    }

    @Test
    public void testOneLetterWithStartAndEnd() {
        this.parseAndCheck("!B!", 1, 2);
    }

    @Test
    public void testOneLabel() {
        this.parseAndCheck("one");
    }

    @Test
    public void testOneLabelWithStartAndEnd() {
        this.parseAndCheck("!one!", 1, 4);
    }

    @Test
    public void testManyLabels() {
        this.parseAndCheck("first.second");
    }

    @Test
    public void testManyLabelsWithStartAndEnd() {
        final String address = "!first.second!";
        this.parseAndCheck(address, 1, address.length() - 1);
    }

    @Test
    public void testManyLabels2() {
        this.parseAndCheck("first.second.third");
    }

    @Test
    public void testManyLabels2WithStartAndEnd() {
        final String address = "!first.second.third!";
        this.parseAndCheck(address, 1, address.length() - 1);
    }

    private void parseAndCheck(final String address) {
        this.parseAndCheck(address, 0, address.length());
    }

    private void parseAndCheck(final String address, final int start, final int end) {
        Assert.assertNull(HostAddress.tryParseName(address, start, end));
    }

    private void parseFails(final String address, final HostAddressProblem problem) {
        this.parseFails(address, 0, address.length(), problem);
    }

    private void parseFails(final String address, final int start, final int end, final HostAddressProblem problem) {
        TestCase.assertEquals("problem", problem, HostAddress.tryParseName(address, start, end));
    }
}
