/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.net.http;

import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

public class HttpMethodVisitor extends Visitor<HttpMethod> {
    @Override
    public final void accept(final HttpMethod method) {
        this.accept0(method);
    }

    final void accept0(final HttpMethod method) {
        if (Visiting.CONTINUE == this.startVisit(method)) {
            method.accept(this);
        }
        this.endVisit(method);
    }

    protected Visiting startVisit(final HttpMethod method) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final HttpMethod method) {
        // nop
    }

    protected void visitHead() {
    }

    protected void visitGet() {
    }

    protected void visitPost() {
    }

    protected void visitPut() {
    }

    protected void visitDelete() {
    }

    protected void visitTrace() {
    }

    protected void visitOptions() {
    }

    protected void visitConnect() {
    }

    protected void visitPatch() {
    }

    protected void visitUnknown(final HttpMethod method) {
    }
}
