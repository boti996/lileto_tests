package boti996.lileto.tests.helpers

import boti996.lileto.tests.translateLileto
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
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