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

package walkingkooka.stream.push;

import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.StandardThrowableTesting;

final public class PushStreamExceptionTest implements StandardThrowableTesting<PushStreamException> {

    @Override
    public PushStreamException createThrowable(final String message) {
        return new PushStreamException(message);
    }

    @Override
    public PushStreamException createThrowable(final String message, final Throwable cause) {
        return new PushStreamException(message, cause);
    }

    @Override
    public Class<PushStreamException> type() {
        return PushStreamException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
