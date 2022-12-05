import spock.lang.Specification

class aoc2022_05_Test extends Specification {

    class Move {
        int N, fromStack, toStack

        Move(int n, int fromstack, int tostack ) {
            N = n
            fromStack = fromstack
            toStack = tostack
        }

        String toString() { "$N: ${fromStack} -> ${toStack}" }
    }

    def makeMovePart2( List<List<String>> stacks, Move m ) {
        def tmp = []
        // Make the same kind of moves to a temporary stack, then from there to the actual target.
        m.N.times { i ->
            tmp.push( stacks[ m.fromStack - 1].pop() )
        }
        m.N.times { i ->
            stacks[m.toStack - 1].push( tmp.pop() )
        }
        return stacks
    }

    def makeMove( List<List<String>> stacks, Move m ) {
        m.N.times { i ->
            stacks[m.toStack - 1].push( stacks[ m.fromStack - 1].pop() )
        }
        return stacks
    }

    def getMyStacks() {
        return [
                'BVWTQNHD'.collect(),
                'BWD'.collect(),
                'CJWQST'.collect(),
                'PTZNRJF'.collect(),
                'TSMJVPG'.collect(),
                'NTFWB'.collect(),
                'NVHFQDLB'.collect(),
                'RFPH'.collect(),
                'HPNLBMSZ'.collect(),
        ]
    }

    def "day 5 a"() {
        given:
            def stacks1 = getMyStacks()
            def stacks2 = getMyStacks()
            println("My starting stacks: ${stacks1.join('\n')}")

            def data = this.getClass().getResource( "data-05.txt" ).text.split('\n')
            def pattern = ~/move (\d+) from (\d+) to (\d+)/
            def filteredData = data.findAll { String s -> s.startsWith("move ") }
            def instructions = filteredData.collect { String s ->
                def matcher = pattern.matcher( s )
                if( matcher.matches(  ) ) {
                    return new Move( matcher[0][1] as int, matcher[0][2] as int, matcher[0][3] as int)
                }
            }

        when:

            instructions.each { Move m ->
                stacks1 = makeMove( stacks1, m )
                stacks2 = makeMovePart2( stacks2, m )
            }
            def result1 = stacks1.collect { stack -> stack[0]}.join()
            def result2 = stacks2.collect { stack -> stack[0]}.join()
            println("part 1: $result1")
            println("part 2: $result2")

        then:
            result1
    }


}
