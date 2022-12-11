import spock.lang.Specification

class aoc2022_11_Test extends Specification {


    class Monkey {
        List<BigInteger> items = []
        Closure op = { BigInteger v -> throw new Exception( "op not initialized" ) }
        long testDivisor = 0
        int trueTarget = -1
        int falseTarget = -1
        long checksMade = 0

        String toString() {
            "items:$items/div:$testDivisor/true:$trueTarget/false:$falseTarget"
        }
    }

    /** Calculate an X value for each cycle. Return one value per cycle. */
    def run( List<Monkey> monkeys, boolean shouldDivide = true ) {
        def log = { s ->
            println(s)
        }

        monkeys.eachWithIndex { m, monkeyIndex ->
//            log("Monkey $monkeyIndex")
            m.items.eachWithIndex { itemValue, index ->
                m.checksMade++
                log("  [$monkeyIndex] Monkey $monkeyIndex inspects an item with a worry level of $itemValue.")
                BigInteger newValue = m.op( itemValue )
                //log("    [$monkeyIndex] Worry level is altered from $itemValue to $newValue.")

                BigInteger dividedValue = shouldDivide ? Math.floor( newValue / 3 ) : newValue
                boolean isDivisible = dividedValue.mod( m.testDivisor ) == 0
//                log("    [$monkeyIndex] Monkey gets bored with item. Worry level is divided by 3 to $dividedValue.")
//                log("    [$monkeyIndex] Current worry level is ${isDivisible ? '' : 'NOT '}divisible by ${m.testDivisor}.")

                int targetMonkey = isDivisible ? m.trueTarget : m.falseTarget
//                log("    [$monkeyIndex] Item with worry level $dividedValue is thrown to monkey $targetMonkey.")
                log("    [$monkeyIndex] Item -> $targetMonkey (level $dividedValue).")
                monkeys[targetMonkey].items.add( dividedValue )
            }
            m.items.clear()
        }
//        log( monkeys.collect { it.toString() }.join( "\n" ) )

        return monkeys
    }

    def "day 11 a"() {
        given:
            def filetext = this.getClass()
                    .getResource( "data-11.txt" )
                    .readLines()
            assert filetext.size() == 55
            def data = todata( filetext )

            def example_file_text = this.getClass()
                    .getResource( "data-11-example.txt" )
                    .readLines()
            assert example_file_text.size() == 27
            def testdata = todata( example_file_text )

        when:
            20.times {
                testdata = run(testdata)
            }
            def checks1 = testdata.collect { it.checksMade }.sort().reverse()
            def test1 = checks1[0] * checks1[1]
            println( "test1 is $test1")

            20.times {
                data = run(data)
            }
            def checks = data.collect { it.checksMade }.sort().reverse()
            def part1 = checks[0] * checks[1]
            println( "part1 is $part1")

        then:
            test1 == 10605
            part1 == 182293
    }



    def "day 11 b"() {
        given:
            def filetext = this.getClass()
                    .getResource( "data-11.txt" )
                    .readLines()
            assert filetext.size() == 55
            def data = todata( filetext )

            def example_file_text = this.getClass()
                    .getResource( "data-11-example.txt" )
                    .readLines()
            assert example_file_text.size() == 27
            def testdata = todata( example_file_text )

        /*

         */

        when:
            20.times {
                int t = it + 1
                testdata = run(testdata, false)
                if( t==1 || t==20 || t % 1000 == 0 ) {
                    println( "== After round $t ==" )
                    testdata.eachWithIndex { Monkey m, int monkeyIndex ->
                        println( "Monkey $monkeyIndex inspected items ${testdata[monkeyIndex].checksMade} times." )
                    }
                    println()
                }
            }
            def checks1 = testdata.collect { it.checksMade }.sort().reverse()
            def test1 = checks1[0] * checks1[1]
            println( "test1 is $test1")
//
//            20.times {
//                data = run(data)
//            }
//            def checks = data.collect { it.checksMade }.sort().reverse()
//            def part1 = checks[0] * checks[1]
//            println( "part1 is $part1")

        then:
            test1 == 10605
//            part1 == 182293
    }



    Closure<BigInteger> toOp( String s ) {
        def p = ~/  Operation: new = (\S+) (\S) (\S+)/
        def matcher = p.matcher( s )
        if( !matcher.matches() ) throw new Exception( "Should be an operation: $s" )
        def first = matcher[0][1]
        if( first != "old" ) throw new Exception( "Should be 'old': $first" )
        def second = matcher[0][3]
        def operator = matcher[0][2]
        if( operator == "+" ) {
            if( second == "old" )
                return { BigInteger old -> old + old }
            else {
                long c = new BigInteger(second)
                return { BigInteger old -> old + c }
            }
        } else if( operator == "*" ) {
            if( second == "old" )
                return { BigInteger old -> old * 1 }
            else {
                BigInteger c = new BigInteger(second)
                return { BigInteger old -> old * c }
            }
        } else throw new Exception( "Unknown operator in: $s" )
    }

    def todata( List<String> list ) {
        List<Monkey> monkeys = []
        int row = 0
        while( row < list.size() ) {
            assert list[row++].startsWith( "Monkey " )
            Monkey m = new Monkey()
            m.items = (list[row++] - "  Starting items: ").split( "," ).collect { Long.parseLong( it.trim() ) }
            m.op = toOp( list[row++] )
            m.testDivisor = Long.parseLong( list[row++] - "  Test: divisible by " )
            m.trueTarget = Integer.parseInt( list[row++] - "    If true: throw to monkey " )
            m.falseTarget = Integer.parseInt( list[row++] - "    If false: throw to monkey " )
            row++
            monkeys << m
        }
        println( "data parsed: $monkeys" )
        return monkeys
    }

}
