import spock.lang.Specification

class aoc2022_07_Test extends Specification {
    def cdcmd = ~/^\$ cd (\S+)$/
    def lscmd = ~/^\$ ls$/
    def dirlisting = ~/^dir (\S+)$/
    def filelisting = ~/^(\d+) (\S+)$/

    def DISKSIZE = 70_000_000
    def NEEDEDSPACE = 30_000_000


    class Fil {
        String name
        long size

        Fil( String name, int size ) {
            this.name = name
            this.size = size
        }
    }

    class Dir {
        String name
        Dir parent
        Set<Dir> subdirs = []
        Set<Fil> files = []
        private long cachedRecursiveSize = -1

        Dir( String name, Dir parent ) {
            this.name = name
            this.parent = parent
        }

        long getLocalSize() {
            files.sum { it.size } ?: 0
        }

        long getRecursiveSize() {
            if( cachedRecursiveSize > -1 ) return cachedRecursiveSize
            long fs = files.sum { it.size } ?: 0
            long ds = subdirs.sum{ it.recursiveSize } ?: 0
            cachedRecursiveSize = fs + ds
            return cachedRecursiveSize
        }

        String toString() {
            "$name ($localSize) [$recursiveSize]"
        }
    }

    Dir parseData( def rows ) {
        def shouldlog = false

        Dir root = new Dir( "/", null )

        Dir currentDir = root
        rows.eachWithIndex { row, index ->
            def cdmatch = cdcmd.matcher(row)
            def lsmatch = lscmd.matcher(row)
            def dirmatch = dirlisting.matcher(row)
            def filematch = filelisting.matcher(row)

            if( cdmatch.matches() ) {
                def name = cdmatch[0][1]
                if( shouldlog ) println("CD $name")
                if( name == ".." ) {
                    if( shouldlog ) println( "CHANGE CURRENT DIR TO PARENT (${currentDir.parent.name})")
                    currentDir = currentDir.parent
                }
                else if( name == "/" ) {
                    currentDir = root
                    if( shouldlog ) println( "CHANGE CURRENT DIR TO ROOT")
                }
                else {
                    def newDir = currentDir.subdirs.find { it.name == name }
                    if( newDir == null )
                        throw new Exception("unexpected cd to $name on line $index")
                    currentDir = newDir
                }
            }
            else if( lsmatch.matches() ) {
                if( shouldlog ) println("LS in ${currentDir.name}")
            }
            else if( dirmatch.matches() ) {
                def name = dirmatch[0][1]
                if( shouldlog ) println(" DIR $name")
                currentDir.subdirs << new Dir( name, currentDir )
            }
            else if( filematch.matches() ) {
                def size = Integer.parseInt( filematch[0][1] )
                def name = filematch[0][2]
                if( shouldlog ) println(" FILE $name $size")
                currentDir.files << new Fil( name, size )
            }
        }

        return root
    }

    def findSmallSubdirs( Dir start, long upperLimit, List<Dir> smallDirs ) {
        // println("find dirs of size max $upperLimit in ${start.name}")
        start.subdirs.each { d ->
            def x = d.recursiveSize
            if( x <= upperLimit ) {
                //println("${d.name} is small")
                smallDirs << d
            }

            findSmallSubdirs( d, upperLimit, smallDirs )
        }
    }

    def flatDirList( Dir start, List<Dir> dirs ) {
        start.subdirs.each { d ->
            dirs << d
            flatDirList( d, dirs )
        }
    }

    def "day 7 a"() {
        given:
            def data = this.getClass().getResource( "data-07.txt" ).text.split('\n')

        when:
            def tree = parseData( data )
            def smallDirs = [] as List<Dir>
            findSmallSubdirs( tree, 100000, smallDirs )
            def rootSize = tree.recursiveSize

        then:
            '$ cd /'.matches( cdcmd )
            '$ cd abc'.matches( cdcmd )
            '$ ls'.matches( lscmd )
            'dir /'.matches( dirlisting )
            'dir xyz'.matches( dirlisting )
            '1234 nisse'.matches( filelisting )

            tree

            println( "day 7 part 1: ${smallDirs.sum { it.recursiveSize}}" )
            println( "root dir size: $rootSize")
    }


    def "day 7 b"() {
        given:
            def data = this.getClass().getResource( "data-07.txt" ).text.split('\n')

        when:
            def tree = parseData( data )
            def rootSize = tree.recursiveSize
            def free = DISKSIZE - rootSize
            def needed = NEEDEDSPACE - free
            def dirs = [] as List<Dir>
            flatDirList( tree, dirs )
            dirs.sort { d -> d.recursiveSize }
            def deleteme = dirs.find { it.recursiveSize > needed }
            //println(dirs.join("\n"))

        then:
            println( "day 7 part 2: delete dir: $deleteme" )
            println( "root dir size: $rootSize")
            println( "space required: $needed")
            1 == 1
    }


}
