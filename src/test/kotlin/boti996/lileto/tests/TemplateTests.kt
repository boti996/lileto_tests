package boti996.lileto.tests

import boti996.lileto.tests.helpers.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

private val templatePlaceholder = "template bracket"
private val templateWithSlotPlaceholder = "template bracket with slot {<=slot>} "
private const val emptinessInMySoul = ""

class TemplateTests : Spek({

    describe("Test the usage of ${BracketType.TEMPLATE.open()} template ${BracketType.TEMPLATE.close()} brackets.") {
        val testCases = mutableListOf(
            singleBracketInPlaintext(
                BracketWithContent(
                    BracketType.TEMPLATE,
                    templatePlaceholder
                ),
                emptinessInMySoul
            ),

            singleBracketInPlaintext_trimWhitespaces(
                BracketWithContent(
                    BracketType.SPECIAL_CHAR,
                    templatePlaceholder
                ),
                emptinessInMySoul
            ),

            multipleBracketsInPlaintext(
                bracketListOf(Array(3) { BracketType.TEMPLATE to templatePlaceholder }.toList()),
                Array(3) { emptinessInMySoul }.toList()
            ),

            evaluatedTemplateInPlaintext(
                templatePlaceholder,
                templatePlaceholder
                ),

            nonEvaluatedTemplateInPlaintext(
                templatePlaceholder,
                emptinessInMySoul
            )


        )

        // Assertions
        testCases.forEach { (input, expected) ->
            it("ASSERTION: $expected") {
                assertEquals(expected, translateLileto(input))
            }
        }
    }
})

internal fun evaluatedTemplateInPlaintext(templateContent: String,
                                          expected: String,
                                          evaluated: Boolean = true)
        : testcase {

    val evaluate = if (evaluated) ".text" else ""
    return singleBracketInPlaintext(
        BracketWithContent(
            BracketType.COMMAND,
            " template = ${BracketType.TEMPLATE.open()}$templateContent${BracketType.TEMPLATE.close()}\n" +
                    "output = template$evaluate "),
        expected
    )
}

internal fun nonEvaluatedTemplateInPlaintext(templateContent: String,
                                          expected: String)
        : testcase {

    return evaluatedTemplateInPlaintext(templateContent, expected, evaluated = false)
}
