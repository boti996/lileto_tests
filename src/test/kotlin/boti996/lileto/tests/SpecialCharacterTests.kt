package boti996.lileto.tests

import boti996.lileto.tests.helpers.*
import org.jetbrains.spek.api.Spek

private val specialCharPlaceholder = SpecialCharacter.VBAR

class SpecialCharacterTests : Spek({

    val testCases = mutableListOf(
        singleBracketInPlaintext(
            BracketWithContent(
                BracketType.SPECIAL_CHAR,
                specialCharPlaceholder.literal()
            ),
            specialCharPlaceholder.character()
        ),

        singleBracketInPlaintext_noClosingMarkerChar(
            BracketWithContent(
                BracketType.SPECIAL_CHAR,
                specialCharPlaceholder.literal()
            ),
            specialCharPlaceholder.character()
        ),

        singleBracketInPlaintext_trimWhitespaces(
            BracketWithContent(
                BracketType.SPECIAL_CHAR,
                specialCharPlaceholder.literal()
            ),
            specialCharPlaceholder.character()
        ),

        multipleBracketsInPlaintext(
            bracketListOf(SpecialCharacter.values().map { specialCharacter -> BracketType.SPECIAL_CHAR to specialCharacter.literal() }),
            SpecialCharacter.values().map { specialCharacter -> specialCharacter.character() }
        ),

        multipleSpecialCharactersInOneBracket(10),

        multipleSpecialCharactersInOneBracket_notCommaSeparated(10),

        multipleSpecialCharactersInOneBracket_unicodeChars(10)
    )

    this.evaluateTestcases(testCases, BracketType.SPECIAL_CHAR)
})

internal fun multipleSpecialCharactersInOneBracket(count: Int)
        = _multipleSpecialCharactersInOneBracket(
    count,
    listOf("Multiple special characters", "in one bracket.")
)

internal fun multipleSpecialCharactersInOneBracket_notCommaSeparated(count: Int)
        = _multipleSpecialCharactersInOneBracket(
    count,
    listOf("Multiple special characters", "with no comma separation."),
    useCommas = false
)

internal fun multipleSpecialCharactersInOneBracket_unicodeChars(count: Int)
        = _multipleSpecialCharactersInOneBracket(
    count,
    listOf("Multiple special characters", "using Unicode character codes"),
    useUnicode = true
)

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

    return buildTestCaseEntry(
        listOf(randomContent.toString()),
        evaluatedContent.toString()
    )
}
