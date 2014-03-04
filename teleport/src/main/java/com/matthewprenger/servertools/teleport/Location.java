/*
 * Copyright 2014 Matthew Prenger
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

package com.matthewprenger.servertools.teleport;

public class Location {

    public final int dimID;
    public final double x, y, z;

    public Location(int dimID, double x, double y, double z) {

        this.dimID = dimID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(int dimID, int x, int y, int z) {

        this.dimID = dimID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int hashCode() {

        return 31 * dimID * (int)x * (int)y * (int)z;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Location) {
            Location loc = (Location) obj;

            return this.dimID == loc.dimID && this.x == loc.x && this.y == loc.y && this.z == loc.z;
        }

        return false;
    }

    @Override
    public String toString() {

        return String.format("DIM: %s, X: %s, Y: %s, Z: %s", this.dimID, this.x, this.y, this.z);
    }
}
