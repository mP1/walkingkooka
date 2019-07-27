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

package walkingkooka.type;

import walkingkooka.visit.Visiting;

public class FakeJavaVisibilityVisitor extends JavaVisibilityVisitor {

    public FakeJavaVisibilityVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final JavaVisibility visibility) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final JavaVisibility visibility) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPublic() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitProtected() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPackagePrivate() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPrivate() {
        throw new UnsupportedOperationException();
    }
}
