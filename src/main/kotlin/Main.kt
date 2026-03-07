fun main() {
    val system = parseSystem(generateSequence(::readLine).toList())

    val analysis = CargoAnalysis(system)
    val result = analysis.computeArrivalCargo()

    result.forEach { (station, cargo) ->
        println("${station.value}: ${cargo.map { it.value }.sorted().joinToString(" ")}")
    }
}