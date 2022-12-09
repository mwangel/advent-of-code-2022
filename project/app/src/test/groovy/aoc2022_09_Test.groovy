import spock.lang.Specification

class aoc2022_09_Test extends Specification {
    def addToVisited( Punkt t, Set<Punkt> visited ) {
        visited.add( new Punkt( x: t.x, y: t.y ) )
    }


    def makeMove2( Punkt h, Punkt t, Tuple2<String, Integer> move, Set<Punkt> visited, boolean follow=false, boolean shouldRecord=true ) {
        move.v2.times {
            if( !follow ) {
                // Actually execute the move for h
                switch( move.v1 ) {
                    case 'R': h.x++; break
                    case 'L': h.x--; break
                    case 'D': h.y--; break
                    case 'U': h.y++; break
                    default: throw new Exception( 'apa!' )
                }
            }

            while( true ) {
                def dx = h.x - t.x
                def dy = h.y - t.y
                def dxlen = Math.abs( dx )
                def dylen = Math.abs( dy )

                if( (dxlen >= 2) || (dylen >= 2) ) {
                    t.x = t.x + Integer.signum( dx )
                    t.y = t.y + Integer.signum( dy )
                    if( shouldRecord ) addToVisited( t, visited )
                }

                if( Math.abs(h.x - t.x) < 2 && Math.abs(h.y - t.y) < 2 ) {
                    // println( "break!" )
                    break
                }
            }
        }
        //if( shouldRecord ) println( "$move - H:$h  T:$t  - $visited" )
        return( t )
    }


    def run3( List<Tuple2<String, Integer>> moves ) {
        def visited = [] as Set<Punkt>
        List<Punkt> knutar = []
        10.times {knutar << new Punkt( x: 0, y: 0 ) }
        addToVisited( knutar.last(), visited )

        moves.each { move ->
            move.v2.times { i ->
                // Move the Head one step.
                moveMe( move, knutar[0] )

                // The other knots on the rope follow, but only one step at a time..
                boolean somethingMoved = true
                while( somethingMoved ) {
                    //..so loop until there is no more movement.
                    somethingMoved = false
                    9.times { k ->
                        somethingMoved |= follow( knutar[k], knutar[k + 1] )
                    }
                    addToVisited( knutar[9], visited )
                }
            }
            //println( "== $move ==  $knutar")
        }
        return visited.size()
    }


    def moveMe( def move, Punkt p ) {
        switch( move.v1 ) {
            case 'R': p.x++; break
            case 'L': p.x--; break
            case 'D': p.y--; break
            case 'U': p.y++; break
            default: throw new Exception( 'apa!' )
        }
        return p
    }

    def follow( Punkt leader, Punkt follower ) {
        def dx = leader.x - follower.x
        def dy = leader.y - follower.y
        def dxlen = Math.abs( dx )
        def dylen = Math.abs( dy )

        if( (dxlen >= 2) || (dylen >= 2) ) {
            follower.x += Integer.signum( dx )
            follower.y += Integer.signum( dy )
            return true
        }

        return false
    }


    def show( Set<Punkt> visits ) {
        return
        def startX = visits.min { it.x }.x
        def startY = visits.min { it.y }.y
        def endX = visits.max { it.x }.x
        def endY = visits.max { it.y }.y

        println( "$startX,$startY -> $endX,$endY for $visits " )

        for( int yy = startY; yy <= endY; yy++ ) {
            def s = new StringBuilder( (endX - startX) * 2 )
            for( int xx = startX; xx <= endX; xx++ ) {
                def v = visits.find { (it.x == xx) && (it.y == yy) }
                if( v != null ) {
                    s.append( "#" )
                } else {
                    s.append( "." )
                }
            }
            println( s.toString() )
        }
        println()
    }

    class Punkt {
        int x, y

        int hashCode() { "${x};${y}".hashCode() }

        String toString() { "$x;$y" }

        boolean equals( Object o ) {
            this.toString() == o.toString()
        }
    }

    def runit( List<Tuple2<String, Integer>> moves ) {
        def visited = [] as Set<Punkt>
        Punkt head = new Punkt( x: 0, y: 0 )
        Punkt tail = new Punkt( x: 0, y: 0 )
        addToVisited( tail, visited )

        moves.each { move ->
            makeMove2( head, tail, move, visited )
        }

        return visited.size()
    }



    def "There are no hash collisions in Punkt."() {
        when:
            Set<Integer> hashes = []
            for( int y=-15; y <= 162; y++ ) {
                for( int x = -101; x <= 28; x++ ) {
                    hashes.add( new Punkt(x:x, y:y).hashCode() )
                }
            }

        then:
            hashes.size() == (15+162+1)*(101+28+1) // plus 1 for zero
    }

    def "day 9 a"() {
        given:
            def data = this.getClass()
                    .getResource( "data-09.txt" )
                    .readLines()
                    .collect { s ->todata( s ) }
            assert data.size() == 2000

            def test_example = ["R 4", "U 4", "L 3", "D 1", "R 4", "D 1", "L 5", "R 2"].collect { s -> todata( s ) }
            def test_right_left = ["R 4", "L 4"].collect { s -> todata( s ) }
            def test_right_left_right = ["R 4", "L 4", "R 4"].collect { s -> todata( s ) }
            def test_1_lap = ["R 4", "U 4", "L 4", "D 4"].collect { s -> todata( s ) }
            def test_2_laps = ["R 4", "U 4", "L 4", "D 4", "R 4", "U 4", "L 4", "D 4"].collect { s -> todata( s ) }
            def test_1_lap_right_turns = ["U 4", "R 4", "D 4", "L 4"].collect { s -> todata( s ) }
            def test_cross = ["R 1", "U 1", "R 1", "U 1", "R 1", "U 1", "R 1", "U 1", "L 4", "D 1", "R 1", "D 1", "R 1", "D 1", "R 1", "D 1", "R 1"].collect { s -> todata( s ) }

        when:
            def part1 = runit( data )

            println( "part1 is $part1" )

        then:
            runit( test_example ) == 13
            runit( test_right_left ) == 4
            runit( test_right_left_right ) == 4
            runit( test_1_lap ) == 13
            runit( test_2_laps ) == 13
            runit( test_1_lap_right_turns ) == 13
            runit( test_cross ) == 8
            part1 == 6236
    }

    def todata( String s ) {
        def v = s.split(' ')
        new Tuple2<String, Integer>( v[0], v[1] as int )
    }

    def "day 9 b"() {
        when:
            def data = this.getClass()
                    .getResource( "data-09.txt" )
                    .readLines()
                    .collect { s -> todata( s )}
            assert data.size() == 2000

            def test_example = ["R 4", "U 4", "L 3", "D 1", "R 4", "D 1", "L 5", "R 2"].collect { s -> todata(s) }
            def test_larger_example = ["R 5", "U 8", "L 8", "D 3", "R 17", "D 10", "L 25", "U 20"].collect {s -> todata(s) }

        then:
            // 2439 is too low!
            def part2 = run3( data )
            println( "part2 is $part2" )

        then:
            run3( test_example ) == 1
            run3( test_larger_example ) == 36
            part2 == 2449
    }


}
