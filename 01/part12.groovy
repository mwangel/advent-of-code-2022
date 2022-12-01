def data = ("data" as File).text.split('\n') // list of strings

List elfLoads = [] // list of integers
def load = 0

data.each { s ->
  if( !s ) {
    elfLoads << cv
    load = 0
  }
  else {
    load += Integer.parseInt(s)
  }
}

println( "part 1: ${elfWeights.max()}" )

f = elfLoads.sort().reverse()
println( "part 2: ${f[0] + f[1] + f[2]}" )
