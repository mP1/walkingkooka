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

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;

/**
 * Template {@link CharSequence} that includes templated handling the boring bits of {@link
 * #charAt(int)} and {@link #subSequence(int, int)} .
 */
abstract class CharSequenceTemplate<C extends CharSequenceTemplate<C>>
        implements CharSequence, HashCodeEqualsDefined, Serializable {

    private static final long serialVersionUID = 4222295145637915231L;

    /**
     * Package private to limit sub classing.
     */
    CharSequenceTemplate() {
        super();
    }

    /**
     * {@see CharSequence#charAt(int)}.
     */
    @Override final public char charAt(final int index) {
        final int length = this.length();
        if (index < 0 || index >= length) {
            throw new StringIndexOutOfBoundsException(
                    "Index " + index + " must be between 0 and " + length);
        }
        return this.charAtIndex(index);
    }

    /**
     * Sub classes must override this method which is called after index has been verified.
     */
    abstract char charAtIndex(int index);

    @Override final public CharSequence subSequence(final int start, final int end) {
        final int length = this.length();
        if (start < 0 || start > end || end > length) {
            throw new StringIndexOutOfBoundsException(
                    "start " + start + ", end " + end + ", length " + length);
        }

        CharSequence result = this;
        for (; ; ) {
            if ((start == 0) && (end == length)) {
                break;
            }
            if (start == end) {
                result = "";
                break;
            }
            result = this.doSubSequence(start, end);
            break;
        }

        return result;
    }

    /**
     * Called after start and end have been verified to be different and valid.
     */
    abstract CharSequence doSubSequence(int start, int end);

    // HashCodeEqualsDefined

    /**
     * Lazily calculates the hash code and stores it for future retrieval.
     */
    @Override final public int hashCode() {
        if (0 == this.hashCode) {
            this.hashCode = this.calculateHashCode();
        }
        return this.hashCode;
    }

    /**
     * This method is invoked only once when the hashcode is first requested. Future requests are
     * taken from the cached value.
     */
    abstract int calculateHashCode();

    /**
     * The cached hash code which is initially set to 0.
     */
    transient int hashCode = 0;

    /**
     * Performs some simple checks for nullness, identity and type using {@link #canBeEqual(Object)}
     * before invoking {@link #doEquals(C)} if types are compatible but different
     * instances.
     */
    @Override final public boolean equals(final Object object) {
        return this == object || this.canBeEqual(object) && this.doEquals(Cast.to(object));
    }

    /**
     * Tests if the argument to {@link #equals(Object)} is compatible for purposes of equality.Note
     * the parameter may be null thus the best testing includes an instance of X test.
     */
    abstract boolean canBeEqual(final Object other);

    /**
     * This method is invoked only if the both objects are the same type and not null.
     */
    abstract boolean doEquals(C object);

    /**
     * Checks a cached field and if null calls {@link #buildToString()}
     */
    @Override final public String toString() {
        if (null == this.toString) {
            this.toString = this.buildToString();
        }
        return this.toString;
    }

    /**
     * May be overridden but probably should not be necessary.
     */
    String buildToString() {
        // intentional to avoid StackOverflowError
        return new StringBuilder(this).toString();
    }

    /**
     * A transient cache of {@link #toString()} result built by {@link #buildToString()}.
     */
    private transient String toString;
}
