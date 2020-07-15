package lifegame.container

data class Project (
        val a: Int,
        val b: Int,
        val c: Int,
        val d: Int,
        val e: Int
) {
    data class Builder(
            var a: Int = -1,
            var b: Int = -1,
            var c: Int = -1,
            var d: Int = -1,
            var e: Int = -1
    ) {

        fun a(a: Int) = apply { this.a = a }
        fun b(b: Int) = apply { this.b = b }
        fun c(c: Int) = apply { this.c = c }
        fun d(d: Int) = apply { this.d = d }
        fun e(e: Int) = apply { this.e = e }
        fun build() = Project(a, b, c, d, e)
    }
}