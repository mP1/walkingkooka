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

import org.junit.jupiter.api.Test;

public final class HostAddressProbablyIp6ProblemTest extends HostAddressProblemTestCase<HostAddressProbablyIp6Problem> {

    @Test
    public void testMessage() {
        this.messageAndCheck(HostAddressProbablyIp6Problem.INSTANCE,
                "!invalid",
                "Host probably an ip6 dot notation address=\"!invalid\"");
    }

    @Override
    public Class<HostAddressProbablyIp6Problem> type() {
        return HostAddressProbablyIp6Problem.class;
    }
}
