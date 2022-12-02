import spock.lang.Specification

class aoc2022_02_Test extends Specification {

    def v( Tuple t ) {
        def s = t.toString()
        def myValue = ['X': 1, 'Y': 2, 'Z': 3]
        def cv = myValue[t[1]]

        //println "$s; $c; $cv"
        switch( s ) {
            case "[A, Y]": return cv + 6; // rock paper
            case "[B, Z]": return cv + 6; // paper scissors
            case "[C, X]": return cv + 6; // scissors rock
            case "[A, X]": return cv + 3;
            case "[B, Y]": return cv + 3;
            case "[C, Z]": return cv + 3;
            default: return cv;
        }
    }

    // pick a play based on the opponent play and the "strategy"
    def strategyPick( elfPlay, strategy ) {
        def win =  ['A':'Y', 'B':'Z', 'C':'X']
        def lose = ['A':'Z', 'B':'X', 'C':'Y']
        def draw = ['A':'X', 'B':'Y', 'C':'Z']

        if( strategy == 'X' ) return lose[ elfPlay ]
        if( strategy == 'Y' ) return draw[ elfPlay ]
        if( strategy == 'Z' ) return win[ elfPlay ]
    }


    def "day 2 a"() {
        given:
            def data = this.getClass().getResource( "data-02.txt" ).text
                    .split( '\n' ).collect { s -> new Tuple( s[0], s[2] ) }
        when:
            def score = data.sum( d -> v( d ) )
        then:
            score == 12586
    }


    def "day 2 b"() {
        given:
            def data = this.getClass().getResource( "data-02.txt" ).text.split('\n')
                    .collect{ s -> new Tuple( s[0], strategyPick(s[0], s[2]) ) }

        when:
            def score = data.sum( d -> v( d ) )

        then:
            score == 13193
    }

}
