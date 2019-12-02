package boti996.lileto.tests

import boti996.lileto.tests.helpers.*
import org.jetbrains.spek.api.Spek

private const val textPlaceholder = "text bracket"

class TextTests : Spek({

    val testCases = mutableListOf(
        "Plain text should not change."
        to "Plain text should not change.",

        singleBracketInPlaintext(
            BracketWithContent(
                BracketType.TEXT,
                textPlaceholder
            ),
            textPlaceholder
        ),

        singleBracketInPlaintext_trimWhitespaces(
            BracketWithContent(
                BracketType.TEXT,
                textPlaceholder
            ),
            textPlaceholder
        ),

        multipleBracketsInPlaintext(
            bracketListOf(Array(3) { BracketType.TEXT to textPlaceholder }.toList()),
            Array(3) { textPlaceholder }.toList()
        ),

        multipleSpecialBracketsEmbeddedIntoTextBracket(
            textPlaceholder,
            SpecialCharacter.values().asList()
        )
    )

    // Test for each bracket type - except special character bracket
    testCases.addAll(
        BracketType.values()
        .filter { bracket -> bracket != BracketType.SPECIAL_CHAR }
        .map { bracket -> singleBracketEmbeddedIntoTextBracket(
            BracketWithContent(
                bracket,
                " "
            )
        )}
    )

    this.evaluateTestcases(testCases, BracketType.TEXT)
})

internal fun singleBracketEmbeddedIntoTextBracket(innerBracket: BracketWithContent)
        : testcase {

    return singleBracketEmbeddedIntoBracket(
        description = listOf("Inserted brackets in text brackets", "should be escaped."),
        outerBracket = BracketWithContent(
            BracketType.TEXT,
            textPlaceholder
        ),
        innerBracket = innerBracket,
        expectedContent = "$textPlaceholder${innerBracket.open()}${innerBracket.content}${innerBracket.close()}"
    )
}

//TODO: unused testcase
internal fun specialBracketEmbeddedIntoTextBracket(textContent: String, specialCharacter: SpecialCharacter)
        : testcase {

    return multipleSpecialBracketsEmbeddedIntoTextBracket(textContent, listOf(specialCharacter))
}

internal fun  multipleSpecialBracketsEmbeddedIntoTextBracket(textContent: String, specialCharacters: List<SpecialCharacter>)
        : testcase {

    val strings = mutableListOf<String>()
    strings.add("Inserted special bracket in text brackets [${BracketType.TEXT.open()}$textContent")
    strings.addAll(specialCharacters.map { specialCharacter ->
        " [${BracketType.SPECIAL_CHAR.open()}${specialCharacter.literal()}${BracketType.SPECIAL_CHAR.close()}] " }
    )
    strings.add("${BracketType.TEXT.close()}] should be resolved.")

    val resolvedSpecials = mutableListOf<String>()
    resolvedSpecials.add("Inserted special bracket in text brackets [$textContent")
    resolvedSpecials.addAll(specialCharacters.map { specialCharacter ->
        " [${specialCharacter.character()}] " }
    )
    resolvedSpecials.add("] should be resolved.")

    return buildTestCaseEntry(strings, resolvedSpecials.joinToString(separator = ""))
}
