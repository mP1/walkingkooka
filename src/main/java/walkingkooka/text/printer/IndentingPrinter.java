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

/**
 * A {@link Printer} that includes additional functionality to support indentation.
 */
public interface IndentingPrinter extends Printer {

    /**
     * Starts a new line, multiple requests are ignored.
     */
    void lineStart() throws PrinterException;

    /**
     * Increases the indentation
     */
    void indent() throws PrinterException;

    /**
     * Removes a previous indentation.
     */
    void outdent() throws PrinterException;
}
