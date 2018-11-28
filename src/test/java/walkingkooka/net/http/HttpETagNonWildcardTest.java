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

public final class HttpETagNonWildcardTest extends HttpETagTestCase<HttpETagNonWildcard> {

    private final static String VALUE = "0123456789ABCDEF";

    @Test
    public void testWith() {
        this.check(this.createETag());
    }

    @Test
    public void testWithEmpty() {
        this.check(HttpETag.with("", HttpETagValidator.STRONG), "", HttpETagValidator.STRONG);
    }

    @Test
    public void testSetValidatorDifferent() {
        final HttpETag etag = this.createETag();
        final HttpETagValidator validator = HttpETagValidator.WEAK;
        this.check(etag.setValidator(validator), this.value(), validator);
        this.check(etag);
    }

    @Override
    HttpETagNonWildcard createETag() {
        return HttpETagNonWildcard.with0(VALUE, HttpETagValidator.STRONG);
    }

    @Override
    String value() {
        return VALUE;
    }

    @Override
    HttpETagValidator validator() {
        return HttpETagValidator.STRONG;
    }

    @Override
    boolean isWildcard() {
        return false;
    }

    @Override
    protected Class<HttpETagNonWildcard> type() {
        return HttpETagNonWildcard.class;
    }
}
