import spock.lang.Specification

class aoc2022_10_Test extends Specification {
    def opToCycles = ["noop":1, "addx":2]

    /** Extend the long operations with nops so that all are executed in 1 cycle. */
    def fixCode( List<String> code ) {
        def result = []
        code.each { s ->
            if( s == "noop" ) result << "noop"
            else {
                def v = s.split(' ')
                assert v[0] == "addx"
                def t = opToCycles[ v[0] ]
                (t-1).times { result << "noop" }
                result << s
            }
        }
        return result
    }

    /** Calculate an X value for each cycle. Return one value per cycle. */
    List<Integer> run( List<String> code ) {
        def cycleSignalValues = [] as List<Integer>
        def fixedCode = fixCode( code )

        def x = 1
        for( String s in fixedCode ) {
            cycleSignalValues << x
            if( s == "noop" ) continue
            else {
                def v = s.split(' ')
                assert v[0] == "addx"
                x += Integer.parseInt( v[1] )
            }
        }
        cycleSignalValues << x
        return cycleSignalValues
    }

    def "day 10 a"() {
        given:
            def data = this.getClass()
                    .getResource( "data-10.txt" )
                    .readLines()
                    .collect { s ->todata( s ) }
            assert data.size() == 139

            def test_data_1 = ["noop", "addx 3", "addx -5"]

            def test_data_2 = this.getClass()
                    .getResource( "data-10-test-2.txt" )
                    .readLines()
                    .collect { s ->todata( s ) }
            assert data.size() == 139

        when:
            def test1 = run( test_data_1 )
            def test2 = run( test_data_2 )
            def part1 = run( data )
            def v = part1[19]*20 + part1[59]*60 + part1[99]*100 + part1[139]*140 + part1[179]*180 + part1[219]*220

        then:
            test1 == [1,1,1,4,4,-1]
            test2[19] == 21
            test2[59] == 19
            test2[99] == 18
            test2[139] == 21
            test2[179] == 16
            test2[219] == 18
            v == 11960
    }

    def todata( String s ) {
        return s
    }


    def "day 10 b"() {
        given:
            def data = this.getClass()
                    .getResource( "data-10.txt" )
                    .readLines()
                    .collect { s ->todata( s ) }
            assert data.size() == 139

            def test_data_2 = this.getClass()
                    .getResource( "data-10-test-2.txt" )
                    .readLines()
                    .collect { s ->todata( s ) }

        when:
            def test2 = run( test_data_2 )
            draw( test2 )

            def part1 = run( data )
            draw( part1 ) // Spells: "EJCFPGLH"

        then:
            true
    }

    def draw( List<Integer> data ) {
        for( int row=0; row < 6; row++ ) {
            StringBuilder s = new StringBuilder(50)
            for( int col=0; col < 40; col++ ) {
                int x = data[row*40 + col]
                if( col==x-1 || col==x || col==x+1 ) s.append( "#" )
                else s.append(".")
            }
            println( s.toString(  ) )
        }
    }
}
