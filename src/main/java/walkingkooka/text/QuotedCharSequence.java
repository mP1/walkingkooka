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

/**
 * A {@link CharSequence} surrounds another {@link CharSequence} with double quotes.
 */
abstract class QuotedCharSequence<S extends QuotedCharSequence<S>> extends CharSequenceTemplate<S> {

    private static final long serialVersionUID = 3371483017716721066L;

    /**
     * Package private constructor.
     */
    QuotedCharSequence(final CharSequence chars) {
        super();

        this.chars = chars;
    }

    @Override final public int length() {
        return this.chars.length() + this.quoteCount();
    }

    abstract int quoteCount();

    // properties

    final CharSequence charSequence() {
        return this.chars;
    }

    final CharSequence chars;

    @Override
    int calculateHashCode() {
        return this.chars.hashCode();
    }

    @Override
    boolean doEquals(final S quoted) {
        return this.chars.equals(quoted.chars);
    }
}
