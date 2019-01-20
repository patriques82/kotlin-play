package K1classes

class Outer {
    inner class Inner {
        fun getOuter(): Outer {
            return this@Outer // can get outer instance
        }
    }
    class StaticInner {
        fun cantGetOuter(): Outer {
            return Outer() // cannot get outer instance
        }
    }
}


