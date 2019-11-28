package boti996.lileto.tests.helpers

import com.sun.org.apache.xpath.internal.operations.Bool

internal fun descriptionListSizeAssertion(description: List<String>, size: Int = 2) =
    assert(description.size == size)
    { "Description must contain the first and last part of the description sentence." }

internal fun trimWhitespaces(string: String, shouldTrim: Boolean = true)
        = if (shouldTrim) string.trim() else string

internal fun useClosingMark(bracket: BracketWithContent, shouldUseClosingMark: Boolean = true)
        = if (shouldUseClosingMark) bracket.close() else "}"

/* General test cases */

internal fun singleBracketInPlaintext(bracket: BracketWithContent,
                                      expectedContent: String,
                                      description: List<String> = listOf("Inserting bracket", "into plain text."))
        : testcase {

    return _multipleBracketsInPlaintext(
        brackets = listOf(bracket),
        expectedContents = listOf(expectedContent),
        description = description
    )
}

internal fun singleBracketInPlaintext_noClosingMarkerChar(bracket: BracketWithContent,
                                                          expectedContent: String)
        : testcase {
    return _multipleBracketsInPlaintext(
        brackets = listOf(bracket),
        expectedContents = listOf(expectedContent),
        description = listOf("Inserting bracket without closing marker character", "into plaintext."),
        useClosingMark = false
    )
}

internal fun singleBracketInPlaintext_trimWhitespaces(bracket: BracketWithContent,
                                                      expectedContent: String)
        : testcase  {

    return _multipleBracketsInPlaintext(
        brackets = listOf(bracket),
        expectedContents = listOf(expectedContent),
        description = listOf(
            "Trim unwanted whitespaces     \t\t\n \t\t\n\n  \n\t\t\t",
            "   \t\t \n  \t\t \t\n \t\t\t \t\t before and after brackets."
        )
    )
}

internal fun multipleBracketsInPlaintext(brackets: List<BracketWithContent>,
                                        expectedContents: List<String>,
                                         description: List<String> = listOf("Insert multiple brackets", "into plain text."))
        : testcase {
    return _multipleBracketsInPlaintext(
        brackets = brackets,
        expectedContents = expectedContents,
        description = description
    )
}

internal fun _multipleBracketsInPlaintext(brackets: List<BracketWithContent>,
                                         expectedContents: List<String>,
                                         description: List<String>,
                                         trimWhitespaces: Boolean = true,
                                         useClosingMark: Boolean = true)
        : testcase {

    descriptionListSizeAssertion(description, 2)

    val strings = mutableListOf<Any>()
    val expectedStrings = mutableListOf<Any>()
    strings.add(description[0])
    expectedStrings.add(trimWhitespaces(description[0], trimWhitespaces))

    for (i in brackets.indices) {
        strings.add(" [")
        strings.add(brackets[i].open())
        strings.add(brackets[i].content)
        strings.add(useClosingMark(brackets[i], useClosingMark))
        strings.add("] ")

        expectedStrings.add(" [")
        expectedStrings.add(expectedContents[i])
        expectedStrings.add("] ")
    }
    strings.add(description[1])
    expectedStrings.add(trimWhitespaces(description[1], trimWhitespaces))

    return buildTestCaseEntry(
        strings,
        expectedStrings.joinToString(separator = "")
    )
}

internal fun singleBracketEmbeddedIntoBracket(outerBracket: BracketWithContent,
                                              innerBracket: BracketWithContent,
                                              expectedContent: String,
                                              description: List<String> = listOf("Insert bracket embedded", "into another bracket."))
        : testcase {

    descriptionListSizeAssertion(description, 2)

    return buildTestCaseEntry(
        listOf(
            description[0],
            " [${outerBracket.open()}",
            outerBracket.content,
            innerBracket.open(), innerBracket.content, innerBracket.close(),
            "${outerBracket.close()}] ",
            description[1]
        ),

        listOf(
            description[0],
            // NOTE: this is the expected content of the outer bracket
            " [$expectedContent] ",
            description[1]
        ).joinToString(separator = "")
    )
}
