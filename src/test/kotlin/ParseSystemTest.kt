// ParseSystemTest.kt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParseSystemTest {

    @Test
    fun `parses minimal valid input`() {
        val lines = listOf(
            "1 0",
            "1 99 5",
            "1"
        )

        val system = parseSystem(lines)

        assertEquals(StationId(1), system.start)
        assertEquals(
            mapOf(
                station(1, unload = 99, load = 5)
            ),
            system.stations
        )
        assertEquals(emptyMap<StationId, List<StationId>>(), system.edges)
    }

    @Test
    fun `parses branching edges correctly`() {
        val lines = listOf(
            "3 2",
            "1 99 1",
            "2 99 2",
            "3 99 3",
            "1 2",
            "1 3",
            "1"
        )

        val system = parseSystem(lines)

        assertEquals(
            mapOf(
                edge(1, 2, 3)
            ),
            system.edges
        )
    }

    @Test
    fun `parses stations and start station correctly`() {
        val lines = listOf(
            "2 1",
            "10 8 4",
            "20 9 5",
            "10 20",
            "10"
        )

        val system = parseSystem(lines)

        assertEquals(StationId(10), system.start)
        assertEquals(
            mapOf(
                StationId(10) to Station(CargoType(8), CargoType(4)),
                StationId(20) to Station(CargoType(9), CargoType(5))
            ),
            system.stations
        )
    }
}