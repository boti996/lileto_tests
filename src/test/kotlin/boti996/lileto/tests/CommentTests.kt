package boti996.lileto.tests

import boti996.lileto.tests.helpers.*
import org.jetbrains.spek.api.Spek

private const val commentPlaceholder = "comment bracket"
private const val emptinessInMySoul = ""

class CommentTests : Spek({

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

    this.evaluateTestcases(testCases, BracketType.COMMENT)
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
