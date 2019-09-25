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

package walkingkooka.text.printer;

import walkingkooka.text.LineEnding;

/**
 * A {@link Printer} that ignores all {@link CharSequence sequences} added to it.
 */
final class SinkPrinter implements Printer {

    /**
     * Singleton
     */
    final static SinkPrinter INSTANCE = new SinkPrinter();

    /**
     * Singleton
     */
    private SinkPrinter() {
        super();
    }

    @Override
    public void print(final CharSequence chars) throws PrinterException {
        // nop
    }

    /**
     * Always returns {@link LineEnding}.
     */
    @Override
    public LineEnding lineEnding() throws PrinterException {
        return LineEnding.NONE;
    }

    @Override
    public void flush() throws PrinterException {
        // nop
    }

    @Override
    public void close() throws PrinterException {
        // nop
    }

    @Override
    public String toString() {
        return "sink";
    }
}
