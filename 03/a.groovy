class D {
    String first, second, third
    D(String x, String y, String z) {
        this.first = x
        this.second = y
        this.third = z
    }
}

def commonChar( String a, String b ) {
    def c = a.find{ x ->
        b.contains(x)
    }

    if( !c ) {
        throw new RuntimeException("$a, $b : $c")
    }

    return c
}

def commonChar( String a, String b, String c ) {
    def x = a.find{
        b.contains(it) && c.contains(it)
    }

    if( !x ) {
        throw new RuntimeException("$a, $b , $c : $x")
    }

    return x
}

def value( String c ) {
    if( c.size(  ) == 0 ) throw new IllegalArgumentException("$c is too short, expected 1 char")
    if( c.size(  ) > 1 ) throw new IllegalArgumentException("$c is too long, expected 1 char")
    def chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    return chars.indexOf( c ) + 1
}

def data = ("data-03.txt" as File).text.split( '\n' ).collect { s ->
    int n = s.size(  ) / 2
    new D( s.substring( 0, n ), s.substring( n, n*2 ), '' )
}

data.each { d ->
    d.third = commonChar( d.first, d.second )
}

def score = data.sum { d -> value(d.third) }
println( "Day 3 part 1: $score" )


// --------------- part 2 ----------------
def strings = ( "data-03.txt" as File ).text.split( '\n' )
def data2 = strings.collate(3)

int sum = 0
data2.each { list ->
    def c = ''
    c = commonChar( list[0], list[1], list[2] )
    if( c == null || c.class == null ) {
        println "crap"
    }
    sum += value( c.toString(  ) )
}

println( "Day 3 part 2: $sum")

assert commonChar( "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "ttgJtRGJQctTZtZT", "CrZsJsPPZsGzwwsLwLmpwMDw" ) == "Z"
assert commonChar( "axxxx", "yyayy", "yyya" ) == "a"
assert value("a") == 1
assert value("z") == 26
assert value("A") == 27
assert value("Z") == 52
assert sum == 2499
