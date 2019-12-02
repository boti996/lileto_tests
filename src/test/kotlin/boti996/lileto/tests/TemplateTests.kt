package boti996.lileto.tests

import boti996.lileto.tests.helpers.*
import org.jetbrains.spek.api.Spek

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

        evaluatedTemplateInPlaintext_possibleContent(
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
            description = listOf("Create multiple template objects", "with insertion.")
        )
    )

    this.evaluateTestcases(testCases, BracketType.TEMPLATE)
})

internal fun evaluatedTemplateInPlaintext(templateContent: String,
                                          expected: String,
                                          evaluated: Boolean = true,
                                          description: List<String> = listOf("Inserted evaluated template bracket", "into plaintext."))
        : testcase {

    val evaluate = if (evaluated) ".text" else ""
    return singleBracketInPlaintext(
        BracketWithContent(
            BracketType.COMMAND,
    " template = ${BracketType.TEMPLATE.open()}$templateContent${BracketType.TEMPLATE.close()}\n" +
            "output = template$evaluate "),
        expected,
        description = description
    )
}

internal fun nonEvaluatedTemplateInPlaintext(templateContent: String,
                                             expected: String,
                                             description: List<String> = listOf("Inserted non-evaluated template bracket", "into plaintext."))
        : testcase {

    return evaluatedTemplateInPlaintext(templateContent, expected, evaluated = false)
}

internal fun evaluatedTemplateInPlaintext_possibleContent(templateContent: String,
                                                          expected: String,
                                                          description: List<String> = listOf(
                                                              "Inserted evaluated template bracket with possible content-elements", "into plaintext."))
        : testcase {

    return evaluatedTemplateInPlaintext(
        templateContent +
                " [${BracketType.TEXT.open()}text braclet${BracketType.TEXT.close()}]" +
                " [${BracketType.SPECIAL_CHAR.open()}${SpecialCharacter.VBAR.literal()}${BracketType.SPECIAL_CHAR.close()}]" +
                " [${BracketType.TEMPLATE.open()}=empty_slot${BracketType.TEMPLATE.close()}]",
        "$expected [text bracket] [${SpecialCharacter.VBAR.character()}] []",
        description = description
    )
}

internal fun evaluatedTemplateInPlaintext_withSlot(templateContent: String,
                                                   slotContent: String,
                                                   expected: String,
                                                   description: List<String> = listOf("Inserted evaluated template with a slot", "into plaintext."))
        : testcase {

    return singleBracketInPlaintext(
        BracketWithContent(
            BracketType.COMMAND,
            " template = ${BracketType.TEMPLATE.open()}$templateContent${BracketType.TEMPLATE.close()}\n" +
                    "template.slot = $slotContent" +
                    "output = template.text "),
        expected,
        description = description
    )
}

internal fun evaluatedTemplateInPlaintext_withUnfilledSlot(templateContent: String,
                                                           expected: String,
                                                           description: List<String> = listOf(
                                                               "Inserted evaluated template with an unfilled slot", "into plaintext."))
        : testcase {

    return evaluatedTemplateInPlaintext_withSlot(
        templateContent,
        liletoNullValue,
        expected,
        description = description)
}

internal fun multipleSlotsInTemplate(templateContent: String,
                                     dataToLoad: String,
                                     expected: String,
                                     description: List<String> = listOf("Inserted evaluated template with multiple slots", "into plaintext."))
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
        description = description
    )
}
