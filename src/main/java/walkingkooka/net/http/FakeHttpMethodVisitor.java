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

public class FakeHttpMethodVisitor extends HttpMethodVisitor {
    @Override
    protected Visiting startVisit(final HttpMethod method) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final HttpMethod method) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitHead() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitGet() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPost() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPut() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitDelete() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTrace() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOptions() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitConnect() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPatch() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitUnknown(final HttpMethod method) {
        throw new UnsupportedOperationException();
    }
}
