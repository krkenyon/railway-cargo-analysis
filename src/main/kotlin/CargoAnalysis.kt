class CargoAnalysis(private val system: RailwaySystem) {

    /*
     Propagates cargo information through the railway graph starting from the initial station.
     A worklist queue is used so propagation continues until a fixed point is reached,
     allowing cycles in the graph.
    */
    fun computeArrivalCargo(): Map<StationId, Set<CargoType>> {
        // set of cargo that can arrive at every station
        val inCargo: MutableMap<StationId, MutableSet<CargoType>> = mutableMapOf()

        // set of cargo that can leave at every station (used for calculation purposes)
        val outCargo: MutableMap<StationId, MutableSet<CargoType>> = mutableMapOf()

        // initialise inCargo, outCargo and stationsToUpdate
        system.stations.forEach { (stationId, station) ->
            inCargo[stationId] = mutableSetOf<CargoType>()
            outCargo[stationId] = mutableSetOf<CargoType>(station.load)
        }
        val stationsToUpdate: ArrayDeque<StationId> = ArrayDeque()
        stationsToUpdate.add(system.start)

        while (stationsToUpdate.isNotEmpty()) {

            val updatedStation = stationsToUpdate.removeFirst()

            val adjacentStations = system.edges[updatedStation].orEmpty() // stations with no outgoing edges are treated as having an empty adjacency list.

            // update every station in the adjacency list with the new cargo information
            for (station in adjacentStations) {

                var changed = false

                for (cargo in outCargo.getValue(updatedStation)) {
                    if (cargo !in inCargo.getValue(station)) {
                        // something has changed in the inCargo so we add to update list later
                        changed = true
                        inCargo.getValue(station).add(cargo)


                        // add to outCargo as long as it's not the type that gets unloaded
                        if (cargo != system.stations.getValue(station).unload) {
                            outCargo.getValue(station).add(cargo)
                        }
                    }
                }
                if (changed) stationsToUpdate.add(station)
            }
        }
        return inCargo.mapValues { it.value.toSet() }
    }


}
