/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.jackrabbit.oak.plugins.index.lucene.writer;

import javax.annotation.Nullable;

import org.apache.jackrabbit.oak.plugins.index.lucene.IndexCopier;
import org.apache.jackrabbit.oak.plugins.index.lucene.IndexDefinition;
import org.apache.jackrabbit.oak.plugins.index.lucene.LuceneIndexConstants;
import org.apache.jackrabbit.oak.plugins.index.lucene.directory.DefaultDirectoryFactory;
import org.apache.jackrabbit.oak.plugins.index.lucene.directory.DirectoryFactory;
import org.apache.jackrabbit.oak.spi.blob.GarbageCollectableBlobStore;
import org.apache.jackrabbit.oak.spi.mount.MountInfoProvider;
import org.apache.jackrabbit.oak.spi.state.NodeBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultIndexWriterFactory implements LuceneIndexWriterFactory {
    private final MountInfoProvider mountInfoProvider;
    private final DirectoryFactory directoryFactory;

    public DefaultIndexWriterFactory(MountInfoProvider mountInfoProvider,
        @Nullable IndexCopier indexCopier, @Nullable GarbageCollectableBlobStore blobStore) {
        this(mountInfoProvider, new DefaultDirectoryFactory(indexCopier, blobStore));
    }

    public DefaultIndexWriterFactory(MountInfoProvider mountInfoProvider,
                                     DirectoryFactory directoryFactory) {
        this.mountInfoProvider = checkNotNull(mountInfoProvider);
        this.directoryFactory = checkNotNull(directoryFactory);
    }

    @Override
    public LuceneIndexWriter newInstance(IndexDefinition definition,
                                         NodeBuilder definitionBuilder, boolean reindex) {
        if (mountInfoProvider.hasNonDefaultMounts()){
            return new MultiplexingIndexWriter(directoryFactory, mountInfoProvider, definition,
                definitionBuilder, reindex);
        }
        return new DefaultIndexWriter(definition, definitionBuilder, directoryFactory,
            LuceneIndexConstants.INDEX_DATA_CHILD_NAME,
            LuceneIndexConstants.SUGGEST_DATA_CHILD_NAME, reindex);
    }
}
