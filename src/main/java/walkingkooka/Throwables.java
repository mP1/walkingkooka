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

package walkingkooka;

import walkingkooka.text.CharSequences;
import walkingkooka.type.PublicStaticHelper;

final public class Throwables implements PublicStaticHelper {

    /**
     * Combines the message and cause message into a single string which may then be passed to an
     * {@link Throwable} and thrown. If the end result is null or empty a {@link #NONE default} is
     * returned.
     */
    public static String message(final String message, final Throwable cause) {
        String result = null;

        for (; ; ) {
            if (CharSequences.isNullOrEmpty(message)) {
                if (null == cause) {
                    break;
                }
                // default to cause class name if no cause message is present
                final String causeMessage = cause.getMessage();
                if (CharSequences.isNullOrEmpty(causeMessage)) {
                    result = cause.getClass().getName();
                    break;
                }
                result = causeMessage;
                break;
            }
            // cause might be null if it is return message
            if (null == cause) {
                result = message;
                break;
            }
            // combine both message and causeMessage
            final String causeMessage = cause.getMessage();
            if (CharSequences.isNullOrEmpty(causeMessage)) {
                result = message;
                break;
            }
            result = message + "(" + causeMessage + ')';
            break;
        }

        return CharSequences.isNullOrEmpty(result) ? Throwables.NONE : result;
    }

    final static String NONE = "<none>";

    /**
     * Stop creation
     */
    private Throwables() {
        throw new UnsupportedOperationException();
    }
}
