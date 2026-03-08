import org.junit.jupiter.api.Assertions.*
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
        assertEquals(1, system.stations.size)
        assertEquals(0, system.edges.size)
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

        assertEquals(listOf(StationId(2), StationId(3)), system.edges[StationId(1)])
    }

    @Test
    fun `empty input throws`() {
        assertThrows(IllegalArgumentException::class.java) {
            parseSystem(emptyList())
        }
    }

    @Test
    fun `missing start line throws`() {
        val lines = listOf(
            "1 0",
            "1 99 5"
        )

        assertThrows(IllegalArgumentException::class.java) {
            parseSystem(lines)
        }
    }

    @Test
    fun `non integer token throws`() {
        val lines = listOf(
            "1 0",
            "1 X 5",
            "1"
        )

        assertThrows(IllegalArgumentException::class.java) {
            parseSystem(lines)
        }
    }

    @Test
    fun `duplicate station id throws`() {
        val lines = listOf(
            "2 0",
            "1 99 1",
            "1 99 2",
            "1"
        )

        assertThrows(IllegalArgumentException::class.java) {
            parseSystem(lines)
        }
    }

    @Test
    fun `edge referencing unknown station throws`() {
        val lines = listOf(
            "2 1",
            "1 99 1",
            "2 99 2",
            "1 3",
            "1"
        )

        assertThrows(IllegalArgumentException::class.java) {
            parseSystem(lines)
        }
    }

    @Test
    fun `start station must exist`() {
        val lines = listOf(
            "1 0",
            "1 99 5",
            "2"
        )

        assertThrows(IllegalArgumentException::class.java) {
            parseSystem(lines)
        }
    }
}