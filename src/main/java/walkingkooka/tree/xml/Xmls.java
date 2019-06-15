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

package walkingkooka.tree.xml;

import walkingkooka.type.PublicStaticHelper;

import java.util.Objects;

/**
 * A collect of utilities related to ASCII.<br> Constants taken from <a>http://en.wikipedia.org/wiki/Control_character</a>
 */
final public class Xmls implements PublicStaticHelper {

    public static String decode(final String encoded) {
        Objects.requireNonNull(encoded, "encoded");

        String result = encoded;
        final int length = encoded.length();
        if (length > 0) {
            final StringBuilder decoded = new StringBuilder(length);
            int unchanged = 0;

            for (int i = 0; i < length; ) {
                final char c = encoded.charAt(i);
                i++;

                if ('&' == c) {
                    final int semiColon = encoded.indexOf(';', i);
                    final String entity = encoded.substring(i, semiColon);
                    i = semiColon + 1;

                    if (entity.equals("lt")) {
                        decoded.append('<');
                        continue;
                    }

                    if (entity.equals("gt")) {
                        decoded.append('>');
                        continue;
                    }

                    if (entity.equals("amp")) {
                        decoded.append('&');
                        continue;
                    }
                    if (entity.equals("apos")) {
                        decoded.append('\'');
                        continue;
                    }
                    if (entity.equals("quot")) {
                        decoded.append('"');
                        continue;
                    }
                    throw new XmlException("Unknown/unsupported Xml entity &" + entity + ";");
                }
                decoded.append(c);
                unchanged++;
            }

            if (length != unchanged) {
                result = decoded.toString();
            }
        }
        return result;
    }

    public static String encode(final String unencoded) {
        Objects.requireNonNull(unencoded, "unencoded");

        String result = unencoded;
        final int length = unencoded.length();
        if (length > 0) {
            final StringBuilder encoded = new StringBuilder(length);
            int unchanged = 0;

            for (int i = 0; i < length; i++) {
                final char c = unencoded.charAt(i);

                if ('<' == c) {
                    encoded.append("&lt;");
                    continue;
                }
                if ('>' == c) {
                    encoded.append("&gt;");
                    continue;
                }
                if ('&' == c) {
                    encoded.append("&amp;");
                    continue;
                }
                if ('\'' == c) {
                    encoded.append("&apos;");
                    continue;
                }
                if ('"' == c) {
                    encoded.append("&quot;");
                    continue;
                }
                encoded.append(c);
                unchanged++;
            }

            if (length != unchanged) {
                result = encoded.toString();
            }
        }
        return result;
    }

    /**
     * Stop creation
     */
    private Xmls() {
        throw new UnsupportedOperationException();
    }
}
