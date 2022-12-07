import spock.lang.Specification

class aoc2022_06_Test extends Specification {

    // USING THIS METHOD TAKES 2 * THE TIME OF THE "AsSet" VARIANT!
    def allDifferent( String s ) {
        def allDifferent = true
        s.each { c ->
            if( s.count( c ) > 1 ) allDifferent = false
        }
        return allDifferent
    }

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

    /**
     * THIS IS ACTUALLY (A LITTLE) SLOWER THAN findFirstDifferent WHEN USING "AsSet" COUNTER..
     * BUT ONLY FOR SMALL VALUES FOR n. THIS IS MUCH FASTER FOR n > 5 AND UP
     */
    long siiit( String data, Integer n ) {
        // keep the chars in a queue of length n
        def q = new LinkedList<Character>()
        // keep the count in a map
        def m = new HashMap<Character, Long>( 1000 ).withDefault { 0 }

        for( int i = 0; i < data.size(); i++ ) {
            Character c = data[i] as Character
            q.addLast( c )
            m[c]++

            if( q.size() > n ) {
                def d = q.removeFirst()
                m[d]--
                if( m[d] == 0 ) m.remove( d )
                if( m.size() == n ) {
                    return i + 1
                }
            }
        }
        throw new Exception( "Not found" )
    }


    def "day 6 a"() {
        given:
            String data = this.getClass().getResource( "data-06.txt" ).text

        when:
            def result = findFirstDifferent( data, 4 )
            def result2 = findFirstDifferent( data, 14 )
            def result3 = siiit( data, 4 )
            def result4 = siiit( data, 14 )

            println( "result 1: $result" )
            println( "result 2: $result2" )

            long t = System.currentTimeMillis()
            1000.times { findFirstDifferent( data, 15 ) }
            println( "time 1: ${System.currentTimeMillis() - t}" )

            long t2 = System.currentTimeMillis()
            1000.times { siiit( data, 15 ) }
            println( "time 2: ${System.currentTimeMillis() - t2}" )

        then:
            5 == findFirstDifferent( "bvwbx", 4 )
            5 == findFirstDifferent( "bvwbjplbgvbhsrlpgdmjqwftvncz", 4 )
            6 == findFirstDifferent( "nppdvjthqldpwncqszvftbrmjlhg", 4 )
            10 == findFirstDifferent( "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 4 )
            23 == findFirstDifferent( "bvwbjplbgvbhsrlpgdmjqwftvncz", 14 )
            23 == findFirstDifferent( "nppdvjthqldpwncqszvftbrmjlhg", 14 )
            29 == findFirstDifferent( "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 14 )
            result == 1779
            result2 == 2635
            result3 == 1779
            result4 == 2635
    }


}
