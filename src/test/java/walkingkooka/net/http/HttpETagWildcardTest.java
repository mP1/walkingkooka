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

import java.util.Optional;

public final class HttpETagWildcardTest extends HttpETagTestCase<HttpETagWildcard> {

    @Test(expected = IllegalArgumentException.class)
    public void testSetWeakWeakFails() {
        HttpETagWildcard.instance().setWeak(HttpETag.WEAK);
    }

    @Override
    HttpETagWildcard createETag() {
        return HttpETagWildcard.instance();
    }

    @Override
    String value() {
        return HttpETag.WILDCARD_VALUE.string();
    }

    @Override
    Optional<HttpETagWeak> weak() {
        return HttpETag.NO_WEAK;
    }

    @Override
    boolean isWildcard() {
        return true;
    }

    @Override
    protected Class<HttpETagWildcard> type() {
        return HttpETagWildcard.class;
    }
}
