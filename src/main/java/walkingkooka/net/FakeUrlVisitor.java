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

import walkingkooka.test.Fake;
import walkingkooka.tree.visit.Visiting;

public class FakeUrlVisitor extends UrlVisitor implements Fake {

    protected FakeUrlVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final Url url) {
        return super.startVisit(url);
    }

    @Override
    protected void endVisit(final Url Url) {
        super.endVisit(Url);
    }

    @Override
    protected void visit(final AbsoluteUrl url) {
        super.visit(url);
    }

    @Override
    protected void visit(final RelativeUrl url) {
        super.visit(url);
    }
}
