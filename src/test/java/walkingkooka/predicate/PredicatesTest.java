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

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicStaticHelperTesting;
import walkingkooka.text.CaseSensitivity;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class PredicatesTest implements PublicStaticHelperTesting<Predicates>,
        PredicateTesting {

    // globsPatterns....................................................................................................

    @Test
    public void testGlobPatternsWithNullExpressionFails() {
        assertThrows(
                NullPointerException.class,
                () -> Predicates.globPatterns(
                        null,
                        CaseSensitivity.SENSITIVE,
                        '\\'
                )
        );
    }

    @Test
    public void testGlobPatternsWithNullCaseSensitiveFails() {
        assertThrows(
                NullPointerException.class,
                () -> Predicates.globPatterns(
                        "*",
                        null,
                        '\\'
                )
        );
    }

    @Test
    public void testGlobPatternsTestMatchCaseSensitive() {
        this.testTrue(
                Predicates.globPatterns(
                        "*Match*",
                        CaseSensitivity.SENSITIVE,
                        '\\'
                ),
                "Match"
        );
    }

    @Test
    public void testGlobPatternsTestMatchCaseInsensitive() {
        this.testTrue(
                Predicates.globPatterns(
                        "*Match*",
                        CaseSensitivity.INSENSITIVE,
                        '\\'
                ),
                "MATCH"
        );
    }

    @Test
    public void testGlobPatternsTestMatchMultipleTokens() {
        this.testTrue(
                Predicates.globPatterns(
                        "*starts ends* *Match*",
                        CaseSensitivity.INSENSITIVE,
                        '\\'
                ),
                "111MATCH222"
        );
    }

    @Test
    public void testGlobPatternsTestNotMatch() {
        this.testFalse(
                Predicates.globPatterns(
                        "*starts ends* *Match*",
                        CaseSensitivity.INSENSITIVE,
                        '\\'
                ),
                "Not!"
        );
    }

    @Test
    public void testGlobPatternsToString() {
        final String pattern = "starts* *contains* ends* match 12345";

        this.checkEquals(
                pattern,
                Predicates.globPatterns(
                        pattern,
                        CaseSensitivity.INSENSITIVE,
                        '\\'
                ).toString()
        );
    }
    
    // class............................................................................................................

    @Override
    public Class<Predicates> type() {
        return Predicates.class;
    }

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
