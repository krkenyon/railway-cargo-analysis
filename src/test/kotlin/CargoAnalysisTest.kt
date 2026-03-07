import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class CargoAnalysisTest {
    @Test
    fun simpleChain() {
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

        assertEquals(emptySet<CargoType>(), result[StationId(1)])
        assertEquals(setOf(CargoType(1)), result[StationId(2)])
        assertEquals(setOf(CargoType(1), CargoType(2)), result[StationId(3)])
    }
}