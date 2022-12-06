String data = ( "data06.txt" as File ).text

/** True if all characters in s are different. */
def allDifferentAsSet( String s ) {
    int i = s.size()
    return i == (s.toSet()).size()
}

/** Find an index in s where there are n different characters. */
def findFirstDifferent( String s, int n ) {
    for( int i = 0; i < s.size() - n + 1; i++ ) {
        def sub = s.substring( i, i + n )
        if( allDifferentAsSet( sub ) ) return i + n
    }

    // A return value of -1 means that there is no place in s
    //  where all characters are different.
    return -1.
}

def result = findFirstDifferent( data, 4 )
println( "result 1: $result" )

def result2 = findFirstDifferent( data, 14 )
println( "result 2: $result2" )
