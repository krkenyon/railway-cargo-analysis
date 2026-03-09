/*
 Core data model for the railway cargo analysis system.
*/
data class RailwaySystem(
    val stations: Map<StationId, Station>,
    val edges: Map<StationId, List<StationId>>,
    val start: StationId
)

@JvmInline
value class StationId(val value: Int)

@JvmInline
value class CargoType(val value: Int)

data class Station(
    val unload: CargoType,
    val load: CargoType
)
