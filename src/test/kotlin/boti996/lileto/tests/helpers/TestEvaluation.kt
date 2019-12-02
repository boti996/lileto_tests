package boti996.lileto.tests.helpers

import boti996.lileto.tests.translateLileto
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

internal fun SpecBody.evaluateTestcases(testCases: testcases, bracketType: BracketType) {

    this.describe("Test the usage of ${bracketType.open()} ${bracketType.name.toLowerCase().replace("_", " ")} ${bracketType.close()} brackets.") {

        // Assertions
        testCases.forEach { (input, expected) ->
            it("ASSERTION: ${expected.replace(Regex("\\s*\\[.*]\\s*"), " ")}") {
                    assertEquals(expected, translateLileto(input))
            }
        }
    }
}