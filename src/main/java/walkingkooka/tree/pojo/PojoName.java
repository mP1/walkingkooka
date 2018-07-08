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
 */
package walkingkooka.tree.pojo;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Holds the name of a node within the tree. The name will be either a field/property name or an index.
 */
public final class PojoName implements Name, Comparable<PojoName> {

    private final static int INDEX_CACHE_SIZE = 128;

    static PojoName index(final int index) {
        if(index < 0){
            throw new IllegalArgumentException("Index " + index + " must not be negative");
        }
        return index < INDEX_CACHE_SIZE ? INDEX_CACHE[index] : new PojoName(index);
    }

    private final static PojoName[] INDEX_CACHE = fillIndexCache();

    private static PojoName[] fillIndexCache() {
        final PojoName[] cache = new PojoName[INDEX_CACHE_SIZE];
        for(int i = 0; i < INDEX_CACHE_SIZE; i++){
            cache[i] = new PojoName(i);
        }
        return cache;
    }

    static PojoName property(final String name) {
        Objects.requireNonNull(name, "name");
        Predicates.failIfNullOrFalse(name, NAME_PREDICATE, "Name is invalid property name %s");

        return new PojoName(name, -1);
    }

    private final static Predicate<CharSequence> NAME_PREDICATE = Predicates.javaIdentifier();

    private PojoName(final int index){
        this(String.valueOf(index), index);
    }

    private PojoName(final String name, final int index){
        this.name = name;
        this.index = index;
    }

    void check(final PojoProperty property) {
        if(!property.name().equals(this)){
            throw new IllegalArgumentException("Invalid child name mismatch expected " + CharSequences.quote(property.name().value()) + " got " + CharSequences.quote(this.name));
        }
    }

    // Name ......................................................................................

    @Override
    public String value() {
        return this.name;
    }

    CharSequence inQuotes() {
        return CharSequences.quote(this.value());
    }

    private final String name;

    final int index;

    // Comparable....................................................................................................

    @Override
    public int compareTo(final PojoName other) {
        return this.name.compareTo(other.name);
    }

    // Object....................................................................................................

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof PojoName && this.equals0(Cast.to(other));
    }

    private boolean equals0(final PojoName other){
        return this.name.equals(other.name);
    }

    public String toString(){
        return this.name;
    }
}
