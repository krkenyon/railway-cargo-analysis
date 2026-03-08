import org.junit.jupiter.api.Assertions.assertEquals

fun station(id: Int, unload: Int, load: Int): Pair<StationId, Station> =
    StationId(id) to Station(
        unload = CargoType(unload),
        load = CargoType(load)
    )

fun edge(from: Int, vararg to: Int): Pair<StationId, List<StationId>> =
    StationId(from) to to.map(::StationId)

fun cargoSet(vararg cargo: Int): Set<CargoType> =
    cargo.map(::CargoType).toSet()

fun expectedArrivals(vararg entries: Pair<Int, Set<CargoType>>): Map<StationId, Set<CargoType>> =
    entries.associate { (stationId, cargo) -> StationId(stationId) to cargo }

fun assertArrivalCargoEquals(
    expected: Map<StationId, Set<CargoType>>,
    actual: Map<StationId, Set<CargoType>>
) {
    assertEquals(expected, actual)
}

fun exampleInputLines() = listOf(
    "3 2",
    "1 99 1",
    "2 99 2",
    "3 99 3",
    "1 2",
    "2 3",
    "1"
)

fun exampleExpectedArrivals() = expectedArrivals(
    1 to emptySet(),
    2 to cargoSet(1),
    3 to cargoSet(1, 2)
)