fun parseSystem(lines: List<String>): RailwaySystem {
    if (lines.isEmpty()) {
        throw IllegalArgumentException("Input is empty")
    }

    var index = 0

    fun nextInts(expected: Int, context: String): List<Int> {
        if (index >= lines.size) {
            throw IllegalArgumentException("Unexpected end of input while reading $context")
        }

        val tokens = lines[index++].trim().split("\\s+".toRegex())

        if (tokens.size != expected) {
            throw IllegalArgumentException("$context must contain $expected integers")
        }

        return tokens.map {
            it.toIntOrNull()
                ?: throw IllegalArgumentException("Invalid integer '$it' in $context")
        }
    }

    val first = nextInts(2, "header")
    val stationCount = first[0]
    val trackCount = first[1]

    if (stationCount < 0 || trackCount < 0) {
        throw IllegalArgumentException("Counts must be non-negative")
    }

    val stations = mutableMapOf<StationId, Station>()

    repeat(stationCount) {
        val parts = nextInts(3, "station definition")

        val id = StationId(parts[0])
        val unload = CargoType(parts[1])
        val load = CargoType(parts[2])

        if (stations.containsKey(id)) {
            throw IllegalArgumentException("Duplicate station id ${parts[0]}")
        }

        stations[id] = Station(unload, load)
    }

    val edges = mutableMapOf<StationId, MutableList<StationId>>()

    repeat(trackCount) {
        val parts = nextInts(2, "track definition")

        val from = StationId(parts[0])
        val to = StationId(parts[1])

        if (!stations.containsKey(from)) {
            throw IllegalArgumentException("Track references unknown station ${parts[0]}")
        }

        if (!stations.containsKey(to)) {
            throw IllegalArgumentException("Track references unknown station ${parts[1]}")
        }

        edges.computeIfAbsent(from) { mutableListOf() }.add(to)
    }

    if (index >= lines.size) {
        throw IllegalArgumentException("Missing start station line")
    }

    val start = lines[index].trim().toIntOrNull()
        ?: throw IllegalArgumentException("Invalid start station id")

    val startId = StationId(start)

    if (!stations.containsKey(startId)) {
        throw IllegalArgumentException("Start station does not exist")
    }

    return RailwaySystem(stations, edges, startId)
}