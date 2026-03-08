import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CargoAnalysisTest {

    private fun exampleInputLines() = listOf(
        "3 2",
        "1 99 1",
        "2 99 2",
        "3 99 3",
        "1 2",
        "2 3",
        "1"
    )

    private fun exampleExpectedResult() = mapOf(
        StationId(1) to emptySet(),
        StationId(2) to setOf(CargoType(1)),
        StationId(3) to setOf(CargoType(1), CargoType(2))
    )

    private fun assertExampleResult(result: Map<StationId, Set<CargoType>>) {
        assertEquals(exampleExpectedResult(), result)
    }

    @Test
    fun `computes cargo for simple chain`() {
        val stations = mapOf(
            StationId(1) to Station(unload = CargoType(99), load = CargoType(1)),
            StationId(2) to Station(unload = CargoType(99), load = CargoType(2)),
            StationId(3) to Station(unload = CargoType(99), load = CargoType(3))
        )

        val edges = mapOf(
            StationId(1) to listOf(StationId(2)),
            StationId(2) to listOf(StationId(3))
        )

        val system = RailwaySystem(stations, edges, start = StationId(1))
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertExampleResult(result)
    }

    @Test
    fun `parses inline example input and analyzes correctly`() {
        val system = parseSystem(exampleInputLines())
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertExampleResult(result)
    }

    @Test
    fun `parses resource input and analyzes correctly`() {
        val lines = checkNotNull(javaClass.getResource("/input.txt")) {
            "Missing test resource: input.txt"
        }.readText().lines().filter { it.isNotBlank() }

        val system = parseSystem(lines)
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertExampleResult(result)
    }

    @Test
    fun `start station has empty arrival cargo`() {
        val stations = mapOf(
            StationId(1) to Station(unload = CargoType(99), load = CargoType(1))
        )

        val system = RailwaySystem(
            stations = stations,
            edges = emptyMap(),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertEquals(mapOf(StationId(1) to emptySet<CargoType>()), result)
    }

    @Test
    fun `cargo propagates independently down branches`() {
        val stations = mapOf(
            StationId(1) to Station(CargoType(99), CargoType(1)),
            StationId(2) to Station(CargoType(99), CargoType(2)),
            StationId(3) to Station(CargoType(99), CargoType(3))
        )

        val edges = mapOf(
            StationId(1) to listOf(StationId(2), StationId(3))
        )

        val system = RailwaySystem(stations, edges, StationId(1))
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertEquals(emptySet<CargoType>(), result[StationId(1)])
        assertEquals(setOf(CargoType(1)), result[StationId(2)])
        assertEquals(setOf(CargoType(1)), result[StationId(3)])
    }

    @Test
    fun `station unload removes matching cargo before onward travel`() {
        val stations = mapOf(
            StationId(1) to Station(CargoType(99), CargoType(1)),
            StationId(2) to Station(CargoType(1), CargoType(2)),
            StationId(3) to Station(CargoType(99), CargoType(3))
        )

        val edges = mapOf(
            StationId(1) to listOf(StationId(2)),
            StationId(2) to listOf(StationId(3))
        )

        val system = RailwaySystem(stations, edges, StationId(1))
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertEquals(emptySet<CargoType>(), result[StationId(1)])
        assertEquals(setOf(CargoType(1)), result[StationId(2)])
        assertEquals(setOf(CargoType(2)), result[StationId(3)])
    }

    @Test
    fun `unreachable stations do not gain cargo from start component`() {
        val stations = mapOf(
            StationId(1) to Station(CargoType(99), CargoType(1)),
            StationId(2) to Station(CargoType(99), CargoType(2)),
            StationId(3) to Station(CargoType(99), CargoType(3))
        )

        val edges = mapOf(
            StationId(1) to listOf(StationId(2))
        )

        val system = RailwaySystem(stations, edges, StationId(1))
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertEquals(emptySet<CargoType>(), result[StationId(1)])
        assertEquals(setOf(CargoType(1)), result[StationId(2)])
        assertEquals(emptySet<CargoType>(), result[StationId(3)])
    }
}