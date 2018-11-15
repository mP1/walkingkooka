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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class HttpRequestParameterNameEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<HttpRequestParameterName> {

    @Test
    public void testDifferent() {
        this.checkNotEquals(HttpRequestParameterName.with("different"));
    }

    @Override
    protected HttpRequestParameterName createObject() {
        return HttpRequestParameterName.with("parameter1");
    }
}
