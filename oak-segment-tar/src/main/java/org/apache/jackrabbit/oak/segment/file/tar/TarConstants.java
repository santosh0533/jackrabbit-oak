/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jackrabbit.oak.segment.file.tar;

class TarConstants {

    private TarConstants() {
        // Prevent instantiation.
    }

    static final String FILE_NAME_FORMAT = "data%05d%s.tar";

    /**
     * Magic byte sequence at the end of the index block.
     * <p>
     * <ul>
     * <li>For each segment in that file, an index entry that contains the UUID,
     * the offset within the file and the size of the segment. Sorted by UUID,
     * to allow using interpolation search.</li>
     * <li>
     * The index footer, which contains metadata of the index (the size,
     * checksum).</li>
     * </ul>
     */
    static final int INDEX_MAGIC = ('\n' << 24) + ('1' << 16) + ('K' << 8) + '\n';

    /**
     * Magic byte sequence at the end of the graph block.
     * <p>
     * The file is read from the end (the tar file is read from the end: the
     * last entry is the index, then the graph). File format:
     * <ul>
     * <li>0 padding to make the footer end at a 512 byte boundary</li>
     * <li>The list of UUIDs (segments included the graph; this includes
     * segments in this tar file, and referenced segments in tar files with a
     * lower sequence number). 16 bytes each.</li>
     * <li>The graph data. The index of the source segment UUID (in the above
     * list, 4 bytes), then the list of referenced segments (the indexes of
     * those; 4 bytes each). Then the list is terminated by -1.</li>
     * <li>The last part is the footer, which contains metadata of the graph
     * (size, checksum, the number of UUIDs).</li>
     * </ul>
     */
    static final int GRAPH_MAGIC = ('\n' << 24) + ('0' << 16) + ('G' << 8) + '\n';

    /**
     * Magic sequence at the end of the binary references block.
     */
    static final int BINARY_REFERENCES_MAGIC = ('\n' << 24) + ('0' << 16) + ('B' << 8) + '\n';

    /**
     * The tar file block size.
     */
    static final int BLOCK_SIZE = 512;

}
