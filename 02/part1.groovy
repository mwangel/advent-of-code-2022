def data = ("data.txt" as File).text.split('\n').collect{ s -> new Tuple(s[0], s[2]) }

def v( Tuple t ) {
  def s = t.toString()
  def myValue = ['X':1, 'Y':2, 'Z':3]
  def cv = myValue[ t[1] ]

  //println "$s; $c; $cv"
  switch( s ) {
    case "[A, Y]": return cv+6; // rock paper
    case "[B, Z]": return cv+6; // paper scissors
    case "[C, X]": return cv+6; // scissors rock
    case "[A, X]": return cv+3;
    case "[B, Y]": return cv+3;
    case "[C, Z]": return cv+3;
    default: return cv;
  }
}

def score = data.sum( d -> v(d) )
println( "part 1: ${score}" )
