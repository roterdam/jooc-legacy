main: func {

	version(linux) {
		"Hello, Linux =)" println()
	}
	version(apple) {
		"Hello, Mac =)" println()
	}
	version(windows) {
		"Hello, Windows =)" println()
	}
	version(!linux, !apple, !windows) {
		"Hi, stranger ;)" println()
	}

}
