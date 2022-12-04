import spock.lang.Specification

class aoc2022_04_Test extends Specification {

    class D {
        int first, second
        D(int x, int y) {
            this.first = x
            this.second = y
        }
        boolean contains(D other) {
            return(first <= other.first && second >= other.second)
        }
        boolean overlaps(D other) {
            return(
                    (first >= other.first && first <= other.second)
                || (second >= other.first && second <= other.second))
        }
        String toString() { "[$first-$second]" }
    }

    def strToRange( String s ) {
        def parts = s.split("-")
        return new D( Integer.parseInt( parts[0] ), Integer.parseInt( parts[1] ))
    }

    def "day 4 a"() {
        given:
            def data = this.getClass().getResource( "data-04.txt" ).text.split( '\n' )

        when:
            def n = data.sum { s ->
                def ranges = s.split(",")
                def a = strToRange( ranges[0] )
                def b = strToRange( ranges[1] )
                println("$a , $b")
                if( a.contains(b) || b.contains( a ) ) return 1
                else return 0
            }
            def x = data.sum { s ->
                def ranges = s.split(",")
                def a = strToRange( ranges[0] )
                def b = strToRange( ranges[1] )
                if( a.overlaps(b) || b.overlaps( a ) ) return 1
                else return 0
            }

            println "contained = $n"
            println "overlapping = $x"

        then:
            new D(1,4).contains( new D(2,3) )
            new D(1,4).contains( new D(2,4) )
            data.size(  ) == 1000
            n > 0
    }


}
