import spock.lang.Specification

class aoc2022_01_Test extends Specification {

    def splitlist(List list) {
        def result = []
        def i = list.find{ !it }
        while( i ) {
            result << list.take(i)
            list.drop(i)
        }
        return result
    }

    def bob() {
        ArrayList.metaClass.splitter = {
            def result = []
            def i = delegate.findIndexOf { v -> (v?.toString(  ) == "0") || (v?.toString()?.trim(  ) == "") }
            while( true ) {
                if( i == 0 ) {
                    delegate = delegate.drop(1)
                    i = delegate.findIndexOf { v -> (v?.toString(  ) == "0") || (v?.toString()?.trim(  ) == "") }
                    continue
                }
                if( i == -1 ) {
                    if( delegate?.size() ) result << delegate
                    break
                }

                result << delegate.take(i)
                delegate = delegate.drop(i)
                i = delegate.findIndexOf { v -> (v?.toString(  ) == "0") || (v?.toString()?.trim(  ) == "") }
            }
            return result
        }
        expect: [1,2,0,3,0,4,5].splitter() == [[1,2],[3],[4,5]]
    }

    def "day 1"() {
        setup:
            println(". = ${new File('.').absolutePath}")
            def data = this.getClass(  ).getResource( "data-01.txt" ).text.split( '\n' ) // list of strings

            List elfLoads = [] // list of integers
            def load = 0

            data.each { s ->
                if( !s ) {
                    elfLoads << load
                    load = 0
                } else {
                    load += Integer.parseInt( s )
                }
            }

            def result1 = elfLoads.max()
            println( "part 1: ${result1}" )

            def f = elfLoads.sort().reverse()
            def result2 = f[0] + f[1] + f[2]
            println( "part 2: ${result2}" )

        expect:
            result1 == 69836
        and:
            result2 == 207968
    }
}
