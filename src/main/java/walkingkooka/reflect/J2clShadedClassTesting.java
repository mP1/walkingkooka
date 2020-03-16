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

package walkingkooka.reflect;

import org.junit.jupiter.api.Assertions;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.text.CharSequences;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public interface J2clShadedClassTesting {

    String J2CL_SHADE_FILENAME = "/.walkingkooka-j2cl-maven-plugin-shade.txt";

    default void fieldDeclarationsCheck(final Class<?> shadedType,
                                        final Class<?> unshadedType) throws IOException {
        try (final InputStream file = shadedType.getResourceAsStream(J2CL_SHADE_FILENAME)) {
            if (null == file) {
                Assertions.fail("Unable to find resource " + CharSequences.quote(J2CL_SHADE_FILENAME));
            }
            final Properties properties = new Properties();
            properties.load(file);

            final Map<String, String> map = Maps.sorted();
            properties.forEach((k, v) -> map.put((String) k, (String) v));

            this.fieldDeclarationsCheck(shadedType,
                    unshadedType,
                    map);
        }
    }

    default void fieldDeclarationsCheck(final Class<?> shadedType,
                                        final Class<?> unshadedType,
                                        final Map<String, String> shadings) {
        final Predicate<Field> publicProtectedFieldPredicate = m -> false == JavaVisibility.of(m).isOrLess(JavaVisibility.PACKAGE_PRIVATE);

        // keep only public and protected shadedType fields..........................................................
        final Set<Field> shadedPublicProtectedFields = Arrays.stream(shadedType.getDeclaredFields())
                .filter(publicProtectedFieldPredicate)
                .collect(Collectors.toSet());

        // this is used to translate field signatures of $emulatedPublicProtectedFields into $jreType field signatures.
        final UnaryOperator<Class> typeShader = t -> {
            String typeName = t.getName();

            return shadings.entrySet()
                    .stream()
                    .filter(e -> typeName.startsWith(e.getKey()))
                    .map(e -> e.getValue() + typeName.substring(e.getKey().length()))
                    .map(n -> {
                        try {
                            return Class.forName(n);
                        } catch (final Exception notFound) {
                            throw new RuntimeException("Unable to find type " + CharSequences.quote(typeName));
                        }
                    })
                    .findFirst()
                    .orElse(t);
        };

        final Set<Field> badShadedFields = Sets.ordered();
        for (final Field shadedField : shadedPublicProtectedFields) {
            try {
                final Field unshadedField = unshadedType.getDeclaredField(shadedField.getName());
                if (false == typeShader.apply(shadedField.getType()).equals(unshadedField.getType()) ||
                        JavaVisibility.of(shadedField) != JavaVisibility.of(unshadedField) ||
                        false == FieldAttributes.get(shadedField).equals(FieldAttributes.get(unshadedField))) {
                    badShadedFields.add(shadedField);
                }

            } catch (final Exception notFound) {
                badShadedFields.add(shadedField);
                continue;
            }

            // verify jre field return type matches emulated
        }

        assertEquals(Sets.empty().stream().map(Object::toString).collect(Collectors.joining("\n")),
                badShadedFields.stream().map(Field::toGenericString).collect(Collectors.joining("\n")),
                () -> "Several " + shadedType.getName() + " fields have different signatures compared to " + unshadedType.getName() + " shadings\n" + shadings.entrySet().stream().map(Entry::toString).collect(Collectors.joining("\n", "", "\n")));
    }

    default void methodSignaturesCheck(final Class<?> shadedType,
                                       final Class<?> unshadedType) throws IOException {
        try (final InputStream file = shadedType.getResourceAsStream(J2CL_SHADE_FILENAME)) {
            if (null == file) {
                Assertions.fail("Unable to find resource " + CharSequences.quote(J2CL_SHADE_FILENAME));
            }
            final Properties properties = new Properties();
            properties.load(file);

            final Map<String, String> map = Maps.sorted();
            properties.forEach((k, v) -> map.put((String) k, (String) v));

            this.methodSignaturesCheck(shadedType,
                    unshadedType,
                    map);
        }
    }

    default void methodSignaturesCheck(final Class<?> shadedType,
                                       final Class<?> unshadedType,
                                       final Map<String, String> shadings) {
        final Predicate<Method> publicProtectedMethodPredicate = m -> false == JavaVisibility.of(m).isOrLess(JavaVisibility.PACKAGE_PRIVATE);

        // keep only public and protected shadedType methods..........................................................
        final Set<Method> shadedPublicProtectedMethods = Arrays.stream(shadedType.getDeclaredMethods())
                .filter(publicProtectedMethodPredicate)
                .collect(Collectors.toSet());

        // this is used to translate method signatures of $emulatedPublicProtectedMethods into $jreType method signatures.
        final UnaryOperator<Class> typeShader = t -> {
            String typeName = t.getName();

            return shadings.entrySet()
                    .stream()
                    .filter(e -> typeName.startsWith(e.getKey()))
                    .map(e -> e.getValue() + typeName.substring(e.getKey().length()))
                    .map(n -> {
                        try {
                            return Class.forName(n);
                        } catch (final Exception notFound) {
                            throw new RuntimeException("Unable to find type " + CharSequences.quote(typeName));
                        }
                    })
                    .findFirst()
                    .orElse(t);
        };

        final Set<Method> badShadedMethods = Sets.ordered();
        for (final Method shadedMethod : shadedPublicProtectedMethods) {
            // try and locate the shaded method with the same name and parameters
            try {
                final Method unshadedMethod = unshadedType.getDeclaredMethod(shadedMethod.getName(),
                        Arrays.stream(shadedMethod.getParameterTypes())
                                .map(typeShader)
                                .toArray(Class[]::new)
                );

                if (false == typeShader.apply(shadedMethod.getReturnType()).equals(unshadedMethod.getReturnType()) ||
                        JavaVisibility.of(shadedMethod) != JavaVisibility.of(unshadedMethod) ||
                        false == MethodAttributes.get(shadedMethod).equals(MethodAttributes.get(unshadedMethod))) {
                    badShadedMethods.add(shadedMethod);
                }

            } catch (final Exception notFound) {
                badShadedMethods.add(shadedMethod);
                continue;
            }

            // verify jre method return type matches emulated
        }

        assertEquals(Sets.empty().stream().map(Object::toString).collect(Collectors.joining("\n")),
                badShadedMethods.stream().map(Method::toGenericString).collect(Collectors.joining("\n")),
                () -> "Several " + shadedType.getName() + " methods have different signatures compared to " + unshadedType.getName() + " shadings\n" + shadings.entrySet().stream().map(Entry::toString).collect(Collectors.joining("\n", "", "\n")));
    }
}
