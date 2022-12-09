import spock.lang.Specification

class aoc2022_08_Test extends Specification {

    def isVisible( int y, int x, int[][] data ) {
        int h = data.size()
        int w = data[0].size()

        if( y==0 || x==0 || y==h-1 || x==w-1 ) return true

        int thisTree = data[y][x]
        boolean fromLeft=true
        boolean fromRight=true
        boolean fromTop=true
        boolean fromBottom=true

        // from the left
        for(int c=0; c < x; c++) {
            if( data[y][c] >= thisTree ) { fromLeft = false; break }
        }

        // from the right
        for(int c=w-1; c > x; c--) {
            if( data[y][c] >= thisTree ) {
                //println("$thisTree at row:$y/col:$x is hidden from the right by ${data[y][c]} at $y;$c")
                fromRight = false; break }
        }

        // from the top
        for(int r=0; r < y; r++) {
            if( data[r][x] >= thisTree ) { fromTop = false; break }
        }

        // from the bottom
        for(int r=h-1; r > y; r--) {
            if( data[r][x] >= thisTree ) { fromBottom = false; break }
        }

        return fromLeft || fromRight || fromTop || fromBottom
    }


    def countSeenTrees( int y, int x, int[][] data ) {
        int h = data.size()
        int w = data[0].size()

        int thisTree = data[y][x]
        def toLeft=0
        def toRight=0
        def upwards=0
        def downwards=0

        // to the right
        for(int c=x+1; c < w; c++) {
            toRight++
            if( data[y][c] < thisTree ) {  }
            else break
        }

        // to the left
        for(int c=x-1; c >= 0; c--) {
            toLeft++
            if( data[y][c] < thisTree ) {}
            else break
        }

        // looking up
        for(int r=y-1; r >= 0; r--) {
            upwards++
            if( data[r][x] < thisTree ) {  }
            else break
        }

        // looking down
        for(int r=y+1; r < h; r++) {
            downwards++
            if( data[r][x] < thisTree ) {  }
            else break
        }

        def v = toLeft * toRight * upwards * downwards

        return v
    }


    def todata( List textdata ) {
        int rows = textdata.size()
        int cols = textdata[0].size()
        int[][] data = new int[rows][cols]
        textdata.eachWithIndex{ String s, int r ->
            data[r] = s.collect {it as int }
        }
        return data
    }

    def "day 8 a"() {
        given:
            def textdata = this.getClass()
                    .getResource( "data-08.txt" )
                    .readLines()
            def data = todata(textdata)

            //data = todata(["30373","25512","65332","33549","35390"])

        when:
            def part1 = 0
            def part2 = 0
            data.eachWithIndex { heights, row ->
                heights.eachWithIndex{ int h, int col ->
                    if( isVisible(row, col, data ))
                        part1++
                    def ss = countSeenTrees( row, col, data )
                    if( ss > part2 ) part2 = ss
                }
            }
            println("part1 is $part1")
            println("part2 is $part2")

        then:
            part1
    }


}
