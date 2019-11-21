package boti996.lileto.tests.helpers

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
        : List<BracketWithContent> = brackets.map {
    BracketWithContent(
        bracket = it.first,
        content = it.second
    )
}

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
