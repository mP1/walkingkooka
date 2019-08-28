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

package walkingkooka.tree.json.marshall;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@link Runnable} when executed removes the parent {@link BasicMarshallerTypedGeneric}.
 */
final class BasicMarshallerTypedGenericRunnable implements Runnable {

    static BasicMarshallerTypedGenericRunnable with(final BasicMarshallerTypedGeneric<?> marshaller) {
        return new BasicMarshallerTypedGenericRunnable(marshaller);
    }

    private BasicMarshallerTypedGenericRunnable(final BasicMarshallerTypedGeneric<?> marshaller) {
        super();
        this.marshaller = marshaller;
    }

    @Override
    public void run() {
        if(false == this.removed.getAndSet(true)) {
            this.marshaller.remove();
        }
    }

    private final AtomicBoolean removed = new AtomicBoolean();

    @Override
    public String toString() {
        return this.marshaller.toString();
    }

    private final BasicMarshallerTypedGeneric<?> marshaller;
}
