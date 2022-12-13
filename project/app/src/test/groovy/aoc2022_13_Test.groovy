import spock.lang.Specification

class aoc2022_13_Test extends Specification {
    def log( String s ) {
        //println s
    }

    enum ORDER {
        INORDER,
        EQUAL,
        OUTOFORDER
    }

    def xcompare( int a, int b ) {
        log( "  int/int : $a - $b")
        if( a == b ) return ORDER.EQUAL
        if( a < b ) return ORDER.INORDER
        return ORDER.OUTOFORDER
    }

    def xcompare( List list, int i ) {
        log( "  list/int: $list - $i")
        return xcompare( list, [i] )
    }

    def xcompare( int i, List list ) {
        log( "  int/list: $i - $list")
        return xcompare( [i], list )
    }

    def xcompare( List left, List right ) {
        log( " list/list: $left - $right")
        int max = Math.max( left.size(), right.size() )
        for( int i=0; i < max; i++ ) {
            if( i >= left.size() && i >= right.size() ) return ORDER.INORDER // both sides ran out of items
            if( i >= left.size() ) { log("left is out of items (index $i): $left" ); return ORDER.INORDER } // left side ran out of items
            if( i >= right.size() ) { log("right is out of items (index $i): $right" ); return ORDER.OUTOFORDER } // right side ran out of items
            def c = xcompare( left[i], right[i] )
            log( "  (${left[i]}, ${right[i]} -> $c)" )
            if( c == ORDER.OUTOFORDER ) return ORDER.OUTOFORDER // if left side was greater then the sides is out of order
            else if( c == ORDER.INORDER ) return ORDER.INORDER // if left side was smaller then the sides are in order
            else {
                // equal, continue the check..
            }
        }
        return ORDER.EQUAL
    }

    def "day 13 a"() {
        given:
            def filetext = this.getClass()
                    .getResource( "data-13.txt" )
//                    .getResource( "data-13-example.txt" )
                    .readLines()
            def data = todata( filetext )

        when:
            def result = 0
            println("-----------------------")
            data.eachWithIndex { t, pairIndex ->
                if( t.v3 == ORDER.INORDER || t.v3 == ORDER.EQUAL ) {
                    result += (pairIndex + 1)
                    log( "Index $pairIndex: ${t.v3}, sum=$result")
                }
                log( " ${t.v1}\n ${t.v2}\n ${t.v3}")
                log("-----------------------")
            }
            println "inorder      = ${data.count{it.v3 == ORDER.INORDER}}"
            println "equal        = ${data.count{it.v3 == ORDER.EQUAL}}"
            println "out of order = ${data.count{it.v3 == ORDER.OUTOFORDER}}"
            println "part 1 result: $result"
        then:
            result == 5717

    }


    def "day 13 b"() {
        given:
            def filetext = this.getClass()
                    .getResource( "data-13.txt" )  // expect 25935
//                    .getResource( "data-13-example.txt" )  // expect 140
                    .readLines()
            def data = todata2( filetext )
            data << [[2]]
            data << [[6]]

        when:
            data.sort( new Comparator<List>() {
                @Override
                int compare( List o1, List o2 ) {
                    def c = xcompare( o1, o2 )
                    if( c == ORDER.INORDER ) return -1
                    if( c == ORDER.OUTOFORDER ) return 1
                    return 0
                }
            } )
        
            println("----------- SORTED ------------")
            log(data.join('\n'))
            def i0 = data.findIndexOf { it == [[2]] } + 1
            def i1 = data.findIndexOf { it == [[6]] } + 1
            def result = i0 * i1
            println( "Part 2: $i0 * $i1 = $result")

        then:
            result == 25935

    }

    /** Parse the file a list of (pairs of lists) separated by blank lines. */
    def todata( List<String> list ) {
        def result = [] as List<Tuple<List, List, Integer>>
        for( int i=0; i < list.size(  ); i++) {
            def str = list[i]
            if( !str.trim().size() ) continue

            def a = Eval.me( str )
            def b = Eval.me( list[++i])
            assert a.class.name == "java.util.ArrayList"
            assert b.class.name == "java.util.ArrayList"

            def c = xcompare( a, b )

            result << new Tuple3( a, b, c )
        }
        return result
    }

    /** Pick up all the lists into one big list of lists, ignore blank lines. */
    def todata2( List<String> list ) {
        def result = [] as List<List>
        list.findAll{it}.each { str ->
            result.add( Eval.me(str) )
        }
        return result
    }
}
