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

package walkingkooka.net.http.server.hateos;

import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.Map;

public abstract class HateosHandlerTestingTestCase<H extends HateosHandler<BigInteger, TestHateosResource, TestHateosResource2>>
        implements HateosHandlerTesting<H, BigInteger, TestHateosResource, TestHateosResource2> {

    HateosHandlerTestingTestCase() {
        super();
    }

    @Override
    public final void testTestNaming() {

    }

    @Override
    public final Map<HttpRequestAttribute<?>, Object> parameters() {
        return HateosHandler.NO_PARAMETERS;
    }

    @Override
    public final BigInteger id() {
        return BigInteger.valueOf(111);
    }

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public final String typeNamePrefix() {
        return "";
    }
}
