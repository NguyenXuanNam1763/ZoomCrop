package com.example.zoomexample


/**
 * Enum representing an edge in the crop window.
 */
enum class Edge {
    LEFT, TOP, RIGHT, BOTTOM;
    /**
     * Gets the coordinate of the Edge
     *
     * @return the Edge coordinate (x-coordinate for LEFT and RIGHT Edges and
     * the y-coordinate for TOP and BOTTOM edges)
     */
    /**
     * Sets the coordinate of the Edge. The coordinate will represent the
     * x-coordinate for LEFT and RIGHT Edges and the y-coordinate for TOP and
     * BOTTOM edges.
     *
     * @param coordinate the position of the edge
     */
    // Member Variables ////////////////////////////////////////////////////////
    var coordinate = 0f

    // Public Methods //////////////////////////////////////////////////////////
    companion object {
        /**
         * Gets the current width of the crop window.
         */
        val width: Float
            get() = RIGHT.coordinate - LEFT.coordinate

        /**
         * Gets the current height of the crop window.
         */
        val height: Float
            get() = BOTTOM.coordinate - TOP.coordinate
    }
}
