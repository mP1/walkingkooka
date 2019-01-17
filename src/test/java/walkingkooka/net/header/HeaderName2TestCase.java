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
 *
 */

package walkingkooka.net.header;

import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;

public abstract class HeaderName2TestCase<N extends HeaderName2<?>, C extends Comparable<C> & HashCodeEqualsDefined>
        extends HeaderNameTestCase<N, C> {

    HeaderName2TestCase() {
        super();
    }

    @Override
    protected final CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }
}
