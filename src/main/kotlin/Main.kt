/*
 Entry point for the railway cargo analysis program.

 The program reads the railway system description from standard input,
 parses it into a RailwaySystem, runs the cargo propagation analysis,
 and prints the cargo types that can arrive at each station.
*/
fun main() {
    val lines = generateSequence(::readlnOrNull).toList()
    val system = parseSystem(lines)

    val analysis = CargoAnalysis(system)
    val result = analysis.computeArrivalCargo()

    result.forEach { (station, cargo) ->
        val cargoList = cargo.map { it.value }.sorted().joinToString(" ")
        println("Station ${station.value}: $cargoList")
    }
}