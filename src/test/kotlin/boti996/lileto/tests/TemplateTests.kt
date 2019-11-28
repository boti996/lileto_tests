package boti996.lileto.tests

import boti996.lileto.tests.helpers.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

private const val templatePlaceholder = "template bracket"
private const val templateWithSlotPlaceholder = "template bracket with slot [ {<=slot>} ]"
private const val templateWith3SlotsPlaceholder = "template bracket with slots [ {</slot1>} ] [ {</slot2>} ] [ {</slot3>} ]"
private const val evaluatedTemplateWith3SlotsChunk = "template bracket with slots"
private const val slotPlaceholder = "slot"
private const val evaluatedTemplateWithSlot = "template bracket with slot [$slotPlaceholder]"
private const val evaluatedTemplateWithUnfilledSlot = "template bracket with slot []"
private const val emptinessInMySoul = ""

private fun addContanerElement(element: Any) = "${BracketType.TEXT.open()}$element${BracketType.TEXT.close()} "

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
            ),

            evaluatedTemplateInPlaintext_withSlot(
                templateWithSlotPlaceholder,
                slotPlaceholder,
                evaluatedTemplateWithSlot
            ),

            evaluatedTemplateInPlaintext_withUnfilledSlot(
                templateWithSlotPlaceholder,
                evaluatedTemplateWithUnfilledSlot
            ),

            multipleSlotsInTemplate(
                templateWith3SlotsPlaceholder,
                BracketType.CONTAINER.open() +
                        "slot1:=${addContanerElement("first")}" +
                        "slot2:=${addContanerElement("second")}" +
                        "slot3:=${addContanerElement("third")}" +
                        BracketType.CONTAINER.close(),
                "$evaluatedTemplateWith3SlotsChunk [first] [second] [third]"
            ),

            multipleSlotsInTemplate(
                templateWith3SlotsPlaceholder,
                BracketType.CONTAINER.open() +
                        "slot1 slot2 slot3 |\n" +
                        addContanerElement(11) +
                        addContanerElement(12) +
                        addContanerElement(13) +

                        addContanerElement(21) +
                        addContanerElement(22) +
                        addContanerElement(23) +

                        addContanerElement(liletoNullValue) +
                        addContanerElement(32) +
                        addContanerElement(liletoNullValue) +

                        addContanerElement(liletoNullValue) +
                        addContanerElement(liletoNullValue) +
                        addContanerElement(liletoNullValue) +

                        BracketType.CONTAINER.close(),
                "$evaluatedTemplateWith3SlotsChunk [11] [12] [13]" +
                        "$evaluatedTemplateWith3SlotsChunk [21] [22] [23]" +
                        "$evaluatedTemplateWith3SlotsChunk [] [32] []" +
                        "$evaluatedTemplateWith3SlotsChunk [] [] []",
                description = listOf("Load multiple lines of data", "into template")
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

//TODO: custom description
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

//TODO: custom description
internal fun nonEvaluatedTemplateInPlaintext(templateContent: String,
                                          expected: String)
        : testcase {

    return evaluatedTemplateInPlaintext(templateContent, expected, evaluated = false)
}

//TODO: custom description
internal fun evaluatedTemplateInPlaintext_withSlot(templateContent: String,
                                                   slotContent: String,
                                                   expected: String)
        : testcase {

    return singleBracketInPlaintext(
        BracketWithContent(
            BracketType.COMMAND,
            " template = ${BracketType.TEMPLATE.open()}$templateContent${BracketType.TEMPLATE.close()}\n" +
                    "template.slot = $slotContent" +
                    "output = template.text "),
        expected
    )
}

//TODO: custom description
internal fun evaluatedTemplateInPlaintext_withUnfilledSlot(templateContent: String,
                                                           expected: String)
        : testcase {

    return evaluatedTemplateInPlaintext_withSlot(
        templateContent,
        liletoNullValue,
        expected)
}

internal fun multipleSlotsInTemplate(templateContent: String,
                                     dataToLoad: String,
                                     expected: String,
                                     description: List<String> = listOf("Template with multiple slots", "evaluated"))
        : testcase {

    assert(dataToLoad.indexOf(BracketType.CONTAINER.open()) == 0 &&
            dataToLoad.indexOf(BracketType.CONTAINER.close()) == dataToLoad.length - 2) {
        "dataToLoad should be in a container bracket" }

    return singleBracketInPlaintext(
        BracketWithContent(
            BracketType.COMMAND,
            " template = ${BracketType.TEMPLATE.open()}$templateContent${BracketType.TEMPLATE.close()}\n" +
                    "< $dataToLoad\n" +
                    "output template.text"),
        expected,
        description
    )
}
