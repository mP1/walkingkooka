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

package walkingkooka.tree.text;

import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

import java.util.Objects;

public abstract class LengthVisitor extends Visitor<Length<?>> {

    protected LengthVisitor() {
        super();
    }

    public final void accept(final Length<?> length) {
        Objects.requireNonNull(length, "length");
        this.accept0(length);
    }

    final void accept0(final Length<?> length) {
        if (Visiting.CONTINUE == this.startVisit(length)) {
            length.accept(this);
        }
        this.endVisit(length);
    }

    protected Visiting startVisit(final Length<?> length) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final Length<?> length) {
        // nop
    }

    protected void visit(final NoneLength length) {
        // nop
    }

    protected void visit(final NormalLength length) {
        // nop
    }

    protected void visit(final NumberLength length) {
        // nop
    }

    protected void visit(final PixelLength length) {
        // nop
    }
}
