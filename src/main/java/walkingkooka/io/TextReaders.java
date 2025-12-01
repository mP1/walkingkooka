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

package walkingkooka.io;

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.reflect.PublicStaticHelper;

import java.io.Reader;
import java.util.function.Consumer;

public final class TextReaders implements PublicStaticHelper {

    /**
     * {@see FakeTextReader}
     */
    public static FakeTextReader fake() {
        return new FakeTextReader();
    }

    /**
     * {@see JavaIoReaderTextReader}
     */
    @GwtIncompatible
    public static TextReader reader(final Reader reader,
                                    final Consumer<Character> echo) {
        return JavaIoReaderTextReader.with(
            reader,
            echo
        );
    }

    /**
     * Stop creation
     */
    private TextReaders() {
        throw new UnsupportedOperationException();
    }
}
