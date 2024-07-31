package mcom

object BasicUtility {
}

@Suppress("UNUSED_PARAMETER")
inline infix fun <reified A, reified B> A.isOf(other: B): Boolean {
    return this is B
}

@Suppress("UNUSED_PARAMETER")
inline infix fun <reified A, reified B> A.notOf(other: B): Boolean {
    return this !is B
}