class CargoAnalysis(private val system: RailwaySystem) {
    // set of cargo that can arrive at every station
    private val inCargo: MutableMap<StationId, MutableSet<CargoType>> = mutableMapOf()

    // set of cargo that can leave at every station (used for calculation purposes)
    private val outCargo: MutableMap<StationId, MutableSet<CargoType>> = mutableMapOf()


    private fun initialiseCargoLists(i: StationId, station: Station) {
        inCargo[i] = mutableSetOf<CargoType>()
        outCargo[i] = mutableSetOf<CargoType>(station.load)
    }

    /* starting with initial station go through all possible paths and propagate cargo information,
    adding to a queue of stations to update to allow for cycles */
    fun computeArrivalCargo(): Map<StationId, Set<CargoType>> {
        // initialise inCargo, outCargo and stationsToUpdate
        system.stations.forEach { (stationId, station) -> initialiseCargoLists(stationId, station) }
        val stationsToUpdate: ArrayDeque<StationId> = ArrayDeque()
        stationsToUpdate.add(system.start)

        while (stationsToUpdate.isNotEmpty()) {

            val first = stationsToUpdate.removeFirst()

            val adjacentStations = system.edges[first]
                ?: emptySet() // if station is at the end of the line it won't be in edges so we assign empty set

            var addToUpdateList = false

            // update every station in the adjacency list with the new cargo information
            for (station in adjacentStations) {
                for (cargo in (outCargo[first] ?: emptySet())) {
                    if (cargo !in (inCargo[station] ?: emptySet())) {
                        // something has changed in the inCargo so we add to update list later
                        addToUpdateList = true
                        inCargo[station]?.add(cargo)

                        // add to outCargo as long as it's not the type that gets unloaded
                        if (cargo != system.stations[station]?.unload) {
                            outCargo[station]?.add(cargo)
                        }
                    }
                }
                if (addToUpdateList) {
                    stationsToUpdate.add(station)

                    // reset for future stations in adjacency list
                    addToUpdateList = false
                }
            }
        }
        return inCargo
    }


}
