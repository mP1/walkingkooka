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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Generates a Test for the given email-tests.xml which is taken from http://code.google.com/p/isemail
 */
final public class DominicsayersComIsemailEmailAddressTestGenerator {

    static public void main(final String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: <path to isEmail xml file>");
        } else {
            final File file = new File(args[0]);
            try (final FileInputStream input = new FileInputStream(file)) {
                final String info = "of tests generator: "
                        + DominicsayersComIsemailEmailAddressTestGenerator.class.getSimpleName() + " file: "
                        + file.getName() + " ---";

                print("//--- start " + info);

                generate(input /* test, */);

                print("//--- end " + info);
            }
        }
    }

    /**
     * Creates the generated *.java and prints the tests one by one. Tests that include email comments are skipped.
     */
    private static void generate(
            final InputStream input/* , final String testClassName , final IndentingPrinter printer */)
            throws Exception {

        // read xml and sort tests w/ parameters
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
        doc.getDocumentElement().normalize();

        final NodeList testNodes = doc.getElementsByTagName("test");
        // final Map<String, Pair<String, Boolean>> tests = Maps.sorted();
        final Map<String, TestDetails> tests = Maps.sorted();
        for (int s = 0; s < testNodes.getLength(); s++) {
            final Node testNode = testNodes.item(s);
            if (testNode.getNodeType() == Node.ELEMENT_NODE) {
                final Element test = (Element) testNode;
                final String address = elementWithName(test, "address");
                final String valid = elementWithName(test, "valid");
                final String id = elementWithName(test, "id");
                final String comment = optionalElementWithName(test,
                        "comment");

                // skip tests where the email includes comments...
                if ((address.indexOf('(') != -1) || (address.indexOf(')') != -1)) {
                    continue;
                }
                tests.put(createId(id),
                        TestDetails.with(address, "true".equals(valid), comment));
            }
        }

        // print tests one by one
        for (final Entry<String, TestDetails> idAndTest : tests.entrySet()) {
            final TestDetails test = idAndTest.getValue();
            final String valid = test.valid ? "parseSuccessful" : "parseFails2";
            final String comment = test.comment;
            final String email = test.email;

            print("@Test");
            print("public void test" + idAndTest.getKey()
                    + makeIntoTestMethodName(email)
                    + makeIntoTestMethodName(comment)
                    + "(){");
            DominicsayersComIsemailEmailAddressTestGenerator
                    .print(valid + "(" + CharSequences.quote(encode(email))
                            + (null != comment ? "," + CharSequences.quoteAndEscape(comment) : "")
                            + ");");
            print("}");
            print();
        }
    }

    private static String makeIntoTestMethodName(final String raw) {
        String string = "";
        if (null != raw) {
            final StringBuilder builder = new StringBuilder();
            builder.append("__");

            for (final char c : raw.toCharArray()) {
                if ('(' == c) {
                    builder.append("_LEFT_PAREN_");
                    continue;
                }
                if (')' == c) {
                    builder.append("_RIGHT_PAREN_");
                    continue;
                }
                if ('@' == c) {
                    builder.append("_ATSIGN_");
                    continue;
                }
                if ('.' == c) {
                    builder.append("_DOT_");
                    continue;
                }
                if (' ' == c) {
                    builder.append('_');
                    continue;
                }
                if (Character.isJavaIdentifierPart(c)) {
                    builder.append(c);
                    continue;
                }
            }

            string = builder.toString();
        }
        return string;
    }

    /**
     * Helper gets the only tag with the name and verifies one tag exists from the parent tag.
     */
    private static String elementWithName(final Element parent, final String tag) throws Exception {
        final NodeList list = parent.getElementsByTagName(tag);
        if (list.getLength() != 1) {
            throw new Exception("Expected child tag " + tag + " in element " + parent.getNodeName());
        }
        return textContent(list);
    }

    /**
     * Attempts to locate an element with the name if present otherwise returns null.
     */
    private static String optionalElementWithName(final Element parent, final String tag) {
        final NodeList list = parent.getElementsByTagName(tag);
        return list.getLength() == 1 ? textContent(list) : null;
    }

    /**
     * Returns the text for the first element in the {@link NodeList}.
     */
    private static String textContent(final NodeList list) {
        return ((Element) list.item(0)).getTextContent().trim();
    }

    /**
     * Makes the {@link String id} into a zero padded string.
     */
    private static String createId(final String id) {
        return CharSequences.padLeft(id, 3, '0').toString();
    }

    /**
     * Escapes any control characters with the equivalent unicode sequence.
     */
    private static String encode(final String raw) {
        final StringBuilder encoded = new StringBuilder();
        final int length = raw.length();

        for (int i = 0; i < length; i++) {
            final char c = raw.charAt(i);
            if ((c == 2400) || (c == 9216)) {
                encoded.append("\\0");
                continue;
            }
            if ((c >= 31) | ('\n' == c) | ('\r' == c)) {
                encoded.append(CharSequences.escape(Character.toString(c)));
                continue;
            }
            encoded.append("\\u").append(CharSequences.padLeft(Integer.toHexString(c).toUpperCase(), 4, '0'));
        }

        return encoded.toString();
    }

    static private void print() {
        print("");
    }

    static private void print(final String line) {
        System.out.println(line);
    }

    private static class TestDetails {

        static TestDetails with(final String email, final boolean valid, final String comment) {
            return new TestDetails(email, valid, comment);
        }

        private TestDetails(final String email, final boolean valid, final String comment) {
            super();
            this.email = email;
            this.valid = valid;
            this.comment = comment;
        }

        String email;
        boolean valid;
        String comment;
    }
}
