fun parseSystem(lines: List<String>): RailwaySystem {
    var index = 0

    fun nextInts(): List<Int> =
        lines[index++].trim().split("\\s+".toRegex()).map { it.toInt() }

    val first = nextInts()
    val stationCount = first[0]
    val trackCount = first[1]

    val stations = mutableMapOf<StationId, Station>()

    repeat(stationCount) {
        val parts = nextInts()
        val id = StationId(parts[0])
        val unload = CargoType(parts[1])
        val load = CargoType(parts[2])
        stations[id] = Station(unload, load)
    }

    val edges = mutableMapOf<StationId, MutableList<StationId>>()

    repeat(trackCount) {
        val parts = nextInts()
        val from = StationId(parts[0])
        val to = StationId(parts[1])
        edges.computeIfAbsent(from) { mutableListOf() }.add(to)
    }

    val start = StationId(lines[index].trim().toInt())

    return RailwaySystem(stations, edges, start)
}