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

package walkingkooka.net;

import org.junit.Test;
import walkingkooka.collect.list.ListTestCase;

public final class UrlParameterValueListTest extends ListTestCase<UrlParameterValueList, String> {

    @Test
    public void testEmpty() {
        this.isEmptyAndCheck(UrlParameterValueList.empty(), true);
    }

    public void testNotEmpty() {
        final UrlParameterValueList list = UrlParameterValueList.empty();

        final String a1 = "a1";
        final String b2 = "b2";
        final String c3 = "c3";

        list.addParameterValue(a1);
        list.addParameterValue(b2);
        list.addParameterValue(c3);

        this.getAndCheck(list, 0, a1);
        this.getAndCheck(list, 1, b2);
        this.getAndCheck(list, 2, c3);
    }

    @Test
    public void testGetInvalidIndexFails() {
        this.getFails(UrlParameterValueList.empty(), 0);
        this.getFails(UrlParameterValueList.empty(), 1);
    }

    @Test
    public void testGetInvalidIndexFails2() {
        final UrlParameterValueList list = UrlParameterValueList.empty();

        list.addParameterValue("a1");
        list.addParameterValue("b2");
        list.addParameterValue("c3");

        this.getFails(list, -1);
        this.getFails(list, 3);
    }


    @Override
    protected UrlParameterValueList createList() {
        return UrlParameterValueList.empty();
    }

    @Override
    protected Class<UrlParameterValueList> type() {
        return UrlParameterValueList.class;
    }
}
