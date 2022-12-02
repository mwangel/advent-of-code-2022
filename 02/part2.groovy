def data = ("data.txt" as File).text.split('\n').collect{ s ->
  new Tuple( s[0], strategyPick(s[0], s[2]) )
}

// pick a play based on the opponent play and the "strategy"
def strategyPick( elfPlay, strategy ) {
  def win =  ['A':'Y', 'B':'Z', 'C':'X']
  def lose = ['A':'Z', 'B':'X', 'C':'Y']
  def draw = ['A':'X', 'B':'Y', 'C':'Z']

  if( strategy == 'X' ) return lose[ elfPlay ]
  if( strategy == 'Y' ) return draw[ elfPlay ]
  if( strategy == 'Z' ) return win[ elfPlay ]
}

// score a round
def v( Tuple t ) {
  def s = t.toString()
  def myValue = ['X':1, 'Y':2, 'Z':3]
  def cv = myValue[ t[1] ]
  switch( s ) {
    case "[A, Y]": return cv+6; // rock paper
    case "[B, Z]": return cv+6; // paper scissors
    case "[C, X]": return cv+6; // scissors rock
    case "[A, X]": return cv+3; // rock rock
    case "[B, Y]": return cv+3; // paper paper
    case "[C, Z]": return cv+3; // scissors scissors
    default: return cv; // loss
  }
}

// unit tests
assert v( new Tuple('A', 'Y') ) == 2+6
assert v( new Tuple('B', 'Y') ) == 3+2
assert v( new Tuple('C', 'Z') ) == 3+3
assert strategyPick('A', 'Y') == 'X'
assert strategyPick('B', 'Y') == 'Y'
assert strategyPick('C', 'Y') == 'Z'

// calculation
def score = data.sum{ v(it) }
println( "part 2: ${score}" )
