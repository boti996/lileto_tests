package boti996.lileto.tests

import boti996.lileto.tests.helpers.*
import boti996.lileto.tests.helpers.BracketType
import boti996.lileto.tests.helpers.BracketWithContent
import boti996.lileto.tests.helpers.bracketListOf
import boti996.lileto.tests.helpers.multipleBracketsInPlaintext
import boti996.lileto.tests.helpers.singleBracketInPlaintext
import boti996.lileto.tests.helpers.singleBracketInPlaintext_trimWhitespaces
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

private const val commentPlaceholder = "comment bracket"
private const val emptinessInMySoul = ""

class CommentTests : Spek({

    describe("Test the usage of ${BracketType.COMMENT.open()} comment ${BracketType.COMMENT.close()} brackets.") {
        val testCases = mutableListOf(
            singleBracketInPlaintext(
                BracketWithContent(
                    BracketType.COMMENT,
                    commentPlaceholder
                ),
                emptinessInMySoul
            ),

            singleBracketInPlaintext_trimWhitespaces(
                BracketWithContent(
                    BracketType.COMMENT,
                    commentPlaceholder
                ),
                emptinessInMySoul
            ),

            multipleBracketsInPlaintext(
                bracketListOf(Array(3) { BracketType.COMMENT to commentPlaceholder }.toList()),
                Array(3) { emptinessInMySoul }.toList()
            )
        )

        // Test for each bracket type
        testCases.addAll(
            BracketType.values()
                .map { bracket -> singleBracketEmbeddedIntoCommentBracket(
                    BracketWithContent(
                        bracket,
                        " "
                    )
                ) }
        )

        // Assertions
        testCases.forEach { (input, expected) ->
            it("ASSERTION: $expected") {
                assertEquals(expected, translateLileto(input))
            }
        }
    }
})

internal fun singleBracketEmbeddedIntoCommentBracket(innerBracket: BracketWithContent)
        : testcase {

    return singleBracketEmbeddedIntoBracket(
        description = listOf("Inserted brackets in comment brackets", "should be escaped."),
        outerBracket = BracketWithContent(
            BracketType.COMMENT,
            commentPlaceholder
        ),
        innerBracket = innerBracket,
        expectedContent = "$commentPlaceholder${innerBracket.open()}${innerBracket.content}${innerBracket.close()}"
    )
}
