String data = ( "data06.txt" as File ).text

def allDifferentAsSet( String s ) {
    int i = s.size()
    return i == (s.toSet()).size()
}

def findFirstDifferent( String s, int n ) {
    for( int i = 0; i < s.size() - n + 1; i++ ) {
        def sub = s.substring( i, i + n )
        if( allDifferentAsSet( sub ) ) return i + n
    }
    return -1
}

def result = findFirstDifferent( data, 4 )
println( "result 1: $result" )

def result2 = findFirstDifferent( data, 14 )
println( "result 2: $result2" )
