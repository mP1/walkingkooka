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

import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} for a graph of {@link Url}.
 */
public abstract class UrlVisitor extends Visitor<Url> {

    protected UrlVisitor() {
        super();
    }

    // Url.............................................................................................................

    public final void accept(final Url url) {
        Objects.requireNonNull(url, "url");

        if (Visiting.CONTINUE == this.startVisit(url)) {
            url.accept(this);
        }
        this.endVisit(url);
    }

    protected Visiting startVisit(final Url url) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final Url Url) {
        // nop
    }

    protected void visit(final AbsoluteUrl url) {
        // nop
    }

    protected void visit(final RelativeUrl url) {
        // nop
    }
}
