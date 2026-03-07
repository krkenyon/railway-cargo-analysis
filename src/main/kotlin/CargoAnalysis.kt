class CargoAnalysis(private val system: RailwaySystem) {
    private fun initialiseCargoLists(i: StationId, station: Station) {
        inCargo[i] = mutableSetOf<CargoType>()
        outCargo[i] = mutableSetOf<CargoType>(station.load)
    }

    fun computeArrivalCargo(): Map<StationId, Set<CargoType>> {
        system.stations.forEach { (stationId, station) -> initialiseCargoLists(stationId, station) }
        val stationsToUpdate: ArrayDeque<StationId> = ArrayDeque()
        stationsToUpdate.add(system.start)
        while (stationsToUpdate.isNotEmpty()) {
            val first = stationsToUpdate.removeFirst()
            // station can be the end of the line so we use emptySet in this case
            val adjacentStations = system.edges[first] ?: emptySet()
            var updateStation = false
            for (station in adjacentStations) {
                for (cargo in (outCargo[first] ?: emptySet())) {
                    if (cargo !in (inCargo[station] ?: emptySet())) {
                        updateStation = true
                        inCargo[station]?.add(cargo)
                        if (cargo != system.stations[station]?.unload) {
                            outCargo[station]?.add(cargo)
                        }
                    }
                }
                if (updateStation) {
                    stationsToUpdate.add(station)
                    updateStation = false
                }
            }
        }
        return inCargo
    }


    private val inCargo: MutableMap<StationId, MutableSet<CargoType>> = mutableMapOf()
    private val outCargo: MutableMap<StationId, MutableSet<CargoType>> = mutableMapOf()

}
