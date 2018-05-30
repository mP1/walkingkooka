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
 */

package walkingkooka.text;

import walkingkooka.type.PackagePrivateStaticHelper;

/**
 * Helper that escapes/unescapes unprintable characters within {@link CharSequence chars}.
 */
final class CharSequenceEscaping implements PackagePrivateStaticHelper {

    /**
     * Escapes all newline, carriage returns and other common characters that are escaped in java by
     * a backslash prefix. Null inputs return null.
     */
    static CharSequence escape(final CharSequence chars) {
        String escaped = null;

        if (null != chars) {
            final int length = chars.length();
            final StringBuilder builder = new StringBuilder((length * 10) / 9);
            for (int i = 0; i < length; i++) {
                final char c = chars.charAt(i);

                switch (c) {
                    case '\\':
                        builder.append("\\\\");
                        break;
                    case '\t':
                        builder.append("\\t");
                        break;
                    case '\r':
                        builder.append("\\r");
                        break;
                    case '\n':
                        builder.append("\\n");
                        break;
                    case '\0':
                        builder.append("\\0");
                        break;
                    case '\'':
                        builder.append("\\\'");
                        break;
                    case '\"':
                        builder.append("\\\"");
                        break;
                    case 1:
                        builder.append("\\u0001");
                        break;
                    case 2:
                        builder.append("\\u0002");
                        break;
                    case 3:
                        builder.append("\\u0003");
                        break;
                    case 4:
                        builder.append("\\u0004");
                        break;
                    case 5:
                        builder.append("\\u0005");
                        break;
                    case 6:
                        builder.append("\\u0006");
                        break;
                    case 7:
                        builder.append("\\u0007");
                        break;
                    case 8:
                        builder.append("\\u0008");
                        break;
                    case 11:
                        builder.append("\\u000B");
                        break;
                    case 12:
                        builder.append("\\u000C");
                        break;
                    case 14:
                        builder.append("\\u000E");
                        break;
                    case 15:
                        builder.append("\\u000F");
                        break;
                    case 16:
                        builder.append("\\u0010");
                        break;
                    case 17:
                        builder.append("\\u0011");
                        break;
                    case 18:
                        builder.append("\\u0012");
                        break;
                    case 19:
                        builder.append("\\u0013");
                        break;
                    case 20:
                        builder.append("\\u0014");
                        break;
                    case 21:
                        builder.append("\\u0015");
                        break;
                    case 22:
                        builder.append("\\u0016");
                        break;
                    case 23:
                        builder.append("\\u0017");
                        break;
                    case 24:
                        builder.append("\\u0018");
                        break;
                    case 25:
                        builder.append("\\u0019");
                        break;
                    case 26:
                        builder.append("\\u001A");
                        break;
                    case 27:
                        builder.append("\\u001B");
                        break;
                    case 28:
                        builder.append("\\u001C");
                        break;
                    case 29:
                        builder.append("\\u001D");
                        break;
                    case 30:
                        builder.append("\\u001E");
                        break;
                    case 31:
                        builder.append("\\u001F");
                        break;
                    default:
                        builder.append(c);
                        break;
                }
            }
            escaped = builder.toString();
        }

        return escaped;
    }

    /**
     * Un-escapes {@link CharSequence chars} escaped by {@link #escape(CharSequence)}. Note support
     * is included for backslash escape sequences and unicode escape sequences.
     */
    static CharSequence unescape(final CharSequence chars) {
        String escaped = null;

        if (null != chars) {
            final int length = chars.length();
            final StringBuilder builder = new StringBuilder(length);

            boolean wasBackslash = false;

            int i = 0;
            while (i < length) {
                final char c = chars.charAt(i);
                i++;

                if (wasBackslash) {
                    switch (c) {
                        case '\\':
                            builder.append('\\');
                            break;
                        case 't':
                            builder.append('\t');
                            break;
                        case 'r':
                            builder.append('\r');
                            break;
                        case 'n':
                            builder.append('\n');
                            break;
                        case '0':
                            builder.append('\0');
                            break;
                        case '\'':
                            builder.append('\'');
                            break;
                        case '"':
                            builder.append('\"');
                            break;
                        case 'u':
                            // incomplete unicode character
                            if ((i + 4) > length) {
                                builder.append("\\u");
                                break;
                            }
                            // \\u0000;
                            final char firstChar = chars.charAt(i);
                            final int firstValue = Character.digit(firstChar, 16);
                            if (firstValue < 0) {
                                builder.append("\\u");
                                break;
                            }
                            final char secondChar = chars.charAt(i + 1);
                            final int secondValue = Character.digit(secondChar, 16);
                            if (secondValue < 0) {
                                builder.append("\\u");
                                break;
                            }
                            final char thirdChar = chars.charAt(i + 2);
                            final int thirdValue = Character.digit(thirdChar, 16);
                            if (thirdValue < 0) {
                                builder.append("\\u");
                                break;
                            }
                            final char fourthChar = chars.charAt(i + 3);
                            final int fourthValue = Character.digit(fourthChar, 16);
                            if (fourthValue < 0) {
                                builder.append("\\u");
                                break;
                            }
                            i = i + 4;
                            builder.append((char) ((firstValue * 0x1000) + (secondValue * 0x0100)
                                    + (thirdValue * 0x10) + fourthValue));
                            break;
                        default:
                            builder.append(c);
                            break;
                    }
                    wasBackslash = false;
                    continue;
                }
                wasBackslash = '\\' == c;
                if (false == wasBackslash) {
                    builder.append(c);
                }
            }
            escaped = builder.toString();
        }

        return escaped;
    }

    /**
     * Stop creation
     */
    private CharSequenceEscaping() {
        throw new UnsupportedOperationException();
    }
}
