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

package walkingkooka;

import org.junit.jupiter.api.Test;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.IsMethodTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class EitherTestCase2<E extends Either<L, R>, L, R> extends EitherTestCase<E, L, R>
    implements HashCodeEqualsDefinedTesting2<E>,
    IsMethodTesting<E>,
    ToStringTesting<E> {

    EitherTestCase2() {
        super();
    }

    // setValue.........................................................................................................

    @Test
    public final void testSetLeftValueNull() {
        final Either<L, R> either = this.createEither()
            .setLeftValue(null);
        this.checkEquals(null, either.leftValue(), "left");
    }

    @Test
    public final void testSetRightValueNull() {
        final Either<L, R> either = this.createEither()
            .setRightValue(null);
        this.checkEquals(null, either.rightValue(), "right");
    }

    // orElseGet.........................................................................................................

    @Test
    public final void testOrElseLeftGetNullSupplierFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().orElseLeftGet(null));
    }

    @Test
    public final void testOrElseRightGetNullSupplierFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().orElseRightGet(null));
    }

    // orElseThrows.....................................................................................................

    @Test
    public final void testOrElseLeftThrowsNullSupplierFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().orElseLeftThrow(null));
    }

    @Test
    public final void testOrElseRightThrowsNullSupplierFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().orElseRightThrow(null));
    }

    // map..............................................................................................................

    @Test
    public final void testMapLeftNullMapperFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().mapLeft(null));
    }

    @Test
    public final void testMapRightNullMapperFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().mapRight(null));
    }

    // ifPresent.........................................................................................................

    @Test
    public final void testIfPresentLeftNullConsumerFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().ifLeftPresent(null));
    }

    @Test
    public final void testIfPresentRightNullConsumerFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().ifRightPresent(null));
    }

    // accept...........................................................................................................

    @Test
    public final void testAcceptNullLeftConsumerFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().accept(null, this::acceptRight));
    }

    @Test
    public final void testAcceptNullRightConsumerFails() {
        assertThrows(NullPointerException.class, () -> this.createEither().accept(this::acceptLeft, null));
    }

    final void acceptLeft(final L left) {
        throw new UnsupportedOperationException();
    }

    final void acceptRight(final R right) {
        throw new UnsupportedOperationException();
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // HashCodeAndEqualsDefined..........................................................................................

    @Override
    public E createObject() {
        return this.createEither();
    }

    // IsMethodTesting...................................................................................................

    @Override
    public E createIsMethodObject() {
        return this.createEither();
    }

    @Override
    public String isMethodTypeNamePrefix() {
        return Either.class.getSimpleName();
    }

    @Override
    public String isMethodTypeNameSuffix() {
        return "";
    }

    @Override
    public Predicate<String> isMethodIgnoreMethodFilter() {
        return Predicates.never();
    }
}
