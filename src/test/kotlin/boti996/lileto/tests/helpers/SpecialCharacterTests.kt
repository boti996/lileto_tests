package boti996.lileto.tests.helpers

import boti996.lileto.tests.translateLileto
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.lang.StringBuilder
import kotlin.test.assertEquals


private val specialCharPlaceHolder = SpecialCharacter.VBAR

class SpecialCharacterTests : Spek({

    describe("Test the usage of ${BracketType.SPECIAL_CHAR.open()} special character ${BracketType.SPECIAL_CHAR.close()} brackets.") {
        val testCases = mutableListOf(
            singleBracketInPlaintext(
                BracketWithContent(
                    BracketType.SPECIAL_CHAR,
                    specialCharPlaceHolder.literal()
                ),
                specialCharPlaceHolder.character()
            ),

            singleBracketInPlaintext_noClosingMarkerChar(
                BracketWithContent(
                    BracketType.SPECIAL_CHAR,
                    specialCharPlaceHolder.literal()
                ),
                specialCharPlaceHolder.character()
            ),

            singleBracketInPlaintext_trimWhitespaces(
                BracketWithContent(
                    BracketType.SPECIAL_CHAR,
                    specialCharPlaceHolder.literal()
                ),
                specialCharPlaceHolder.character()
            ),

            multipleBracketsInPlaintext(
                bracketListOf(SpecialCharacter.values().map {
                        specialCharacter ->  BracketType.SPECIAL_CHAR to specialCharacter.literal() }),
                SpecialCharacter.values().map { specialCharacter -> specialCharacter.character() }
            ),

            multipleSpecialCharactersInOneBracket(10),

            multipleSpecialCharactersInOneBracket_notCommaSeparated(10),

            multipleSpecialCharactersInOneBracket_unicodeChars(10)
        )

        // Assertions
        testCases.forEach { (input, expected) ->
            it("ASSERTION: $expected") {
                assertEquals(expected, translateLileto(input))
            }
        }
    }
})

internal fun multipleSpecialCharactersInOneBracket(count: Int)
        = _multipleSpecialCharactersInOneBracket(count,
    listOf("Multiple special characters", "in one bracket."))

internal fun multipleSpecialCharactersInOneBracket_notCommaSeparated(count: Int)
        = _multipleSpecialCharactersInOneBracket(count,
    listOf("Multiple special characters", "with no comma separation."),
    useCommas = false)

internal fun multipleSpecialCharactersInOneBracket_unicodeChars(count: Int)
        = _multipleSpecialCharactersInOneBracket(count,
    listOf("Multiple special characters", "using Unicode character codes"),
    useUnicode = true)

internal fun _multipleSpecialCharactersInOneBracket(count: Int,
                                                    description: List<String>,
                                                    useCommas: Boolean = true,
                                                    useUnicode: Boolean = false)
        : testcase {

    assert(count >= 0)

    descriptionListSizeAssertion(description, 2)

    val specialCharacters = SpecialCharacter.values()

    fun getRandomUnicode() : Char = (Char.MIN_VALUE..Char.MAX_VALUE).random()

    fun getRandomCharacter() : Any
            = if (useUnicode) getRandomUnicode()
              else specialCharacters.random()

    fun getContentCharacter(char: Any)
            = if (char is Char) char.toInt().toString()
              else (char as SpecialCharacter).literal()

    fun getEvaluatedCharacter(char: Any)
            = if (char is Char) char.toString()
              else (char as SpecialCharacter).character()

    val randomContent = StringBuilder()
    val evaluatedContent = StringBuilder()

    randomContent.append("${description[0]} [")
    evaluatedContent.append("${description[0]} [")

    randomContent.append(BracketType.SPECIAL_CHAR.open())

    for (i in 0 until count) {
        val character = getRandomCharacter()
        randomContent.append(getContentCharacter(character))
        if (useCommas && i < count - 1) randomContent.append(',')
        evaluatedContent.append(getEvaluatedCharacter(character))
    }

    randomContent.append(BracketType.SPECIAL_CHAR.close())

    randomContent.append("] ${description[1]}")
    evaluatedContent.append("] ${description[1]}")

    return buildTestCaseEntry(listOf(randomContent.toString()), evaluatedContent.toString())
}
