import org.junit.jupiter.api.Test

class CargoAnalysisTest {

    @Test
    fun `simple chain propagates cargo forward`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 99, load = 2),
                station(3, unload = 99, load = 3)
            ),
            edges = mapOf(
                edge(1, 2),
                edge(2, 3)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet(),
                2 to cargoSet(1),
                3 to cargoSet(1, 2)
            ),
            result
        )
    }

    @Test
    fun `start station has empty arrival cargo`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1)
            ),
            edges = emptyMap(),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet()
            ),
            result
        )
    }

    @Test
    fun `branches receive cargo independently`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 99, load = 2),
                station(3, unload = 99, load = 3)
            ),
            edges = mapOf(
                edge(1, 2, 3)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet(),
                2 to cargoSet(1),
                3 to cargoSet(1)
            ),
            result
        )
    }

    @Test
    fun `cargo merges from multiple incoming paths`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 99, load = 2),
                station(3, unload = 99, load = 3),
                station(4, unload = 99, load = 4)
            ),
            edges = mapOf(
                edge(1, 2, 3),
                edge(2, 4),
                edge(3, 4)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet(),
                2 to cargoSet(1),
                3 to cargoSet(1),
                4 to cargoSet(1, 2, 3)
            ),
            result
        )
    }

    @Test
    fun `unload removes matching cargo before onward travel`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 1, load = 2),
                station(3, unload = 99, load = 3)
            ),
            edges = mapOf(
                edge(1, 2),
                edge(2, 3)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet(),
                2 to cargoSet(1),
                3 to cargoSet(2)
            ),
            result
        )
    }

    @Test
    fun `unloading absent cargo has no effect`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 7, load = 2),
                station(3, unload = 99, load = 3)
            ),
            edges = mapOf(
                edge(1, 2),
                edge(2, 3)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet(),
                2 to cargoSet(1),
                3 to cargoSet(1, 2)
            ),
            result
        )
    }

    @Test
    fun `unreachable stations remain empty`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 99, load = 2),
                station(3, unload = 99, load = 3)
            ),
            edges = mapOf(
                edge(1, 2)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet(),
                2 to cargoSet(1),
                3 to emptySet()
            ),
            result
        )
    }

    @Test
    fun `full cycle reaches stable cargo set at every station`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 99, load = 2),
                station(3, unload = 99, load = 3)
            ),
            edges = mapOf(
                edge(1, 2),
                edge(2, 3),
                edge(3, 1)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to cargoSet(1, 2, 3),
                2 to cargoSet(1, 2, 3),
                3 to cargoSet(1, 2, 3)
            ),
            result
        )
    }

    @Test
    fun `partial cycle reaches fixed point`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 1),
                station(2, unload = 99, load = 2),
                station(3, unload = 99, load = 3)
            ),
            edges = mapOf(
                edge(1, 2),
                edge(2, 3),
                edge(3, 2)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to emptySet(),
                2 to cargoSet(1, 2, 3),
                3 to cargoSet(1, 2, 3)
            ),
            result
        )
    }

    @Test
    fun `self loop reaches stable result`() {
        val system = RailwaySystem(
            stations = mapOf(
                station(1, unload = 99, load = 5)
            ),
            edges = mapOf(
                edge(1, 1)
            ),
            start = StationId(1)
        )

        val result = CargoAnalysis(system).computeArrivalCargo()

        assertArrivalCargoEquals(
            expectedArrivals(
                1 to cargoSet(5)
            ),
            result
        )
    }
}