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

package walkingkooka.net.http.server;

import walkingkooka.Binary;
import walkingkooka.ToStringBuilder;
import walkingkooka.io.file.FileExtension;

import java.util.Objects;
import java.util.Optional;

/**
 * Holds A file response the filename and binary.
 */
public final class FileResponse {

    /**
     * Factory that creates a {@link FileResponse}
     */
    public static FileResponse with(final String filename, final Binary binary) {
        Objects.requireNonNull(filename, "filename");
        Objects.requireNonNull(binary, "binary");


        return new FileResponse(filename, binary);
    }

    private FileResponse(final String filename, final Binary binary) {
        super();

        this.filename = filename;
        this.binary = binary;
    }

    public String filename() {
        return this.filename;
    }

    private final String filename;

    /**
     * Returns the file extension if one is present.
     */
    public Optional<FileExtension> fileExtension() {
        return FileExtension.extract(this.filename);
    }

    public Binary binary() {
        return this.binary;
    }

    private final Binary binary;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.filename)
                .separator(" ")
                .value(this.binary)
                .build();
    }
}
