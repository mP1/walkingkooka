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

package walkingkooka.math;

import walkingkooka.visit.Visiting;
import walkingkooka.visit.Visitor;

import java.util.Objects;

/**
 * A {@link Visitor} that dispatches to a visit overload for each for all JDK {@link Number} types.
 */
public abstract class NumberTypeVisitor extends Visitor<Class<?>> {

    protected NumberTypeVisitor() {
        super();
    }

    @Override
    public final void accept(final Class<?> number) {
        Objects.requireNonNull(number, "number");

        if (Visiting.CONTINUE == this.startVisit(number)) {
            switch (number.getName()) {
                case "java.math.BigDecimal":
                    this.visitBigDecimal();
                    break;
                case "java.math.BigInteger":
                    this.visitBigInteger();
                    break;
                case "java.lang.Byte":
                    this.visitByte();
                    break;
                case "java.lang.Double":
                    this.visitDouble();
                    break;
                case "java.lang.Float":
                    this.visitFloat();
                    break;
                case "java.lang.Integer":
                    this.visitInteger();
                    break;
                case "java.lang.Long":
                    this.visitLong();
                    break;
                case "java.lang.Short":
                    this.visitShort();
                    break;
                default:
                    this.visitUnknown(number);
                    break;
            }
        }
        this.endVisit(number);
    }

    protected Visiting startVisit(final Class<?> classs) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final Class<?> classs) {
    }

    protected void visitBigDecimal() {
    }

    protected void visitBigInteger() {
    }

    protected void visitByte() {
    }

    protected void visitDouble() {
    }

    protected void visitFloat() {
    }

    protected void visitInteger() {
    }

    protected void visitLong() {
    }

    protected void visitShort() {
    }

    /**
     * Invoked for non native {@link Number} types. The default behaviour is to return the same {@link Number} but other
     * options include converting the given {@link Number} to one of the supported {@link Number} types.
     */
    protected void visitUnknown(final Class<?> classs) {
    }
}
