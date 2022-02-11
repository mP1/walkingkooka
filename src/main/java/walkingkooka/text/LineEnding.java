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

package walkingkooka.text;

import java.util.Objects;

/**
 * A possible line ending. Note it is not possible to create instances only the available constants
 * or singletons may be used. Identity checking is always safe.
 */
final public class LineEnding implements CharSequence {

    private final static String CR_STRING = "\r";

    /**
     * Carriage return
     */
    public final static LineEnding CR = new LineEnding(CR_STRING);

    private final static String CRNL_STRING = "\r\n";

    /**
     * Carriage return new line
     */
    public final static LineEnding CRNL = new LineEnding(CRNL_STRING);

    private final static String NL_STRING = "\n";

    /**
     * New line
     */
    public final static LineEnding NL = new LineEnding(NL_STRING);

    private final static String NONE_STRING = "";

    /**
     * None
     */
    public final static LineEnding NONE = new LineEnding(NONE_STRING);

    /**
     * The actual system line ending.
     */
    //public final static LineEnding SYSTEM = from(SystemProperty.LINE_SEPARATOR.requiredPropertyValue());
    // j2cl SystemProperty is not supported due to AccessController usage. https://github.com/mP1/walkingkooka/issues/2438
    // default required because property will be absent https://github.com/mP1/walkingkooka/issues/2445
    public final static LineEnding SYSTEM = from(System.getProperty("line.separator", "\n"));

    /**
     * Returns the {@link LineEnding} for the given {@link String line ending}.
     */
    public static LineEnding from(final String lineEnding) {
        Objects.requireNonNull(lineEnding, "lineEnding");

        final LineEnding result;

        switch (lineEnding) {
            case CR_STRING:
                result = CR;
                break;
            case CRNL_STRING:
                result = CRNL;
                break;
            case NL_STRING:
                result = NL;
                break;
            case NONE_STRING:
                result = NONE;
                break;
            default:
                throw new IllegalArgumentException("Unknown line endings=" + CharSequences.quoteAndEscape(lineEnding));
        }

        return result;
    }

    /**
     * Private constructor
     */
    private LineEnding(final String value) {
        this.value = value;
    }

    @Override
    public int length() {
        return this.value.length();
    }

    @Override
    public char charAt(final int index) {
        return this.value.charAt(index);
    }

    @Override
    public CharSequence subSequence(final int start, final int end) {
        final String value = this.value;
        return (start == 0) && (end == value.length()) ? this : this.value.subSequence(start, end);
    }

    private final String value;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof LineEnding)
                && this.equals0((LineEnding) other));
    }

    private boolean equals0(final LineEnding other) {
        return this.value.equals(other.value);
    }

    /**
     * Dumps the actual {@link String} holding the line ending characters.
     */
    @Override
    public String toString() {
        return this.value;
    }
}
