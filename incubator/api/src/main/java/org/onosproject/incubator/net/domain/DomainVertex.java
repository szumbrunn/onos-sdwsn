/*
 * Copyright 2015 Open Networking Laboratory
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
 */
package org.onosproject.incubator.net.domain;

import com.google.common.annotations.Beta;
import org.onlab.graph.Vertex;
import org.onosproject.net.DeviceId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Representation of the intent domain or a device that is part of the intent
 * domain graph.
 */
@Beta
public class DomainVertex implements Vertex {
    // FIXME we will want to add a type enum or subclasses for the two different types

    // A domain vertex is either an intent domain or a device:
    private final IntentDomainId id;
    // ----- or -----
    private final DeviceId deviceId;

    // Serialization constructor
    private DomainVertex() {
        this.id = null;
        this.deviceId = null;
    }

    DomainVertex(IntentDomainId id) {
        this.id = checkNotNull(id, "Intent domain ID cannot be null.");
        this.deviceId = null;
    }

    DomainVertex(DeviceId id) {
        this.id = null;
        this.deviceId = checkNotNull(id, "Device ID cannot be null.");
    }
}
