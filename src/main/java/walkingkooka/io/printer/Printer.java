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

package walkingkooka.io.printer;

import walkingkooka.text.LineEnding;

import java.io.Closeable;

/**
 * A {@link Printer} may be used to print {@link CharSequence characters} to a target.
 */
public interface Printer extends PrinterLike, Closeable {

    /**
     * Prints of or more characters.
     */
    void print(CharSequence chars) throws PrinterException;

    /**
     * Returns the recommended {@link LineEnding}.
     */
    LineEnding lineEnding() throws PrinterException;

    /**
     * Flushes this {@link Printer}
     */
    void flush() throws PrinterException;

    /**
     * Closes this {@link Printer}. Any future prints will fail.
     */
    @Override
    void close() throws PrinterException;
}
