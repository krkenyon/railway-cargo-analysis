fun main() {
    val first = readLine()!!.split(" ").map { it.toInt() }
    val S = first[0]
    val T = first[1]

    val stations = mutableMapOf<StationId, Station>()

    repeat(S) {
        val parts = readLine()!!.split(" ").map { it.toInt() }
        val s = StationId(parts[0])
        val unload = CargoType(parts[1])
        val load = CargoType(parts[2])

        stations[s] = Station(unload, load)
    }

    val edges = mutableMapOf<StationId, MutableList<StationId>>()

    repeat(T) {
        val parts = readLine()!!.split(" ").map { it.toInt() }
        val from = StationId(parts[0])
        val to = StationId(parts[1])

        edges.computeIfAbsent(from) { mutableListOf() }.add(to)
    }

    val start = StationId(readLine()!!.toInt())

    val system = RailwaySystem(stations, edges, start)

    val analysis = CargoAnalysis(system)
    val result = analysis.computeArrivalCargo()

    result.forEach { (station, cargo) ->
        println("$station: ${cargo.joinToString(" ")}")
    }
}