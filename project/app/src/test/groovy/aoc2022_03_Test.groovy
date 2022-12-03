import spock.lang.Specification

class aoc2022_03_Test extends Specification {

    class D {
        String first, second, third
        D(String x, String y, String z) {
            this.first = x
            this.second = y
            this.third = z
        }
    }

    def commonChar( String a, String b ) {
        def c = a.find{ x ->
            b.contains(x)
        }

        if( !c ) {
            throw new RuntimeException("$a, $b : $c")
        }

        return c
    }

    def commonChar( String a, String b, String c ) {
        def x = a.find{
            b.contains(it) && c.contains(it)
        }

        if( !x ) {
            throw new RuntimeException("$a, $b , $c : $x")
        }

        return x
    }

    def value( String c ) {
        if( c.size(  ) == 0 ) throw new IllegalArgumentException("$c is too short, expected 1 char")
        if( c.size(  ) > 1 ) throw new IllegalArgumentException("$c is too long, expected 1 char")
        def chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        return chars.indexOf( c ) + 1
    }

    def "day 3 a"() {
        given:
            def data = this.getClass().getResource( "data-03.txt" ).text.split( '\n' ).collect { s ->
                int n = s.size(  ) / 2
                new D( s.substring( 0, n ), s.substring( n, n*2 ), '' )
            }
            data.each { d ->
                def c = ''
                c = commonChar( d.first, d.second )
                if( c == null || c.class == null ) {
                    println "crap"
                }
                println( "${d.first} | ${d.second} : $c, ${c.class.name}, ${d.third.class.name}")
                d.third = c.toString(  )
            }

        when:
            def score = data.sum { d -> value(d.third) }
            println(score)

        then:
            commonChar( "vJrwpWtwJgWr", "hcsFMMfFFhFp" ) == "p"
            commonChar(
                    "hMhhDBwMhDDfCRRBjFDD",
                    "TTWjdWmrmdWqjlmmmjJz" ) == 'j'
            value("a") == 1
            value("z") == 26
            value("A") == 27
            value("Z") == 52
        and:
            score == 7793
    }


    def "day 3 b"() {
        when:
            def strings = this.getClass().getResource( "data-03.txt" ).text.split( '\n' )
            def data = strings.collate(3)

            int sum = 0
            data.each { list ->
                def c = ''
                c = commonChar( list[0], list[1], list[2] )
                if( c == null || c.class == null ) {
                    println "crap"
                }
                sum += value( c.toString(  ) )
            }

            println(sum)

        then:
            commonChar( "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn", "ttgJtRGJQctTZtZT", "CrZsJsPPZsGzwwsLwLmpwMDw" ) == "Z"
            commonChar( "axxxx", "yyayy", "yyya" ) == "a"
            value("a") == 1
            value("z") == 26
            value("A") == 27
            value("Z") == 52
        and:
            sum == 2499
    }

}
