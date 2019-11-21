package boti996.lileto.tests

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

const val textPlaceHolder = "text bracket"

class TextTests : Spek({
    describe("Test the usage of ${BracketType.TEXT.open()} text ${BracketType.TEXT.close()} brackets.") {
        val testCases = mutableListOf(
            "Plain text should not change."
            to "Plain text should not change.",

            singleBracketInPlaintext(
                BracketWithContent(BracketType.TEXT, textPlaceHolder),
                textPlaceHolder),

            singleBracketInPlaintext_trimWhitespaces(
                BracketWithContent(BracketType.TEXT, textPlaceHolder),
                textPlaceHolder),

            multipleBracketsInPlaintext(
                bracketListOf(Array(3) {BracketType.TEXT to textPlaceHolder}),
                Array(3) {textPlaceHolder}.toList()),

            multipleSpecialBracketsEmbeddedIntoTextBracket(
                textPlaceHolder,
                SpecialCharacter.values().asList()
            )
        )

        // Test for each bracket type
        testCases.addAll(BracketType.values()
            .filter { bracket -> bracket != BracketType.SPECIAL_CHAR }
            .map { bracket -> singleBracketEmbeddedIntoTextBracket(BracketWithContent(bracket, " "))}
        )

        // Assertions
        testCases.forEach { (input, expected) ->
            it("ASSERTION: $expected") {
                assertEquals(expected, translateLileto(input))
            }
        }

    }
})

internal fun singleBracketEmbeddedIntoTextBracket(innerBracket: BracketWithContent)
        : Pair<String, String> {

    return singleBracketEmbeddedIntoBracket(
        description = listOf("Inserted brackets in text brackets", "should be escaped."),
        outerBracket = BracketWithContent(BracketType.TEXT, textPlaceHolder),
        innerBracket = innerBracket,
        expectedContent = "$textPlaceHolder${innerBracket.open()}${innerBracket.content}${innerBracket.close()}")
}

internal fun specialBracketEmbeddedIntoTextBracket(textContent: String, specialCharacter: SpecialCharacter)
        : Pair<String, String> {

    return multipleSpecialBracketsEmbeddedIntoTextBracket(textContent, listOf(specialCharacter))
}

internal fun  multipleSpecialBracketsEmbeddedIntoTextBracket(textContent: String, specialCharacters: List<SpecialCharacter>)
        : Pair<String, String> {

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
