import spock.lang.Specification

class aoc2022_12_Test extends Specification {

    def "day 12 a"() {
        given:
            def filetext = this.getClass()
                    .getResource( "data-12.txt" )
//                    .getResource( "data-12-example.txt" )
                    .readLines()
            def data = todata( filetext )
            Map<String,XY> xydata = toxydata( filetext )
            def (sx, sy) = find( 'S', data )
            def (ex, ey) = find( 'E', data )
            def ek = XY.makeKey( ex, ey )
            def theEnd = xydata[ ek ]
            def sk = XY.makeKey( sx, sy )
            def theStart = xydata[ sk ]
            theStart.beenHere = true
            int width = filetext[0].size()
            int height = filetext.size()

        when:
            println( "from $sx $sy to $ex $ey" )
            def part1 = go2( theStart, theEnd, xydata, width, height, [theStart] )
        then:
            part1 == 330

        when:
            def asquares = xydata.values(  ).findAll {it.h == (int) 'a' }
            def shortest = Integer.MAX_VALUE
            asquares.each { XY start ->
                // reset the board
                xydata.values().each { XY xy -> xy.parent = null; xy.beenHere = false }
                def len = go2(start, theEnd, xydata, width, height, [start])
                if( len > 0 && len < shortest ) {
                    shortest = len
                    println("* New shortest scenic path: $len")
                }
            }
            println("Shortest scenic path: $shortest")
        then:
            shortest < 330
    }

    def moves = [[x: 1, y: 0], [x: 0, y: 1], [x: -1, y: 0], [x: 0, y: -1]]


    def shortest = 99999
    Map<String, Integer> bestFromXY = [:]

    boolean isPossibleMove( XY from, XY to, Map<String, XY> map, int width, int height ) {
        if( (int) from.dist( to ) > 1 ) return false
        else if( to.x < 0 ) return false
        else if( to.x >= width ) return false
        else if( to.y < 0 ) return false
        else if( to.y >= height ) return false
        else if( from.h - to.h < -1 ) return false
        else if( to.beenHere ) return false
        return true
    }


    class XY {
        int x = -1
        int y = -1
        int h = -1
        boolean beenHere = false
        boolean isEnd = false
        XY parent = null
        String parentKey = null

        String getKey() { makeKey( x, y ) }

        static String makeKey( int x, int y ) { "$x;$y" }

        def dist( XY other ) {
            def dx = x - other.x
            def dy = y - other.y
            return Math.sqrt( (dx * dx + dy * dy) )
        }

        def blockDist( XY other ) {
            def dx = x - other.x
            def dy = y - other.y
            return Math.abs(dx) + Math.abs(dy)
        }

        def hdiff( XY other ) {
            Math.abs( h - other.h )
        }

        String toString() { key }

        int hashCode() { getKey().hashCode() }

        boolean equals( Object other ) { other.class == XY.class && other.x == this.x && other.y == this.y }
    }

    def go2( XY start, XY end, Map<String,XY> grid, int width, int height, List<XY> heads ) {
        while( true ) {
            // Find the place that is the closest to the end.
            XY bestHead = heads.min{ it.blockDist( end ) }
            //show( grid, heads, width, height )
            //println( "Best: $bestHead (parent ${bestHead?.parent})" )
            if( bestHead == null ) {
                //println("Trouble: No way forward. heads=$heads")
                //show( grid, heads, width, height )
                return -1
            }

            for( int i = 0; i < 4; i++ ) {
                // Attempt all legal moves from this place...
                def tx = bestHead.x + moves[i]['x']
                def ty = bestHead.y + moves[i]['y']
                def txy = grid[XY.makeKey( tx, ty )]
                if( txy == null ) continue // outside of the map, left or up

                if( isPossibleMove( bestHead, txy, grid, width, height ) ) {
                    txy.parent = bestHead

                    //...and put them in the list of heads...
                    txy.beenHere = true
                    heads << txy
                    //println( "mark $txy (parent $bestHead)" )

                    if( grid[txy.key].isEnd ) {
                        //println("--------------")
                        int n = 0
                        XY p = grid[txy.key]
                        while( p != start ) {
                            //println( " -- $p")
                            p = p.parent
                            n++
                        }
                        //println( "FOUND IT! Steps: $n" )
                        return n
                    }
                }
            }
            // Finally remove this place from the list of heads.
            heads.remove( bestHead )
        }
    }

    def show( Map<String,XY> grid, List<XY> heads, int width, int height ) {
        StringBuilder s = new StringBuilder( width * height + 500 )
        height.times { y ->
            width.times { x ->
                def k = XY.makeKey( x, y )
                def p = grid[k]
                def b = p.beenHere
                def c = (b && heads.contains(p))
                s.append( b ? (c ? "@" : "*") : "." )
            }
            s.append( '\n' )
        }
        println s
    }

    def go( int sx, int sy, int ex, int ey, List<String> data, Set<String> visited, List<String> steps ) {
        steps.add( key( sx, sy ) )
        def pad = ' ' * steps.size()
        //println(pad + "Stepped to $sx, $sy")

        if( sx == ex && sy == ey ) {
            println( "FOUND A PATH: ${visited.size()}. Steps: $steps. Visited: $visited" )
            if( visited.size() < shortest ) shortest = visited.size()
            steps.clear()
            return
        }
        visited.add( key( sx, sy ) )

        int maxx = data[0].size() - 1
        int maxy = data.size() - 1

        for( int i = 0; i < 4; i++ ) {
            // Pick the next move to try.
            def tx = sx + moves[i]['x']
            def ty = sy + moves[i]['y']
            //println(pad+" considering [$tx, $ty]")
            if( tx < 0 || tx > maxx || ty < 0 || ty > maxy ) {
                continue
            }

            def a = data[sy][sx]
            def b = data[ty][tx]
            def dh = Math.abs( (int) a - (int) b )
            if( a == 'S' ) dh = Math.abs( (int) 'a' - (int) b ) // Start is on height 'a'
            if( b == 'E' ) dh = Math.abs( (int) 'z' - (int) a ) // End is on height 'z'

            def beenThere = visited.contains( key( tx, ty ) )

            if( b == 'E' && dh > -2 && dh < 2 ) println( "ÄÄÄÄ $sx,$sy $tx,$ty $beenThere $dh: ${steps.join( ", " )}" )

            if( (!beenThere) && (dh <= 1) && (steps.size() < shortest) ) {
                // The move is allowed so make it.
                go( tx, ty, ex, ey, data, visited, steps )
            } else {
                //println"      skip $tx, $ty (dh=$dh, beenThere=$beenThere)"
            }
        }

        // finally forget this path
//        println"Leaving    $sx, $sy"
        visited.remove( key( sx, sy ) )
        steps.remove( key( sx, sy ) )
    }

    //def show(List<String> data, int x, int y)

    String key( int x, int y ) { "$x;$y" }

    def find( c, data ) {
        for( int r = 0; r < data.size(); r++ ) {
            def k = data[r].indexOf( c )
            if( k > -1 ) return [k, r]
        }
        throw new Exception( "$c is not in the data" )
    }

    def todata( List<String> list ) {
        return list
    }

    def toxydata( List<String> list ) {
        def data = new HashMap<String, XY>()
        list.eachWithIndex { String row, int r ->
            row.eachWithIndex { String h, int c ->
                boolean isEnd = h == "E"
                boolean isStart = h == "S"
                if( h == "S" ) h = "a"
                if( h == "E" ) h = "z"
                data[ XY.makeKey( c, r )] = new XY( x: c, y: r, h: (int) h, beenHere: false, isEnd: isEnd )
            }
        }
        return data
    }

}
