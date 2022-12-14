import spock.lang.Specification

class aoc2022_14_Test extends Specification {
    def log( String s ) {
        println s
    }

    enum FlowResult { MOVED, STOPPED, DRAINED }

    class World {
        def minx = Integer.MAX_VALUE
        def miny = 0
        def maxx = Integer.MIN_VALUE
        def maxy = Integer.MIN_VALUE

        def rock = [] as Set<String>
        def sand = [] as Set<String>
        def whatever = [] as Set<String>

        def key( def x, def y ) { "$x;$y" }

        def isFree( def x, def y ) { !isOccupied( x, y ) }

        def isOccupied( def x, def y ) { whatever.contains( key(x,y) ) }
        def isRock( def x, def y ) { rock.contains( key(x,y) ) }
        def isSand( def x, def y ) { sand.contains( key(x,y) ) }

        def setRock( def x, def y ) {
            maxy = Math.max( maxy, y )

            def k = key(x,y)
            rock << k
            setOccupied( x, y )
        }
        def setSand( def x, def y ) {
            def k = key(x,y)
            sand << k
            setOccupied( x, y )
        }
        def setOccupied( def x, def y ) {
            maxx = Math.max( maxx, x )
            minx = Math.min( minx, x )

            def k = key(x,y)
            whatever << k
        }

        def clear( def x, def y ) {
            def k = key( x, y )
            rock.remove( k )
            sand.remove( k )
            whatever.remove( k )
        }

        def draw() {
            def sb = new StringBuilder( 2000 )
            sb.append( "($minx,$miny - $maxx,$maxy)\n" )
            (miny..(maxy+1)).each { y ->
                (minx..maxx).each { x ->
                    if( isRock( x, y ) )
                        sb.append( "#" )
                    else if( isSand( x, y ) )
                        sb.append( "o" )
                    else
                        sb.append( "." )
                }
                sb.append( "\n" )
            }
            println( sb.toString(  ) )
        }
    }

    def flowSandFrom( def x, def y, World world ) {
        // out of this world?
        if( y >= world.maxy ) {
            world.clear( x, y )
            return [0, 0, FlowResult.DRAINED]
        }

        // down, left+down, right+down
        if( world.isFree( x, y+1 ) ) {
            world.clear( x, y )
            world.setSand( x, y+1 )
            return [x, y+1, FlowResult.MOVED]
        }
        else if( world.isFree( x-1, y+1 ) ) {
            world.clear( x, y )
            world.setSand( x-1, y+1 )
            return [x-1, y+1, FlowResult.MOVED]
        }
        else if( world.isFree( x+1, y+1 ) ) {
            world.clear( x, y )
            world.setSand( x+1, y+1 )
            return [x+1, y+1, FlowResult.MOVED]
        }

        return [x, y, FlowResult.STOPPED]
    }

    def "day 14 a"() {
        given:
            def filetext = this.getClass()
                    .getResource( "data-14.txt" )
//                    .getResource( "data-14-example.txt" )
                    .readLines()
            def data = todata( filetext )

        when:
            data.draw(  )
            def result = 0

            while( true ) {
                def fr = flowSandFrom( 500, 0, data )
                while( fr[2] == FlowResult.MOVED ) {
                    fr = flowSandFrom( fr[0], fr[1], data )
                }
                //data.draw()
                if( fr[2] == FlowResult.DRAINED ) {
                    println( "A grain of sand disappeared from the world.." )
                    break
                }
            }
            data.draw()

            println "part 1 result: ${data.sand.size()}"

        then:
            result == 672
    }



    def flowSandFrom_part2( def x, def y, World world ) {
        // at the bottom of the world?
        if( y == world.maxy + 1 ) {
            world.setSand( x, y )
            return [x, y, FlowResult.STOPPED]
        }

        // down, left+down, right+down
        if( world.isFree( x, y+1 ) ) {
            world.clear( x, y )
            world.setSand( x, y+1 )
            return [x, y+1, FlowResult.MOVED]
        }
        else if( world.isFree( x-1, y+1 ) ) {
            world.clear( x, y )
            world.setSand( x-1, y+1 )
            return [x-1, y+1, FlowResult.MOVED]
        }
        else if( world.isFree( x+1, y+1 ) ) {
            world.clear( x, y )
            world.setSand( x+1, y+1 )
            return [x+1, y+1, FlowResult.MOVED]
        }

        world.setSand( x, y )
        return [x, y, FlowResult.STOPPED]
    }

    def "day 14 b"() {
        given:
            def filetext = this.getClass()
                    .getResource( "data-14.txt" )
//                    .getResource( "data-14-example.txt" )
                    .readLines()
            def data = todata( filetext )

        when:
            data.draw(  )
            def result = 0

            while( true ) {
                def fr = flowSandFrom_part2( 500, 0, data )
                while( fr[2] == FlowResult.MOVED ) {
                    fr = flowSandFrom_part2( fr[0], fr[1], data )
                }
                if( fr[0] == 500 && fr[1] == 0 && fr[2] == FlowResult.STOPPED ) {
                    println( "The world is now full of sand." )
                    break
                }
            }
            data.draw()

            println "part 2 result: ${data.sand.size()}"

        then:
            result == 672
    }



    /** Parse the input */
    def todata( List<String> list ) {
        def result = new World()

        list.each { str ->
            def endpoints = str
                    .split(' -> ')
                    .collect {it.split(",") }
                    .collect { xy -> new Tuple2<>( Integer.parseInt( xy[0] ), Integer.parseInt( xy[1] )) }

            for( int i=0; i < endpoints.size() - 1; i++ ) {
                def (fromx, fromy) = endpoints[i]
                def (tox, toy) = endpoints[i + 1]
                def dx = (int) Math.signum(tox-fromx)
                def dy = (int) Math.signum(toy-fromy)
                while( (fromx != tox) || (fromy != toy) ) {
                    result.setRock( fromx, fromy )
                    fromx += dx
                    fromy += dy
                }
                result.setRock( fromx, fromy )
            }
        }
        return result
    }

}
