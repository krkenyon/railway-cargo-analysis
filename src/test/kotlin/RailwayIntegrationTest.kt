import org.junit.jupiter.api.Test

class RailwayIntegrationTest {

    @Test
    fun `inline example parses and analyzes correctly`() {
        val system = parseSystem(exampleInputLines())
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(exampleExpectedArrivals(), result)
    }

    @Test
    fun `resource example parses and analyzes correctly`() {
        val lines = checkNotNull(javaClass.getResource("/input.txt")) {
            "Missing test resource: input.txt"
        }.readText()
            .lines()
            .filter { it.isNotBlank() }

        val system = parseSystem(lines)
        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(exampleExpectedArrivals(), result)
    }
}