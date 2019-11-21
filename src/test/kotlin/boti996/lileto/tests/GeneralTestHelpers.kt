package boti996.lileto.tests

/**
 * Store the literals of Lileto brackets
 * @param literals opening and closing characters
 */
internal enum class BracketType(private val literals: Pair<Char, Char>) {
    TEXT(Pair('"', '"')),
    TEMPLATE(Pair('<', '>')),
    SPECIAL_CHAR(Pair('$', '$')),
    COMMAND(Pair('!', '!')),
    CONTAINER(Pair('[', ']')),
    COMMENT(Pair('.', '.'));

    fun open() = "{${literals.first}"
    fun close() = "${literals.second}}"
}

/**
 * Store the literals of Lileto special characters
 * @param values Lileto literal and the resolved values
 */
internal enum class SpecialCharacter(private val values: Pair<String, Char>) {
    ENTER       (Pair("e", '\n')),
    TAB         (Pair("t", '\t')),
    SPACE       (Pair("s", ' ')),
    O_R_BRACKET (Pair("orb", '(')),
    C_R_BBRACKET(Pair("crb", ')')),
    EQUALS_SIGN (Pair("eq", '=')),
    PLUS_SIGN   (Pair("pl", '+')),
    O_C_BRACKET (Pair("ocb", '{')),
    C_C_BRACKET (Pair("ccb", '}')),
    POINT       (Pair("pe", '.')),
    COMMA       (Pair("co", ',')),
    SEMICOLON   (Pair("sc", ';')),
    COLON       (Pair("cl", ':')),
    VBAR        (Pair("pi", '|')),
    LT_SIGN     (Pair("ls", '<')),
    GT_SIGN     (Pair("gt", '>')),
    QUESTION_M  (Pair("qm", '?')),
    EXCLAM_M    (Pair("dm", '!'));

    fun literal() = values.first
    fun character() = values.second.toString()
}

/**
 * Store bracket with it's content
 * @param bracket type of bracket
 */
internal data class BracketWithContent(val bracket: BracketType, val content: Any) {
    fun open() = bracket.open()
    fun close() = bracket.close()
}

/**
 * Convert an [Array] of [BracketType] and [Any] type of content
 * into a [List] of [BracketWithContent]
 * @param brackets data to convert
 */
internal fun bracketListOf(brackets: Array<Pair<BracketType, Any>>)
        : List<BracketWithContent> = brackets.map { BracketWithContent(bracket = it.first, content = it.second) }

/**
 * Concatenate a [List] of [Any] type of data into a [String] and return it in [Pair] with the expected [String]
 * @param input data to concatenate in Lileto language format
 * @param expected expected output of Lileto in plain text
 */
internal fun buildTestCaseEntry(input: List<Any>,
                                expected: String)
        : Pair<String, String> {

    return Pair(input.joinToString(separator = ""), expected)
}

internal fun descriptionListSizeAssertion(description: List<String>, size: Int = 2) =
    assert(description.size == size)
    { "Description must contain the first and last part of the description sentence." }

internal fun trimWhitespaces(string: String, shouldTrim: Boolean = true)
        = if (shouldTrim) string.trim() else string

/* General test cases */

internal fun singleBracketInPlaintext(bracket: BracketWithContent,
                                      expectedContent: String)
        : Pair<String, String> {

    return multipleBracketsInPlaintext(
        brackets = listOf(bracket),
        expectedContents = listOf(expectedContent),
        description = listOf("Inserting bracket", "into plain text."))
    }

internal fun singleBracketInPlaintext_trimWhitespaces(bracket: BracketWithContent,
                                                      expectedContent: String)
        : Pair<String, String>  {

    return multipleBracketsInPlaintext(
        brackets = listOf(bracket),
        expectedContents = listOf(expectedContent),
        description = listOf(
            "Trim unwanted whitespaces     \t\t\n \t\t\n\n  \n\t\t\t",
            "   \t\t \n  \t\t \t\n \t\t\t \t\t before and after brackets.")
    )
}

internal fun multipleBracketsInPlaintext(brackets: List<BracketWithContent>,
                                         expectedContents: List<String>,
                                         description: List<String> = listOf("Insert multiple brackets", "into plain text."),
                                         trimWhitespaces: Boolean = true)
        : Pair<String, String>  {

    descriptionListSizeAssertion(description, 2)

    val strings = mutableListOf<Any>()
    val expectedStrings = mutableListOf<Any>()
    strings.add(description[0])
    expectedStrings.add(trimWhitespaces(description[0], trimWhitespaces))

    for (i in brackets.indices) {
        strings.add(" [")
        strings.add(brackets[i].open())
        strings.add(brackets[i].content)
        strings.add(brackets[i].close())
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
        : Pair<String, String> {

    descriptionListSizeAssertion(description, 2)

    return buildTestCaseEntry(
        listOf(description[0],
            " [${outerBracket.open()}",
            outerBracket.content,
            innerBracket.open(), innerBracket.content, innerBracket.close(),
            "${outerBracket.close()}] ",
            description[1]),

        listOf(description[0],
            // NOTE: this is the expected content of the outer bracket
            " [$expectedContent] ",
            description[1]).joinToString(separator = "")
    )
}
