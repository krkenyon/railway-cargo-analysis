fun main() {
    val lines = generateSequence(::readlnOrNull).toList()
    val system = parseSystem(lines)

    val analysis = CargoAnalysis(system)
    val result = analysis.computeArrivalCargo()

    result.forEach { (station, cargo) ->
        println("${station.value}: ${cargo.map { it.value }.sorted().joinToString(" ")}")
    }
}